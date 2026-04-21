package com.minicad.tools.coverage;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

/**
 * Phase 0 coverage analysis tool.
 * Parses AP242 Ed2 mim_lf.exp, StepEntityResolver.java, and model classes
 * to produce a gap report.
 */
public class CoverageAnalyzer {

    private static final String SCHEMA_PATH = "schemas/ap242ed2_dis2_mim_lf_v1.101.exp";
    private static final String RESOLVER_PATH = "src/main/java/com/minicad/step/semantic/StepEntityResolver.java";
    private static final String MODEL_BASE = "src/main/java/com/minicad/step/model";
    private static final String OUTPUT_DIR = "doc/generated";

    public static void main(String[] args) throws Exception {
        System.out.println("=== MiniCAD STEP AP242 Ed2 Coverage Analyzer ===\n");

        // 1. Parse standard entities from EXPRESS schema
        Set<String> standardEntities = parseSchemaEntities(SCHEMA_PATH);
        System.out.println("[1] Standard entities from AP242 Ed2 schema: " + standardEntities.size());

        // 2. Parse registered entities from StepEntityResolver
        Map<String, String> registeredEntities = parseResolverRegistrations(RESOLVER_PATH);
        System.out.println("[2] Entities registered in resolver: " + registeredEntities.size() + " unique keys");

        // 3. Scan model classes
        Set<String> modelClasses = scanModelClasses(MODEL_BASE);
        System.out.println("[3] StepXxx model classes: " + modelClasses.size());

        // 4. Compute gaps
        GapReport report = computeGapReport(standardEntities, registeredEntities, modelClasses);
        printSummary(report);

        // 5. Write output files
        writeOutputs(OUTPUT_DIR, standardEntities, registeredEntities, modelClasses, report);
        System.out.println("\n[5] Reports written to " + OUTPUT_DIR + "/");
    }

    // =========================================================================
    // 1. Parse EXPRESS schema for ENTITY declarations
    // =========================================================================

    static Set<String> parseSchemaEntities(String schemaPath) throws IOException {
        Set<String> entities = new TreeSet<>();
        // ENTITY lines can end with ; or have SUBTYPE/ABSTRACT on next line
        Pattern entityPattern = Pattern.compile("^ENTITY\\s+(\\w+)");

        try (BufferedReader br = Files.newBufferedReader(Path.of(schemaPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher m = entityPattern.matcher(line);
                if (m.find()) {
                    // EXPRESS schema uses lowercase; convert to UPPERCASE for comparison with resolver
                    entities.add(m.group(1).toUpperCase());
                }
            }
        }
        return entities;
    }

    // =========================================================================
    // 2. Parse registry.put calls from StepEntityResolver.java
    // =========================================================================

    static Map<String, String> parseResolverRegistrations(String resolverPath) throws IOException {
        Map<String, String> registrations = new TreeMap<>();
        // Match registry.put("ENTITY_NAME", ...) — first string arg is the entity name
        Pattern putPattern = Pattern.compile("registry\\.put\\(\\s*\"([A-Z_0-9]+)\"");

        String content = Files.readString(Path.of(resolverPath));
        Matcher m = putPattern.matcher(content);
        while (m.find()) {
            String entityName = m.group(1);
            registrations.put(entityName, "direct");
        }

        // Also capture batch alias registrations — these are calls like:
        // registerRepresentationAliases(registry, true, "ENTITY_A", "ENTITY_B", ...);
        // We parse the string literals in these helper calls
        Map<String, String> aliasRegistrations = parseAliasRegistrations(content);
        for (Map.Entry<String, String> e : aliasRegistrations.entrySet()) {
            registrations.putIfAbsent(e.getKey(), e.getValue());
        }

        return registrations;
    }

    /**
     * Parse batch alias helper calls to extract entity names registered via helpers.
     * These calls look like: registerXxxAliases(registry, ..., "NAME1", "NAME2", ...);
     * We extract all uppercase quoted strings inside these calls.
     */
    static Map<String, String> parseAliasRegistrations(String content) {
        Map<String, String> aliases = new TreeMap<>();
        // Match helper method calls that start with "register" and contain "Aliases" or "register"
        Pattern aliasCallPattern = Pattern.compile(
            "register\\w*\\(.*?\\);",
            Pattern.DOTALL
        );
        // String literal pattern for ALL_CAPS entity names
        Pattern entityNamePattern = Pattern.compile("\"([A-Z][A-Z0-9_]{2,})\"");

        Matcher callMatcher = aliasCallPattern.matcher(content);
        while (callMatcher.find()) {
            String call = callMatcher.group();
            // Only process calls that look like batch registration helpers
            if (!call.contains("registry") && !call.contains("Map")) {
                continue;
            }
            // Skip if this is a registry.put call (handled separately)
            if (call.startsWith("registry.put")) {
                continue;
            }
            Matcher nameMatcher = entityNamePattern.matcher(call);
            while (nameMatcher.find()) {
                String name = nameMatcher.group(1);
                // Filter out non-entity constants like "registry", "Map", etc.
                if (name.length() > 3 && Character.isUpperCase(name.charAt(0))) {
                    aliases.put(name, "alias");
                }
            }
        }
        return aliases;
    }

    // =========================================================================
    // 3. Scan StepXxx model classes
    // =========================================================================

    static Set<String> scanModelClasses(String modelBase) throws IOException {
        Set<String> modelNames = new TreeSet<>();
        Pattern stepClassPattern = Pattern.compile("(?:public\\s+)?(?:abstract\\s+)?(?:final\\s+)?(?:@\\w+\\s+)*"
            + "(?:record|class)\\s+(Step\\w+)");

        try (Stream<Path> stream = Files.walk(Path.of(modelBase))) {
            stream.filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> {
                    try {
                        String content = Files.readString(p);
                        Matcher m = stepClassPattern.matcher(content);
                        if (m.find()) {
                            String className = m.group(1);
                            // Convert StepXxx back to EXPRESS-style name
                            String expressName = toExpressName(className);
                            modelNames.add(expressName);
                        }
                    } catch (IOException e) {
                        // skip
                    }
                });
        }
        return modelNames;
    }

