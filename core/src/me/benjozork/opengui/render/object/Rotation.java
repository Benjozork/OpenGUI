package me.benjozork.opengui.render.object;

/**
 * Describes a rotation applied to an object.<br/>
 * Allows to set rotation angle and relative rotation origin.
 *
 * @author Benjozork
 */
public class Rotation {

    public Rotation(float degrees, float originX, float originY) {
        this.degrees = degrees;
        this.originX = originX;
        this.originY = originY;
    }

    public float degrees;

    public float originX;

    public float originY;

}