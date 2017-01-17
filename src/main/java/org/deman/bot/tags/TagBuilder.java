package org.deman.bot.tags;


import org.deman.bot.engine.Context;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by deman on 11/01/17.
 */
@FunctionalInterface
public interface TagBuilder<T extends Tag> {

    T build(Map<String,String> attributes,List<Tag> tags);

}
