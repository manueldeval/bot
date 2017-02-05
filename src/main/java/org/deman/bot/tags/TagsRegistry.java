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

    private BotTagBuilder botTagBuilder = new BotTagBuilder();
    private MapTagBuilder mapTagBuilder = new MapTagBuilder();

    public TagsRegistry() {
        this.tagBuilders.put("think", THINK_BUILDER);
        this.tagBuilders.put("nop", NOP_BUILDER);
        this.tagBuilders.put("srai", SRAI_BUILDER);
        this.tagBuilders.put("sraix", SRAI_BUILDER);
        this.tagBuilders.put("li", LI_BUILDER);
        this.tagBuilders.put("random", RANDOM_BUILDER);
        this.tagBuilders.put("uppercase", UPPERCASE_BUILDER);
        this.tagBuilders.put("lowercase", LOWERCASE_BUILDER);
        this.tagBuilders.put("formal", FORMAL_BUILDER);
        this.tagBuilders.put("set", SET_BUILDER);
        this.tagBuilders.put("get", GET_BUILDER);
        this.tagBuilders.put("template", TEMPLATE_BUILDER);
        this.tagBuilders.put("star", STAR_BUILDER);
        this.tagBuilders.put("thatstar", THATSTAR_BUILDER);
        this.tagBuilders.put("topicstar", TOPICSTAR_BUILDER);
        this.tagBuilders.put("sr", SR_BUILDER);
        this.tagBuilders.put("br", BR_BUILDER);
        this.tagBuilders.put("bot", botTagBuilder);
        this.tagBuilders.put("condition", CONDITION_BUILDER);
        this.tagBuilders.put("explode", EXPLODE_BUILDER);
        this.tagBuilders.put("first", FIRST_BUILDER);
        this.tagBuilders.put("rest", REST_BUILDER);
        this.tagBuilders.put("sentence", SENTENCE_BUILDER);
        this.tagBuilders.put("input", INPUT_BUILDER);
        this.tagBuilders.put("request", REQUEST_BUILDER);
        this.tagBuilders.put("response", RESPONSE_BUILDER);
        this.tagBuilders.put("map", mapTagBuilder);
        this.tagBuilders.put("a",NOP_BUILDER);
        this.tagBuilders.put("that",THAT_BUILDER);
        this.tagBuilders.put("id",ID_BUILDER);
    }

    public BotTagBuilder getBotTagBuilder() {
        return botTagBuilder;
    }

    public MapTagBuilder getMapTagBuilder() {
        return mapTagBuilder;
    }

    public void register(String name, TagBuilder builder) {
        this.tagBuilders.put(name, builder);
    }

    public boolean exists(String tagName) {
        return this.tagBuilders.get(tagName.toLowerCase()) != null;
    }

    public Tag generate(String name, Map<String, String> attributes, List<Tag> tags) {
        return this.tagBuilders.getOrDefault(name.toLowerCase(), NOP_BUILDER).build(attributes, tags);
    }

}
