package org.deman.bot.engine;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

/**
 * Created by deman on 30/01/17.
 */
public class StarStack {

    @JsonProperty
    private Stack<List<String>> stack = new Stack<>();

    public Optional<String> getStarAt(int index) {
        if (index <= currentStars().size() && index > 0) {
            return Optional.of(currentStars().get(index - 1));
        } else {
            return Optional.empty();
        }
    }

    private List<String> currentStars(){
        return stack.peek();
    }

    public void pushStars(List<String> stars){
        stack.push(stars);
    }

    public void popStars(){
        stack.pop();
    }
}
