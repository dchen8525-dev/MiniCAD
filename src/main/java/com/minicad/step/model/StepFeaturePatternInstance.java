package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FEATURE_PATTERN_INSTANCE.
 * A feature pattern instance entity.
 *
 * @param id STEP instance id
 * @param name instance name
 * @param patternDef pattern definition reference
 * @param instancePosition instance position in pattern
 * @param instanceIndex instance index number
 * @param replicatedFeature replicated feature at this position
 */
public record StepFeaturePatternInstance(
    int id,
    String name,
    StepEntity patternDef,
    StepEntity instancePosition,
    int instanceIndex,
    StepEntity replicatedFeature) implements StepEntity {
}