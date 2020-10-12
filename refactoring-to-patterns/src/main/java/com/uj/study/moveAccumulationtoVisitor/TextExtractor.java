package com.uj.study.moveAccumulationtoVisitor;


/**
 * @author ：unclejet
 * @date ：Created in 2020/10/13 上午6:30
 * @description：
 * @modified By：
 * @version:
 */
public class TextExtractor implements NodeVisitor {
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
            node.accept(this);
        }
        return (results.toString());
    }

    @Override
    public void visitTag(Tag node) {
        Tag tag = node;
        String tagName = tag.getTagName();
        if (tagName.equalsIgnoreCase("PRE"))
            isPreTag = true;
        else if (tagName.equalsIgnoreCase("SCRIPT"))
            isScriptTag = true;
    }

    @Override
    public void visitEndTag(EndTag node) {
        EndTag endTag = node;
        String tagName = endTag.getTagName();
        if (tagName.equalsIgnoreCase("PRE"))
            isPreTag = false;
        else if (tagName.equalsIgnoreCase("SCRIPT"))
            isScriptTag = false;
    }

    @Override
    public void visitLinkTag(LinkTag node) {
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

    @Override
    public void visitStringNode(StringNode node) {
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
