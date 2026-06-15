package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved EDGE_CURVE.
 *
 * @param id step id
 * @param name step label
 * @param start start vertex
 * @param end end vertex
 * @param edgeGeometry referenced edge geometry
 * @param sameSense orientation flag
 */
/**
 * Resolved EDGE_CURVE.
 *
 * @param id step id
 * @param name step label
 * @param start start vertex
 * @param end end vertex
 * @param edgeGeometry referenced edge geometry
 * @param sameSense orientation flag
 */
public final class StepEdgeCurve implements StepEntity {
    private final int id;
    private final String name;
    private final StepVertexPoint start;
    private final StepVertexPoint end;
    private final StepEntity edgeGeometry;
    private final boolean sameSense;

    public StepEdgeCurve(int id, String name, StepVertexPoint start, StepVertexPoint end, StepEntity edgeGeometry, boolean sameSense) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.edgeGeometry = edgeGeometry;
        this.sameSense = sameSense;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepVertexPoint getStart() {
        return start;
    }

    public StepVertexPoint getEnd() {
        return end;
    }

    public StepEntity getEdgeGeometry() {
        return edgeGeometry;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepVertexPoint start() { return getStart(); }
    public StepVertexPoint end() { return getEnd(); }
    public StepEntity edgeGeometry() { return getEdgeGeometry(); }
    public boolean sameSense() { return isSameSense(); }

    public StepVertexPoint getStart() {
        return start;
    }

    public StepVertexPoint getEnd() {
        return end;
    }

    public StepEntity getEdgeGeometry() {
        return edgeGeometry;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEdgeCurve that = (StepEdgeCurve) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(edgeGeometry, that.edgeGeometry) && sameSense == that.sameSense;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, start, end, edgeGeometry, sameSense);
    }

    @Override
    public String toString() {
        return "StepEdgeCurve{" + "id=" + id + "name=" + name + "start=" + start + "end=" + end + "edgeGeometry=" + edgeGeometry + "sameSense=" + sameSense + "}";
    }
}
