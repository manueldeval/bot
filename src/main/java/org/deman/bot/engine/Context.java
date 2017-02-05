package org.deman.bot.engine;


/**
 * Created by deman on 11/01/17.
 */
public class Context {
    private Engine engine;
    private State state;

    public Context(Engine engine, State state) {
        this.engine = engine;
        this.state = state;
    }

    public Engine getEngine() {
        return engine;
    }

    public State getState() {
        return state;
    }
}
