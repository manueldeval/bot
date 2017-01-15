package org.deman.bot.decision;

import java.util.Optional;

/**
 * Created by deman on 13/01/17.
 */
public class Token {

    private String value;

    private Token next = null;

    public Token(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Optional<Token> getNext() {
        return Optional.ofNullable(next);
    }

    public void setNext(Token next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return value + getNext().map(t -> " | " + t).orElse("");
    }
}
