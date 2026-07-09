package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved COLOR_SPECIFICATION.
 * A color specification with RGB or named values.
 *
 * @param id STEP instance id
 * @param name color name
 * @param red red component (0-1)
 * @param green green component (0-1)
 * @param blue blue component (0-1)
 */
/**
 * Resolved COLOR_SPECIFICATION.
 * A color specification with RGB or named values.
 *
 * @param id STEP instance id
 * @param name color name
 * @param red red component (0-1)
 * @param green green component (0-1)
 * @param blue blue component (0-1)
 */
public final class StepColorSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final double red;
    private final double green;
    private final double blue;

    public StepColorSpecification(int id, String name, double red, double green, double blue) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepColorSpecification that = (StepColorSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && red == that.red && green == that.green && blue == that.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, red, green, blue);
    }

    @Override
    public String toString() {
        return "StepColorSpecification{" + "id=" + id + "name=" + name + "red=" + red + "green=" + green + "blue=" + blue + "}";
    }
}
