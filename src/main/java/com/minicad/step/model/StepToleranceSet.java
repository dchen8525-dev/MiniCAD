package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TOLERANCE_SET.
 * A tolerance set entity containing multiple tolerances.
 *
 * @param id STEP instance id
 * @param name set name
 * @param tolerances list of geometric tolerances
 * @param toleranceContext tolerance context reference
 * @param appliedTo geometry the tolerances apply to
 */
public final class StepToleranceSet extends AbstractStepEntity {
    private final List<StepEntity> tolerances;
    private final StepEntity toleranceContext;
    private final StepEntity appliedTo;

    public StepToleranceSet(int id, String name, List<StepEntity> tolerances, StepEntity toleranceContext, StepEntity appliedTo) {
        super(id, name);
        this.tolerances = tolerances == null ? null : java.util.List.copyOf(tolerances);
        this.toleranceContext = toleranceContext;
        this.appliedTo = appliedTo;
    }

    public List<StepEntity> getTolerances() {
        return tolerances;
    }

    public StepEntity getToleranceContext() {
        return toleranceContext;
    }

    public StepEntity getAppliedTo() {
        return appliedTo;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("tolerances", tolerances);
        state.put("toleranceContext", toleranceContext);
        state.put("appliedTo", appliedTo);
        return state;
    }
}
