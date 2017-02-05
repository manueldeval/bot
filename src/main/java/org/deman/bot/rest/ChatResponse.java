package org.deman.bot.rest;

import org.deman.bot.engine.State;

/**
 * Created by deman on 05/02/17.
 */
public class ChatResponse {

    private State state;

    private String output;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ChatResponse(State state, String output) {
        this.state = state;
        this.output = output;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
