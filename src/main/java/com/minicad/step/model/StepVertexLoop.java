package com.minicad.step.model;

import java.util.Objects;

/**
 * Resolved VERTEX_LOOP.
 *
 * @param id step id
 * @param name step label
 * @param loopVertex referenced single vertex
 */
/**
 * Resolved VERTEX_LOOP.
 *
 * @param id step id
 * @param name step label
 * @param loopVertex referenced single vertex
 */
public final class StepVertexLoop implements StepLoop {
    private final int id;
    private final String name;
    private final StepVertexPoint loopVertex;

    public StepVertexLoop(int id, String name, StepVertexPoint loopVertex) {
        this.id = id;
        this.name = name;
        this.loopVertex = loopVertex;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepVertexPoint getLoopVertex() {
        return loopVertex;
    }

    // Record-style accessor
    public StepVertexPoint loopVertex() {
        return loopVertex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVertexLoop that = (StepVertexLoop) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(loopVertex, that.loopVertex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, loopVertex);
    }

    @Override
    public String toString() {
        return "StepVertexLoop{" + "id=" + id + "name=" + name + "loopVertex=" + loopVertex + "}";
    }
}
