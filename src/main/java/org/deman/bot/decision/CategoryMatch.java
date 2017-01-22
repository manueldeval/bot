package org.deman.bot.decision;

import org.deman.bot.rules.Category;

import java.util.LinkedList;

/**
 * Created by deman on 22/01/17.
 */
public class CategoryMatch {

    private Category category;

    private LinkedList<Match> matches = new LinkedList<>();

    public CategoryMatch(Category category,Match match) {
        this.category = category;
        this.matches.add(match);
    }


    public CategoryMatch(Category category) {
        this.category = category;
    }


    public Category getCategory() {
        return category;
    }

    public LinkedList<Match> getMatches() {
        return matches;
    }

    public CategoryMatch pushMatch(Match match){
        this.matches.addFirst(match);
        return this;
    }
}
