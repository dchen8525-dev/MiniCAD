package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved ELEMENT.
 * A finite element analysis element.
 */
public record StepFeaElement(
    int id,
    String name,
    String elementType,
    List<StepEntity> nodes,
    StepEntity elementProperty) implements StepEntity {
}
