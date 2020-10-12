package com.uj.study.moveAccumulationtoVisitor;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/13 上午6:32
 * @description：
 * @modified By：
 * @version:
 */
public class StringNode implements Node {
    public String getText() {
        return null;
    }

    @Override
    public void accept(NodeVisitor nodeVisitor) {
        nodeVisitor.visitStringNode(this);
    }
}
