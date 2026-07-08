package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.core.base.StepEntity;
import com.minicad.step.model.geometry.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

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
        resolver.coordinateList(instance, definition, 1, 2, 3));
  }

  StepDirection resolveDirection(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DIRECTION");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepDirection(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.coordinateList(instance, definition, 1, 2, 3));
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
    if (resolver.isUnset(definition.parameters().get(2)) || resolver.isUnset(definition.parameters().get(3))) {
      throw new UnsupportedStepEntityException(
          "AXIS2_PLACEMENT_3D requires explicit axis and ref direction");
    }
    return new StepAxis2Placement3D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "AXIS2_PLACEMENT_3D location must reference CARTESIAN_POINT"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepDirection.class,
            "AXIS2_PLACEMENT_3D axis must reference DIRECTION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepDirection.class,
            "AXIS2_PLACEMENT_3D ref direction must reference DIRECTION"));
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
    return new StepAxis2Placement2D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "AXIS2_PLACEMENT_2D location must reference CARTESIAN_POINT"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepDirection.class,
            "AXIS2_PLACEMENT_2D ref direction must reference DIRECTION"));
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
}