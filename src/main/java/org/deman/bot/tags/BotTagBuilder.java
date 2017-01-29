package org.deman.bot.tags;

import org.deman.bot.engine.Context;

import java.util.*;
import java.util.function.Function;

/**
 * Created by deman on 29/01/17.
 */
public class BotTagBuilder implements TagBuilder {

    private Map<String, Function<String, String>> propertiesProvider = new HashMap<>();

    private final static Map<String, String> DEFAULT = new HashMap<String, String>() {{
        put("name", "Jarvis");
    }};

    public BotTagBuilder() {
        propertiesProvider.put("default", DEFAULT::get);
    }

    @Override
    public Tag build(Map<String, String> attributes, List<Tag> childrens) {
        return new DefaultTag("bot", attributes, childrens) {
            public Optional<String> generate(Context context) {
                String attrName = attributes.getOrDefault("name", null);
                return propertiesProvider.values().stream()
                        .map(f -> f.apply(attrName))
                        .filter(Objects::nonNull)
                        .findFirst();
            }
        };
    }
}
