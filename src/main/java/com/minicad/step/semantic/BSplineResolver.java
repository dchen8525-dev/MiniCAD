package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.List;

/**
 * B-Spline resolver - handles B-Spline curves and surfaces including rational variants.
 */
final class BSplineResolver {

  private final StepEntityResolver resolver;

  BSplineResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === B-Spline Curves ===

  StepBSplineCurveWithKnots resolveBSplineCurveWithKnots(StepEntityInstance instance) {
    StepEntityDefinition spline = resolver.definition(instance, "B_SPLINE_CURVE_WITH_KNOTS");
    if (instance.hasDefinition("B_SPLINE_CURVE")) {
      StepEntityResolver.requireParameterCount(instance, spline, 3);
      StepEntityDefinition base = resolver.definition(instance, "B_SPLINE_CURVE");
      StepEntityResolver.requireParameterCountIn(instance, base, 5, 6);
      boolean hasName = base.parameters().size() == 6;
      return new StepBSplineCurveWithKnots(
          instance.id(),
          hasName ? resolver.stringValue(instance, base, 0) : "",
          resolver.integerValue(instance, base, hasName ? 1 : 0),
          resolver.referenceList(
              instance,
              base,
              hasName ? 2 : 1,
              StepCartesianPoint.class,
              "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
          resolver.enumValue(instance, base, hasName ? 3 : 2),
          resolver.booleanValue(instance, base, hasName ? 4 : 3),
          resolver.booleanValue(instance, base, hasName ? 5 : 4),
          resolver.integerList(instance, spline, 0),
          resolver.numberList(instance, spline, 1),
          resolver.enumValue(instance, spline, 2));
    }
    StepEntityResolver.requireParameterCount(instance, spline, 9);
    return new StepBSplineCurveWithKnots(
        instance.id(),
        resolver.stringValue(instance, spline, 0),
        resolver.integerValue(instance, spline, 1),
        resolver.referenceList(
            instance,
            spline,
            2,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
        resolver.enumValue(instance, spline, 3),
        resolver.booleanValue(instance, spline, 4),
        resolver.booleanValue(instance, spline, 5),
        resolver.integerList(instance, spline, 6),
        resolver.numberList(instance, spline, 7),
        resolver.enumValue(instance, spline, 8));
  }

  StepBSplineCurve resolveBSplineCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "B_SPLINE_CURVE");
    StepEntityResolver.requireParameterCountIn(instance, definition, 5, 6);
    boolean hasName = definition.parameters().size() == 6;
    return new StepBSplineCurve(
        instance.id(),
        hasName ? resolver.stringValue(instance, definition, 0) : "",
        resolver.integerValue(instance, definition, hasName ? 1 : 0),
        resolver.referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
        resolver.enumValue(instance, definition, hasName ? 3 : 2),
        resolver.booleanValue(instance, definition, hasName ? 4 : 3),
        resolver.booleanValue(instance, definition, hasName ? 5 : 4));
  }

