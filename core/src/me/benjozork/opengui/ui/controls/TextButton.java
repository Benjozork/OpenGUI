package me.benjozork.opengui.ui.controls;

import me.benjozork.opengui.render.object.TextComponent;
import me.benjozork.opengui.ui.annotation.PartiallySkinnableProperty;

/**
 * @author Benjozork
 */
    public class TextButton extends Button {

    @PartiallySkinnableProperty(field = "font", alias = "font")
    public TextComponent text;

    public TextButton() {
        super();
    }

    @Override
    public void init() {
        super.init();
        if (this.text == null) this.text = new TextComponent(this.getContext());
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw() {
        super.draw();
        this.getBackend().text(text, getAbsX() + getWidth() / 2 - text.getWidth() / 2, getAbsY() + getHeight() / 2 - text.getHeight() / 2);
    }

}