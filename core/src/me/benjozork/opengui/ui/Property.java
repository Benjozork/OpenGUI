package me.benjozork.opengui.ui;

/**
 * @author Benjozork
 */
public class Property<T> {

    private Element parent;

    private boolean inherited = false;

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setInherited(boolean b) {
        this.inherited = b;
    }

    public boolean isInherited() {
        return inherited;
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

}