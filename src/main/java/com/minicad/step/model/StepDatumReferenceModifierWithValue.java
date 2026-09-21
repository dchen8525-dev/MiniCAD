package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DATUM_REFERENCE_MODIFIER_WITH_VALUE.
 * A datum reference modifier with an associated value (e.g., maximum material condition value).
 */
public final class StepDatumReferenceModifierWithValue extends AbstractStepEntity {
    private final String modifierType;
    private final Double modifierValue;
    private final StepEntity modifierUnit;
    private final StepEntity referencedDatum;

    public StepDatumReferenceModifierWithValue(int id, String name, String modifierType, Double modifierValue, StepEntity modifierUnit, StepEntity referencedDatum) {
        super(id, name);
        this.modifierType = modifierType;
        this.modifierValue = modifierValue;
        this.modifierUnit = modifierUnit;
        this.referencedDatum = referencedDatum;
    }

    public String getModifierType() {
        return modifierType;
    }

    public Double getModifierValue() {
        return modifierValue;
    }

    public StepEntity getModifierUnit() {
        return modifierUnit;
    }

    public StepEntity getReferencedDatum() {
        return referencedDatum;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("modifierType", modifierType);
        state.put("modifierValue", modifierValue);
        state.put("modifierUnit", modifierUnit);
        state.put("referencedDatum", referencedDatum);
        return state;
    }
}
