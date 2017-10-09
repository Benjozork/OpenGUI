package me.benjozork.opengui.ui.controls;

import me.benjozork.opengui.render.object.TextComponent;
import me.benjozork.opengui.render.object.Vector2;
import me.benjozork.opengui.render.object.resource.NinePatch;
import me.benjozork.opengui.render.object.resource.Texture;
import me.benjozork.opengui.ui.Control;
import me.benjozork.opengui.ui.Event;
import me.benjozork.opengui.ui.Style;
import me.benjozork.opengui.ui.annotation.DefaultStyle;
import me.benjozork.opengui.ui.annotation.FallbackStyle;
import me.benjozork.opengui.ui.annotation.PartiallySkinnableProperty;
import me.benjozork.opengui.ui.annotation.SkinnableProperty;

/**
 * @author Benjozork
 */
public class CheckBox extends Control {

    @DefaultStyle
    public Style normal;

    @FallbackStyle (fallback = "normal")
    public Style highlighted_info;

    @FallbackStyle(fallback = "highlighted_info")
    public Style highlighted_warning;

    @FallbackStyle(fallback = "highlighted_info")
    public Style highlighted_error;

    public NinePatch checkbox_default;

    public NinePatch checkbox_hovered;

    public NinePatch checkbox_disabled;

    public Texture tick;

    public Texture tick_hovered;

    public Texture tick_disabled;

    // // //

    @PartiallySkinnableProperty (field = "font", alias = "font")
    public TextComponent text;

    public boolean ticked = true;

    @SkinnableProperty
    public int textHorizontalOffset = 5;

    @SkinnableProperty
    public float tickWidthRatio = 0.7f;

    @SkinnableProperty
    public int width = 25;

    @Override
    public void init() {
        if (this.text == null) this.text = new TextComponent(this.getContext());
    }

    @SuppressWarnings ("SuspiciousNameCombination")
    @Override
    public void draw() {
        super.draw();

        if (disabled) {
            this.getBackend().ninepatch(checkbox_disabled, getAbsX(), getAbsY(), width, width);
        } else if (isBeingHovered()) {
            this.getBackend().ninepatch(checkbox_hovered, getAbsX(), getAbsY(), width, width);
        } else {
            this.getBackend().ninepatch(checkbox_default, getAbsX(), getAbsY(), width, width);
        }

        if (ticked &&! isBeingHovered()) {
            this.drawTick(tick);
        } else if (ticked && disabled) {
            this.drawTick(tick_disabled);
        } else if (ticked && isBeingHovered()) {
            this.drawTick(tick_hovered);
        }

        if (text != null) this.getBackend().text(text, getAbsX() + width + textHorizontalOffset, getAbsY() + width / 2 - this.getBackend().findTextDimensions(text).y / 2);
    }

    public void drawTick(Texture texture) {
        this.getBackend().texture(texture, getAbsX() + width / 2 - width * tickWidthRatio / 2, getAbsY() + width / 2 - width * tickWidthRatio / 2, width * tickWidthRatio, width * tickWidthRatio);
    }

    @Override
    public boolean isBeingHovered() {
        Vector2 mousePos = this.getBackend().getMousePos();
        return (mousePos.x > getAbsX()
                && mousePos.x < getAbsX() + width + textHorizontalOffset + this.getBackend().findTextDimensions(text).x
                && mousePos.y > getAbsY()
                && mousePos.y < getAbsY() + width
        );
    }

    @Override
    public boolean onClick(Vector2 mousePos) {
        if (! disabled)  {
            ticked =! ticked;
            this.callEvent(Event.VALUE_CHANGED);
            return true;
        }
        return false;
    }

}