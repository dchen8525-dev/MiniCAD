package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LINE_SEGMENT.
 * A simple line segment defined by two endpoints.
 *
 * @param id STEP instance id
 * @param name segment name
 * @param startPoint the start point of the segment
 * @param endPoint the end point of the segment
 */
/**
 * Resolved LINE_SEGMENT.
 * A simple line segment defined by two endpoints.
 *
 * @param id STEP instance id
 * @param name segment name
 * @param startPoint the start point of the segment
 * @param endPoint the end point of the segment
 */
public final class StepLineSegment implements StepEntity {
    private final int id;
    private final String name;
    private final StepCartesianPoint startPoint;
    private final StepCartesianPoint endPoint;

    public StepLineSegment(int id, String name, StepCartesianPoint startPoint, StepCartesianPoint endPoint) {
        this.id = id;
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepCartesianPoint getStartPoint() {
        return startPoint;
    }

    public StepCartesianPoint getEndPoint() {
        return endPoint;
    }

    // Record-style accessors
    public StepCartesianPoint startPoint() { return getStartPoint(); }
    public StepCartesianPoint endPoint() { return getEndPoint(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLineSegment that = (StepLineSegment) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(startPoint, that.startPoint) && Objects.equals(endPoint, that.endPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startPoint, endPoint);
    }

    @Override
    public String toString() {
        return "StepLineSegment{" + "id=" + id + "name=" + name + "startPoint=" + startPoint + "endPoint=" + endPoint + "}";
    }
}
