package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TOLERANCE_MODIFIER.
 * A tolerance modifier entity.
 *
 * @param id STEP instance id
 * @param name modifier name
 * @param modifierType modifier type (M, L, S, etc.)
 * @param modifierValue modifier value if applicable
 * @param appliedTolerance tolerance the modifier applies to
 */
public final class StepToleranceModifier extends AbstractStepEntity {
    private final String modifierType;
    private final double modifierValue;
    private final StepEntity appliedTolerance;

    public StepToleranceModifier(int id, String name, String modifierType, double modifierValue, StepEntity appliedTolerance) {
        super(id, name);
        this.modifierType = modifierType;
        this.modifierValue = modifierValue;
        this.appliedTolerance = appliedTolerance;
    }

    public String getModifierType() {
        return modifierType;
    }

    public double getModifierValue() {
        return modifierValue;
    }

    public StepEntity getAppliedTolerance() {
        return appliedTolerance;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("modifierType", modifierType);
        state.put("modifierValue", modifierValue);
        state.put("appliedTolerance", appliedTolerance);
        return state;
    }
}
