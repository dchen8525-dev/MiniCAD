package com.minicad.tools.coverage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Phase 0 coverage analysis tool.
 *
 * <p>Scans the AP242 schema and local source code to build a static coverage matrix
 * across the resolver, model, builder, exporter, and tests layers. This tool is
 * intentionally based on source scanning so it remains useful even when the project
 * is temporarily not compiling.</p>
 */
public class CoverageAnalyzer {

    private static final String SCHEMA_PATH = "schemas/ap242ed2_dis2_mim_lf_v1.101.exp";
    private static final String RESOLVER_PATH = "src/main/java/com/minicad/step/semantic/StepEntityResolver.java";
    private static final String BUILDER_PATH = "src/main/java/com/minicad/step/semantic/StepCadBuilder.java";
    private static final String EXPORTER_PATH = "src/main/java/com/minicad/app/StepPreviewJsonExporter.java";
    private static final String MODEL_BASE = "src/main/java/com/minicad/step/model";
    private static final String TEST_BASE = "src/test/java";
    private static final String OUTPUT_DIR = "doc/generated";

    private static final Pattern ENTITY_PATTERN = Pattern.compile("^ENTITY\\s+(\\w+)");
    private static final Pattern REGISTRY_PUT_PATTERN = Pattern.compile("registry\\.put\\(\\s*\"([A-Z_0-9]+)\"");
    private static final Pattern ALIAS_CALL_PATTERN = Pattern.compile("register\\w*\\(.*?\\);", Pattern.DOTALL);
    private static final Pattern UPPERCASE_LITERAL_PATTERN = Pattern.compile("\"([A-Z][A-Z0-9_]{2,})\"");
    private static final Pattern STEP_CLASS_PATTERN = Pattern.compile(
        "(?:public\\s+)?(?:abstract\\s+)?(?:final\\s+)?(?:@\\w+\\s+)*(?:record|class)\\s+(Step\\w+)");
    private static final Set<String> NON_ENTITY_TOKENS = Set.of(
        "JSON", "HTTP", "UUID", "DEBUG", "TRACE", "ERROR", "WARN", "INFO",
        "TRUE", "FALSE", "NULL", "DONE", "AREA", "LENGTH", "VALUE", "COUNT");

    public static void main(String[] args) throws Exception {
        System.out.println("=== MiniCAD STEP AP242 Coverage Analyzer ===\n");

        Set<String> standardEntities = parseSchemaEntities(SCHEMA_PATH);
        System.out.println("[1] AP242 standard entities: " + standardEntities.size());

        Map<String, String> registeredEntities = parseResolverRegistrations(RESOLVER_PATH);
        System.out.println("[2] Resolver registrations: " + registeredEntities.size() + " unique keys");

        Set<String> modelClasses = scanModelClasses(MODEL_BASE);
        System.out.println("[3] StepXxx model classes: " + modelClasses.size());

        Set<String> builderEntities = parseBuilderEntities(BUILDER_PATH);
        System.out.println("[4] Builder entity references: " + builderEntities.size());

        Set<String> exporterEntities = parseExporterEntities(EXPORTER_PATH);
        System.out.println("[5] Exporter entity references: " + exporterEntities.size());

        Set<String> testedEntities = parseTestEntities(TEST_BASE);
        System.out.println("[6] Test entity references: " + testedEntities.size());

        CoverageReport report = computeCoverageReport(
            standardEntities,
            registeredEntities,
            modelClasses,
            builderEntities,
            exporterEntities,
            testedEntities);
        printSummary(report);

        writeOutputs(OUTPUT_DIR, report);
        System.out.println("\n[7] Reports written to " + OUTPUT_DIR + "/");
    }

    // =========================================================================
    // 1. Parse EXPRESS schema for ENTITY declarations
    // =========================================================================

