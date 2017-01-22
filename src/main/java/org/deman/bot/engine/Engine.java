package org.deman.bot.engine;

import org.deman.bot.aiml.AimlParser;
import org.deman.bot.aiml.AimlParserException;
import org.deman.bot.aiml.TemplateCompiler;
import org.deman.bot.decision.CategoryMatch;
import org.deman.bot.decision.DecisionTreeNode;
import org.deman.bot.decision.Match;
import org.deman.bot.decision.RootDecisionTreeNode;
import org.deman.bot.engine.Context;
import org.deman.bot.rules.Category;
import org.deman.bot.tags.Tag;
import org.deman.bot.tags.TagsRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.deman.bot.tags.TagsDefinition.NOP_INSTANCE;

/**
 * Created by deman on 11/01/17.
 */
public class Engine {

    private static final Logger logger = LoggerFactory.getLogger(Engine.class);
    private List<Category> categories = new ArrayList<>();
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
        categories.addAll(newCategories);
        //logger.debug(decisionTree.toJson());
    }

    public Optional<String> onNewUserInput(Context context, String userInput) {
        return Stream.of(userInput.replace(","," ").split("[\\.\\?!]"))
                .filter(sentence -> !"".equals(sentence))
                .map(sentence -> onNewSentence(context, sentence))
                .reduce(Optional.empty(),this::mergeSentences);
    }

    protected Optional<String> mergeSentences(Optional<String> o1, Optional<String> o2) {
        if (!o1.isPresent() && !o2.isPresent()){
            return Optional.empty();
        } else if (o1.isPresent() && !o2.isPresent()){
            return o1;
        } else if (!o1.isPresent() && o2.isPresent()){
            return o2;
        } else {
            return Optional.of(o1.get()+" "+o2.get());
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
        Optional<CategoryMatch> categoryMatch = decisionTree.match(sentence);
        if (logger.isDebugEnabled()) {
            logger.debug(sentence + " ==> " + categoryMatch
                    .map(CategoryMatch::getCategory)
                    .map(Category::toString)
                    .orElse("no category found."));
            logger.debug(sentence + " ==> " + categoryMatch
                    .map(CategoryMatch::getMatches)
                    .orElse(new LinkedList<>())
                    .stream()
                    .map(Match::toString)
                    .collect(Collectors.joining(",")));

        }
        return categoryMatch
                .map(CategoryMatch::getCategory)
                .map(compile())
                .flatMap(tag -> tag.generate(context));
    }

    private Function<Category, Tag> compile() {
        return cat -> {
            try {
                return TemplateCompiler.compile(cat, tagsRegistry);
            } catch (AimlParserException e) {
                logger.error(e.getMessage(), e);
                return NOP_INSTANCE;
            }
        };
    }
}

