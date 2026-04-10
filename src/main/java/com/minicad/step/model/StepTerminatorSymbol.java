package com.minicad.step.model;

import java.util.List;

/**
 * Minimal TERMINATOR_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param styles presentation style assignments
 * @param item referenced annotation symbol
 * @param annotatedCurve referenced annotation curve occurrence
 */
public record StepTerminatorSymbol(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepAnnotationSymbol item,
        StepEntity annotatedCurve
) implements StepEntity {

    public StepTerminatorSymbol {
        styles = List.copyOf(styles);
    }
}
