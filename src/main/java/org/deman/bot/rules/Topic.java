package org.deman.bot.rules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deman on 08/01/17.
 */
public class Topic {

    protected List<Category> categories = new ArrayList<>();

    public Topic(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
/*
concat(state,
    think(state,set(state,"tmp",star(0,state)))
    srai(state,"smile"),
    srai(state,get("tmp"))
)
{{think}}
    {{set name="tmp"}}
        {{star/}}
    {{/set}}
{{/think}}
{{srai}}smile{{/srai}}
{{srai}}{{get name="tmp"/}}{{/srai}}
*/