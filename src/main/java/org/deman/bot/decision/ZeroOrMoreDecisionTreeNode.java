package org.deman.bot.decision;

import org.deman.bot.rules.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by deman on 15/01/17.
 */
public class ZeroOrMoreDecisionTreeNode extends DecisionTreeNode {

    private final static Logger logger = LoggerFactory.getLogger(ZeroOrMoreDecisionTreeNode.class);

    private boolean isHighOrder;

    public ZeroOrMoreDecisionTreeNode(boolean isHighOrder) {
        this.isHighOrder = isHighOrder;
    }

    public Optional<Category> match(Token tokens) {
        Optional<Category> result;
        if (getCategory() != null) {
            result = Optional.of(getCategory());
        } else {
            result = matchNext(tokens);
        }

        if (result.isPresent()) {
            logger.debug(this.toString() + " matches: " + (tokens == null ? "null" : tokens.getValue()));
        }
        return result;
    }

    private Optional<Category> selfMatchOnNext(Token tokens) {
        if (tokens == null || !isTokenAllowed(tokens)) {
            return Optional.empty();
        }
        Token next = tokens.getNext().orElse(null);
        return match(next);
    }

    private Optional<Category> matchChildrenOnNext(Token tokens) {
        Token next = tokens.getNext().orElse(null);
        return matchChildrenNodes(next);
    }

    private Optional<Category> matchChildrenOnCurrent(Token tokens) {
        return matchChildrenNodes(tokens);
    }

    private List<Function<Token, Optional<Category>>> highOrderSearch = Arrays.asList(
            this::selfMatchOnNext,
            this::matchChildrenOnCurrent,
            this::matchChildrenOnNext
    );

    private List<Function<Token, Optional<Category>>> lowOrderSearch = Arrays.asList(
            this::matchChildrenOnCurrent,
            this::matchChildrenOnNext,
            this::selfMatchOnNext
    );

    private List<Function<Token, Optional<Category>>> getSearchFunctions() {
        return isHighOrder ? highOrderSearch : lowOrderSearch;
    }

    private Optional<Category> matchNext(Token tokens) {
        return getSearchFunctions().stream()
                .map(f -> f.apply(tokens))
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.empty());
    }

    /* Un wildcard n'a pas le droit de matcher that ou topic! */
    private boolean isTokenAllowed(Token tokens) {
        return !tokens.getValue().equalsIgnoreCase("<that>") &&
                !tokens.getValue().equalsIgnoreCase("<topic>");
    }

    @Override
    public String toString() {
        return (isHighOrder ? "H" : "L") + "0+";
    }


}
