package org.deman.bot.decision;

import org.deman.bot.rules.Category;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by deman on 15/01/17.
 */
public class ValueDecisionTreeNode extends DecisionTreeNode {

    protected String value;

    public ValueDecisionTreeNode(String value) {
        this.value = value;
    }

    public Optional<Category> match(Token tokens) {
        if (tokens != null && value.equalsIgnoreCase(tokens.getValue())) {
            if (getCategory() != null) {
                return Optional.of(getCategory());
            } else {
                Token next = tokens.getNext().orElse(null);
                return matchChildrenNodes(next);
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
