package me.benjozork.opengui.serialization.loaders.skin;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import me.benjozork.opengui.OpenGUI;
import me.benjozork.opengui.render.object.Font;
import me.benjozork.opengui.render.object.resource.Path;
import me.benjozork.opengui.render.object.resource.Resource;
import me.benjozork.opengui.serialization.loaders.FontDeserializer;
import me.benjozork.opengui.serialization.loaders.RelativePathDeserializer;
import me.benjozork.opengui.ui.Context;
import me.benjozork.opengui.ui.Element;
import me.benjozork.opengui.ui.Style;
import me.benjozork.opengui.ui.annotation.IgnoreParentClassStyles;
import me.benjozork.opengui.ui.annotation.PartiallySkinnableProperty;
import me.benjozork.opengui.ui.annotation.SkinnableProperty;
import me.benjozork.opengui.utils.Log;

/**
 * Reads an {@link Element} class manifest and extracts the available resources for each {@link Style}<br/>
 * declared in the corresponding {@link Element} class.
 *
 * @author Benjozork
 */
public class ManifestDeserializer {

    private static final Log log = Log.create("ManifestDeserializer");

    private Font defaultTextFont;

    public ManifestDeserializer(Font defaultTextFont) {
        this.defaultTextFont = defaultTextFont;
    }

    public HashMap<String, Object> parseManifest(Path baseSkinPath, Class<? extends Element> elementClass, JsonElement manifestData) {

        log.print("    [ Parsing " + elementClass.getSimpleName() + " resource manifest ]");

        HashMap<String, Object> returnResources = new HashMap<String, Object>();

        // Extract (partially) skinnable fields & resource fields

        ArrayList<Field> resourceFields = new ArrayList<Field>();
        ArrayList<Field> skinnableFields = new ArrayList<Field>();
        ArrayList<Field> partiallySkinnableFields = new ArrayList<Field>();

        for (Field field : elementClass.getFields()) {

            if (Resource.class.isAssignableFrom(field.getType())) {
                resourceFields.add(field);
            } else if (field.getAnnotation(SkinnableProperty.class) != null) {
                skinnableFields.add(field);
            } else if (field.getAnnotation(PartiallySkinnableProperty.class) != null) {
                partiallySkinnableFields.add(field);
            }

        }

        // Extract declared styles from element class

        ArrayList<String> styles = new ArrayList<String>();

        final boolean ignoreSuperclassStyles = elementClass.getAnnotation(IgnoreParentClassStyles.class) != null;

        for (Field field : elementClass.getFields()) {
            if (field.getType().equals(Style.class)) {
                if (!(ignoreSuperclassStyles &&! field.getDeclaringClass().equals(elementClass))) styles.add(field.getName());
            }
        }

        for (String style : styles) {

            if (manifestData.getAsJsonObject().get("styles").getAsJsonObject().has(style)) {

                JsonObject styleData = manifestData.getAsJsonObject().get("styles").getAsJsonObject().get(style).getAsJsonObject();

                // Parse resource fields

                for (Field field : resourceFields) {

                    // Check if field data is present

                    if (styleData.has(field.getName())) {

                        String resourceID = elementClass.getSimpleName().toLowerCase() + "#" + field.getName() + "$" + style;

                        // Extract field value

                        Resource resourceValue = (Resource) OpenGUI.getGson().fromJson(styleData.get(field.getName()), field.getType());

                        Path baseElementPath = baseSkinPath.cpy();
                        baseElementPath.path += "/" + elementClass.getSimpleName().toLowerCase() + "/";

                        resourceValue.path = new GsonBuilder().registerTypeAdapter(Path.class, new RelativePathDeserializer(baseElementPath)).create().fromJson(styleData.get(field.getName()).getAsJsonObject().get("path"), Path.class);

                        // Assign field value

                        returnResources.put(resourceID, resourceValue);

                    } else {
                        log.error("Missing ressource \"" + field.getName() + "\" data for style \"" + style + "\" in \"" + elementClass.getSimpleName() + "\" resource manifest.");
                    }

                }

                // Parse skinnable fields

                for (Field field : skinnableFields) {

                    // Check if field data is present

                    if (styleData.has(field.getName())) {

                        String resourceID = elementClass.getSimpleName().toLowerCase() + "#" + field.getName() + "$" + style;

                        Object value;

                        if (field.getType().equals(Font.class)) {
                            value = new FontDeserializer(defaultTextFont).deserialize(styleData.get(field.getAnnotation(PartiallySkinnableProperty.class).alias()), Font.class, null);
                        } else {
                            value = OpenGUI.getGson().fromJson(styleData.get(field.getName()), field.getType());
                        }

                        // Extract field value

                        returnResources.put(resourceID, value);

                    }

                }

                // Parse partially skinnable fields

                for (Field field : partiallySkinnableFields) {

                    // Check if field data is present

                    if (styleData.has(field.getAnnotation(PartiallySkinnableProperty.class).alias())) {

                        Field toInject;

                        try {
                            toInject = field.getType().getDeclaredField(field.getAnnotation(PartiallySkinnableProperty.class).field());
                        } catch (NoSuchFieldException e) {
                            log.error("Invalid partially skinnable field target \"" + field.getAnnotation(PartiallySkinnableProperty.class).field() + "\" for field type \"" + field.getType().getSimpleName() + "\" in element class \"" + elementClass.getSimpleName() + "\".");
                            continue;
                        }

                        String resourceID = elementClass.getSimpleName().toLowerCase() + "#" + field.getName() + "." + toInject.getName() + "$" + style;

                        Object value;

                        if (toInject.getType().equals(Font.class)) {
                            value = new FontDeserializer(defaultTextFont).deserialize(styleData.get(field.getAnnotation(PartiallySkinnableProperty.class).alias()), Font.class, null);
                        } else {
                            value = OpenGUI.getGson().fromJson(styleData.get(field.getAnnotation(PartiallySkinnableProperty.class).alias()), toInject.getType());
                        }

                        // Extract field value

                        returnResources.put(resourceID, value);

                    }
                }

            } else {

                log.error("Missing style \"" + style + "\" declaration in \"" + elementClass.getSimpleName() + "\" resource manifest.");

            }

        }

        return returnResources;

    }

}