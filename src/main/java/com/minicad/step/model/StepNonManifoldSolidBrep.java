package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved NON_MANIFOLD_SOLID_BREP.
 * A B-rep solid whose boundary may be a non-manifold shell.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param outer the surface (open or closed shell) forming the boundary
 */
public final class StepNonManifoldSolidBrep extends AbstractStepEntity {
    private final StepEntity outer;

    public StepNonManifoldSolidBrep(int id, String name, StepEntity outer) {
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("outer", outer);
        return state;
    }
}
