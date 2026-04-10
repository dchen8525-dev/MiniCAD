package com.minicad.step.model;

/**
 * Minimal USER_DEFINED_MARKER.
 *
 * @param id STEP instance id
 * @param name marker name
 * @param mappingSource representation map
 * @param mappingTarget placement target
 */
public record StepUserDefinedMarker(
        int id,
        String name,
        StepRepresentationMap mappingSource,
        StepEntity mappingTarget
) implements StepEntity {
}
