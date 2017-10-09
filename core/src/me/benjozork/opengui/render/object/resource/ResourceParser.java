package me.benjozork.opengui.render.object.resource;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.benjozork.opengui.OpenGUI;
import me.benjozork.opengui.ui.Skin;
import me.benjozork.opengui.utils.Log;

/**
 * Used to parse a {@link Resource} object from a JSON element.
 *
 * @author Benjozork
 */
public class ResourceParser {

    private static final Log log = Log.create("ResourceParser");

    /**
     * Used to parse a {@link Resource} object from a JSON element.
     *
     * @param jsonElement the JSON element to get data from
     * @param skinPrunedPath the base directory of the {@link Skin}
     * @param resourceName the name of the resource being parsed (debugging purposes)
     * @param elementClass the name of the element being processed (debugging purposes)
     *
     * @return a {@link Resource} object
     */
    public static Resource parse(JsonElement jsonElement, Path skinPrunedPath, String resourceName, Class elementClass) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String type = jsonObject.get("type").getAsString();

        Class typeClass;

        try {
            typeClass = Class.forName("me.benjozork.opengui.render.object.resource." + type);
        } catch (ClassNotFoundException e) {
            log.error("Invalid resource class name \"" + type + "\" for resource \"" + resourceName + "\" of element \"" + elementClass.getSimpleName() + "\".");
            return null;
        } catch (NoClassDefFoundError e) {
            log.error("Invalid resource class name \"" + type + "\" for resource \"" + resourceName + "\" of element \"" + elementClass.getSimpleName() + "\".");
            return null;
        }


        if (Resource.class.isAssignableFrom(typeClass)) {
            jsonObject.remove("type");
            Resource resource = (Resource) OpenGUI.getGson().fromJson(jsonElement, typeClass);
            resource.path = skinPrunedPath.cpy();
            resource.path.path += "/" + elementClass.getSimpleName().toLowerCase() + "/" + jsonObject.get("path").getAsString();
            return resource;
        } else {
            log.error("Resource type class \"" + type + "\" must extends Resource.");
            return null;
        }

    }

}