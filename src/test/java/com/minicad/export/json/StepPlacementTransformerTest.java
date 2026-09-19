package com.minicad.export.json;

import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Runtime behaviour tests for StepPlacementTransformer.matrixForPlacementEntity
 * and matrixForMappedPlacement, which are the only homes of both.
 *
 * <p>They used to have a second implementation in
 * preview/sampling/MatrixTransformHelper, reachable from nothing but its own
 * test. That copy delegated to the same StepAssemblyGraphBuilder for the
 * Axis2Placement3D case, so the two agreed there, but it differed in two places
 * that no longer exist: its own pointFromPlacement hard-coded z = 0.0 where this
 * one reads the coordinates' third component when present, and its
 * matrixForMappedPlacement composed inverse(target) * origin where this one
 * composes target * inverse(origin) -- opposite sides of the map. The tests
 * below therefore pin the live composition order rather than a matrix length,
 * and pin the 2D translation read here.
 */
class StepPlacementTransformerTest {

    private static final double EPS = 1.0e-9;

    @Test
    @DisplayName("Axis2Placement3D at the origin gives an identity basis")
    void axis2Placement3DAtOriginGivesIdentityBasis() {
        double[] matrix = StepPlacementTransformer.matrixForPlacementEntity(placement3D(), null);

        assertNotNull(matrix);
        assertEquals(16, matrix.length);
        assertEquals(1.0, matrix[0], EPS);
        assertEquals(1.0, matrix[5], EPS);
        assertEquals(1.0, matrix[10], EPS);
        assertEquals(0.0, matrix[3], EPS);
        assertEquals(0.0, matrix[7], EPS);
        assertEquals(0.0, matrix[11], EPS);
    }

    @Test
    @DisplayName("Axis2Placement2D keeps its origin in the translation column")
    void axis2Placement2DKeepsItsOrigin() {
        StepAxis2Placement2D placement = placement2D(Arrays.asList(1.0, 2.0, 0.0), refDirection());

        double[] matrix = StepPlacementTransformer.matrixForPlacementEntity(placement, null);

        assertNotNull(matrix);
        assertEquals(16, matrix.length);
        assertEquals(1.0, matrix[3], EPS);
        assertEquals(2.0, matrix[7], EPS);
        assertEquals(0.0, matrix[11], EPS);
        assertEquals(1.0, matrix[0], EPS);
    }

    @Test
    @DisplayName("Axis2Placement2D without a refDirection defaults to +X")
    void axis2Placement2DWithoutRefDirectionDefaultsToX() {
        double[] matrix = StepPlacementTransformer.matrixForPlacementEntity(
                placement2D(Arrays.asList(3.0, 4.0, 0.0), null), null);

        assertNotNull(matrix);
        assertEquals(3.0, matrix[3], EPS);
        assertEquals(4.0, matrix[7], EPS);
        assertEquals(1.0, matrix[0], EPS);
    }

    @Test
    @DisplayName("Axis2Placement2D reads a third coordinate into z")
    void axis2Placement2DReadsAThirdCoordinateIntoZ() {
        // The deleted preview copy forced z to 0.0 here; this one takes whatever
        // the placement's own cartesian point carries.
        double[] matrix = StepPlacementTransformer.matrixForPlacementEntity(
                placement2D(Arrays.asList(1.0, 2.0, 9.0), null), null);

        assertNotNull(matrix);
        assertEquals(1.0, matrix[3], EPS);
        assertEquals(2.0, matrix[7], EPS);
        assertEquals(9.0, matrix[11], EPS);
    }

    @Test
    @DisplayName("an unsupported placement type has no matrix")
    void unsupportedPlacementTypeHasNoMatrix() {
        assertNull(StepPlacementTransformer.matrixForPlacementEntity(null, null));
    }

    @Test
    @DisplayName("a mapped placement without both placements has no matrix")
    void mappedPlacementWithoutBothPlacementsHasNoMatrix() {
        assertNull(StepPlacementTransformer.matrixForMappedPlacement(null, null, null));
    }

    @Test
    @DisplayName("a mapped placement composes the target with the inverse of the source")
    void mappedPlacementComposesTargetWithInverseOfSource() {
        // A quarter turn about Z as the source and a +X translation as the target.
        // The fixture has to rotate: with a translation-only source the two
        // composition orders commute and the assertion below would hold under the
        // flipped order too -- which is precisely the order the deleted preview
        // copy used, so the fixture would have pinned nothing.
        double[] matrix = StepPlacementTransformer.matrixForMappedPlacement(
                rotating3D(), placement3DAt(1.0, 0.0, 0.0), null);

        assertNotNull(matrix);
        assertArrayEquals(
                new double[]{
                        0.0, 1.0, 0.0, 1.0,
                        -1.0, 0.0, 0.0, 0.0,
                        0.0, 0.0, 1.0, 0.0,
                        0.0, 0.0, 0.0, 1.0
                },
                matrix,
                EPS,
                "target * inverse(source) takes the source origin to the target origin; the "
                        + "reversed order would put (0,-1,0) in the translation column");
    }

    /** A quarter turn about Z at the origin: axis +Z, refDirection +Y. */
    private static StepAxis2Placement3D rotating3D() {
        return new StepAxis2Placement3D(
                5,
                "",
                new StepCartesianPoint(6, "", Arrays.asList(0.0, 0.0, 0.0)),
                new StepDirection(7, "", Arrays.asList(0.0, 0.0, 1.0)),
                new StepDirection(8, "", Arrays.asList(0.0, 1.0, 0.0)));
    }

    private static StepAxis2Placement3D placement3D() {
        return placement3DAt(0.0, 0.0, 0.0);
    }

    private static StepAxis2Placement3D placement3DAt(double x, double y, double z) {
        return new StepAxis2Placement3D(
                4,
                "",
                new StepCartesianPoint(1, "", Arrays.asList(x, y, z)),
                new StepDirection(2, "", Arrays.asList(0.0, 0.0, 1.0)),
                new StepDirection(3, "", Arrays.asList(1.0, 0.0, 0.0)));
    }

    private static StepDirection refDirection() {
        return new StepDirection(11, "", Arrays.asList(1.0, 0.0, 0.0));
    }

    private static StepAxis2Placement2D placement2D(java.util.List<Double> origin, StepDirection ref) {
        return new StepAxis2Placement2D(12, "", new StepCartesianPoint(10, "", origin), ref);
    }
}