  StepRationalBSplineCurve resolveRationalBSplineCurve(StepEntityInstance instance) {
    StepEntityDefinition rational = resolver.definition(instance, "RATIONAL_B_SPLINE_CURVE");
    StepEntityResolver.requireParameterCount(instance, rational, 1);
    StepEntityDefinition base = resolver.definition(instance, "B_SPLINE_CURVE");
    StepEntityResolver.requireParameterCountIn(instance, base, 5, 6);
    boolean hasName = base.parameters().size() == 6;
    List<Integer> knotMultiplicities = List.of();
    List<Double> knots = List.of();
    String knotSpec = "";
    if (instance.hasDefinition("B_SPLINE_CURVE_WITH_KNOTS")) {
      StepEntityDefinition knotDefinition = resolver.definition(instance, "B_SPLINE_CURVE_WITH_KNOTS");
      StepEntityResolver.requireParameterCount(instance, knotDefinition, 3);
      knotMultiplicities = resolver.integerList(instance, knotDefinition, 0);
      knots = resolver.numberList(instance, knotDefinition, 1);
      knotSpec = resolver.enumValue(instance, knotDefinition, 2);
    }
    return new StepRationalBSplineCurve(
        instance.id(),
        hasName ? resolver.stringValue(instance, base, 0) : "",
        resolver.integerValue(instance, base, hasName ? 1 : 0),
        resolver.referenceList(
            instance,
            base,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
        resolver.enumValue(instance, base, hasName ? 3 : 2),
        resolver.booleanValue(instance, base, hasName ? 4 : 3),
        resolver.booleanValue(instance, base, hasName ? 5 : 4),
        resolver.numberList(instance, rational, 0),
        knotMultiplicities,
        knots,
        knotSpec);
  }

  // === B-Spline Surfaces ===

  StepBSplineSurfaceWithKnots resolveBSplineSurfaceWithKnots(StepEntityInstance instance) {
    StepEntityDefinition knots = resolver.definition(instance, "B_SPLINE_SURFACE_WITH_KNOTS");
    if (instance.hasDefinition("B_SPLINE_SURFACE")) {
      StepEntityResolver.requireParameterCount(instance, knots, 5);
      StepEntityDefinition base = resolver.definition(instance, "B_SPLINE_SURFACE");
      StepEntityResolver.requireParameterCount(instance, base, 7);
      return new StepBSplineSurfaceWithKnots(
          instance.id(),
          "",
          resolver.integerValue(instance, base, 0),
          resolver.integerValue(instance, base, 1),
          resolver.referenceGrid(
              instance,
              base,
              2,
              StepCartesianPoint.class,
              "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
          resolver.enumValue(instance, base, 3),
          resolver.booleanValue(instance, base, 4),
          resolver.booleanValue(instance, base, 5),
          resolver.booleanValue(instance, base, 6),
          resolver.integerList(instance, knots, 0),
          resolver.integerList(instance, knots, 1),
          resolver.numberList(instance, knots, 2),
          resolver.numberList(instance, knots, 3),
          resolver.enumValue(instance, knots, 4));
    }
    StepEntityResolver.requireParameterCount(instance, knots, 13);
    return new StepBSplineSurfaceWithKnots(
        instance.id(),
        resolver.stringValue(instance, knots, 0),
        resolver.integerValue(instance, knots, 1),
        resolver.integerValue(instance, knots, 2),
        resolver.referenceGrid(
            instance,
            knots,
            3,
            StepCartesianPoint.class,
            "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
        resolver.enumValue(instance, knots, 4),
        resolver.booleanValue(instance, knots, 5),
        resolver.booleanValue(instance, knots, 6),
        resolver.booleanValue(instance, knots, 7),
        resolver.integerList(instance, knots, 8),
        resolver.integerList(instance, knots, 9),
        resolver.numberList(instance, knots, 10),
        resolver.numberList(instance, knots, 11),
        resolver.enumValue(instance, knots, 12));
  }

  StepBSplineSurface resolveBSplineSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "B_SPLINE_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    return new StepBSplineSurface(
        instance.id(),
        "",
        resolver.integerValue(instance, definition, 0),
        resolver.integerValue(instance, definition, 1),
        resolver.referenceGrid(
            instance,
            definition,
            2,
            StepCartesianPoint.class,
            "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
        resolver.enumValue(instance, definition, 3),
        resolver.booleanValue(instance, definition, 4),
        resolver.booleanValue(instance, definition, 5),
        resolver.booleanValue(instance, definition, 6));
  }

  StepRationalBSplineSurface resolveRationalBSplineSurface(StepEntityInstance instance) {
    StepEntityDefinition rational = resolver.definition(instance, "RATIONAL_B_SPLINE_SURFACE");
    StepEntityResolver.requireParameterCount(instance, rational, 1);
    StepEntityDefinition base = resolver.definition(instance, "B_SPLINE_SURFACE");
    StepEntityResolver.requireParameterCount(instance, base, 7);
    List<Integer> uMultiplicities = List.of();
    List<Integer> vMultiplicities = List.of();
    List<Double> uKnots = List.of();
    List<Double> vKnots = List.of();
    String knotSpec = "";
    if (instance.hasDefinition("B_SPLINE_SURFACE_WITH_KNOTS")) {
      StepEntityDefinition knotDefinition = resolver.definition(instance, "B_SPLINE_SURFACE_WITH_KNOTS");
      StepEntityResolver.requireParameterCount(instance, knotDefinition, 5);
      uMultiplicities = resolver.integerList(instance, knotDefinition, 0);
      vMultiplicities = resolver.integerList(instance, knotDefinition, 1);
      uKnots = resolver.numberList(instance, knotDefinition, 2);
      vKnots = resolver.numberList(instance, knotDefinition, 3);
      knotSpec = resolver.enumValue(instance, knotDefinition, 4);
    }
    return new StepRationalBSplineSurface(
        instance.id(),
        "",
        resolver.integerValue(instance, base, 0),
        resolver.integerValue(instance, base, 1),
        resolver.referenceGrid(
            instance,
            base,
            2,
            StepCartesianPoint.class,
            "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
        resolver.enumValue(instance, base, 3),
        resolver.booleanValue(instance, base, 4),
        resolver.booleanValue(instance, base, 5),
        resolver.booleanValue(instance, base, 6),
        StepResolverValueHelpers.numberGrid(instance, rational, 0),
        uMultiplicities,
        vMultiplicities,
        uKnots,
        vKnots,
        knotSpec);
  }

  // === B-Spline with Breakpoints ===

  StepBSplineCurveWithKnotsAndBreakpoints resolveBSplineCurveWithKnotsAndBreakpoints(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS");
    if (instance.hasDefinition("B_SPLINE_CURVE_WITH_KNOTS")) {
      StepEntityResolver.requireParameterCount(instance, definition, 1);
      StepEntityDefinition knots = resolver.definition(instance, "B_SPLINE_CURVE_WITH_KNOTS");
      StepEntityResolver.requireParameterCount(instance, knots, 3);
      StepEntityDefinition base = resolver.definition(instance, "B_SPLINE_CURVE");
      StepEntityResolver.requireParameterCountIn(instance, base, 5, 6);
      boolean hasName = base.parameters().size() == 6;
      return new StepBSplineCurveWithKnotsAndBreakpoints(
          instance.id(),
          hasName ? resolver.stringValue(instance, base, 0) : "",
          resolver.integerValue(instance, base, hasName ? 1 : 0),
          resolver.referenceList(
              instance,
              base,
              hasName ? 2 : 1,
              StepCartesianPoint.class,
              "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS control points must reference CARTESIAN_POINT"),
          resolver.integerList(instance, knots, 0),
          resolver.numberList(instance, knots, 1),
          resolver.numberList(instance, definition, 0),
          resolver.enumValue(instance, base, hasName ? 3 : 2),
          resolver.booleanValue(instance, base, hasName ? 4 : 3),
          resolver.booleanValue(instance, base, hasName ? 5 : 4));
    }
    // Handle case without B_SPLINE_CURVE_WITH_KNOTS supertype
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntityDefinition base = resolver.definition(instance, "B_SPLINE_CURVE");
    StepEntityResolver.requireParameterCountIn(instance, base, 5, 6);
    boolean hasName = base.parameters().size() == 6;
    return new StepBSplineCurveWithKnotsAndBreakpoints(
        instance.id(),
        hasName ? resolver.stringValue(instance, base, 0) : "",
        resolver.integerValue(instance, base, hasName ? 1 : 0),
        resolver.referenceList(
            instance,
            base,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS control points must reference CARTESIAN_POINT"),
        resolver.integerList(instance, definition, 0),
        resolver.numberList(instance, definition, 1),
        resolver.numberList(instance, definition, 2),
        resolver.enumValue(instance, base, hasName ? 3 : 2),
        resolver.booleanValue(instance, base, hasName ? 4 : 3),
        resolver.booleanValue(instance, base, hasName ? 5 : 4));
  }

  StepBSplineSurfaceWithKnotsAndBreakpoints resolveBSplineSurfaceWithKnotsAndBreakpoints(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS");
    if (instance.hasDefinition("B_SPLINE_SURFACE_WITH_KNOTS")) {
      StepEntityResolver.requireParameterCount(instance, definition, 2);
      StepEntityDefinition knots = resolver.definition(instance, "B_SPLINE_SURFACE_WITH_KNOTS");
      StepEntityResolver.requireParameterCount(instance, knots, 5);
      StepEntityDefinition base = resolver.definition(instance, "B_SPLINE_SURFACE");
      StepEntityResolver.requireParameterCount(instance, base, 7);
      return new StepBSplineSurfaceWithKnotsAndBreakpoints(
          instance.id(),
          "",
          resolver.integerValue(instance, base, 0),
          resolver.integerValue(instance, base, 1),
          resolver.referenceGrid(
              instance,
              base,
              2,
              StepCartesianPoint.class,
              "B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS control points must reference CARTESIAN_POINT"),
          resolver.integerList(instance, knots, 0),
          resolver.integerList(instance, knots, 1),
          resolver.numberList(instance, knots, 2),
          resolver.numberList(instance, knots, 3),
          resolver.numberList(instance, definition, 0),
          resolver.numberList(instance, definition, 1),
          resolver.enumValue(instance, base, 3),
          resolver.booleanValue(instance, base, 4),
          resolver.booleanValue(instance, base, 5),
          resolver.booleanValue(instance, base, 6));
    }
    // Handle case without B_SPLINE_SURFACE_WITH_KNOTS supertype
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    StepEntityDefinition base = resolver.definition(instance, "B_SPLINE_SURFACE");
    StepEntityResolver.requireParameterCount(instance, base, 7);
    return new StepBSplineSurfaceWithKnotsAndBreakpoints(
        instance.id(),
        "",
        resolver.integerValue(instance, base, 0),
        resolver.integerValue(instance, base, 1),
        resolver.referenceGrid(
            instance,
            base,
            2,
            StepCartesianPoint.class,
            "B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS control points must reference CARTESIAN_POINT"),
        resolver.integerList(instance, definition, 0),
        resolver.integerList(instance, definition, 1),
        resolver.numberList(instance, definition, 2),
        resolver.numberList(instance, definition, 3),
        resolver.numberList(instance, definition, 4),
        resolver.numberList(instance, definition, 5),
        resolver.enumValue(instance, base, 3),
        resolver.booleanValue(instance, base, 4),
        resolver.booleanValue(instance, base, 5),
        resolver.booleanValue(instance, base, 6));
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
}
