package com.minicad.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generates a source-derived STEP capability report.
 *
 * <p>The report intentionally separates model classes, resolver registration,
 * geometry/preview build references, exporter references, and test references.
 * These are signals, not proof of complete CAD correctness.
 */
public final class StepCapabilityReportApp {

    private static final Path DEFAULT_ROOT = Path.of(".");
    private static final Pattern REGISTRY_PUT_PATTERN = Pattern.compile("registry\\.put\\(\\s*\"([A-Z0-9_]+)\"");
    private static final Pattern STEP_CLASS_PATTERN = Pattern.compile("\\bStep([A-Z][A-Za-z0-9]*)\\b");
    private static final Pattern ENTITY_LITERAL_PATTERN = Pattern.compile("\"([A-Z][A-Z0-9_]{2,})\"");

    private StepCapabilityReportApp() {
    }

    public static void main(String[] args) throws IOException {
        Path root = DEFAULT_ROOT;
        boolean json = false;
        boolean writeDocs = false;
        Path output = null;
        for (String arg : args) {
            if ("--json".equals(arg)) {
                json = true;
            } else if ("--write-docs".equals(arg)) {
                writeDocs = true;
            } else if (arg.startsWith("--root=")) {
                root = Path.of(arg.substring("--root=".length()));
            } else if (arg.startsWith("--out=")) {
                output = Path.of(arg.substring("--out=".length()));
            } else {
                throw new IllegalArgumentException("Usage: StepCapabilityReportApp [--json] [--write-docs] [--root=<path>] [--out=<path>]");
            }
        }

        CapabilityReport report = scan(root);
        String rendered = json ? toJson(report) : toMarkdown(report);
        if (output != null) {
            write(output, rendered);
        } else {
            System.out.print(rendered);
        }
        if (writeDocs) {
            write(root.resolve(Path.of("doc", "generated", "coverage.md")), toMarkdown(report));
            write(root.resolve(Path.of("doc", "generated", "coverage.json")), toJson(report));
        }
    }

    static CapabilityReport scan(Path root) throws IOException {
        Path sourceRoot = root.resolve(Path.of("src", "main", "java")).normalize();
        Path testRoot = root.resolve(Path.of("src", "test", "java")).normalize();
        Set<String> modelEntities = scanModelEntities(sourceRoot.resolve(Path.of("com", "minicad", "step", "model")));
        Set<String> registeredEntities = scanRegisteredEntities(sourceRoot.resolve(Path.of("com", "minicad", "step", "semantic")));
        Set<String> builderEntities = scanStepClassReferences(sourceRoot.resolve(Path.of("com", "minicad", "step", "semantic")));
        Set<String> exporterEntities = scanStepClassReferences(sourceRoot.resolve(Path.of("com", "minicad", "app")));
        Set<String> testedEntities = scanTestEntities(testRoot);

        Set<String> allEntities = new TreeSet<>();
        allEntities.addAll(modelEntities);
        allEntities.addAll(registeredEntities);
        builderEntities.retainAll(allEntities);
        exporterEntities.retainAll(allEntities);
        testedEntities.retainAll(allEntities);

        List<CapabilityRow> rows = allEntities.stream()
                .map(entity -> new CapabilityRow(
                        entity,
                        modelEntities.contains(entity),
                        registeredEntities.contains(entity),
                        builderEntities.contains(entity),
                        exporterEntities.contains(entity),
                        testedEntities.contains(entity)))
                .sorted(Comparator.comparing(CapabilityRow::entity))
                .toList();

        return new CapabilityReport(
                modelEntities,
                registeredEntities,
                builderEntities,
                exporterEntities,
                testedEntities,
                rows);
    }

    private static Set<String> scanModelEntities(Path modelRoot) throws IOException {
        Set<String> entities = new TreeSet<>();
        if (!Files.isDirectory(modelRoot)) {
            return entities;
        }
        try (var stream = Files.walk(modelRoot)) {
            stream.filter(path -> Files.isRegularFile(path) && path.getFileName().toString().startsWith("Step") && path.getFileName().toString().endsWith(".java"))
                    .map(path -> path.getFileName().toString())
                    .map(fileName -> fileName.substring(0, fileName.length() - ".java".length()))
                    .map(StepCapabilityReportApp::stepClassToEntityName)
                    .forEach(entities::add);
        }
        return entities;
    }

    private static Set<String> scanRegisteredEntities(Path semanticRoot) throws IOException {
        Set<String> entities = new TreeSet<>();
        for (Path file : javaFiles(semanticRoot)) {
            Matcher matcher = REGISTRY_PUT_PATTERN.matcher(Files.readString(file));
            while (matcher.find()) {
                entities.add(matcher.group(1));
            }
        }
        return entities;
    }

    private static Set<String> scanStepClassReferences(Path root) throws IOException {
        Set<String> entities = new TreeSet<>();
        for (Path file : javaFiles(root)) {
            String text = Files.readString(file);
            Matcher matcher = STEP_CLASS_PATTERN.matcher(text);
            while (matcher.find()) {
                String entityName = camelToUpperUnderscore(matcher.group(1));
                if (!entityName.endsWith("_APP") && !"ENTITY".equals(entityName) && !"VALUE".equals(entityName)) {
                    entities.add(entityName);
                }
            }
        }
        return entities;
    }

    private static Set<String> scanTestEntities(Path testRoot) throws IOException {
        Set<String> entities = scanStepClassReferences(testRoot);
        for (Path file : javaFiles(testRoot)) {
            String text = Files.readString(file);
            Matcher matcher = ENTITY_LITERAL_PATTERN.matcher(text);
            while (matcher.find()) {
                entities.add(matcher.group(1));
            }
        }
        return entities;
    }

