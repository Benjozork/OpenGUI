package me.benjozork.opengui.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks an {@link me.benjozork.opengui.ui.Element} subclass as being<br/>
 * internal; Therefore, {@link me.benjozork.opengui.OpenGUI} will not try to load any resources<br/>
 * for this element while loading a {@link me.benjozork.opengui.ui.Skin}.
 *
 * @author Benjozork
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InternalElement {

}