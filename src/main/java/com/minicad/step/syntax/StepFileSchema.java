package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

import java.util.List;
import java.util.Objects;

/**
 * Parsed HEADER section FileSchema entry.
 * Contains the schema names declared in the STEP file.
 */
/**
 * Parsed HEADER section FileSchema entry.
 * Contains the schema names declared in the STEP file.
 */
public final class StepFileSchema {
    private final List<String> schemaNames;

    public StepFileSchema(List<String> schemaNames) {
        this.schemaNames = schemaNames == null ? null : java.util.List.copyOf(schemaNames);
    }

    public List<String> getSchemaNames() {
        return schemaNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFileSchema that = (StepFileSchema) o;
        return Objects.equals(schemaNames, that.schemaNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaNames);
    }

    @Override
    public String toString() {
        return "StepFileSchema{" + "schemaNames=" + schemaNames + "}";
    }
}
