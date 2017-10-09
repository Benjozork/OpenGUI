package me.benjozork.opengui.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Benjozork
 */
@Target (ElementType.FIELD)
@Retention (RetentionPolicy.RUNTIME)
public @interface DefaultStyle {

}