    private static List<Path> javaFiles(Path root) throws IOException {
        if (!Files.isDirectory(root)) {
            return List.of();
        }
        try (var stream = Files.walk(root)) {
            return stream
                    .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".java"))
                    .sorted()
                    .toList();
        }
    }

    private static String stepClassToEntityName(String className) {
        if (!className.startsWith("Step")) {
            return camelToUpperUnderscore(className);
        }
        return camelToUpperUnderscore(className.substring("Step".length()));
    }

    static String camelToUpperUnderscore(String text) {
        String normalized = text
                .replace("BSpline", "B_SPLINE")
                .replace("2D", "_2D")
                .replace("3D", "_3D");
        StringBuilder builder = new StringBuilder(normalized.length() + 8);
        for (int i = 0; i < normalized.length(); i++) {
            char ch = normalized.charAt(i);
            if (i > 0 && Character.isUpperCase(ch)) {
                char previous = normalized.charAt(i - 1);
                char next = i + 1 < normalized.length() ? normalized.charAt(i + 1) : 0;
                boolean dimensionSuffix = Character.isDigit(previous) && ch == 'D' && (next == 0 || next == '_');
                if (!dimensionSuffix
                        && (Character.isLowerCase(previous)
                        || Character.isDigit(previous)
                        || (next != 0 && Character.isLowerCase(next)))) {
                    builder.append('_');
                }
            }
            builder.append(Character.toUpperCase(ch));
        }
        return builder.toString();
    }

    static String toMarkdown(CapabilityReport report) {
        StringBuilder markdown = new StringBuilder(64_000);
        markdown.append("# MiniCAD STEP Capability Report\n\n");
        markdown.append("Generated from source scanning. This report is a capability signal, not a geometric correctness proof.\n\n");
        markdown.append("## Summary\n\n");
        markdown.append("| Metric | Count |\n");
        markdown.append("| --- | ---: |\n");
        markdown.append("| Model classes | ").append(report.modelEntities().size()).append(" |\n");
        markdown.append("| Registered entities | ").append(report.registeredEntities().size()).append(" |\n");
        markdown.append("| Builder-referenced entities | ").append(report.builderEntities().size()).append(" |\n");
        markdown.append("| Exporter-referenced entities | ").append(report.exporterEntities().size()).append(" |\n");
        markdown.append("| Test-referenced entities | ").append(report.testedEntities().size()).append(" |\n");
        markdown.append("| Matrix rows | ").append(report.rows().size()).append(" |\n\n");

        markdown.append("## Matrix\n\n");
        markdown.append("| Entity | Model | Registered | Builder | Exporter | Tested |\n");
        markdown.append("| --- | :---: | :---: | :---: | :---: | :---: |\n");
        for (CapabilityRow row : report.rows()) {
            markdown.append("| ").append(row.entity())
                    .append(" | ").append(mark(row.modelClass()))
                    .append(" | ").append(mark(row.registered()))
                    .append(" | ").append(mark(row.builderReferenced()))
                    .append(" | ").append(mark(row.exporterReferenced()))
                    .append(" | ").append(mark(row.testReferenced()))
                    .append(" |\n");
        }
        return markdown.toString();
    }

    static String toJson(CapabilityReport report) {
        Map<String, Object> summary = new TreeMap<>();
        summary.put("modelClasses", report.modelEntities().size());
        summary.put("registeredEntities", report.registeredEntities().size());
        summary.put("builderReferencedEntities", report.builderEntities().size());
        summary.put("exporterReferencedEntities", report.exporterEntities().size());
        summary.put("testReferencedEntities", report.testedEntities().size());
        summary.put("matrixRows", report.rows().size());

        StringBuilder json = new StringBuilder(64_000);
        json.append("{\n");
        json.append("  \"summary\": ").append(mapToJson(summary)).append(",\n");
        json.append("  \"rows\": [\n");
        List<String> rowJson = new ArrayList<>();
        for (CapabilityRow row : report.rows()) {
            rowJson.add("    {"
                    + "\"entity\":\"" + escapeJson(row.entity()) + "\","
                    + "\"model\":" + row.modelClass() + ","
                    + "\"registered\":" + row.registered() + ","
                    + "\"builder\":" + row.builderReferenced() + ","
                    + "\"exporter\":" + row.exporterReferenced() + ","
                    + "\"tested\":" + row.testReferenced()
                    + "}");
        }
        json.append(String.join(",\n", rowJson)).append("\n");
        json.append("  ]\n");
        json.append("}\n");
        return json.toString();
    }

    private static String mapToJson(Map<String, Object> values) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (!first) {
                json.append(',');
            }
            first = false;
            json.append("\"").append(escapeJson(entry.getKey())).append("\":").append(entry.getValue());
        }
        json.append('}');
        return json.toString();
    }

    private static String mark(boolean value) {
        return value ? "yes" : "";
    }

    private static void write(Path path, String text) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, text, StandardCharsets.UTF_8);
    }

    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    record CapabilityReport(
            Set<String> modelEntities,
            Set<String> registeredEntities,
            Set<String> builderEntities,
            Set<String> exporterEntities,
            Set<String> testedEntities,
            List<CapabilityRow> rows) {
    }

    record CapabilityRow(
            String entity,
            boolean modelClass,
            boolean registered,
            boolean builderReferenced,
            boolean exporterReferenced,
            boolean testReferenced) {
    }
}
