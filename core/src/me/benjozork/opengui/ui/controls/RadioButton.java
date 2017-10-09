package me.benjozork.opengui.ui.controls;

import me.benjozork.opengui.render.object.TextComponent;
import me.benjozork.opengui.render.object.Vector2;
import me.benjozork.opengui.render.object.resource.Texture;
import me.benjozork.opengui.ui.Control;
import me.benjozork.opengui.ui.Style;
import me.benjozork.opengui.ui.annotation.DefaultStyle;
import me.benjozork.opengui.ui.annotation.FallbackStyle;
import me.benjozork.opengui.ui.annotation.PartiallySkinnableProperty;
import me.benjozork.opengui.ui.annotation.SkinnableProperty;

/**
 * @author Benjozork
 */
public class RadioButton extends Control {

    @DefaultStyle
    public Style normal;

    @FallbackStyle (fallback = "normal")
    public Style highlighted_info;

    @FallbackStyle(fallback = "highlighted_info")
    public Style highlighted_warning;

    @FallbackStyle(fallback = "highlighted_info")
    public Style highlighted_error;

    public Texture radiobutton_default;

    public Texture radiobutton_hovered;

    public Texture radiobutton_disabled;

    public Texture tick_default;

    public Texture tick_hovered;

    public Texture tick_disabled;

    @PartiallySkinnableProperty (field = "font", alias = "font")
    public TextComponent text;

    @SkinnableProperty
    public int textHorizontalOffset = 5;

    @SkinnableProperty
    public float tickWidthRatio = 0.5f;

    @SkinnableProperty
    public int width = 25;

    private RadioButtonGroup group;

    private boolean ticked = true;

    @Override
    public void init() {
        if (this.text == null) this.text = new TextComponent(this.getContext());
        if (! (this.getParent() instanceof RadioButtonGroup)) {
            throw new IllegalStateException("RadioButton must always have a RadioButtonGroup parent");
        } else {
            this.group = (RadioButtonGroup) this.getParent();
            this.group.buttons.add(this);
        }
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw() {
        super.draw();

        if (isBeingHovered()) {
            this.getBackend().texture(radiobutton_hovered, getAbsX(), getAbsY(), width, width);
        } else if (disabled) {
            this.getBackend().texture(radiobutton_disabled, getAbsX(), getAbsY(), width, width);
        } else {
            this.getBackend().texture(radiobutton_default, getAbsX(), getAbsY(), width, width);
        }

        if (ticked) {

            int tickX = (int) (getAbsX() + (width / 2) - ((width * tickWidthRatio) / 2));
            int tickY = (int) (getAbsY() + (width / 2) - ((width * tickWidthRatio) / 2));

            if (isBeingHovered()) {
                this.getBackend().texture(tick_hovered, tickX, tickY, width * tickWidthRatio, width * tickWidthRatio);
            } else if (disabled) {
                this.getBackend().texture(tick_disabled, tickX, tickY, width * tickWidthRatio, width * tickWidthRatio);
            } else {
                this.getBackend().texture(tick_default, tickX, tickY, width * tickWidthRatio, width * tickWidthRatio);
            }
        }

        int textX = this.getAbsX() + width + textHorizontalOffset;
        int textY = this.getAbsY() + width / 2 - text.getHeight() / 2;

        this.getBackend().text(text, textX, textY);
    }

    @Override
    public boolean isBeingHovered() {
        Vector2 mousePos = this.getBackend().getMousePos();
        return mousePos.x > getAbsX()
                && mousePos.x < getAbsX() + width + textHorizontalOffset + text.getWidth()
                && mousePos.y > getAbsY()
                && mousePos.y < getAbsY() + width;
    }

    @Override
    public boolean onClick(Vector2 mousePos) {
        this.setTicked(true);
        group.updateButtonStates(this);
        return true;
    }

    public boolean isTicked() {
        return ticked;
    }

    public void setTicked(boolean ticked) {
        this.ticked = ticked;
    }

}