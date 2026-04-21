package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved REPRESENTATION_CONTEXT_3D.
 * A 3D representation context.
 */
public record StepRepresentationContext3d(
    int id,
    String name,
    String contextType,
    List<Double> coordinateSpaceDimensions) implements StepEntity {
}
