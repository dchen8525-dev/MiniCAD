package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FEA_SHELL_ELEMENT_PROPERTY.
 */
public record StepFeaShellElementProperty(
    int id,
    String name,
    List<StepEntity> properties,
    StepEntity material
) implements StepEntity {

    public StepFeaShellElementProperty {
        properties = List.copyOf(properties);
    }
}
