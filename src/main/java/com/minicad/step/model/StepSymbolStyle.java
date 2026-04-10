package com.minicad.step.model;

/**
 * Minimal SYMBOL_STYLE.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styleOfSymbol symbol style payload
 */
public record StepSymbolStyle(
        int id,
        String name,
        StepEntity styleOfSymbol
) implements StepEntity {
}
