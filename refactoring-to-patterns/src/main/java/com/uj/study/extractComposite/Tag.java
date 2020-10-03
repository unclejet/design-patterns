package com.uj.study.extractComposite;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/4 上午6:31
 * @description：
 * @modified By：
 * @version:
 */
public class Tag {
    private int tagBegin;

    private int tagEnd;

    private String tagContents;

    private String tagLine;

    public Tag(int tagBegin, int tagEnd, String tagContents, String tagLine) {
        this.tagBegin = tagBegin;
        this.tagEnd = tagEnd;
        this.tagContents = tagContents;
        this.tagLine = tagLine;
    }
}
