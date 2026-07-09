package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved DATUM_REFERENCE_MODIFIER_WITH_VALUE.
 * A datum reference modifier with an associated value (e.g., maximum material condition value).
 */
/**
 * Resolved DATUM_REFERENCE_MODIFIER_WITH_VALUE.
 * A datum reference modifier with an associated value (e.g., maximum material condition value).
 */
public final class StepDatumReferenceModifierWithValue implements StepEntity {
    private final int id;
    private final String name;
    private final String modifierType;
    private final Double modifierValue;
    private final StepEntity modifierUnit;
    private final StepEntity referencedDatum;

    public StepDatumReferenceModifierWithValue(int id, String name, String modifierType, Double modifierValue, StepEntity modifierUnit, StepEntity referencedDatum) {
        this.id = id;
        this.name = name;
        this.modifierType = modifierType;
        this.modifierValue = modifierValue;
        this.modifierUnit = modifierUnit;
        this.referencedDatum = referencedDatum;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDatumReferenceModifierWithValue that = (StepDatumReferenceModifierWithValue) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(modifierType, that.modifierType) && Objects.equals(modifierValue, that.modifierValue) && Objects.equals(modifierUnit, that.modifierUnit) && Objects.equals(referencedDatum, that.referencedDatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modifierType, modifierValue, modifierUnit, referencedDatum);
    }

    @Override
    public String toString() {
        return "StepDatumReferenceModifierWithValue{" + "id=" + id + "name=" + name + "modifierType=" + modifierType + "modifierValue=" + modifierValue + "modifierUnit=" + modifierUnit + "referencedDatum=" + referencedDatum + "}";
    }
}
