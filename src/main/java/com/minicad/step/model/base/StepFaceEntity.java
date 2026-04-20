package com.minicad.step.model.base;

import java.util.List;

import com.minicad.step.model.topology.StepOrientedFace;
import com.minicad.step.model.topology.StepFaceSurface;
import com.minicad.step.model.topology.StepFaceBound;
import com.minicad.step.model.topology.StepAdvancedFace;

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
}
