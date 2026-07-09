package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved DATUM_REFERENCE_MODIFIER.
 * A modifier applied to a datum reference (e.g., MMB, LMB).
 */
/**
 * Resolved DATUM_REFERENCE_MODIFIER.
 * A modifier applied to a datum reference (e.g., MMB, LMB).
 */
public final class StepDatumReferenceModifier implements StepEntity {
    private final int id;
    private final String name;
    private final String modifierType;
    private final StepEntity referencedDatum;

    public StepDatumReferenceModifier(int id, String name, String modifierType, StepEntity referencedDatum) {
        this.id = id;
        this.name = name;
        this.modifierType = modifierType;
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

    public StepEntity getReferencedDatum() {
        return referencedDatum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDatumReferenceModifier that = (StepDatumReferenceModifier) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(modifierType, that.modifierType) && Objects.equals(referencedDatum, that.referencedDatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modifierType, referencedDatum);
    }

    @Override
    public String toString() {
        return "StepDatumReferenceModifier{" + "id=" + id + "name=" + name + "modifierType=" + modifierType + "referencedDatum=" + referencedDatum + "}";
    }
}
