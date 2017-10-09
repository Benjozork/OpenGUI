package me.benjozork.opengui.render.backends;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;

import me.benjozork.opengui.render.Backend;
import me.benjozork.opengui.render.object.Color;
import me.benjozork.opengui.render.object.Font;
import me.benjozork.opengui.render.object.Rotation;
import me.benjozork.opengui.render.object.TextComponent;
import me.benjozork.opengui.render.object.resource.ExternalPath;
import me.benjozork.opengui.render.object.resource.InternalPath;
import me.benjozork.opengui.render.object.resource.NinePatch;
import me.benjozork.opengui.render.object.resource.Path;
import me.benjozork.opengui.render.object.resource.Texture;

/**
 * A LibGDX implementation of {@link Backend}.<br/>
 * Allows to use OpenGUI with a LibGDX backend.
 *
 * @author Benjozork
 */
public class GDXBackend implements Backend {

    public SpriteBatch spriteBatch;
    public ShapeRenderer shapeRenderer;

    private boolean isDrawingShapes = false;
    private boolean isDrawingSprites = false;

    private HashMap<Texture, com.badlogic.gdx.graphics.Texture> loadedTextures;

    private HashMap<NinePatch, com.badlogic.gdx.graphics.g2d.NinePatch> loadedNinePatches;

    private HashMap<TextComponent, me.benjozork.opengui.render.object.Vector2> textComponentDimensions;

    private HashMap<Font, BitmapFont> loadedFonts;

    private GlyphLayout tempLayout = new GlyphLayout();
    private Color color = new Color(255, 255, 255);
    private HashSet<Integer> buttonsChecked = new HashSet<Integer>();

    public GDXBackend() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        shapeRenderer.setAutoShapeType(true);

        this.loadedTextures = new HashMap<Texture, com.badlogic.gdx.graphics.Texture>();
        this.loadedNinePatches = new HashMap<NinePatch, com.badlogic.gdx.graphics.g2d.NinePatch>();
        this.textComponentDimensions = new HashMap<TextComponent, me.benjozork.opengui.render.object.Vector2>();
        this.loadedFonts = new HashMap<Font, BitmapFont>();
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public HashMap<Texture, com.badlogic.gdx.graphics.Texture> getLoadedTextures() {
        return loadedTextures;
    }

    @Override
    public void setColor(Color c) {
        this.color = c;
        shapeRenderer.setColor(new com.badlogic.gdx.graphics.Color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 255));
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void rect(float x, float y, float width, float height, Rotation rotation, boolean filled) {
        prepareBatchesShape();
        if (filled) shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(x, y, rotation.originX, rotation.originY, width, height, 1, 1, rotation.degrees);
    }

    @Override
    public void rect(float x, float y, float width, float height, boolean filled) {
        prepareBatchesShape();
        if (filled) shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(x, y, width, height);
    }

    @Override
    public void rect(Vector2 v1, Vector2 v2, boolean filled) {
        prepareBatchesShape();
        if (filled) shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(v1.x, v1.y, v2.x - v1.x, v2.y - v1.y);
    }

    @Override
    public void line(float x1, float y1, float x2, float y2) {
        prepareBatchesShape();
        shapeRenderer.line(x1, y1, x2, y2);
    }

    @Override
    public void line(Vector2 start, Vector2 end) {
        prepareBatchesShape();
        shapeRenderer.line(start, end);
    }

    @Override
    public void polygon(float[] vertices) {
        prepareBatchesShape();
        shapeRenderer.polygon(vertices);
    }

    @Override
    public void text(TextComponent component, float x, float y) {
        prepareBatchesSprite();
        BitmapFont font = this.getFont(component.getFont());
        font.draw(spriteBatch, component.getText(), x, y + this.findTextDimensions(component).y);
    }

    @Override
    public void texture(Texture texture, float x, float y, float width, float height) {
        prepareBatchesSprite();

        if (! loadedTextures.containsKey(texture)) {

            // Load texture in libGDX format

            if (texture.path instanceof InternalPath) {
                com.badlogic.gdx.graphics.Texture texture1 = new com.badlogic.gdx.graphics.Texture(Gdx.files.internal(texture.path.path));
                texture1.setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Linear, com.badlogic.gdx.graphics.Texture.TextureFilter.Linear);
                loadedTextures.put(texture, texture1);
            } else if (texture.path instanceof ExternalPath) {
                loadedTextures.put(texture, new com.badlogic.gdx.graphics.Texture(Gdx.files.external(texture.path.path)));
            }

        }

