package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

/**
 * Resolver for visualization entities in STEP files.
 * Handles resolution of light sources, cameras, and view-related entities.
 */
class VisualizationResolver {

    private final StepEntityResolver resolver;

    VisualizationResolver(StepEntityResolver resolver) {
        this.resolver = resolver;
    }

    StepLightSource resolveLightSource(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "LIGHT_SOURCE");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepLightSource(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.numberValue(instance, definition, 2));
    }

    StepLightSourceAmbient resolveLightSourceAmbient(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "LIGHT_SOURCE_AMBIENT");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepLightSourceAmbient(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.numberValue(instance, definition, 2));
    }

    StepLightSourceDirectional resolveLightSourceDirectional(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "LIGHT_SOURCE_DIRECTIONAL");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepLightSourceDirectional(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.numberValue(instance, definition, 2),
            resolver.resolve(resolver.referenceId(instance, definition, 3)));
    }

    StepLightSourcePositional resolveLightSourcePositional(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "LIGHT_SOURCE_POSITIONAL");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepLightSourcePositional(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.numberValue(instance, definition, 2),
            resolver.resolve(resolver.referenceId(instance, definition, 3)));
    }

    StepLightSourceSpot resolveLightSourceSpot(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "LIGHT_SOURCE_SPOT");
        resolver.requireParameterCount(instance, definition, 8);
        return new StepLightSourceSpot(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.numberValue(instance, definition, 2),
            resolver.resolve(resolver.referenceId(instance, definition, 3)),
            resolver.resolve(resolver.referenceId(instance, definition, 4)),
            resolver.numberValue(instance, definition, 5),
            resolver.numberValue(instance, definition, 6));
    }

    StepCameraModelD2 resolveCameraModelD2(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "CAMERA_MODEL_D2");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepCameraModelD2(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.resolve(resolver.referenceId(instance, definition, 2)));
    }

    StepCameraModelD3 resolveCameraModelD3(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "CAMERA_MODEL_D3");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepCameraModelD3(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.resolve(resolver.referenceId(instance, definition, 2)),
            resolver.numberValue(instance, definition, 3));
    }

    StepCameraUsage resolveCameraUsage(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "CAMERA_USAGE");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepCameraUsage(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.stringValue(instance, definition, 1),
            resolver.resolve(resolver.referenceId(instance, definition, 2)));
    }

    StepCameraImage resolveCameraImage(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "CAMERA_IMAGE");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepCameraImage(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.stringValue(instance, definition, 1),
            resolver.integerValue(instance, definition, 2),
            resolver.integerValue(instance, definition, 3));
    }

    StepPlanarBox resolvePlanarBox(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "PLANAR_BOX");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepPlanarBox(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.numberValue(instance, definition, 2),
            resolver.numberValue(instance, definition, 3));
    }

    StepPlanarExtent resolvePlanarExtent(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "PLANAR_EXTENT");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepPlanarExtent(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.numberValue(instance, definition, 1),
            resolver.numberValue(instance, definition, 2));
    }

    StepViewVolume resolveViewVolume(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "VIEW_VOLUME");
        resolver.requireParameterCount(instance, definition, 7);
        return new StepViewVolume(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.numberValue(instance, definition, 1),
            resolver.numberValue(instance, definition, 2),
            resolver.numberValue(instance, definition, 3),
            resolver.numberValue(instance, definition, 4),
            resolver.numberValue(instance, definition, 5));
    }
}