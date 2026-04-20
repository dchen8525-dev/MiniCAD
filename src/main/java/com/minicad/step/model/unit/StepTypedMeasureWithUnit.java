package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal typed measure-with-unit subtype.
 *
 * @param id STEP instance id
 * @param entityName specific STEP entity name such as LENGTH_MEASURE_WITH_UNIT
 * @param valueComponent numeric value
 * @param unitComponent referenced unit entity
 */
public record StepTypedMeasureWithUnit(
    int id,
    String entityName,
    double valueComponent,
    StepEntity unitComponent) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
