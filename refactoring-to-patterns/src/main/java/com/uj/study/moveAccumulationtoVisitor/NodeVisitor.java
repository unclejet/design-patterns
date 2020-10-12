package com.uj.study.moveAccumulationtoVisitor;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/13 上午6:53
 * @description：
 * @modified By：
 * @version:
 */
public interface NodeVisitor {
    void visitTag(Tag tag);

    void visitEndTag(EndTag endTag);

    void visitLinkTag(LinkTag link);

    void visitStringNode(StringNode stringNode);
}
