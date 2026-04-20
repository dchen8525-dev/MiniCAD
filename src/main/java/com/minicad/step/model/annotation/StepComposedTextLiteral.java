package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COMPOSED_TEXT_LITERAL.
 */
public record StepComposedTextLiteral(
    int id,
    String name,
    List<StepEntity> components
) implements StepEntity {

    public StepComposedTextLiteral {
        components = List.copyOf(components);
    }
}
