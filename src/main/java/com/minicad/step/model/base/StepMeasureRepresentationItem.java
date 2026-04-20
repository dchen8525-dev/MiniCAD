package com.minicad.step.model.base;

/**
 * Minimal measure representation item for native validation payloads.
 *
 * @param id STEP instance id
 * @param name item name
 * @param measureType typed measure wrapper name
 * @param value numeric value
 * @param unit unit reference
 */
public record StepMeasureRepresentationItem(
        int id,
        String name,
        String measureType,
        double value,
        StepEntity unit
) implements StepEntity {
}
