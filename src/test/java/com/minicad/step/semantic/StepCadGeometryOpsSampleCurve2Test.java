package com.minicad.step.semantic;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry2d.BSplineCurve2;
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
import com.minicad.geometry2d.TrimmedCurve2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runtime behaviour tests for the SAMPLE_CURVE2_RULES dispatch in
 * StepCadGeometryOps.sampleCurve2, one test per branch plus the terminal
 * throw for a type no rule covers.
 *
 * The builder is never consulted by sampleCurve2 (its handlers only use
 * geometry arithmetic and the instance's own trim helpers), so a null
 * builder is fine here -- same convention as StepCadCurveBuilderTest.
 */
class StepCadGeometryOpsSampleCurve2Test {

    private static final double EPS = 1.0e-9;

    private final StepCadGeometryOps ops = new StepCadGeometryOps(null);

    private static Point2 p2(double x, double y) {
        return new Point2(x, y);
    }

    private static Direction2 d2(double x, double y) {
        return new Direction2(x, y);
    }

    private static void assertPoint2(Point2 point, double x, double y) {
        assertEquals(x, point.getX(), 1.0e-6);
        assertEquals(y, point.getY(), 1.0e-6);
    }

    @Test
    @DisplayName("Line2 samples its two endpoints regardless of segment count")
    void line2() {
        Line2 line = new Line2(p2(0, 0), d2(1, 0), 10.0);
        List<Point2> pts = ops.sampleCurve2(line, 72);
        assertEquals(2, pts.size());
        assertPoint2(pts.get(0), 0.0, 0.0);
        assertPoint2(pts.get(1), 10.0, 0.0);
    }

    @Test
    @DisplayName("Circle2 sweeps 0..2pi with segments + 1 points")
    void circle2() {
        Circle2 circle = new Circle2(p2(0, 0), d2(1, 0), 1.0);
        List<Point2> pts = ops.sampleCurve2(circle, 8);
        assertEquals(9, pts.size());
        assertPoint2(pts.get(0), 1.0, 0.0);
        assertPoint2(pts.get(2), 0.0, 1.0);
        assertPoint2(pts.get(8), 1.0, 0.0);
    }

    @Test
    @DisplayName("Ellipse2 sweeps 0..2pi with segments + 1 points")
    void ellipse2() {
        Ellipse2 ellipse = new Ellipse2(p2(0, 0), d2(1, 0), 3.0, 2.0);
        List<Point2> pts = ops.sampleCurve2(ellipse, 8);
        assertEquals(9, pts.size());
        assertPoint2(pts.get(0), 3.0, 0.0);
        assertPoint2(pts.get(2), 0.0, 2.0);
    }

    @Test
    @DisplayName("BSplineCurve2 sweeps its own start..end parameter range")
    void bsplineCurve2() {
        BSplineCurve2 spline = new BSplineCurve2(
                1, Arrays.asList(p2(0, 0), p2(10, 0)), Arrays.asList(2, 2), Arrays.asList(1.0, 4.0));
        List<Point2> pts = ops.sampleCurve2(spline, 6);
        assertEquals(7, pts.size());
        // startParameter/endParameter are the first/last knot: 1.0 and 4.0
        assertPoint2(pts.get(0), spline.pointAt(1.0).getX(), spline.pointAt(1.0).getY());
        assertPoint2(pts.get(6), spline.pointAt(4.0).getX(), spline.pointAt(4.0).getY());
        // sweep parameter at index 2 is 1 + (4-1)*2/6 = 2
        assertPoint2(pts.get(2), spline.pointAt(2.0).getX(), spline.pointAt(2.0).getY());
    }

    @Test
    @DisplayName("RationalBSplineCurve2 samples through its own sample(segments)")
    void rationalBsplineCurve2() {
        RationalBSplineCurve2 spline = new RationalBSplineCurve2(
                1, Arrays.asList(p2(0, 0), p2(10, 0)), Arrays.asList(1.0, 1.0),
                Arrays.asList(2, 2), Arrays.asList(0.0, 1.0));
        List<Point2> pts = ops.sampleCurve2(spline, 6);
        assertEquals(7, pts.size());
        assertPoint2(pts.get(0), 0.0, 0.0);
        assertPoint2(pts.get(6), 10.0, 0.0);
    }

    @Test
    @DisplayName("TrimmedCurve2 samples its basis and trims to the trim points")
    void trimmedCurve2() {
        Circle2 circle = new Circle2(p2(0, 0), d2(1, 0), 1.0);
        TrimmedCurve2 trimmed = new TrimmedCurve2(circle, 0.0, Math.PI, true);
        List<Point2> pts = ops.sampleCurve2(trimmed, 8);
        assertTrue(pts.size() >= 2);
        assertPoint2(pts.get(0), 1.0, 0.0);
        assertPoint2(pts.get(pts.size() - 1), -1.0, 0.0);
    }

    @Test
    @DisplayName("Polyline2 returns its own points")
    void polyline2() {
        Polyline2 polyline = new Polyline2(Arrays.asList(p2(0, 0), p2(1, 1), p2(2, 0)));
        List<Point2> pts = ops.sampleCurve2(polyline, 72);
        assertEquals(3, pts.size());
        assertPoint2(pts.get(1), 1.0, 1.0);
    }

    @Test
    @DisplayName("CompositeCurve2 concatenates segment samples, sharing junction points")
    void compositeCurve2() {
        Line2 a = new Line2(p2(0, 0), d2(1, 0), 1.0);
        Line2 b = new Line2(p2(1, 0), d2(0, 1), 1.0);
        CompositeCurve2 composite = new CompositeCurve2(Arrays.asList(a, b));
        List<Point2> pts = ops.sampleCurve2(composite, 72);
        // segment a contributes 2 pts, segment b contributes 1 pt (start shared)
        assertEquals(3, pts.size());
        assertPoint2(pts.get(0), 0.0, 0.0);
        assertPoint2(pts.get(2), 1.0, 1.0);
    }

    @Test
    @DisplayName("Parabola2 samples the fixed window t in [-2, 2]")
    void parabola2() {
        Parabola2 parabola = new Parabola2(p2(0, 0), d2(1, 0), 1.0);
        List<Point2> pts = ops.sampleCurve2(parabola, 4);
        assertEquals(5, pts.size());
        assertPoint2(pts.get(0), parabola.pointAt(-2.0).getX(), parabola.pointAt(-2.0).getY());
        assertPoint2(pts.get(2), parabola.pointAt(0.0).getX(), parabola.pointAt(0.0).getY());
        assertPoint2(pts.get(4), parabola.pointAt(2.0).getX(), parabola.pointAt(2.0).getY());
    }

    @Test
    @DisplayName("Hyperbola2 samples the fixed window t in [1, 2]")
    void hyperbola2() {
        Hyperbola2 hyperbola = new Hyperbola2(p2(0, 0), d2(1, 0), 2.0, 1.0);
        List<Point2> pts = ops.sampleCurve2(hyperbola, 4);
        assertEquals(5, pts.size());
        assertPoint2(pts.get(0), hyperbola.pointAt(1.0).getX(), hyperbola.pointAt(1.0).getY());
        assertPoint2(pts.get(4), hyperbola.pointAt(2.0).getX(), hyperbola.pointAt(2.0).getY());
    }

    @Test
    @DisplayName("a curve type with no rule throws UnsupportedGeometryException")
    void unsupportedTypeThrows() {
        // DegenerateCurve2 has no sampleCurve2 rule (it did not have one in the
        // original chain either), so it must hit the terminal throw.
        DegenerateCurve2 degenerate = new DegenerateCurve2(p2(5, 7));
        assertThrows(UnsupportedGeometryException.class, () -> ops.sampleCurve2(degenerate, 72));
    }
}
