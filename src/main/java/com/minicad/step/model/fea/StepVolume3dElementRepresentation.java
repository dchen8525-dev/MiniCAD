package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved VOLUME_3D_ELEMENT_REPRESENTATION.
 * A representation of 3D volume finite elements.
 */
public record StepVolume3dElementRepresentation(
    int id,
    String name,
    List<StepEntity> elements,
    StepEntity mesh
) implements StepEntity {

    public StepVolume3dElementRepresentation {
        elements = List.copyOf(elements);
    }
}
