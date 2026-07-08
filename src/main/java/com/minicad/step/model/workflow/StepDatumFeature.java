package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved DATUM_FEATURE.
 * A datum feature used for geometric dimensioning and tolerancing.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param description feature description
 * @param ofShape product definition shape
 */
/**
 * Resolved DATUM_FEATURE.
 * A datum feature used for geometric dimensioning and tolerancing.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param description feature description
 * @param ofShape product definition shape
 */
public final class StepDatumFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity ofShape;

    public StepDatumFeature(int id, String name, String description, StepEntity ofShape) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ofShape = ofShape;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDatumFeature that = (StepDatumFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(ofShape, that.ofShape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ofShape);
    }

    @Override
    public String toString() {
        return "StepDatumFeature{" + "id=" + id + "name=" + name + "description=" + description + "ofShape=" + ofShape + "}";
    }
}
