package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved THREAD_DEFINITION.
 * A thread definition entity.
 *
 * @param id STEP instance id
 * @param name thread name
 * @param nominalDiameter nominal diameter
 * @param pitch thread pitch
 * @param threadType thread type (internal/external)
 * @param length thread length
 * @param threadProfile thread profile type
 */
public record StepThreadDefinition(
    int id,
    String name,
    Double nominalDiameter,
    Double pitch,
    String threadType,
    Double length,
    String threadProfile) implements StepEntity {
}