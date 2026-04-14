package com.minicad.step.model;

/**
 * Resolved COLOR_SPECIFICATION.
 * A color specification with RGB or named values.
 *
 * @param id STEP instance id
 * @param name color name
 * @param red red component (0-1)
 * @param green green component (0-1)
 * @param blue blue component (0-1)
 */
public record StepColorSpecification(
    int id,
    String name,
    double red,
    double green,
    double blue) implements StepEntity {
}
