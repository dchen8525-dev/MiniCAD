package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;

import com.minicad.step.model.StepConnectedEdgeSet;
import java.util.Objects;

/**
 * Resolved EDGE_BASED_WIREFRAME_MODEL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param boundaries connected edge sets
 */
/**
 * Resolved EDGE_BASED_WIREFRAME_MODEL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param boundaries connected edge sets
 */
public final class StepEdgeBasedWireframeModel implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepConnectedEdgeSet> boundaries;

    public StepEdgeBasedWireframeModel(int id, String name, List<StepConnectedEdgeSet> boundaries) {
        this.id = id;
        this.name = name;
        this.boundaries = boundaries == null ? null : java.util.List.copyOf(boundaries);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepConnectedEdgeSet> getBoundaries() {
        return boundaries;
    }

    // Record-style accessor
    public List<StepConnectedEdgeSet> boundaries() {
        return boundaries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEdgeBasedWireframeModel that = (StepEdgeBasedWireframeModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(boundaries, that.boundaries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, boundaries);
    }

    @Override
    public String toString() {
        return "StepEdgeBasedWireframeModel{" + "id=" + id + "name=" + name + "boundaries=" + boundaries + "}";
    }
}
