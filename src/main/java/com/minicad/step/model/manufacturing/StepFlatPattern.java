package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FLAT_PATTERN.
 * A flat pattern entity for sheet metal.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param flatGeometry flat pattern geometry
 * @param bendLines bend line locations
 * @param formingFeatures forming features in flat state
 * @param grainDirection grain direction reference
 * @param unfoldingSequence unfolding sequence operations
 */
public record StepFlatPattern(
    int id,
    String name,
    StepEntity flatGeometry,
    List<StepEntity> bendLines,
    List<StepEntity> formingFeatures,
    StepEntity grainDirection,
    List<StepEntity> unfoldingSequence) implements StepEntity {
}