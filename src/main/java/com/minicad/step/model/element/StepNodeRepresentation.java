package com.minicad.step.model.element;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved NODE_REPRESENTATION.
 * Graphical representation of a finite element node.
 */
public record StepNodeRepresentation(
    int id,
    String name,
    List<StepEntity> representedNodes) implements StepEntity {
}
