package com.minicad.step.semantic;

import com.minicad.common.Epsilon;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Clothoid3;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Hyperbola3;
import com.minicad.geometry.HyperboloidSurface;
import com.minicad.geometry.Line3;
import com.minicad.geometry.OffsetSurface3;
import com.minicad.geometry.Parabola3;
import com.minicad.geometry.ParaboloidSurface;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.RationalBSplineSurface3;
import com.minicad.geometry.RuledSurface3;
import com.minicad.geometry.SphericalSurface;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.SurfaceOfConstantRadius3;
import com.minicad.geometry.SurfaceOfLinearExtrusion3;
import com.minicad.geometry.SurfaceOfProjection3;
import com.minicad.geometry.SurfaceOfRevolution3;
import com.minicad.geometry.SurfaceOfTranslation3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.CompositeCurve2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Hyperbola2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Parabola2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.Polyline2;
import com.minicad.geometry2d.RationalBSplineCurve2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.geometry2d.Vector2;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.geometry.StepCartesianTransformationOperator;
import com.minicad.step.model.geometry.StepDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

final class StepCadGeometryOps {

    private final StepCadBuilder builder;

    StepCadGeometryOps(StepCadBuilder builder) {
        this.builder = builder;
    }

    Curve3 liftCurve2(Curve2 curve2) {
        List<Point2> points2 = sampleCurve2(curve2, 72);
        List<CartesianPoint> points3 = points2.stream()
                .map(point -> new CartesianPoint(point.x(), point.y(), 0.0))
                .collect(Collectors.toList());
        return new Polyline3(points3);
    }

    Curve2 approximateOffsetCurve2(Curve2 basisCurve, double distance) {
        List<Point2> sampled = sampleCurve2(basisCurve, 72);
        List<Point2> offsetPoints = new ArrayList<>(sampled.size());
        for (int index = 0; index < sampled.size(); index++) {
            Point2 point = sampled.get(index);
            Vector2 tangent = tangentAt(sampled, index);
            Vector2 normal = new Vector2(-tangent.y(), tangent.x());
            Direction2 direction = normal.isZero() ? new Direction2(0.0, 1.0) : normal.normalize();
            offsetPoints.add(point.add(direction.asVector().scale(distance)));
        }
        return new Polyline2(offsetPoints);
    }

    Curve3 approximateOffsetCurve3(Curve3 basisCurve, double distance, Direction3 refDirection) {
        List<CartesianPoint> sampled = sampleCurve3(basisCurve, 72);
        List<CartesianPoint> offsetPoints = new ArrayList<>(sampled.size());
        Vector3 ref = refDirection.asVector();
        for (int index = 0; index < sampled.size(); index++) {
            CartesianPoint point = sampled.get(index);
            Vector3 tangent = tangentAt3(sampled, index);
            Vector3 normal = tangent.cross(ref);
            if (normal.isZero()) {
                normal = ref;
            }
            offsetPoints.add(point.add(normal.normalize().asVector().scale(distance)));
        }
        return new Polyline3(offsetPoints);
    }

