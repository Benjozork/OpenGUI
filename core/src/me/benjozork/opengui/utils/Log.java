package me.benjozork.opengui.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Allows to log message to the Java output.<br/>
 * WARNING: Single thread use only.
 * @author angelickite
 */
public class Log {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

    private static Log latestPrintLog;

    private final String tag;

    private Log(String tag) {
        this.tag = tag;
    }

    /**
     * Creates a new logger with the supplied tag
     * @param tag the tag to be used
     * @return a logger
     */
    public static Log create(String tag) {
        return new Log(tag);
    }

    /**
     * Creates a new logger with the supplied object class name as the tag
     * @param obj the object to use to se the tag
     * @return a logger
     */
    public static Log create(Object obj) {
        return new Log(obj.getClass().getSimpleName());
    }

    /**
     * Prints a formatted string
     * @param message the message to be printed
     * @param args the objects to format the message with
     */
    public void print(String message, Object... args) {
        boolean isNewLog = latestPrintLog == null || latestPrintLog != this;
        if (isNewLog) {
            printNewline();
            printInfo();
        }

        latestPrintLog = this;

        printMessage(message, args);
    }

    /**
     * Prints a formatted error message
     * @param message the message to be printed
     * @param args the objects to format the message with
     */
    public void error(String message, Object... args) {
        print("ERROR: " + message, args);
    }

    /**
     * Prints a formatted warning message
     * @param message the message to be printed
     * @param args the objects to format the message with
     */
    public void warn(String message, Object... args) {
        print("WARN: " + message, args);
    }

    /**
     * Prints a formatted fatal message
     * @param message the message to be printed
     * @param args the objects to format the message with
     */
    public void fatal(String message, Object... args) {
        print("FATAL: " + message, args);
    }

    /**
     * Prints a formatted debug message
     * @param message the message to be printed
     * @param args the objects to format the message with
     */
    public void debug(String message, Object... args) {
        print("DEBUG: " + message, args);
    }

    private void printNewline() {
        System.out.println();
    }

    private void printInfo() {
        String date = sdf.format(new Date());
        System.out.printf("[%s] (%s)\n", tag, date);
    }

    private void printMessage(String message, Object... args) {
        System.out.printf("-> %s\n", String.format(message, args));
    }

}