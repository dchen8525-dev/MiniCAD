package com.minicad.preview.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minicad.common.Epsilon;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.*;
import com.minicad.geometry2d.*;
import com.minicad.helper.MathUtilityHelper;
import com.minicad.preview.payload.FaceSurfacePayload;
import com.minicad.preview.payload.ParametricLoopPayload;
import com.minicad.preview.payload.UvBounds;
import com.minicad.preview.payload.UvPoint;
import com.minicad.preview.payload.VectorPayload;
import com.minicad.preview.sampling.PreviewCurveEvaluator;
import com.minicad.preview.sampling.PreviewPcurveSampler;
import com.minicad.export.json.StepPreviewJsonExporter;
import com.minicad.step.model.StepAnnotationCurveOccurrence;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.*;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.*;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.FaceBound;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.VertexLoop;
import com.minicad.step.semantic.StepCadBuilder;

import java.util.*;
import com.minicad.export.json.StepEdgePayloadBuilder;
import com.minicad.export.json.StepPointExtractor;
import com.minicad.export.json.StepTypeNameResolver;

/** UV mapping, parametric surface projection, and pcurve sampling.
 *  Extracted from StepPreviewJsonExporter to isolate UV mapping logic. */
public final class PreviewUvMapper {

    private static final Logger log = LoggerFactory.getLogger(PreviewUvMapper.class);

    private PreviewUvMapper() {}

    // ─── mapperForSurface ─────────────────────────────────────────────────

    public static ParametricSurfaceMapper mapperForSurface(StepEntity geometry, StepCadBuilder builder) {
        return SurfaceMapperHelper.mapperForSurface(geometry, builder);
    }

    // ─── extrusionMapper / revolutionMapper ───────────────────────────────

    public static ParametricSurfaceMapper extrusionMapper(
            StepSurfaceOfLinearExtrusion extrusionSurface,
            StepCadBuilder builder
    ) {
        return SurfaceMapperHelper.extrusionMapper(extrusionSurface, builder);
    }

    public static ParametricSurfaceMapper revolutionMapper(
            StepSurfaceOfRevolution revolutionSurface,
            StepCadBuilder builder
    ) {
        return SurfaceMapperHelper.revolutionMapper(revolutionSurface, builder);
    }

    // ─── nearestUvOnBSplineSurface / nearestUvOnRationalBSplineSurface ────

    public static UvPoint nearestUvOnBSplineSurface(BSplineSurface3 surface, CartesianPoint point, UvPoint previous) {
        return SurfaceMapperHelper.nearestUvOnBSplineSurface(surface, point, previous);
    }

    public static UvPoint nearestUvOnRationalBSplineSurface(
            RationalBSplineSurface3 surface,
            CartesianPoint point,
            UvPoint previous
    ) {
        return SurfaceMapperHelper.nearestUvOnRationalBSplineSurface(surface, point, previous);
    }

    // ─── inverseUniformScaleTransform ─────────────────────────────────────

    public static double[] inverseUniformScaleTransform(double[] matrix) {
        return MathUtilityHelper.inverseUniformScaleTransform(matrix);
    }

    // ─── clamp ────────────────────────────────────────────────────────────

    public static double clamp(double value, double min, double max) {
        return MathUtilityHelper.clamp(value, min, max);
    }

    // ─── buildParametricLoops ─────────────────────────────────────────────

