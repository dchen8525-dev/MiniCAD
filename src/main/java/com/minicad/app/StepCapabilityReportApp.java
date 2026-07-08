package com.minicad.app;

import com.minicad.builder.StepCapabilityRegistry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
    private static final Pattern EXPRESS_ENTITY_PATTERN = Pattern.compile("(?im)^\\s*ENTITY\\s+([A-Za-z0-9_]+)\\b");

    private StepCapabilityReportApp() {
    }

    public static void main(String[] args) throws IOException {
        Path root = DEFAULT_ROOT;
        boolean json = false;
        boolean writeDocs = false;
        Path output = null;
        Path schema = null;
        String schemaName = null;
        for (String arg : args) {
            if ("--json".equals(arg)) {
                json = true;
            } else if ("--write-docs".equals(arg)) {
                writeDocs = true;
            } else if (arg.startsWith("--root=")) {
                root = Path.of(arg.substring("--root=".length()));
            } else if (arg.startsWith("--out=")) {
                output = Path.of(arg.substring("--out=".length()));
            } else if (arg.startsWith("--schema=")) {
                schema = Path.of(arg.substring("--schema=".length()));
            } else if (arg.startsWith("--schema-name=")) {
                schemaName = arg.substring("--schema-name=".length());
            } else {
                throw new IllegalArgumentException("Usage: StepCapabilityReportApp [--json] [--write-docs] [--root=<path>] [--out=<path>] [--schema=<path>] [--schema-name=<name>]");
            }
        }

        CapabilityReport report = scan(root);
        String rendered;
        if (schema != null) {
            SchemaCoverageReport schemaReport = scanSchemaCoverage(report, schemaName(schema, schemaName), schema);
            rendered = json ? toJson(schemaReport) : toMarkdown(schemaReport);
        } else {
            rendered = json ? toJson(report) : toMarkdown(report);
        }
        if (output != null) {
            write(output, rendered);
        } else {
            System.out.print(rendered);
        }
        if (writeDocs) {
            write(root.resolve(Path.of("doc", "generated", "coverage.md")), toMarkdown(report));
            write(root.resolve(Path.of("doc", "generated", "coverage.json")), toJson(report));
            writeKnownSchemaDocs(root, report);
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
        Map<String, StepCapabilityRegistry.Capability> declaredCapabilities = StepCapabilityRegistry.loadDefault();

        Set<String> allEntities = new TreeSet<>();
        allEntities.addAll(modelEntities);
        allEntities.addAll(registeredEntities);
        allEntities.addAll(declaredCapabilities.keySet());
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
                        testedEntities.contains(entity),
                        declaredCapabilities.get(entity)))
                .sorted(Comparator.comparing(CapabilityRow::entity))
                .collect(Collectors.toList());

        return new CapabilityReport(
                modelEntities,
                registeredEntities,
                builderEntities,
                exporterEntities,
                testedEntities,
                declaredCapabilities,
                rows);
    }

    static SchemaCoverageReport scanSchemaCoverage(CapabilityReport capabilityReport, String schemaName, Path schemaPath) throws IOException {
        Set<String> schemaEntities = scanExpressSchemaEntities(schemaPath);
        List<SchemaCoverageRow> rows = schemaEntities.stream()
                .map(entity -> {
                    CapabilityRow capabilityRow = capabilityReport.rowByEntity().get(entity);
                    return new SchemaCoverageRow(
                            entity,
                            capabilityRow != null && capabilityRow.modelClass(),
                            capabilityRow != null && capabilityRow.registered(),
                            capabilityRow != null && capabilityRow.builderReferenced(),
                            capabilityRow != null && capabilityRow.exporterReferenced(),
                            capabilityRow != null && capabilityRow.testReferenced(),
                            capabilityRow == null ? null : capabilityRow.declaredCapability());
                })
                .sorted(Comparator.comparing(SchemaCoverageRow::entity))
                .collect(Collectors.toList());
        return new SchemaCoverageReport(schemaName, schemaPath.normalize(), schemaEntities, rows);
    }

    static Set<String> scanExpressSchemaEntities(Path schemaPath) throws IOException {
        String text = Files.readString(schemaPath);
        Set<String> entities = new TreeSet<>();
        Matcher matcher = EXPRESS_ENTITY_PATTERN.matcher(text);
        while (matcher.find()) {
            entities.add(matcher.group(1).toUpperCase());
        }
        if (!entities.isEmpty()) {
            return entities;
        }
        for (String line : text.split("\\R")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#") || trimmed.startsWith("//")) {
                continue;
            }
            int comment = trimmed.indexOf('#');
            if (comment >= 0) {
                trimmed = trimmed.substring(0, comment).trim();
            }
            String[] parts = trimmed.split("\\s+");
            if (parts.length > 0 && parts[0].matches("[A-Za-z][A-Za-z0-9_]*")) {
                entities.add(parts[0].toUpperCase());
            }
        }
        return entities;
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
                    .collect(Collectors.toList());
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
        markdown.append("| Declared capability entries | ").append(report.declaredCapabilities().size()).append(" |\n");
        markdown.append("| Matrix rows | ").append(report.rows().size()).append(" |\n\n");

        markdown.append("## Matrix\n\n");
        markdown.append("| Entity | Quality | Model | Registered | Builder | Exporter | Tested | Declared | Limitations |\n");
        markdown.append("| --- | :---: | :---: | :---: | :---: | :---: | :---: | --- | --- |\n");
        for (CapabilityRow row : report.rows()) {
            markdown.append("| ").append(row.entity())
                    .append(" | ").append(row.qualityLevel())
                    .append(" | ").append(mark(row.modelClass()))
                    .append(" | ").append(mark(row.registered()))
                    .append(" | ").append(mark(row.builderReferenced()))
                    .append(" | ").append(mark(row.exporterReferenced()))
                    .append(" | ").append(mark(row.testReferenced()))
                    .append(" | ").append(declaredLevel(row.declaredCapability()))
                    .append(" | ").append(declaredLimitations(row.declaredCapability()))
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
        summary.put("declaredCapabilityEntries", report.declaredCapabilities().size());
        summary.put("matrixRows", report.rows().size());

        StringBuilder json = new StringBuilder(64_000);
        json.append("{\n");
        json.append("  \"summary\": ").append(mapToJson(summary)).append(",\n");
        json.append("  \"rows\": [\n");
        List<String> rowJson = new ArrayList<>();
        for (CapabilityRow row : report.rows()) {
            rowJson.add("    {"
                    + "\"entity\":\"" + escapeJson(row.entity()) + "\","
                    + "\"qualityLevel\":\"" + row.qualityLevel() + "\","
                    + "\"model\":" + row.modelClass() + ","
                    + "\"registered\":" + row.registered() + ","
                    + "\"builder\":" + row.builderReferenced() + ","
                    + "\"exporter\":" + row.exporterReferenced() + ","
                    + "\"tested\":" + row.testReferenced() + ","
                    + "\"declaredLevel\":\"" + escapeJson(declaredLevel(row.declaredCapability())) + "\","
                    + "\"limitations\":\"" + escapeJson(declaredLimitations(row.declaredCapability())) + "\""
                    + "}");
        }
        json.append(String.join(",\n", rowJson)).append("\n");
        json.append("  ]\n");
        json.append("}\n");
        return json.toString();
    }

    static String toMarkdown(SchemaCoverageReport report) {
        StringBuilder markdown = new StringBuilder(64_000);
        markdown.append("# MiniCAD ").append(report.schemaName()).append(" Schema Coverage Report\n\n");
        markdown.append("Generated from EXPRESS schema declarations and source capability scanning. This report is a capability signal, not a geometric correctness proof.\n\n");
        markdown.append("Schema source: `").append(report.schemaPath()).append("`\n\n");
        markdown.append("## Summary\n\n");
        markdown.append("| Metric | Count |\n");
        markdown.append("| --- | ---: |\n");
        markdown.append("| Schema entities | ").append(report.schemaEntities().size()).append(" |\n");
        markdown.append("| Model classes | ").append(report.modelCount()).append(" |\n");
        markdown.append("| Registered entities | ").append(report.registeredCount()).append(" |\n");
        markdown.append("| Builder-referenced entities | ").append(report.builderCount()).append(" |\n");
        markdown.append("| Exporter-referenced entities | ").append(report.exporterCount()).append(" |\n");
        markdown.append("| Test-referenced entities | ").append(report.testedCount()).append(" |\n");
        markdown.append("| Declared capability entries | ").append(report.declaredCount()).append(" |\n");
        markdown.append("| Full signal rows | ").append(report.fullSignalCount()).append(" |\n");
        markdown.append("| No signal rows | ").append(report.noSignalCount()).append(" |\n\n");
        markdown.append("## Matrix\n\n");
        markdown.append("| Entity | Quality | Model | Registered | Builder | Exporter | Tested | Declared | Limitations |\n");
        markdown.append("| --- | :---: | :---: | :---: | :---: | :---: | :---: | --- | --- |\n");
        for (SchemaCoverageRow row : report.rows()) {
            markdown.append("| ").append(row.entity())
                    .append(" | ").append(row.qualityLevel())
                    .append(" | ").append(mark(row.modelClass()))
                    .append(" | ").append(mark(row.registered()))
                    .append(" | ").append(mark(row.builderReferenced()))
                    .append(" | ").append(mark(row.exporterReferenced()))
                    .append(" | ").append(mark(row.testReferenced()))
                    .append(" | ").append(declaredLevel(row.declaredCapability()))
                    .append(" | ").append(declaredLimitations(row.declaredCapability()))
                    .append(" |\n");
        }
        return markdown.toString();
    }

    static String toJson(SchemaCoverageReport report) {
        Map<String, Object> summary = new TreeMap<>();
        summary.put("schemaEntities", report.schemaEntities().size());
        summary.put("modelClasses", report.modelCount());
        summary.put("registeredEntities", report.registeredCount());
        summary.put("builderReferencedEntities", report.builderCount());
        summary.put("exporterReferencedEntities", report.exporterCount());
        summary.put("testReferencedEntities", report.testedCount());
        summary.put("declaredCapabilityEntries", report.declaredCount());
        summary.put("fullSignalRows", report.fullSignalCount());
        summary.put("noSignalRows", report.noSignalCount());

        StringBuilder json = new StringBuilder(64_000);
        json.append("{\n");
        json.append("  \"schemaName\": \"").append(escapeJson(report.schemaName())).append("\",\n");
        json.append("  \"schemaPath\": \"").append(escapeJson(report.schemaPath().toString())).append("\",\n");
        json.append("  \"summary\": ").append(mapToJson(summary)).append(",\n");
        json.append("  \"rows\": [\n");
        List<String> rowJson = new ArrayList<>();
        for (SchemaCoverageRow row : report.rows()) {
            rowJson.add("    {"
                    + "\"entity\":\"" + escapeJson(row.entity()) + "\","
                    + "\"qualityLevel\":\"" + row.qualityLevel() + "\","
                    + "\"model\":" + row.modelClass() + ","
                    + "\"registered\":" + row.registered() + ","
                    + "\"builder\":" + row.builderReferenced() + ","
                    + "\"exporter\":" + row.exporterReferenced() + ","
                    + "\"tested\":" + row.testReferenced() + ","
                    + "\"declaredLevel\":\"" + escapeJson(declaredLevel(row.declaredCapability())) + "\","
                    + "\"limitations\":\"" + escapeJson(declaredLimitations(row.declaredCapability())) + "\""
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

    private static String declaredLevel(StepCapabilityRegistry.Capability capability) {
        return capability == null ? "" : capability.level();
    }

    private static String declaredLimitations(StepCapabilityRegistry.Capability capability) {
        return capability == null ? "" : capability.limitations();
    }

    private static void write(Path path, String text) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, text, StandardCharsets.UTF_8);
    }

    private static void writeKnownSchemaDocs(Path root, CapabilityReport report) throws IOException {
        Path schemas = root.resolve("schemas");
        Path ap242 = schemas.resolve("ap242ed2_dis2_mim_lf_v1.101.exp");
        if (Files.isRegularFile(ap242)) {
            SchemaCoverageReport ap242Report = scanSchemaCoverage(report, "AP242 Ed2", ap242);
            write(root.resolve(Path.of("doc", "generated", "ap242-coverage.md")), toMarkdown(ap242Report));
            write(root.resolve(Path.of("doc", "generated", "ap242-coverage.json")), toJson(ap242Report));
        }
        Path ap214 = schemas.resolve("ap214-curated-entities.lst");
        if (Files.isRegularFile(ap214)) {
            SchemaCoverageReport ap214Report = scanSchemaCoverage(report, "AP214 Curated", ap214);
            write(root.resolve(Path.of("doc", "generated", "ap214-coverage.md")), toMarkdown(ap214Report));
            write(root.resolve(Path.of("doc", "generated", "ap214-coverage.json")), toJson(ap214Report));
        }
    }

    private static String schemaName(Path schemaPath, String explicitName) {
        if (explicitName != null && !explicitName.isBlank()) {
            return explicitName;
        }
        String fileName = schemaPath.getFileName().toString();
        int dot = fileName.indexOf('.');
        return dot > 0 ? fileName.substring(0, dot) : fileName;
    }

    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    static class CapabilityReport {
        private final Set<String> modelEntities;
        private final Set<String> registeredEntities;
        private final Set<String> builderEntities;
        private final Set<String> exporterEntities;
        private final Set<String> testedEntities;
        private final Map<String, StepCapabilityRegistry.Capability> declaredCapabilities;
        private final List<CapabilityRow> rows;

        CapabilityReport(Set<String> modelEntities, Set<String> registeredEntities,
                          Set<String> builderEntities, Set<String> exporterEntities,
                          Set<String> testedEntities,
                          Map<String, StepCapabilityRegistry.Capability> declaredCapabilities,
                          List<CapabilityRow> rows) {
            this.modelEntities = modelEntities;
            this.registeredEntities = registeredEntities;
            this.builderEntities = builderEntities;
            this.exporterEntities = exporterEntities;
            this.testedEntities = testedEntities;
            this.declaredCapabilities = declaredCapabilities;
            this.rows = rows;
        }

        Set<String> modelEntities() { return modelEntities; }
        Set<String> registeredEntities() { return registeredEntities; }
        Set<String> builderEntities() { return builderEntities; }
        Set<String> exporterEntities() { return exporterEntities; }
        Set<String> testedEntities() { return testedEntities; }
        Map<String, StepCapabilityRegistry.Capability> declaredCapabilities() { return declaredCapabilities; }
        List<CapabilityRow> rows() { return rows; }

        Map<String, CapabilityRow> rowByEntity() {
            Map<String, CapabilityRow> byEntity = new TreeMap<>();
            for (CapabilityRow row : rows) {
                byEntity.put(row.entity(), row);
            }
            return byEntity;
        }
    }

    static class CapabilityRow {
        private final String entity;
        private final boolean modelClass;
        private final boolean registered;
        private final boolean builderReferenced;
        private final boolean exporterReferenced;
        private final boolean testReferenced;
        private final StepCapabilityRegistry.Capability declaredCapability;

        CapabilityRow(String entity, boolean modelClass, boolean registered,
                      boolean builderReferenced, boolean exporterReferenced,
                      boolean testReferenced, StepCapabilityRegistry.Capability declaredCapability) {
            this.entity = entity;
            this.modelClass = modelClass;
            this.registered = registered;
            this.builderReferenced = builderReferenced;
            this.exporterReferenced = exporterReferenced;
            this.testReferenced = testReferenced;
            this.declaredCapability = declaredCapability;
        }

        String entity() { return entity; }
        boolean modelClass() { return modelClass; }
        boolean registered() { return registered; }
        boolean builderReferenced() { return builderReferenced; }
        boolean exporterReferenced() { return exporterReferenced; }
        boolean testReferenced() { return testReferenced; }
        StepCapabilityRegistry.Capability declaredCapability() { return declaredCapability; }

        String qualityLevel() {
            return StepCapabilityReportApp.qualityLevel(
                    modelClass, registered, builderReferenced, exporterReferenced, testReferenced);
        }
    }

    static class SchemaCoverageReport {
        private final String schemaName;
        private final Path schemaPath;
        private final Set<String> schemaEntities;
        private final List<SchemaCoverageRow> rows;

        SchemaCoverageReport(String schemaName, Path schemaPath,
                              Set<String> schemaEntities, List<SchemaCoverageRow> rows) {
            this.schemaName = schemaName;
            this.schemaPath = schemaPath;
            this.schemaEntities = schemaEntities;
            this.rows = rows;
        }

        String schemaName() { return schemaName; }
        Path schemaPath() { return schemaPath; }
        Set<String> schemaEntities() { return schemaEntities; }
        List<SchemaCoverageRow> rows() { return rows; }

        long modelCount() {
            return rows.stream().filter(SchemaCoverageRow::modelClass).count();
        }

        long registeredCount() {
            return rows.stream().filter(SchemaCoverageRow::registered).count();
        }

        long builderCount() {
            return rows.stream().filter(SchemaCoverageRow::builderReferenced).count();
        }

        long exporterCount() {
            return rows.stream().filter(SchemaCoverageRow::exporterReferenced).count();
        }

        long testedCount() {
            return rows.stream().filter(SchemaCoverageRow::testReferenced).count();
        }

        long declaredCount() {
            return rows.stream().filter(row -> row.declaredCapability() != null).count();
        }

        long fullSignalCount() {
            return rows.stream().filter(row -> row.modelClass()
                    && row.registered()
                    && row.builderReferenced()
                    && row.exporterReferenced()
                    && row.testReferenced()).count();
        }

        long noSignalCount() {
            return rows.stream().filter(row -> !row.modelClass()
                    && !row.registered()
                    && !row.builderReferenced()
                    && !row.exporterReferenced()
                    && !row.testReferenced()).count();
        }
    }

    static class SchemaCoverageRow {
        private final String entity;
        private final boolean modelClass;
        private final boolean registered;
        private final boolean builderReferenced;
        private final boolean exporterReferenced;
        private final boolean testReferenced;
        private final StepCapabilityRegistry.Capability declaredCapability;

        SchemaCoverageRow(String entity, boolean modelClass, boolean registered,
                          boolean builderReferenced, boolean exporterReferenced,
                          boolean testReferenced, StepCapabilityRegistry.Capability declaredCapability) {
            this.entity = entity;
            this.modelClass = modelClass;
            this.registered = registered;
            this.builderReferenced = builderReferenced;
            this.exporterReferenced = exporterReferenced;
            this.testReferenced = testReferenced;
            this.declaredCapability = declaredCapability;
        }

        String entity() { return entity; }
        boolean modelClass() { return modelClass; }
        boolean registered() { return registered; }
        boolean builderReferenced() { return builderReferenced; }
        boolean exporterReferenced() { return exporterReferenced; }
        boolean testReferenced() { return testReferenced; }
        StepCapabilityRegistry.Capability declaredCapability() { return declaredCapability; }

        String qualityLevel() {
            return StepCapabilityReportApp.qualityLevel(
                    modelClass, registered, builderReferenced, exporterReferenced, testReferenced);
        }
    }

    private static String qualityLevel(
            boolean modelClass,
            boolean registered,
            boolean builderReferenced,
            boolean exporterReferenced,
            boolean testReferenced
    ) {
        if (testReferenced) {
            return "L4";
        }
        if (exporterReferenced) {
            return "L3";
        }
        if (builderReferenced) {
            return "L2";
        }
        if (registered) {
            return "L1";
        }
        if (modelClass) {
            return "L0";
        }
        return "";
    }
}