    /**
     * Convert StepXxxClassName to EXPRESS_NAME format.
     * E.g., StepCartesianPoint -> CARTESIAN_POINT
     */
    static String toExpressName(String className) {
        // Remove "Step" prefix
        String name = className.startsWith("Step") ? className.substring(4) : className;
        // Insert underscore before capital letters and convert to uppercase
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

    // =========================================================================
    // 4. Compute gap report
    // =========================================================================

    static GapReport computeGapReport(Set<String> standardEntities,
                                       Map<String, String> registeredEntities,
                                       Set<String> modelClasses) {
        Set<String> unregistered = new TreeSet<>();
        Set<String> aliasCovered = new TreeSet<>();
        Set<String> registeredNoModel = new TreeSet<>();
        Set<String> registeredWithModel = new TreeSet<>();

        for (String entity : standardEntities) {
            String regType = registeredEntities.get(entity);
            if (regType == null) {
                unregistered.add(entity);
            } else if ("alias".equals(regType)) {
                aliasCovered.add(entity);
                if (modelClasses.contains(entity)) {
                    registeredWithModel.add(entity);
                } else {
                    registeredNoModel.add(entity);
                }
            } else {
                // direct registration
                if (modelClasses.contains(entity)) {
                    registeredWithModel.add(entity);
                } else {
                    registeredNoModel.add(entity);
                }
            }
        }

        // Non-standard registered entities (MiniCAD aliases)
        Set<String> nonStandard = new TreeSet<>();
        for (String key : registeredEntities.keySet()) {
            if (!standardEntities.contains(key)) {
                nonStandard.add(key);
            }
        }

        return new GapReport(standardEntities, registeredEntities, modelClasses,
            unregistered, aliasCovered, registeredNoModel, registeredWithModel, nonStandard);
    }

    // =========================================================================
    // 5. Print summary
    // =========================================================================

    static void printSummary(GapReport report) {
        System.out.println("\n=== Coverage Summary ===");
        System.out.println("AP242 Ed2 standard entities:    " + report.standardEntities.size());
        System.out.println("Resolver registrations:         " + report.registeredEntities.size() + " unique keys");
        System.out.println("  (of which standard):          " + (report.registeredEntities.size() - report.nonStandard.size()));
        System.out.println("  (of which MiniCAD aliases):   " + report.nonStandard.size());
        System.out.println("StepXxx model classes:          " + report.modelClasses.size());
        System.out.println();
        System.out.println("--- Gap Classification ---");
        System.out.println("Registered with model class:    " + report.registeredWithModel.size());
        System.out.println("Registered, alias-covered:      " + report.aliasCovered.size());
        System.out.println("Registered, no model class:     " + report.registeredNoModel.size());
        System.out.println("Unregistered (schema gap):      " + report.unregistered.size());
        System.out.println();

        double coveragePct = 100.0 * (report.registeredWithModel.size() + report.aliasCovered.size())
            / report.standardEntities.size();
        System.out.printf("Schema coverage (registered):   %.1f%%%n",
            100.0 * (report.standardEntities.size() - report.unregistered.size()) / report.standardEntities.size());
        System.out.printf("Semantic coverage (w/ model):   %.1f%%%n",
            100.0 * report.registeredWithModel.size() / report.standardEntities.size());
    }

    // =========================================================================
    // 6. Write output files
    // =========================================================================

    static void writeOutputs(String outputDir,
                             Set<String> standardEntities,
                             Map<String, String> registeredEntities,
                             Set<String> modelClasses,
                             GapReport report) throws IOException {
        Path dir = Path.of(outputDir);
        Files.createDirectories(dir);

        // ap242-entities.txt
        Files.write(dir.resolve("ap242-entities.txt"),
            report.standardEntities.stream().sorted().collect(Collectors.toList()));

        // ap242-registered-standard-entities.txt
        List<String> registeredStandard = report.registeredEntities.keySet().stream()
            .filter(standardEntities::contains)
            .sorted()
            .collect(Collectors.toList());
        Files.write(dir.resolve("ap242-registered-standard-entities.txt"), registeredStandard);

        // ap242-model-classes.txt
        List<String> modelLines = report.modelClasses.stream()
            .sorted()
            .map(name -> {
                String className = "Step" + toClassName(name);
                // Find the package
                String pkg = findModelPackage(className);
                return (pkg != null ? pkg + "." : "?") + "  " + className + "  ->  " + name;
            })
            .collect(Collectors.toList());
        Files.write(dir.resolve("ap242-model-classes.txt"), modelLines);

        // ap242-gap-report.md
        writeGapReport(dir.resolve("ap242-gap-report.md"), report);
    }

    static String toClassName(String expressName) {
        // EXPRESS_NAME -> ExpressName
        String[] parts = expressName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                sb.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    sb.append(part.substring(1).toLowerCase());
                }
            }
        }
        return sb.toString();
    }

    static String findModelPackage(String className) {
        // Try to find the package by scanning model directories
        Path modelBase = Path.of(MODEL_BASE);
        try (Stream<Path> stream = Files.walk(modelBase)) {
            return stream.filter(p -> p.toString().endsWith(className + ".java"))
                .findFirst()
                .map(p -> {
                    Path rel = modelBase.relativize(p.getParent());
                    return "com.minicad.step.model." + rel.toString().replace(File.separatorChar, '.');
                })
                .orElse(null);
        } catch (IOException e) {
            return null;
        }
    }

    static void writeGapReport(Path path, GapReport report) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("# AP242 Ed2 Gap Report\n\n");
        sb.append("Generated by CoverageAnalyzer\n\n");

        sb.append("## Summary\n\n");
        sb.append("| Metric | Count |\n");
        sb.append("|--------|-------|\n");
        sb.append("| AP242 Ed2 standard entities | ").append(report.standardEntities.size()).append(" |\n");
        sb.append("| Resolver registrations (unique) | ").append(report.registeredEntities.size()).append(" |\n");
        sb.append("| Registered (standard) | ").append(report.registeredEntities.size() - report.nonStandard.size()).append(" |\n");
        sb.append("| MiniCAD aliases (non-standard) | ").append(report.nonStandard.size()).append(" |\n");
        sb.append("| StepXxx model classes | ").append(report.modelClasses.size()).append(" |\n\n");

        double schemaCoverage = 100.0 * (report.standardEntities.size() - report.unregistered.size())
            / report.standardEntities.size();
        double semanticCoverage = 100.0 * report.registeredWithModel.size()
            / report.standardEntities.size();
        sb.append("| Schema coverage (registered) | ").append(String.format("%.1f%%", schemaCoverage)).append(" |\n");
        sb.append("| Semantic coverage (w/ model) | ").append(String.format("%.1f%%", semanticCoverage)).append(" |\n\n");

        sb.append("## Gap Classification\n\n");

        // Unregistered
        sb.append("### Unregistered (").append(report.unregistered.size()).append(")\n\n");
        sb.append("Standard entities not registered in StepEntityResolver.\n\n");
        writeEntityList(sb, report.unregistered, 50);

        // Registered, alias-covered
        sb.append("### Alias-covered (").append(report.aliasCovered.size()).append(")\n\n");
        sb.append("Registered via batch alias helpers (no dedicated resolver needed).\n\n");
        writeEntityList(sb, report.aliasCovered, 50);

        // Registered, no model class
        sb.append("### Registered, no model class (").append(report.registeredNoModel.size()).append(")\n\n");
        sb.append("Registered in resolver but no dedicated StepXxx model class exists "
            + "(may use generic model or resolve to existing types).\n\n");
        writeEntityList(sb, report.registeredNoModel, 50);

        // Registered with model class
        sb.append("### Registered with model class (").append(report.registeredWithModel.size()).append(")\n\n");
        sb.append("Fully supported: registered in resolver with corresponding StepXxx model.\n\n");
        writeEntityList(sb, report.registeredWithModel, 50);

        // Non-standard aliases
        sb.append("### MiniCAD Aliases (").append(report.nonStandard.size()).append(")\n\n");
        sb.append("Non-standard entity names registered as aliases/variants.\n\n");
        writeEntityList(sb, report.nonStandard, 50);

        Files.writeString(path, sb.toString());
    }

    static void writeEntityList(StringBuilder sb, Set<String> entities, int maxShow) {
        if (entities.isEmpty()) {
            sb.append("*(none)*\n\n");
            return;
        }
        int count = 0;
        for (String e : entities) {
            if (count >= maxShow) {
                sb.append("\n... and ").append(entities.size() - maxShow).append(" more\n");
                break;
            }
            sb.append("- `").append(e).append("`\n");
            count++;
        }
        sb.append("\n");
    }

    // =========================================================================
    // Data holder
    // =========================================================================

    record GapReport(Set<String> standardEntities,
                     Map<String, String> registeredEntities,
                     Set<String> modelClasses,
                     Set<String> unregistered,
                     Set<String> aliasCovered,
                     Set<String> registeredNoModel,
                     Set<String> registeredWithModel,
                     Set<String> nonStandard) {}
}
