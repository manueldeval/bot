package org.deman.bot.tags;

import com.oracle.webservices.internal.api.databinding.Databinding;
import org.apache.commons.lang3.text.WordUtils;
import org.deman.bot.engine.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by deman on 11/01/17.
 */
public class TagsDefinition {

    public static final DefaultTag NOP_INSTANCE = new DefaultTag("nop",null,null){
        public Optional<String> generate(Context context) {
            return Optional.empty();
        }
    };

    public static TagBuilder<DefaultTag> NOP_BUILDER = (name, attributes, tags) -> NOP_INSTANCE;

    public static TagBuilder<DefaultTag> THINK_BUILDER = (name, attributes, tags) -> new DefaultTag(name,attributes,tags){
        public Optional<String> generate(Context context) {
            getChildrens().forEach(c -> c.generate(context));
            return Optional.empty();
        }
    };

    public static TagBuilder<DefaultTag> SRAI_BUILDER = (name, attributes, tags) -> new DefaultTag(name,attributes,tags){
        public Optional<String> generate(Context context) {
            Optional<String> value = concat(context, tags);
            System.out.println("Content of srai: "+value.get());
            return context.getEngine().evaluate(context, value.orElse(""));
        }
    };

    public static TagBuilder<DefaultTag> SET_BUILDER = (name, attributes, tags) -> new DefaultTag(name,attributes,tags){
        public Optional<String> generate(Context context) {
            Optional<String> value = concat(context, tags);
            String name = attributes.get("name");
            context.getState().getVars().put(name, value.orElse(""));
            return Optional.empty();
        }
    };

    public static TagBuilder<DefaultTag> GET_BUILDER = (name, attributes, tags) -> new DefaultTag(name,attributes,tags){
        public Optional<String> generate(Context context) {
            String name = attributes.get("name");
            return Optional.ofNullable(context.getState().getVars().get(name));
        }
    };

    public static TagBuilder<DefaultTag> UPPERCASE_BUILDER = (name, attributes, tags) -> new DefaultTag(name,attributes,tags){
        public Optional<String> generate(Context context) {
            return concat(context, tags).map(String::toUpperCase);
        }
    };

    public static TagBuilder<DefaultTag> LOWERCASE_BUILDER = (name, attributes, tags) -> new DefaultTag(name,attributes,tags){
        public Optional<String> generate(Context context) {
            return concat(context, tags).map(String::toLowerCase);
        }
    };

    public static TagBuilder<DefaultTag> FORMAL_BUILDER = (name, attributes, tags) -> new DefaultTag(name,attributes,tags){
        public Optional<String> generate(Context context) {
            return concat(context, tags)
                    .map(String::toLowerCase)
                    .map(WordUtils::capitalize);
        }
    };

    public static TagBuilder<DefaultTag> RANDOM_BUILDER = (name, attributes, tags) -> new DefaultTag(name,attributes,tags){
        public Optional<String> generate(Context context) {
            return tags.isEmpty()?Optional.empty():tags.get((int)(Math.random()*tags.size())).generate(context);
        }
    };

    public static TagBuilder<DefaultTag> LI_BUILDER = (name, attributes, tags) -> new DefaultTag(name,attributes,tags){
        public Optional<String> generate(Context context) {
            return concat(context, tags);
        }
    };

    public static TagBuilder<DefaultTag> TEMPLATE_BUILDER = (name, attributes, tags) -> new DefaultTag(name,attributes,tags){
        public Optional<String> generate(Context context) {
            return concat(context, tags);
        }
    };

    /*
    public static TagBuilder<DefaultTag> TEXT = (name,text,tags) -> new DefaultTag(name,null, null){
        public Optional<String> generate(Context context) {
            return Optional.ofNullable("");
        }
    };
    */
    public static Optional<String> concat(Context context, List<Tag> tags) {
        return tags.stream()
                .map(tag -> tag.generate(context))
                .reduce(Optional.empty(),
                        (a,b) -> (a.isPresent()||b.isPresent())?
                                Optional.of(a.orElse("")+b.orElse(""))
                                : Optional.empty());
    }


}
