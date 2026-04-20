package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.unit.StepUncertaintyMeasureWithUnit;

/**
 * Minimal global uncertainty assigned context.
 *
 * @param id STEP instance id
 * @param uncertainties referenced uncertainty entities
 */
public record StepGlobalUncertaintyAssignedContext(
        int id,
        List<StepUncertaintyMeasureWithUnit> uncertainties
) implements StepEntity {

    public StepGlobalUncertaintyAssignedContext {
        uncertainties = List.copyOf(uncertainties);
    }

    @Override
    public String name() {
        return "";
    }
}
