package me.benjozork.opengui.render.object.resource;

/**
 * @author Benjozork
 */
public class InternalPath extends Path {

    public InternalPath(String path) {
        this.path = path;
    }

    public Path cpy() {
        InternalPath path = new InternalPath("");
        path.path = this.path;
        return path;
    }

}