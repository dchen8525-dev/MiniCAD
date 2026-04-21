package com.minicad.step.model.element;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved LINE_ELEMENT.
 * A 1D line finite element.
 */
public record StepLineElement(
    int id,
    String name,
    List<StepEntity> nodes,
    StepEntity elementProperty) implements StepEntity {
}
