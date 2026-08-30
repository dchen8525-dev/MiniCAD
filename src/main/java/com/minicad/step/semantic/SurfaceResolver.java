package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.common.UnsupportedStepEntityException;
import java.util.List;

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

  StepBoundedSurface resolveBoundedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "BOUNDED_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepBoundedSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0));
  }
  // === Swept / Projected Surface Entities ===

  StepOffsetSurface2 resolveOffsetSurface2(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "OFFSET_SURFACE_2");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    StepEntity basisSurface = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "OFFSET_SURFACE_2 basis_surface must reference a supported surface");
    }
    return new StepOffsetSurface2(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        basisSurface,
        resolver.numberValue(instance, definition, 3),
        resolver.booleanValue(instance, definition, 4));
  }

  StepRuledSurface resolveRuledSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RULED_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepRuledSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "RULED_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepSurfaceOfConstantRadius resolveSurfaceOfConstantRadius(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_OF_CONSTANT_RADIUS");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity sweptSurface = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedSurfaceReference(sweptSurface)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_OF_CONSTANT_RADIUS swept_surface must reference a supported surface");
    }
    return new StepSurfaceOfConstantRadius(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        sweptSurface,
        resolver.numberValue(instance, definition, 3));
  }

  StepSurfaceOfLinearExtrusion resolveSurfaceOfLinearExtrusion(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_OF_LINEAR_EXTRUSION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity sweptCurve = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedCurveReference(sweptCurve)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_OF_LINEAR_EXTRUSION swept_curve must reference a supported curve");
    }
    return new StepSurfaceOfLinearExtrusion(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        sweptCurve,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepVector.class,
            "SURFACE_OF_LINEAR_EXTRUSION extrusion_axis must reference VECTOR"));
  }

  StepSurfaceOfProjection resolveSurfaceOfProjection(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_OF_PROJECTION");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    StepEntity profile = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedCurveReference(profile)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_OF_PROJECTION profile must reference a supported curve");
    }
    return new StepSurfaceOfProjection(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        profile,
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepSurfaceOfRevolution resolveSurfaceOfRevolution(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_OF_REVOLUTION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity sweptCurve = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedCurveReference(sweptCurve)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_OF_REVOLUTION swept_curve must reference a supported curve");
    }
    return new StepSurfaceOfRevolution(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        sweptCurve,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepAxis1Placement.class,
            "SURFACE_OF_REVOLUTION axis_position must reference AXIS1_PLACEMENT"));
  }

  StepSurfaceOfTranslation resolveSurfaceOfTranslation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_OF_TRANSLATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity profile = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedCurveReference(profile)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_OF_TRANSLATION profile must reference a supported curve");
    }
    return new StepSurfaceOfTranslation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        profile,
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  // === Bounded / Trimmed Surface Entities ===

  StepCurveBoundedSurface resolveCurveBoundedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CURVE_BOUNDED_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity basisSurface = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "CURVE_BOUNDED_SURFACE basis_surface must reference a supported surface");
    }
    List<StepEntity> boundaries =
        resolver.entityReferenceList(
            instance,
            definition,
            2,
            "CURVE_BOUNDED_SURFACE boundaries must contain entity references");
    if (boundaries.isEmpty()) {
      throw new StepResolutionException("CURVE_BOUNDED_SURFACE boundaries must not be empty");
    }
    for (StepEntity boundary : boundaries) {
      if (!(boundary instanceof StepPcurve)
          && !(boundary instanceof StepSurfaceCurve)
          && !(boundary instanceof StepSeamCurve)
          && !(boundary instanceof StepCompositeCurveOnSurface)
          && !(boundary instanceof StepCompositeCurve)
          && !StepResolverValueHelpers.isSupportedCurveReference(boundary)) {
        throw new UnsupportedStepEntityException(
            "CURVE_BOUNDED_SURFACE boundaries must reference supported curve entities");
      }
    }
    return new StepCurveBoundedSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        basisSurface,
        boundaries,
        resolver.booleanValue(instance, definition, 3));
  }

  StepRectangularCompositeSurface resolveRectangularCompositeSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RECTANGULAR_COMPOSITE_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    StepEntity parentSurface = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedSurfaceReference(parentSurface)) {
      throw new UnsupportedStepEntityException(
          "RECTANGULAR_COMPOSITE_SURFACE parent_surface must reference a supported surface");
    }
    return new StepRectangularCompositeSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        parentSurface,
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4),
        resolver.numberValue(instance, definition, 5),
        resolver.numberValue(instance, definition, 6));
  }

  StepRectangularTrimmedSurface resolveRectangularTrimmedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RECTANGULAR_TRIMMED_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    StepEntity basisSurface = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "RECTANGULAR_TRIMMED_SURFACE basis_surface must reference a supported surface");
    }
    return new StepRectangularTrimmedSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        basisSurface,
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4),
        resolver.numberValue(instance, definition, 5),
        resolver.booleanValue(instance, definition, 6),
        resolver.booleanValue(instance, definition, 7));
  }

  StepSurfacePatch resolveSurfacePatch(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_PATCH");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity basisSurface = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_PATCH basis_surface must reference a supported surface");
    }
    return new StepSurfacePatch(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        basisSurface,
        resolver.booleanValue(instance, definition, 3));
  }

  // === Surface Model Entities ===

  StepGeometricSurfaceSet resolveGeometricSurfaceSet(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GEOMETRIC_SURFACE_SET");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepEntity> elements =
        resolver.entityReferenceList(
            instance, definition, 1, "GEOMETRIC_SURFACE_SET elements must contain entity references");
    for (StepEntity element : elements) {
      if (!StepResolverValueHelpers.isSupportedSurfaceReference(element)) {
        throw new UnsupportedStepEntityException(
            "GEOMETRIC_SURFACE_SET elements must be supported surfaces");
      }
    }
    return new StepGeometricSurfaceSet(instance.id(), resolver.stringValue(instance, definition, 0), elements);
  }

  StepMachinedSurface resolveMachinedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MACHINED_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    int faceId = resolver.referenceId(instance, definition, 1);
    return new StepMachinedSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(faceId));
  }

  StepSurface resolveSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    return new StepSurface(instance.id(), resolver.inheritedRepresentationItemName(instance));
  }

  StepSurfaceModel resolveSurfaceModel(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_MODEL");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    return new StepSurfaceModel(instance.id(), resolver.inheritedRepresentationItemName(instance));
  }
}
