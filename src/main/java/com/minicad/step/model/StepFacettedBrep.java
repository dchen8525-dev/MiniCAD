package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved FACETTED_BREP.
 * A faceted B-rep defined by a closed shell of planar faces.
 *
 * @param id STEP id
 * @param name STEP label
 * @param outer the outer closed shell
 */
public final class StepFacettedBrep extends AbstractStepEntity {
    private final StepEntity outer;

    public StepFacettedBrep(int id, String name, StepEntity outer) {
        super(id, name);
        this.outer = outer;
    }

    public StepEntity getOuter() {
        return outer;
    }

    // Java Bean style alias for outer
    public StepEntity isOuter() {
        return outer;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity outer() { return getOuter(); }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "outer", outer);
    }
}
