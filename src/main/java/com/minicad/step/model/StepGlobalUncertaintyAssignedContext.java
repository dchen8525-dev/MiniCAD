package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal global uncertainty assigned context.
 *
 * @param id STEP instance id
 * @param uncertainties referenced uncertainty entities
 */
public final class StepGlobalUncertaintyAssignedContext extends AbstractStepEntity {
    private final List<StepUncertaintyMeasureWithUnit> uncertainties;

    public StepGlobalUncertaintyAssignedContext(int id, List<StepUncertaintyMeasureWithUnit> uncertainties) {
        super(id, "");
        this.uncertainties = uncertainties == null ? null : java.util.List.copyOf(uncertainties);
    }

    public List<StepUncertaintyMeasureWithUnit> getUncertainties() {
        return uncertainties;
    }

    // Record-style accessors
    public List<StepUncertaintyMeasureWithUnit> uncertainties() { return getUncertainties(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("uncertainties", uncertainties);
        return state;
    }
}
