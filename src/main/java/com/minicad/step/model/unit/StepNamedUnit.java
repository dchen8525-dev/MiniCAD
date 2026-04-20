package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal named unit marker.
 *
 * @param id STEP instance id
 * @param unitKind derived unit kind such as LENGTH_UNIT
 */
public record StepNamedUnit(int id, String unitKind) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
