package com.minicad.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Capability scanner for MiniCAD project.
 *
 * Scans and reports:
 * - All model classes
 * - All registry entries
 * - All entity factories
 * - All builder handlers
 * - All exporter handlers
 *
 * Outputs JSON report for documentation generation.
 */
public class CapabilityScanner {

    private static final String PROJECT_ROOT = System.getProperty("user.dir", ".");
    private static final String MODEL_DIR = "src/main/java/com/minicad/step/model";
    private static final String SEMANTIC_DIR = "src/main/java/com/minicad/step/semantic";
    private static final String EXPORTER_DIR = "src/main/java/com/minicad/step/exporter";

    public static void main(String[] args) throws IOException {
        System.out.println("MiniCAD Capability Scanner");
        System.out.println("===========================");

        Map<String, Object> report = new HashMap<>();

        // 1. Count model classes
        int modelClassCount = countModelClasses();
        report.put("modelClasses", modelClassCount);
        System.out.println("Model classes: " + modelClassCount);

        // 2. Scan registry entries
        Map<String, List<String>> registryEntries = scanRegistryEntries();
        int totalRegistryEntries = registryEntries.values().stream()
                .mapToInt(List::size)
                .sum();
        report.put("registryEntries", registryEntries);
        report.put("totalRegistryEntries", totalRegistryEntries);
        System.out.println("Registry entries: " + totalRegistryEntries);

        // 3. Scan entity factories
        List<String> entityFactories = scanEntityFactories();
        report.put("entityFactories", entityFactories);
        report.put("entityFactoryCount", entityFactories.size());
        System.out.println("Entity factories: " + entityFactories.size());

        // 4. Scan builder handlers
        List<String> builderHandlers = scanBuilderHandlers();
        report.put("builderHandlers", builderHandlers);
        report.put("builderHandlerCount", builderHandlers.size());
        System.out.println("Builder handlers: " + builderHandlers.size());

        // 5. Scan exporter handlers
        List<String> exporterHandlers = scanExporterHandlers();
        report.put("exporterHandlers", exporterHandlers);
        report.put("exporterHandlerCount", exporterHandlers.size());
        System.out.println("Exporter handlers: " + exporterHandlers.size());

        // 6. Registry breakdown
        System.out.println("\nRegistry Breakdown:");
        registryEntries.forEach((registry, entries) -> {
            System.out.println("  " + registry + ": " + entries.size() + " entries");
        });

        // 7. Generate JSON report
        String jsonReport = generateJsonReport(report);
        Path outputPath = Paths.get("doc/generated/capability-report.json");
        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, jsonReport.getBytes());
        System.out.println("\nJSON report written to: " + outputPath);

