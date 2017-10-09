package me.benjozork.opengui.ui;

import java.lang.reflect.Method;
import java.util.*;

import me.benjozork.opengui.render.object.Buttons;
import me.benjozork.opengui.ui.annotation.ZIndex;

/**
 * A {@link Layout} is a specific set of customized and configured Elements.</br>
 * One can be appended, prepended or inserted to or into a {@link Context}<br/>
 * or a {@link Container}.
 *
 * @author Benjozork
 */
public class Layout {

    private transient Context context;

    private transient Object listener;

    private transient ArrayList<Element> root = new ArrayList<Element>();

    private transient HashMap<HashMap<Element, Event>, Method> eventMethodMap = new HashMap<HashMap<Element, Event>, Method>();

    public Layout(Context context, ArrayList<Element> root, Object listener) {
        this.context = context;
        this.root = root;
        this.listener = listener;
    }

    public void update() {

        if (this.getContext().getBackend().isButtonJustPressed(Buttons.LEFT)) {
            for (Element child : this.getRoot()) {
                if (child.isBeingHovered()) {
                    child.propagateClickAction(this.getContext().getBackend().getMousePos());
                }
            }
        }

        for (Element element : root) element.update();
    }

    public void draw() {

        // Insert elements with their z-indices in the LinkedHashMap

        LinkedHashMap<Element, Integer> elementsAndZIndices = new LinkedHashMap<Element, Integer>();
        for (Element element : root) {
            try {
                if (element.getClass().getMethod("draw").getAnnotation(ZIndex.class) != null) {
                    elementsAndZIndices.put(element, element.getClass().getMethod("draw").getAnnotation(ZIndex.class).index());
                } else {
                    elementsAndZIndices.put(element, 0);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        // Sort the LinkedHashMap

        List<Map.Entry<Element, Integer>> list = new LinkedList<Map.Entry<Element, Integer>>(elementsAndZIndices.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Element, Integer>>() {
            public int compare(Map.Entry<Element, Integer> o1, Map.Entry<Element, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        elementsAndZIndices.clear();

        for (Map.Entry<Element, Integer> entry : list) {
            elementsAndZIndices.put(entry.getKey(), entry.getValue());
        }

        for (Element element : elementsAndZIndices.keySet()) {
            element.draw();
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * @return a {@link HashSet} of the elements present in this layout.
     */
    public ArrayList<Element> getRoot() {
        return root;
    }

    public void setRoot(ArrayList<Element> root) {
        this.root = root;
    }

    /**
     * @return the object that takes care of UI interaction
     */
    public Object getListener() {
        return listener;
    }

    /**
     * Sets the object that takes care of UI interaction
     * @param listener the object to use
     */
    public void setListener(Object listener) {
        this.listener = listener;
    }

    /**
     * @return a {@link HashMap} that contains a Element + Event key and a Method value. Used for event calling and firing.
     */
    public HashMap<HashMap<Element, Event>, Method> getEventMethodMap() {
        return eventMethodMap;
    }

    /**
     * @param eventMethodMap  a {@link HashMap} that contains a Element + Event key and a Method value. Used for event calling and firing.
     */
    public void setEventMethodMap(HashMap<HashMap<Element, Event>, Method> eventMethodMap) {
        this.eventMethodMap = eventMethodMap;
    }

}