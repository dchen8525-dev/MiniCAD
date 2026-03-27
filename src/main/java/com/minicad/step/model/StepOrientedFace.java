package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ORIENTED_FACE.
 *
 * @param id step id
 * @param name step label
 * @param faceElement referenced base face
 * @param orientation orientation flag
 */
public record StepOrientedFace(
        int id,
        String name,
        StepFaceEntity faceElement,
        boolean orientation
) implements StepFaceEntity {

    @Override
    public List<StepFaceBound> bounds() {
        return faceElement.bounds();
    }
}
