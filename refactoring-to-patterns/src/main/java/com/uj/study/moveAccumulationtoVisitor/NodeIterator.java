package com.uj.study.moveAccumulationtoVisitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/11 上午8:53
 * @description：
 * @modified By：
 * @version:
 */
public class NodeIterator implements Iterator {
    private List<Node> nodes = new ArrayList<>();
    private int index;

    @Override
    public boolean hasNext() {
        return index < nodes.size();
    }

    @Override
    public Object next() {
        return nodes.remove(index++);
    }

    public boolean hasMoreNodes() {
        return index < nodes.size();
    }

    public Node nextNode() {
        return nodes.remove(index++);
    }

    public void add(Node node) {
        nodes.add(node);
    }
}
