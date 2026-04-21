package com.minicad.step.model.element;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved UNIFORM_VOLUME_ELEMENT.
 * A uniform volume finite element.
 */
public record StepUniformVolumeElement(
    int id,
    String name,
    List<StepEntity> nodes,
    StepEntity elementProperty) implements StepEntity {
}