        // 8. Generate summary
        System.out.println("\nSummary:");
        System.out.println("  Model Classes: " + modelClassCount);
        System.out.println("  Registry Entries: " + totalRegistryEntries);
        System.out.println("  Entity Factories: " + entityFactories.size());
        System.out.println("  Builder Handlers: " + builderHandlers.size());
        System.out.println("  Exporter Handlers: " + exporterHandlers.size());
    }

    private static int countModelClasses() throws IOException {
        Path modelPath = Paths.get(PROJECT_ROOT, MODEL_DIR);
        if (!Files.exists(modelPath)) {
            return 0;
        }

        return (int) Files.walk(modelPath)
                .filter(path -> path.toString().endsWith(".java"))
                .count();
    }

    private static Map<String, List<String>> scanRegistryEntries() throws IOException {
        Map<String, List<String>> registries = new HashMap<>();

        Path semanticPath = Paths.get(PROJECT_ROOT, SEMANTIC_DIR);
        if (!Files.exists(semanticPath)) {
            return registries;
        }

        // Find all Registry files
        List<File> registryFiles = Files.walk(semanticPath)
                .filter(path -> path.toString().endsWith("Registry.java") ||
                               path.toString().endsWith("Registry1.java") ||
                               path.toString().endsWith("Registry2.java"))
                .map(Path::toFile)
                .collect(Collectors.toList());

        // Scan each registry file
        Pattern registryPutPattern = Pattern.compile(
                "registry\\.put\\s*\\(\\s*\"([A-Z_]+)\"\\s*,"
        );

        for (File registryFile : registryFiles) {
            String registryName = registryFile.getName()
                    .replace(".java", "")
                    .replace("1", "")
                    .replace("2", "");

            List<String> entries = new ArrayList<>();
            List<String> lines = Files.readAllLines(registryFile.toPath());

            for (String line : lines) {
                Matcher matcher = registryPutPattern.matcher(line);
                if (matcher.find()) {
                    entries.add(matcher.group(1));
                }
            }

            if (!entries.isEmpty()) {
                registries.merge(registryName, entries, (old, newEntries) -> {
                    old.addAll(newEntries);
                    return old;
                });
            }
        }

        return registries;
    }

    private static List<String> scanEntityFactories() throws IOException {
        List<String> factories = new ArrayList<>();

        Path semanticPath = Paths.get(PROJECT_ROOT, SEMANTIC_DIR);
        if (!Files.exists(semanticPath)) {
            return factories;
        }

        // Look for resolve methods in StepEntityResolver
        File resolverFile = semanticPath.resolve("StepEntityResolver.java").toFile();
        if (resolverFile.exists()) {
            Pattern resolveMethodPattern = Pattern.compile(
                    "private static StepEntity resolve([A-Z][a-zA-Z]+)\\s*\\("
            );

            List<String> lines = Files.readAllLines(resolverFile.toPath());
            for (String line : lines) {
                Matcher matcher = resolveMethodPattern.matcher(line);
                if (matcher.find()) {
                    factories.add("resolve" + matcher.group(1));
                }
            }
        }

        return factories;
    }

    private static List<String> scanBuilderHandlers() throws IOException {
        List<String> handlers = new ArrayList<>();

        Path semanticPath = Paths.get(PROJECT_ROOT, SEMANTIC_DIR);
        if (!Files.exists(semanticPath)) {
            return handlers;
        }

        // Look for build methods in StepCadBuilder
        File builderFile = semanticPath.resolve("StepCadBuilder.java").toFile();
        if (builderFile.exists()) {
            Pattern buildMethodPattern = Pattern.compile(
                    "private static ([A-Z][a-zA-Z]+) build([A-Z][a-zA-Z]+)\\s*\\("
            );

            List<String> lines = Files.readAllLines(builderFile.toPath());
            for (String line : lines) {
                Matcher matcher = buildMethodPattern.matcher(line);
                if (matcher.find()) {
                    handlers.add("build" + matcher.group(2));
                }
            }
        }

        return handlers;
    }

    private static List<String> scanExporterHandlers() throws IOException {
        List<String> handlers = new ArrayList<>();

        Path exporterPath = Paths.get(PROJECT_ROOT, EXPORTER_DIR);
        if (!Files.exists(exporterPath)) {
            return handlers;
        }

        // Look for export methods in exporter files
        Pattern exportMethodPattern = Pattern.compile(
                "private static void export([A-Z][a-zA-Z]+)\\s*\\(|" +
                "private static String export([A-Z][a-zA-Z]+)\\s*\\("
        );

        List<File> exporterFiles = Files.walk(exporterPath)
                .filter(path -> path.toString().endsWith(".java"))
                .map(Path::toFile)
                .collect(Collectors.toList());

        for (File exporterFile : exporterFiles) {
            List<String> lines = Files.readAllLines(exporterFile.toPath());
            for (String line : lines) {
                Matcher matcher = exportMethodPattern.matcher(line);
                if (matcher.find()) {
                    handlers.add("export" + matcher.group(1));
                }
            }
        }

        return handlers;
    }

    private static String generateJsonReport(Map<String, Object> report) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        // Model classes
        json.append("  \"modelClasses\": ").append(report.get("modelClasses")).append(",\n");

        // Total registry entries
        json.append("  \"totalRegistryEntries\": ").append(report.get("totalRegistryEntries")).append(",\n");

        // Entity factory count
        json.append("  \"entityFactoryCount\": ").append(report.get("entityFactoryCount")).append(",\n");

        // Builder handler count
        json.append("  \"builderHandlerCount\": ").append(report.get("builderHandlerCount")).append(",\n");

        // Exporter handler count
        json.append("  \"exporterHandlerCount\": ").append(report.get("exporterHandlerCount")).append(",\n");

        // Registry breakdown
        json.append("  \"registryBreakdown\": {\n");
        Map<String, List<String>> registries = (Map<String, List<String>>) report.get("registryEntries");
        int registryCount = 0;
        for (Map.Entry<String, List<String>> entry : registries.entrySet()) {
            registryCount++;
            json.append("    \"").append(entry.getKey()).append("\": {\n");
            json.append("      \"count\": ").append(entry.getValue().size()).append(",\n");
            json.append("      \"entities\": [\n");
            for (int i = 0; i < entry.getValue().size(); i++) {
                json.append("        \"").append(entry.getValue().get(i)).append("\"");
                if (i < entry.getValue().size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append("      ]\n");
            json.append("    }");
            if (registryCount < registries.size()) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  }\n");

        json.append("}\n");

        return json.toString();
    }
}
