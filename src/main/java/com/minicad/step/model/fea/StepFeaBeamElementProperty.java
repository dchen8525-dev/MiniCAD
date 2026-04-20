package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FEA_BEAM_ELEMENT_PROPERTY.
 */
public record StepFeaBeamElementProperty(
    int id,
    String name,
    List<StepEntity> properties,
    StepEntity material,
    StepEntity crossSection
) implements StepEntity {

    public StepFeaBeamElementProperty {
        properties = List.copyOf(properties);
    }
}
