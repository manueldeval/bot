package org.deman.bot.decision;

import org.deman.bot.rules.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by deman on 15/01/17.
 */
public class RootDecisionTreeNode extends DecisionTreeNode {

    private static final Logger logger = LoggerFactory.getLogger(DecisionTreeNode.class);

    public Optional<Category> match(String s){
        return match(tokenize(s).orElse(null));
    }

    @Override
    public Optional<Category> match(Token token) {
        logger.debug("Search category for: "+token);
        return matchChildrenNodes(token);
    }

    public static final String THAT = " <that> ";
    public static final String TOPIC = " <topic> ";

    public void addCategory(Category category) {
        String decisionLine = category.getPattern() + THAT + category.getThat() + TOPIC + category.getTopic();
        tokenize(decisionLine).ifPresent(token -> this.createChildrenNode(token, category));
    }

    public Optional<Token> tokenize(String s) {
        List<String> reverseSplitted = Arrays.asList(s.split("\\s+"));
        Collections.reverse(reverseSplitted);
        return reverseSplitted.stream()
                .filter(word -> !"".equals(word)) // Trim
                .map(Token::new).reduce((t1, t2) -> {
                    t2.setNext(t1);
                    return t2;
                });
    }
    @Override
    public String toString() {
        return "[ROOT]";
    }
}
