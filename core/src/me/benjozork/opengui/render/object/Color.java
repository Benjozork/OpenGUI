package me.benjozork.opengui.render.object;

/**
 * @author Benjozork
 */
public class Color {

    private float red;
    private float green;
    private float blue;
    private float alpha;

    public Color(float red, float green, float blue, float alpha) {
        this.red = clamp(red, 0, 255);
        this.green = clamp(green, 0, 255);
        this.blue = clamp(blue, 0, 255);
        this.alpha = clamp(alpha, 0, 255);
    }

    public Color (float red, float green, float blue) {
        this.red = clamp(red, 0, 255);
        this.green = clamp(green, 0, 255);
        this.blue = clamp(blue, 0, 255);
        this.alpha = 255;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }

    public void add(Color c) {
        red += c.red; green += c.green; blue += c.blue; alpha += c.alpha;
        autoClamp();
    }

    public void add(float red, float green, float blue, float alpha) {
        this.red += red; this.green += green; this.blue += blue; this.alpha += alpha;
        autoClamp();
    }

    public void add(float red, float green, float blue) {
        this.red += red; this.green += green; this.blue += blue;
        autoClamp();
    }

    public void sub(Color c) {
        red -= c.red; green -= c.green; blue -= c.blue; alpha -= c.alpha;
        autoClamp();
    }

    public void sub(float red, float green, float blue, float alpha) {
        this.red -= red; this.green -= green; this.blue -= blue; this.alpha -= alpha;
        autoClamp();
    }

    public void sub(float red, float green, float blue) {
        this.red -= red; this.green -= green; this.blue -= blue;
        autoClamp();
    }

    public void mul(Color c) {
        this.red *= c.red; this.green *= c.green; this.blue *= c.blue; this.alpha *= c.alpha;
        autoClamp();
    }

    public void mul(float red, float green, float blue, float alpha) {
        this.red *= red; this.green *= green; this.blue *= blue; this.alpha *= alpha;
        autoClamp();
    }

    public void mul(float red, float green, float blue) {
        this.red *= red; this.green *= green; this.blue *= blue;
        autoClamp();
    }

    public void avg(Color c) {
        this.red = ((this.red + c.red) / 2); this.green = ((this.green + c.green) / 2); this.blue = ((this.blue + c.blue) / 2); this.alpha = ((this.alpha + c.alpha) / 2);
        autoClamp();
    }

    public Color cpy() {
        return new Color(red, green, blue, alpha);
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public void autoClamp() {
        this.red = clamp(this.red, 0, 255);
        this.green = clamp(this.green, 0, 255);
        this.blue = clamp(this.blue, 0, 255);
        this.alpha = clamp(this.alpha, 0, 255);
    }

}