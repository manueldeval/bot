package org.deman.bot.rules;

import org.deman.bot.tags.Tag;

/**
 * Created by deman on 08/01/17.
 */
public class Category {

    private String template;

    private String pattern;

    private String topic;

    private String that;

    @Override
    public String toString() {
        return "Category{" +
                "pattern='" + pattern + '\'' +
                ", topic='" + topic + '\'' +
                ", that='" + that + '\'' +
                ", template='" + template + '\'' +
                '}';
    }

    public Category(String topic, String that, String pattern, String template) {
        this.template = template;
        this.pattern = pattern;
        this.topic = topic;
        this.that = that;
    }

    public String getThat() {
        return that;
    }

    public String getTemplate() {
        return template;
    }

    public String getPattern() {
        return pattern;
    }

    public String getTopic() {
        return topic;
    }
}