    static Set<String> parseSchemaEntities(String schemaPath) throws IOException {
        Set<String> entities = new TreeSet<>();
        try (BufferedReader br = Files.newBufferedReader(Path.of(schemaPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = ENTITY_PATTERN.matcher(line);
                if (matcher.find()) {
                    entities.add(matcher.group(1).toUpperCase(Locale.ROOT));
                }
            }
        }
        return entities;
    }

    // =========================================================================
    // 2. Parse resolver registrations
    // =========================================================================

    static Map<String, String> parseResolverRegistrations(String resolverPath) throws IOException {
        Map<String, String> registrations = new TreeMap<>();
        String content = Files.readString(Path.of(resolverPath));

        Matcher putMatcher = REGISTRY_PUT_PATTERN.matcher(content);
        while (putMatcher.find()) {
            registrations.put(putMatcher.group(1), "direct");
        }

        Map<String, String> aliasRegistrations = parseAliasRegistrations(content);
        for (Map.Entry<String, String> entry : aliasRegistrations.entrySet()) {
            registrations.putIfAbsent(entry.getKey(), entry.getValue());
        }

        return registrations;
    }

    static Map<String, String> parseAliasRegistrations(String content) {
        Map<String, String> aliases = new TreeMap<>();
        Matcher callMatcher = ALIAS_CALL_PATTERN.matcher(content);
        while (callMatcher.find()) {
            String call = callMatcher.group();
            if (!call.contains("registry")) {
                continue;
            }
            if (call.startsWith("registry.put")) {
                continue;
            }
            Matcher nameMatcher = UPPERCASE_LITERAL_PATTERN.matcher(call);
            while (nameMatcher.find()) {
                aliases.put(nameMatcher.group(1), "alias");
            }
        }
        return aliases;
    }

    // =========================================================================
    // 3. Scan model classes
    // =========================================================================

    static Set<String> scanModelClasses(String modelBase) throws IOException {
        Set<String> modelNames = new TreeSet<>();
        try (Stream<Path> stream = Files.walk(Path.of(modelBase))) {
            stream.filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        Matcher matcher = STEP_CLASS_PATTERN.matcher(Files.readString(path));
                        if (matcher.find()) {
                            modelNames.add(toExpressName(matcher.group(1)));
                        }
                    } catch (IOException ignored) {
                    }
                });
        }
        return modelNames;
    }

    // =========================================================================
    // 4. Static support-layer scans
    // =========================================================================

    static Set<String> parseBuilderEntities(String builderPath) throws IOException {
        return parseSupportLiterals(Path.of(builderPath));
    }

    static Set<String> parseExporterEntities(String exporterPath) throws IOException {
        return parseSupportLiterals(Path.of(exporterPath));
    }

    static Set<String> parseTestEntities(String testBase) throws IOException {
        Set<String> entities = new TreeSet<>();
        try (Stream<Path> stream = Files.walk(Path.of(testBase))) {
            for (Path path : stream.filter(p -> p.toString().endsWith(".java")).toList()) {
                entities.addAll(parseSupportLiterals(path));
            }
        }
        return entities;
    }

    static Set<String> parseSupportLiterals(Path path) throws IOException {
        String content = Files.readString(path);
        Set<String> entities = new TreeSet<>();
        Matcher matcher = UPPERCASE_LITERAL_PATTERN.matcher(content);
        while (matcher.find()) {
            String token = matcher.group(1);
            if (looksLikeEntityName(token)) {
                entities.add(token);
            }
        }
        return entities;
    }

    static boolean looksLikeEntityName(String token) {
        if (token.length() < 3) {
            return false;
        }
        if (token.startsWith("STAGE_") || token.endsWith("_DONE") || token.endsWith("_PROGRESS")) {
            return false;
        }
        if (token.contains("JSON") || token.contains("HTTP") || token.contains("UUID")) {
            return false;
        }
        return !NON_ENTITY_TOKENS.contains(token);
    }

    // =========================================================================
    // 5. Compute coverage report
    // =========================================================================

    static CoverageReport computeCoverageReport(Set<String> standardEntities,
                                                Map<String, String> registeredEntities,
                                                Set<String> modelClasses,
                                                Set<String> builderEntities,
                                                Set<String> exporterEntities,
                                                Set<String> testedEntities) {
        Set<String> union = new TreeSet<>(standardEntities);
        union.addAll(registeredEntities.keySet());
        union.addAll(modelClasses);
        union.addAll(builderEntities);
        union.addAll(exporterEntities);
        union.addAll(testedEntities);

        Map<String, EntityCoverage> coverageByEntity = new TreeMap<>();
        for (String entity : union) {
            boolean standard = standardEntities.contains(entity);
            boolean resolved = registeredEntities.containsKey(entity);
            boolean alias = "alias".equals(registeredEntities.get(entity));
            boolean model = modelClasses.contains(entity);
            boolean built = builderEntities.contains(entity);
            boolean exported = exporterEntities.contains(entity);
            boolean tested = testedEntities.contains(entity);
            coverageByEntity.put(entity, new EntityCoverage(
                entity,
                standard,
                resolved,
                alias,
                model,
                built,
                exported,
                tested,
                determineStage(resolved, built, exported, tested)));
        }

        Set<String> nonStandard = coverageByEntity.values().stream()
            .filter(entity -> !entity.standard() && entity.resolved())
            .map(EntityCoverage::name)
            .collect(Collectors.toCollection(TreeSet::new));

        Set<String> unregistered = coverageByEntity.values().stream()
            .filter(entity -> entity.standard() && !entity.resolved())
            .map(EntityCoverage::name)
            .collect(Collectors.toCollection(TreeSet::new));

        Set<String> aliasCovered = coverageByEntity.values().stream()
            .filter(entity -> entity.standard() && entity.alias())
            .map(EntityCoverage::name)
            .collect(Collectors.toCollection(TreeSet::new));

        Set<String> registeredNoModel = coverageByEntity.values().stream()
            .filter(entity -> entity.standard() && entity.resolved() && !entity.model())
            .map(EntityCoverage::name)
            .collect(Collectors.toCollection(TreeSet::new));

        Set<String> registeredWithModel = coverageByEntity.values().stream()
            .filter(entity -> entity.standard() && entity.resolved() && entity.model())
            .map(EntityCoverage::name)
            .collect(Collectors.toCollection(TreeSet::new));

        Set<String> resolvedOnly = coverageByEntity.values().stream()
            .filter(entity -> entity.standard() && "resolved_only".equals(entity.stage()))
            .map(EntityCoverage::name)
            .collect(Collectors.toCollection(TreeSet::new));

        Set<String> resolvedAndBuilt = coverageByEntity.values().stream()
            .filter(entity -> entity.standard() && "resolved_and_built".equals(entity.stage()))
            .map(EntityCoverage::name)
            .collect(Collectors.toCollection(TreeSet::new));

        Set<String> resolvedBuiltPreviewed = coverageByEntity.values().stream()
            .filter(entity -> entity.standard() && "resolved_built_previewed".equals(entity.stage()))
            .map(EntityCoverage::name)
            .collect(Collectors.toCollection(TreeSet::new));

        Set<String> resolvedBuiltPreviewedTested = coverageByEntity.values().stream()
            .filter(entity -> entity.standard() && "resolved_built_previewed_tested".equals(entity.stage()))
            .map(EntityCoverage::name)
            .collect(Collectors.toCollection(TreeSet::new));

        Set<String> registeredNotBuilt = coverageByEntity.values().stream()
            .filter(entity -> entity.standard() && entity.resolved() && !entity.built())
            .map(EntityCoverage::name)
            .collect(Collectors.toCollection(TreeSet::new));

        Set<String> builtNotPreviewed = coverageByEntity.values().stream()
            .filter(entity -> entity.standard() && entity.built() && !entity.exported())
            .map(EntityCoverage::name)
            .collect(Collectors.toCollection(TreeSet::new));

        Set<String> previewedNotTested = coverageByEntity.values().stream()
            .filter(entity -> entity.standard() && entity.exported() && !entity.tested())
            .map(EntityCoverage::name)
            .collect(Collectors.toCollection(TreeSet::new));

        return new CoverageReport(
            standardEntities,
            registeredEntities,
            modelClasses,
            builderEntities,
            exporterEntities,
            testedEntities,
            coverageByEntity,
            unregistered,
            aliasCovered,
            registeredNoModel,
            registeredWithModel,
            nonStandard,
            resolvedOnly,
            resolvedAndBuilt,
            resolvedBuiltPreviewed,
            resolvedBuiltPreviewedTested,
            registeredNotBuilt,
            builtNotPreviewed,
            previewedNotTested);
    }

    static String determineStage(boolean resolved, boolean built, boolean exported, boolean tested) {
        if (!resolved) {
            return "unresolved";
        }
        if (built && exported && tested) {
            return "resolved_built_previewed_tested";
        }
        if (built && exported) {
            return "resolved_built_previewed";
        }
        if (built) {
            return "resolved_and_built";
        }
        return "resolved_only";
    }

    // =========================================================================
    // 6. Summary output
    // =========================================================================

    static void printSummary(CoverageReport report) {
        System.out.println("\n=== Coverage Summary ===");
        System.out.println("AP242 Ed2 standard entities:           " + report.standardEntities().size());
        System.out.println("Resolver registrations:                " + report.registeredEntities().size() + " unique keys");
        System.out.println("Builder entity references:             " + report.builderEntities().size());
        System.out.println("Exporter entity references:            " + report.exporterEntities().size());
        System.out.println("Test entity references:                " + report.testedEntities().size());
        System.out.println("StepXxx model classes:                 " + report.modelClasses().size());
        System.out.println();
        System.out.println("--- Stage Classification ---");
        System.out.println("Resolved only:                         " + report.resolvedOnly().size());
        System.out.println("Resolved and built:                    " + report.resolvedAndBuilt().size());
        System.out.println("Resolved, built, previewed:            " + report.resolvedBuiltPreviewed().size());
        System.out.println("Resolved, built, previewed, tested:    " + report.resolvedBuiltPreviewedTested().size());
        System.out.println("Unregistered (schema gap):             " + report.unregistered().size());
        System.out.println();
        double schemaCoverage = 100.0
            * (report.standardEntities().size() - report.unregistered().size())
            / report.standardEntities().size();
        double stagedCoverage = 100.0
            * report.resolvedBuiltPreviewedTested().size()
            / report.standardEntities().size();
        System.out.printf("Schema coverage (registered):          %.1f%%%n", schemaCoverage);
        System.out.printf("End-to-end coverage (tested):          %.1f%%%n", stagedCoverage);
    }

    // =========================================================================
    // 7. Write reports
    // =========================================================================

    static void writeOutputs(String outputDir, CoverageReport report) throws IOException {
        Path dir = Path.of(outputDir);
        Files.createDirectories(dir);

        Files.write(dir.resolve("ap242-entities.txt"),
            report.standardEntities().stream().sorted().toList());

        Files.write(dir.resolve("ap242-registered-standard-entities.txt"),
            report.registeredEntities().keySet().stream()
                .filter(report.standardEntities()::contains)
                .sorted()
                .toList());

        Files.write(dir.resolve("ap242-model-classes.txt"),
            report.modelClasses().stream()
                .sorted()
                .map(name -> {
                    String className = "Step" + toClassName(name);
                    String pkg = findModelPackage(className);
                    return (pkg != null ? pkg + "." : "?") + "  " + className + "  ->  " + name;
                })
                .toList());

        Files.write(dir.resolve("builder-entities.txt"), report.builderEntities().stream().sorted().toList());
        Files.write(dir.resolve("exporter-entities.txt"), report.exporterEntities().stream().sorted().toList());
        Files.write(dir.resolve("tested-entities.txt"), report.testedEntities().stream().sorted().toList());
        Files.write(dir.resolve("registered-not-built.txt"), report.registeredNotBuilt().stream().sorted().toList());
        Files.write(dir.resolve("built-not-previewed.txt"), report.builtNotPreviewed().stream().sorted().toList());
        Files.write(dir.resolve("previewed-not-tested.txt"), report.previewedNotTested().stream().sorted().toList());

        writeGapReport(dir.resolve("ap242-gap-report.md"), report);
        writeSupportMatrix(dir.resolve("step-support-matrix.md"), report);
    }

    static void writeGapReport(Path path, CoverageReport report) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("# AP242 Gap Report\n\n");
        sb.append("Generated by CoverageAnalyzer from static source scanning.\n\n");

        sb.append("## Summary\n\n");
        sb.append("| Metric | Count |\n");
        sb.append("|--------|-------|\n");
        sb.append("| AP242 standard entities | ").append(report.standardEntities().size()).append(" |\n");
        sb.append("| Resolver registrations | ").append(report.registeredEntities().size()).append(" |\n");
        sb.append("| Builder entity references | ").append(report.builderEntities().size()).append(" |\n");
        sb.append("| Exporter entity references | ").append(report.exporterEntities().size()).append(" |\n");
        sb.append("| Test entity references | ").append(report.testedEntities().size()).append(" |\n");
        sb.append("| Model classes | ").append(report.modelClasses().size()).append(" |\n");
        sb.append("| Resolved only | ").append(report.resolvedOnly().size()).append(" |\n");
        sb.append("| Resolved and built | ").append(report.resolvedAndBuilt().size()).append(" |\n");
        sb.append("| Resolved, built, previewed | ").append(report.resolvedBuiltPreviewed().size()).append(" |\n");
        sb.append("| Resolved, built, previewed, tested | ").append(report.resolvedBuiltPreviewedTested().size()).append(" |\n\n");

        appendEntitySection(sb, "Unregistered", "Standard entities missing from resolver registration.", report.unregistered(), 80);
        appendEntitySection(sb, "Registered But Not Built", "Registered in resolver but not detected in builder source.", report.registeredNotBuilt(), 80);
        appendEntitySection(sb, "Built But Not Previewed", "Detected in builder but not in preview/export source.", report.builtNotPreviewed(), 80);
        appendEntitySection(sb, "Previewed But Not Tested", "Detected in exporter but not in tests.", report.previewedNotTested(), 80);
        appendEntitySection(sb, "Alias Covered", "Registered via alias helper instead of a dedicated resolver binding.", report.aliasCovered(), 80);
        appendEntitySection(sb, "MiniCAD Non-Standard", "Registered aliases or local extensions outside the AP242 schema.", report.nonStandard(), 80);

        Files.writeString(path, sb.toString());
    }

    static void writeSupportMatrix(Path path, CoverageReport report) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("# STEP Support Matrix\n\n");
        sb.append("Generated by CoverageAnalyzer from static source scanning.\n\n");
        sb.append("Columns:\n\n");
        sb.append("- `resolver`: entity name found in `StepEntityResolver`\n");
        sb.append("- `model`: corresponding `StepXxx` model class found under `step.model`\n");
        sb.append("- `builder`: entity name referenced in `StepCadBuilder`\n");
        sb.append("- `exporter`: entity name referenced in `StepPreviewJsonExporter`\n");
        sb.append("- `tests`: entity name referenced under `src/test/java`\n");
        sb.append("- `stage`: coarse end-to-end status derived from the above columns\n\n");

        sb.append("| Entity | Standard | Resolver | Alias | Model | Builder | Exporter | Tests | Stage |\n");
        sb.append("|--------|----------|----------|-------|-------|---------|----------|-------|-------|\n");

        for (EntityCoverage entity : report.coverageByEntity().values()) {
            sb.append("| `").append(entity.name()).append("`");
            sb.append(" | ").append(bool(entity.standard()));
            sb.append(" | ").append(bool(entity.resolved()));
            sb.append(" | ").append(bool(entity.alias()));
            sb.append(" | ").append(bool(entity.model()));
            sb.append(" | ").append(bool(entity.built()));
            sb.append(" | ").append(bool(entity.exported()));
            sb.append(" | ").append(bool(entity.tested()));
            sb.append(" | `").append(entity.stage()).append("` |\n");
        }

        Files.writeString(path, sb.toString());
    }

    static void appendEntitySection(StringBuilder sb,
                                    String title,
                                    String description,
                                    Set<String> entities,
                                    int maxShow) {
        sb.append("## ").append(title).append(" (").append(entities.size()).append(")\n\n");
        sb.append(description).append("\n\n");
        writeEntityList(sb, entities, maxShow);
    }

    static void writeEntityList(StringBuilder sb, Set<String> entities, int maxShow) {
        if (entities.isEmpty()) {
            sb.append("*(none)*\n\n");
            return;
        }
        int count = 0;
        for (String entity : entities) {
            if (count >= maxShow) {
                sb.append("\n... and ").append(entities.size() - maxShow).append(" more\n\n");
                return;
            }
            sb.append("- `").append(entity).append("`\n");
            count++;
        }
        sb.append("\n");
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    static String bool(boolean value) {
        return value ? "Y" : "";
    }

    static String toExpressName(String className) {
        String name = className.startsWith("Step") ? className.substring(4) : className;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append('_');
            }
            sb.append(Character.toUpperCase(c));
        }
        return sb.toString();
    }

    static String toClassName(String expressName) {
        String[] parts = expressName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            sb.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                sb.append(part.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return sb.toString();
    }

    static String findModelPackage(String className) {
        Path modelBase = Path.of(MODEL_BASE);
        try (Stream<Path> stream = Files.walk(modelBase)) {
            return stream.filter(path -> path.toString().endsWith(className + ".java"))
                .findFirst()
                .map(path -> {
                    Path relative = modelBase.relativize(path.getParent());
                    return "com.minicad.step.model." + relative.toString().replace(File.separatorChar, '.');
                })
                .orElse(null);
        } catch (IOException ignored) {
            return null;
        }
    }

    // =========================================================================
    // Data holders
    // =========================================================================

    record EntityCoverage(String name,
                          boolean standard,
                          boolean resolved,
                          boolean alias,
                          boolean model,
                          boolean built,
                          boolean exported,
                          boolean tested,
                          String stage) {
    }

    record CoverageReport(Set<String> standardEntities,
                          Map<String, String> registeredEntities,
                          Set<String> modelClasses,
                          Set<String> builderEntities,
                          Set<String> exporterEntities,
                          Set<String> testedEntities,
                          Map<String, EntityCoverage> coverageByEntity,
                          Set<String> unregistered,
                          Set<String> aliasCovered,
                          Set<String> registeredNoModel,
                          Set<String> registeredWithModel,
                          Set<String> nonStandard,
                          Set<String> resolvedOnly,
                          Set<String> resolvedAndBuilt,
                          Set<String> resolvedBuiltPreviewed,
                          Set<String> resolvedBuiltPreviewedTested,
                          Set<String> registeredNotBuilt,
                          Set<String> builtNotPreviewed,
                          Set<String> previewedNotTested) {
    }
}
