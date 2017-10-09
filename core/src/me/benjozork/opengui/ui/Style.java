package me.benjozork.opengui.ui;

/**
 * @author Benjozork
 */
public class Style {

    private String name;

    public Style(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + name.hashCode();
        return hashCode;
    }

}