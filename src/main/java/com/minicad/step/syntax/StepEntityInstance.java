package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

import java.util.List;
import java.util.Locale;

/**
 * Raw entity instance parsed from STEP syntax.
 *
 * @param id numeric instance id
 * @param definitions one or more simple entity definitions
 */
public record StepEntityInstance(int id, List<StepEntityDefinition> definitions) {

    public StepEntityInstance(int id, String name, List<StepValue> parameters) {
        this(id, List.of(new StepEntityDefinition(name, parameters)));
    }

    /**
     * Creates an immutable entity record.
     */
    public StepEntityInstance {
        definitions = List.copyOf(definitions);
        if (definitions.isEmpty()) {
            throw new StepParseException("entity #" + id + " must contain at least one definition");
        }
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
        return definitions.stream().anyMatch(definition -> definition.name().equalsIgnoreCase(entityName));
    }

    public StepEntityDefinition requireDefinition(String entityName) {
        return definitions.stream()
                .filter(definition -> definition.name().equalsIgnoreCase(entityName))
                .findFirst()
                .orElseThrow(() -> new StepParseException(
                        "entity #" + id + " does not contain definition " + entityName.toUpperCase(Locale.ROOT)
                ));
    }
}
