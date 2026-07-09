package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DATUM_REFERENCE_ELEMENT.
 * A datum reference element used in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name element name
 * @param description element description
 * @param ofShape product definition shape
 * @param compartments datum reference compartments
 */
/**
 * Resolved DATUM_REFERENCE_ELEMENT.
 * A datum reference element used in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name element name
 * @param description element description
 * @param ofShape product definition shape
 * @param compartments datum reference compartments
 */
public final class StepDatumReferenceElement implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity ofShape;
    private final List<StepEntity> compartments;

    public StepDatumReferenceElement(int id, String name, String description, StepEntity ofShape, List<StepEntity> compartments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ofShape = ofShape;
        this.compartments = compartments == null ? null : java.util.List.copyOf(compartments);
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

    public List<StepEntity> getCompartments() {
        return compartments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDatumReferenceElement that = (StepDatumReferenceElement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(ofShape, that.ofShape) && Objects.equals(compartments, that.compartments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ofShape, compartments);
    }

    @Override
    public String toString() {
        return "StepDatumReferenceElement{" + "id=" + id + "name=" + name + "description=" + description + "ofShape=" + ofShape + "compartments=" + compartments + "}";
    }
}
