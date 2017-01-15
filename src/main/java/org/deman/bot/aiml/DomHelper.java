package org.deman.bot.aiml;

import org.w3c.dom.Node;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by deman on 09/01/17.
 */
public class DomHelper {

    public static List<Node> findNodesWithName(Node node, String name) {
        return childrens(node).stream()
                .filter(child -> name.equals(child.getNodeName()))
                .collect(Collectors.toList());
    }

    public static Optional<Node> findFirstNodeWithName(Node node, String name) {
        return childrens(node).stream()
                .filter(child -> name.equals(child.getNodeName()))
                .findFirst();
    }

    public static List<Node> childrens(Node node) {
        if (node.getChildNodes() != null && node.getChildNodes().getLength() > 0) {
            return IntStream
                    .range(0, node.getChildNodes().getLength())
                    .mapToObj(node.getChildNodes()::item)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public static Map<String, String> getAttributes(Node node) {
        Map<String, String> attributes = new HashMap<>();
        if (node.getAttributes() != null && node.getAttributes().getLength() != 0) {
            for (int attr = 0; attr < node.getAttributes().getLength(); attr++) {
                Node attribute = node.getAttributes().item(attr);
                attributes.put(attribute.getNodeName(), attribute.getNodeValue());
            }
        }
        return attributes;
    }

    public static Optional<String> getAttribute(Node node, String attr) {
        if (node.getAttributes() != null
                && node.getAttributes().getLength() != 0
                && node.getAttributes().getNamedItem(attr) != null) {
            return Optional.ofNullable(node.getAttributes().getNamedItem(attr).getNodeValue());
        }
        return Optional.empty();
    }

    public static String serializeNode(Node node) {
        StringWriter writer = new StringWriter();
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerException e) {
            return "";
        }
    }
}
