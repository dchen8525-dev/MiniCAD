package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FACETTED_BREP.
 * A faceted B-rep defined by a closed shell of planar faces.
 *
 * @param id STEP id
 * @param name STEP label
 * @param outer the outer closed shell
 */
/**
 * Resolved FACETTED_BREP.
 * A faceted B-rep defined by a closed shell of planar faces.
 *
 * @param id STEP id
 * @param name STEP label
 * @param outer the outer closed shell
 */
public final class StepFacettedBrep implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity outer;

    public StepFacettedBrep(int id, String name, StepEntity outer) {
        this.id = id;
        this.name = name;
        this.outer = outer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getOuter() {
        return outer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFacettedBrep that = (StepFacettedBrep) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(outer, that.outer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, outer);
    }

    @Override
    public String toString() {
        return "StepFacettedBrep{" + "id=" + id + "name=" + name + "outer=" + outer + "}";
    }
}
