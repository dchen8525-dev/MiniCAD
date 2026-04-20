package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PATTERN.
 * Represents a pattern feature in manufacturing (hole pattern, etc).
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param features features in the pattern
 * @param patternType pattern type (linear, circular, etc)
 * @param spacing pattern spacing
 */
public record StepPattern(
    int id,
    String name,
    List<StepEntity> features,
    String patternType,
    Double spacing) implements StepEntity {
}