package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved DATUM_REFERENCE_MODIFIER.
 * A modifier applied to a datum reference (e.g., MMB, LMB).
 */
public final class StepDatumReferenceModifier extends AbstractStepEntity {
    private final String modifierType;
    private final StepEntity referencedDatum;

    public StepDatumReferenceModifier(int id, String name, String modifierType, StepEntity referencedDatum) {
        super(id, name);
        this.modifierType = modifierType;
        this.referencedDatum = referencedDatum;
    }

    public String getModifierType() {
        return modifierType;
    }

    public StepEntity getReferencedDatum() {
        return referencedDatum;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "modifierType", modifierType,
                "referencedDatum", referencedDatum);
    }
}
