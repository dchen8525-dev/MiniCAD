package com.minicad.step.model;

/**
 * Minimal application protocol definition metadata.
 *
 * @param id STEP instance id
 * @param status protocol status text
 * @param schemaName interpreted model schema name
 * @param year protocol year
 * @param application application context
 */
public record StepApplicationProtocolDefinition(
        int id,
        String status,
        String schemaName,
        int year,
        StepApplicationContext application
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
