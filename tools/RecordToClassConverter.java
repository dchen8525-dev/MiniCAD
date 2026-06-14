package com.minicad.tools;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * Batch converts Java 21 record classes to JDK 11 compatible regular classes.
 *
 * Usage: java RecordToClassConverter <sourceDir> <targetDir>
 *
 * - Converts "public record X(...)" to "public final class X"
 * - Generates private final fields, constructor, getters (getXxx style)
 * - Generates equals, hashCode, toString
 * - Handles compact constructors with validation
 * - Preserves custom methods and static factory methods
 */
public class RecordToClassConverter {

    private static final Pattern RECORD_PATTERN = Pattern.compile(
        "public\\s+(?:final\\s+)?record\\s+(\\w+)\\s*\\(([^)]*)\\)\\s+(implements\\s+[^\\{]+)?\\{",
        Pattern.MULTILINE
    );

    private static final Pattern FIELD_PATTERN = Pattern.compile(
        "(\\w+)\\s+(\\w+)(?:\\s*,\\s*)*"
    );

    private static final Pattern COMPACT_CONSTRUCTOR_PATTERN = Pattern.compile(
        "public\\s+(\\w+)\\s*\\{([^\\}]*(?:\\{[^\\}*\\}[^\\}]*)*)\\}",
        Pattern.MULTILINE
    );

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java RecordToClassConverter <sourceDir> <targetDir>");
            System.exit(1);
        }

        Path sourceDir = Paths.get(args[0]);
        Path targetDir = Paths.get(args[1]);

        Files.walk(sourceDir)
            .filter(p -> p.toString().endsWith(".java"))
            .forEach(p -> convertFile(p, sourceDir, targetDir));

        System.out.println("Conversion complete.");
    }

    private static void convertFile(Path sourceFile, Path sourceDir, Path targetDir) {
        try {
            String content = Files.readString(sourceFile);

            // Check if it's a record class
            Matcher recordMatcher = RECORD_PATTERN.matcher(content);
            if (!recordMatcher.find()) {
                // Not a record, just copy
                Path targetFile = targetDir.resolve(sourceDir.relativize(sourceFile));
                Files.createDirectories(targetFile.getParent());
                Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                return;
            }

            // Convert the record
            String converted = convertRecord(content);

            Path targetFile = targetDir.resolve(sourceDir.relativize(sourceFile));
            Files.createDirectories(targetFile.getParent());
            Files.writeString(targetFile, converted);

            System.out.println("Converted: " + sourceFile);
        } catch (IOException e) {
            System.err.println("Error converting " + sourceFile + ": " + e.getMessage());
        }
    }

    private static String convertRecord(String content) {
        Matcher recordMatcher = RECORD_PATTERN.matcher(content);

        while (recordMatcher.find()) {
            String className = recordMatcher.group(1);
            String params = recordMatcher.group(2);
            String implementsClause = recordMatcher.group(3) != null ? recordMatcher.group(3).trim() : "";

            // Parse fields
            List<FieldInfo> fields = parseFields(params);

            // Find compact constructor if exists
            String compactConstructorBody = findCompactConstructor(content, className);

            // Generate replacement
            StringBuilder replacement = new StringBuilder();
            replacement.append("public final class ").append(className);
            if (!implementsClause.isEmpty()) {
                replacement.append(" ").append(implementsClause);
            }
            replacement.append(" {\n");

            // Fields
            for (FieldInfo f : fields) {
                replacement.append("    private final ").append(f.type).append(" ").append(f.name).append(";\n");
            }
            replacement.append("\n");

            // Constructor
            replacement.append("    public ").append(className).append("(");
            for (int i = 0; i < fields.size(); i++) {
                FieldInfo f = fields.get(i);
                replacement.append(f.type).append(" ").append(f.name);
                if (i < fields.size() - 1) replacement.append(", ");
            }
            replacement.append(") {\n");

            // Compact constructor validation logic (if exists)
            if (compactConstructorBody != null) {
                replacement.append("        ").append(compactConstructorBody).append("\n");
            }

            // Field assignments
            for (FieldInfo f : fields) {
                replacement.append("        this.").append(f.name).append(" = ").append(f.name).append(";\n");
            }
            replacement.append("    }\n\n");

            // Getters (getXxx style)
            for (FieldInfo f : fields) {
                String getterName = "get" + capitalize(f.name);
                replacement.append("    public ").append(f.type).append(" ").append(getterName).append("() {\n");
                replacement.append("        return ").append(f.name).append(";\n");
                replacement.append("    }\n\n");
            }

            // equals
            replacement.append("    @Override\n");
            replacement.append("    public boolean equals(Object o) {\n");
            replacement.append("        if (this == o) return true;\n");
            replacement.append("        if (!(o instanceof ").append(className).append(")) return false;\n");
            replacement.append("        ").append(className).append(" that = (").append(className).append(") o;\n");
            replacement.append("        return ");
            for (int i = 0; i < fields.size(); i++) {
                FieldInfo f = fields.get(i);
                if (f.type.equals("int") || f.type.equals("long") || f.type.equals("double") ||
                    f.type.equals("float") || f.type.equals("boolean") || f.type.equals("char")) {
                    replacement.append(f.name).append(" == that.").append(f.name);
                } else {
                    replacement.append("Objects.equals(").append(f.name).append(", that.").append(f.name).append(")");
                }
                if (i < fields.size() - 1) replacement.append("\n            && ");
            }
            replacement.append(";\n    }\n\n");

            // hashCode
            replacement.append("    @Override\n");
            replacement.append("    public int hashCode() {\n");
            replacement.append("        return Objects.hash(");
            for (int i = 0; i < fields.size(); i++) {
                replacement.append(fields.get(i).name);
                if (i < fields.size() - 1) replacement.append(", ");
            }
            replacement.append(");\n    }\n\n");

            // toString
            replacement.append("    @Override\n");
            replacement.append("    public String toString() {\n");
            replacement.append("        return \"").append(className).append("[");
            for (int i = 0; i < fields.size(); i++) {
                FieldInfo f = fields.get(i);
                replacement.append(f.name).append("=").append(f.name);
                if (i < fields.size() - 1) replacement.append(", ");
            }
            replacement.append("]\";\n    }\n");

            // Replace the record declaration
            content = recordMatcher.group().replaceFirst(
                Pattern.quote(recordMatcher.group()),
                Matcher.quoteReplacement(replacement.toString())
            );
        }

        // Remove compact constructor from body (already merged into regular constructor)
        content = COMPACT_CONSTRUCTOR_PATTERN.matcher(content).replaceAll("");

        return content;
    }

    private static List<FieldInfo> parseFields(String params) {
        List<FieldInfo> fields = new ArrayList<>();
        String[] parts = params.split(",");
        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;
            String[] typeAndName = part.split("\\s+");
            if (typeAndName.length >= 2) {
                fields.add(new FieldInfo(typeAndName[0], typeAndName[1]));
            }
        }
        return fields;
    }

    private static String findCompactConstructor(String content, String className) {
        Pattern p = Pattern.compile("public\\s+" + className + "\\s*\\{([^\\}]*)\\}", Pattern.MULTILINE);
        Matcher m = p.matcher(content);
        if (m.find()) {
            return m.group(1).trim();
        }
        return null;
    }

    private static String capitalize(String s) {
        if (s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static class FieldInfo {
        String type;
        String name;
        FieldInfo(String type, String name) {
            this.type = type;
            this.name = name;
        }
    }
}