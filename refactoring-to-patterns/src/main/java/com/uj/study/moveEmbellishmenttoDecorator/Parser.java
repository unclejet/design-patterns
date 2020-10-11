package com.uj.study.moveEmbellishmenttoDecorator;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/11 上午8:54
 * @description：
 * @modified By：
 * @version:
 */
public class Parser {
    private NodeIterator nodeIterator = new NodeIterator();

    private boolean shouldDecodeNodes = false;

    public void setNodeDecoding(boolean shouldDecodeNodes) {
        this.shouldDecodeNodes = shouldDecodeNodes;
    }

    public boolean isShouldDecodeNodes() {
        return shouldDecodeNodes;
    }

    public static Parser createParser() {
        return  new Parser();
    }

    public NodeIterator elements() {
        return nodeIterator;
    }

    public void addNode(Node node) {
        nodeIterator.add(node);
    }
}
