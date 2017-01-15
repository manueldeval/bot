package org.deman.bot.aiml;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.deman.bot.engine.Context;
import org.deman.bot.rules.Category;
import org.deman.bot.tags.Tag;
import org.deman.bot.tags.TagsDefinition;
import org.deman.bot.tags.TagsRegistry;
import org.deman.bot.tags.TextTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by deman on 15/01/17.
 */
public class TemplateCompiler {

    public static final Logger logger = LoggerFactory.getLogger(TemplateCompiler.class);

    public static final String TEXT_TAG = "#text";
    public static final String CDATA_SECTION = "#cdata-section";
    public static final String DOCUMENT_TAG = "#document";
    public static final String COMMENT_TAG = "#comment";

    public static Tag compile(Category category, TagsRegistry tagsRegistry) throws AimlParserException {
        InputSource is = new InputSource(new StringReader(category.getTemplate()));
        DOMParser parser = new DOMParser();
        try {
            parser.parse(is);
        } catch (SAXException | IOException e) {
            throw new AimlParserException(e);
        }
        Document node = parser.getDocument();
        return processNode(node, tagsRegistry)
                .orElse(TagsDefinition.NOP.build("nop", null, null));
    }

    private static Optional<Tag> processNode(Node node, TagsRegistry tagsRegistry) {
        String name = node.getNodeName();
        // Get Instance of tagbuilder
        if (tagsRegistry.exists(name)) {
            Map<String, String> attributes = DomHelper.getAttributes(node);
            List<Node> childrens = DomHelper.childrens(node);
            List<Tag> tags = childrens.stream()
                    .map(childNode -> processNode(node, tagsRegistry))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            return Optional.of(tagsRegistry.generate(name, attributes, tags));
        } else if (TEXT_TAG.equals(name) || CDATA_SECTION.equals(name)) {
            return Optional.of(new TextTag(node.getNodeValue()));
        } else {
            if (!DOCUMENT_TAG.equals(name) && !COMMENT_TAG.equals(name)) {
                logger.error("Unrecognized tag : " + name);
            }
            return Optional.empty();
        }
    }

}
