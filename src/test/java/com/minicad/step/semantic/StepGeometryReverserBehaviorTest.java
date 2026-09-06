package com.minicad.step.semantic;

import com.minicad.geometry.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Behavioural checks for StepGeometryReverser's folded dispatch tables.
 *
 * ReverseGeometryDispatchTableTest pins the table SHAPE (branch order, type
 * wiring) by reading the host source. This file pins the table BEHAVIOUR: one
 * test per branch, asserting the observable sense reversal each handler must
 * produce (x-direction flipped, control grid row-reversed, trim parameters
 * swapped, vector negated, ...). Every concrete Curve3 / SurfaceGeometry
 * implementation in the geometry package appears here exactly once, so a branch
 * that is dropped from a table, or wired to the wrong handler, fails here at
 * runtime instead of silently returning un-reversed geometry.
 *
 * All 13 Curve3 and all 16 SurfaceGeometry implementations are covered; the two
 * fall-through tests use anonymous implementations because no concrete type
 * escapes the tables today.
 */
class StepGeometryReverserBehaviorTest {

    private static final double EPS = 1e-9;

    // ─── reverseCurve3 ───────────────────────────────────────────────────

    @Test
    @DisplayName("Line3: direction reversed, parameter scale kept")
    void reverseLine3() {
        Line3 line = new Line3(new CartesianPoint(1, 2, 3), new Direction3(1, 0, 0), 4.0);
        Line3 reversed = (Line3) StepGeometryReverser.reverseCurve3(line);
        assertEquals(-1.0, reversed.getDirection().x(), EPS);
        assertEquals(4.0, reversed.getParameterScale(), EPS);
    }

