package com.minicad.step.syntax;

import java.util.List;

/**
 * Raw header entry from the STEP HEADER section.
 *
 * @param name entry name
 * @param parameters entry parameters
 */
public record StepHeaderEntry(String name, List<StepValue> parameters) {

    public StepHeaderEntry {
        parameters = List.copyOf(parameters);
    }
}
