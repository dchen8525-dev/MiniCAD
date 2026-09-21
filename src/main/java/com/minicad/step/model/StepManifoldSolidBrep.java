package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved MANIFOLD_SOLID_BREP.
 *
 * @param id step id
 * @param name step label
 * @param outer referenced closed shell
 */
public final class StepManifoldSolidBrep extends AbstractStepEntity {
    private final StepEntity outer;

    public StepManifoldSolidBrep(int id, String name, StepEntity outer) {
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

    // Record-style accessors for compatibility
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity outer() { return getOuter(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("outer", outer);
        return state;
    }
}
