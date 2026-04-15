package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FILTER_FEATURE.
 * A filter feature entity.
 *
 * @param id STEP instance id
 * @param name filter name
 * @param filterType filter type (air, liquid, magnetic)
 * @param filterGeometry filter geometry representation
 * @param filterMedia filter media specification
 * @varianceMicron variance micron rating
 * @varianceFlow variance flow capacity
 * @param replacementInterval replacement interval specification
 */
public record StepFilterFeature(
    int id,
    String name,
    String filterType,
    StepEntity filterGeometry,
    StepEntity filterMedia,
    double varianceMicron,
    double varianceFlow,
    String replacementInterval) implements StepEntity {
}