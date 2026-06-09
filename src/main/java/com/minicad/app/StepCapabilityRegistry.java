package com.minicad.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Declarative STEP entity capability registry.
 */
public final class StepCapabilityRegistry {

    private static final String DEFAULT_RESOURCE = "minicad/capabilities/step-capabilities.tsv";

    private StepCapabilityRegistry() {
    }

    public static Map<String, Capability> loadDefault() {
        InputStream input = StepCapabilityRegistry.class.getClassLoader().getResourceAsStream(DEFAULT_RESOURCE);
        if (input == null) {
            return Map.of();
        }
        try (input) {
            return load(input);
        } catch (IOException ex) {
            throw new IllegalStateException("failed to load STEP capability registry " + DEFAULT_RESOURCE, ex);
        }
    }

    static Map<String, Capability> load(InputStream input) throws IOException {
        Map<String, Capability> capabilities = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split("\\t", -1);
                if (parts.length != 8) {
                    throw new IOException("invalid capability registry row at line " + lineNumber + ": expected 8 tab-separated columns");
                }
                if ("entity".equalsIgnoreCase(parts[0])) {
                    continue;
                }
                Capability capability = new Capability(
                        normalizeEntity(parts[0]),
                        parts[1].trim(),
                        parseBoolean(parts[2], lineNumber),
                        parseBoolean(parts[3], lineNumber),
                        parseBoolean(parts[4], lineNumber),
                        parseBoolean(parts[5], lineNumber),
                        parseBoolean(parts[6], lineNumber),
                        parts[7].trim());
                capabilities.put(capability.entity(), capability);
            }
        }
        return Map.copyOf(capabilities);
    }

    private static String normalizeEntity(String entity) {
        return entity.trim().toUpperCase();
    }

    private static boolean parseBoolean(String text, int lineNumber) throws IOException {
        String normalized = text.trim().toLowerCase();
        if ("true".equals(normalized) || "yes".equals(normalized) || "1".equals(normalized)) {
            return true;
        }
        if ("false".equals(normalized) || "no".equals(normalized) || "0".equals(normalized)) {
            return false;
        }
        throw new IOException("invalid boolean '" + text + "' at line " + lineNumber);
    }

    public record Capability(
            String entity,
            String level,
            boolean parsed,
            boolean resolved,
            boolean built,
            boolean exported,
            boolean tested,
            String limitations) {
    }
}
