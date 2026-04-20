package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal derived unit definition.
 *
 * @param id STEP instance id
 * @param elements unit elements
 * @param unitKind derived unit kind such as FORCE_UNIT
 */
public record StepDerivedUnit(int id, List<StepDerivedUnitElement> elements, String unitKind)
    implements StepEntity {

    public StepDerivedUnit {
        elements = List.copyOf(elements);
    }

    @Override
    public String name() {
        return "";
    }
}
