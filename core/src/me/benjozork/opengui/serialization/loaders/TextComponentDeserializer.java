package me.benjozork.opengui.serialization.loaders;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.benjozork.opengui.render.object.Font;
import me.benjozork.opengui.render.object.TextComponent;
import me.benjozork.opengui.ui.Context;

/**
 * Deserializes {@link TextComponent} objects using the skin's default font if<br/>
 * an alternative one is not present in the JSON data.
 *
 * @author Benjozork
 */
public class TextComponentDeserializer implements JsonDeserializer<TextComponent> {

    private Context context;

    public TextComponentDeserializer(Context context) {
        this.context = context;
    }

    @Override
    public TextComponent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        TextComponent textComponent = new TextComponent(context, jsonElement.getAsJsonObject().get("text").getAsString());
        if (jsonElement.getAsJsonObject().get("font") != null) {
            textComponent.setFont((Font) jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("font"), Font.class));
        } else {
            textComponent.setFont(this.context.getSkin().getDefaultTextFont());
        }
        return textComponent;
    }

}