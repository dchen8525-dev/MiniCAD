package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal BREP_WITH_VOIDS.
 *
 * @param id step id
 * @param name step label
 * @param outer referenced closed shell
 * @param voids referenced void closed shells
 */
public final class StepBrepWithVoids extends AbstractStepEntity {
    private final StepEntity outer;
    private final List<StepEntity> voids;

    public StepBrepWithVoids(int id, String name, StepEntity outer, List<StepEntity> voids) {
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
