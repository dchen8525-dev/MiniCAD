package com.minicad.app;

import com.minicad.common.Epsilon;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Vector3;

import java.util.List;

record PreviewPayload(
        PreviewStats stats,
        BoundsPayload bounds,
        ValidationPayload validation,
        ProductMetadataExtractor.ProductMetadata product,
        UnitExtractor.UnitInfo units,
        List<PmiPayload> pmi,
        List<UnsupportedBooleanPayload> unsupportedBooleans,
        List<UnsupportedFacePayload> unsupportedFaces,
        List<EdgePayload> edges,
        List<FacePayload> faces,
        List<RepresentationPayload> representations,
        List<InstancePayload> instances
) {
}

record AssemblyData(
        List<RepresentationPayload> representations,
        List<InstancePayload> instances,
        List<UnsupportedFacePayload> unsupportedFaces,
        GeometrySummary summary,
        BoundsPayload bounds
) {
}

record AssemblyMetrics(
        GeometrySummary summary,
        BoundsPayload bounds
) {
}

record GeometryCollection(
        List<EdgePayload> edges,
        List<FacePayload> faces,
        List<UnsupportedFacePayload> unsupportedFaces
) {
}

record RepresentationBuildResult(
        RepresentationPayload payload,
        List<UnsupportedFacePayload> unsupportedFaces
) {
}

record PreviewStats(
        int entityCount,
        int solidCount,
        int shellCount,
        int faceCount,
        int edgeCount,
        int unsupportedFaceCount,
        int unsupportedBooleanCount
) {
}

record BoundsPayload(PointPayload min, PointPayload max) {
}

record ValidationPayload(
        int representationCount,
        int instanceCount,
        int renderedFaceCount,
        int renderedEdgeCount,
        double approxSurfaceArea,
        double approxEdgeLength,
        PointPayload center,
        ValidationReportPayload report
) {
}

record ValidationReportPayload(
        String status,
        int okCount,
        int warnCount,
        List<ValidationCheckPayload> checks
) {
}

record ValidationCheckPayload(
        String propertyId,
        String name,
        String measureType,
        double expected,
        double actual,
        double delta,
        String status,
        boolean matches
) {
}

record ValidationContext(
        int representationCount,
        int instanceCount,
        PointPayload center,
        double sizeX,
        double sizeY,
        double sizeZ
) {
}

record PmiPayload(
        String name,
        String text,
        PointPayload position,
        List<PointPayload> leader,
        List<Integer> targetIds,
        List<PmiTargetPayload> targets
) {
}

record PmiTargetPayload(
        int id,
        String type,
        String name,
        List<String> instanceIds,
        String viaRelationshipType,
        Integer viaRelationshipId,
        String viaUsageType,
        Integer viaUsageId,
        String viaDefinitionType,
        Integer viaDefinitionId
) {
}

record RepresentationPayload(
        int id,
        String name,
        List<String> layers,
        ColorPayload color,
        List<EdgePayload> edges,
        List<FacePayload> faces
) {
}

record InstancePayload(
        String id,
        String parentId,
        int productDefinitionId,
        Integer occurrenceId,
        Integer representationId,
        List<Integer> representationIds,
        String label,
        String description,
        double[] localMatrix,
        double[] worldMatrix,
        int depth
) {
}

record BinaryPreviewPayload(
        PreviewStats stats,
        BoundsPayload bounds,
        ValidationPayload validation,
        ProductMetadataExtractor.ProductMetadata product,
        UnitExtractor.UnitInfo units,
        List<PmiPayload> pmi,
        List<UnsupportedBooleanPayload> unsupportedBooleans,
        List<UnsupportedFacePayload> unsupportedFaces,
        List<BinaryEdgePayload> edges,
        List<BinaryFacePayload> faces,
        List<BinaryRepresentationPayload> representations,
        List<InstancePayload> instances
) {
}

record BinaryRepresentationPayload(
        int id,
        String name,
        List<String> layers,
        ColorPayload color,
        List<BinaryEdgePayload> edges,
        List<BinaryFacePayload> faces
) {
}

record BinaryEdgePayload(int stepId, int pointOffset, int pointCount, EdgeCurvePayload curve, ColorPayload color) {
}

record BinaryFacePayload(
        int stepId,
        String name,
        String surfaceType,
        PointPayload origin,
        VectorPayload normal,
        boolean sameSense,
        ColorPayload color,
        List<String> layers,
        FaceSurfacePayload surface,
        List<ParametricLoopPayload> uvLoops,
        List<BinaryLoopPayload> loops,
        int triangleOffset,
        int triangleCount
) {
}

