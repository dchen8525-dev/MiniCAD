package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DATUM_REFERENCE_COMPARTMENT.
 * A compartment of a datum reference in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name compartment name
 * @param description compartment description
 * @param ofShape product definition shape
 * @param precedence datum precedence
 * @param referencedDatum referenced datum
 */
/**
 * Resolved DATUM_REFERENCE_COMPARTMENT.
 * A compartment of a datum reference in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name compartment name
 * @param description compartment description
 * @param ofShape product definition shape
 * @param precedence datum precedence
 * @param referencedDatum referenced datum
 */
public final class StepDatumReferenceCompartment implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity ofShape;
    private final int precedence;
    private final StepEntity referencedDatum;

    public StepDatumReferenceCompartment(int id, String name, String description, StepEntity ofShape, int precedence, StepEntity referencedDatum) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ofShape = ofShape;
        this.precedence = precedence;
        this.referencedDatum = referencedDatum;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getOfShape() {
        return ofShape;
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
        StepDatumReferenceCompartment that = (StepDatumReferenceCompartment) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(ofShape, that.ofShape) && precedence == that.precedence && Objects.equals(referencedDatum, that.referencedDatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ofShape, precedence, referencedDatum);
    }

    @Override
    public String toString() {
        return "StepDatumReferenceCompartment{" + "id=" + id + "name=" + name + "description=" + description + "ofShape=" + ofShape + "precedence=" + precedence + "referencedDatum=" + referencedDatum + "}";
    }
}
