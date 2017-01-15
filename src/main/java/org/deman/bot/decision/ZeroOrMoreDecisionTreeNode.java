package org.deman.bot.decision;

import org.deman.bot.rules.Category;

import java.util.Optional;

/**
 * Created by deman on 15/01/17.
 */
public class ZeroOrMoreDecisionTreeNode extends DecisionTreeNode {

    public Optional<Category> match(Token tokens){
        if (getCategory() != null) { // Quoi qu'il arrive si la cat est non vide c'est un match
            return Optional.of(getCategory());
        } else if (tokens == null){
            return Optional.empty();
        } else {
            Optional<Category> categoryOptional = matchChildrenNodes(tokens);
            if (categoryOptional.isPresent()){
                return categoryOptional;
            } else {
                Token next = tokens.getNext().orElse(null);
                return match(next);
            }
        }
    }

    @Override
    public String toString() {
        return "0+";
    }
}
