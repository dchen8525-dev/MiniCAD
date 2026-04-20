package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal measure-with-unit value.
 *
 * @param id STEP instance id
 * @param valueComponent numeric value
 * @param unitComponent referenced unit entity
 */
public record StepMeasureWithUnit(int id, double valueComponent, StepEntity unitComponent) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
