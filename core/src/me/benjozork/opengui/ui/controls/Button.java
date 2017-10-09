package me.benjozork.opengui.ui.controls;

import jdk.nashorn.internal.ir.annotations.Ignore;

import me.benjozork.opengui.render.object.Vector2;
import me.benjozork.opengui.render.object.resource.NinePatch;
import me.benjozork.opengui.ui.Control;
import me.benjozork.opengui.ui.Style;
import me.benjozork.opengui.ui.annotation.FallbackStyle;
import me.benjozork.opengui.ui.annotation.IgnoreParentClassStyles;

/**
 * @author Benjozork
 */
public class Button extends Control {

    public Style normal;

    @FallbackStyle(fallback = "confirm_neutral")
    public Style confirm_positive;

    @FallbackStyle(fallback = "confirm_neutral")
    public Style confirm_negative;

    @FallbackStyle(fallback = "normal")
    public Style confirm_neutral;

    public NinePatch button_default;

    public NinePatch button_hovered;

    public NinePatch button_disabled;

    public Button() {
        super();
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw() {
        super.draw();
        if (isBeingHovered()) {
            this.getBackend().ninepatch(button_hovered, getAbsX(), getAbsY(), width, height);
        } else {
            this.getBackend().ninepatch(button_default, getAbsX(), getAbsY(), width, height);
        }
    }

    @Override
    public boolean isBeingHovered() {
        Vector2 mousePos = this.getBackend().getMousePos();
        return mousePos.x > getAbsX()
                && mousePos.x < getAbsX() + getWidth()
                && mousePos.y > getAbsY()
                && mousePos.y < getAbsY() + getHeight();
    }

    @Override
    public boolean onClick(Vector2 mousePos) {
        return true;
    }

}