package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry2d.*;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Curve 2D builder - sample extraction from StepCadBuilder.
 * Demonstrates the refactoring pattern for extracting specialized builders.
 *
 * This class shows how 2D curve build methods can be extracted into dedicated
 * builder classes, reducing the size of the main StepCadBuilder (7429 lines).
 * Full extraction would include all 2D curve build methods.
 *
 * Note: This is a sample extraction showing the pattern. The actual build
 * methods in StepCadBuilder use caching maps. For full extraction, those
 * caches would need to be moved or shared.
 */
final class StepCurve2DBuilder {

  private final StepCadBuilder builder;

  StepCurve2DBuilder(StepCadBuilder builder) {
    this.builder = builder;
  }

  // === Basic 2D Geometry ===

  /**
   * Builds a 2D point from a STEP CARTESIAN_POINT.
   * Demonstrates calling builder's requireEntity helper.
   */
  Point2 buildPoint2(int id) {
    StepCartesianPoint point = builder.requireEntity(id, StepCartesianPoint.class, "CARTESIAN_POINT");
    if (point.coordinates().size() != 2) {
      throw new StepResolutionException("entity #" + id + " is not a 2D CARTESIAN_POINT");
    }
    return new Point2(point.coordinates().get(0), point.coordinates().get(1));
  }

  /**
   * Builds a 2D direction from a STEP DIRECTION.
   */
  Direction2 buildDirection2(int id) {
    StepDirection direction = builder.requireEntity(id, StepDirection.class, "DIRECTION");
    if (direction.directionRatios().size() != 2) {
      throw new StepResolutionException("entity #" + id + " is not a 2D DIRECTION");
    }
    return Direction2.from(new Vector2(
        direction.directionRatios().get(0),
        direction.directionRatios().get(1)
    ));
  }

  // === Basic 2D Curves ===

  /**
   * Builds a 2D line from a STEP LINE.
   * Demonstrates calling builder's other build methods for dependencies.
   */
  Line2 buildLine2(int id) {
    StepLine line = builder.requireEntity(id, StepLine.class, "LINE");
    if (line.point().coordinates().size() != 2
        || line.vector().isOrientation().directionRatios().size() != 2) {
      throw new StepResolutionException("entity #" + id + " is not a 2D LINE");
    }
    return new Line2(
        buildPoint2(line.point().id()),
        buildDirection2(line.vector().isOrientation().id()),
        line.vector().magnitude()
    );
  }

  /**
   * Builds a 2D circle from a STEP CIRCLE.
   */
  Circle2 buildCircle2(int id) {
    StepCircle circle = builder.requireEntity(id, StepCircle.class, "CIRCLE");
    // Get placement - can be 2D or 3D
    StepEntity position = circle.position();

    if (position instanceof StepAxis2Placement2D) {
      StepAxis2Placement2D placement2d = (StepAxis2Placement2D) position;
      Point2 center = buildPoint2(placement2d.getLocation().id());
      Direction2 xDir = placement2d.getRefDirection() != null
          ? buildDirection2(placement2d.getRefDirection().id())
          : Direction2.xAxis();
      double radius = circle.radius();
      if (radius <= 0) {
        throw new UnsupportedGeometryException("CIRCLE radius must be positive");
      }
      return new Circle2(center, xDir, radius);
    } else if (position instanceof StepAxis2Placement3D) {
      // For 3D placement in XY plane, extract 2D components
      StepAxis2Placement3D placement3d = (StepAxis2Placement3D) position;
      StepCartesianPoint loc = placement3d.getLocation();
      Point2 center = new Point2(loc.coordinates().get(0), loc.coordinates().get(1));
      Direction2 xDir = Direction2.xAxis(); // Default for 3D placement in XY plane
      double radius = circle.radius();
      if (radius <= 0) {
        throw new UnsupportedGeometryException("CIRCLE radius must be positive");
      }
      return new Circle2(center, xDir, radius);
    } else {
      throw new StepResolutionException(
          "entity #" + id + " CIRCLE position must be AXIS2_PLACEMENT_2D or 3D");
    }
  }

