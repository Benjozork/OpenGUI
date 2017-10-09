package me.benjozork.opengui.render.object;

import me.benjozork.opengui.render.object.resource.Path;

/**
 * @author Benjozork
 */
public class Font {

    private Path path;

    private int size;

    private Color color;

    public Font(Path path, int size, Color color) {
        this.path = path;
        this.size = size;
        this.color = color;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}