package com.minicad.step.model.security;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ACCESS_FEATURE.
 * An access feature entity.
 *
 * @param id STEP instance id
 * @param name access name
 * @param accessType access type (door, panel, hatch, inspection)
 * @param accessGeometry access geometry representation
 * @param accessOpening access opening dimensions
 * @param accessLocation access location placement
 * @varianceFrequency access variance frequency (regular, emergency)
 */
public record StepAccessFeature(
    int id,
    String name,
    String accessType,
    StepEntity accessGeometry,
    List<Double> accessOpening,
    StepEntity accessLocation,
    String varianceFrequency) implements StepEntity {
}