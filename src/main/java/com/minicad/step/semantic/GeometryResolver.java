package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.step.syntax.StepValue;

import java.util.List;

/**
 * Geometry resolver - sample extraction from StepEntityResolver.
 * Demonstrates the refactoring pattern for extracting specialized resolvers.
 *
 * This class shows how resolve methods can be extracted into dedicated
 * resolver classes, reducing the size of the main StepEntityResolver.
 * Full extraction would include ~150 geometry resolve methods.
 */
final class GeometryResolver {

  private final StepEntityResolver resolver;

  GeometryResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Basic Point/Direction/Vector ===

  StepCartesianPoint resolveCartesianPoint(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CARTESIAN_POINT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepCartesianPoint(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        StepResolverValueHelpers.coordinateList(instance, definition, 1, 2, 3));
  }

  StepDirection resolveDirection(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DIRECTION");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepDirection(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        StepResolverValueHelpers.coordinateList(instance, definition, 1, 2, 3));
  }

  StepVector resolveVector(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "VECTOR");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepVector(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepDirection.class,
            "VECTOR orientation must reference DIRECTION"),
        resolver.numberValue(instance, definition, 2));
  }

  // === Axis Placements ===

  StepAxis2Placement3D resolveAxis2Placement3D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "AXIS2_PLACEMENT_3D");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    // Per ISO 10303-42, axis and ref_direction are OPTIONAL. When omitted they
    // default to (0,0,1) and the projection of (1,0,0) onto the plane normal to
    // axis. Applying the defaults keeps valid STEP files parseable instead of
    // being rejected, and guarantees non-null axis/refDirection downstream
    // (so matrixForPlacement / convert2DPlacementTo3D can never NPE on them).
    StepCartesianPoint location = resolver.requireEntity(
        resolver.referenceId(instance, definition, 1),
        StepCartesianPoint.class,
        "AXIS2_PLACEMENT_3D location must reference CARTESIAN_POINT");
    StepDirection axis = StepResolverValueHelpers.isUnset(definition.parameters().get(2))
        ? defaultAxis()
        : resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepDirection.class,
            "AXIS2_PLACEMENT_3D axis must reference DIRECTION");
    StepDirection refDirection = StepResolverValueHelpers.isUnset(definition.parameters().get(3))
        ? defaultRefDirection(axis)
        : resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepDirection.class,
            "AXIS2_PLACEMENT_3D ref direction must reference DIRECTION");
    return new StepAxis2Placement3D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        location,
        axis,
        refDirection);
  }

  StepAxis1Placement resolveAxis1Placement(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "AXIS1_PLACEMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepAxis1Placement(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "AXIS1_PLACEMENT location must reference CARTESIAN_POINT"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepDirection.class,
            "AXIS1_PLACEMENT axis must reference DIRECTION"));
  }

  StepAxis2Placement2D resolveAxis2Placement2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "AXIS2_PLACEMENT_2D");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    // Per ISO 10303-42, ref_direction is OPTIONAL and defaults to (1,0).
    StepCartesianPoint location = resolver.requireEntity(
        resolver.referenceId(instance, definition, 1),
        StepCartesianPoint.class,
        "AXIS2_PLACEMENT_2D location must reference CARTESIAN_POINT");
    StepDirection refDirection = StepResolverValueHelpers.isUnset(definition.parameters().get(2))
        ? new StepDirection(0, "", List.of(1.0, 0.0))
        : resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepDirection.class,
            "AXIS2_PLACEMENT_2D ref direction must reference DIRECTION");
    return new StepAxis2Placement2D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        location,
        refDirection);
  }

  private static StepDirection defaultAxis() {
    return new StepDirection(0, "", List.of(0.0, 0.0, 1.0));
  }

  private static StepDirection defaultRefDirection(StepDirection axis) {
    List<Double> r = axis != null ? axis.directionRatios() : null;
    double ax = 0.0, ay = 0.0, az = 1.0;
    if (r != null && r.size() >= 3) {
      ax = r.get(0); ay = r.get(1); az = r.get(2);
      double len = Math.sqrt(ax * ax + ay * ay + az * az);
      if (len > 1e-12) {
        ax /= len; ay /= len; az /= len;
      } else {
        ax = 0.0; ay = 0.0; az = 1.0;
      }
    }
    // Seed = X axis; if axis is parallel to X, fall back to Y axis.
    double sx = 1.0, sy = 0.0, sz = 0.0;
    double dot = ax * sx + ay * sy + az * sz;
    if (Math.abs(dot) > 0.999) {
      sx = 0.0; sy = 1.0; sz = 0.0;
      dot = ax * sx + ay * sy + az * sz;
    }
    double rx = sx - dot * ax, ry = sy - dot * ay, rz = sz - dot * az;
    double rlen = Math.sqrt(rx * rx + ry * ry + rz * rz);
    if (rlen < 1e-12) {
      rx = 1.0; ry = 0.0; rz = 0.0;
    } else {
      rx /= rlen; ry /= rlen; rz /= rlen;
    }
    return new StepDirection(0, "", List.of(rx, ry, rz));
  }

  // === Basic Curves ===

  StepLine resolveLine(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "LINE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepLine(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "LINE pnt must reference CARTESIAN_POINT"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepVector.class,
            "LINE dir must reference VECTOR"));
  }

  StepPolyline resolvePolyline(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "POLYLINE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepCartesianPoint> points = resolver.referenceList(
        instance, definition, 1, StepCartesianPoint.class,
        "POLYLINE points must reference CARTESIAN_POINT");
    return new StepPolyline(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        points);
  }

  // === Basic Curves (Circle and Ellipse kept here) ===

  StepCircle resolveCircle(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CIRCLE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity position = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(position instanceof StepAxis2Placement3D)
        && !(position instanceof StepAxis2Placement2D)) {
      throw new StepResolutionException(
          "CIRCLE position must reference AXIS2_PLACEMENT_3D or AXIS2_PLACEMENT_2D");
    }
    return new StepCircle(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        position,
        resolver.numberValue(instance, definition, 2));
  }

  StepEllipse resolveEllipse(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ELLIPSE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity position = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(position instanceof StepAxis2Placement3D)
        && !(position instanceof StepAxis2Placement2D)) {
      throw new StepResolutionException(
          "ELLIPSE position must reference AXIS2_PLACEMENT_3D or AXIS2_PLACEMENT_2D");
    }
    return new StepEllipse(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        position,
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  // === 2D Geometry ===

  StepCircle2D resolveCircle2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CIRCLE_2D");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepCircle2D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement2D.class,
            "CIRCLE_2D position must reference AXIS2_PLACEMENT_2D"),
        resolver.numberValue(instance, definition, 2));
  }

  StepEllipse2D resolveEllipse2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ELLIPSE_2D");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepEllipse2D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement2D.class,
            "ELLIPSE_2D position must reference AXIS2_PLACEMENT_2D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepLine2D resolveLine2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "LINE_2D");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepLine2D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "LINE_2D point must reference CARTESIAN_POINT"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepDirection.class,
            "LINE_2D direction must reference DIRECTION"));
  }

  StepPolyline2D resolvePolyline2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "POLYLINE_2D");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepPolyline2D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepCartesianPoint.class,
            "POLYLINE_2D points must reference CARTESIAN_POINT"));
  }

  StepBoundedSurface resolveBoundedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "BOUNDED_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    return new StepBoundedSurface(instance.id(), resolver.inheritedRepresentationItemName(instance));
  }

  // === 2D B-Spline Curves ===

  StepBSplineCurve2D resolveBSplineCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "B_SPLINE_CURVE_2D");
    StepEntityResolver.requireParameterCountIn(instance, definition, 4, 5);
    boolean hasName = definition.parameters().size() == 5;
    return new StepBSplineCurve2D(
        instance.id(),
        hasName ? resolver.stringValue(instance, definition, 0) : "",
        resolver.integerValue(instance, definition, hasName ? 1 : 0),
        resolver.referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE_2D control points must reference CARTESIAN_POINT"),
        resolver.enumValue(instance, definition, hasName ? 3 : 2));
  }

  StepRationalBSplineCurve2D resolveRationalBSplineCurve2D(StepEntityInstance instance) {
    StepEntityDefinition rational = resolver.definition(instance, "RATIONAL_B_SPLINE_CURVE_2D");
    StepEntityResolver.requireParameterCount(instance, rational, 1);
    StepEntityDefinition base = resolver.definition(instance, "B_SPLINE_CURVE_2D");
    StepEntityResolver.requireParameterCountIn(instance, base, 4, 5);
    boolean hasName = base.parameters().size() == 5;
    return new StepRationalBSplineCurve2D(
        instance.id(),
        hasName ? resolver.stringValue(instance, base, 0) : "",
        resolver.integerValue(instance, base, hasName ? 1 : 0),
        resolver.referenceList(
            instance,
            base,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "RATIONAL_B_SPLINE_CURVE_2D control points must reference CARTESIAN_POINT"),
        resolver.numberList(instance, rational, 0),
        resolver.enumValue(instance, base, hasName ? 3 : 2));
  }

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
