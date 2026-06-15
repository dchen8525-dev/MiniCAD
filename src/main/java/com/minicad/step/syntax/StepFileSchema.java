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

    /**
     * Creates a StepFileSchema from a StepHeaderEntry.
     * FILE_SCHEMA((schema_name1, schema_name2, ...))
     */
    public static StepFileSchema from(StepHeaderEntry entry) {
        if (entry == null) {
            return null;
        }
        List<StepValue> params = entry.parameters();
        if (params == null || params.isEmpty()) {
            return new StepFileSchema(null);
        }
        StepValue first = params.get(0);
        if (first instanceof StepValue.ListValue) {
            StepValue.ListValue lv = (StepValue.ListValue) first;
            List<String> result = new java.util.ArrayList<>();
            for (StepValue elem : lv.elements()) {
                if (elem instanceof StepValue.StringValue) {
                    result.add(((StepValue.StringValue) elem).value());
                }
            }
            return new StepFileSchema(result.isEmpty() ? null : result);
        }
        return new StepFileSchema(null);
    }

    public StepFileSchema(List<String> schemaNames) {
        this.schemaNames = schemaNames == null ? null : java.util.List.copyOf(schemaNames);
    }

    public List<String> getSchemaNames() {
        return schemaNames;
    }

    // Record-style accessor
    public List<String> schemaNames() { return schemaNames; }

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
