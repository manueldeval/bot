package org.deman.bot.decision;

import org.deman.bot.rules.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by deman on 15/01/17.
 */
public class OneOrMoreDecisionTreeNode  extends DecisionTreeNode {

    private final static Logger logger = LoggerFactory.getLogger(OneOrMoreDecisionTreeNode.class);

    private boolean isHighOrder;

    public OneOrMoreDecisionTreeNode(boolean isHighOrder){
        this.isHighOrder = isHighOrder;
    }

    public Optional<CategoryMatch> match(Token tokens) {
        Optional<CategoryMatch> result;
        // Si le token existe et qu'il match...
        if (isTokenMatching(tokens)) {
            if (getCategory() != null) {
                result = Optional.of(new CategoryMatch(getCategory()));
            } else {
                result =matchNext(tokens);
            }
        } else {
            result = Optional.empty();
        }
        return result.map(categoryMatch -> categoryMatch.pushMatch(Match.wildcard(tokens.getValue())));
    }

    private Optional<CategoryMatch> matchNext(Token tokens) {
        Token next = tokens.getNext();
        // Sinon il faut aller voir en profondeur.
        if (isHighOrder) {
            Optional<CategoryMatch> selfMatch = match(next);
            if (selfMatch.isPresent()) {
                return selfMatch;
            } else {
                return matchChildrenNodes(next);
            }
        } else {
            Optional<CategoryMatch> childrenMatch = matchChildrenNodes(next);
            if (childrenMatch.isPresent()) {
                return childrenMatch;
            } else {
                return match(next);
            }
        }
    }

    /* Un wildcard n'a pas le droit de matcher that ou topic! */
    private boolean isTokenMatching(Token tokens) {
        return tokens != null &&
                !tokens.getValue().equalsIgnoreCase("<that>") &&
                !tokens.getValue().equalsIgnoreCase("<topic>");
    }

    @Override
    public String toString() {
        return (isHighOrder?"H":"L")+"1+"+(category==null?"":"@");
    }

}