        com.badlogic.gdx.graphics.g2d.TextureRegion toDraw = new TextureRegion(loadedTextures.get(texture));

        spriteBatch.draw(toDraw, x, y, width, height);

    }

    @Override
    public void texture(Texture texture, float x, float y, float width, float height, Rotation rotation) {
        prepareBatchesSprite();

        if (! loadedTextures.containsKey(texture)) {

            // Load texture in libGDX format

            if (texture.path instanceof InternalPath) {
                loadedTextures.put(texture, new com.badlogic.gdx.graphics.Texture(Gdx.files.internal(texture.path.path)));
            } else if (texture.path instanceof ExternalPath) {
                loadedTextures.put(texture, new com.badlogic.gdx.graphics.Texture(Gdx.files.external(texture.path.path)));
            }

        }

        com.badlogic.gdx.graphics.g2d.TextureRegion toDraw = new TextureRegion(loadedTextures.get(texture));

        spriteBatch.draw(toDraw, x, y, rotation.originX, rotation.originY, width, height, 1f, 1f, rotation.degrees);

    }

    @Override
    public void ninepatch(NinePatch ninePatch, float x, float y, float width, float height) {
        prepareBatchesSprite();

        if (! loadedNinePatches.containsKey(ninePatch)) {

            // Load texture in libGDX format

            if (ninePatch.path instanceof InternalPath) {
                com.badlogic.gdx.graphics.g2d.NinePatch ninePatch1 = new com.badlogic.gdx.graphics.g2d.NinePatch(new com.badlogic.gdx.graphics.Texture(Gdx.files.internal(ninePatch.path.path)), ninePatch.left, ninePatch.right, ninePatch.top, ninePatch.bottom);
                ninePatch1.getTexture().setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Linear, com.badlogic.gdx.graphics.Texture.TextureFilter.Linear);
                loadedNinePatches.put(ninePatch, ninePatch1);
            } else if (ninePatch.path instanceof ExternalPath) {
                loadedNinePatches.put(ninePatch, new com.badlogic.gdx.graphics.g2d.NinePatch(new com.badlogic.gdx.graphics.Texture(Gdx.files.external(ninePatch.path.path)), ninePatch.left, ninePatch.right, ninePatch.top, ninePatch.bottom));
            }

        }

        com.badlogic.gdx.graphics.g2d.NinePatch toDraw = loadedNinePatches.get(ninePatch);

        toDraw.draw(spriteBatch, x, y, width, height);

    }

    @Override
    public void ninepatch(NinePatch ninePatch, float x, float y, float width, float height, Rotation rotation) {
        prepareBatchesSprite();

        if (! loadedNinePatches.containsKey(ninePatch)) {

            // Load texture in libGDX format

            if (ninePatch.path instanceof InternalPath) {
                loadedNinePatches.put(ninePatch, new com.badlogic.gdx.graphics.g2d.NinePatch(new com.badlogic.gdx.graphics.Texture(Gdx.files.internal(ninePatch.path.path)), ninePatch.left, ninePatch.right, ninePatch.top, ninePatch.bottom));
            } else if (ninePatch.path instanceof ExternalPath) {
                loadedNinePatches.put(ninePatch, new com.badlogic.gdx.graphics.g2d.NinePatch(new com.badlogic.gdx.graphics.Texture(Gdx.files.external(ninePatch.path.path)), ninePatch.left, ninePatch.right, ninePatch.top, ninePatch.bottom));
            }

        }

        com.badlogic.gdx.graphics.g2d.NinePatch toDraw = loadedNinePatches.get(ninePatch);

        toDraw.draw(spriteBatch, x, y, rotation.originX, rotation.originY, width, height, 1f, 1f, rotation.degrees);

    }

    @Override
    public Reader getReader(Path skinPath) {
        if (skinPath instanceof InternalPath) {
            return Gdx.files.internal(skinPath.path).reader();
        } else if (skinPath instanceof ExternalPath) {
            return Gdx.files.external(skinPath.path).reader();
        } else return null;
    }

    public FileHandle getFileHandle(Path skinPath) {
        if (skinPath instanceof InternalPath) {
            return Gdx.files.internal(skinPath.path);
        } else if (skinPath instanceof ExternalPath) {
            return Gdx.files.external(skinPath.path);
        } else return null;
    }

    @Override
    public boolean isKeyDown(int key) {
        return Gdx.input.isKeyPressed(key);
    }

    @Override
    public boolean isKeyUp(int key) {
        return ! Gdx.input.isKeyPressed(key);
    }

    @Override
    public boolean isKeyJustPressed(int key) {
        return Gdx.input.isKeyJustPressed(key);
    }

    @Override
    public me.benjozork.opengui.render.object.Vector2 getMousePos() {
        return new me.benjozork.opengui.render.object.Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    }

    @Override
    public boolean isButtonPressed(int button) {
        return Gdx.input.isButtonPressed(button);
    }

    @Override
    public boolean isButtonJustPressed(int button) {
        if (Gdx.input.isButtonPressed(button) && ! buttonsChecked.contains(button)) {
            buttonsChecked.add(button);
            return true;
        } else if (! Gdx.input.isButtonPressed(button)) {
            buttonsChecked.remove(button);
            return false;
        }
        return false;
    }

    @Override
    public me.benjozork.opengui.render.object.Vector2 findTextDimensions(TextComponent textComponent) {

        // We could use a custom equals() method but that isn't really necessary right now

        for (TextComponent component : textComponentDimensions.keySet()) {
            if (component.getText().equals(textComponent.getText())
                    && component.getFont().equals(textComponent.getFont())) {
                return textComponentDimensions.get(component);
            }
        }
        tempLayout.setText(this.getFont(textComponent.getFont()), textComponent.getText());
        textComponentDimensions.put(textComponent, new me.benjozork.opengui.render.object.Vector2(tempLayout.width, tempLayout.height));
        return new me.benjozork.opengui.render.object.Vector2(tempLayout.width, tempLayout.height);
    }

    @Override
    public me.benjozork.opengui.render.object.Vector2 findTexturreDimensions(Texture texture) {

        // We could use a custom equals() method but that isn't really necessary right now

        for (Texture texture1 : loadedTextures.keySet()) {
            if (texture1.path.equals(texture.path)) {
                return new me.benjozork.opengui.render.object.Vector2(loadedTextures.get(texture1).getWidth(), loadedTextures.get(texture1).getHeight());
            }
        }
        loadedTextures.put(texture, new com.badlogic.gdx.graphics.Texture(Gdx.files.internal(texture.path.path)));
        return new me.benjozork.opengui.render.object.Vector2(loadedTextures.get(texture).getWidth(), loadedTextures.get(texture).getHeight());
    }

    @Override
    public BitmapFont getFont(Font font) {

      // todo use a custom equals() method instead of this mess

        Font searchFont = new Font(font.getPath(), font.getSize(), font.getColor());

        for (Font font1 : loadedFonts.keySet()) {
            if (font1.getPath() == font.getPath()
                && font1.getSize() == font.getSize()
                && font1.getColor() == font.getColor()) {
                return loadedFonts.get(font1);
            }
        }

        Path path = font.getPath();
        if (path.path.endsWith(".png")) {
            return new BitmapFont(getFileHandle(path));
        } else if (path.path.endsWith(".ttf")) {
            FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(getFileHandle(path));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.color = getGDXColor(font.getColor());
            parameter.size = font.getSize();

            final BitmapFont font2 = fontGenerator.generateFont(parameter);
            font2.getRegion().getTexture().setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Linear, com.badlogic.gdx.graphics.Texture.TextureFilter.Linear);
            loadedFonts.put(searchFont, font2);
            return font2;

        } else {
            throw new IllegalArgumentException("invalid font format: " + path.path.substring(path.path.lastIndexOf(".") + 1));
        }

    }

    @Override
    public void setTint(Color c) {
        this.spriteBatch.setColor(getGDXColor(c));
    }

    private com.badlogic.gdx.graphics.Color getGDXColor(Color c) {
        com.badlogic.gdx.graphics.Color color1 = new com.badlogic.gdx.graphics.Color();
        return color1.set(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
    }

    // // //

    private void prepareBatchesShape() {
        /*isDrawingShapes = shapeRenderer.isDrawing();
        isDrawingSprites = spriteBatch.isDrawing();
        if (! isDrawingShapes) {
            if (isDrawingSprites) {
                spriteBatch.end();
                isDrawingSprites = false;
            }
            shapeRenderer.begin();
            isDrawingShapes = true;
        }*/
    }

    private void prepareBatchesSprite() {
        /*isDrawingShapes = shapeRenderer.isDrawing();
        isDrawingSprites = spriteBatch.isDrawing();
        if (! isDrawingSprites) {
            if (isDrawingShapes) {
                shapeRenderer.end();
                isDrawingShapes = false;
            }
            spriteBatch.begin();
            isDrawingSprites = true;
        }*/
    }

}