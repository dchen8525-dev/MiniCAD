package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved CALCULATED_GEOMETRIC_REPRESENTATION_ITEM.
 * A geometric representation item whose values are computed from other geometry.
 */
/**
 * Resolved CALCULATED_GEOMETRIC_REPRESENTATION_ITEM.
 * A geometric representation item whose values are computed from other geometry.
 */
public final class StepCalculatedGeometricRepresentationItem implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity sourceGeometry;

    public StepCalculatedGeometricRepresentationItem(int id, String name, StepEntity sourceGeometry) {
        this.id = id;
        this.name = name;
        this.sourceGeometry = sourceGeometry;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSourceGeometry() {
        return sourceGeometry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCalculatedGeometricRepresentationItem that = (StepCalculatedGeometricRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sourceGeometry, that.sourceGeometry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sourceGeometry);
    }

    @Override
    public String toString() {
        return "StepCalculatedGeometricRepresentationItem{" + "id=" + id + "name=" + name + "sourceGeometry=" + sourceGeometry + "}";
    }
}
