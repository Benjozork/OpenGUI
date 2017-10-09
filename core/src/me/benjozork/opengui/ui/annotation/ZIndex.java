package me.benjozork.opengui.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets the z-index of a certain element.<br/>
 * Elements with a higher z-index will be draw first, while lower<br/>
 * indices will be draw last.
 * @author Benjozork
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ZIndex {

    int index();

}