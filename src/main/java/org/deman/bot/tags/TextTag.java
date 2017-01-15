package org.deman.bot.tags;

import org.deman.bot.engine.Context;

import java.util.*;

/**
 * Created by deman on 15/01/17.
 */
public class TextTag implements Tag {

    private String text;

    public TextTag(String text) {
        this.text = text;
    }

    @Override
    public String getName() {
        return "text";
    }

    @Override
    public Map<String, String> getAttributes() {
        return new HashMap<>();
    }

    @Override
    public List<Tag> getChildrens() {
        return Collections.emptyList();
    }

    @Override
    public Optional<String> generate(Context context) {
        return Optional.ofNullable(text);
    }
}
