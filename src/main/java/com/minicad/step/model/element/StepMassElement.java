package com.minicad.step.model.element;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved MASS_ELEMENT.
 * A mass finite element.
 */
public record StepMassElement(
    int id,
    String name,
    List<StepEntity> nodes,
    double mass) implements StepEntity {
}