  /**
   * Builds a 2D ellipse from a STEP ELLIPSE.
   */
  Ellipse2 buildEllipse2(int id) {
    StepEllipse ellipse = builder.requireEntity(id, StepEllipse.class, "ELLIPSE");
    StepEntity position = ellipse.position();

    if (position instanceof StepAxis2Placement2D) {
      StepAxis2Placement2D placement2d = (StepAxis2Placement2D) position;
      Point2 center = buildPoint2(placement2d.getLocation().id());
      Direction2 xDir = placement2d.getRefDirection() != null
          ? buildDirection2(placement2d.getRefDirection().id())
          : Direction2.xAxis();
      double semiAxis1 = ellipse.semiAxis1();
      double semiAxis2 = ellipse.semiAxis2();
      if (semiAxis1 <= 0 || semiAxis2 <= 0) {
        throw new UnsupportedGeometryException("ELLIPSE semi-axes must be positive");
      }
      return new Ellipse2(center, xDir, semiAxis1, semiAxis2);
    } else if (position instanceof StepAxis2Placement3D) {
      StepAxis2Placement3D placement3d = (StepAxis2Placement3D) position;
      StepCartesianPoint loc = placement3d.getLocation();
      Point2 center = new Point2(loc.coordinates().get(0), loc.coordinates().get(1));
      Direction2 xDir = Direction2.xAxis();
      double semiAxis1 = ellipse.semiAxis1();
      double semiAxis2 = ellipse.semiAxis2();
      if (semiAxis1 <= 0 || semiAxis2 <= 0) {
        throw new UnsupportedGeometryException("ELLIPSE semi-axes must be positive");
      }
      return new Ellipse2(center, xDir, semiAxis1, semiAxis2);
    } else {
      throw new StepResolutionException(
          "entity #" + id + " ELLIPSE position must be AXIS2_PLACEMENT_2D or 3D");
    }
  }

  /**
   * Builds a 2D polyline from a STEP POLYLINE.
   */
  Polyline2 buildPolyline2(int id) {
    StepPolyline polyline = builder.requireEntity(id, StepPolyline.class, "POLYLINE");
    List<StepCartesianPoint> stepPoints = polyline.points();
    List<Point2> points = new ArrayList<>(stepPoints.size());
    for (StepCartesianPoint stepPoint : stepPoints) {
      if (stepPoint.coordinates().size() != 2) {
        throw new StepResolutionException(
            "entity #" + id + " POLYLINE point must have 2 coordinates");
      }
      points.add(new Point2(
          stepPoint.coordinates().get(0),
          stepPoint.coordinates().get(1)
      ));
    }
    return new Polyline2(points);
  }

  /**
   * Builds a 2D B-spline curve from a STEP B_SPLINE_CURVE_WITH_KNOTS.
   * Demonstrates the correct constructor signature matching original implementation.
   */
  BSplineCurve2 buildBSplineCurve2(int id) {
    StepBSplineCurveWithKnots spline = builder.requireEntity(id, StepBSplineCurveWithKnots.class,
        "B_SPLINE_CURVE_WITH_KNOTS");

    List<StepCartesianPoint> controlPoints = spline.getControlPoints();
    List<Point2> points2d = new ArrayList<>(controlPoints.size());
    for (StepCartesianPoint point : controlPoints) {
      if (point.coordinates().size() != 2) {
        throw new UnsupportedGeometryException("B_SPLINE_CURVE_WITH_KNOTS is not a 2D spline");
      }
      points2d.add(buildPoint2(point.id()));
    }

    return new BSplineCurve2(
        spline.getDegree(),
        points2d,
        spline.getKnotMultiplicities(),
        spline.getKnots()
    );
  }

  /**
   * Builds a 2D composite curve from a STEP COMPOSITE_CURVE.
   * Demonstrates calling buildCurve2 for recursive building.
   */
  CompositeCurve2 buildCompositeCurve2(int id) {
    StepCompositeCurve composite = builder.requireEntity(id, StepCompositeCurve.class, "COMPOSITE_CURVE");
    List<StepCompositeCurveSegment> segments = composite.segments();

    List<Curve2> curves = new ArrayList<>(segments.size());
    for (StepCompositeCurveSegment segment : segments) {
      Object built = builder.buildCurve2(segment.parentCurve());
      if (built instanceof Curve2) {
        curves.add((Curve2) built);
      } else {
        throw new UnsupportedGeometryException("COMPOSITE_CURVE segment must be a 2D curve");
      }
    }

    return new CompositeCurve2(curves);
  }

  /**
   * Demonstrates dispatch pattern for building various 2D curve types.
   * Note: In full extraction, this would match StepCadBuilder.buildCurve2().
   */
  Object buildCurve2Dispatch(StepEntity item) {
    if (item instanceof StepLine) {
      return buildLine2(item.id());
    }
    if (item instanceof StepCircle) {
      return buildCircle2(item.id());
    }
    if (item instanceof StepEllipse) {
      return buildEllipse2(item.id());
    }
    if (item instanceof StepPolyline) {
      return buildPolyline2(item.id());
    }
    if (item instanceof StepBSplineCurveWithKnots) {
      return buildBSplineCurve2(item.id());
    }
    if (item instanceof StepCompositeCurve) {
      return buildCompositeCurve2(item.id());
    }
    // In full extraction, many more curve types would be handled
    throw new UnsupportedGeometryException(
        "2D curve type " + StepCadBuilder.stepEntityTypeName(item) + " is not supported");
  }
}