record BinaryLoopPayload(boolean outer, int pointOffset, int pointCount) {
}

record PointRange(int offset, int count) {
}

record UvPoint(double u, double v) {
}

record ParametricLoopPayload(boolean outer, List<UvPoint> points) {
}

record UvBounds(double minU, double minV, double maxU, double maxV) {
    double uSpan() {
        return maxU - minU;
    }

    double vSpan() {
        return maxV - minV;
    }
}

record EdgePayload(int stepId, List<PointPayload> points, EdgeCurvePayload curve, ColorPayload color) {
    EdgePayload(int stepId, List<PointPayload> points, EdgeCurvePayload curve) {
        this(stepId, points, curve, null);
    }
}

record EdgeCurvePayload(
        int stepId,
        String type,
        String basisType,
        Integer basisStepId,
        List<Double> center,
        List<Double> axis,
        List<Double> xDirection,
        Double radius,
        Double semiAxis1,
        Double semiAxis2,
        Boolean orientation,
        Boolean senseAgreement,
        Double offsetDistance,
        Boolean selfIntersect,
        List<Double> refDirection,
        Double transformScale,
        String masterRepresentation,
        List<String> associatedSurfaceTypes,
        List<Integer> associatedSurfaceStepIds,
        String sourceType,
        Integer sourceStepId,
        double startAngle,
        double sweepAngle
) {
    EdgeCurvePayload(
            int stepId,
            String type,
            String basisType,
            Integer basisStepId,
            List<Double> center,
            List<Double> axis,
            List<Double> xDirection,
            Double radius,
            Double semiAxis1,
            Double semiAxis2,
            double startAngle,
            double sweepAngle
    ) {
        this(stepId, type, basisType, basisStepId, center, axis, xDirection, radius, semiAxis1, semiAxis2,
                null, null, null, null, null, null, null, null, null, null, null, startAngle, sweepAngle);
    }
}

record FaceSurfacePayload(
        String type,
        List<Double> center,
        List<Double> axis,
        List<Double> xDirection,
        double radius,
        Double minorRadius,
        Double semiAngle,
        double lowerHeight,
        double upperHeight,
        double startAngle,
        double sweepAngle,
        Integer uDegree,
        Integer vDegree,
        List<List<List<Double>>> controlPoints,
        List<Integer> uMultiplicities,
        List<Integer> vMultiplicities,
        List<Double> uKnots,
        List<Double> vKnots,
        String sourceType,
        Integer sourceStepId,
        String basisType,
        Integer basisStepId,
        Boolean orientation,
        Double offsetDistance,
        Double trimU1,
        Double trimU2,
        Double trimV1,
        Double trimV2,
        Boolean implicitOuter,
        Double transformScale
) {
    FaceSurfacePayload(
            String type,
            List<Double> center,
            List<Double> axis,
            List<Double> xDirection,
            double radius,
            Double minorRadius,
            Double semiAngle,
            double lowerHeight,
            double upperHeight,
            double startAngle,
            double sweepAngle,
            Integer uDegree,
            Integer vDegree,
            List<List<List<Double>>> controlPoints,
            List<Integer> uMultiplicities,
            List<Integer> vMultiplicities,
            List<Double> uKnots,
            List<Double> vKnots
    ) {
        this(type, center, axis, xDirection, radius, minorRadius, semiAngle, lowerHeight, upperHeight, startAngle, sweepAngle,
                uDegree, vDegree, controlPoints, uMultiplicities, vMultiplicities, uKnots, vKnots,
                null, null, null, null, null, null, null, null, null, null, null, null);
    }
}

record FacePayload(
        int stepId,
        String name,
        String surfaceType,
        PointPayload origin,
        VectorPayload normal,
        boolean sameSense,
        ColorPayload color,
        double transparency,
        PbrPayload pbr,
        List<String> layers,
        List<LoopPayload> loops,
        List<PointPayload> triangles,
        FaceSurfacePayload surface,
        List<ParametricLoopPayload> uvLoops
) {
}

record UnsupportedFacePayload(
        int stepId,
        String name,
        String surfaceType,
        String reason
) {
}

record UnsupportedBooleanPayload(
        int stepId,
        String name,
        String type,
        String reason
) {
}

record PreviewFaceResult(FacePayload face, UnsupportedFacePayload unsupportedFace) {
}

