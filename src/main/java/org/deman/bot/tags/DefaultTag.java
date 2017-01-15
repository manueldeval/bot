package org.deman.bot.tags;

import org.deman.bot.engine.Context;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Created by deman on 11/01/17.
 */
public abstract class DefaultTag implements Tag {

    protected String name;
    protected Map<String,String> attributes = new HashMap<>();
    protected List<Tag> childrens = new ArrayList<>();

    public DefaultTag(String name, Map<String, String> attributes, List<Tag> childrens) {
        this.name = name;
        this.attributes = attributes;
        this.childrens = childrens;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public List<Tag> getChildrens() {
        return childrens;
    }

    @Override
    public abstract Optional<String> generate(Context context);
}