    public static List<ParametricLoopPayload> buildParametricLoops(List<FaceBound> bounds, ParametricSurfaceMapper mapper) {
        List<ParametricLoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            if (bound.loop() instanceof VertexLoop) {
                return List.of();
            }
            List<CartesianPoint> points3d = sampleLoop(bound);
            if (points3d.size() < 4) {
                return List.of();
            }
            List<UvPoint> uvPoints = new ArrayList<>(points3d.size());
            UvPoint previous = null;
            for (CartesianPoint point : points3d) {
                UvPoint uv = mapper.project(point, previous);
                if (uv == null) {
                    return List.of();
                }
                uvPoints.add(uv);
                previous = uv;
            }
            uvPoints = normalizePeriodicLoop(uvPoints, mapper);
            uvPoints.set(0, uvPoints.get(0));
            uvPoints.set(uvPoints.size() - 1, uvPoints.get(0));
            loops.add(new ParametricLoopPayload(bound.outer(), List.copyOf(uvPoints)));
        }
        return List.copyOf(loops);
    }

    public static List<ParametricLoopPayload> buildParametricLoops(
            StepFaceEntity stepFace,
            StepEntity geometry,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        List<ParametricLoopPayload> loops = new ArrayList<>();
        boolean promoteSingleOuter = stepFace.bounds().size() == 1
                && stepFace.bounds().stream().noneMatch(com.minicad.step.model.StepFaceBound::outer);
        for (com.minicad.step.model.StepFaceBound bound : stepFace.bounds()) {
            if (!(bound.loop() instanceof com.minicad.step.model.StepEdgeLoop)) {
                return List.of();
            }
            com.minicad.step.model.StepEdgeLoop edgeLoop = (com.minicad.step.model.StepEdgeLoop) bound.loop();
            List<UvPoint> loopPoints = new ArrayList<>();
            boolean firstEdge = true;
            for (com.minicad.step.model.StepOrientedEdge orientedEdge : edgeLoop.edges()) {
                List<UvPoint> edgePoints = sampleParametricOrientedEdge(orientedEdge, geometry, mapper, builder);
                if (edgePoints == null || edgePoints.size() < 2) {
                    return List.of();
                }
                int startIndex = firstEdge ? 0 : 1;
                for (int index = startIndex; index < edgePoints.size(); index++) {
                    loopPoints.add(edgePoints.get(index));
                }
                firstEdge = false;
            }
            if (loopPoints.size() < 4) {
                return List.of();
            }
            if (!bound.orientation()) {
                loopPoints = reverseClosedLoop(loopPoints);
            }
            loopPoints = normalizePeriodicLoop(loopPoints, mapper);
            if (!PreviewPcurveSampler.sameUv(loopPoints.get(0), loopPoints.get(loopPoints.size() - 1))) {
                loopPoints.add(loopPoints.get(0));
            }
            loops.add(new ParametricLoopPayload(bound.outer() || promoteSingleOuter, List.copyOf(loopPoints)));
        }
        return List.copyOf(loops);
    }

    // ─── normalizePeriodicLoop ────────────────────────────────────────────

    public static List<UvPoint> normalizePeriodicLoop(List<UvPoint> points, ParametricSurfaceMapper mapper) {
        if (points.size() < 2) {
            return points;
        }
        Double uPeriod = mapper.uPeriod();
        Double vPeriod = mapper.vPeriod();
        List<UvPoint> normalized = new ArrayList<>(points.size());
        UvPoint previous = null;
        for (UvPoint point : points) {
            double u = point.u();
            double v = point.v();
            if (previous != null) {
                if (uPeriod != null) {
                    u = unwrapPeriodic(u, previous.u(), uPeriod);
                }
                if (vPeriod != null) {
                    v = unwrapPeriodic(v, previous.v(), vPeriod);
                }
            }
            UvPoint normalizedPoint = new UvPoint(u, v);
            normalized.add(normalizedPoint);
            previous = normalizedPoint;
        }
        if (normalized.size() >= 2) {
            UvPoint first = normalized.get(0);
            UvPoint last = normalized.get(normalized.size() - 1);
            double u = last.u();
            double v = last.v();
            if (uPeriod != null) {
                u = unwrapPeriodic(u, first.u(), uPeriod);
            }
            if (vPeriod != null) {
                v = unwrapPeriodic(v, first.v(), vPeriod);
            }
            normalized.set(normalized.size() - 1, new UvPoint(u, v));
        }
        return normalized;
    }

    // ─── boundsOf ─────────────────────────────────────────────────────────

    public static UvBounds boundsOf(List<ParametricLoopPayload> loops) {
        double minU = Double.POSITIVE_INFINITY;
        double minV = Double.POSITIVE_INFINITY;
        double maxU = Double.NEGATIVE_INFINITY;
        double maxV = Double.NEGATIVE_INFINITY;
        for (ParametricLoopPayload loop : loops) {
            for (UvPoint point : loop.points()) {
                minU = Math.min(minU, point.u());
                minV = Math.min(minV, point.v());
                maxU = Math.max(maxU, point.u());
                maxV = Math.max(maxV, point.v());
            }
        }
        if (!Double.isFinite(minU) || !Double.isFinite(minV) || !Double.isFinite(maxU) || !Double.isFinite(maxV)) {
            return null;
        }
        return new UvBounds(minU, minV, maxU, maxV);
    }

    // ─── withSurfaceSourceMetadata ────────────────────────────────────────

    public static FaceSurfacePayload withSurfaceSourceMetadata(FaceSurfacePayload base, StepEntity geometry) {
        if (base == null || geometry == null) {
            return base;
        }
        String basisType = null;
        Integer basisStepId = null;
        Boolean orientation = null;
        Double offsetDistance = null;
        Double trimU1 = null;
        Double trimU2 = null;
        Double trimV1 = null;
        Double trimV2 = null;
        Boolean implicitOuter = null;
        Double transformScale = null;

        if (geometry instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) geometry;
            basisType = StepTypeNameResolver.surfaceTypeName(trimmedSurface.basisSurface());
            basisStepId = trimmedSurface.basisSurface().id();
            trimU1 = trimmedSurface.u1();
            trimU2 = trimmedSurface.u2();
            trimV1 = trimmedSurface.v1();
            trimV2 = trimmedSurface.v2();
        } else if (geometry instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) geometry;
            basisType = StepTypeNameResolver.surfaceTypeName(boundedSurface.basisSurface());
            basisStepId = boundedSurface.basisSurface().id();
            implicitOuter = boundedSurface.implicitOuter();
        } else if (geometry instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) geometry;
            basisType = StepTypeNameResolver.surfaceTypeName(orientedSurface.surfaceElement());
            basisStepId = orientedSurface.surfaceElement().id();
            orientation = orientedSurface.orientation();
        } else if (geometry instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) geometry;
            basisType = StepTypeNameResolver.surfaceTypeName(offsetSurface.basisSurface());
            basisStepId = offsetSurface.basisSurface().id();
            offsetDistance = offsetSurface.distance();
        } else if (geometry instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) geometry).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) geometry;
            basisType = StepTypeNameResolver.surfaceTypeName(replica.parent());
            basisStepId = replica.parent().id();
            transformScale = replica.transformation().scale();
        }

        return new FaceSurfacePayload(
                base.type(),
                base.center(),
                base.axis(),
                base.xDirection(),
                base.radius(),
                base.minorRadius(),
                base.semiAngle(),
                base.lowerHeight(),
                base.upperHeight(),
                base.startAngle(),
                base.sweepAngle(),
                base.uDegree(),
                base.vDegree(),
                base.controlPoints(),
                base.uMultiplicities(),
                base.vMultiplicities(),
                base.uKnots(),
                base.vKnots(),
                StepTypeNameResolver.surfaceTypeName(geometry),
                geometry.id(),
                basisType,
                basisStepId,
                orientation,
                offsetDistance,
                trimU1,
                trimU2,
                trimV1,
                trimV2,
                implicitOuter,
                transformScale
        );
    }

    // ─── basisDirectionForNormal ──────────────────────────────────────────

    public static List<Double> basisDirectionForNormal(Direction3 normal) {
        Vector3 axis = normal.asVector();
        Vector3 reference = Math.abs(axis.x()) < 0.9
                ? new Vector3(1.0, 0.0, 0.0)
                : new Vector3(0.0, 1.0, 0.0);
        Direction3 xDirection = reference.subtract(axis.scale(reference.dot(axis))).normalize().asDirection();
        return List.of(xDirection.x(), xDirection.y(), xDirection.z());
    }

    // ─── sampleParametricOrientedEdge ─────────────────────────────────────

    private static List<UvPoint> sampleParametricOrientedEdge(
            com.minicad.step.model.StepOrientedEdge orientedEdge,
            StepEntity faceGeometry,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        StepVertexPoint startVertex = orientedEdge.orientation()
                ? orientedEdge.edgeElement().start()
                : orientedEdge.edgeElement().end();
        StepVertexPoint endVertex = orientedEdge.orientation()
                ? orientedEdge.edgeElement().end()
                : orientedEdge.edgeElement().start();
        StepEntity edgeGeometry = orientedEdge.edgeElement().edgeGeometry();
        StepEntity associatedSource = PcurveMatcher.unwrapAssociatedCurveGeometry(edgeGeometry);
        List<StepEntity> pcurves = List.of();  // Default: no pcurves
        if (pcurves.isEmpty()) {
            if (PcurveMatcher.shouldFallbackToProjectedEdge(edgeGeometry)) {
                List<UvPoint> fallback = projectSampledEdge(orientedEdge, mapper, builder);
                if (fallback != null) {
                    return fallback;
                }
            }
            return null;
        }
        UvPoint projectedStart = mapper.project(pointFromStep(startVertex.point()), null);
        UvPoint projectedEnd = mapper.project(pointFromStep(endVertex.point()), projectedStart);
        List<UvPoint> best = null;
        double bestScore = Double.POSITIVE_INFINITY;
        int unsupportedPcurveCount = 0;
        for (StepEntity pcurve : pcurves) {
            Object built;
            try {
                built = builder.buildPcurve2(pcurve.id());
            } catch (UnsupportedGeometryException ex) {
                unsupportedPcurveCount++;
                continue;
            }
            if (built instanceof Line2) {
            Line2 line = (Line2) built;
                UvPoint start = PreviewPcurveSampler.snapToLine(projectedStart, line);
                UvPoint end = PreviewPcurveSampler.snapToLine(projectedEnd, line);
                double score = PreviewPcurveSampler.distanceSquared(projectedStart, start) + PreviewPcurveSampler.distanceSquared(projectedEnd, end);
                List<UvPoint> samples = PreviewPcurveSampler.sampleLinePcurve(line, start, end);
                if (best == null || score < bestScore) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof BSplineCurve2) {
            BSplineCurve2 spline = (BSplineCurve2) built;
                List<UvPoint> samples = PreviewPcurveSampler.sampleSplinePcurve(spline, projectedStart, projectedEnd);
                if (!samples.isEmpty()) {
                    double score = PreviewPcurveSampler.distanceSquared(projectedStart, samples.get(0)) + PreviewPcurveSampler.distanceSquared(projectedEnd, samples.get(samples.size() - 1));
                    if (best == null || score < bestScore) {
                        best = samples;
                        bestScore = score;
                    }
                }
                continue;
            }
            if (built instanceof Circle2) {
            Circle2 circle = (Circle2) built;
                UvPoint start = PreviewPcurveSampler.snapToCircle(projectedStart, circle);
                UvPoint end = PreviewPcurveSampler.snapToCircle(projectedEnd, circle);
                double score = PreviewPcurveSampler.distanceSquared(projectedStart, start) + PreviewPcurveSampler.distanceSquared(projectedEnd, end);
                List<UvPoint> samples = PreviewPcurveSampler.sampleCirclePcurve(circle, start, end);
                if (!samples.isEmpty() && (best == null || score < bestScore)) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof Ellipse2) {
            Ellipse2 ellipse = (Ellipse2) built;
                UvPoint start = PreviewPcurveSampler.snapToEllipse(projectedStart, ellipse);
                UvPoint end = PreviewPcurveSampler.snapToEllipse(projectedEnd, ellipse);
                double score = PreviewPcurveSampler.distanceSquared(projectedStart, start) + PreviewPcurveSampler.distanceSquared(projectedEnd, end);
                List<UvPoint> samples = PreviewPcurveSampler.sampleEllipsePcurve(ellipse, start, end);
                if (!samples.isEmpty() && (best == null || score < bestScore)) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof TrimmedCurve2) {
            TrimmedCurve2 trimmed = (TrimmedCurve2) built;
                List<UvPoint> samples = PreviewPcurveSampler.sampleTrimmedPcurve(trimmed, projectedStart, projectedEnd);
                if (!samples.isEmpty()) {
                    double score = PreviewPcurveSampler.distanceSquared(projectedStart, samples.get(0)) + PreviewPcurveSampler.distanceSquared(projectedEnd, samples.get(samples.size() - 1));
                    if (best == null || score < bestScore) {
                        best = samples;
                        bestScore = score;
                    }
                }
            }
        }
        if (best == null) {
            List<UvPoint> fallback = projectSampledEdge(orientedEdge, mapper, builder);
            if (fallback != null) {
                return fallback;
            }
        }
        return best;
    }

    // ─── projectSampledEdge ───────────────────────────────────────────────

    private static List<UvPoint> projectSampledEdge(
            com.minicad.step.model.StepOrientedEdge orientedEdge,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> sampled = sampleStepOrientedEdge(orientedEdge, builder);
        if (sampled.size() < 2) {
            return null;
        }
        List<UvPoint> points = new ArrayList<>(sampled.size());
        UvPoint previous = null;
        for (CartesianPoint point : sampled) {
            UvPoint uv = mapper.project(point, previous);
            if (uv == null) {
                return null;
            }
            points.add(uv);
            previous = uv;
        }
        return List.copyOf(points);
    }

    // ─── sampleStepOrientedEdge ───────────────────────────────────────────

    private static List<CartesianPoint> sampleStepOrientedEdge(
            com.minicad.step.model.StepOrientedEdge orientedEdge,
            StepCadBuilder builder
    ) {
        com.minicad.step.model.StepEdgeCurve edge = orientedEdge.edgeElement();
        CartesianPoint start = pointFromStep(orientedEdge.orientation() ? edge.start().point() : edge.end().point());
        CartesianPoint end = pointFromStep(orientedEdge.orientation() ? edge.end().point() : edge.start().point());
        boolean naturalForward = orientedEdge.orientation() ? edge.sameSense() : !edge.sameSense();
        Curve3 curve = curveForLooseEdge(edge.edgeGeometry(), builder);
        if (curve == null) {
            return List.of();
        }
        try {
            return sampleEdge(start, end, curve, naturalForward);
        } catch (com.minicad.common.GeometryException ex) {
            return List.of(start, end);
        }
    }

    // ─── shouldFallbackToProjectedEdge / associatedGeometrySummary ────────

    public static boolean shouldFallbackToProjectedEdge(StepEntity edgeGeometry) {
        StepEntity unwrapped = unwrapAssociatedCurveGeometry(edgeGeometry);
        if (unwrapped instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) unwrapped;
            return surfaceCurve.associatedGeometry().isEmpty();
        } else if (unwrapped instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) unwrapped;
            return seamCurve.associatedGeometry().isEmpty();
        } else {
            return true;
        }
    }

    public static String associatedGeometrySummary(StepEntity edgeGeometry) {
        StepEntity unwrapped = unwrapAssociatedCurveGeometry(edgeGeometry);
        List<StepEntity> associated;
        if (unwrapped instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) unwrapped;
            associated = surfaceCurve.associatedGeometry();
        } else if (unwrapped instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) unwrapped;
            associated = seamCurve.associatedGeometry();
        } else {
            associated = List.of();
        }
        if (associated.isEmpty()) {
            return "[]";
        }
        return associated.stream()
                .map(entity -> StepTypeNameResolver.surfaceTypeName(entity) + "#" + entity.id())
                .collect(java.util.stream.Collectors.joining("|"));
    }

    // ─── unwrapAssociatedCurveGeometry ────────────────────────────────────

    public static StepEntity unwrapAssociatedCurveGeometry(StepEntity edgeGeometry) {
        StepEntity current = edgeGeometry;
        for (int depth = 0; depth < 16; depth++) {
            if (current instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) current;
                current = orientedCurve.curveElement();
                continue;
            }
            if (current instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) current).entityName())) {
                StepGeometricReplica replica = (StepGeometricReplica) current;
                current = replica.parent();
                continue;
            }
            if (current instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) current;
                current = occurrence.item();
                continue;
            }
            if (current instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) current;
                current = dimensionCurve.item();
                continue;
            }
            if (current instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) current;
                current = leaderCurve.item();
                continue;
            }
            if (current instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) current;
                current = projectionCurve.item();
                continue;
            }
            if (current instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) current;
                current = annotationOccurrence.item();
                continue;
            }
            if (current instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) current;
                current = terminatorSymbol.annotatedCurve();
                continue;
            }
            return current;
        }
        return current;
    }

    // ─── pcurveBasisSurfaceSummary ────────────────────────────────────────

    public static String pcurveBasisSurfaceSummary(List<StepEntity> pcurves) {
        return pcurves.stream()
                .map(pcurve -> {
                    if (pcurve instanceof StepPcurve) {
            StepPcurve exact = (StepPcurve) pcurve;
                        return "#" + exact.id() + "->#" + exact.basisSurface().id();
                    }
                    if (pcurve instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve degenerate = (StepDegeneratePcurve) pcurve;
                        return "#" + degenerate.id() + "->#" + degenerate.basisSurface().id();
                    }
                    return "#" + pcurve.id();
                })
                .collect(java.util.stream.Collectors.joining("|"));
    }

    // ─── matchingPcurves ──────────────────────────────────────────────────

    public static List<StepEntity> matchingPcurves(List<StepEntity> associatedGeometry, StepEntity faceGeometry) {
        Set<Integer> acceptableSurfaceIds = acceptablePcurveBasisSurfaceIds(faceGeometry);
        List<StepEntity> matches = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve && acceptableSurfaceIds.contains(((StepPcurve) associated).basisSurface().id())) {
                StepPcurve pcurve = (StepPcurve) associated;
                matches.add(pcurve);
            } else if (associated instanceof StepDegeneratePcurve && acceptableSurfaceIds.contains(((StepDegeneratePcurve) associated).basisSurface().id())) {
                StepDegeneratePcurve pcurve = (StepDegeneratePcurve) associated;
                matches.add(pcurve);
            }
        }
        return List.copyOf(matches);
    }

    // ─── acceptablePcurveBasisSurfaceIds ──────────────────────────────────

    public static Set<Integer> acceptablePcurveBasisSurfaceIds(StepEntity faceGeometry) {
        LinkedHashSet<Integer> ids = new LinkedHashSet<>();
        StepEntity current = faceGeometry;
        for (int depth = 0; depth < 16 && current != null; depth++) {
            ids.add(current.id());
            if (current instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) current;
                current = trimmedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) current;
                current = boundedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) current;
                current = orientedSurface.surfaceElement();
                continue;
            }
            if (current instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) current;
                current = offsetSurface.basisSurface();
                continue;
            }
            if (current instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) current).entityName())) {
                StepGeometricReplica replica = (StepGeometricReplica) current;
                current = replica.parent();
                continue;
            }
            break;
        }
        return Set.copyOf(ids);
    }

    // ─── Private helper methods (copied from StepPreviewJsonExporter) ─────

    private static double unwrapPeriodic(double value, Double previous, double period) {
        return MathUtilityHelper.unwrapPeriodic(value, previous, period);
    }

    private static CartesianPoint transformCartesian(CartesianPoint point, double[] matrix) {
        return MathUtilityHelper.transformCartesian(point, matrix);
    }

    private static VectorPayload transform(VectorPayload vector, double[] matrix) {
        return MathUtilityHelper.transform(vector, matrix);
    }

    private static List<CartesianPoint> sampleLoop(FaceBound bound) {
        if (bound.loop() instanceof VertexLoop) {
            VertexLoop vertexLoop = (VertexLoop) bound.loop();
            return List.of(vertexLoop.vertex().point());
        }
        if (bound.loop() instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) bound.loop();
            List<CartesianPoint> sampled = new ArrayList<>(polyLoop.points());
            if (!sampled.isEmpty() && sampled.get(0).distanceTo(sampled.get(sampled.size() - 1)) > 1.0e-9) {
                sampled.add(sampled.get(0));
            }
            return bound.orientation() ? sampled : reverseClosedLoop(sampled);
        }
        if (!(bound.loop() instanceof EdgeLoop)) {
            throw new com.minicad.common.UnsupportedGeometryException("preview export requires EDGE_LOOP, POLY_LOOP or VERTEX_LOOP");
        }
        EdgeLoop edgeLoop = (EdgeLoop) bound.loop();
        List<CartesianPoint> sampled = new ArrayList<>();
        boolean firstEdge = true;
        for (OrientedEdge orientedEdge : edgeLoop.edges()) {
            List<CartesianPoint> edgePoints = StepEdgePayloadBuilder.sampleOrientedEdge(orientedEdge);
            int startIndex = firstEdge ? 0 : 1;
            for (int i = startIndex; i < edgePoints.size(); i++) {
                sampled.add(edgePoints.get(i));
            }
            firstEdge = false;
        }
        if (!sampled.isEmpty() && sampled.get(0).distanceTo(sampled.get(sampled.size() - 1)) > 1.0e-9) {
            sampled.add(sampled.get(0));
        }
        return bound.orientation() ? sampled : reverseClosedLoop(sampled);
    }

    private static <T> List<T> reverseClosedLoop(List<T> points) {
        if (points.size() < 2) {
            return points;
        }
        List<T> reversed = new ArrayList<>(points);
        if (reversed.get(0).equals(reversed.get(reversed.size() - 1))) {
            T start = reversed.remove(reversed.size() - 1);
            java.util.Collections.reverse(reversed);
            reversed.add(reversed.get(0));
            reversed.set(0, start);
            reversed.set(reversed.size() - 1, start);
            return reversed;
        }
        java.util.Collections.reverse(reversed);
        return reversed;
    }

    private static CartesianPoint pointFromStep(StepCartesianPoint point) {
        return StepPointExtractor.pointFromStep(point);
    }

    private static Curve3 curveForLooseEdge(StepEntity item, StepCadBuilder builder) {
        try {
            if (item instanceof StepLine) {
            StepLine line = (StepLine) item;
                return builder.buildLine(line.id());
            }
            if (item instanceof StepCircle) {
            StepCircle circle = (StepCircle) item;
                return builder.buildCircle(circle.id());
            }
            if (item instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) item;
                return builder.buildEllipse(ellipse.id());
            }
            if (item instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) item;
                return builder.buildPolyline(polyline.id());
            }
            if (item instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots spline = (StepBSplineSurfaceWithKnots) item;
                return builder.buildBSplineCurve(spline.id());
            }
            if (item instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) item;
                return builder.buildTrimmedCurve(trimmedCurve.id());
            }
            if (item instanceof com.minicad.step.model.StepCompositeCurve) {
                com.minicad.step.model.StepCompositeCurve compositeCurve = (com.minicad.step.model.StepCompositeCurve) item;
                return builder.buildCompositeCurve(compositeCurve.id());
            }
            return null;
        } catch (Exception ex) {
            // Recoverable degradation: edge build failed, signal and return null.
            log.warn("PreviewUvMapper edge build failed; returning null", ex);
            return null;
        }
    }

    private static List<CartesianPoint> sampleEdge(CartesianPoint start, CartesianPoint end, Curve3 curve, boolean naturalForward) {
        if (curve instanceof Line3) {
            return List.of(start, end);
        }
        if (curve instanceof Polyline3) {
            Polyline3 polyline = (Polyline3) curve;
            List<CartesianPoint> points = new ArrayList<>(polyline.points());
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        List<CartesianPoint> points = new ArrayList<>(curve.sample(72));
        if (!naturalForward) {
            java.util.Collections.reverse(points);
        }
        if (!points.isEmpty()) {
            points.set(0, start);
            points.set(points.size() - 1, end);
        }
        return List.copyOf(points);
    }
}
