package com.minicad.preview.sampling;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Point2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PreviewSamplingTest {

    private static final double EPS = 1e-9;

    // ── MatrixTransformHelper.transformCartesian ───────────────────────────

    @Test
    void transformCartesian_identityKeepsPoint() {
        double[] id = {
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
        CartesianPoint p = new CartesianPoint(1.0, 2.0, 3.0);
        CartesianPoint r = MatrixTransformHelper.transformCartesian(p, id);
        assertEquals(1.0, r.x(), EPS);
        assertEquals(2.0, r.y(), EPS);
        assertEquals(3.0, r.z(), EPS);
    }

    @Test
    void transformCartesian_translation() {
        // translation lives in the last column (m3, m7, m11)
        double[] translate = {
                1, 0, 0, 5,
                0, 1, 0, 7,
                0, 0, 1, 9,
                0, 0, 0, 1
        };
        CartesianPoint p = new CartesianPoint(1.0, 2.0, 3.0);
        CartesianPoint r = MatrixTransformHelper.transformCartesian(p, translate);
        assertEquals(6.0, r.x(), EPS);
        assertEquals(9.0, r.y(), EPS);
        assertEquals(12.0, r.z(), EPS);
    }

    @Test
    void transformCartesian_scale() {
        double[] scale = {
                2, 0, 0, 0,
                0, 2, 0, 0,
                0, 0, 2, 0,
                0, 0, 0, 1
        };
        CartesianPoint p = new CartesianPoint(1.0, 2.0, 3.0);
        CartesianPoint r = MatrixTransformHelper.transformCartesian(p, scale);
        assertEquals(2.0, r.x(), EPS);
        assertEquals(4.0, r.y(), EPS);
        assertEquals(6.0, r.z(), EPS);
    }

    // ── Curve3SamplingHelper.arcSweep ──────────────────────────────────────

    @Test
    void arcSweep_forwardPositive() {
        assertEquals(Math.PI / 2.0,
                Curve3SamplingHelper.arcSweep(0.0, Math.PI / 2.0, false, true), EPS);
    }

    @Test
    void arcSweep_forwardNegativeWraps() {
        // negative delta wraps up into (0, 2pi]
        assertEquals(3.0 * Math.PI / 2.0,
                Curve3SamplingHelper.arcSweep(0.0, -Math.PI / 2.0, false, true), EPS);
    }

    @Test
    void arcSweep_reverseNegative() {
        assertEquals(-Math.PI / 2.0,
                Curve3SamplingHelper.arcSweep(0.0, -Math.PI / 2.0, false, false), EPS);
    }

    @Test
    void arcSweep_reversePositiveWraps() {
        // positive delta wraps down into (-2pi, 0]
        assertEquals(-3.0 * Math.PI / 2.0,
                Curve3SamplingHelper.arcSweep(0.0, Math.PI / 2.0, false, false), EPS);
    }

    @Test
    void arcSweep_closedForcesFullTurn() {
        assertEquals(2.0 * Math.PI,
                Curve3SamplingHelper.arcSweep(0.0, 1.0, true, true), EPS);
        assertEquals(-2.0 * Math.PI,
                Curve3SamplingHelper.arcSweep(0.0, 1.0, true, false), EPS);
    }

    // ── Curve2SamplingHelper circle / ellipse ──────────────────────────────

    @Test
    void sampleCircle2Points_isClosedAndOnRadius() {
        Circle2 circle = new Circle2(new Point2(0.0, 0.0), new Direction2(1.0, 0.0), 5.0);
        List<Point2> pts = Curve2SamplingHelper.sampleCircle2Points(circle, 8);
        assertEquals(9, pts.size()); // segments + 1 (closed loop)
        // first and last coincide
        assertEquals(pts.get(0).x(), pts.get(8).x(), EPS);
        assertEquals(pts.get(0).y(), pts.get(8).y(), EPS);
        // every sampled point lies on the radius
        for (Point2 p : pts) {
            double d = Math.hypot(p.x(), p.y());
            assertEquals(5.0, d, EPS);
        }
    }

    @Test
    void sampleEllipse2Points_isClosedAndBounded() {
        Ellipse2 ellipse = new Ellipse2(new Point2(0.0, 0.0), new Direction2(1.0, 0.0), 4.0, 2.0);
        List<Point2> pts = Curve2SamplingHelper.sampleEllipse2Points(ellipse, 12);
        assertEquals(13, pts.size());
        // closed
        assertEquals(pts.get(0).x(), pts.get(12).x(), EPS);
        assertEquals(pts.get(0).y(), pts.get(12).y(), EPS);
        // first point is at the +semiAxis1 end along xDirection
        assertEquals(4.0, pts.get(0).x(), EPS);
        assertEquals(0.0, pts.get(0).y(), EPS);
        // all points stay within the axis-aligned bounding box
        for (Point2 p : pts) {
            assertTrue(Math.abs(p.x()) <= 4.0 + EPS);
            assertTrue(Math.abs(p.y()) <= 2.0 + EPS);
        }
    }

    // ── PreviewSurfaceSampler.sampleSurfaceGrid ────────────────────────────

    @Test
    void sampleSurfaceGrid_bilinearCornersMatchControlPoints() {
        // 2x2 control grid, degree 1 (bilinear) -> sampleGrid is exact at corners
        List<List<CartesianPoint>> control = new ArrayList<>();
        control.add(List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(1, 0, 0)));
        control.add(List.of(new CartesianPoint(0, 1, 0), new CartesianPoint(1, 1, 0)));

        BSplineSurface3 surface = new BSplineSurface3(
                1, 1, control,
                List.of(2, 2), List.of(2, 2),
                List.of(0.0, 1.0), List.of(0.0, 1.0));

        List<List<CartesianPoint>> grid = PreviewSurfaceSampler.sampleSurfaceGrid(surface, 3, 2);
        // uSegments=3 -> 4 rows, vSegments=2 -> 3 columns
        assertEquals(4, grid.size());
        assertEquals(3, grid.get(0).size());

        assertClose(new CartesianPoint(0, 0, 0), grid.get(0).get(0));   // (u=0, v=0)
        assertClose(new CartesianPoint(0, 1, 0), grid.get(3).get(0));   // (u=1, v=0)
        assertClose(new CartesianPoint(1, 0, 0), grid.get(0).get(2));   // (u=0, v=1)
        assertClose(new CartesianPoint(1, 1, 0), grid.get(3).get(2));   // (u=1, v=1)
    }

    private void assertClose(CartesianPoint expected, CartesianPoint actual) {
        assertEquals(expected.x(), actual.x(), 1e-9);
        assertEquals(expected.y(), actual.y(), 1e-9);
        assertEquals(expected.z(), actual.z(), 1e-9);
    }
}
