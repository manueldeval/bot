package org.deman.bot.tags;

import org.deman.bot.engine.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * Created by deman on 29/01/17.
 */
public class MapTagBuilder implements TagBuilder {

    private Map<String, Function<String, String>> propertiesProvider = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(MapTagBuilder.class);

    public void register(String mapName, Function<String, String> provider){
        this.propertiesProvider.put(mapName,provider);
    }


    public void unRegister(String mapName, Function<String, String> provider){
        this.propertiesProvider.remove(mapName);
    }

    @Override
    public Tag build(Map<String, String> attributes, List<Tag> childrens) {
        return new DefaultTag("map", attributes, childrens) {
            public Optional<String> generate(Context context) {
                String mapName = attributes.get("name");
                if (mapName != null) {
                    Function<String,String> provider = propertiesProvider.get(mapName);
                    if (provider != null){
                        return TagsDefinition.concat(context,childrens).map(provider);
                    } else {
                        logger.error("No map provider named: '"+mapName+"'");
                        return Optional.empty();
                    }
                } else {
                    logger.error("Required map name attribute is missing.");
                    return Optional.empty();
                }
            }
        };
    }
}
