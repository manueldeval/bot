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
        this.tagBuilders.put("think", THINK_BUILDER);
        this.tagBuilders.put("nop", NOP_BUILDER);
        this.tagBuilders.put("srai", SRAI_BUILDER);
        this.tagBuilders.put("li", LI_BUILDER);
        this.tagBuilders.put("random", RANDOM_BUILDER);
        this.tagBuilders.put("uppercase", UPPERCASE_BUILDER);
        this.tagBuilders.put("lowercase", LOWERCASE_BUILDER);
        this.tagBuilders.put("formal", FORMAL_BUILDER);
        this.tagBuilders.put("set", SET_BUILDER);
        this.tagBuilders.put("get", GET_BUILDER);
        this.tagBuilders.put("template", TEMPLATE_BUILDER);
    }

    public void register(String name, TagBuilder builder) {
        this.tagBuilders.put(name, builder);
    }

    public boolean exists(String tagName){
        return this.tagBuilders.get(tagName) != null;
    }

    public Tag generate(String name, Map<String, String> attributes, List<Tag> tags) {
        return this.tagBuilders.getOrDefault(name, NOP_BUILDER).build(attributes, tags);
    }

}
