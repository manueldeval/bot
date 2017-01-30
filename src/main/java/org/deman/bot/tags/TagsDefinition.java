package org.deman.bot.tags;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.deman.bot.engine.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by deman on 11/01/17.
 */
public class TagsDefinition {

    private static final Logger logger = LoggerFactory.getLogger(TagsDefinition.class);

    public static final DefaultTag NOP_INSTANCE = new DefaultTag("nop", null, null) {
        public Optional<String> generate(Context context) {
            return Optional.empty();
        }
    };

    public static TagBuilder NOP_BUILDER = (attributes, tags) -> NOP_INSTANCE;

    public static TagBuilder THINK_BUILDER = (attributes, tags) -> new DefaultTag("think", attributes, tags) {
        public Optional<String> generate(Context context) {
            getChildrens().forEach(c -> c.generate(context));
            return Optional.empty();
        }
    };

    public static TagBuilder SRAI_BUILDER = (attributes, tags) -> new DefaultTag("srai", attributes, tags) {
        public Optional<String> generate(Context context) {
            Optional<String> value = concat(context, tags);
            return context.getEngine().evaluate(context, value.orElse(""));
        }
    };

    public static TagBuilder SET_BUILDER = (attributes, tags) -> new DefaultTag("set", attributes, tags) {
        public Optional<String> generate(Context context) {
            Optional<String> value = concat(context, tags);
            String name = attributes.get("name");
            context.getState().getVars().put(name, value.orElse(""));
            return Optional.empty();
        }
    };

    public static TagBuilder GET_BUILDER = (attributes, tags) -> new DefaultTag("get", attributes, tags) {
        public Optional<String> generate(Context context) {
            String name = attributes.get("name");
            return Optional.ofNullable(context.getState().getVars().get(name));
        }
    };

    public static TagBuilder UPPERCASE_BUILDER = (attributes, tags) -> new DefaultTag("uppercase", attributes, tags) {
        public Optional<String> generate(Context context) {
            return concat(context, tags).map(String::toUpperCase);
        }
    };

    public static TagBuilder LOWERCASE_BUILDER = (attributes, tags) -> new DefaultTag("lowercase", attributes, tags) {
        public Optional<String> generate(Context context) {
            return concat(context, tags).map(String::toLowerCase);
        }
    };

    public static TagBuilder FORMAL_BUILDER = (attributes, tags) -> new DefaultTag("formal", attributes, tags) {
        public Optional<String> generate(Context context) {
            return concat(context, tags)
                    .map(String::toLowerCase)
                    .map(WordUtils::capitalize);
        }
    };

    public static TagBuilder SENTENCE_BUILDER = (attributes, tags) -> new DefaultTag("sentence", attributes, tags) {
        public Optional<String> generate(Context context) {
            return concat(context, tags)
                    .map(String::toLowerCase)
                    .map(StringUtils::capitalize);
        }
    };

    public static TagBuilder RANDOM_BUILDER = (attributes, tags) -> new DefaultTag("random", attributes, tags) {
        public Optional<String> generate(Context context) {
            List<Tag> liTags = tags.stream().filter(tag -> tag.getName().equals("li")).collect(Collectors.toList());
            return liTags.isEmpty() ? Optional.empty() : liTags.get((int) (Math.random() * liTags.size())).generate(context);
        }
    };

    public static TagBuilder LI_BUILDER = (attributes, tags) -> new DefaultTag("li", attributes, tags) {
        public Optional<String> generate(Context context) {
            return concat(context, tags);
        }
    };

    public static TagBuilder TEMPLATE_BUILDER = (attributes, tags) -> new DefaultTag("template", attributes, tags) {
        public Optional<String> generate(Context context) {
            return concat(context, tags);
        }
    };

    public static TagBuilder STAR_BUILDER = (attributes, tags) -> new DefaultTag("star", attributes, tags) {
        public Optional<String> generate(Context context) {
            int index = 1;
            String strIndex = attributes.get("index");
            if (strIndex != null) {
                try {
                    index = Integer.parseInt(strIndex);
                } catch (Exception e) {
                    logger.error("Bad index attribute format for star, must be an integer.", e);
                    return Optional.empty();
                }
            }
            return context.getState().getPatternStars().getStarAt(index);
        }
    };

