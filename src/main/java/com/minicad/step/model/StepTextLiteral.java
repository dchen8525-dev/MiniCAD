package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TEXT_LITERAL.
 * A single text string at a specific placement.
 *
 * @param id STEP instance id
 * @param name text name
 * @param literal the text content
 * @param path text path placement
 */
public record StepTextLiteral(
    int id,
    String name,
    String literal,
    StepEntity path) implements StepEntity {
}
