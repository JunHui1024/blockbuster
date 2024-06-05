package com.cjh.blockbuster.core.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;

/**
 *
 * @author 沉默王二
 * @date 4/15/23
 */
public class MarkdownConverter {
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Document document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
