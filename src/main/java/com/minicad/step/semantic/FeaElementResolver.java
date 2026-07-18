package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

/**
 * Resolver for FEA (Finite Element Analysis) element entities in STEP files.
 * Handles resolution of volume, surface, line, mass, and connectivity elements.
 */
class FeaElementResolver {

    private final StepEntityResolver resolver;

    FeaElementResolver(StepEntityResolver resolver) {
        this.resolver = resolver;
    }

    StepElementVolume resolveElementVolume(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "ELEMENT_VOLUME");
        resolver.requireParameterCount(instance, definition, 2);
        return new StepElementVolume(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.numberValue(instance, definition, 1));
    }

    StepVolumeElement resolveVolumeElement(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "VOLUME_ELEMENT");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepVolumeElement(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.entityReferenceList(instance, definition, 1,
                "VOLUME_ELEMENT nodes must contain entity references"),
            resolver.tryResolveReference(definition.parameters().get(2)));
    }

    StepSurfaceElement resolveSurfaceElement(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "SURFACE_ELEMENT");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepSurfaceElement(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.entityReferenceList(instance, definition, 1,
                "SURFACE_ELEMENT nodes must contain entity references"),
            resolver.tryResolveReference(definition.parameters().get(2)));
    }

    StepLineElement resolveLineElement(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "LINE_ELEMENT");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepLineElement(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.entityReferenceList(instance, definition, 1,
                "LINE_ELEMENT nodes must contain entity references"),
            resolver.tryResolveReference(definition.parameters().get(2)));
    }

    StepMassElement resolveMassElement(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "MASS_ELEMENT");
        resolver.requireParameterCount(instance, definition, 3);
        return new StepMassElement(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.entityReferenceList(instance, definition, 1,
                "MASS_ELEMENT nodes must contain entity references"),
            resolver.numberValue(instance, definition, 2));
    }

    StepConnectivityElement resolveConnectivityElement(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "CONNECTIVITY_ELEMENT");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepConnectivityElement(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.entityReferenceList(instance, definition, 1,
                "CONNECTIVITY_ELEMENT nodes must contain entity references"),
            resolver.tryResolveReference(definition.parameters().get(2)));
    }

    StepElementGeometricDescription resolveElementGeometricDescription(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "ELEMENT_GEOMETRIC_DESCRIPTION");
        resolver.requireParameterCount(instance, definition, 3);
        return new StepElementGeometricDescription(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.stringValue(instance, definition, 1),
            resolver.resolve(resolver.referenceId(instance, definition, 2)));
    }

    StepUniformSurfaceElement resolveUniformSurfaceElement(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "UNIFORM_SURFACE_ELEMENT");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepUniformSurfaceElement(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.entityReferenceList(instance, definition, 1,
                "UNIFORM_SURFACE_ELEMENT nodes must contain entity references"),
            resolver.tryResolveReference(definition.parameters().get(2)));
    }

    StepUniformVolumeElement resolveUniformVolumeElement(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "UNIFORM_VOLUME_ELEMENT");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepUniformVolumeElement(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.entityReferenceList(instance, definition, 1,
                "UNIFORM_VOLUME_ELEMENT nodes must contain entity references"),
            resolver.tryResolveReference(definition.parameters().get(2)));
    }

    StepElementVolume2d resolveElementVolume2d(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "ELEMENT_VOLUME_2D");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepElementVolume2d(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.entityReferenceList(instance, definition, 1,
                "ELEMENT_VOLUME_2D nodes must contain entity references"),
            resolver.stringValue(instance, definition, 2));
    }

    StepElementVolume3d resolveElementVolume3d(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "ELEMENT_VOLUME_3D");
        resolver.requireParameterCount(instance, definition, 4);
        return new StepElementVolume3d(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.entityReferenceList(instance, definition, 1,
                "ELEMENT_VOLUME_3D nodes must contain entity references"),
            resolver.stringValue(instance, definition, 2));
    }

    StepNodeSet resolveNodeSet(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "NODE_SET");
        resolver.requireParameterCount(instance, definition, 3);
        return new StepNodeSet(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.entityReferenceList(instance, definition, 1,
                "NODE_SET nodes must contain entity references"));
    }

    StepElementSet resolveElementSet(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "ELEMENT_SET");
        resolver.requireParameterCount(instance, definition, 3);
        return new StepElementSet(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.entityReferenceList(instance, definition, 1,
                "ELEMENT_SET elements must contain entity references"));
    }
}