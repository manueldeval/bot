package org.deman.bot.decision;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public Optional<CategoryMatch> match(Token tokens) {
        Optional<CategoryMatch> result;
        if (getCategory() != null) {
            result = Optional.of(new CategoryMatch(getCategory()));
        } else {
            result = matchNext(tokens);
        }
        return result;
    }

    private Optional<CategoryMatch> selfMatchOnNext(Token tokens) {
        if (tokens == null || !isTokenAllowed(tokens)) {
            return Optional.empty();
        }
        Token next = tokens.getNext();
        return match(next).map(cMatch -> cMatch.pushMatch(Match.wildcard(tokens.getValue())));
    }

    private Optional<CategoryMatch> matchChildrenOnNext(Token tokens) {
        Token next = tokens.getNext();
        return matchChildrenNodes(next).map(cMatch -> cMatch.pushMatch(Match.wildcard(tokens.getValue())));
    }

    private Optional<CategoryMatch> matchChildrenOnCurrent(Token tokens) {
        return matchChildrenNodes(tokens).map(cMatch -> cMatch.pushMatch(Match.wildcard("")));
    }

    private List<Function<Token, Optional<CategoryMatch>>> highOrderSearch = Arrays.asList(
            this::selfMatchOnNext,
            this::matchChildrenOnCurrent,
            this::matchChildrenOnNext
    );

    private List<Function<Token, Optional<CategoryMatch>>> lowOrderSearch = Arrays.asList(
            this::matchChildrenOnCurrent,
            this::matchChildrenOnNext,
            this::selfMatchOnNext
    );

    private List<Function<Token, Optional<CategoryMatch>>> getSearchFunctions() {
        return isHighOrder ? highOrderSearch : lowOrderSearch;
    }

    private Optional<CategoryMatch> matchNext(Token tokens) {
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
        return (isHighOrder ? "H" : "L") + "0+"+(category==null?"":"@");
    }


}
