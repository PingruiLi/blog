package com.lpr.blog.utils;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

public class MarkdownUtils {
    public static String convertToHtml(String content) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(content);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public static String markdownToHtmlExtension(String content) {
        Set<Extension> headingAnchor = Collections.singleton(HeadingAnchorExtension.create());
        List<Extension> table = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder().extensions(table).build();
        Node document = parser.parse(content);
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(headingAnchor)
                .extensions(table)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    @Override
                    public AttributeProvider create(AttributeProviderContext attributeProviderContext) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        return renderer.render(document);
    }

    static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            if(node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if(node instanceof TableBlock) {
                attributes.put("class", "ui celled table");
            }
        }
    }
}
