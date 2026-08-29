package com.minicad.preview.sampling;

import com.minicad.builder.StepAssemblyGraphBuilder;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.*;
import com.minicad.step.semantic.StepCadBuilder;

/**
 * Utility class for 4x4 affine transformation matrix operations.
 * Provides methods to build matrices from STEP placement entities,
 * transform points, and compose/invert matrices.
 *
 * <p>All methods are static and stateless - no instance state is maintained.
 */
public final class MatrixTransformHelper {

    private MatrixTransformHelper() {
        // Utility class - prevent instantiation
    }

    /**
     * Transforms a Cartesian point by a 4x4 transformation matrix.
     */
    public static CartesianPoint transformCartesian(CartesianPoint point, double[] matrix) {
        double x = point.x();
        double y = point.y();
        double z = point.z();
        return new CartesianPoint(
                matrix[0] * x + matrix[1] * y + matrix[2] * z + matrix[3],
                matrix[4] * x + matrix[5] * y + matrix[6] * z + matrix[7],
                matrix[8] * x + matrix[9] * y + matrix[10] * z + matrix[11]
        );
    }

    /**
     * Builds a 4x4 transformation matrix from a STEP placement entity.
     * Supports StepAxis2Placement3D and StepAxis2Placement2D.
     */
    public static double[] matrixForPlacementEntity(StepEntity placement, StepCadBuilder builder) {
        if (placement instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement3D = (StepAxis2Placement3D) placement;
            return StepAssemblyGraphBuilder.matrixForPlacement(placement3D);
        }
        if (placement instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement2D = (StepAxis2Placement2D) placement;
            CartesianPoint origin = pointFromPlacement(placement2D);
            if (origin == null) return null;
            Vector3 x;
            if (placement2D.refDirection() == null) {
                x = new Vector3(1.0, 0.0, 0.0);
            } else {
                java.util.List<Double> ratios = placement2D.refDirection().directionRatios();
                x = new Vector3(ratios.get(0), ratios.get(1), 0.0).normalize().asVector();
            }
            Vector3 z = new Vector3(0.0, 0.0, 1.0);
            Vector3 y = z.cross(x).normalize().asVector();
            return new double[]{
                    x.x(), y.x(), z.x(), origin.x(),
                    x.y(), y.y(), z.y(), origin.y(),
                    x.z(), y.z(), z.z(), origin.z(),
                    0.0, 0.0, 0.0, 1.0
            };
        }
        return null;
    }

    /**
     * Extracts a 3D Cartesian point from a 2D placement entity.
     */
    public static CartesianPoint pointFromPlacement(StepAxis2Placement2D placement2D) {
        StepCartesianPoint point = placement2D.location();
        return new CartesianPoint(point.coordinates().get(0), point.coordinates().get(1), 0.0);
    }

    /**
     * Builds a transformation matrix for a mapped placement.
     * Computes the matrix that maps from the origin placement to the target placement.
     */
    public static double[] matrixForMappedPlacement(StepEntity mappedOrigin, StepEntity mappingTarget, StepCadBuilder builder) {
        StepEntity originPlacement = null;
        StepEntity targetPlacement = null;
        if (mappedOrigin instanceof StepAxis2Placement3D || mappedOrigin instanceof StepAxis2Placement2D) {
            originPlacement = mappedOrigin;
        }
        if (mappingTarget instanceof StepAxis2Placement3D || mappingTarget instanceof StepAxis2Placement2D) {
            targetPlacement = mappingTarget;
        }
        if (mappedOrigin instanceof StepRepresentation) {
            StepRepresentation mappedRep = (StepRepresentation) mappedOrigin;
            // Check for placement-like items in the representation
            for (StepEntity repItem : mappedRep.items()) {
                if (repItem instanceof StepAxis2Placement3D) {
                    originPlacement = repItem;
                    break;
                }
            }
        }
        double[] originMatrix = originPlacement == null ? null : matrixForPlacementEntity(originPlacement, builder);
        double[] targetMatrix = targetPlacement == null ? null : matrixForPlacementEntity(targetPlacement, builder);
        if (originMatrix == null || targetMatrix == null) return null;
        return composeMatrices(invertMatrix(targetMatrix), originMatrix);
    }

    /**
     * Inverts a 4x4 affine transformation matrix.
     * Assumes the matrix represents a rigid transformation (rotation + translation).
     */
    public static double[] invertMatrix(double[] m) {
        double[] inv = new double[16];
        inv[0] = m[0]; inv[1] = m[4]; inv[2] = m[8];
        inv[4] = m[1]; inv[5] = m[5]; inv[6] = m[9];
        inv[8] = m[2]; inv[9] = m[6]; inv[10] = m[10];
        Vector3 t = new Vector3(m[3], m[7], m[11]);
        Vector3 col0 = new Vector3(m[0], m[4], m[8]);
        Vector3 col1 = new Vector3(m[1], m[5], m[9]);
        Vector3 col2 = new Vector3(m[2], m[6], m[10]);
        double tx = -t.dot(col0); double ty = -t.dot(col1); double tz = -t.dot(col2);
        inv[3] = tx; inv[7] = ty; inv[11] = tz;
        inv[12] = 0.0; inv[13] = 0.0; inv[14] = 0.0; inv[15] = 1.0;
        return inv;
    }

    /**
     * Composes two 4x4 transformation matrices.
     * Returns a matrix representing transformation A followed by transformation B.
     */
    public static double[] composeMatrices(double[] a, double[] b) {
        double[] c = new double[16];
        c[0] = a[0]*b[0] + a[1]*b[4] + a[2]*b[8];
        c[1] = a[0]*b[1] + a[1]*b[5] + a[2]*b[9];
        c[2] = a[0]*b[2] + a[1]*b[6] + a[2]*b[10];
        c[3] = a[0]*b[3] + a[1]*b[7] + a[2]*b[11] + a[3];
        c[4] = a[4]*b[0] + a[5]*b[4] + a[6]*b[8];
        c[5] = a[4]*b[1] + a[5]*b[5] + a[6]*b[9];
        c[6] = a[4]*b[2] + a[5]*b[6] + a[6]*b[10];
        c[7] = a[4]*b[3] + a[5]*b[7] + a[6]*b[11] + a[7];
        c[8] = a[8]*b[0] + a[9]*b[4] + a[10]*b[8];
        c[9] = a[8]*b[1] + a[9]*b[5] + a[10]*b[9];
        c[10] = a[8]*b[2] + a[9]*b[6] + a[10]*b[10];
        c[11] = a[8]*b[3] + a[9]*b[7] + a[10]*b[11] + a[11];
        c[12] = 0.0; c[13] = 0.0; c[14] = 0.0; c[15] = 1.0;
        return c;
    }
}
