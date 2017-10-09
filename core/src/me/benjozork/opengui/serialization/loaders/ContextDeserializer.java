package me.benjozork.opengui.serialization.loaders;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

import me.benjozork.opengui.render.Backend;
import me.benjozork.opengui.render.object.resource.Path;
import me.benjozork.opengui.serialization.loaders.skin.SkinDeserializer;
import me.benjozork.opengui.ui.Context;
import me.benjozork.opengui.ui.Skin;

/**
 * @author Benjozork
 */
public class ContextDeserializer implements JsonDeserializer<Context> {

    @Override
    public Context deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {

        String backendClassName = jsonElement.getAsJsonObject().get("backendClass").getAsString();
        Class<? extends Backend> backendClass;

        try {
            backendClass = (Class<? extends Backend>) Class.forName(backendClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            return null;
        }

        Backend backendInstance = null;

        try {
            backendInstance = backendClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        JsonElement skinPathData = jsonElement.getAsJsonObject().get("skinPath");
        Path skinPath = deserializationContext.deserialize(skinPathData, Path.class);

        SkinDeserializer skinDeserializer = new SkinDeserializer(backendInstance, skinPath);
        JsonElement skinData = new JsonParser().parse(backendInstance.getReader(skinPath));

        Skin skinInstance = skinDeserializer.deserialize(skinData, Skin.class, deserializationContext);


        return new Context(backendInstance, skinInstance, 800, 600);

    }

}