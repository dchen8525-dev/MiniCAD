package com.minicad.step.model;

/**
 * Resolved DATUM.
 * A datum reference used in GD&T.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param description datum description
 * @param target referenced target
 * @param orientation orientation flag
 */
public record StepDatum(
    int id,
    String name,
    String description,
    StepEntity target,
    boolean orientation) implements StepEntity {
}
