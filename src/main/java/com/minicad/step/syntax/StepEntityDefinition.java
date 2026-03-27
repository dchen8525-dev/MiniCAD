package com.minicad.step.syntax;

import java.util.List;

/**
 * One simple entity definition, either as a standalone instance or as one component of a complex entity instance.
 *
 * @param name entity name
 * @param parameters raw parameter values
 */
public record StepEntityDefinition(String name, List<StepValue> parameters) {

    public StepEntityDefinition {
        parameters = List.copyOf(parameters);
    }
}
