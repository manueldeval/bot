package org.deman.bot.engine;

import java.util.Optional;
import java.util.Stack;

/**
 * Created by deman on 05/02/17.
 */
public class RequestResponseStack {

    private Stack<String> stack = new Stack<>();

    public Optional<String> getAt(int index) {
        return index < stack.size() ? Optional.ofNullable(stack.get(stack.size() - index - 1)) : Optional.empty();
    }

    public Optional<String> current() {
        return getAt(0);
    }

    public void push(String value, int maxHistory) {
        if (stack.size() > maxHistory) {
            stack.remove(0);
        }
        stack.push(value);
    }
}
