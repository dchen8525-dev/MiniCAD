package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.unit.StepGlobalUnitAssignedContext;
/**
 * Minimal geometric representation context.
 *
 * @param id STEP instance id
 * @param coordinateSpaceDimension coordinate space dimension
 * @param contextIdentifier context identifier
 * @param contextType context type
 * @param globalUnitAssignedContext optional global unit assignments from the same complex entity
 * @param globalUncertaintyAssignedContext optional global uncertainty assignments from the same complex entity
 */
public record StepGeometricRepresentationContext(
        int id,
        int coordinateSpaceDimension,
        String contextIdentifier,
        String contextType,
        StepGlobalUnitAssignedContext globalUnitAssignedContext,
        StepGlobalUncertaintyAssignedContext globalUncertaintyAssignedContext
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