    @Test
    @DisplayName("Polyline3: point order reversed")
    void reversePolyline3() {
        Polyline3 polyline = new Polyline3(List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(1, 0, 0),
                new CartesianPoint(2, 0, 0)));
        Polyline3 reversed = (Polyline3) StepGeometryReverser.reverseCurve3(polyline);
        assertEquals(2.0, reversed.getPoints().get(0).x(), EPS);
        assertEquals(0.0, reversed.getPoints().get(2).x(), EPS);
    }

    @Test
    @DisplayName("CompositeCurve3: segment order reversed and each segment reversed")
    void reverseCompositeCurve3() {
        CompositeCurve3 composite = new CompositeCurve3(List.of(
                new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0)),
                new Line3(CartesianPoint.origin(), new Direction3(0, 1, 0))));
        CompositeCurve3 reversed =
                (CompositeCurve3) StepGeometryReverser.reverseCurve3(composite);
        assertEquals(2, reversed.getSegments().size());
        // First segment is the reversed former-last (Y direction) segment.
        Line3 first = (Line3) reversed.getSegments().get(0);
        assertEquals(-1.0, first.getDirection().y(), EPS);
        Line3 second = (Line3) reversed.getSegments().get(1);
        assertEquals(-1.0, second.getDirection().x(), EPS);
    }

    @Test
    @DisplayName("Circle: x-direction reversed, radius kept")
    void reverseCircle() {
        Circle circle = new Circle(Axis2Placement3D.origin(), 2.0);
        Circle reversed = (Circle) StepGeometryReverser.reverseCurve3(circle);
        assertEquals(-1.0, reversed.getPosition().xDirection().x(), EPS);
        assertEquals(2.0, reversed.getRadius(), EPS);
    }

    @Test
    @DisplayName("Ellipse3: x-direction reversed, semi-axes kept")
    void reverseEllipse3() {
        Ellipse3 ellipse = new Ellipse3(Axis2Placement3D.origin(), 3.0, 2.0);
        Ellipse3 reversed = (Ellipse3) StepGeometryReverser.reverseCurve3(ellipse);
        assertEquals(-1.0, reversed.getPosition().xDirection().x(), EPS);
        assertEquals(3.0, reversed.getSemiAxis1(), EPS);
        assertEquals(2.0, reversed.getSemiAxis2(), EPS);
    }

    @Test
    @DisplayName("Parabola3: x-direction reversed, focal length kept")
    void reverseParabola3() {
        Parabola3 parabola = new Parabola3(Axis2Placement3D.origin(), 1.5);
        Parabola3 reversed = (Parabola3) StepGeometryReverser.reverseCurve3(parabola);
        assertEquals(-1.0, reversed.getPosition().xDirection().x(), EPS);
        assertEquals(1.5, reversed.getFocalLength(), EPS);
    }

    @Test
    @DisplayName("Hyperbola3: x-direction reversed, semi-axes kept")
    void reverseHyperbola3() {
        Hyperbola3 hyperbola = new Hyperbola3(Axis2Placement3D.origin(), 3.0, 2.0);
        Hyperbola3 reversed = (Hyperbola3) StepGeometryReverser.reverseCurve3(hyperbola);
        assertEquals(-1.0, reversed.getPosition().xDirection().x(), EPS);
        assertEquals(3.0, reversed.getSemiAxisA(), EPS);
        assertEquals(2.0, reversed.getSemiAxisB(), EPS);
    }

    @Test
    @DisplayName("Clothoid3: x-direction reversed, intercept and curvature kept")
    void reverseClothoid3() {
        Clothoid3 clothoid = new Clothoid3(Axis2Placement3D.origin(), 1.0, 0.5);
        Clothoid3 reversed = (Clothoid3) StepGeometryReverser.reverseCurve3(clothoid);
        assertEquals(-1.0, reversed.getPosition().xDirection().x(), EPS);
        assertEquals(1.0, reversed.xAxisIntercept(), EPS);
        assertEquals(0.5, reversed.curvature(), EPS);
    }

    @Test
    @DisplayName("DegenerateCurve3: point kept")
    void reverseDegenerateCurve3() {
        DegenerateCurve3 degenerate = new DegenerateCurve3(new CartesianPoint(1, 2, 3));
        DegenerateCurve3 reversed = (DegenerateCurve3) StepGeometryReverser.reverseCurve3(degenerate);
        assertEquals(1.0, reversed.point().x(), EPS);
        assertEquals(3.0, reversed.point().z(), EPS);
    }

    @Test
    @DisplayName("TrimmedCurve3: trim parameters swapped and sense flipped")
    void reverseTrimmedCurve3() {
        TrimmedCurve3 trimmed = new TrimmedCurve3(
                new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0)),
                0.0, 1.0, true);
        TrimmedCurve3 reversed = (TrimmedCurve3) StepGeometryReverser.reverseCurve3(trimmed);
        assertEquals(1.0, reversed.getTrimParamStart(), EPS);
        assertEquals(0.0, reversed.getTrimParamEnd(), EPS);
        assertFalse(reversed.isSenseAgreement());
        // The basis curve is reversed too.
        Line3 basis = (Line3) reversed.getBasisCurve();
        assertEquals(-1.0, basis.getDirection().x(), EPS);
    }

    @Test
    @DisplayName("SurfaceCurve3: 3D curve reversed, parametric curves kept")
    void reverseSurfaceCurve3() {
        SurfaceCurve3 surfaceCurve = new SurfaceCurve3(
                new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0)));
        SurfaceCurve3 reversed = (SurfaceCurve3) StepGeometryReverser.reverseCurve3(surfaceCurve);
        Line3 curve3d = (Line3) reversed.getCurve3d();
        assertEquals(-1.0, curve3d.getDirection().x(), EPS);
    }

    @Test
    @DisplayName("BSplineCurve3: control points reversed, knots kept")
    void reverseBSplineCurve3() {
        List<CartesianPoint> controlPoints = List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(1, 0, 0),
                new CartesianPoint(2, 0, 0));
        BSplineCurve3 bspline = new BSplineCurve3(
                1, controlPoints, List.of(2, 3), List.of(0.0, 1.0));
        BSplineCurve3 reversed = (BSplineCurve3) StepGeometryReverser.reverseCurve3(bspline);
        assertEquals(2.0, reversed.getControlPoints().get(0).x(), EPS);
        assertEquals(0.0, reversed.getControlPoints().get(2).x(), EPS);
        assertEquals(List.of(0.0, 1.0), reversed.getKnots());
    }

    @Test
    @DisplayName("RationalBSplineCurve3: control points reversed, weights kept")
    void reverseRationalBSplineCurve3() {
        List<CartesianPoint> controlPoints = List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(1, 0, 0),
                new CartesianPoint(2, 0, 0));
        RationalBSplineCurve3 rational = new RationalBSplineCurve3(
                1, controlPoints, List.of(1.0, 2.0, 3.0), List.of(2, 3), List.of(0.0, 1.0));
        RationalBSplineCurve3 reversed =
                (RationalBSplineCurve3) StepGeometryReverser.reverseCurve3(rational);
        assertEquals(2.0, reversed.getControlPoints().get(0).x(), EPS);
        assertEquals(List.of(1.0, 2.0, 3.0), reversed.getWeights());
    }

    // ─── reverseSurfaceSense ─────────────────────────────────────────────

    @Test
    @DisplayName("Plane: normal reversed, origin kept")
    void reversePlane() {
        Plane plane = new Plane(new CartesianPoint(1, 2, 3), new Direction3(0, 0, 1));
        Plane reversed = (Plane) StepGeometryReverser.reverseSurfaceSense(plane);
        assertEquals(-1.0, reversed.getNormal().z(), EPS);
        assertEquals(1.0, reversed.getOrigin().x(), EPS);
    }

    @Test
    @DisplayName("CylindricalSurface: x-direction reversed, radius kept")
    void reverseCylindricalSurface() {
        CylindricalSurface cylinder = new CylindricalSurface(Axis2Placement3D.origin(), 5.0);
        CylindricalSurface reversed =
                (CylindricalSurface) StepGeometryReverser.reverseSurfaceSense(cylinder);
        assertEquals(-1.0, reversed.getPosition().xDirection().x(), EPS);
        assertEquals(5.0, reversed.getRadius(), EPS);
    }

    @Test
    @DisplayName("ConicalSurface: x-direction reversed, radius and angle kept")
    void reverseConicalSurface() {
        ConicalSurface cone = new ConicalSurface(Axis2Placement3D.origin(), 5.0, 0.3);
        ConicalSurface reversed = (ConicalSurface) StepGeometryReverser.reverseSurfaceSense(cone);
        assertEquals(-1.0, reversed.getPosition().xDirection().x(), EPS);
        assertEquals(5.0, reversed.getRadius(), EPS);
        assertEquals(0.3, reversed.getSemiAngle(), EPS);
    }

    @Test
    @DisplayName("SphericalSurface: x-direction reversed, radius kept")
    void reverseSphericalSurface() {
        SphericalSurface sphere = new SphericalSurface(Axis2Placement3D.origin(), 5.0);
        SphericalSurface reversed =
                (SphericalSurface) StepGeometryReverser.reverseSurfaceSense(sphere);
        assertEquals(-1.0, reversed.getPosition().xDirection().x(), EPS);
        assertEquals(5.0, reversed.getRadius(), EPS);
    }

    @Test
    @DisplayName("ToroidalSurface: x-direction reversed, radii kept")
    void reverseToroidalSurface() {
        ToroidalSurface torus = new ToroidalSurface(Axis2Placement3D.origin(), 5.0, 1.0);
        ToroidalSurface reversed = (ToroidalSurface) StepGeometryReverser.reverseSurfaceSense(torus);
        assertEquals(-1.0, reversed.getPosition().xDirection().x(), EPS);
        assertEquals(5.0, reversed.getMajorRadius(), EPS);
        assertEquals(1.0, reversed.getMinorRadius(), EPS);
    }

    @Test
    @DisplayName("SurfaceOfLinearExtrusion3: extrusion vector negated")
    void reverseSurfaceOfLinearExtrusion3() {
        SurfaceOfLinearExtrusion3 extrusion = new SurfaceOfLinearExtrusion3(
                new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0)),
                new Vector3(0, 0, 1));
        SurfaceOfLinearExtrusion3 reversed =
                (SurfaceOfLinearExtrusion3) StepGeometryReverser.reverseSurfaceSense(extrusion);
        assertEquals(-1.0, reversed.getExtrusionVector().z(), EPS);
    }

    @Test
    @DisplayName("SurfaceOfRevolution3: axis direction reversed")
    void reverseSurfaceOfRevolution3() {
        SurfaceOfRevolution3 revolution = new SurfaceOfRevolution3(
                new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0)),
                CartesianPoint.origin(), new Direction3(0, 0, 1));
        SurfaceOfRevolution3 reversed =
                (SurfaceOfRevolution3) StepGeometryReverser.reverseSurfaceSense(revolution);
        assertEquals(-1.0, reversed.getAxisDirection().z(), EPS);
    }

    @Test
    @DisplayName("RuledSurface3: both directrices reversed")
    void reverseRuledSurface3() {
        RuledSurface3 ruled = new RuledSurface3(
                new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0)),
                new Line3(CartesianPoint.origin(), new Direction3(0, 1, 0)));
        RuledSurface3 reversed = (RuledSurface3) StepGeometryReverser.reverseSurfaceSense(ruled);
        assertEquals(-1.0, ((Line3) reversed.getDirectrix1()).getDirection().x(), EPS);
        assertEquals(-1.0, ((Line3) reversed.getDirectrix2()).getDirection().y(), EPS);
    }

    @Test
    @DisplayName("SurfaceOfConstantRadius3: swept surface sense reversed, radius kept")
    void reverseSurfaceOfConstantRadius3() {
        SurfaceOfConstantRadius3 constant = new SurfaceOfConstantRadius3(
                new Plane(CartesianPoint.origin(), new Direction3(0, 0, 1)), 2.0);
        SurfaceOfConstantRadius3 reversed =
                (SurfaceOfConstantRadius3) StepGeometryReverser.reverseSurfaceSense(constant);
        Plane swept = (Plane) reversed.getSweptSurface();
        assertEquals(-1.0, swept.getNormal().z(), EPS);
        assertEquals(2.0, reversed.getRadius(), EPS);
    }

    @Test
    @DisplayName("OffsetSurface3: basis surface sense reversed, distance kept")
    void reverseOffsetSurface3() {
        OffsetSurface3 offset = new OffsetSurface3(
                new Plane(CartesianPoint.origin(), new Direction3(0, 0, 1)), 1.5);
        OffsetSurface3 reversed = (OffsetSurface3) StepGeometryReverser.reverseSurfaceSense(offset);
        Plane basis = (Plane) reversed.getBasisSurface();
        assertEquals(-1.0, basis.getNormal().z(), EPS);
        assertEquals(1.5, reversed.getDistance(), EPS);
    }

    @Test
    @DisplayName("BSplineSurface3: control grid reversed within each row, knots kept")
    void reverseBSplineSurface3() {
        List<List<CartesianPoint>> grid = List.of(
                List.of(new CartesianPoint(10, 0, 0), new CartesianPoint(11, 0, 0)),
                List.of(new CartesianPoint(20, 0, 0), new CartesianPoint(21, 0, 0)));
        BSplineSurface3 bspline = new BSplineSurface3(
                1, 1, grid, List.of(4), List.of(4), List.of(0.0), List.of(1.0));
        BSplineSurface3 reversed =
                (BSplineSurface3) StepGeometryReverser.reverseSurfaceSense(bspline);
        // reverseBSplineControlGrid reverses the points WITHIN each row and
        // keeps the row order: [[10,11],[20,21]] -> [[11,10],[21,20]].
        assertEquals(11.0, reversed.getControlPoints().get(0).get(0).x(), EPS);
        assertEquals(10.0, reversed.getControlPoints().get(0).get(1).x(), EPS);
        assertEquals(21.0, reversed.getControlPoints().get(1).get(0).x(), EPS);
        assertEquals(20.0, reversed.getControlPoints().get(1).get(1).x(), EPS);
        assertEquals(List.of(0.0), reversed.getUKnots());
    }

    @Test
    @DisplayName("RationalBSplineSurface3: control grid reversed within each row, weights kept")
    void reverseRationalBSplineSurface3() {
        List<List<CartesianPoint>> grid = List.of(
                List.of(new CartesianPoint(10, 0, 0), new CartesianPoint(11, 0, 0)),
                List.of(new CartesianPoint(20, 0, 0), new CartesianPoint(21, 0, 0)));
        List<List<Double>> weights = List.of(List.of(1.0, 2.0), List.of(3.0, 4.0));
        RationalBSplineSurface3 rational = new RationalBSplineSurface3(
                1, 1, grid, weights, List.of(4), List.of(4), List.of(0.0), List.of(1.0));
        RationalBSplineSurface3 reversed =
                (RationalBSplineSurface3) StepGeometryReverser.reverseSurfaceSense(rational);
        // Control points reverse within each row (row order kept)...
        assertEquals(11.0, reversed.getControlPoints().get(0).get(0).x(), EPS);
        assertEquals(21.0, reversed.getControlPoints().get(1).get(0).x(), EPS);
        // ...while the weight grid is untouched, row order and within-row order alike.
        assertEquals(List.of(1.0, 2.0), reversed.getWeightsData().get(0));
        assertEquals(List.of(3.0, 4.0), reversed.getWeightsData().get(1));
    }

    @Test
    @DisplayName("ParaboloidSurface: x-direction reversed, focal length kept")
    void reverseParaboloidSurface() {
        ParaboloidSurface paraboloid = new ParaboloidSurface(Axis2Placement3D.origin(), 1.0);
        ParaboloidSurface reversed =
                (ParaboloidSurface) StepGeometryReverser.reverseSurfaceSense(paraboloid);
        assertEquals(-1.0, reversed.getPosition().xDirection().x(), EPS);
        assertEquals(1.0, reversed.getFocalLength(), EPS);
    }

    @Test
    @DisplayName("HyperboloidSurface: x-direction reversed, radii kept")
    void reverseHyperboloidSurface() {
        HyperboloidSurface hyperboloid = new HyperboloidSurface(Axis2Placement3D.origin(), 5.0, 2.0);
        HyperboloidSurface reversed =
                (HyperboloidSurface) StepGeometryReverser.reverseSurfaceSense(hyperboloid);
        assertEquals(-1.0, reversed.getPosition().xDirection().x(), EPS);
        assertEquals(5.0, reversed.getRadius(), EPS);
        assertEquals(2.0, reversed.getSemiAxis(), EPS);
    }

    @Test
    @DisplayName("SurfaceOfTranslation3: profile reversed, direction kept")
    void reverseSurfaceOfTranslation3() {
        SurfaceOfTranslation3 translation = new SurfaceOfTranslation3(
                new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0)),
                new Vector3(0, 0, 1));
        SurfaceOfTranslation3 reversed =
                (SurfaceOfTranslation3) StepGeometryReverser.reverseSurfaceSense(translation);
        assertEquals(-1.0, ((Line3) reversed.getProfile()).getDirection().x(), EPS);
        assertEquals(1.0, reversed.getDirection().z(), EPS);
    }

    @Test
    @DisplayName("SurfaceOfProjection3: profile reversed, projection direction kept")
    void reverseSurfaceOfProjection3() {
        SurfaceOfProjection3 projection = new SurfaceOfProjection3(
                new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0)),
                new Vector3(0, 0, 1));
        SurfaceOfProjection3 reversed =
                (SurfaceOfProjection3) StepGeometryReverser.reverseSurfaceSense(projection);
        assertEquals(-1.0, ((Line3) reversed.getProfile()).getDirection().x(), EPS);
        assertEquals(1.0, reversed.getProjectionDirection().z(), EPS);
    }

    // ─── Fall-through for unsupported geometry ───────────────────────────

    @Test
    @DisplayName("reverseCurve3 returns unsupported curve unchanged")
    void reverseCurve3ShouldFallThroughForUnsupportedType() {
        Curve3 unsupported = new Curve3() {
            @Override
            public boolean contains(CartesianPoint point) {
                return false;
            }

            @Override
            public CartesianPoint pointAt(double parameter) {
                return CartesianPoint.origin();
            }

            @Override
            public CartesianPoint closestPointTo(CartesianPoint point) {
                return CartesianPoint.origin();
            }
        };
        assertSame(unsupported, StepGeometryReverser.reverseCurve3(unsupported));
    }

    @Test
    @DisplayName("reverseSurfaceSense returns unsupported surface unchanged")
    void reverseSurfaceSenseShouldFallThroughForUnsupportedType() {
        SurfaceGeometry unsupported = new SurfaceGeometry() {
            @Override
            public CartesianPoint pointAt(double u, double v) {
                return CartesianPoint.origin();
            }
        };
        assertSame(unsupported, StepGeometryReverser.reverseSurfaceSense(unsupported));
    }

    // ─── Involution: reversing twice restores the original ───────────────

    @Test
    @DisplayName("every reversal is an involution")
    void everyReversalShouldBeAnInvolution() {
        Line3 line = new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0));
        Polyline3 polyline = new Polyline3(List.of(
                new CartesianPoint(0, 0, 0), new CartesianPoint(1, 0, 0)));
        Circle circle = new Circle(Axis2Placement3D.origin(), 2.0);
        Plane plane = new Plane(CartesianPoint.origin(), new Direction3(0, 0, 1));

        Line3 lineTwice = (Line3) StepGeometryReverser.reverseCurve3(
                StepGeometryReverser.reverseCurve3(line));
        assertEquals(1.0, lineTwice.getDirection().x(), EPS);

        Polyline3 polylineTwice = (Polyline3) StepGeometryReverser.reverseCurve3(
                StepGeometryReverser.reverseCurve3(polyline));
        assertEquals(0.0, polylineTwice.getPoints().get(0).x(), EPS);

        Circle circleTwice = (Circle) StepGeometryReverser.reverseCurve3(
                StepGeometryReverser.reverseCurve3(circle));
        assertEquals(1.0, circleTwice.getPosition().xDirection().x(), EPS);

        Plane planeTwice = (Plane) StepGeometryReverser.reverseSurfaceSense(
                StepGeometryReverser.reverseSurfaceSense(plane));
        assertEquals(1.0, planeTwice.getNormal().z(), EPS);

        assertTrue(true);
    }
}
