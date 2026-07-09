package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DATUM_REFERENCE.
 * A datum reference used in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param precedence datum precedence
 * @param referencedDatum referenced shape aspect
 */
/**
 * Resolved DATUM_REFERENCE.
 * A datum reference used in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param precedence datum precedence
 * @param referencedDatum referenced shape aspect
 */
public final class StepDatumReference implements StepEntity {
    private final int id;
    private final String name;
    private final int precedence;
    private final StepEntity referencedDatum;

    public StepDatumReference(int id, String name, int precedence, StepEntity referencedDatum) {
        this.id = id;
        this.name = name;
        this.precedence = precedence;
        this.referencedDatum = referencedDatum;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrecedence() {
        return precedence;
    }

    public StepEntity getReferencedDatum() {
        return referencedDatum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDatumReference that = (StepDatumReference) o;
        return id == that.id && Objects.equals(name, that.name) && precedence == that.precedence && Objects.equals(referencedDatum, that.referencedDatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, precedence, referencedDatum);
    }

    @Override
    public String toString() {
        return "StepDatumReference{" + "id=" + id + "name=" + name + "precedence=" + precedence + "referencedDatum=" + referencedDatum + "}";
    }
}
