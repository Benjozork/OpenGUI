package me.benjozork.opengui.render.object;

import java.beans.PropertyEditor;

import me.benjozork.opengui.OpenGUI;
import me.benjozork.opengui.ui.Context;

/**
 * @author Benjozork
 */
public class TextComponent {

    private String text = "";

    private Font font;

    private int width, height;

    public String getText() {
        return text;
    }

    public TextComponent(Context context) {
        this.font = context.getSkin().getDefaultTextFont();
    }

    public TextComponent(Context context, String text) {
        this.font = context.getSkin().getDefaultTextFont();
        this.setText(text);
    }

    public void setText(String text) {
        this.text = text;
        this.width = (int) OpenGUI.getContext().getBackend().findTextDimensions(this).x;
        this.height = (int) OpenGUI.getContext().getBackend().findTextDimensions(this).y;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
        this.width = (int) OpenGUI.getContext().getBackend().findTextDimensions(this).x;
        this.height = (int) OpenGUI.getContext().getBackend().findTextDimensions(this).y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}