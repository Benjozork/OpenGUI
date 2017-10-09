package me.benjozork.opengui.serialization.loaders;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import me.benjozork.opengui.ui.Context;
import me.benjozork.opengui.ui.Element;
import me.benjozork.opengui.ui.Event;
import me.benjozork.opengui.ui.Layout;
import me.benjozork.opengui.ui.annotation.Listener;

/**
 * Loads a layout of {@link Element} objects.
 *
 * @author Benjozork
 */
public class LayoutDeserializer implements JsonDeserializer<Layout> {

    private Context context;

    public LayoutDeserializer(Context context) {
        this.context = context;
    }

    @Override
    public Layout deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {

        // Listener

        String listenerClassName = jsonElement.getAsJsonObject().get("listenerClass").getAsString();
        Class listenerClass = null;

        try {
            listenerClass = Class.forName(listenerClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }

        Object listenerInstance = null;

        try {
            listenerInstance = listenerClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Layout layoutInstance = new Layout(context, null, listenerInstance);

        // Element root

        ArrayList<Element> root = new ArrayList<Element>();

        JsonArray rootArray = jsonElement.getAsJsonObject().get("root").getAsJsonArray();

        ElementDeserializer elementDeserializer = new ElementDeserializer(context, null);

        for (JsonElement elementData : rootArray) {
            Element element = elementDeserializer.deserialize(elementData, Element.class, deserializationContext);
            element.setLayout(layoutInstance);
            root.add(element);
        }

        layoutInstance.setRoot(root);

        // Listener methods

        HashMap<HashMap<Element, Event>, Method> listenerMethods = new HashMap<HashMap<Element, Event>, Method>();

        for (Method method : listenerClass.getDeclaredMethods()) {

            final Listener annotation = method.getAnnotation(Listener.class);

            if (annotation != null) {
                final String elementName = annotation.element();

                for (Element element : layoutInstance.getRoot()) {
                    if (element.getName().equals(elementName)) {
                        HashMap<Element, Event> keyMap = new HashMap<Element, Event>();
                        keyMap.put(element, annotation.event());
                        listenerMethods.put(keyMap, method);
                    }
                }
            }

        }

        layoutInstance.setEventMethodMap(listenerMethods);

        return layoutInstance;

    }

}