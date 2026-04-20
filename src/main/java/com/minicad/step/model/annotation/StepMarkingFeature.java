package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MARKING_FEATURE.
 * A marking feature entity.
 *
 * @param id STEP instance id
 * @param name marking name
 * @param markingType marking type (laser, stamp, ink, engrave)
 * @param markingGeometry marking geometry representation
 * @param markingContent marking content text/symbol
 * @param markingDepth marking depth for engraving
 * @param markingPosition marking position placement
 */
public record StepMarkingFeature(
    int id,
    String name,
    String markingType,
    StepEntity markingGeometry,
    String markingContent,
    double markingDepth,
    StepEntity markingPosition) implements StepEntity {
}