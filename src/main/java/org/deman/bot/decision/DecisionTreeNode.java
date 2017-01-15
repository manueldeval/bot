package org.deman.bot.decision;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.deman.bot.rules.Category;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by deman on 15/01/17.
 */
// http://docs.pandorabots.com/tutorials/wildcards/
// HELLO #0 > HELLO _1 > HELLO THERE > HELLO ^0 > HELLO *1
public abstract class DecisionTreeNode {

    private static final String HIGH0 = "#";
    private static final String HIGH1 = "_";
    private static final String LOW0 = "^";
    private static final String LOW1 = "*";
    private static Pattern SET_PATTERN = Pattern.compile("<set *?>(.+)</set *?>");

    private DecisionTreeNode h0Node = null;
    private DecisionTreeNode h1Node = null;
    private List<ListDecisionTreeNode> listNodes = new ArrayList<>();
    private DecisionTreeNode l0Node = null;
    private DecisionTreeNode l1Node = null;
    private Map<String, DecisionTreeNode> nodes = new HashMap<>();

    protected Category category = null;

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public abstract Optional<Category> match(Token token);

    protected Optional<Category> matchChildrenNodes(Token next) {
        return createEligibleStream(next.getValue())
                .map(node -> node.match(next))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    protected Stream<DecisionTreeNode> createEligibleStream(String token) {
        return Stream.of(
                Stream.of(h0Node, h1Node),
                listNodes.stream(),
                Stream.of(nodes.get(token)),
                Stream.of(l0Node, l1Node)
        ).flatMap(Function.identity())
                .filter(Objects::nonNull);
    }

    public void createChildrenNode(Token token, Category category) {
        if (token == null) {
            return;
        }

        String value = token.getValue();
        DecisionTreeNode child;
        if (HIGH0.equals(value)) {
            child = getOrCreateH0Node();
        } else if (HIGH1.equals(value)) {
            child = getOrCreateH1Node();
        } else if (LOW0.equals(value)) {
            child = getOrCreateL0Node();
        } else if (LOW1.equals(value)) {
            child = getOrCreateL1Node();
        } else if (value.matches("<set *?>.+</set>")) {
            child = getOrCreateListNode(value);
        } else {
            child = getOrCreateValueNode(value);
        }
        if (token.getNext().isPresent()) {
            child.createChildrenNode(token.getNext().get(), category);
        } else {
            this.category = category;
        }
    }

    private DecisionTreeNode getOrCreateValueNode(String value) {
        DecisionTreeNode child;
        child = nodes.get(value);
        if (nodes.get(value) == null) {
            child = new ValueDecisionTreeNode(value);
            nodes.put(value, child);
        }
        return child;
    }

    private DecisionTreeNode getOrCreateListNode(String value) {
        DecisionTreeNode child;
        Matcher matcher = SET_PATTERN.matcher(value);
        String listName = matcher.group(1);
        Optional<ListDecisionTreeNode> nodeOpt = listNodes.stream()
                .filter(node -> node.getNameOfSet().equals(listName))
                .findAny();
        if (!nodeOpt.isPresent()) {
            ListDecisionTreeNode listDecisionNode = new ListDecisionTreeNode(listName);
            listNodes.add(listDecisionNode);
            child = listDecisionNode;
        } else {
            child = nodeOpt.get();
        }
        return child;
    }

    private DecisionTreeNode getOrCreateL1Node() {
        DecisionTreeNode child;
        if (l1Node == null) {
            l1Node = new OneOrMoreDecisionTreeNode();
        }
        child = l1Node;
        return child;
    }

    private DecisionTreeNode getOrCreateL0Node() {
        DecisionTreeNode child;
        if (l0Node == null) {
            l0Node = new ZeroOrMoreDecisionTreeNode();
        }
        child = l0Node;
        return child;
    }

    private DecisionTreeNode getOrCreateH1Node() {
        DecisionTreeNode child;
        if (h1Node == null) {
            h1Node = new OneOrMoreDecisionTreeNode();
        }
        child = h1Node;
        return child;
    }

    private DecisionTreeNode getOrCreateH0Node() {
        DecisionTreeNode child;
        if (h0Node == null) {
            h0Node = new ZeroOrMoreDecisionTreeNode();
        }
        child = h0Node;
        return child;
    }

    private Map<String,Object> toDebugStructure() {

        Map<String,Object> map = new HashMap<>();

        Stream.of(
                Stream.of(h0Node, h1Node),
                listNodes.stream(),
                nodes.values().stream(),
                Stream.of(l0Node, l1Node)
        ).flatMap(Function.identity())
                .filter(Objects::nonNull)
                .forEach(v -> map.put(v.toString(),v.toDebugStructure()));

        return map;
    }

    public String toJson()  {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(toDebugStructure());
        } catch (JsonProcessingException e) {
            return "Error";
        }
    }
}
