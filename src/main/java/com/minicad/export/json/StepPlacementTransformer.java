package com.minicad.export.json;

import com.minicad.builder.StepAssemblyGraphBuilder;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.*;
import com.minicad.step.semantic.StepCadBuilder;

import java.util.List;

/**
 * Utility class for placement and transformation operations.
 * Extracted from StepPreviewJsonExporter to improve maintainability.
 *
 * <p>This class handles matrix calculations for STEP entity placements and
 * coordinate system transformations. Transforming mapped-item payloads by a
 * matrix lives in {@link StepMappedItemTransformer}, which is the single
 * implementation — this class used to carry a second, divergent copy that
 * nothing called.</p>
 */
public final class StepPlacementTransformer {

    private StepPlacementTransformer() {
        // Utility class - prevent instantiation
    }

    /**
     * Computes the transformation matrix for a mapped placement.
     * Used for assembly instance positioning.
     *
     * @param mappedOrigin the source placement
     * @param mappingTarget the target placement
     * @param builder the CAD builder for geometry resolution
     * @return the 4x4 transformation matrix, or null if placement types are not supported
     */
    public static double[] matrixForMappedPlacement(
            StepEntity mappedOrigin,
            StepEntity mappingTarget,
            StepCadBuilder builder
    ) {
        double[] sourceMatrix = matrixForPlacementEntity(mappedOrigin, builder);
        double[] targetMatrix = matrixForPlacementEntity(mappingTarget, builder);
        if (sourceMatrix == null || targetMatrix == null) {
            return null;
        }
        return StepAssemblyGraphBuilder.multiplyMatrices(
                targetMatrix,
                StepAssemblyGraphBuilder.inverseRigidTransform(sourceMatrix)
        );
    }

    /**
     * Computes the transformation matrix for a placement entity.
     * Supports Axis2Placement3D and Axis2Placement2D.
     *
     * @param placement the placement entity
     * @param builder the CAD builder for geometry resolution
     * @return the 4x4 transformation matrix, or null if placement type is not supported
     */
    public static double[] matrixForPlacementEntity(StepEntity placement, StepCadBuilder builder) {
        if (placement instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement3D = (StepAxis2Placement3D) placement;
            return StepAssemblyGraphBuilder.matrixForPlacement(placement3D);
        }
        if (placement instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement2D = (StepAxis2Placement2D) placement;
            CartesianPoint origin = pointFromPlacement(placement2D);
            if (origin == null) {
                return null;
            }
            Vector3 x;
            if (placement2D.refDirection() == null) {
                x = new Vector3(1.0, 0.0, 0.0);
            } else {
                List<Double> ratios = placement2D.refDirection().directionRatios();
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
     * Computes the transformation matrix for a mapped item.
     * Handles both CartesianTransformationOperator and Axis2Placement3D targets.
     *
     * @param mappedItem the mapped item
     * @param builder the CAD builder for geometry resolution
     * @return the 4x4 transformation matrix, or null if mapping is not supported
     */
    public static double[] mappedItemMatrix(StepMappedItem mappedItem, StepCadBuilder builder) {
        StepRepresentationMap mappingSource = mappedItem.mappingSource();
        if (!(mappingSource.mappedOrigin() instanceof StepAxis2Placement3D)) {
            return null;
        }
        StepAxis2Placement3D originPlacement = (StepAxis2Placement3D) mappingSource.mappedOrigin();
        double[] sourceMatrix = StepAssemblyGraphBuilder.matrixForPlacement(originPlacement);
        double[] targetMatrix;
        if (mappedItem.mappingTarget() instanceof StepCartesianTransformationOperator) {
            StepCartesianTransformationOperator transformation = (StepCartesianTransformationOperator) mappedItem.mappingTarget();
            targetMatrix = matrixForTransformationOperator(transformation, builder);
        } else if (mappedItem.mappingTarget() instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D targetPlacement = (StepAxis2Placement3D) mappedItem.mappingTarget();
            targetMatrix = StepAssemblyGraphBuilder.matrixForPlacement(targetPlacement);
        } else {
            return null;
        }
        return StepAssemblyGraphBuilder.multiplyMatrices(
                targetMatrix,
                StepAssemblyGraphBuilder.inverseRigidTransform(sourceMatrix)
        );
    }

    /**
     * Computes the transformation matrix for a Cartesian transformation operator.
     * Handles axis vectors, scale, and origin point.
     *
     * @param transformation the transformation operator
     * @param builder the CAD builder for geometry resolution
     * @return the 4x4 transformation matrix
     */
    public static double[] matrixForTransformationOperator(
            StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        Vector3 axis1 = transformation.axis1() == null
                ? new Vector3(1.0, 0.0, 0.0)
                : builder.buildDirection(transformation.axis1().id()).asVector();
        Vector3 axis2;
        if (transformation.axis2() != null) {
            axis2 = builder.buildDirection(transformation.axis2().id()).asVector();
        } else {
            Vector3 fallback = new Vector3(0.0, 1.0, 0.0);
            axis2 = axis1.cross(fallback).isZero() ? new Vector3(0.0, 0.0, 1.0) : fallback;
        }
        Vector3 axis3;
        if (transformation.axis3() != null) {
            axis3 = builder.buildDirection(transformation.axis3().id()).asVector();
        } else {
            Vector3 cross = axis1.cross(axis2);
            axis3 = cross.isZero() ? new Vector3(0.0, 0.0, 1.0) : cross.normalize().asVector();
        }
        double scale = transformation.scale() == null ? 1.0 : transformation.scale();
        CartesianPoint origin = builder.buildPoint(transformation.localOrigin().id());
        return new double[]{
                axis1.x() * scale, axis2.x() * scale, axis3.x() * scale, origin.x(),
                axis1.y() * scale, axis2.y() * scale, axis3.y() * scale, origin.y(),
                axis1.z() * scale, axis2.z() * scale, axis3.z() * scale, origin.z(),
                0.0, 0.0, 0.0, 1.0
        };
    }

    /**
     * Creates a CartesianPoint from a STEP CartesianPoint.
     * Handles 2D and 3D points by padding missing coordinates with zero.
     *
     * @param point the STEP CartesianPoint
     * @return the CartesianPoint
     */
    public static CartesianPoint pointFromStep(StepCartesianPoint point) {
        double x = point.coordinates().get(0);
        double y = point.coordinates().size() > 1 ? point.coordinates().get(1) : 0.0;
        double z = point.coordinates().size() > 2 ? point.coordinates().get(2) : 0.0;
        return new CartesianPoint(x, y, z);
    }

    // ─── Private helper methods ─────────────────────────────────────

    private static CartesianPoint pointFromPlacement(StepAxis2Placement2D placement) {
        if (placement.location() == null) {
            return null;
        }
        List<Double> coords = placement.location().coordinates();
        double z = coords.size() > 2 ? coords.get(2) : 0.0;
        return new CartesianPoint(coords.get(0), coords.size() > 1 ? coords.get(1) : 0.0, z);
    }

}
