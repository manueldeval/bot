package org.deman.bot.tags;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.deman.bot.tags.TagsDefinition.*;

/**
 * Created by deman on 11/01/17.
 */
public class TagsRegistry {

    private Map<String, TagBuilder> tagBuilders = new HashMap<>();

    public TagsRegistry() {
        this.tagBuilders.put("think", THINK);
        this.tagBuilders.put("nop", NOP);
        this.tagBuilders.put("srai", SRAI);
        this.tagBuilders.put("li", LI);
        this.tagBuilders.put("random", RANDOM);
        this.tagBuilders.put("uppercase", UPPERCASE);
        this.tagBuilders.put("lowercase", LOWERCASE);
        this.tagBuilders.put("formal", FORMAL);
        this.tagBuilders.put("set", SET);
        this.tagBuilders.put("get", GET);
        this.tagBuilders.put("template", TEMPLATE);
    }

    public void register(String name, TagBuilder builder) {
        this.tagBuilders.put(name, builder);
    }

    public boolean exists(String tagName){
        return this.tagBuilders.get(tagName) != null;
    }

    public Tag generate(String name, Map<String, String> attributes, List<Tag> tags) {
        return this.tagBuilders.getOrDefault(name, NOP).build(name, attributes, tags);
    }

}
