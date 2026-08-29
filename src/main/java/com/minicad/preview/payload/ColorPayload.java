package com.minicad.preview.payload;

/**
 * RGB color payload for preview rendering.
 */
public final class ColorPayload {
    private final int red;
    private final int green;
    private final int blue;

    public ColorPayload(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }
    public int getGreen() {
        return green;
    }
    public int getBlue() {
        return blue;
    }

    // Record-style accessors
    public int red() { return red; }
    public int green() { return green; }
    public int blue() { return blue; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorPayload that = (ColorPayload) o;
        return red == that.red && green == that.green && blue == that.blue;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(red, green, blue);
    }

    @Override
    public String toString() {
        return "ColorPayload{red=" + red + ", green=" + green + ", blue=" + blue + "}";
    }
}
