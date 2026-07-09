package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMMON_DATUM.
 * A datum established from two or more datum features.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param description datum description
 * @param ofShape product definition shape
 * @param constituentDatums constituent datum references
 */
/**
 * Resolved COMMON_DATUM.
 * A datum established from two or more datum features.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param description datum description
 * @param ofShape product definition shape
 * @param constituentDatums constituent datum references
 */
public final class StepCommonDatum implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity ofShape;
    private final List<StepEntity> constituentDatums;

    public StepCommonDatum(int id, String name, String description, StepEntity ofShape, List<StepEntity> constituentDatums) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ofShape = ofShape;
        this.constituentDatums = constituentDatums == null ? null : java.util.List.copyOf(constituentDatums);
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

    public List<StepEntity> getConstituentDatums() {
        return constituentDatums;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCommonDatum that = (StepCommonDatum) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(ofShape, that.ofShape) && Objects.equals(constituentDatums, that.constituentDatums);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ofShape, constituentDatums);
    }

    @Override
    public String toString() {
        return "StepCommonDatum{" + "id=" + id + "name=" + name + "description=" + description + "ofShape=" + ofShape + "constituentDatums=" + constituentDatums + "}";
    }
}
