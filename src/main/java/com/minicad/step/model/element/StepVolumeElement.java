package com.minicad.step.model.element;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved VOLUME_ELEMENT.
 * A 3D volume finite element.
 */
public record StepVolumeElement(
    int id,
    String name,
    List<StepEntity> nodes,
    StepEntity elementProperty) implements StepEntity {
}
