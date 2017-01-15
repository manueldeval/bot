package org.deman.bot.engine;

import org.deman.bot.aiml.AimlParser;
import org.deman.bot.aiml.AimlParserException;
import org.deman.bot.aiml.TemplateCompiler;
import org.deman.bot.decision.DecisionTreeNode;
import org.deman.bot.decision.RootDecisionTreeNode;
import org.deman.bot.engine.Context;
import org.deman.bot.rules.Category;
import org.deman.bot.tags.TagsRegistry;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by deman on 11/01/17.
 */
public class Engine {

    private List<Category> categories = new ArrayList<>();
    private RootDecisionTreeNode decisionTree = new RootDecisionTreeNode();

    public void loadAimlFile(String filename) throws AimlParserException {
        TagsRegistry tagsRegistry = new TagsRegistry();
        // Load
        List<Category> newCategories = AimlParser.parse(new File(filename),tagsRegistry);
        // Check
        for (Category cat : newCategories) {
            TemplateCompiler.compile(cat, tagsRegistry); // Not mandatory... just to see warnings at statup
            decisionTree.addCategory(cat);
        }
        // AddAll
        categories.addAll(newCategories);

        System.out.println(decisionTree.toJson());
    }

    public Optional<String> evaluate(Context context, String value) {
        return Optional.empty();
    }
}

