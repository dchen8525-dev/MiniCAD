package com.minicad.step.syntax;

import java.util.List;

/**
 * Raw entity instance parsed from STEP syntax.
 *
 * @param id numeric instance id
 * @param name entity name
 * @param parameters raw parameter values
 */
public record StepEntityInstance(int id, String name, List<StepValue> parameters) {

    /**
     * Creates an immutable entity record.
     */
    public StepEntityInstance {
        parameters = List.copyOf(parameters);
    }
}
