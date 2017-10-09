package me.benjozork.opengui.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field that can be set to a default value in a skin manifest corresponding to the<br/>
 * element class this annotation applied to.
 *
 * @author Benjozork
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkinnableProperty {

}