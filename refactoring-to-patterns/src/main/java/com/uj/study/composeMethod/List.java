package com.uj.study.composeMethod;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/30 上午7:05
 * @description：
 * @modified By：
 * @version:
 */
public class List {
    private boolean readOnly;
    private int size;
    private Object[] elements;

    public void add(Object element) {
        if (!readOnly) {
            int newSize = size + 1;
            if (newSize > elements.length) {
                Object[] newElements =
                        new Object[elements.length + 10];
                for (int i = 0; i < size; i++)
                    newElements[i] = elements[i];
                elements = newElements;
            }
            elements[size++] = element;
        }
    }
}
