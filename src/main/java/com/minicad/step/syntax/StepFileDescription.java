package com.minicad.step.syntax;

import java.util.List;
import java.util.Objects;
/**
 * Parsed HEADER section FileDescription entry.
 * Contains protocol names and implementation level.
 */
/**
 * Parsed HEADER section FileDescription entry.
 * Contains protocol names and implementation level.
 */
public final class StepFileDescription {
    private final List<String> description;
    private final String implementationLevel;

    public StepFileDescription(List<String> description, String implementationLevel) {
        this.description = description == null ? null : java.util.List.copyOf(description);
        this.implementationLevel = implementationLevel;
    }

    public List<String> getDescription() {
        return description;
    }

    public String getImplementationLevel() {
        return implementationLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFileDescription that = (StepFileDescription) o;
        return Objects.equals(description, that.description) && Objects.equals(implementationLevel, that.implementationLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, implementationLevel);
    }

    @Override
    public String toString() {
        return "StepFileDescription{" + "description=" + description + "implementationLevel=" + implementationLevel + "}";
    }
}
