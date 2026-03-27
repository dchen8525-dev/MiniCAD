package com.minicad.step.model;

import java.util.List;

/**
 * Minimal global unit assigned context.
 *
 * @param id STEP instance id
 * @param units referenced unit entities
 */
public record StepGlobalUnitAssignedContext(int id, List<StepEntity> units) implements StepEntity {

    public StepGlobalUnitAssignedContext {
        units = List.copyOf(units);
    }

    @Override
    public String name() {
        return "";
    }
}
