package me.benjozork.opengui.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.benjozork.opengui.ui.Element;

/**
 * @author Benjozork
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChildrenClassFilter {

    FilterType type();

    Class<? extends Element>[] classes();

    enum FilterType {

        INCLUDE_ONLY,
        EXCLUDE_ALL

    }

}