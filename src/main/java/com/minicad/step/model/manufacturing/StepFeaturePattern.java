package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FEATURE_PATTERN.
 * Represents a feature pattern definition in manufacturing.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param baseFeature base feature being patterned
 * @param patternType pattern type (linear, circular, mirror, etc)
 * @param parameters pattern parameters (spacing, count, angle, etc)
 */
public record StepFeaturePattern(
    int id,
    String name,
    StepEntity baseFeature,
    String patternType,
    List<Double> parameters) implements StepEntity {
}