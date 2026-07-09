package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved ORIENTED_EDGE.
 *
 * @param id step id
 * @param name step label
 * @param edgeElement referenced base edge
 * @param orientation orientation flag
 */
/**
 * Resolved ORIENTED_EDGE.
 *
 * @param id step id
 * @param name step label
 * @param edgeElement referenced base edge
 * @param orientation orientation flag
 */
public final class StepOrientedEdge implements StepEntity {
    private final int id;
    private final String name;
    private final StepEdgeCurve edgeElement;
    private final boolean orientation;

    public StepOrientedEdge(int id, String name, StepEdgeCurve edgeElement, boolean orientation) {
        this.id = id;
        this.name = name;
        this.edgeElement = edgeElement;
        this.orientation = orientation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEdgeCurve getEdgeElement() {
        return edgeElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEdgeCurve edgeElement() { return getEdgeElement(); }
    public boolean orientation() { return isOrientation(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrientedEdge that = (StepOrientedEdge) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(edgeElement, that.edgeElement) && orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, edgeElement, orientation);
    }

    @Override
    public String toString() {
        return "StepOrientedEdge{" + "id=" + id + "name=" + name + "edgeElement=" + edgeElement + "orientation=" + orientation + "}";
    }
}
