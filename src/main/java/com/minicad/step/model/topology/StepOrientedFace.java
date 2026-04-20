package com.minicad.step.model.topology;

import java.util.List;

import com.minicad.step.model.base.StepFaceEntity;

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
