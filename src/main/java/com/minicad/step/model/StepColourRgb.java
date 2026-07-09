package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal RGB colour definition.
 *
 * @param id STEP instance id
 * @param name colour name
 * @param red red channel in [0, 1]
 * @param green green channel in [0, 1]
 * @param blue blue channel in [0, 1]
 */
/**
 * Minimal RGB colour definition.
 *
 * @param id STEP instance id
 * @param name colour name
 * @param red red channel in [0, 1]
 * @param green green channel in [0, 1]
 * @param blue blue channel in [0, 1]
 */
public final class StepColourRgb implements StepEntity {
    private final int id;
    private final String name;
    private final double red;
    private final double green;
    private final double blue;

    public StepColourRgb(int id, String name, double red, double green, double blue) {
        this.id = id;
        this.name = name;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    // Record-style accessors
    public String name() {
        return name;
    }

    public double red() {
        return red;
    }

    public double green() {
        return green;
    }

    public double blue() {
        return blue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepColourRgb that = (StepColourRgb) o;
        return id == that.id && Objects.equals(name, that.name) && red == that.red && green == that.green && blue == that.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, red, green, blue);
    }

    @Override
    public String toString() {
        return "StepColourRgb{" + "id=" + id + "name=" + name + "red=" + red + "green=" + green + "blue=" + blue + "}";
    }
}
