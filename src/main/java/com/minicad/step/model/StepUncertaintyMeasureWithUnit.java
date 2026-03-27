package com.minicad.step.model;

/**
 * Minimal uncertainty measure with unit.
 *
 * @param id STEP instance id
 * @param valueComponent numeric value
 * @param unitComponent referenced unit entity
 * @param name uncertainty name
 * @param description uncertainty description
 */
public record StepUncertaintyMeasureWithUnit(
        int id,
        double valueComponent,
        StepEntity unitComponent,
        String name,
        String description
) implements StepEntity {

    @Override
    public String name() {
        return name;
    }
}
