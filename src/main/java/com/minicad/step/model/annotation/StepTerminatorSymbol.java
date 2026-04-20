package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal TERMINATOR_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param styles presentation style assignments
 * @param item referenced supported annotation content or occurrence
 * @param annotatedCurve referenced annotation curve occurrence
 */
public record StepTerminatorSymbol(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepEntity item,
        StepEntity annotatedCurve
) implements StepEntity {

    public StepTerminatorSymbol {
        styles = List.copyOf(styles);
    }
}
