package com.minicad.step.model;

/**
 * Resolved COMPOSITE_CURVE_SEGMENT.
 *
 * @param id STEP id
 * @param transition transition-code enum
 * @param sameSense same-sense flag
 * @param parentCurve parent curve
 */
public record StepCompositeCurveSegment(
        int id,
        String transition,
        boolean sameSense,
        StepEntity parentCurve
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
