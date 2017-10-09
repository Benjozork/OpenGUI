package me.benjozork.opengui.render.object.resource;

/**
 * @author Benjozork
 */
public class ExternalPath extends Path {

    public ExternalPath(String path) {
        this.path = path;
    }

    public Path cpy() {
        ExternalPath path = new ExternalPath("");
        path.path = this.path;
        return path;
    }

}