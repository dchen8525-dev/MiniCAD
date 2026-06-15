package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved DATUM.
 * A datum reference used in GD&T.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param description datum description
 * @param target referenced target
 * @param orientation orientation flag
 */
/**
 * Resolved DATUM.
 * A datum reference used in GD&T.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param description datum description
 * @param target referenced target
 * @param orientation orientation flag
 */
public final class StepDatum implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity target;
    private final boolean orientation;

    public StepDatum(int id, String name, String description, StepEntity target, boolean orientation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.target = target;
        this.orientation = orientation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getTarget() {
        return target;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // Record-style accessor
    public StepEntity target() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDatum that = (StepDatum) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(target, that.target) && orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, target, orientation);
    }

    @Override
    public String toString() {
        return "StepDatum{" + "id=" + id + "name=" + name + "description=" + description + "target=" + target + "orientation=" + orientation + "}";
    }
}
