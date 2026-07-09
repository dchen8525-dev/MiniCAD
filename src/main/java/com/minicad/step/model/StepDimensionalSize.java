package com.minicad.step.model.technical.tolerance;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved DIMENSIONAL_SIZE.
 * A dimensional size of a shape aspect.
 *
 * @param id STEP instance id
 * @param name size name
 * @param description size description
 * @param ofShape shape aspect being measured
 */
/**
 * Resolved DIMENSIONAL_SIZE.
 * A dimensional size of a shape aspect.
 *
 * @param id STEP instance id
 * @param name size name
 * @param description size description
 * @param ofShape shape aspect being measured
 */
public final class StepDimensionalSize implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity ofShape;

    public StepDimensionalSize(int id, String name, String description, StepEntity ofShape) {
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
        StepDimensionalSize that = (StepDimensionalSize) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(ofShape, that.ofShape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ofShape);
    }

    @Override
    public String toString() {
        return "StepDimensionalSize{" + "id=" + id + "name=" + name + "description=" + description + "ofShape=" + ofShape + "}";
    }
}
