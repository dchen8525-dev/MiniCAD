package com.minicad.step.model;

/**
 * Marker interface for resolved STEP loop subtypes.
 */
public sealed interface StepLoop extends StepEntity permits StepEdgeLoop, StepPolyLoop, StepVertexLoop {
}
