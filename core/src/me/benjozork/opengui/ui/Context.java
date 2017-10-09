package me.benjozork.opengui.ui;

import me.benjozork.opengui.render.Backend;
import me.benjozork.opengui.render.object.resource.Path;

/**
 * A {@link Context} manages the UI of an application. It handles loading<br/>
 * control/container data from JSON, managing layouts, and producing dialogs.
 *
 * @author Benjozork
 */
public class Context {

    private Path skinPath;

    private transient Backend backend;

    private transient Skin skin;

    private transient Layout root;

    private transient int width, height;

    public Context(Backend backend, Skin skin, int width, int height) {
        this.backend = backend;
        this.skin = skin;
        this.width = width;
        this.height = height;
    }

    /**
     * @return the {@link Backend} to used for the UI rendering tasks.
     */
    public Backend getBackend() {
        return backend;
    }

    /**
     * Sets the {@link Backend} to be used for the UI rendering tasks.
     * @param renderer the {@link Backend} to use
     */
    public void setBackend(Backend renderer) {
        this.backend = renderer;
    }

    /**
     * @return the {@link Skin} used to render the UI
     */
    public Skin getSkin() {
        return skin;
    }

    /**
     * Sets the {@link Skin} that is used to render the UI
     * @param skin the {@link Skin} to use
     */
    public void setSkin(Skin skin) {
        this.skin = skin;
    }


    public Layout getRoot() {
        return root;
    }

    public void setRoot(Layout root) {
        this.root = root;
    }

    public Path getSkinPath() {
        return skinPath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}