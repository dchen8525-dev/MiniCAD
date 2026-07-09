package com.minicad.step.model.topology;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SUBEDGE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param start start vertex
 * @param end end vertex
 * @param parentEdge parent edge or subedge
 */
/**
 * Resolved SUBEDGE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param start start vertex
 * @param end end vertex
 * @param parentEdge parent edge or subedge
 */
public final class StepSubedge implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity start;
    private final StepEntity end;
    private final StepEntity parentEdge;

    public StepSubedge(int id, String name, StepEntity start, StepEntity end, StepEntity parentEdge) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.parentEdge = parentEdge;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getStart() {
        return start;
    }

    public StepEntity getEnd() {
        return end;
    }

    public StepEntity getParentEdge() {
        return parentEdge;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity start() { return getStart(); }
    public StepEntity end() { return getEnd(); }
    public StepEntity parentEdge() { return getParentEdge(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSubedge that = (StepSubedge) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(parentEdge, that.parentEdge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, start, end, parentEdge);
    }

    @Override
    public String toString() {
        return "StepSubedge{" + "id=" + id + "name=" + name + "start=" + start + "end=" + end + "parentEdge=" + parentEdge + "}";
    }
}
