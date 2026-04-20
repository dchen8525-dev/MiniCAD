package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved KEYWAY_FEATURE.
 * A keyway feature entity.
 *
 * @param id STEP instance id
 * @param name keyway name
 * @param keywayType keyway type classification
 * @param keywayWidth keyway width
 * @param keywayDepth keyway depth
 * @param keywayLength keyway length
 * @param keywayPosition keyway position placement
 * @param shaftDiameter reference shaft diameter
 */
public record StepKeywayFeature(
    int id,
    String name,
    String keywayType,
    double keywayWidth,
    double keywayDepth,
    double keywayLength,
    StepEntity keywayPosition,
    double shaftDiameter) implements StepEntity {
}