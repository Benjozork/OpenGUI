package me.benjozork.opengui.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.benjozork.opengui.render.object.Color;

/**
 * @author Benjozork
 */
public class Utils {

    /*
     * Color instances used to markup console text.
     * WARN: Warning text. Non-failing problems.
     * ERROR: Error text. Failing problems.
     * FATAL: Critical problem text. Interrupts the game.
     * DEBUG: Debugging text. Technical information used for debugging.
     */
    public static final Color WARN = new Color(255, 255, 100);
    public static final Color ERROR = new Color(255, 100, 100);
    public static final Color FATAL = new Color(255, 65, 65);
    public static final Color DEBUG = new Color(255, 100, 255);

    private static long lastFrameTime;

    public static float delta() {
        float deltaTime;
        long time = System.nanoTime();
        if (lastFrameTime == -1)
            lastFrameTime = time;
        deltaTime = (time - lastFrameTime) / 1000000000.0f;
        lastFrameTime = time;
        return deltaTime;
    }

    public static List<Field> getInheritedPrivateFields(Class<?> type) {
        List<Field> result = new ArrayList<Field>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            Collections.addAll(result, i.getDeclaredFields());
            i = i.getSuperclass();
        }

        return result;
    }

}