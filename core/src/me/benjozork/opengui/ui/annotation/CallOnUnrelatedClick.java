package me.benjozork.opengui.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies a method that is to be called when the user clicks on something that<br/>
 * is not part of the {@link me.benjozork.opengui.ui.Element} it is defined for.
 *
 * @author Benjozork
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CallOnUnrelatedClick {

}