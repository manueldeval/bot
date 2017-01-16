package org.deman.bot.engine;


/**
 * Created by deman on 11/01/17.
 */
public class Context {
    private Engine engine;
    private State state = new State();

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Engine getEngine() {
        return engine;
    }

    public State getState() {
        return state;
    }
}
