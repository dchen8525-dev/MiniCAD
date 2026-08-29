package com.minicad.preview.sampling;

import com.minicad.geometry.CartesianPoint;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepDirection;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatrixTransformHelperTest {

    private static final double EPS = 1.0e-9;

    private static double[] identity() {
        return new double[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
    }

    // rigid transform: rotation about Z by theta + translation (tx,ty,tz)
    private static double[] rigid(double theta, double tx, double ty, double tz) {
        double c = Math.cos(theta), s = Math.sin(theta);
        return new double[]{
                c, -s, 0, tx,
                s, c, 0, ty,
                0, 0, 1, tz,
                0, 0, 0, 1
        };
    }

    @Test
    void transformCartesianTranslation() {
        double[] m = rigid(0, 1, 2, 3);
        CartesianPoint p = MatrixTransformHelper.transformCartesian(new CartesianPoint(0, 0, 0), m);
        assertTrue(Math.abs(p.x() - 1) < EPS);
        assertTrue(Math.abs(p.y() - 2) < EPS);
        assertTrue(Math.abs(p.z() - 3) < EPS);
    }

    @Test
    void transformCartesianRotation90() {
        double[] m = rigid(Math.PI / 2, 0, 0, 0);
        CartesianPoint p = MatrixTransformHelper.transformCartesian(new CartesianPoint(1, 0, 0), m);
        assertTrue(Math.abs(p.x() - 0) < 1.0e-9);
        assertTrue(Math.abs(p.y() - 1) < 1.0e-9);
        assertTrue(Math.abs(p.z() - 0) < EPS);
    }

    @Test
    void transformCartesianIdentity() {
        CartesianPoint p = MatrixTransformHelper.transformCartesian(new CartesianPoint(4, 5, 6), identity());
        assertTrue(Math.abs(p.x() - 4) < EPS);
        assertTrue(Math.abs(p.y() - 5) < EPS);
        assertTrue(Math.abs(p.z() - 6) < EPS);
    }

    @Test
    void invertMatrixRoundTrip() {
        double[] m = rigid(0.7, 2, -3, 5);
        double[] inv = MatrixTransformHelper.invertMatrix(m);
        double[] back = MatrixTransformHelper.invertMatrix(inv);
        assertArrayEquals(m, back, 1.0e-9);
    }

    @Test
    void composeWithIdentity() {
        double[] m = rigid(0.3, 1, 1, 1);
        assertArrayEquals(m, MatrixTransformHelper.composeMatrices(m, identity()), 1.0e-9);
        assertArrayEquals(m, MatrixTransformHelper.composeMatrices(identity(), m), 1.0e-9);
    }

    @Test
    void composeWithInverseYieldsIdentity() {
        // For a rigid transform, composing the inverse with the original must be identity
        double[] m = rigid(0.7, 2, -3, 5);
        double[] inv = MatrixTransformHelper.invertMatrix(m);
        double[] composed = MatrixTransformHelper.composeMatrices(inv, m);
        assertArrayEquals(identity(), composed, 1.0e-9);
    }

    @Test
    void composeMatricesNonTrivial() {
        double[] a = rigid(Math.PI / 2, 0, 0, 0);
        double[] b = rigid(0, 1, 0, 0);
        double[] c = MatrixTransformHelper.composeMatrices(a, b);
        // compose(a, b) applies b first, then a: translate +X by 1 -> (2,0,0), then rotate 90 about Z -> (0,2,0)
        CartesianPoint p = MatrixTransformHelper.transformCartesian(new CartesianPoint(1, 0, 0), c);
        assertTrue(Math.abs(p.x() - 0) < 1.0e-9);
        assertTrue(Math.abs(p.y() - 2) < 1.0e-9);

        // equivalent to applying b then a sequentially
        CartesianPoint sequential = MatrixTransformHelper.transformCartesian(
                MatrixTransformHelper.transformCartesian(new CartesianPoint(1, 0, 0), b), a);
        assertTrue(Math.abs(sequential.x() - p.x()) < 1.0e-9);
        assertTrue(Math.abs(sequential.y() - p.y()) < 1.0e-9);
    }

    @Test
    void matrixForPlacementEntityAxis2Placement3D() {
        StepCartesianPoint loc = new StepCartesianPoint(1, "", Arrays.asList(0.0, 0.0, 0.0));
        StepDirection axis = new StepDirection(2, "", Arrays.asList(0.0, 0.0, 1.0));
        StepDirection ref = new StepDirection(3, "", Arrays.asList(1.0, 0.0, 0.0));
        StepAxis2Placement3D place = new StepAxis2Placement3D(4, "", loc, axis, ref);
        double[] m = MatrixTransformHelper.matrixForPlacementEntity(place, null);
        assertNotNull(m);
        assertTrue(m.length == 16);
    }

    @Test
    void matrixForPlacementEntityAxis2Placement2DWithRef() {
        StepCartesianPoint loc = new StepCartesianPoint(10, "", Arrays.asList(1.0, 2.0, 0.0));
        StepDirection ref = new StepDirection(11, "", Arrays.asList(1.0, 0.0, 0.0));
        StepAxis2Placement2D place = new StepAxis2Placement2D(12, "", loc, ref);
        double[] m = MatrixTransformHelper.matrixForPlacementEntity(place, null);
        assertNotNull(m);
        assertTrue(m.length == 16);
        assertTrue(Math.abs(m[3] - 1.0) < EPS); // origin x
        assertTrue(Math.abs(m[7] - 2.0) < EPS); // origin y
        assertTrue(Math.abs(m[0] - 1.0) < EPS); // x basis
    }

    @Test
    void matrixForPlacementEntityAxis2Placement2DNullRef() {
        StepCartesianPoint loc = new StepCartesianPoint(10, "", Arrays.asList(3.0, 4.0, 0.0));
        StepAxis2Placement2D place = new StepAxis2Placement2D(13, "", loc, null);
        double[] m = MatrixTransformHelper.matrixForPlacementEntity(place, null);
        assertNotNull(m);
        assertTrue(Math.abs(m[3] - 3.0) < EPS);
        assertTrue(Math.abs(m[0] - 1.0) < EPS); // default x = (1,0,0)
    }

    @Test
    void pointFromPlacement2D() {
        StepCartesianPoint loc = new StepCartesianPoint(10, "", Arrays.asList(1.0, 2.0, 9.0));
        StepAxis2Placement2D place = new StepAxis2Placement2D(12, "", loc, null);
        CartesianPoint p = MatrixTransformHelper.pointFromPlacement(place);
        assertTrue(Math.abs(p.x() - 1.0) < EPS);
        assertTrue(Math.abs(p.y() - 2.0) < EPS);
        assertTrue(Math.abs(p.z() - 0.0) < EPS);
    }

    @Test
    void matrixForPlacementEntityUnknownReturnsNull() {
        assertNull(MatrixTransformHelper.matrixForPlacementEntity(null, null));
    }

    @Test
    void matrixForMappedPlacementNullReturnsNull() {
        assertNull(MatrixTransformHelper.matrixForMappedPlacement(null, null, null));
    }

    @Test
    void matrixForMappedPlacementBothPlacements() {
        StepCartesianPoint locA = new StepCartesianPoint(1, "", Arrays.asList(0.0, 0.0, 0.0));
        StepDirection axisA = new StepDirection(2, "", Arrays.asList(0.0, 0.0, 1.0));
        StepDirection refA = new StepDirection(3, "", Arrays.asList(1.0, 0.0, 0.0));
        StepAxis2Placement3D placeA = new StepAxis2Placement3D(4, "", locA, axisA, refA);

        StepCartesianPoint locB = new StepCartesianPoint(5, "", Arrays.asList(1.0, 0.0, 0.0));
        StepDirection axisB = new StepDirection(6, "", Arrays.asList(0.0, 0.0, 1.0));
        StepDirection refB = new StepDirection(7, "", Arrays.asList(1.0, 0.0, 0.0));
        StepAxis2Placement3D placeB = new StepAxis2Placement3D(8, "", locB, axisB, refB);

        double[] m = MatrixTransformHelper.matrixForMappedPlacement(placeA, placeB, null);
        assertNotNull(m);
        assertTrue(m.length == 16);
    }
}
