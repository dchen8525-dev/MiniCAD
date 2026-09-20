package com.minicad.step.model;

import java.util.List;


/**
 * Marker interface for resolved STEP face subtypes.
 */
public interface StepFaceEntity extends StepEntity {

    /**
     * Returns the face bounds.
     *
     * @return immutable face-bound list
     */
    List<StepFaceBound> bounds();

    /**
     * Returns the face bounds (Java Bean style).
     *
     * @return immutable face-bound list
     */
    default List<StepFaceBound> getBounds() {
        return bounds();
    }
}
