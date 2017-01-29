package org.deman.bot.engine;

import java.util.*;

/**
 * Created by deman on 11/01/17.
 */
public class State {

    private Map<String, String> vars = new HashMap<>();

    private Stack<List<String>> patternStars = new Stack<>();

    public Map<String, String> getVars() {
        return vars;
    }

    public Optional<String> getPatternStar(int index) {
        if (index <= currentPatternStars().size() && index > 0) {
            return Optional.of(currentPatternStars().get(index - 1));
        } else {
            return Optional.empty();
        }
    }

    private List<String> currentPatternStars(){
        return patternStars.peek();
    }

    public void pushPatternStars(List<String> stars){
        patternStars.push(stars);
    }

    public void popPatternStars(){
        patternStars.pop();
    }
}
