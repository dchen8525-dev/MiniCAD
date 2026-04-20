package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ELEMENT_SET.
 * A named set of finite elements.
 */
public record StepElementSet(
    int id,
    String name,
    List<StepEntity> elements
) implements StepEntity {

    public StepElementSet {
        elements = List.copyOf(elements);
    }
}
