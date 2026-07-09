package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal SEAM_EDGE.
 * A seam edge where the start and end vertices are the same (closed edge on a surface seam).
 *
 * @param id STEP id
 * @param name STEP label
 * @param edgeStart start vertex
 * @param edgeEnd end vertex (same as start for seam edges)
 */
/**
 * Minimal SEAM_EDGE.
 * A seam edge where the start and end vertices are the same (closed edge on a surface seam).
 *
 * @param id STEP id
 * @param name STEP label
 * @param edgeStart start vertex
 * @param edgeEnd end vertex (same as start for seam edges)
 */
public final class StepSeamEdge implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity edgeStart;
    private final StepEntity edgeEnd;

    public StepSeamEdge(int id, String name, StepEntity edgeStart, StepEntity edgeEnd) {
        this.id = id;
        this.name = name;
        this.edgeStart = edgeStart;
        this.edgeEnd = edgeEnd;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getEdgeStart() {
        return edgeStart;
    }

    public StepEntity getEdgeEnd() {
        return edgeEnd;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity edgeStart() { return getEdgeStart(); }
    public StepEntity edgeEnd() { return getEdgeEnd(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSeamEdge that = (StepSeamEdge) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(edgeStart, that.edgeStart) && Objects.equals(edgeEnd, that.edgeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, edgeStart, edgeEnd);
    }

    @Override
    public String toString() {
        return "StepSeamEdge{" + "id=" + id + "name=" + name + "edgeStart=" + edgeStart + "edgeEnd=" + edgeEnd + "}";
    }
}
