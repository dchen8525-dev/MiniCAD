package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

import java.util.List;
import java.util.Locale;

/**
 * Raw entity instance parsed from STEP syntax.
 */
public final class StepEntityInstance {

    private final int id;
    private final List<StepEntityDefinition> definitions;

    public StepEntityInstance(int id, List<StepEntityDefinition> definitions) {
        if (definitions.isEmpty()) {
            throw new StepParseException("entity #" + id + " must contain at least one definition");
        }
        this.id = id;
        this.definitions = List.copyOf(definitions);
    }

    public StepEntityInstance(int id, String name, List<StepValue> parameters) {
        this(id, List.of(new StepEntityDefinition(name, parameters)));
    }

    public int id() {
        return id;
    }

    public List<StepEntityDefinition> definitions() {
        return definitions;
    }

    public boolean isComplex() {
        return definitions.size() > 1;
    }

    public String name() {
        return definitions.stream().map(StepEntityDefinition::name).reduce((left, right) -> left + "+" + right).orElse("");
    }

    public List<StepValue> parameters() {
        return definitions.getFirst().parameters();
    }

    public boolean hasDefinition(String entityName) {
        for (StepEntityDefinition def : definitions) {
            if (equalsAsciiIgnoreCase(def.name(), entityName)) {
                return true;
            }
        }
        return false;
    }

    public StepEntityDefinition requireDefinition(String entityName) {
        for (StepEntityDefinition def : definitions) {
            if (equalsAsciiIgnoreCase(def.name(), entityName)) {
                return def;
            }
        }
        throw new StepParseException(
                "entity #" + id + " does not contain definition " + entityName.toUpperCase(Locale.ROOT)
        );
    }

    private static boolean equalsAsciiIgnoreCase(String a, String b) {
        if (a.length() != b.length()) return false;
        for (int i = 0; i < a.length(); i++) {
            char ca = a.charAt(i);
            char cb = b.charAt(i);
            if (ca != cb) {
                // ASCII case-insensitive: letters differ by bit 5 (0x20)
                if ((ca | 0x20) != (cb | 0x20)) return false;
            }
        }
        return true;
    }
}
