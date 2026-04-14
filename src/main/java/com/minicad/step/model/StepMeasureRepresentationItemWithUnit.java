package com.minicad.step.model;

/**
 * Resolved MEASURE_REPRESENTATION_ITEM_WITH_UNIT.
 * A measure with unit as a representation item.
 *
 * @param id STEP instance id
 * @param name item name
 * @param measureValue measure value
 * @param unit unit reference
 */
public record StepMeasureRepresentationItemWithUnit(
    int id,
    String name,
    double measureValue,
    StepEntity unit) implements StepEntity {
}