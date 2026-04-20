package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved LINE_SEGMENT.
 * A simple line segment defined by two endpoints.
 *
 * @param id STEP instance id
 * @param name segment name
 * @param startPoint the start point of the segment
 * @param endPoint the end point of the segment
 */
public record StepLineSegment(
    int id,
    String name,
    StepCartesianPoint startPoint,
    StepCartesianPoint endPoint) implements StepEntity {
}
