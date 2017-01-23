package org.deman.bot.decision;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by deman on 15/01/17.
 */
public class ValueDecisionTreeNode extends DecisionTreeNode {

    private static final Logger logger = LoggerFactory.getLogger(ValueDecisionTreeNode.class);

    protected String value;

    public ValueDecisionTreeNode(String value) {
        this.value = value;
    }

    public Optional<CategoryMatch> match(Token tokens) {
        Optional<CategoryMatch> result;
        // Si le token existe et qu'il match...
        if (isTokenMatching(tokens)) {
            if (getCategory() != null) { // Si on a une categorie, c'est un match
                result = Optional.of(new CategoryMatch(getCategory()));
            } else {
                // Sinon il faut aller voir en profondeur.
                Token next = tokens.getNext();
                result = matchChildrenNodes(next);
            }
        } else {
            // Si le token est vide ou qu'il ne match pas... pas de match
            result = Optional.empty();
        }

        return result.map(categoryMatch -> categoryMatch.pushMatch(Match.value(tokens.getValue())));
    }

    private boolean isTokenMatching(Token tokens) {
        return tokens != null && value.equalsIgnoreCase(tokens.getValue());
    }

    @Override
    public String toString() {
        return value+(category==null?"":"@");
    }
}
