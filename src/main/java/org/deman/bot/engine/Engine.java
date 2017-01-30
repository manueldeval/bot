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

import java.io.File;
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

    public void loadAimlFile(String filename) throws AimlParserException {
        TagsRegistry tagsRegistry = new TagsRegistry();
        // Load
        List<Category> newCategories = AimlParser.parse(new File(filename), tagsRegistry);
        // Check
        for (Category cat : newCategories) {
            TemplateCompiler.compile(cat, tagsRegistry); // Not mandatory... just to see warnings at statup
            decisionTree.addCategory(cat);
        }
        // AddAll
        //logger.debug(decisionTree.toJson());
    }

    public Optional<String> onNewUserInput(Context context, String userInput) {
        return Stream.of(userInput.replace(",", " ").split("[\\.\\?!]"))
                .filter(sentence -> !"".equals(sentence))
                .map(sentence -> onNewSentence(context, sentence))
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

    public Optional<String> onNewSentence(Context context, String sentence) {
        // Setup context...
        context.setEngine(this);

        // And go evaluation!
        return evaluate(context, sentence);
    }

    public Optional<String> evaluate(Context context, String sentence) {
        sentence = sentence + " <that> * <topic> * ";
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

