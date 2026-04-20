package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ORDINATE_DIMENSION_REPRESENTATION.
 * An ordinate dimension representation entity.
 *
 * @param id STEP instance id
 * * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param ordinateOrigin ordinate origin point
 * @param ordinateDirection ordinate direction
 */
public record StepOrdinateDimensionRepresentation(
    int id,
    String name,
    List<StepEntity> items,
    StepEntity context,
    StepEntity ordinateOrigin,
    StepEntity ordinateDirection) implements StepEntity {
}