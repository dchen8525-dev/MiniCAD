package com.minicad.step.model;

/**
 * Minimal SI unit definition.
 *
 * @param id STEP instance id
 * @param unitKind derived unit kind such as LENGTH_UNIT
 * @param prefix optional SI prefix enum name
 * @param unitName SI base unit enum name
 */
public record StepSiUnit(int id, String unitKind, String prefix, String unitName) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
