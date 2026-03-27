package com.minicad.step.model;

import java.util.List;

/**
 * Marker interface for resolved STEP face subtypes.
 */
public sealed interface StepFaceEntity extends StepEntity permits StepAdvancedFace, StepFaceSurface, StepOrientedFace {

    /**
     * Returns the face bounds.
     *
     * @return immutable face-bound list
     */
    List<StepFaceBound> bounds();
}
