package me.benjozork.opengui.serialization.loaders.skin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import org.reflections.Reflections;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import me.benjozork.opengui.render.Backend;
import me.benjozork.opengui.render.object.Font;
import me.benjozork.opengui.render.object.resource.Path;
import me.benjozork.opengui.serialization.loaders.RelativePathDeserializer;
import me.benjozork.opengui.ui.Element;
import me.benjozork.opengui.ui.Skin;
import me.benjozork.opengui.ui.annotation.InternalElement;
import me.benjozork.opengui.utils.Log;

/**
 * Deserializes {@link Skin} objects by reading a main file and corresponding {@link Element} class manifests.
 *
 * @author Benjozork
 */
public class SkinDeserializer implements JsonDeserializer<Skin> {

    private static final Log log = Log.create("SkinDeserializer");

    private Backend backend;

    private Path mainFilePath;

    public SkinDeserializer(Backend backend, Path mainFilePath) {
        this.backend = backend;
        this.mainFilePath = mainFilePath;
    }

    @Override
    public Skin deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {
        JsonObject skinData = jsonElement.getAsJsonObject();

        // Get the root directory of the skin

        Path prunedPath = mainFilePath.cpy();
        prunedPath.path = prunedPath.path.substring(0, mainFilePath.path.lastIndexOf("/"));

        RelativePathDeserializer relativePathDeserializer = new RelativePathDeserializer(prunedPath.cpy());

        // Set basic skin data

        Skin skin = new Skin();
        skin.setName(skinData.get("name").getAsString());
        skin.setAuthors((String[]) deserializationContext.deserialize(skinData.get("authors"), String[].class));
        skin.setVersion(skinData.get("version").getAsString());
        skin.setElementClassPackages((String[]) deserializationContext.deserialize(skinData.get("elementClassPackages"), String[].class));

        Gson gson = new GsonBuilder().registerTypeAdapter(Path.class, relativePathDeserializer).create();
        skin.setDefaultTextFont(gson.fromJson(skinData.get("defaultTextFont"), Font.class));

        // Retrieve element classes supported by the skin

        ArrayList<Class<? extends Element>> elementClasses = new ArrayList<Class<? extends Element>>();

        for (String s : skin.getElementClassPackages()) {
            Reflections reflections = new Reflections(s);
            for (Class<? extends Element> candidateClass : reflections.getSubTypesOf(Element.class)) {
                if (candidateClass.getAnnotation(InternalElement.class) == null) elementClasses.add(candidateClass);
            }
        }

        // Retrieve resources for each supported element class

        ArrayList<Class> parsedClasses = new ArrayList<Class>();

        nextElement:
        for (Class c : elementClasses) {

            // Find resource manifest

            Path resourceManifestPath = prunedPath.cpy();
            resourceManifestPath.path += "/" + c.getSimpleName().toLowerCase() + "/" + c.getSimpleName().toLowerCase() + ".json";

            JsonObject resourceManifestData = null;

            Reader reader = null;

            try {

                // Read manifest

                reader = backend.getReader(resourceManifestPath);

            } catch (Exception e) {

                resourceManifestData = findAlternateManifest(c, backend, prunedPath);

                if (resourceManifestData == null) {
                    log.error("Missing resource manifest for element class \"" + c.getSimpleName().toLowerCase() + "\" in skin \"" + skin.getName() + " " + skin.getVersion() + "\".");
                    continue;
                }
            }

            if (reader != null) resourceManifestData = new JsonParser().parse(reader).getAsJsonObject();

            if (resourceManifestData.has("alternateSuperclass")) try {
                c = Class.forName(resourceManifestData.get("alternateSuperclass").getAsString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoClassDefFoundError e) {
                e.printStackTrace();
            }

            if (! parsedClasses.contains(c)) {
                HashMap<String, Object> finalResources = new ManifestDeserializer(skin.getDefaultTextFont()).parseManifest(prunedPath, c, resourceManifestData);
                parsedClasses.add(c);

                // Add resources to skin

                skin.getResources().putAll(finalResources);
            }

        }

        return skin;

    }

    private static JsonObject findAlternateManifest(Class<? extends Element> clazz, Backend backend, Path baseSkinPath) {
        Class superclass = clazz.getSuperclass();
        if (superclass.equals(Object.class) || superclass.getAnnotation(InternalElement.class) != null) return null;

        Path newManifestPath = baseSkinPath.cpy();
        newManifestPath.path += "/" + superclass.getSimpleName().toLowerCase() + "/" + superclass.getSimpleName().toLowerCase() + ".json";

        Reader reader = null;

        try {
            reader = backend.getReader(newManifestPath);
        } catch (Exception e) {
            JsonObject recursiveManifest = findAlternateManifest(superclass, backend, baseSkinPath);
            if (recursiveManifest != null) return recursiveManifest;
        }

        if (reader != null) {
            JsonObject ret = new JsonParser().parse(reader).getAsJsonObject();
            ret.add("alternateSuperclass", new JsonPrimitive(superclass.getName()));
            return ret;
        } else {
            log.error("No alternate resource manifest for element class \"" + clazz.getSimpleName().toLowerCase() + "\" found.");
            return null;
        }

    }

}