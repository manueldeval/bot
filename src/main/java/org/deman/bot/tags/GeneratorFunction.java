package org.deman.bot.tags;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Created by deman on 11/01/17.
 */
public interface GeneratorFunction extends BiFunction<Map<String,String>,List<Tag>,Optional<String>> {
}
