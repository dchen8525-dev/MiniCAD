package com.minicad.step.model.base;

/**
 * Minimal value representation item.
 *
 * @param id STEP instance id
 * @param name item name
 * @param valueType typed wrapper name
 * @param valueText unwrapped literal text
 */
public record StepValueRepresentationItem(
        int id,
        String name,
        String valueType,
        String valueText
) implements StepEntity {
}
