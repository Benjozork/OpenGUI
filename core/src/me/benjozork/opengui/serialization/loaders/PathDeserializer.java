package me.benjozork.opengui.serialization.loaders;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.benjozork.opengui.render.object.resource.ExternalPath;
import me.benjozork.opengui.render.object.resource.InternalPath;
import me.benjozork.opengui.render.object.resource.Path;

/**
 * Deserializes a {@link Path} object using a single {@link String}.
 *
 * @author Benjozork
 */
public class PathDeserializer implements JsonDeserializer<Path> {

    @Override
    public Path deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String data = jsonElement.getAsString();

        if (! data.contains("://")) throw new IllegalArgumentException("missing path type");
        String pathType = data.substring(0, data.indexOf("://"));
         if (pathType.equals("internal")) return new InternalPath(data.replace("internal://", ""));
        else if (pathType.equals("external")) return new ExternalPath(data.replace("external://", ""));
        else throw new IllegalArgumentException("invalid path type: " + pathType);
    }

}