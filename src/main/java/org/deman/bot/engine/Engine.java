package org.deman.bot.engine;

import org.deman.bot.aiml.AimlParser;
import org.deman.bot.aiml.AimlParserException;
import org.deman.bot.aiml.TemplateCompiler;
import org.deman.bot.decision.CategoryMatch;
import org.deman.bot.decision.RootDecisionTreeNode;
import org.deman.bot.rules.Category;
import org.deman.bot.tags.Tag;
import org.deman.bot.tags.TagsRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static org.deman.bot.tags.TagsDefinition.NOP_INSTANCE;

/**
 * Created by deman on 11/01/17.
 */
public class Engine {

    private static final Logger logger = LoggerFactory.getLogger(Engine.class);
    private RootDecisionTreeNode decisionTree = new RootDecisionTreeNode();
    private TagsRegistry tagsRegistry = new TagsRegistry();
    private int maxHistory = 50;

    public void unloadAiml(){
        decisionTree = new RootDecisionTreeNode();
    }

    public void loadAimlFilesFromPath(String path) throws AimlParserException {
        Path baseDir = Paths.get(path);
        try(Stream<Path> pathStream = Files.list(baseDir)){
            pathStream.map(Path::toString).forEach((filename) -> {
                try {
                    logger.info("Loading : "+filename);
                    loadAimlFile(filename);
                } catch (AimlParserException e) {
                    logger.error(e.getMessage(),e);
                }
            });
        } catch (IOException e) {
            throw new AimlParserException(e);
        }
    }


    public void loadAimlFile(String filename) throws AimlParserException {
        try {
            loadAimlStream(new FileInputStream(new File(filename)));
        } catch (FileNotFoundException e) {
            new AimlParserException(e);
        }
    }

    public void loadAimlStream(InputStream stream) throws AimlParserException {
        TagsRegistry tagsRegistry = new TagsRegistry();
        // Load
        List<Category> newCategories = AimlParser.parse(stream, tagsRegistry);
        // Check
        for (Category cat : newCategories) {
            TemplateCompiler.compile(cat, tagsRegistry); // Not mandatory... just to see warnings at statup
            decisionTree.addCategory(cat);
        }
        // AddAll
        //logger.debug(decisionTree.toJson());
    }

    public Optional<String> onNewUserInput(State state, String userInput) {
        return Stream.of(userInput.replace(",", " ").split("[\\.\\?!]"))
                .filter(sentence -> !"".equals(sentence))
                .map(sentence -> onNewSentence(state, sentence))
                .reduce(Optional.empty(), this::mergeSentences);
    }

    protected Optional<String> mergeSentences(Optional<String> o1, Optional<String> o2) {
        if (!o1.isPresent() && !o2.isPresent()) {
            return Optional.empty();
        } else if (o1.isPresent() && !o2.isPresent()) {
            return o1;
        } else if (!o1.isPresent() && o2.isPresent()) {
            return o2;
        } else {
            return Optional.of(o1.get() + " " + o2.get());
        }
    }

    public Optional<String> onNewSentence(State state, String sentence) {
        // Setup context...
        Context context = new Context(this,state);

        context.getState().getRequestStack().push(sentence, maxHistory);

        // And go evaluation!
        Optional<String> optResult = evaluate(context, sentence);

        context.getState().getResponseStack().push(optResult.orElse("*"), maxHistory);
        return optResult;
    }

    public Optional<String> evaluate(Context context, String sentence) {

        sentence = sentence + " <that> " +
                context.getState().getResponseStack().current().orElse("*") +
                " <topic> " +
                context.getState().getVars().getOrDefault("topic","*");
        System.out.println(">>>>>>>>"+sentence);
        Optional<CategoryMatch> optCategoryMatch = decisionTree.match(sentence);
        if (logger.isDebugEnabled()) {
            logger.debug("_____________________________________________");
            logger.debug(sentence + "\n" + optCategoryMatch.toString());
        }
        return optCategoryMatch.map(catMatch -> generateResult(context, catMatch))
                .orElse(Optional.empty());
    }

    private Optional<String> generateResult(Context context, CategoryMatch catMatch) {
        Category category = catMatch.getCategory();

        context.getState().getPatternStars().pushStars(catMatch.getPatternStar());
        context.getState().getThatStars().pushStars(catMatch.getThatStar());
        context.getState().getTopicStars().pushStars(catMatch.getTopicStar());

        Tag tag = NOP_INSTANCE;
        try {
            tag = TemplateCompiler.compile(category, tagsRegistry);
        } catch (AimlParserException e) {
            logger.error("Error during the compilation of " + category, e);
        }
        Optional<String> result = tag.generate(context);

        context.getState().getPatternStars().popStars();
        context.getState().getTopicStars().popStars();
        context.getState().getThatStars().popStars();
        return result;
    }

}

