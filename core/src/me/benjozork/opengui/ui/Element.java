package me.benjozork.opengui.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import me.benjozork.opengui.render.Backend;
import me.benjozork.opengui.render.object.Buttons;
import me.benjozork.opengui.render.object.Vector2;
import me.benjozork.opengui.ui.controls.Button;

/**
 * @author Benjozork
 */
public abstract class Element {

    private String name;

    private ArrayList<Element> children;

    private transient Context context;

    private transient Layout layout;

    private transient Skin skin;

    private transient Element parent;

    private Vector2 relativeOrigin;

    private int x;
    private int y;

    protected int width;
    protected int height;

    protected boolean disabled;

    private Anchor anchor;

    private Pair<Element, Anchor> snap;

    private ArrayList<Stretch> stretches;

    public Element() {
        this.children = new ArrayList<Element>();
        this.relativeOrigin = new Vector2(0, 0);
        this.stretches = new ArrayList<Stretch>();
    }

    public List<Element> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void addChild(Element e) {
        children.add(e);
        e.setParent(this);
    }

    public void removeChild(Element e) {
        if (! children.remove(e)) throw new IllegalArgumentException("element \"" + e.getName() + "\" not a child of \"" + this.getName() + "\".");
    }

    public abstract void init();

    public void update() {
        stretchTo((Stretch[]) stretches.toArray(new Stretch[]{}));
        if (anchor != null) anchorTo(anchor);
        if (snap != null) snapTo(snap.getKey(), snap.getValue());

        for (Element element : children) element.update();
    }

    public void draw() {
        for (Element element : children) element.draw();
    }

