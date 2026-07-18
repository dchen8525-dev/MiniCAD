package com.minicad.export.json;

import com.minicad.builder.StepAssemblyGraphBuilder;
import com.minicad.helper.MathUtilityHelper;
import com.minicad.helper.StepMetadataExtractor;
import com.minicad.preview.payload.*;
import com.minicad.step.model.StepMappedItem;
import com.minicad.step.model.StepRepresentationMap;
import com.minicad.step.semantic.StepCadBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class for transforming mapped items in STEP geometry.
 * Extracted from StepPreviewJsonExporter for better organization.
 */
public final class StepMappedItemTransformer {

    private StepMappedItemTransformer() {
        // Utility class
    }

    /**
     * Computes the transformation matrix for a mapped item.
     */
    public static double[] mappedItemMatrix(StepMappedItem mappedItem, StepCadBuilder builder) {
        StepRepresentationMap mappingSource = mappedItem.mappingSource();
        if (!(mappingSource.mappedOrigin() instanceof com.minicad.step.model.StepAxis2Placement3D)) {
            return null;
        }
        com.minicad.step.model.StepAxis2Placement3D originPlacement = (com.minicad.step.model.StepAxis2Placement3D) mappingSource.mappedOrigin();
        double[] sourceMatrix = StepAssemblyGraphBuilder.matrixForPlacement(originPlacement);
        double[] targetMatrix;
        if (mappedItem.mappingTarget() instanceof com.minicad.step.model.StepCartesianTransformationOperator) {
            com.minicad.step.model.StepCartesianTransformationOperator transformation = (com.minicad.step.model.StepCartesianTransformationOperator) mappedItem.mappingTarget();
            targetMatrix = StepPlacementTransformer.matrixForTransformationOperator(transformation, builder);
        } else if (mappedItem.mappingTarget() instanceof com.minicad.step.model.StepAxis2Placement3D) {
            com.minicad.step.model.StepAxis2Placement3D targetPlacement = (com.minicad.step.model.StepAxis2Placement3D) mappedItem.mappingTarget();
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
     * Transforms an edge payload with the given matrix.
     */
    public static EdgePayload transformMappedEdge(EdgePayload edge, int mappedItemId, double[] matrix) {
        return transformMappedEdge(edge, mappedItemId, matrix, null, null);
    }

    /**
     * Transforms an edge payload with the given matrix and source information.
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
     * Transforms a face payload with the given matrix and metadata.
     */
    public static FacePayload transformMappedFace(
            FacePayload face,
            int mappedItemId,
            double[] matrix,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<LoopPayload> loops = face.loops().stream()
                .map(loop -> new LoopPayload(
                        loop.outer(),
                        loop.points().stream().map(point -> MathUtilityHelper.transform(point, matrix)).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        List<PointPayload> triangles = face.triangles().stream()
                .map(point -> MathUtilityHelper.transform(point, matrix))
                .collect(Collectors.toList());
        int[] rgb = metadata.rgb() != null ? metadata.rgb() : null;
        ColorPayload color = rgb == null ? face.color() : PayloadConversionHelper.toColorPayload(rgb);
        double transparency = metadata.transparency() > 0 ? metadata.transparency() : face.transparency();
        PbrPayload pbr = metadata.pbr() != null ? PayloadConversionHelper.toPbrPayload(metadata.pbr()) : face.pbr();
        List<String> layers = metadata.layers().isEmpty() ? face.layers() : metadata.layers();
        return new FacePayload(
                mappedPayloadId(mappedItemId, face.stepId(), 2),
                face.name(),
                face.surfaceType(),
                MathUtilityHelper.transform(face.origin(), matrix),
                MathUtilityHelper.transform(face.normal(), matrix),
                face.sameSense(),
                color,
                transparency,
                pbr,
                layers,
                loops,
                triangles,
                null,
                List.of()
        );
    }

    /**
     * Transforms a curve payload with the given matrix and source information.
     */
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

    /**
     * Generates a unique payload ID for mapped items.
     */
    private static int mappedPayloadId(int mappedItemId, int sourceId, int salt) {
        return -Math.abs(mappedItemId * 10_000 + sourceId * 10 + salt);
    }
}