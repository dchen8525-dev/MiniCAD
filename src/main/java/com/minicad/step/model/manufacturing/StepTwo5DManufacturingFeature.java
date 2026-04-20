package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TWO5D_MANUFACTURING_FEATURE.
 * Represents a 2.5D manufacturing feature (hole, slot, step, etc).
 *
 * @param id STEP instance id
 * @param name feature name
 * @param featureType type of manufacturing feature
 * @param profile profile definition
 * @param depth feature depth
 * @param direction direction of feature
 */
public record StepTwo5DManufacturingFeature(
    int id,
    String name,
    String featureType,
    StepEntity profile,
    Double depth,
    StepEntity direction) implements StepEntity {
}