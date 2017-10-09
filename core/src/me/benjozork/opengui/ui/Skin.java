package me.benjozork.opengui.ui;

import java.util.HashMap;

import me.benjozork.opengui.render.object.Font;
import me.benjozork.opengui.render.object.resource.Resource;

/**
 * @author Benjozork
 */
public class Skin {

    private String name;

    private String[] authors;

    private String version;

    private String[] elementClassPackages;

    private Font defaultTextFont;

    private transient HashMap<String, Object> resources = new HashMap<String, Object>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String[] getElementClassPackages() {
        return elementClassPackages;
    }

    public void setElementClassPackages(String[] elementClassPackages) {
        this.elementClassPackages = elementClassPackages;
    }

    public Font getDefaultTextFont() {
        return defaultTextFont;
    }

    public void setDefaultTextFont(Font defaultTextFont) {
        this.defaultTextFont = defaultTextFont;
    }

    public HashMap<String, Object> getResources() {
        return resources;
    }

    public void setResources(HashMap<String, Object> resources) {
        this.resources = resources;
    }

}