package com.minicad.step.model;

import java.util.List;

/**
 * Minimal derived unit definition.
 *
 * @param id STEP instance id
 * @param elements unit elements
 */
public record StepDerivedUnit(int id, List<StepDerivedUnitElement> elements) implements StepEntity {

    public StepDerivedUnit {
        elements = List.copyOf(elements);
    }

    @Override
    public String name() {
        return "";
    }
}
