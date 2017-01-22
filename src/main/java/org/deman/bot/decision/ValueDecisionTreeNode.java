package org.deman.bot.decision;

import org.deman.bot.rules.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by deman on 15/01/17.
 */
public class ValueDecisionTreeNode extends DecisionTreeNode {

    private static final Logger logger = LoggerFactory.getLogger(ValueDecisionTreeNode.class);

    protected String value;

    public ValueDecisionTreeNode(String value) {
        this.value = value;
    }

    public Optional<Category> match(Token tokens) {
        Optional<Category> result;

        // Si le token existe et qu'il match...
        if (isTokenMatching(tokens)) {
            if (getCategory() != null) { // Si on a une categorie, c'est un match
                result = Optional.of(getCategory());
            } else {
                // Sinon il faut aller voir en profondeur.
                Token next = tokens.getNext();
                result = matchChildrenNodes(next);
            }
        } else {
            // Si le token est vide ou qu'il ne match pas... pas de match
            result = Optional.empty();
        }

        if (result.isPresent()){
            logger.debug("Exact match: "+ (tokens==null?"null":tokens.getValue()));
        }
        return result;
    }

    private boolean isTokenMatching(Token tokens) {
        return tokens != null && value.equalsIgnoreCase(tokens.getValue());
    }

    @Override
    public String toString() {
        return value;
    }
}
