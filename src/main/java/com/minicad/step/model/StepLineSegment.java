package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LINE_SEGMENT.
 * A simple line segment defined by two endpoints.
 *
 * @param id STEP instance id
 * @param name segment name
 * @param startPoint the start point of the segment
 * @param endPoint the end point of the segment
 */
public final class StepLineSegment extends AbstractStepEntity {
    private final StepCartesianPoint startPoint;
    private final StepCartesianPoint endPoint;

    public StepLineSegment(int id, String name, StepCartesianPoint startPoint, StepCartesianPoint endPoint) {
        super(id, name);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("startPoint", startPoint);
        state.put("endPoint", endPoint);
        return state;
    }
}
