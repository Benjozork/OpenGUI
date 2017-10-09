package me.benjozork.opengui.render.object.resource;

/**
 * @author Benjozork
 */
public class RelativePath extends Path {

    @Override
    public Path cpy() {
        RelativePath path = new RelativePath();
        path.path = this.path;
        return path;
    }

}
