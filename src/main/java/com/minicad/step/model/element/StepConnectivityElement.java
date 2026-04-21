package com.minicad.step.model.element;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved CONNECTIVITY_ELEMENT.
 * A connectivity finite element.
 */
public record StepConnectivityElement(
    int id,
    String name,
    List<StepEntity> nodes,
    StepEntity elementProperty) implements StepEntity {
}
