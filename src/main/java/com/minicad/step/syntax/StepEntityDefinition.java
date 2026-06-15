package com.minicad.step.syntax;

import java.util.List;
import java.util.Objects;

/**
 * One simple entity definition, either as a standalone instance or as one component of a complex entity instance.
 *
 * @param name entity name
 * @param parameters raw parameter values
 */
/**
 * One simple entity definition, either as a standalone instance or as one component of a complex entity instance.
 *
 * @param name entity name
 * @param parameters raw parameter values
 */
public final class StepEntityDefinition {
    private final String name;
    private final List<StepValue> parameters;

    public StepEntityDefinition(String name, List<StepValue> parameters) {
        this.name = name;
        this.parameters = parameters == null ? null : java.util.List.copyOf(parameters);
    }

    public String getName() {
        return name;
    }

    public List<StepValue> getParameters() {
        return parameters;
    }

    // Record-style accessors
    public String name() { return getName(); }
    public List<StepValue> parameters() { return getParameters(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEntityDefinition that = (StepEntityDefinition) o;
        return Objects.equals(name, that.name) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameters);
    }

    @Override
    public String toString() {
        return "StepEntityDefinition{" + "name=" + name + "parameters=" + parameters + "}";
    }
}
