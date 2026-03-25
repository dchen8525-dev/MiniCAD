package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ADVANCED_FACE.
 *
 * @param id step id
 * @param name step label
 * @param bounds face bounds
 * @param faceGeometry supporting surface
 * @param sameSense orientation flag
 */
public record StepAdvancedFace(
        int id,
        String name,
        List<StepFaceBound> bounds,
        StepEntity faceGeometry,
        boolean sameSense
) implements StepEntity {

    /**
     * Creates an immutable advanced-face record.
     */
    public StepAdvancedFace {
        bounds = List.copyOf(bounds);
    }
}
