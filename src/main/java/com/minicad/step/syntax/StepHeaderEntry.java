package com.minicad.step.syntax;

import java.util.List;
import java.util.Objects;

/**
 * Raw header entry from the STEP HEADER section.
 *
 * @param name entry name
 * @param parameters entry parameters
 */
/**
 * Raw header entry from the STEP HEADER section.
 *
 * @param name entry name
 * @param parameters entry parameters
 */
public final class StepHeaderEntry {
    private final String name;
    private final List<StepValue> parameters;

    public StepHeaderEntry(String name, List<StepValue> parameters) {
        this.name = name;
        this.parameters = parameters == null ? null : java.util.List.copyOf(parameters);
    }

    public String getName() {
        return name;
    }

    public List<StepValue> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHeaderEntry that = (StepHeaderEntry) o;
        return Objects.equals(name, that.name) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameters);
    }

    @Override
    public String toString() {
        return "StepHeaderEntry{" + "name=" + name + "parameters=" + parameters + "}";
    }
}
