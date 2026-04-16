package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COMPOSITE_CURVE_2D.
 * A connected sequence of curve segments in 2D.
 *
 * @param id step id
 * @param name step label
 * @param segments ordered list of composite curve segments
 * @param selfIntersect whether the curve self-intersects
 */
public record StepCompositeCurve2D(
        int id,
        String name,
        List<StepCompositeCurveSegment> segments,
        boolean selfIntersect
) implements StepEntity {
    public StepCompositeCurve2D {
        segments = List.copyOf(segments);
    }
}
