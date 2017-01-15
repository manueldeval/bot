package org.deman.bot.aiml;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.deman.bot.rules.Category;
import org.deman.bot.tags.TagsRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by deman on 09/01/17.
 */
public class AimlParser {

    public static final String UTF_8 = "UTF-8";
    public static final String AIML_TAG = "aiml";
    public static final String CATEGORY_TAG = "category";
    public static final String TOPICS_TAG = "topics";
    public static final String EMPTY = "";
    public static final String TOPIC_NAME_ATTR = "name";
    public static final String DEFAULT_TOPIC = "*";
    public static final String DEFAULT_THAT = "*";
    public static final String TEMPLATE_TAG = "template";
    public static final String PATTERN_TAG = "pattern";
    public static final String THAT_TAG = "that";

    public static List<Category> parse(File file, TagsRegistry tagsRegistry) throws AimlParserException {
        InputSource is;
        try {
            is = new InputSource(new InputStreamReader(new FileInputStream(file), UTF_8));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            throw new AimlParserException(e);
        }
        return parse(is, tagsRegistry);
    }

    public static List<Category> parse(String xml, TagsRegistry tagsRegistry) throws AimlParserException {
        return parse(new InputSource(new StringReader(xml)), tagsRegistry);
    }

    public static List<Category> parse(InputSource is, TagsRegistry tagsRegistry) throws AimlParserException {
        return parseXml(is);
    }

    private static List<Category> parseXml(InputSource is) throws AimlParserException {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(is);
        } catch (SAXException | IOException e) {
            throw new AimlParserException(e);
        }
        Document document = parser.getDocument();
        Optional<Node> aimlNodeOpt = DomHelper.findFirstNodeWithName(document, AIML_TAG);

        List<Tuple<String, Node>> categoryNodesWithDefaultTopicName = aimlNodeOpt.map(AimlParser::getRootCategoryNodes).orElse(Collections.emptyList());
        List<Tuple<String, Node>> categoryNodesWithTopicName = aimlNodeOpt.map(AimlParser::getCategoryNodesOfTopics).orElse(Collections.emptyList());

        List<Tuple<String, Node>> allCategoryNodesWithName = new ArrayList<>();
        allCategoryNodesWithName.addAll(categoryNodesWithTopicName);
        allCategoryNodesWithName.addAll(categoryNodesWithDefaultTopicName);

        return allCategoryNodesWithName.stream()
                .map(tuple -> new Category(tuple.getX(), getThatNode(tuple.getY()), getPatternNode(tuple.getY()), getTemplateNode(tuple.getY())))
                .collect(Collectors.toList());
    }

    private static List<Tuple<String, Node>> getCategoryNodesOfTopics(Node aimlNode) {
        List<Node> topicNodes = findTopics(aimlNode);
        return topicNodes.stream()
                .flatMap(topicNode -> {
                    String topicName = getTopicNodeName(topicNode);
                    return findCategories(topicNode).stream().map(categoryNode -> new Tuple<>(topicName, categoryNode));
                }).collect(Collectors.toList());
    }

    private static List<Tuple<String, Node>> getRootCategoryNodes(Node aimlNode) {
        List<Node> categoryNodes = findCategories(aimlNode);
        return categoryNodes.stream()
                .map(categoryNode -> new Tuple<>(DEFAULT_TOPIC, categoryNode))
                .collect(Collectors.toList());
    }

    private static String getTemplateNode(Node categoryNode) {
        return DomHelper
                .findFirstNodeWithName(categoryNode, TEMPLATE_TAG)
                .map(DomHelper::serializeNode)
                .orElse("");
    }

    private static String getPatternNode(Node categoryNode) {
        return DomHelper
                .findFirstNodeWithName(categoryNode, PATTERN_TAG)
                .map(DomHelper::serializeNode)
                .orElse("")
                .replaceAll("<\\?xml.*?>", "")
                .replaceAll("<pattern *?>", "")
                .replaceAll("</pattern *?>", "")
                .replaceAll("<pattern *?/>", "");
    }

    private static String getThatNode(Node node) {
        return DomHelper
                .findFirstNodeWithName(node, THAT_TAG)
                .map(DomHelper::serializeNode)
                .orElse(DEFAULT_THAT)
                .replaceAll("<\\?xml.*?>", "")
                .replaceAll("<that *?>", "")
                .replaceAll("</that *?>", "")
                .replaceAll("<that *?/>", "");
    }

    private static List<Node> findTopics(Node aiml) {
        return DomHelper.findNodesWithName(aiml, TOPICS_TAG);
    }

    private static List<Node> findCategories(Node aiml) {
        return DomHelper.findNodesWithName(aiml, CATEGORY_TAG);
    }

    private static String getTopicNodeName(Node topicNode) {
        return DomHelper.getAttribute(topicNode, TOPIC_NAME_ATTR).orElse(EMPTY);
    }

}
