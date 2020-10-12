package com.uj.study.moveAccumulationtoVisitor;


/**
 * @author ：unclejet
 * @date ：Created in 2020/10/13 上午6:30
 * @description：
 * @modified By：
 * @version:
 */
public class TextExtractor {
    private Parser parser;

    private boolean isPreTag;
    private boolean isScriptTag;
    private StringBuffer results;

    public String extractText() throws ParserException {
        Node node;
        parser.flushScanners();
        parser.registerScanners();

        for (NodeIterator e = parser.elements(); e.hasMoreNodes();) {
            node = e.nextNode();
            if (node instanceof StringNode) {
                accept((StringNode) node);
            } else if (node instanceof LinkTag) {
                accept((LinkTag) node);
            } else if (node instanceof EndTag) {
                accept((EndTag) node);
            } else if (node instanceof Tag) {
                accept((Tag) node);
            }
        }
        return (results.toString());
    }

    private void accept(Tag node) {
        Tag tag = node;
        String tagName = tag.getTagName();
        if (tagName.equalsIgnoreCase("PRE"))
            isPreTag = true;
        else if (tagName.equalsIgnoreCase("SCRIPT"))
            isScriptTag = true;
    }

    private void accept(EndTag node) {
        EndTag endTag = node;
        String tagName = endTag.getTagName();
        if (tagName.equalsIgnoreCase("PRE"))
            isPreTag = false;
        else if (tagName.equalsIgnoreCase("SCRIPT"))
            isScriptTag = false;
    }

    private void accept(LinkTag node) {
        LinkTag link = node;
        if (isPreTag)
            results.append(link.getLinkText());
        else
            collapse(results, Translate.decode(link.getLinkText()));
        if (getLinks()) {
            results.append("<");
            results.append(link.getLink());
            results.append(">");
        }
    }

    private void accept(StringNode node) {
        if (!isScriptTag) {
            StringNode stringNode = node;
            if (isPreTag)
                results.append(stringNode.getText());
            else {
                String text = Translate.decode(stringNode.getText());
                if (getReplaceNonBreakingSpace())
                    text = text.replace("\\a0", " ");
                if (getCollapse())
                    collapse(results, text);
                else
                    results.append(text);
            }
        }
    }

    private boolean getLinks() {
        return false;
    }

    private void collapse(StringBuffer results, String text) {

    }

    private boolean getCollapse() {
        return false;
    }

    private boolean getReplaceNonBreakingSpace() {
        return false;
    }
}
