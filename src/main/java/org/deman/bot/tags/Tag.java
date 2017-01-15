package org.deman.bot.tags;

import org.deman.bot.engine.Context;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by deman on 11/01/17.
 */
public interface Tag {

    String getName();
    Map<String,String> getAttributes();
    List<Tag> getChildrens();

    Optional<String> generate(Context context);

}
