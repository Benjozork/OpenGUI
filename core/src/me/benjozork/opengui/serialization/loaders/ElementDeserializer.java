package me.benjozork.opengui.serialization.loaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;

import me.benjozork.opengui.render.object.TextComponent;
import me.benjozork.opengui.render.object.resource.Resource;
import me.benjozork.opengui.ui.Context;
import me.benjozork.opengui.ui.Element;
import me.benjozork.opengui.ui.annotation.PartiallySkinnableProperty;
import me.benjozork.opengui.ui.annotation.SkinnableProperty;
import me.benjozork.opengui.utils.Log;
import me.benjozork.opengui.utils.Utils;

/**
 * Deserializes {@link Element} data and assigns values to an {@link Element} object<br/>
 * according to the data present in the JSON data.
 *
 * @author Benjozork
 */
public class ElementDeserializer implements JsonDeserializer<Element> {

    private static final Log log = Log.create("ElementDeserializer");

    private Context context;

    private Element parentParsing;

    public ElementDeserializer(Context context, Element parentParsing) {
        this.context = context;
        this.parentParsing = parentParsing;
    }

    @Override
    public Element deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {

        String elementClassName = jsonElement.getAsJsonObject().get("class").getAsString();
        Class<? extends Element> elementClass = null;

        try {
            elementClass = (Class<? extends Element>) Class.forName(elementClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            return null;
        }

        Element elementInstance = null;

        try {
            elementInstance = elementClass.newInstance();
            elementInstance.setParent(parentParsing);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        elementInstance.setContext(context);

        for (Field field : Utils.getInheritedPrivateFields(elementClass)) {

            final JsonElement jsonData = jsonElement.getAsJsonObject().get(field.getName());

            if (Modifier.isTransient(field.getModifiers()) || jsonData == null || Resource.class.isAssignableFrom(field.getType())) continue;

            Object value = deserializationContext.deserialize(jsonData, field.getType());
            try {
                field.setAccessible(true);
                if (field.getName().equals("children")) {
                    Type elementListType = new TypeToken<List<Element>>(){}.getType();
                    Gson childrenGson = new GsonBuilder()
                            .registerTypeAdapter(Element.class, new ElementDeserializer(context, elementInstance))
                            .registerTypeAdapter(TextComponent.class, new TextComponentDeserializer(context))
                            .create();
                    List<Element> elementList = childrenGson.fromJson(jsonData, elementListType);
                    field.set(elementInstance, elementList);
                }
                else field.set(elementInstance, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Field field : Utils.getInheritedPrivateFields(elementClass)) {
            if (Resource.class.isAssignableFrom(field.getType())
                    || field.getAnnotation(SkinnableProperty.class) != null
                    || field.getAnnotation(PartiallySkinnableProperty.class) != null) {

                String styleName = "normal";

                if (jsonElement.getAsJsonObject().has("style")) {
                    // todo validate style and find fallback if necessary
                    styleName = jsonElement.getAsJsonObject().get("style").getAsString();
                } else {
                    // todo autofield logic
                }

                if (field.getAnnotation(PartiallySkinnableProperty.class) != null) {

                    final String toInjectName = field.getAnnotation(PartiallySkinnableProperty.class).field();
                    try {
                        if (field.get(elementInstance) != null) {
                            Field toInject = field.getType().getDeclaredField(toInjectName);
                            toInject.setAccessible(true);
                            final String resourceIdentifier = elementClass.getSimpleName().toLowerCase() + "#" + field.getName() + "." + toInject.getName() + "$" + styleName;
                            if (this.context.getSkin().getResources().containsKey(resourceIdentifier)) {
                                toInject.set(field.get(elementInstance), this.context.getSkin().getResources().get(resourceIdentifier));
                                continue;
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        log.error("Invalid partially skinned field name \"" + toInjectName + "\" for class \"" + field.getType().getSimpleName() + "\" of element class \"" + elementClass.getSimpleName() + "\".");
                    }

                } else if (field.getAnnotation(SkinnableProperty.class) != null) {

                    String resourceIdentifier = elementClass.getSimpleName().toLowerCase() + "#" + field.getName() + "$" + styleName;

                    if (context.getSkin().getResources().containsKey(resourceIdentifier)) {
                        try {
                            field.set(elementInstance, context.getSkin().getResources().get(resourceIdentifier));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (Resource.class.isAssignableFrom(field.getType())) {

                    final String resourceIdentifier = field.getDeclaringClass().getSimpleName().toLowerCase() + "#" + field.getName() + "$" + styleName;

                    try {
                        field.set(elementInstance, this.context.getSkin().getResources().get(resourceIdentifier));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        elementInstance.init();

        return elementInstance;

    }

}