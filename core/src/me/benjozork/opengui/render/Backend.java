package me.benjozork.opengui.render;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

import java.io.Reader;

import me.benjozork.opengui.render.object.Color;
import me.benjozork.opengui.render.object.Font;
import me.benjozork.opengui.render.object.Rotation;
import me.benjozork.opengui.render.object.TextComponent;
import me.benjozork.opengui.render.object.resource.NinePatch;
import me.benjozork.opengui.render.object.resource.Path;
import me.benjozork.opengui.render.object.resource.Texture;

/**
 * An interface which provides methods for rendering OpenGUI elements.
 *
 * @author Benjozork
 */
public interface Backend {

    void setColor(Color c);

    Color getColor();

    void rect(float x, float y, float width, float height, Rotation rotation, boolean filled);

    void rect(float x, float y, float width, float height, boolean filled);

    void rect(Vector2 v1, Vector2 vector2, boolean filled);

    void line(float x1, float y1, float x2, float y2);

    void line(Vector2 start, Vector2 end);

    void polygon(float[] vertices);

    void text(TextComponent component, float x, float y);

    void texture(Texture texture, float x, float y, float width, float height);

    void texture(Texture texture, float x, float y, float width, float height, Rotation rotation);

    void ninepatch(NinePatch texture, float x, float y, float width, float height);

    void ninepatch(NinePatch texture, float x, float y, float width, float height, Rotation rotation);

    Reader getReader(Path skinPath);

    boolean isKeyDown(int key);

    boolean isKeyUp(int key);

    boolean isKeyJustPressed(int key);

    me.benjozork.opengui.render.object.Vector2 getMousePos();

    boolean isButtonPressed(int button);

    boolean isButtonJustPressed(int button);

    me.benjozork.opengui.render.object.Vector2 findTextDimensions(TextComponent textComponent);

    me.benjozork.opengui.render.object.Vector2 findTexturreDimensions(Texture texture);

    BitmapFont getFont(Font font);

    void setTint(Color c);

}