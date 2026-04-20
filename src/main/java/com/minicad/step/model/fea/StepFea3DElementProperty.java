package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FEA_3D_ELEMENT_PROPERTY.
 */
public record StepFea3DElementProperty(
    int id,
    String name,
    List<StepEntity> properties,
    StepEntity material
) implements StepEntity {

    public StepFea3DElementProperty {
        properties = List.copyOf(properties);
    }
}
