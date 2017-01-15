package org.deman.bot.decision;

import org.deman.bot.rules.Category;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Optional;

/**
 * Created by deman on 15/01/17.
 */
public class ListDecisionTreeNode extends DecisionTreeNode {

    public String nameOfSet;

    public String getNameOfSet() {
        return nameOfSet;
    }

    public ListDecisionTreeNode(String nameOfSet) {
        this.nameOfSet = nameOfSet;
    }

    @Override
    public Optional<Category> match(Token token) {
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        return "<SET>"+nameOfSet+"</SET>";
    }

}
