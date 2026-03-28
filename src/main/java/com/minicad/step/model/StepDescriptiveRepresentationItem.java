package com.minicad.step.model;

/**
 * Minimal descriptive representation item.
 *
 * @param id STEP instance id
 * @param name item name
 * @param description descriptive text
 */
public record StepDescriptiveRepresentationItem(int id, String name, String description) implements StepEntity {
}
