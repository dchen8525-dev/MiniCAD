package com.minicad.export.json;

import com.minicad.builder.StepAssemblyGraphBuilder;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Vector3;
import com.minicad.helper.MathUtilityHelper;
import com.minicad.preview.payload.EdgeCurvePayload;
import com.minicad.preview.payload.EdgePayload;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.VectorPayload;
import com.minicad.step.model.*;
import com.minicad.step.semantic.StepCadBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for placement and transformation operations.
 * Extracted from StepPreviewJsonExporter to improve maintainability.
 *
 * <p>This class handles matrix calculations for STEP entity placements,
 * coordinate system transformations, and mapped item transformations.
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
     * Transforms an edge payload using a transformation matrix.
     *
     * @param edge the edge payload to transform
     * @param mappedItemId the mapped item ID
     * @param matrix the 4x4 transformation matrix
     * @return the transformed edge payload
     */
    public static EdgePayload transformMappedEdge(EdgePayload edge, int mappedItemId, double[] matrix) {
        return transformMappedEdge(edge, mappedItemId, matrix, null, null);
    }

    /**
     * Transforms an edge payload using a transformation matrix with source information.
     *
     * @param edge the edge payload to transform
     * @param mappedItemId the mapped item ID
     * @param matrix the 4x4 transformation matrix
     * @param sourceType the source geometry type (optional)
     * @param sourceStepId the source STEP ID (optional)
     * @return the transformed edge payload
     */
    public static EdgePayload transformMappedEdge(
            EdgePayload edge,
            int mappedItemId,
            double[] matrix,
            String sourceType,
            Integer sourceStepId
    ) {
        List<PointPayload> points = edge.points().stream()
                .map(point -> MathUtilityHelper.transform(point, matrix))
                .collect(Collectors.toList());
        return new EdgePayload(
                mappedPayloadId(mappedItemId, edge.stepId(), 1),
                points,
                transformMappedCurve(edge.curve(), matrix, sourceType, sourceStepId),
                edge.color()
        );
    }

    /**
     * Transforms a face payload using a transformation matrix.
     *
     * @param face the face payload to transform
     * @param mappedItemId the mapped item ID
     * @param matrix the 4x4 transformation matrix
     * @return the transformed face payload
     */
    public static FacePayload transformMappedFace(FacePayload face, int mappedItemId, double[] matrix) {
        List<PointPayload> points = face.triangles().stream()
                .map(point -> MathUtilityHelper.transform(point, matrix))
                .collect(Collectors.toList());
        VectorPayload normal = MathUtilityHelper.transform(face.normal(), matrix);
        PointPayload origin = MathUtilityHelper.transform(face.origin(), matrix);
        return new FacePayload(
                mappedPayloadId(mappedItemId, face.stepId(), 0),
                face.name(),
                face.surfaceType(),
                origin,
                normal,
                face.sameSense(),
                face.color(),
                face.transparency(),
                face.pbr(),
                face.layers(),
                face.loops(),
                points,
                face.surface(),
                face.uvLoops()
        );
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

    private static EdgeCurvePayload transformMappedCurve(
            EdgeCurvePayload curve,
            double[] matrix,
            String sourceType,
            Integer sourceStepId
    ) {
        if (curve == null) {
            return null;
        }
        List<Double> center = curve.center() == null
                ? null
                : PreviewSerializers.pointList(MathUtilityHelper.transform(new PointPayload(curve.center().get(0), curve.center().get(1), curve.center().get(2)), matrix));
        List<Double> axis = curve.axis() == null
                ? null
                : PreviewSerializers.vectorList(MathUtilityHelper.transform(new VectorPayload(curve.axis().get(0), curve.axis().get(1), curve.axis().get(2)), matrix));
        List<Double> xDirection = curve.xDirection() == null
                ? null
                : PreviewSerializers.vectorList(MathUtilityHelper.transform(new VectorPayload(curve.xDirection().get(0), curve.xDirection().get(1), curve.xDirection().get(2)), matrix));
        List<Double> refDirection = curve.refDirection() == null
                ? null
                : PreviewSerializers.vectorList(MathUtilityHelper.transform(new VectorPayload(curve.refDirection().get(0), curve.refDirection().get(1), curve.refDirection().get(2)), matrix));
        return new EdgeCurvePayload(
                curve.stepId(),
                curve.type(),
                curve.basisType(),
                curve.basisStepId(),
                center,
                axis,
                xDirection,
                curve.radius(),
                curve.semiAxis1(),
                curve.semiAxis2(),
                curve.orientation(),
                curve.senseAgreement(),
                curve.offsetDistance(),
                curve.selfIntersect(),
                refDirection,
                curve.transformScale(),
                curve.masterRepresentation(),
                curve.associatedSurfaceTypes(),
                curve.associatedSurfaceStepIds(),
                sourceType != null ? sourceType : curve.sourceType(),
                sourceStepId != null ? sourceStepId : curve.sourceStepId(),
                curve.startAngle(),
                curve.sweepAngle()
        );
    }

    private static int mappedPayloadId(int mappedItemId, int sourceId, int typeIndex) {
        return mappedItemId * 1000000 + sourceId * 10 + typeIndex;
    }
}