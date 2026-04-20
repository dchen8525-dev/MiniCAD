package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ELEMENT_VOLUME_3D.
 * A 3D finite element volume (solid/tetrahedral/hexahedral element).
 */
public record StepElementVolume3d(
    int id,
    String name,
    List<StepEntity> nodes,
    String elementType
) implements StepEntity {

    public StepElementVolume3d {
        nodes = List.copyOf(nodes);
    }
}
