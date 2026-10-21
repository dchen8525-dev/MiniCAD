package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * FACETED_BREP_AND_BREP_WITH_VOIDS (single-T alias variant of FACETTED_BREP_AND_BREP_WITH_VOIDS).
 *
 * <p>A faceted boundary representation with an outer closed shell and
 * optional void (inner) closed shells.</p>
 *
 * @param id   STEP id
 * @param name STEP label
 * @param outer the outer closed shell
 * @param voids referenced void closed shells
 */
public final class StepFacetedBrepAndBrepWithVoids extends AbstractStepEntity {
    private final StepEntity outer;
    private final List<StepEntity> voids;

    public StepFacetedBrepAndBrepWithVoids(int id, String name, StepEntity outer, List<StepEntity> voids) {
        super(id, name);
        this.outer = outer;
        this.voids = voids == null ? null : List.copyOf(voids);
    }

    public StepEntity getOuter() {
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
