package org.deman.bot.decision;

/**
 * Created by deman on 22/01/17.
 */
public class Match {

    private MatchType type;

    private String value;

    public Match(MatchType type, String value) {
        this.type = type;
        this.value = value;
    }

    public static Match value(String value){
        return new Match(MatchType.VALUE,value);
    }

    public static Match wildcard(String value){
        return new Match(MatchType.WILDCARD,value);
    }

    public MatchType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value.trim();
    }

    @Override
    public String toString() {
        return type.name()+"("+ value + ")";
    }
}
