package org.deman.bot.decision;

import org.deman.bot.rules.Category;

import java.util.Optional;

/**
 * Created by deman on 15/01/17.
 */
public class OneOrMoreDecisionTreeNode  extends DecisionTreeNode {

    public Optional<Category> match(Token tokens){
        if (getCategory() != null && tokens != null) {
            return Optional.of(getCategory());
        } else if (tokens == null){
            return Optional.empty();
        } else {
            Token next = tokens.getNext().orElse(null);
            Optional<Category> categoryOptional = matchChildrenNodes(next);
            if (categoryOptional.isPresent()){
                return categoryOptional;
            } else {
                return match(next);
            }
        }
    }

    @Override
    public String toString() {
        return "1+";
    }
}
