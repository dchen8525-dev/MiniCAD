package com.minicad.preview.sampling;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.CompositeCurve2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.DegenerateCurve2;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Hyperbola2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Parabola2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.Polyline2;
import com.minicad.geometry2d.RationalBSplineCurve2;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.TrimmedCurve2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Curve2SamplingTest {

    private static final double EPS = 1.0e-6;

    private static Point2 p2(double x, double y) {
        return new Point2(x, y);
    }

    private static Direction2 d2(double x, double y) {
        return new Direction2(x, y);
    }

    // ---- sampleLooseCurve2 branches ----

    @Test
    void sampleLooseLine2() {
        Line2 line = new Line2(p2(0, 0), d2(1, 0), 10.0);
        List<Point2> pts = Curve2SamplingHelper.sampleLooseCurve2(line);
        assertEquals(2, pts.size());
        assertTrue(pts.get(0).subtract(p2(0, 0)).norm() < EPS);
        assertTrue(pts.get(1).subtract(p2(10, 0)).norm() < EPS);
    }

    @Test
    void sampleLooseCircle2() {
        Circle2 circle = new Circle2(p2(0, 0), d2(1, 0), 1.0);
        List<Point2> pts = Curve2SamplingHelper.sampleLooseCurve2(circle);
        assertEquals(73, pts.size());
        assertTrue(pts.get(0).subtract(p2(1, 0)).norm() < 1.0e-9);
        assertTrue(pts.get(pts.size() - 1).subtract(p2(1, 0)).norm() < 1.0e-9);
    }

    @Test
    void sampleLooseEllipse2() {
        Ellipse2 ellipse = new Ellipse2(p2(0, 0), d2(1, 0), 3.0, 2.0);
        List<Point2> pts = Curve2SamplingHelper.sampleLooseCurve2(ellipse);
        assertEquals(73, pts.size());
    }

    @Test
    void sampleLooseParabola2() {
        Parabola2 parabola = new Parabola2(p2(0, 0), d2(1, 0), 1.0);
        List<Point2> pts = Curve2SamplingHelper.sampleLooseCurve2(parabola);
        assertEquals(73, pts.size());
    }

    @Test
    void sampleLooseHyperbola2() {
        Hyperbola2 hyperbola = new Hyperbola2(p2(0, 0), d2(1, 0), 2.0, 1.0);
        List<Point2> pts = Curve2SamplingHelper.sampleLooseCurve2(hyperbola);
        assertEquals(73, pts.size());
    }

    @Test
    void sampleLooseDegenerateCurve2() {
        DegenerateCurve2 degenerate = new DegenerateCurve2(p2(5, 7));
        List<Point2> pts = Curve2SamplingHelper.sampleLooseCurve2(degenerate);
        assertEquals(1, pts.size());
        assertTrue(pts.get(0).subtract(p2(5, 7)).norm() < EPS);
    }

    @Test
    void sampleLooseBSplineCurve2() {
        List<Point2> cps = Arrays.asList(p2(0, 0), p2(1, 0));
        BSplineCurve2 spline = new BSplineCurve2(1, cps, Arrays.asList(2, 2), Arrays.asList(0.0, 1.0));
        List<Point2> pts = Curve2SamplingHelper.sampleLooseCurve2(spline);
        assertEquals(73, pts.size());
    }

    @Test
    void sampleLooseRationalBSplineCurve2() {
        List<Point2> cps = Arrays.asList(p2(0, 0), p2(1, 0));
        RationalBSplineCurve2 spline = new RationalBSplineCurve2(
                1, cps, Arrays.asList(1.0, 1.0), Arrays.asList(2, 2), Arrays.asList(0.0, 1.0));
        List<Point2> pts = Curve2SamplingHelper.sampleLooseCurve2(spline);
        assertEquals(73, pts.size());
    }

    @Test
    void sampleLoosePolyline2() {
        Polyline2 polyline = new Polyline2(Arrays.asList(p2(0, 0), p2(1, 1), p2(2, 0)));
        List<Point2> pts = Curve2SamplingHelper.sampleLooseCurve2(polyline);
        assertEquals(3, pts.size());
        assertTrue(pts.get(0).subtract(p2(0, 0)).norm() < EPS);
        assertTrue(pts.get(2).subtract(p2(2, 0)).norm() < EPS);
    }

    @Test
    void sampleLooseCompositeCurve2() {
        Line2 a = new Line2(p2(0, 0), d2(1, 0), 1.0);
        Line2 b = new Line2(p2(1, 0), d2(0, 1), 1.0);
        CompositeCurve2 composite = new CompositeCurve2(Arrays.asList(a, b));
        List<Point2> pts = Curve2SamplingHelper.sampleLooseCurve2(composite);
        // segment a contributes 2 pts, segment b contributes 1 pt (start shared)
        assertEquals(3, pts.size());
    }

    @Test
    void sampleLooseTrimmedCurve2Closed() {
        Circle2 circle = new Circle2(p2(0, 0), d2(1, 0), 1.0);
        TrimmedCurve2 trimmed = new TrimmedCurve2(circle, 0.0, Math.PI, true);
        List<Point2> pts = Curve2SamplingHelper.sampleLooseCurve2(trimmed);
        assertTrue(pts.size() >= 2);
        assertTrue(pts.get(0).subtract(p2(1, 0)).norm() < 1.0e-6);
        assertTrue(pts.get(pts.size() - 1).subtract(p2(-1, 0)).norm() < 1.0e-6);
    }

    @Test
    void sampleLooseUnsupportedThrows() {
        // A Curve2 anonymous subtype has no matching branch
        Curve2 unknown = new Curve2() {
            @Override
            public Point2 pointAt(double parameter) {
                return p2(0, 0);
            }

            @Override
            public boolean contains(Point2 point) {
                return false;
            }
        };
        assertThrows(UnsupportedGeometryException.class, () -> Curve2SamplingHelper.sampleLooseCurve2(unknown));
    }

    // ---- sampleTrimmedCurve2 open branch ----

    @Test
    void sampleTrimmedCurve2Open() {
        Line2 line = new Line2(p2(0, 0), d2(1, 0), 10.0);
        TrimmedCurve2 trimmed = new TrimmedCurve2(line, 0.25, 0.75, true);
        Point2 trimStart = trimmed.trimStart();
        Point2 trimEnd = trimmed.trimEnd();
        List<Point2> pts = Curve2SamplingHelper.sampleTrimmedCurve2(trimmed, 72);
        assertTrue(pts.size() >= 2);
        assertTrue(pts.get(0).subtract(trimStart).norm() < 1.0e-6);
        assertTrue(pts.get(pts.size() - 1).subtract(trimEnd).norm() < 1.0e-6);
    }

    @Test
    void sampleTrimmedCurve2DegenerateBasis() {
        // basis produces a single point -> falls back to trim endpoints
        DegenerateCurve2 degenerate = new DegenerateCurve2(p2(4, 4));
        TrimmedCurve2 trimmed = new TrimmedCurve2(degenerate, 0.0, 1.0, true);
        List<Point2> pts = Curve2SamplingHelper.sampleTrimmedCurve2(trimmed, 72);
        assertEquals(2, pts.size());
    }

    // ---- appendClosedTrimmedPoints2 both directions ----

    @Test
    void appendClosedTrimmedPoints2Reversed() {
        List<Point2> target = new ArrayList<>();
        List<Point2> basis = Arrays.asList(p2(0, 0), p2(1, 0), p2(2, 0));
        Curve2SamplingHelper.appendClosedTrimmedPoints2(target, basis, 0, 2, false);
        assertTrue(target.size() >= 1);
        assertTrue(target.get(target.size() - 1).subtract(p2(2, 0)).norm() < EPS);
    }

    // ---- nearestPointIndex2 / addDistinctPoint2 ----

    @Test
    void nearestPointIndex2PicksClosest() {
        List<Point2> pts = Arrays.asList(p2(0, 0), p2(10, 0), p2(20, 0));
        assertEquals(1, Curve2SamplingHelper.nearestPointIndex2(pts, p2(11, 0)));
        assertEquals(0, Curve2SamplingHelper.nearestPointIndex2(pts, p2(1, 0)));
    }

    @Test
    void addDistinctPoint2SkipsDuplicates() {
        List<Point2> pts = new ArrayList<>();
        pts.add(p2(0, 0));
        Curve2SamplingHelper.addDistinctPoint2(pts, p2(0, 0));
        assertEquals(1, pts.size());
        Curve2SamplingHelper.addDistinctPoint2(pts, p2(1, 0));
        assertEquals(2, pts.size());
    }

    // ---- curveTypeName(Curve2) ----

    @Test
    void curveTypeNameCurve2AllBranches() {
        assertEquals("LINE", Curve2SamplingHelper.curveTypeName(new Line2(p2(0, 0), d2(1, 0))));
        assertEquals("CIRCLE", Curve2SamplingHelper.curveTypeName(new Circle2(p2(0, 0), d2(1, 0), 1.0)));
        assertEquals("ELLIPSE", Curve2SamplingHelper.curveTypeName(new Ellipse2(p2(0, 0), d2(1, 0), 3.0, 2.0)));
        assertEquals("PARABOLA", Curve2SamplingHelper.curveTypeName(new Parabola2(p2(0, 0), d2(1, 0), 1.0)));
        assertEquals("HYPERBOLA", Curve2SamplingHelper.curveTypeName(new Hyperbola2(p2(0, 0), d2(1, 0), 2.0, 1.0)));
        assertEquals("DEGENERATE_CURVE", Curve2SamplingHelper.curveTypeName(new DegenerateCurve2(p2(0, 0))));
        List<Point2> cps = Arrays.asList(p2(0, 0), p2(1, 0));
        assertEquals("B_SPLINE_CURVE", Curve2SamplingHelper.curveTypeName(
                new BSplineCurve2(1, cps, Arrays.asList(2, 2), Arrays.asList(0.0, 1.0))));
        assertEquals("RATIONAL_B_SPLINE_CURVE", Curve2SamplingHelper.curveTypeName(
                new RationalBSplineCurve2(1, cps, Arrays.asList(1.0, 1.0), Arrays.asList(2, 2), Arrays.asList(0.0, 1.0))));
        assertEquals("TRIMMED_CURVE", Curve2SamplingHelper.curveTypeName(
                new TrimmedCurve2(new Circle2(p2(0, 0), d2(1, 0), 1.0), 0.0, Math.PI, true)));
        assertEquals("POLYLINE", Curve2SamplingHelper.curveTypeName(
                new Polyline2(Arrays.asList(p2(0, 0), p2(1, 0)))));
        assertEquals("COMPOSITE_CURVE", Curve2SamplingHelper.curveTypeName(
                new CompositeCurve2(Arrays.asList(new Line2(p2(0, 0), d2(1, 0))))));
    }

    // ---- curveTypeName(Curve3) representative branches ----

    private static Axis2Placement3D axisZ() {
        return new Axis2Placement3D(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1), new Direction3(1, 0, 0));
    }

    @Test
    void curveTypeNameCurve3Branches() {
        assertEquals("LINE", Curve2SamplingHelper.curveTypeName(
                new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0))));
        assertEquals("CIRCLE", Curve2SamplingHelper.curveTypeName(new Circle(axisZ(), 1.0)));
        assertEquals("ELLIPSE", Curve2SamplingHelper.curveTypeName(new Ellipse3(axisZ(), 2.0, 1.0)));
        assertEquals("POLYLINE", Curve2SamplingHelper.curveTypeName(
                new Polyline3(Arrays.asList(new CartesianPoint(0, 0, 0), new CartesianPoint(1, 0, 0)))));
        assertEquals("DEGENERATE_CURVE", Curve2SamplingHelper.curveTypeName(
                new DegenerateCurve3(new CartesianPoint(0, 0, 0))));
        Curve3 composite = new CompositeCurve3(Arrays.asList(
                new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0))));
        assertEquals("COMPOSITE_CURVE", Curve2SamplingHelper.curveTypeName(composite));
    }
}
