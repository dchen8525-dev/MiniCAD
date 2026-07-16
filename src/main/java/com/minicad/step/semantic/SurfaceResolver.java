package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

/**
 * Surface resolver - handles surface geometry entities.
 * Extracted from GeometryResolver to keep each resolver under 1000 lines.
 */
final class SurfaceResolver {

  private final StepEntityResolver resolver;

  SurfaceResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Basic Surfaces ===

  StepPlane resolvePlane(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PLANE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepPlane(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "PLANE position must reference AXIS2_PLACEMENT_3D"));
  }

  // === Additional Surfaces ===

  StepCylindricalSurface resolveCylindricalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CYLINDRICAL_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepCylindricalSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "CYLINDRICAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2));
  }

  StepConicalSurface resolveConicalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONICAL_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepConicalSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "CONICAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepToroidalSurface resolveToroidalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TOROIDAL_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepToroidalSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "TOROIDAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepDegenerateToroidalSurface resolveDegenerateToroidalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DEGENERATE_TOROIDAL_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepDegenerateToroidalSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "DEGENERATE_TOROIDAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        resolver.booleanValue(instance, definition, 4));
  }

  StepSphericalSurface resolveSphericalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SPHERICAL_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepSphericalSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "SPHERICAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2));
  }

  StepCylindricalSurfaceWithEllipticalAxis resolveCylindricalSurfaceWithEllipticalAxis(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepCylindricalSurfaceWithEllipticalAxis(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepConicalSurfaceWithEllipticalAxis resolveConicalSurfaceWithEllipticalAxis(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepConicalSurfaceWithEllipticalAxis(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4));
  }

  StepToroidalSurfaceWithEllipticalAxis resolveToroidalSurfaceWithEllipticalAxis(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepToroidalSurfaceWithEllipticalAxis(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4));
  }

  StepToroidalSurfaceWithCylindricalAxis resolveToroidalSurfaceWithCylindricalAxis(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepToroidalSurfaceWithCylindricalAxis(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis1Placement.class,
            "TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS position must reference AXIS1_PLACEMENT"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepToroidalSurfaceWithSpecifiedBends resolveToroidalSurfaceWithSpecifiedBends(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    return new StepToroidalSurfaceWithSpecifiedBends(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        resolver.resolve(resolver.referenceId(instance, definition, 5)));
  }

  StepSphericalSurfaceWithEllipticalAxis resolveSphericalSurfaceWithEllipticalAxis(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepSphericalSurfaceWithEllipticalAxis(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepParaboloidSurface resolveParaboloidSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PARABOLOID_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepParaboloidSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.numberValue(instance, definition, 2));
  }

  StepHyperboloidSurface resolveHyperboloidSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "HYPERBOLOID_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepHyperboloidSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepSurfaceOfLinearExtrusion resolveSurfaceOfLinearExtrusion(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_OF_LINEAR_EXTRUSION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepSurfaceOfLinearExtrusion(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepVector.class,
            "SURFACE_OF_LINEAR_EXTRUSION extrusion_axis must reference VECTOR"));
  }

  StepSurfaceOfRevolution resolveSurfaceOfRevolution(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_OF_REVOLUTION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepSurfaceOfRevolution(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepAxis1Placement.class,
            "SURFACE_OF_REVOLUTION axis_position must reference AXIS1_PLACEMENT"));
  }

  StepBoundedSurface resolveBoundedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "BOUNDED_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepBoundedSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0));
  }
}