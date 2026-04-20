package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved THREAD.
 * Represents a thread feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name thread name
 * @param nominalDiameter nominal diameter
 * @param pitch thread pitch
 * @param threadType thread type (internal/external)
 * @param length thread length
 */
public record StepThread(
    int id,
    String name,
    Double nominalDiameter,
    Double pitch,
    String threadType,
    Double length) implements StepEntity {
}