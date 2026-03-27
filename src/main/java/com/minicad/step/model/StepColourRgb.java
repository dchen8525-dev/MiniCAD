package com.minicad.step.model;

/**
 * Minimal RGB colour definition.
 *
 * @param id STEP instance id
 * @param name colour name
 * @param red red channel in [0, 1]
 * @param green green channel in [0, 1]
 * @param blue blue channel in [0, 1]
 */
public record StepColourRgb(int id, String name, double red, double green, double blue) implements StepEntity {
}
