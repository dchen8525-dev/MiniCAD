package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;
/**
 * Resolved REPRESENTATION_CONTEXT_3D.
 * A 3D representation context.
 */
/**
 * Resolved REPRESENTATION_CONTEXT_3D.
 * A 3D representation context.
 */
public final class StepRepresentationContext3d implements StepEntity {
    private final int id;
    private final String name;
    private final String contextType;
    private final List<Double> coordinateSpaceDimensions;

    public StepRepresentationContext3d(int id, String name, String contextType, List<Double> coordinateSpaceDimensions) {
        this.id = id;
        this.name = name;
        this.contextType = contextType;
        this.coordinateSpaceDimensions = coordinateSpaceDimensions == null ? null : java.util.List.copyOf(coordinateSpaceDimensions);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContextType() {
        return contextType;
    }

    public List<Double> getCoordinateSpaceDimensions() {
        return coordinateSpaceDimensions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRepresentationContext3d that = (StepRepresentationContext3d) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(contextType, that.contextType) && Objects.equals(coordinateSpaceDimensions, that.coordinateSpaceDimensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contextType, coordinateSpaceDimensions);
    }

    @Override
    public String toString() {
        return "StepRepresentationContext3d{" + "id=" + id + "name=" + name + "contextType=" + contextType + "coordinateSpaceDimensions=" + coordinateSpaceDimensions + "}";
    }
}
