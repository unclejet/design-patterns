package com.uj.study.introduceNullObject;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/9 上午6:11
 * @description：
 * @modified By：
 * @version:
 */
public class NavigationApplet extends Applet {
    private MouseEventHandler mouseEventHandler = new NullMouseEventHandler();
    private GraphicsContext graphicsContext;

    public boolean mouseMove(Event event, int x, int y) {
        return mouseEventHandler.mouseMove(graphicsContext, event, x, y);
    }

    public boolean mouseDown(Event event, int x, int y) {
        return mouseEventHandler.mouseDown(graphicsContext, event, x, y);
    }

    public boolean mouseUp(Event event, int x, int y) {
        return mouseEventHandler.mouseUp(graphicsContext, event, x, y);
    }

    public boolean mouseExit(Event event, int x, int y) {
        return mouseEventHandler.mouseExit(graphicsContext, event, x, y);
    }
}
