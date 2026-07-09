package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

/**
 * Resolved FILL_AREA_STYLE_HATCHING.
 */
/**
 * Resolved FILL_AREA_STYLE_HATCHING.
 */
public final class StepFillAreaStyleHatching implements StepEntity {
    private final int id;
    private final String name;
    private final double angle;
    private final double spacing;

    public StepFillAreaStyleHatching(int id, String name, double angle, double spacing) {
        this.id = id;
        this.name = name;
        this.angle = angle;
        this.spacing = spacing;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAngle() {
        return angle;
    }

    public double getSpacing() {
        return spacing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFillAreaStyleHatching that = (StepFillAreaStyleHatching) o;
        return id == that.id && Objects.equals(name, that.name) && angle == that.angle && spacing == that.spacing;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, angle, spacing);
    }

    @Override
    public String toString() {
        return "StepFillAreaStyleHatching{" + "id=" + id + "name=" + name + "angle=" + angle + "spacing=" + spacing + "}";
    }
}
