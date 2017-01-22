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

    public Optional<Category> match(Token tokens) {
        Optional<Category> result;
        // Si le token existe et qu'il match...
        if (isTokenMatching(tokens)) {
            if (getCategory() != null) {
                result = Optional.of(getCategory());
            } else {
                result =matchNext(tokens);
            }
        } else {
            result = Optional.empty();
        }
        if (result.isPresent()){
            logger.debug(this.toString()+" matches: "+ (tokens==null?"null":tokens.getValue()));
        }
        return result;
    }

    private Optional<Category> matchNext(Token tokens) {
        Token next = tokens.getNext().orElse(null);
        // Sinon il faut aller voir en profondeur.
        if (isHighOrder) {
            Optional<Category> selfMatch = match(next);
            if (selfMatch.isPresent()) {
                return selfMatch;
            } else {
                return matchChildrenNodes(next);
            }
        } else {
            Optional<Category> childrenMatch = matchChildrenNodes(next);
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
        return (isHighOrder?"H":"L")+"1+";
    }

}
