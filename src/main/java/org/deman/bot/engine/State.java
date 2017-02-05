package org.deman.bot.engine;

import java.util.*;

/**
 * Created by deman on 11/01/17.
 */
public class State {

    private String userId = "Lord";
    private Map<String, String> vars = new HashMap<>();
    private StarStack patternStars =  new StarStack();
    private StarStack topicStars =  new StarStack();
    private StarStack thatStars =  new StarStack();
    private RequestResponseStack requestStack = new RequestResponseStack();
    private RequestResponseStack responseStack = new RequestResponseStack();

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public StarStack getPatternStars() {
        return patternStars;
    }

    public StarStack getTopicStars() {
        return topicStars;
    }

    public StarStack getThatStars() {
        return thatStars;
    }

    public Map<String, String> getVars() {
        return vars;
    }

    public RequestResponseStack getRequestStack() {
        return requestStack;
    }

    public RequestResponseStack getResponseStack() {
        return responseStack;
    }
}
