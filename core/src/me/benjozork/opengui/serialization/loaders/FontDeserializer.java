package me.benjozork.opengui.serialization.loaders;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import me.benjozork.opengui.OpenGUI;
import me.benjozork.opengui.render.object.Font;
import me.benjozork.opengui.utils.Utils;

/**
 * Used to deserialize {@link Font} objects. Replaces any null field value by it's value in the skin's default font.
 *
 * @author Benjozork
 */
public class FontDeserializer implements JsonDeserializer<Font> {

    private Font defaultTextFont;

    public FontDeserializer(Font defaultTextFont) {
        this.defaultTextFont = defaultTextFont;
    }

    @Override
    public Font deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {
        Font baseFont = OpenGUI.getGson().fromJson(jsonElement, Font.class);
        for (Field field : Utils.getInheritedPrivateFields(Font.class)) {
            try {
                field.setAccessible(true);
                if (field.getType().equals(int.class)) {
                    field.set(baseFont, field.get(defaultTextFont));
                    continue;
                }
                if (field.get(baseFont) == null) {
                    field.set(baseFont, field.get(defaultTextFont));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return baseFont;
    }

}