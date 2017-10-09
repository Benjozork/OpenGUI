package me.benjozork.opengui.render.object;

/**
 * Provides an implementation for a two-dimensional vector and<br/>
 * useful operations associated with it.<br/>
 *
 * Uses code from LibGDX by Badlogic Games.
 *
 * @author Benjozork
 * @author badlogicgames@gmail.com
 *
 */
public class Vector2 {

    public float x;
    public float y;

    // Constructors

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    // Modifiers

    public Vector2 set(Vector2 v) {
        x = v.x;
        y = v.y;
        return this;
    }

    public Vector2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 add(Vector2 v) {
        x += v.x; y += v.y;
        return this;
    }

    public Vector2 add(float x, float y) {
        this.x += x; this.y += y;
        return this;
    }

    public Vector2 sub(Vector2 v) {
        x -= v.x; y -= v.y;
        return this;
    }

    public Vector2 sub(float x, float y) {
        this.x -= x; this.y -= y;
        return this;
    }

    public Vector2 mul(Vector2 v) {
        x *= v.x; y *= v.y;
        return this;
    }

    public Vector2 mul(float x, float y) {
        this.x *= x; this.y *= y;
        return this;
    }

    public Vector2 div(Vector2 v) {
        x /= v.x; y /= v.y;
        return this;
    }

    public Vector2 div(float x, float y) {
        this.x /= x; this.y /= y;
        return this;
    }

    public float len() {
        return (float)Math.sqrt(x * x + y * y);
    }

    public Vector2 nor() {
        float len = len();
        if (len != 0) {
            x /= len;
            y /= len;
        }
        return this;
    }

    public float dot(Vector2 v) {
        return x * v.x + y * v.y;
    }

    public float dot(float ox, float oy) {
        return x * ox + y * oy;
    }

    public Vector2 scl (float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public Vector2 scl(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vector2 scl(Vector2 v) {
        this.x *= v.x;
        this.y *= v.y;
        return this;
    }

    public Vector2 mulAdd(Vector2 vec, float scalar) {
        this.x += vec.x * scalar;
        this.y += vec.y * scalar;
        return this;
    }

    public Vector2 mulAdd(Vector2 vec, Vector2 mulVec) {
        this.x += vec.x * mulVec.x;
        this.y += vec.y * mulVec.y;
        return this;
    }

    public float dst(Vector2 v) {
        final float x_d = v.x - x;
        final float y_d = v.y - y;
        return (float)Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public float dst (float x, float y) {
        final float x_d = x - this.x;
        final float y_d = y - this.y;
        return (float)Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public Vector2 cpy() {
        return new Vector2(this);
    }

}