package org.deman.bot.rest;

import org.deman.bot.engine.State;

/**
 * Created by deman on 05/02/17.
 */
public class ChatRequest {

    private State state;

    private String input;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