    public static TagBuilder THATSTAR_BUILDER = (attributes, tags) -> new DefaultTag("thatstar", attributes, tags) {
        public Optional<String> generate(Context context) {
            int index = 1;
            String strIndex = attributes.get("index");
            if (strIndex != null) {
                try {
                    index = Integer.parseInt(strIndex);
                } catch (Exception e) {
                    logger.error("Bad index attribute format for thatstar, must be an integer.", e);
                    return Optional.empty();
                }
            }
            return context.getState().getThatStars().getStarAt(index);
        }
    };

    public static TagBuilder TOPICSTAR_BUILDER = (attributes, tags) -> new DefaultTag("topicstar", attributes, tags) {
        public Optional<String> generate(Context context) {
            int index = 1;
            String strIndex = attributes.get("index");
            if (strIndex != null) {
                try {
                    index = Integer.parseInt(strIndex);
                } catch (Exception e) {
                    logger.error("Bad index attribute format for topicstar, must be an integer.", e);
                    return Optional.empty();
                }
            }
            return context.getState().getTopicStars().getStarAt(index);
        }
    };

    public static TagBuilder SR_BUILDER = (attributes, tags) -> new DefaultTag("sr", attributes, tags) {
        public Optional<String> generate(Context context) {
            return context.getEngine().evaluate(context, context.getState().getPatternStars().getStarAt(1).orElse(""));
        }
    };

    public static TagBuilder BR_BUILDER = (attributes, tags) -> new DefaultTag("br", attributes, tags) {
        public Optional<String> generate(Context context) {
            return Optional.of("\n");
        }
    };

    public static TagBuilder CONDITION_BUILDER = (attributes, tags) -> new DefaultTag("condition", attributes, tags) {
        public Optional<String> generate(Context context) {
            String varName = attributes.get("name");
            if (varName == null) {
                logger.warn("Unable to get the attribute 'name' for condition");
                return Optional.empty();
            }

            String value = context.getState().getVars().get(varName);
            List<Tag> lis = this.getChildrens().stream().filter(tag -> tag.getName().equals("li")).collect(Collectors.toList());
            Optional<Tag> liWithTheGoodValue = lis.stream().filter(tag -> tag.getAttributes().get("value").equals(value)).findFirst();
            Optional<Tag> liWithNoValue = lis.stream().filter(tag -> tag.getAttributes().get("value") == null).findFirst();

            return liWithTheGoodValue.orElseGet(() -> liWithNoValue.orElse(NOP_INSTANCE)).generate(context);
        }
    };

    public static TagBuilder EXPLODE_BUILDER = (attributes, tags) -> new DefaultTag("explode", attributes, tags) {
        public Optional<String> generate(Context context) {
            return concat(context, tags).map(s -> s.replace("", " ").trim());
        }
    };

    public static TagBuilder FIRST_BUILDER = (attributes, tags) -> new DefaultTag("first", attributes, tags) {
        public Optional<String> generate(Context context) {
            return concat(context, tags).flatMap(s -> Stream.of(s.split("\\s+")).findFirst());
        }
    };

    public static TagBuilder REST_BUILDER = (attributes, tags) -> new DefaultTag("rest", attributes, tags) {
        public Optional<String> generate(Context context) {
            return concat(context, tags).map(s -> {
                List<String> splitted = Arrays.asList(s.split("\\s+"));
                if (splitted.size() > 1){
                    splitted.remove(0);
                }
                return splitted.stream().collect(Collectors.joining(" "));
            });
        }
    };

    public static Optional<String> concat(Context context, List<Tag> tags) {
        return tags.stream()
                .map(tag -> tag.generate(context))
                .reduce(Optional.empty(),
                        (a, b) -> (a.isPresent() || b.isPresent()) ?
                                Optional.of(a.orElse("") + b.orElse(""))
                                : Optional.empty());
    }


}
