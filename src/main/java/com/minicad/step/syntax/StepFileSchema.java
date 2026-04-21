package com.minicad.step.syntax;

import java.util.List;
/**
 * Parsed HEADER section FileSchema entry.
 * Contains the schema names declared in the STEP file.
 */
public record StepFileSchema(
    List<String> schemaNames) {

    public static StepFileSchema from(StepHeaderEntry entry) {
        if (!"FILE_SCHEMA".equalsIgnoreCase(entry.name())) {
            throw new IllegalArgumentException("Expected FILE_SCHEMA, got " + entry.name());
        }
        List<StepValue> params = entry.parameters();
        List<String> names = extractStringList(params.get(0));
        return new StepFileSchema(names);
    }

    private static List<String> extractStringList(StepValue value) {
        if (value instanceof StepValue.ListValue list) {
            return list.elements().stream()
                .map(v -> v instanceof StepValue.StringValue sv ? sv.value() : v.toString())
                .toList();
        }
        return List.of();
    }
}
