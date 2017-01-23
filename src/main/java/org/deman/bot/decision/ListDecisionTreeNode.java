package org.deman.bot.decision;


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
    public Optional<CategoryMatch> match(Token token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "<SET_BUILDER>"+nameOfSet+"</SET_BUILDER>";
    }

}
