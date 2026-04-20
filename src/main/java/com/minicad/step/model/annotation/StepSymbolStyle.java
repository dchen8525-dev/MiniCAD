package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
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
