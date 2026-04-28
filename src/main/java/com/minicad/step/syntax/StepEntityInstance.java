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
    private final List<String> normalizedDefinitionNames;
    private final String name;

    public StepEntityInstance(int id, List<StepEntityDefinition> definitions) {
        if (definitions.isEmpty()) {
            throw new StepParseException("entity #" + id + " must contain at least one definition");
        }
        this.id = id;
        this.definitions = List.copyOf(definitions);
        this.normalizedDefinitionNames = this.definitions.stream()
                .map(definition -> asciiUpper(definition.name()))
                .toList();
        this.name = buildName(this.definitions);
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

    public List<String> normalizedDefinitionNames() {
        return normalizedDefinitionNames;
    }

    public boolean isComplex() {
        return definitions.size() > 1;
    }

    public String name() {
        return name;
    }

    public List<StepValue> parameters() {
        return definitions.getFirst().parameters();
    }

    public boolean hasDefinition(String entityName) {
        String normalizedEntityName = asciiUpper(entityName);
        for (String normalizedDefinitionName : normalizedDefinitionNames) {
            if (normalizedDefinitionName.equals(normalizedEntityName)) {
                return true;
            }
        }
        return false;
    }

    public StepEntityDefinition requireDefinition(String entityName) {
        String normalizedEntityName = asciiUpper(entityName);
        for (int i = 0; i < definitions.size(); i++) {
            if (normalizedDefinitionNames.get(i).equals(normalizedEntityName)) {
                return definitions.get(i);
            }
        }
        throw new StepParseException(
                "entity #" + id + " does not contain definition " + entityName.toUpperCase(Locale.ROOT)
        );
    }

    private static String asciiUpper(String value) {
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch >= 'a' && ch <= 'z') {
                chars[i] = (char) (ch - ('a' - 'A'));
            }
        }
        return new String(chars);
    }

    private static String buildName(List<StepEntityDefinition> definitions) {
        if (definitions.size() == 1) {
            return definitions.getFirst().name();
        }
        StringBuilder builder = new StringBuilder();
        for (StepEntityDefinition definition : definitions) {
            if (!builder.isEmpty()) {
                builder.append('+');
            }
            builder.append(definition.name());
        }
        return builder.toString();
    }
}
