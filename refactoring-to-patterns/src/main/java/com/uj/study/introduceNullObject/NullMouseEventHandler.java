package com.uj.study.introduceNullObject;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/9 上午6:14
 * @description：
 * @modified By：
 * @version:
 */
public class NullMouseEventHandler extends MouseEventHandler {

    public NullMouseEventHandler() {
        super(null);
    }

    @Override
    public boolean mouseMove(GraphicsContext graphicsContext, Event event, int x, int y) {
        return true;
    }

    @Override
    public boolean mouseDown(GraphicsContext graphicsContext, Event event, int x, int y) {
        return true;
    }

    @Override
    public boolean mouseUp(GraphicsContext graphicsContext, Event event, int x, int y) {
        return true;
    }

    @Override
    public boolean mouseExit(GraphicsContext graphicsContext, Event event, int x, int y) {
        return true;
    }
}
