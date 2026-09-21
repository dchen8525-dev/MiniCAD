package com.minicad.tool;

/**
 * Naming helpers shared by the code generators.
 *
 * <p>ModelClassGenerator and ResolverMethodGenerator used to carry identical private
 * copies of {@code toCamelCase} and its {@code capitalize} helper. Both now delegate
 * here so the conversions live in one place.
 */
public final class ToolNaming {
    private ToolNaming() {
        // utility class
    }

    public static String toCamelCase(String upper) {
        if (upper == null) return "";
        String[] parts = upper.toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                sb.append(capitalize(part));
            }
        }
        return sb.toString();
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
