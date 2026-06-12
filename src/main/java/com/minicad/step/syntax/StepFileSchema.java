package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

import java.util.List;

/**
 * Parsed HEADER section FileSchema entry.
 * Contains the schema names declared in the STEP file.
 */
public record StepFileSchema(
    List<String> schemaNames) {

    public StepFileSchema {
        schemaNames = List.copyOf(schemaNames);
    }

    public static StepFileSchema from(StepHeaderEntry entry) {
        if (!"FILE_SCHEMA".equalsIgnoreCase(entry.name())) {
            throw new IllegalArgumentException("Expected FILE_SCHEMA, got " + entry.name());
        }
        List<StepValue> params = entry.parameters();
        if (params.size() != 1) {
            throw new StepParseException("FILE_SCHEMA header entry expected 1 parameter, got " + params.size());
        }
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
