package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved THREAD_FEATURE.
 * A thread feature entity with complete thread definition.
 *
 * @param id STEP instance id
 * @param name thread name
 * @param threadType thread type (internal, external)
 * @param threadStandard thread standard specification
 * @param nominalDiameter nominal diameter
 * @param pitch thread pitch
 * @param threadLength thread length
 * @param numberOfStarts number of thread starts
 * @param threadProfile thread profile shape
 * @param threadDirection thread direction (right-hand, left-hand)
 */
public record StepThreadFeature(
    int id,
    String name,
    String threadType,
    String threadStandard,
    double nominalDiameter,
    double pitch,
    double threadLength,
    int numberOfStarts,
    StepEntity threadProfile,
    String threadDirection) implements StepEntity {
}