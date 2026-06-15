package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.topology.StepOrientedEdge;
import java.util.Objects;

/**
 * Resolved ORIENTED_PATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param pathElement referenced path-like element
 * @param orientation whether the oriented path agrees with the referenced path orientation
 * @param edges derived oriented-edge list
 */
/**
 * Resolved ORIENTED_PATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param pathElement referenced path-like element
 * @param orientation whether the oriented path agrees with the referenced path orientation
 * @param edges derived oriented-edge list
 */
public final class StepOrientedPath implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity pathElement;
    private final boolean orientation;
    private final List<StepOrientedEdge> edges;

    public StepOrientedPath(int id, String name, StepEntity pathElement, boolean orientation, List<StepOrientedEdge> edges) {
        this.id = id;
        this.name = name;
        this.pathElement = pathElement;
        this.orientation = orientation;
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPathElement() {
        return pathElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    public List<StepOrientedEdge> getEdges() {
        return edges;
    }

    // Record-style accessors
    public List<StepOrientedEdge> edges() {
        return edges;
    }

    public boolean orientation() {
        return orientation;
    }

    public StepEntity pathElement() {
        return pathElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrientedPath that = (StepOrientedPath) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(pathElement, that.pathElement) && orientation == that.orientation && Objects.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pathElement, orientation, edges);
    }

    @Override
    public String toString() {
        return "StepOrientedPath{" + "id=" + id + "name=" + name + "pathElement=" + pathElement + "orientation=" + orientation + "edges=" + edges + "}";
    }
}
