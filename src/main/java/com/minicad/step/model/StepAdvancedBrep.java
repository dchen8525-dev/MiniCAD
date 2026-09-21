package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ADVANCED_BREP.
 * An advanced boundary representation with voids.
 *
 * @param id STEP instance id
 * @param name B-rep name
 * @param outer outer shell
 * @param voids list of void shells
 */
public final class StepAdvancedBrep extends AbstractStepEntity {
    private final StepEntity outer;
    private final List<StepEntity> voids;

    public StepAdvancedBrep(int id, String name, StepEntity outer, List<StepEntity> voids) {
        super(id, name);
        this.outer = outer;
        this.voids = voids == null ? null : java.util.List.copyOf(voids);
    }

    public StepEntity getOuter() {
        return outer;
    }

    // Java Bean style alias for outer
    public StepEntity isOuter() {
        return outer;
    }

    public List<StepEntity> getVoids() {
        return voids;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity outer() { return getOuter(); }
    public List<StepEntity> voids() { return getVoids(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("outer", outer);
        state.put("voids", voids);
        return state;
    }
}
