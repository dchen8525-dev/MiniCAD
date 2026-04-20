package com.minicad.step.model.topology;

import java.util.List;

import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.base.StepFaceEntity;

/**
 * Resolved FACE_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param bounds face bounds
 * @param faceGeometry supporting surface
 * @param sameSense orientation flag
 */
public record StepFaceSurface(
        int id,
        String name,
        List<StepFaceBound> bounds,
        StepEntity faceGeometry,
        boolean sameSense
) implements StepFaceEntity {

    /**
     * Creates an immutable face-surface record.
     */
    public StepFaceSurface {
        bounds = List.copyOf(bounds);
    }
}