    public boolean callEvent(Event event) {

        final HashMap<HashMap<Element, Event>, Method> map = this.getLayout().getEventMethodMap();

        final HashMap<Element, Event> keyMap = new HashMap<Element, Event>();

        keyMap.put(this, event);

        try {
            if (map.containsKey(keyMap)) map.get(keyMap).invoke(layout.getListener(), this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }

        return false;

    }

    public void propagateClickAction(Vector2 mousePos) {
        for (Element child : this.getChildren()) {
            if (child.isBeingHovered()) child.propagateClickAction(mousePos);
        }
        this.onClick(mousePos);
        this.callEvent(Event.CLICKED);
    }

    public abstract boolean isBeingHovered();

    public abstract boolean onClick(Vector2 mousePos);

    public int getAbsX() {
        int absX = getRelX();

        if (hasParent()) {
            Element parent = this.getParent();
            do {
                absX += parent.getRelX();
            } while (parent.hasParent());
        }
        absX += relativeOrigin.x;

        return absX;
    }

    public int getAbsY() {
        int absY = getRelY();

        if (hasParent()) {
            Element parent = this.getParent();
            do {
                absY += parent.getRelY();
            } while (parent.hasParent());
        }
        absY += relativeOrigin.y;

        return absY;
    }

    public int getRelX() {
        return x;
    }

    public void setRelX(int x) {
        this.x = x;
    }

    public int getRelY() {
        return y;
    }

    public void setRelY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector2 getRelativeOrigin() {
        return relativeOrigin;
    }

    public void setRelativeOrigin(Vector2 relativeOrigin) {
        this.relativeOrigin = relativeOrigin;
    }

    public void setRelativeOrigin(float x, float y) {
        this.relativeOrigin.set(x, y);
    }

    public boolean hasParent() {
        return parent != null;
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    /**
     * Anchors the element to the speicifed anchor in the parent container
     *
     * @param anchor the {@link Anchor} value used to position the element
     */
    public void anchorTo(Anchor anchor) {
        this.anchor = anchor;
        switch (anchor) {
            case CENTER:
                this.setRelativeOrigin(parent.getWidth() / 2 - getWidth() / 2, parent.getHeight() / 2 - getHeight() / 2);
                break;
            case TOP_LEFT:
                this.setRelativeOrigin(0, parent.getHeight() - getHeight());
                break;
            case TOP:
                this.setRelativeOrigin(parent.getWidth() / 2 - getWidth() / 2, parent.getHeight() - getHeight());
                break;
            case TOP_RIGHT:
                this.setRelativeOrigin(parent.getWidth() - getWidth(), parent.getHeight() - getHeight());
                break;
            case RIGHT:
                this.setRelativeOrigin(parent.getWidth() - getWidth(), parent.getHeight() / 2 - getHeight() / 2);
                break;
            case BOTTOM_RIGHT:
                this.setRelativeOrigin(parent.getWidth() - getWidth(), 0);
                break;
            case BOTTOM:
                this.setRelativeOrigin(parent.getWidth() / 2 - getWidth() / 2, 0);
                break;
            case BOTTOM_LEFT:
                this.setRelativeOrigin(0, 0);
                this.setRelX(0);
                this.setRelX(0);
                break;
            case LEFT:
                this.setRelativeOrigin(0, parent.getHeight() / 2 - getHeight() / 2);
                break;
        }
    }

    /**
     * Snaps the element to another element, using the provided anchor
     *
     * @param target the element to which this should be snapped to
     * @param anchor the anchor to use for the snapping
     */
    public void snapTo(Element target, Anchor anchor) {
        this.snap = new Pair<Element, Anchor>(target, anchor);
        switch (anchor) {
            case CENTER:
                throw new IllegalArgumentException("cannot use center anchor for snapping");
            case TOP_LEFT:
                this.setRelativeOrigin(target.getAbsX(), target.getAbsY() + target.getHeight());
                break;
            case TOP:
                this.setRelativeOrigin(target.getAbsX() + target.getWidth() / 2 - getWidth() / 2, target.getAbsY() + target.getHeight());
                break;
            case TOP_RIGHT:
                this.setRelativeOrigin(target.getAbsX() + target.getWidth() - getWidth(), target.getAbsY() + target.getHeight());
                break;
            case RIGHT:
                this.setRelativeOrigin(target.getAbsX() + target.getWidth(), target.getAbsY() + target.getHeight() / 2 - getHeight() / 2);
                break;
            case BOTTOM_RIGHT:
                this.setRelativeOrigin(target.getAbsX() + target.getWidth() - getWidth(), target.getAbsY() - getHeight() / 2);
                break;
            case BOTTOM:
                this.setRelativeOrigin(target.getAbsX() + target.getWidth() / 2 - getWidth() / 2, target.getAbsY() - getHeight());
                break;
            case BOTTOM_LEFT:
                this.setRelativeOrigin(target.getAbsX(), target.getAbsY() - getHeight());
                break;
            case LEFT:
                this.setRelativeOrigin(target.getAbsX() - getWidth(), target.getAbsY() + target.getWidth() / 2 - getWidth() / 2);
                break;
        }
    }

    /**
     * Stretches the element in the given directions
     *
     * @param stretches the directions to stretch the element towards
     */
    public void stretchTo(Stretch... stretches) {
        for (Stretch stretch : stretches) {
            if (! this.stretches.contains(stretch)) this.stretches.add(stretch);
            int dx, dy;
            switch (stretch) {
                case TOP:
                    dy = (parent.getAbsY() + parent.getHeight()) - (getAbsY() + getHeight());
                    setHeight(getHeight() + dy);
                    break;
                case RIGHT:
                    dx = parent.getWidth() - (getRelX() + getWidth());
                    setWidth(getWidth() + dx);
                    break;
                case BOTTOM:
                    dy = (getAbsY() + getHeight()) - (parent.getAbsY() + parent.getHeight());
                    setRelY(getRelY() - dy);
                    setHeight(getHeight() + dy);
                    break;
                case LEFT:
                    dx = (getAbsX() + getWidth()) - (parent.getAbsY() + parent.getWidth());
                    setRelX(getRelX() - dx);
                    setWidth(getWidth() + dx);
                    break;
                case WIDTH:
                    setRelX(0);
                    setWidth(parent.getWidth());
                case HEIGHT:
                    setRelY(0);
                    setHeight(parent.getHeight());
                case ALL:
                    setRelX(0);
                    setRelY(0);
                    setWidth(parent.getWidth());
                    setHeight(parent.getHeight());
            }
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Backend getBackend() {
        return getContext().getBackend();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        for (Element child : children) {
            child.setLayout(layout);
        }
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

}