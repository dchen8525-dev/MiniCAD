package com.minicad.step.syntax;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Parsed HEADER section FileName entry.
 * Contains file name, timestamp, author, organization, etc.
 */
public record StepFileName(
    String name,
    String timeStamp,
    List<String> author,
    List<String> organization,
    String preprocessorVersion,
    String originatingSystem,
    String authorization) {

    public static StepFileName from(StepHeaderEntry entry) {
        if (!"FILE_NAME".equalsIgnoreCase(entry.name())) {
            throw new IllegalArgumentException("Expected FILE_NAME, got " + entry.name());
        }
        List<StepValue> params = entry.parameters();
        return new StepFileName(
            extractString(params.get(0)),
            extractString(params.get(1)),
            extractStringList(params.get(2)),
            extractStringList(params.get(3)),
            extractString(params.get(4)),
            extractString(params.get(5)),
            extractString(params.get(6)));
    }

    private static List<String> extractStringList(StepValue value) {
        value = unwrap(value);
        if (value instanceof StepValue.ListValue list) {
            return list.elements().stream()
                .map(v -> extractString(v))
                .toList();
        }
        return List.of();
    }

    private static String extractString(StepValue value) {
        value = unwrap(value);
        if (value instanceof StepValue.OmittedValue || value instanceof StepValue.NotProvidedValue) {
            return "";
        }
        return value instanceof StepValue.StringValue sv ? sv.value() : "";
    }

    private static StepValue unwrap(StepValue value) {
        if (value instanceof StepValue.TypedValue tv) return tv.value();
        return value;
    }
}
