package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

/**
 * Resolver for FEA boundary condition entities in STEP files.
 * Handles resolution of displacement, velocity, acceleration, force, pressure, and thermal boundary conditions.
 */
class BoundaryConditionResolver {

    private final StepEntityResolver resolver;

    BoundaryConditionResolver(StepEntityResolver resolver) {
        this.resolver = resolver;
    }

    StepDisplacementBoundaryCondition resolveDisplacementBoundaryCondition(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "DISPLACEMENT_BOUNDARY_CONDITION");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepDisplacementBoundaryCondition(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.numberValue(instance, definition, 2),
            resolver.numberValue(instance, definition, 3),
            resolver.numberValue(instance, definition, 4));
    }

    StepVelocityBoundaryCondition resolveVelocityBoundaryCondition(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "VELOCITY_BOUNDARY_CONDITION");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepVelocityBoundaryCondition(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.numberValue(instance, definition, 2),
            resolver.numberValue(instance, definition, 3),
            resolver.numberValue(instance, definition, 4));
    }

    StepAccelerationBoundaryCondition resolveAccelerationBoundaryCondition(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "ACCELERATION_BOUNDARY_CONDITION");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepAccelerationBoundaryCondition(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.numberValue(instance, definition, 2),
            resolver.numberValue(instance, definition, 3),
            resolver.numberValue(instance, definition, 4));
    }

    StepForceBoundaryCondition resolveForceBoundaryCondition(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "FORCE_BOUNDARY_CONDITION");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepForceBoundaryCondition(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.numberValue(instance, definition, 2),
            resolver.numberValue(instance, definition, 3),
            resolver.numberValue(instance, definition, 4));
    }

    StepPressureBoundaryCondition resolvePressureBoundaryCondition(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "PRESSURE_BOUNDARY_CONDITION");
        resolver.requireParameterCount(instance, definition, 3);
        return new StepPressureBoundaryCondition(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.numberValue(instance, definition, 2));
    }

    StepThermalBoundaryCondition resolveThermalBoundaryCondition(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "THERMAL_BOUNDARY_CONDITION");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepThermalBoundaryCondition(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.numberValue(instance, definition, 2),
            resolver.numberValue(instance, definition, 3));
    }
}