record SurfacePatch(
        List<CartesianPoint> bottom,
        List<CartesianPoint> top,
        List<CartesianPoint> left,
        List<CartesianPoint> right
) {

    int uSegments() {
        return bottom.size() - 1;
    }

    int vSegments() {
        return left.size() - 1;
    }

    CartesianPoint pointAt(double u, double v) {
        CartesianPoint c0 = sample(bottom, u);
        CartesianPoint c1 = sample(top, u);
        CartesianPoint d0 = sample(left, v);
        CartesianPoint d1 = sample(right, v);
        CartesianPoint p00 = bottom.getFirst();
        CartesianPoint p10 = bottom.getLast();
        CartesianPoint p01 = top.getFirst();
        CartesianPoint p11 = top.getLast();
        return bilinearBlend(c0, c1, d0, d1, p00, p10, p01, p11, u, v);
    }

    Vector3 normalAt(double u, double v) {
        double du = Math.max(1.0 / Math.max(uSegments(), 1), 1.0e-3);
        double dv = Math.max(1.0 / Math.max(vSegments(), 1), 1.0e-3);
        CartesianPoint p = pointAt(u, v);
        CartesianPoint pu = pointAt(Math.min(1.0, u + du), v);
        CartesianPoint pv = pointAt(u, Math.min(1.0, v + dv));
        Vector3 normal = pu.subtract(p).cross(pv.subtract(p));
        if (normal.norm() <= Epsilon.EPS) {
            return new Vector3(0.0, 0.0, 1.0);
        }
        return normal.normalize().asVector();
    }

    private static CartesianPoint sample(List<CartesianPoint> polyline, double t) {
        double clamped = Math.max(0.0, Math.min(1.0, t));
        double scaled = clamped * (polyline.size() - 1);
        int low = Math.min((int) Math.floor(scaled), polyline.size() - 1);
        int high = Math.min(low + 1, polyline.size() - 1);
        double alpha = scaled - low;
        return interpolate(polyline.get(low), polyline.get(high), alpha);
    }

    private static CartesianPoint bilinearBlend(
            CartesianPoint c0,
            CartesianPoint c1,
            CartesianPoint d0,
            CartesianPoint d1,
            CartesianPoint p00,
            CartesianPoint p10,
            CartesianPoint p01,
            CartesianPoint p11,
            double u,
            double v
    ) {
        double x = (1.0 - v) * c0.x() + v * c1.x() + (1.0 - u) * d0.x() + u * d1.x()
                - ((1.0 - u) * (1.0 - v) * p00.x() + u * (1.0 - v) * p10.x()
                + (1.0 - u) * v * p01.x() + u * v * p11.x());
        double y = (1.0 - v) * c0.y() + v * c1.y() + (1.0 - u) * d0.y() + u * d1.y()
                - ((1.0 - u) * (1.0 - v) * p00.y() + u * (1.0 - v) * p10.y()
                + (1.0 - u) * v * p01.y() + u * v * p11.y());
        double z = (1.0 - v) * c0.z() + v * c1.z() + (1.0 - u) * d0.z() + u * d1.z()
                - ((1.0 - u) * (1.0 - v) * p00.z() + u * (1.0 - v) * p10.z()
                + (1.0 - u) * v * p01.z() + u * v * p11.z());
        return new CartesianPoint(x, y, z);
    }

    private static CartesianPoint interpolate(CartesianPoint a, CartesianPoint b, double alpha) {
        return new CartesianPoint(
                a.x() + (b.x() - a.x()) * alpha,
                a.y() + (b.y() - a.y()) * alpha,
                a.z() + (b.z() - a.z()) * alpha
        );
    }
}

record LoopPayload(boolean outer, List<PointPayload> points) {
}

record PointPayload(double x, double y, double z) {
}

record VectorPayload(double x, double y, double z) {
}

record ColorPayload(int red, int green, int blue) {
}

record PbrPayload(double diffuse, double specular, Double specularExponent, int[] specularColor) {
}

record GeometrySummary(int faceCount, int edgeCount, double approxSurfaceArea, double approxEdgeLength) {
}

record RepresentationMeshes(String name, List<FaceNode> faces, List<EdgeNode> edges) {
}

record FaceNode(FacePayload face, int meshIndex, String name) {
}

record EdgeNode(EdgePayload edge, int meshIndex, String name) {
}

record FloatArrayData(float[] values, int count, float[] min, float[] max) {
}

record IntArrayData(int[] values, int count) {
}

record IndexedTriangleMesh(FloatArrayData positions, FloatArrayData normals, IntArrayData indices) {
}