    List<Point2> sampleCurve2(Curve2 curve, int segments) {
        if (curve instanceof Line2) {
            Line2 line = (Line2) curve;
            return List.of(line.pointAt(0.0), line.pointAt(1.0));
        }
        if (curve instanceof Circle2) {
            Circle2 circle = (Circle2) curve;
            List<Point2> points = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                points.add(circle.pointAt(Math.PI * 2.0 * index / segments));
            }
            return List.copyOf(points);
        }
        if (curve instanceof Ellipse2) {
            Ellipse2 ellipse = (Ellipse2) curve;
            List<Point2> points = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                points.add(ellipse.pointAt(Math.PI * 2.0 * index / segments));
            }
            return List.copyOf(points);
        }
        if (curve instanceof BSplineCurve2) {
            BSplineCurve2 spline = (BSplineCurve2) curve;
            List<Point2> points = new ArrayList<>(segments + 1);
            double start = spline.startParameter();
            double end = spline.endParameter();
            for (int index = 0; index <= segments; index++) {
                points.add(spline.pointAt(start + (end - start) * index / segments));
            }
            return List.copyOf(points);
        }
        if (curve instanceof RationalBSplineCurve2) {
            RationalBSplineCurve2 spline = (RationalBSplineCurve2) curve;
            return spline.sample(segments);
        }
        if (curve instanceof TrimmedCurve2) {
            TrimmedCurve2 trimmedCurve = (TrimmedCurve2) curve;
            return sampleTrimmedCurve2(trimmedCurve, segments);
        }
        if (curve instanceof Polyline2) {
            Polyline2 polyline = (Polyline2) curve;
            return polyline.points();
        }
        if (curve instanceof CompositeCurve2) {
            CompositeCurve2 compositeCurve = (CompositeCurve2) curve;
            List<Point2> points = new ArrayList<>();
            boolean first = true;
            for (Curve2 segment : compositeCurve.segments()) {
                List<Point2> segmentPoints = sampleCurve2(segment, segments);
                int start = first ? 0 : 1;
                for (int i = start; i < segmentPoints.size(); i++) {
                    points.add(segmentPoints.get(i));
                }
                first = false;
            }
            return List.copyOf(points);
        }
        if (curve instanceof Parabola2) {
            Parabola2 parabola = (Parabola2) curve;
            List<Point2> points = new ArrayList<>(segments + 1);
            double tMin = -2.0;
            double tMax = 2.0;
            for (int index = 0; index <= segments; index++) {
                double t = tMin + (tMax - tMin) * index / segments;
                points.add(parabola.pointAt(t));
            }
            return List.copyOf(points);
        }
        if (curve instanceof Hyperbola2) {
            Hyperbola2 hyperbola = (Hyperbola2) curve;
            List<Point2> points = new ArrayList<>(segments + 1);
            double tMin = 1.0;
            double tMax = 2.0;
            for (int index = 0; index <= segments; index++) {
                double t = tMin + (tMax - tMin) * index / segments;
                points.add(hyperbola.pointAt(t));
            }
            return List.copyOf(points);
        }
        throw new UnsupportedGeometryException("curve sampling for " + curveTypeName(curve) + " is unsupported");
    }

    List<Point2> normalizeClosedLoop2(List<Point2> points) {
        if (points.size() < 3) {
            throw new UnsupportedGeometryException("profile loop must contain at least 3 points");
        }
        List<Point2> normalized = new ArrayList<>();
        for (Point2 point : points) {
            if (normalized.isEmpty() || point.subtract(normalized.get(normalized.size() - 1)).norm() > 1.0e-9) {
                normalized.add(point);
            }
        }
        if (normalized.size() < 3) {
            throw new UnsupportedGeometryException("profile loop must contain at least 3 distinct points");
        }
        if (normalized.get(0).subtract(normalized.get(normalized.size() - 1)).norm() > 1.0e-9) {
            normalized.add(normalized.get(0));
        }
        return List.copyOf(normalized);
    }

    List<Point2> reverseClosedLoop2(List<Point2> points) {
        List<Point2> reversed = new ArrayList<>(points.size());
        for (int index = points.size() - 2; index >= 0; index--) {
            reversed.add(points.get(index));
        }
        reversed.add(reversed.get(0));
        return List.copyOf(reversed);
    }

    List<CartesianPoint> closeLoop3(List<CartesianPoint> points) {
        List<CartesianPoint> closed = new ArrayList<>(points);
        if (closed.get(0).distanceTo(closed.get(closed.size() - 1)) > 1.0e-9) {
            closed.add(closed.get(0));
        }
        return List.copyOf(closed);
    }

    List<CartesianPoint> reverseClosedLoop3(List<CartesianPoint> points) {
        List<CartesianPoint> closed = closeLoop3(points);
        List<CartesianPoint> reversed = new ArrayList<>(closed.size());
        for (int index = closed.size() - 2; index >= 0; index--) {
            reversed.add(closed.get(index));
        }
        reversed.add(reversed.get(0));
        return List.copyOf(reversed);
    }

    List<CartesianPoint> sampleCurve3(Curve3 curve, int segments) {
        if (curve instanceof TrimmedCurve3) {
            TrimmedCurve3 trimmedCurve = (TrimmedCurve3) curve;
            return sampleTrimmedCurve3(trimmedCurve, segments);
        }
        if (curve instanceof SurfaceCurve3) {
            SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
            return sampleCurve3(surfaceCurve.curve3d(), segments);
        }
        if (curve instanceof Polyline3) {
            Polyline3 polyline = (Polyline3) curve;
            return polyline.points();
        }
        if (curve instanceof CompositeCurve3) {
            CompositeCurve3 compositeCurve = (CompositeCurve3) curve;
            List<CartesianPoint> points = new ArrayList<>();
            boolean first = true;
            for (Curve3 segment : compositeCurve.segments()) {
                List<CartesianPoint> segmentPoints = sampleCurve3(segment, segments);
                int start = first ? 0 : 1;
                for (int i = start; i < segmentPoints.size(); i++) {
                    points.add(segmentPoints.get(i));
                }
                first = false;
            }
            return List.copyOf(points);
        }
        List<CartesianPoint> points = curve.sample(segments);
        if (points.isEmpty()) {
            throw new UnsupportedGeometryException("curve sampling for " + curve.getClass().getSimpleName() + " is unsupported");
        }
        return points;
    }

    Curve3 transformCurve3(Curve3 curve, StepCartesianTransformationOperator transformation) {
        double scale = transformationScale(transformation);
        if (curve instanceof Line3) {
            Line3 line = (Line3) curve;
            return new Line3(
                    transformPoint3(line.origin(), transformation),
                    transformDirection3(line.direction(), transformation),
                    line.parameterScale() * Math.abs(scale));
        }
        if (curve instanceof Circle) {
            Circle circle = (Circle) curve;
            return new Circle(
                    transformPlacement(circle.position(), transformation),
                    circle.radius() * scale);
        }
        if (curve instanceof Ellipse3) {
            Ellipse3 ellipse = (Ellipse3) curve;
            return new Ellipse3(
                    transformPlacement(ellipse.position(), transformation),
                    ellipse.semiAxis1() * scale,
                    ellipse.semiAxis2() * scale);
        }
        if (curve instanceof Polyline3) {
            Polyline3 polyline = (Polyline3) curve;
            return new Polyline3(polyline.points().stream()
                    .map(point -> transformPoint3(point, transformation))
                    .collect(Collectors.toList()));
        }
        if (curve instanceof BSplineCurve3) {
            BSplineCurve3 spline = (BSplineCurve3) curve;
            return new BSplineCurve3(
                    spline.degree(),
                    spline.controlPoints().stream().map(point -> transformPoint3(point, transformation)).collect(Collectors.toList()),
                    spline.knotMultiplicities(),
                    spline.knots());
        }
        if (curve instanceof RationalBSplineCurve3) {
            RationalBSplineCurve3 spline = (RationalBSplineCurve3) curve;
            return new RationalBSplineCurve3(
                    spline.degree(),
                    spline.controlPoints().stream().map(point -> transformPoint3(point, transformation)).collect(Collectors.toList()),
                    spline.weights(),
                    spline.knotMultiplicities(),
                    spline.knots());
        }
        if (curve instanceof SurfaceCurve3) {
            SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
            return new SurfaceCurve3(
                    transformCurve3(surfaceCurve.curve3d(), transformation),
                    surfaceCurve.parametricCurves().stream()
                            .map(binding -> new SurfaceCurve3.ParametricCurve(
                                    transformSurfaceGeometry(binding.surface(), transformation),
                                    binding.curve2()))
                            .collect(Collectors.toList()));
        }
        if (curve instanceof TrimmedCurve3) {
            TrimmedCurve3 trimmedCurve = (TrimmedCurve3) curve;
            return new TrimmedCurve3(
                    transformCurve3(trimmedCurve.basisCurve(), transformation),
                    trimmedCurve.trimParamStart(),
                    trimmedCurve.trimParamEnd(),
                    trimmedCurve.senseAgreement());
        }
        if (curve instanceof CompositeCurve3) {
            CompositeCurve3 compositeCurve = (CompositeCurve3) curve;
            return new CompositeCurve3(
                    compositeCurve.segments().stream()
                            .map(segment -> transformCurve3(segment, transformation))
                            .collect(Collectors.toList()));
        }
        if (curve instanceof Parabola3) {
            Parabola3 parabola = (Parabola3) curve;
            return new Parabola3(
                    transformPlacement(parabola.position(), transformation),
                    parabola.focalDistance() * scale);
        }
        if (curve instanceof Hyperbola3) {
            Hyperbola3 hyperbola = (Hyperbola3) curve;
            return new Hyperbola3(
                    transformPlacement(hyperbola.position(), transformation),
                    hyperbola.semiAxisA() * scale,
                    hyperbola.semiAxisB() * scale);
        }
        if (curve instanceof Clothoid3) {
            Clothoid3 clothoid = (Clothoid3) curve;
            return new Clothoid3(
                    transformPlacement(clothoid.position(), transformation),
                    clothoid.xAxisIntercept() * scale,
                    clothoid.curvature() * scale);
        }
        if (curve instanceof DegenerateCurve3) {
            DegenerateCurve3 degenerate = (DegenerateCurve3) curve;
            return new DegenerateCurve3(
                    transformPoint3(degenerate.point(), transformation));
        }
        throw new UnsupportedGeometryException("curve replica for " + curveTypeName(curve) + " is unsupported");
        };
    }

    Curve2 transformCurve2(Curve2 curve, StepCartesianTransformationOperator transformation) {
        double scale = transformationScale(transformation);
        if (curve instanceof Line2) {
            Line2 line = (Line2) curve;
            return new Line2(
                    transformPoint2(line.origin(), transformation),
                    transformDirection2(line.direction(), transformation),
                    line.parameterScale() * Math.abs(scale));
        }
        if (curve instanceof Circle2) {
            Circle2 circle = (Circle2) curve;
            return new Circle2(
                    transformPoint2(circle.center(), transformation),
                    transformDirection2(circle.xDirection(), transformation),
                    circle.radius() * scale);
        }
        if (curve instanceof Ellipse2) {
            Ellipse2 ellipse = (Ellipse2) curve;
            return new Ellipse2(
                    transformPoint2(ellipse.center(), transformation),
                    transformDirection2(ellipse.xDirection(), transformation),
                    ellipse.semiAxis1() * scale,
                    ellipse.semiAxis2() * scale);
        }
        if (curve instanceof Polyline2) {
            Polyline2 polyline = (Polyline2) curve;
            return new Polyline2(polyline.points().stream()
                    .map(point -> transformPoint2(point, transformation))
                    .collect(Collectors.toList()));
        }
        if (curve instanceof BSplineCurve2) {
            BSplineCurve2 spline = (BSplineCurve2) curve;
            return new BSplineCurve2(
                    spline.degree(),
                    spline.controlPoints().stream().map(point -> transformPoint2(point, transformation)).collect(Collectors.toList()),
                    spline.knotMultiplicities(),
                    spline.knots());
        }
        if (curve instanceof RationalBSplineCurve2) {
            RationalBSplineCurve2 spline = (RationalBSplineCurve2) curve;
            return new RationalBSplineCurve2(
                    spline.degree(),
                    spline.controlPoints().stream().map(point -> transformPoint2(point, transformation)).collect(Collectors.toList()),
                    spline.weights(),
                    spline.knotMultiplicities(),
                    spline.knots());
        }
        if (curve instanceof TrimmedCurve2) {
            TrimmedCurve2 trimmedCurve = (TrimmedCurve2) curve;
            return new TrimmedCurve2(
                    transformCurve2(trimmedCurve.basisCurve(), transformation),
                    trimmedCurve.trimParamStart(),
                    trimmedCurve.trimParamEnd(),
                    trimmedCurve.senseAgreement());
        }
        if (curve instanceof CompositeCurve2) {
            CompositeCurve2 compositeCurve = (CompositeCurve2) curve;
            return new CompositeCurve2(
                    compositeCurve.segments().stream()
                            .map(segment -> transformCurve2(segment, transformation))
                            .collect(Collectors.toList()));
        }
        if (curve instanceof Parabola2) {
            Parabola2 parabola = (Parabola2) curve;
            return new Parabola2(
                    transformPoint2(parabola.vertex(), transformation),
                    transformDirection2(parabola.axisDirection(), transformation),
                    parabola.focalDistance() * scale);
        }
        if (curve instanceof Hyperbola2) {
            Hyperbola2 hyperbola = (Hyperbola2) curve;
            return new Hyperbola2(
                    transformPoint2(hyperbola.center(), transformation),
                    transformDirection2(hyperbola.xDirection(), transformation),
                    hyperbola.semiAxisA() * scale,
                    hyperbola.semiAxisB() * scale);
        }
        throw new UnsupportedGeometryException("curve replica for " + curveTypeName(curve) + " is unsupported");
    }

    SurfaceGeometry transformSurfaceGeometry(SurfaceGeometry surface, StepCartesianTransformationOperator transformation) {
        double scale = Math.abs(transformationScale(transformation));
        if (surface instanceof Plane) {
            Plane plane = (Plane) surface;
            return transformPlane(plane, transformation);
        }
        if (surface instanceof OffsetSurface3) {
            OffsetSurface3 offsetSurface = (OffsetSurface3) surface;
            return new OffsetSurface3(
                    transformSurfaceGeometry(offsetSurface.basisSurface(), transformation),
                    offsetSurface.distance() * scale);
        }
        if (surface instanceof CylindricalSurface) {
            CylindricalSurface cylindricalSurface = (CylindricalSurface) surface;
            return new CylindricalSurface(
                    transformPlacement(cylindricalSurface.position(), transformation),
                    cylindricalSurface.radius() * scale);
        }
        if (surface instanceof ConicalSurface) {
            ConicalSurface conicalSurface = (ConicalSurface) surface;
            return new ConicalSurface(
                    transformPlacement(conicalSurface.position(), transformation),
                    conicalSurface.radius() * scale,
                    conicalSurface.semiAngle());
        }
        if (surface instanceof ToroidalSurface) {
            ToroidalSurface toroidalSurface = (ToroidalSurface) surface;
            return new ToroidalSurface(
                    transformPlacement(toroidalSurface.position(), transformation),
                    toroidalSurface.majorRadius() * scale,
                    toroidalSurface.minorRadius() * scale);
        }
        if (surface instanceof SphericalSurface) {
            SphericalSurface sphericalSurface = (SphericalSurface) surface;
            return new SphericalSurface(
                    transformPlacement(sphericalSurface.position(), transformation),
                    sphericalSurface.radius() * scale);
        }
        if (surface instanceof BSplineSurface3) {
            BSplineSurface3 splineSurface = (BSplineSurface3) surface;
            return new BSplineSurface3(
                    splineSurface.uDegree(),
                    splineSurface.vDegree(),
                    splineSurface.controlPoints().stream()
                            .map(row -> row.stream().map(point -> transformPoint3(point, transformation)).collect(Collectors.toList()))
                            .collect(Collectors.toList()),
                    splineSurface.uMultiplicities(),
                    splineSurface.vMultiplicities(),
                    splineSurface.uKnots(),
                    splineSurface.vKnots());
        }
        if (surface instanceof RationalBSplineSurface3) {
            RationalBSplineSurface3 splineSurface = (RationalBSplineSurface3) surface;
            return new RationalBSplineSurface3(
                    splineSurface.uDegree(),
                    splineSurface.vDegree(),
                    splineSurface.controlPoints().stream()
                            .map(row -> row.stream().map(point -> transformPoint3(point, transformation)).collect(Collectors.toList()))
                            .collect(Collectors.toList()),
                    splineSurface.weightsData(),
                    splineSurface.uMultiplicities(),
                    splineSurface.vMultiplicities(),
                    splineSurface.uKnots(),
                    splineSurface.vKnots());
        }
        if (surface instanceof SurfaceOfLinearExtrusion3) {
            SurfaceOfLinearExtrusion3 extrusionSurface = (SurfaceOfLinearExtrusion3) surface;
            return new SurfaceOfLinearExtrusion3(
                    transformCurve3(extrusionSurface.sweptCurve(), transformation),
                    transformVector3(extrusionSurface.extrusionVector(), transformation));
        }
        if (surface instanceof SurfaceOfRevolution3) {
            SurfaceOfRevolution3 revolutionSurface = (SurfaceOfRevolution3) surface;
            return new SurfaceOfRevolution3(
                    transformCurve3(revolutionSurface.sweptCurve(), transformation),
                    transformPoint3(revolutionSurface.axisOrigin(), transformation),
                    transformDirection3(revolutionSurface.axisDirection(), transformation));
        }
        if (surface instanceof RuledSurface3) {
            RuledSurface3 ruledSurface = (RuledSurface3) surface;
            return new RuledSurface3(
                    transformCurve3(ruledSurface.directrix1(), transformation),
                    transformCurve3(ruledSurface.directrix2(), transformation));
        }
        if (surface instanceof SurfaceOfConstantRadius3) {
            SurfaceOfConstantRadius3 constantRadiusSurface = (SurfaceOfConstantRadius3) surface;
            return new SurfaceOfConstantRadius3(
                    transformSurfaceGeometry(constantRadiusSurface.sweptSurface(), transformation),
                    constantRadiusSurface.radius() * scale);
        }
        if (surface instanceof ParaboloidSurface) {
            ParaboloidSurface paraboloid = (ParaboloidSurface) surface;
            return new ParaboloidSurface(
                    transformPlacement(paraboloid.position(), transformation),
                    paraboloid.focalLength() * scale);
        }
        if (surface instanceof HyperboloidSurface) {
            HyperboloidSurface hyperboloid = (HyperboloidSurface) surface;
            return new HyperboloidSurface(
                    transformPlacement(hyperboloid.position(), transformation),
                    hyperboloid.radius() * scale,
                    hyperboloid.semiAxis() * scale);
        }
        if (surface instanceof SurfaceOfTranslation3) {
            SurfaceOfTranslation3 translation = (SurfaceOfTranslation3) surface;
            return new SurfaceOfTranslation3(
                    transformCurve3(translation.profile(), transformation),
                    transformVector3(translation.direction(), transformation));
        }
        if (surface instanceof SurfaceOfProjection3) {
            SurfaceOfProjection3 projection = (SurfaceOfProjection3) surface;
            return new SurfaceOfProjection3(
                    transformCurve3(projection.profile(), transformation),
                    transformVector3(projection.projectionDirection(), transformation));
        }
        throw new UnsupportedGeometryException("surface replica for " + surfaceTypeName(surface) + " is unsupported");
    }

    String unsupportedReplicaSurfaceTransformation(StepCartesianTransformationOperator transformation) {
        double scale = transformationScale(transformation);
        if (Math.abs(scale) <= 1.0e-9) {
            return "SURFACE_REPLICA zero scale";
        }
        Vector3 axis1 = transformAxis1_3(transformation);
        Vector3 axis2 = transformAxis2OrDefault3(transformation, axis1);
        Vector3 axis3 = transformAxis3OrDefault3(transformation, axis1, axis2);
        double tolerance = 1.0e-6;
        if (Math.abs(axis1.dot(axis2)) > tolerance
                || Math.abs(axis1.dot(axis3)) > tolerance
                || Math.abs(axis2.dot(axis3)) > tolerance) {
            return "SURFACE_REPLICA non-uniform scale";
        }
        return null;
    }

    static String curveTypeName(Curve3 curve) {
        if (curve instanceof Line3) {
            return "LINE";
        }
        if (curve instanceof Circle) {
            return "CIRCLE";
        }
        if (curve instanceof Ellipse3) {
            return "ELLIPSE";
        }
        if (curve instanceof Polyline3) {
            return "POLYLINE";
        }
        if (curve instanceof BSplineCurve3) {
            return "B_SPLINE_CURVE";
        }
        if (curve instanceof RationalBSplineCurve3) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (curve instanceof TrimmedCurve3) {
            return "TRIMMED_CURVE";
        }
        if (curve instanceof SurfaceCurve3) {
            return "SURFACE_CURVE";
        }
        if (curve instanceof CompositeCurve3) {
            return "COMPOSITE_CURVE";
        }
        return curve.getClass().getSimpleName();
    }

    static String curveTypeName(Curve2 curve) {
        if (curve instanceof Line2) {
            return "LINE";
        }
        if (curve instanceof Circle2) {
            return "CIRCLE";
        }
        if (curve instanceof Ellipse2) {
            return "ELLIPSE";
        }
        if (curve instanceof Polyline2) {
            return "POLYLINE";
        }
        if (curve instanceof BSplineCurve2) {
            return "B_SPLINE_CURVE";
        }
        if (curve instanceof RationalBSplineCurve2) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (curve instanceof TrimmedCurve2) {
            return "TRIMMED_CURVE";
        }
        if (curve instanceof CompositeCurve2) {
            return "COMPOSITE_CURVE";
        }
        return curve.getClass().getSimpleName();
    }

    private List<Point2> sampleTrimmedCurve2(TrimmedCurve2 trimmedCurve, int segments) {
        List<Point2> sampled = sampleCurve2(trimmedCurve.basisCurve(), segments);
        if (sampled.size() < 2) {
            return List.of(trimmedCurve.trimStart(), trimmedCurve.trimEnd());
        }
        boolean closed = sampled.get(0).subtract(sampled.get(sampled.size() - 1)).norm() <= 1.0e-9;
        List<Point2> basisPoints = closed ? List.copyOf(sampled.subList(0, sampled.size() - 1)) : sampled;
        int startIndex = nearestPointIndex2(basisPoints, trimmedCurve.trimStart());
        int endIndex = nearestPointIndex2(basisPoints, trimmedCurve.trimEnd());

        List<Point2> trimmed = new ArrayList<>();
        trimmed.add(trimmedCurve.trimStart());
        if (closed) {
            appendClosedTrimmedPoints2(trimmed, basisPoints, startIndex, endIndex, trimmedCurve.senseAgreement());
        } else {
            appendOpenTrimmedPoints2(trimmed, basisPoints, startIndex, endIndex);
        }
        addDistinctPoint2(trimmed, trimmedCurve.trimEnd());
        return List.copyOf(trimmed);
    }

    private int nearestPointIndex2(List<Point2> points, Point2 target) {
        int nearestIndex = 0;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            double distance = points.get(index).subtract(target).norm();
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }

    private void appendClosedTrimmedPoints2(
            List<Point2> target,
            List<Point2> basisPoints,
            int startIndex,
            int endIndex,
            boolean senseAgreement
    ) {
        int size = basisPoints.size();
        int index = startIndex;
        while (index != endIndex) {
            index = senseAgreement ? (index + 1) % size : (index - 1 + size) % size;
            addDistinctPoint2(target, basisPoints.get(index));
        }
    }

    private void appendOpenTrimmedPoints2(
            List<Point2> target,
            List<Point2> basisPoints,
            int startIndex,
            int endIndex
    ) {
        if (startIndex <= endIndex) {
            for (int index = startIndex + 1; index <= endIndex; index++) {
                addDistinctPoint2(target, basisPoints.get(index));
            }
            return;
        }
        for (int index = startIndex - 1; index >= endIndex; index--) {
            addDistinctPoint2(target, basisPoints.get(index));
        }
    }

    private void addDistinctPoint2(List<Point2> points, Point2 candidate) {
        if (points.isEmpty() || points.get(points.size() - 1).subtract(candidate).norm() > 1.0e-9) {
            points.add(candidate);
        }
    }

    private List<CartesianPoint> sampleTrimmedCurve3(TrimmedCurve3 trimmedCurve, int segments) {
        List<CartesianPoint> sampled = sampleCurve3(trimmedCurve.basisCurve(), segments);
        if (sampled.size() < 2) {
            return List.of(trimmedCurve.trimStart(), trimmedCurve.trimEnd());
        }
        boolean closed = sampled.get(0).distanceTo(sampled.get(sampled.size() - 1)) <= 1.0e-9;
        List<CartesianPoint> basisPoints = closed ? List.copyOf(sampled.subList(0, sampled.size() - 1)) : sampled;
        int startIndex = nearestPointIndex3(basisPoints, trimmedCurve.trimStart());
        int endIndex = nearestPointIndex3(basisPoints, trimmedCurve.trimEnd());

        List<CartesianPoint> trimmed = new ArrayList<>();
        trimmed.add(trimmedCurve.trimStart());
        if (closed) {
            appendClosedTrimmedPoints3(trimmed, basisPoints, startIndex, endIndex, trimmedCurve.senseAgreement());
        } else {
            appendOpenTrimmedPoints3(trimmed, basisPoints, startIndex, endIndex);
        }
        addDistinctPoint3(trimmed, trimmedCurve.trimEnd());
        return List.copyOf(trimmed);
    }

    private int nearestPointIndex3(List<CartesianPoint> points, CartesianPoint target) {
        int nearestIndex = 0;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            double distance = points.get(index).distanceTo(target);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }

    private void appendClosedTrimmedPoints3(
            List<CartesianPoint> target,
            List<CartesianPoint> basisPoints,
            int startIndex,
            int endIndex,
            boolean senseAgreement
    ) {
        int size = basisPoints.size();
        int index = startIndex;
        while (index != endIndex) {
            index = senseAgreement ? (index + 1) % size : (index - 1 + size) % size;
            addDistinctPoint3(target, basisPoints.get(index));
        }
    }

    private void appendOpenTrimmedPoints3(
            List<CartesianPoint> target,
            List<CartesianPoint> basisPoints,
            int startIndex,
            int endIndex
    ) {
        if (startIndex <= endIndex) {
            for (int index = startIndex + 1; index <= endIndex; index++) {
                addDistinctPoint3(target, basisPoints.get(index));
            }
            return;
        }
        for (int index = startIndex - 1; index >= endIndex; index--) {
            addDistinctPoint3(target, basisPoints.get(index));
        }
    }

    private void addDistinctPoint3(List<CartesianPoint> points, CartesianPoint candidate) {
        if (points.isEmpty() || points.get(points.size() - 1).distanceTo(candidate) > 1.0e-9) {
            points.add(candidate);
        }
    }

    private Plane transformPlane(Plane plane, StepCartesianTransformationOperator transformation) {
        return new Plane(
                transformPoint3(plane.origin(), transformation),
                transformDirection3(plane.normal(), transformation));
    }

    Axis2Placement3D transformPlacement(Axis2Placement3D placement, StepCartesianTransformationOperator transformation) {
        return new Axis2Placement3D(
                transformPoint3(placement.location(), transformation),
                transformDirection3(placement.axis(), transformation),
                transformDirection3(placement.refDirection(), transformation));
    }

    CartesianPoint transformPoint3(CartesianPoint point, StepCartesianTransformationOperator transformation) {
        TransformBasis3 basis = transformBasis3(transformation);
        double scale = transformationScale(transformation);
        Vector3 offset = basis.x().scale(point.x() * scale)
                .add(basis.y().scale(point.y() * scale))
                .add(basis.z().scale(point.z() * scale));
        return builder.buildPoint(transformation.localOrigin().id()).add(offset);
    }

    Vector3 transformVector3(Vector3 vector, StepCartesianTransformationOperator transformation) {
        TransformBasis3 basis = transformBasis3(transformation);
        double scale = transformationScale(transformation);
        return basis.x().scale(vector.x() * scale)
                .add(basis.y().scale(vector.y() * scale))
                .add(basis.z().scale(vector.z() * scale));
    }

    Point2 transformPoint2(Point2 point, StepCartesianTransformationOperator transformation) {
        Vector2 basisX = transformAxis1_2(transformation);
        Vector2 basisY = transformAxis2OrDefault2(transformation, basisX);
        double scale = transformationScale(transformation);
        Vector2 offset = basisX.scale(point.x() * scale).add(basisY.scale(point.y() * scale));
        Point2 origin = origin2(transformation);
        return origin.add(offset);
    }

    Direction3 transformDirection3(Direction3 direction, StepCartesianTransformationOperator transformation) {
        TransformBasis3 basis = transformBasis3(transformation);
        Vector3 source = direction.asVector();
        return Direction3.from(
                basis.x().scale(source.x())
                        .add(basis.y().scale(source.y()))
                        .add(basis.z().scale(source.z()))
        );
    }

    Direction2 transformDirection2(Direction2 direction, StepCartesianTransformationOperator transformation) {
        Vector2 basisX = transformAxis1_2(transformation);
        Vector2 basisY = transformAxis2OrDefault2(transformation, basisX);
        Vector2 source = direction.asVector();
        return Direction2.from(basisX.scale(source.x()).add(basisY.scale(source.y())));
    }

    private Vector3 transformAxis1_3(StepCartesianTransformationOperator transformation) {
        return transformation.axis1() == null
                ? new Vector3(1.0, 0.0, 0.0)
                : builder.buildDirection(transformation.axis1().id()).asVector();
    }

    private TransformBasis3 transformBasis3(StepCartesianTransformationOperator transformation) {
        Vector3 axis1 = transformAxis1_3(transformation);
        Vector3 axis2 = transformAxis2OrDefault3(transformation, axis1);
        Vector3 axis3 = transformAxis3OrDefault3(transformation, axis1, axis2);
        validateOrthogonalBasis3(transformation, axis1, axis2, axis3);
        return new TransformBasis3(axis1, axis2, axis3);
    }

    private static void validateOrthogonalBasis3(
            StepCartesianTransformationOperator transformation,
            Vector3 axis1,
            Vector3 axis2,
            Vector3 axis3
    ) {
        double tolerance = 1.0e-6;
        if (Math.abs(axis1.dot(axis2)) > tolerance
                || Math.abs(axis1.dot(axis3)) > tolerance
                || Math.abs(axis2.dot(axis3)) > tolerance) {
            throw new UnsupportedGeometryException("CARTESIAN_TRANSFORMATION_OPERATOR_3D #" + transformation.id()
                    + " axes must be orthogonal");
        }
    }

    private Vector3 transformAxis2OrDefault3(StepCartesianTransformationOperator transformation, Vector3 axis1) {
        if (transformation.axis2() != null) {
            return builder.buildDirection(transformation.axis2().id()).asVector();
        }
        Vector3 fallback = new Vector3(0.0, 1.0, 0.0);
        return axis1.cross(fallback).isZero() ? new Vector3(0.0, 0.0, 1.0) : fallback;
    }

    private Vector3 transformAxis3OrDefault3(StepCartesianTransformationOperator transformation, Vector3 axis1, Vector3 axis2) {
        if (transformation.axis3() != null) {
            return builder.buildDirection(transformation.axis3().id()).asVector();
        }
        Vector3 cross = axis1.cross(axis2);
        return cross.isZero() ? new Vector3(0.0, 0.0, 1.0) : cross.normalize().asVector();
    }

    private Vector2 transformAxis1_2(StepCartesianTransformationOperator transformation) {
        if (transformation.axis1() == null) {
            return new Vector2(1.0, 0.0);
        }
        StepDirection direction = transformation.axis1();
        if (direction.directionRatios().size() != 2) {
            throw new UnsupportedGeometryException("2D replica transformation axis1 must be 2D");
        }
        return builder.buildDirection2(direction.id()).asVector();
    }

    private Vector2 transformAxis2OrDefault2(StepCartesianTransformationOperator transformation, Vector2 axis1) {
        if (transformation.axis2() != null) {
            StepDirection direction = transformation.axis2();
            if (direction.directionRatios().size() != 2) {
                throw new UnsupportedGeometryException("2D replica transformation axis2 must be 2D");
            }
            return builder.buildDirection2(direction.id()).asVector();
        }
        return new Vector2(-axis1.y(), axis1.x());
    }

    private Point2 origin2(StepCartesianTransformationOperator transformation) {
        StepCartesianPoint origin = transformation.localOrigin();
        if (origin.coordinates().size() != 2) {
            throw new UnsupportedGeometryException("2D replica transformation origin must be 2D");
        }
        return builder.buildPoint2(origin.id());
    }

    private static double transformationScale(StepCartesianTransformationOperator transformation) {
        return transformation.scale() == null ? 1.0 : transformation.scale();
    }

    private static Vector2 tangentAt(List<Point2> points, int index) {
        Point2 previous = points.get(Math.max(index - 1, 0));
        Point2 next = points.get(Math.min(index + 1, points.size() - 1));
        Vector2 tangent = next.subtract(previous);
        return tangent.isZero() ? new Vector2(1.0, 0.0) : tangent;
    }

    private static Vector3 tangentAt3(List<CartesianPoint> points, int index) {
        CartesianPoint previous = points.get(Math.max(index - 1, 0));
        CartesianPoint next = points.get(Math.min(index + 1, points.size() - 1));
        Vector3 tangent = next.subtract(previous);
        return tangent.isZero() ? new Vector3(1.0, 0.0, 0.0) : tangent;
    }

    private record TransformBasis3(Vector3 x, Vector3 y, Vector3 z) {
    }
}
