package com.minicad.step.syntax;

import java.util.List;
/**
 * Parsed HEADER section FileDescription entry.
 * Contains protocol names and implementation level.
 */
public record StepFileDescription(
    List<String> description,
    String implementationLevel) {

    public static StepFileDescription from(StepHeaderEntry entry) {
        if (!"FILE_DESCRIPTION".equalsIgnoreCase(entry.name())) {
            throw new IllegalArgumentException("Expected FILE_DESCRIPTION, got " + entry.name());
        }
        List<StepValue> params = entry.parameters();
        List<String> desc = extractStringList(params.get(0));
        String level = extractString(params.get(1));
        return new StepFileDescription(desc, level);
    }

    private static List<String> extractStringList(StepValue value) {
        if (value instanceof StepValue.ListValue list) {
            return list.elements().stream()
                .map(v -> v instanceof StepValue.StringValue sv ? sv.value() : v.toString())
                .toList();
        }
        return List.of();
    }

    private static String extractString(StepValue value) {
        value = unwrap(value);
        return value instanceof StepValue.StringValue sv ? sv.value() : "";
    }

    private static StepValue unwrap(StepValue value) {
        if (value instanceof StepValue.TypedValue tv) return tv.value();
        return value;
    }
}
