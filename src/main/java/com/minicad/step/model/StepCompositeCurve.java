package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COMPOSITE_CURVE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param segments ordered curve segments
 * @param selfIntersect self-intersection flag
 */
public record StepCompositeCurve(
        int id,
        String name,
        List<StepCompositeCurveSegment> segments,
        boolean selfIntersect
) implements StepEntity {

    public StepCompositeCurve {
        segments = List.copyOf(segments);
    }
}
