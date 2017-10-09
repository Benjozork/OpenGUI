package me.benjozork.opengui.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;

import me.benjozork.opengui.render.object.resource.Resource;

/**
 * @author Benjozork
 */
@Target (ElementType.FIELD)
@Retention (RetentionPolicy.RUNTIME)
public @interface ApplicableResources {

    HashSet<Resource> resources = new HashSet<Resource>();

}