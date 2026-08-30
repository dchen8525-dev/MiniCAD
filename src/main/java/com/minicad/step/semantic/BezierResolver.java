package com.minicad.step.semantic;

import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.List;

/**
 * Bezier resolver - handles Bezier curves/surfaces, Offset curves/surfaces,
 * Oriented geometry, Composite curves, Uniform/QuasiUniform curves/surfaces.
 */
final class BezierResolver {

  private final StepEntityResolver resolver;

  BezierResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Offset Curves ===

  StepOffsetCurve3D resolveOffsetCurve3D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "OFFSET_CURVE_3D");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    StepEntity basisCurve = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedCurveReference(basisCurve)) {
      throw new UnsupportedStepEntityException(
          "OFFSET_CURVE_3D basis_curve must reference a supported curve");
    }
    return new StepOffsetCurve3D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        basisCurve,
        resolver.numberValue(instance, definition, 2),
        resolver.booleanValue(instance, definition, 3),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 4),
            StepDirection.class,
            "OFFSET_CURVE_3D ref_direction must reference DIRECTION"));
  }

  StepOffsetCurve2D resolveOffsetCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "OFFSET_CURVE_2D");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity basisCurve = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedCurveReference(basisCurve)) {
      throw new UnsupportedStepEntityException(
          "OFFSET_CURVE_2D basis_curve must reference a supported curve");
    }
    return new StepOffsetCurve2D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        basisCurve,
        resolver.numberValue(instance, definition, 2),
        resolver.booleanValue(instance, definition, 3));
  }

  // === Oriented Geometry ===

  StepOrientedCurve resolveOrientedCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORIENTED_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    int curveElementId = resolver.referenceId(instance, definition, 1);
    if (curveElementId == instance.id()) {
      throw new UnsupportedStepEntityException(
          "ORIENTED_CURVE curve_element must not self-reference");
    }
    StepEntity curveElement = resolver.resolve(curveElementId);
    if (!StepResolverValueHelpers.isSupportedCurveReference(curveElement)) {
      throw new UnsupportedStepEntityException(
          "ORIENTED_CURVE curve_element must reference a supported curve");
    }
    return new StepOrientedCurve(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        curveElement,
        resolver.booleanValue(instance, definition, 2));
  }

  StepOrientedSurface resolveOrientedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORIENTED_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity surfaceElement = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedSurfaceReference(surfaceElement)) {
      throw new UnsupportedStepEntityException(
          "ORIENTED_SURFACE surface_element must reference a supported surface");
    }
    return new StepOrientedSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        surfaceElement,
        resolver.booleanValue(instance, definition, 2));
  }

  StepOffsetSurface resolveOffsetSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "OFFSET_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity basisSurface = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "OFFSET_SURFACE basis_surface must reference a supported surface");
    }
    return new StepOffsetSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        basisSurface,
        resolver.numberValue(instance, definition, 2),
        resolver.booleanValue(instance, definition, 3));
  }

  // === Composite Curves ===

  StepCompositeCurveSegment resolveCompositeCurveSegment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "COMPOSITE_CURVE_SEGMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity parentCurve = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!StepResolverValueHelpers.isSupportedCurveReference(parentCurve)) {
      throw new UnsupportedStepEntityException(
          "COMPOSITE_CURVE_SEGMENT parent_curve must reference a supported curve");
    }
    return new StepCompositeCurveSegment(
        instance.id(),
        resolver.enumValue(instance, definition, 0),
        resolver.booleanValue(instance, definition, 1),
        parentCurve);
  }

  StepCompositeCurve resolveCompositeCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "COMPOSITE_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepCompositeCurve(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepCompositeCurveSegment.class,
            "COMPOSITE_CURVE segments must contain COMPOSITE_CURVE_SEGMENT references"),
        resolver.booleanValue(instance, definition, 2));
  }

  // === Bezier Curves and Surfaces ===

  StepBezierCurve resolveBezierCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "BEZIER_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_CURVE")) {
      return new StepBezierCurve(instance.id(), resolver.inheritedRepresentationItemName(instance), 0, List.of(), "", false, false);
    }
    StepEntityResolver.ResolvedBSplineCurveData spline = resolver.resolveInheritedBSplineCurveData(instance);
    return new StepBezierCurve(
        instance.id(),
        spline.name(),
        spline.getDegree(),
        spline.getControlPoints(),
        spline.curveForm(),
        spline.closedCurve(),
        spline.selfIntersect());
  }

  StepPiecewiseBezierCurve resolvePiecewiseBezierCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PIECEWISE_BEZIER_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_CURVE")) {
      return new StepPiecewiseBezierCurve(instance.id(), resolver.inheritedRepresentationItemName(instance), 0, List.of(), "", false, false);
    }
    StepEntityResolver.ResolvedBSplineCurveData spline = resolver.resolveInheritedBSplineCurveData(instance);
    return new StepPiecewiseBezierCurve(
        instance.id(),
        spline.name(),
        spline.getDegree(),
        spline.getControlPoints(),
        spline.curveForm(),
        spline.closedCurve(),
        spline.selfIntersect());
  }

  StepQuasiUniformCurve resolveQuasiUniformCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "QUASI_UNIFORM_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_CURVE")) {
      return new StepQuasiUniformCurve(instance.id(), resolver.inheritedRepresentationItemName(instance), 0, List.of(), "", false, false);
    }
    StepEntityResolver.ResolvedBSplineCurveData spline = resolver.resolveInheritedBSplineCurveData(instance);
    return new StepQuasiUniformCurve(
        instance.id(),
        spline.name(),
        spline.getDegree(),
        spline.getControlPoints(),
        spline.curveForm(),
        spline.closedCurve(),
        spline.selfIntersect());
  }

  StepUniformCurve resolveUniformCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "UNIFORM_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_CURVE")) {
      return new StepUniformCurve(instance.id(), resolver.inheritedRepresentationItemName(instance), 0, List.of(), "", false, false);
    }
    StepEntityResolver.ResolvedBSplineCurveData spline = resolver.resolveInheritedBSplineCurveData(instance);
    return new StepUniformCurve(
        instance.id(),
        spline.name(),
        spline.getDegree(),
        spline.getControlPoints(),
        spline.curveForm(),
        spline.closedCurve(),
        spline.selfIntersect());
  }

  StepBoundedSurface resolveBoundedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "BOUNDED_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    return new StepBoundedSurface(instance.id(), resolver.inheritedRepresentationItemName(instance));
  }

  StepUniformSurface resolveUniformSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "UNIFORM_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_SURFACE")) {
      return new StepUniformSurface(instance.id(), resolver.inheritedRepresentationItemName(instance), 0, 0, List.of(), "", false, false, false);
    }
    StepEntityResolver.ResolvedBSplineSurfaceData surface = resolver.resolveInheritedBSplineSurfaceData(instance);
    return new StepUniformSurface(
        instance.id(),
        surface.name(),
        surface.getUDegree(),
        surface.getVDegree(),
        surface.getControlPoints(),
        surface.surfaceForm(),
        surface.uClosed(),
        surface.vClosed(),
        surface.selfIntersect());
  }

  StepBezierSurface resolveBezierSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "BEZIER_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_SURFACE")) {
      return new StepBezierSurface(instance.id(), resolver.inheritedRepresentationItemName(instance), 0, 0, List.of(), "", false, false, false);
    }
    StepEntityResolver.ResolvedBSplineSurfaceData surface = resolver.resolveInheritedBSplineSurfaceData(instance);
    return new StepBezierSurface(
        instance.id(),
        surface.name(),
        surface.getUDegree(),
        surface.getVDegree(),
        surface.getControlPoints(),
        surface.surfaceForm(),
        surface.uClosed(),
        surface.vClosed(),
        surface.selfIntersect());
  }

  StepPiecewiseBezierSurface resolvePiecewiseBezierSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PIECEWISE_BEZIER_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_SURFACE")) {
      return new StepPiecewiseBezierSurface(instance.id(), resolver.inheritedRepresentationItemName(instance), 0, 0, List.of(), "", false, false, false);
    }
    StepEntityResolver.ResolvedBSplineSurfaceData surface = resolver.resolveInheritedBSplineSurfaceData(instance);
    return new StepPiecewiseBezierSurface(
        instance.id(),
        surface.name(),
        surface.getUDegree(),
        surface.getVDegree(),
        surface.getControlPoints(),
        surface.surfaceForm(),
        surface.uClosed(),
        surface.vClosed(),
        surface.selfIntersect());
  }

  StepQuasiUniformSurface resolveQuasiUniformSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "QUASI_UNIFORM_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_SURFACE")) {
      return new StepQuasiUniformSurface(instance.id(), resolver.inheritedRepresentationItemName(instance), 0, 0, List.of(), "", false, false, false);
    }
    StepEntityResolver.ResolvedBSplineSurfaceData surface = resolver.resolveInheritedBSplineSurfaceData(instance);
    return new StepQuasiUniformSurface(
        instance.id(),
        surface.name(),
        surface.getUDegree(),
        surface.getVDegree(),
        surface.getControlPoints(),
        surface.surfaceForm(),
        surface.uClosed(),
        surface.vClosed(),
        surface.selfIntersect());
  }

  // === 2D Bezier and Uniform/QuasiUniform Curves ===

  StepBezierCurve2D resolveBezierCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "BEZIER_CURVE_2D");
    StepEntityResolver.requireParameterCountIn(instance, definition, 3, 4);
    boolean hasName = definition.parameters().size() == 4;
    return new StepBezierCurve2D(
        instance.id(),
        hasName ? resolver.stringValue(instance, definition, 0) : "",
        resolver.integerValue(instance, definition, hasName ? 1 : 0),
        resolver.referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "BEZIER_CURVE_2D control points must reference CARTESIAN_POINT"));
  }

  StepQuasiUniformCurve2D resolveQuasiUniformCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "QUASI_UNIFORM_CURVE_2D");
    StepEntityResolver.requireParameterCountIn(instance, definition, 4, 5);
    boolean hasName = definition.parameters().size() == 5;
    return new StepQuasiUniformCurve2D(
        instance.id(),
        hasName ? resolver.stringValue(instance, definition, 0) : "",
        resolver.integerValue(instance, definition, hasName ? 1 : 0),
        resolver.referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "QUASI_UNIFORM_CURVE_2D control points must reference CARTESIAN_POINT"),
        resolver.enumValue(instance, definition, hasName ? 3 : 2));
  }

  StepUniformCurve2D resolveUniformCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "UNIFORM_CURVE_2D");
    StepEntityResolver.requireParameterCountIn(instance, definition, 4, 5);
    boolean hasName = definition.parameters().size() == 5;
    return new StepUniformCurve2D(
        instance.id(),
        hasName ? resolver.stringValue(instance, definition, 0) : "",
        resolver.integerValue(instance, definition, hasName ? 1 : 0),
        resolver.referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "UNIFORM_CURVE_2D control points must reference CARTESIAN_POINT"),
        resolver.enumValue(instance, definition, hasName ? 3 : 2));
  }
}
