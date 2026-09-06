package com.minicad.export.json;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Point2;
import com.minicad.helper.MathUtilityHelper;
import com.minicad.helper.StepMetadataExtractor;
import com.minicad.preview.payload.ColorPayload;
import com.minicad.preview.payload.EdgeCurvePayload;
import com.minicad.preview.payload.EdgePayload;
import com.minicad.preview.payload.PayloadConversionHelper;
import com.minicad.preview.payload.SurfacePatch;
import com.minicad.preview.sampling.ConicSamplingHelper;
import com.minicad.preview.sampling.Curve2SamplingHelper;
import com.minicad.preview.sampling.Curve3SamplingHelper;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.model.StepBSplineCurve;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBezierCurve;
import com.minicad.step.model.StepBoundedCurve;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepClothoid;
import com.minicad.step.model.StepCompositeCurveOnSurface;
import com.minicad.step.model.StepConicCurve;
import com.minicad.step.model.StepDegenerateCurve;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepGeometricSet;
import com.minicad.step.model.StepIndexedPolyCurve;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepLineSegment;
import com.minicad.step.model.StepOrientedCurve;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepQuasiUniformCurve;
import com.minicad.step.model.StepRationalBSplineCurve;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepUniformCurve;
import com.minicad.step.model.StepPiecewiseBezierCurve;
import com.minicad.step.model.*;
import com.minicad.topology.Edge;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.VertexLoop;
import com.minicad.topology.FaceBound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.minicad.geometry.Clothoid3;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Hyperbola3;
import com.minicad.geometry.Parabola3;
import com.minicad.preview.payload.RepresentationBuildResult;

/**
 * Builds edge payloads from STEP edge entities.
 *
 * <p>Handles:</p>
 * <ul>
 *   <li>Edge sampling and point extraction</li>
 *   <li>Edge payload construction for various curve types</li>
 *   <li>Edge transformation for mapped items</li>
 * </ul>
 *
 * @since 1.0
 */
public final class StepEdgePayloadBuilder {
    private static final Logger log = LoggerFactory.getLogger(StepEdgePayloadBuilder.class);

    private StepEdgePayloadBuilder() {}

    // ================================================================================
    // PUBLIC API METHODS
    // ================================================================================

    /**
     * Samples an oriented edge to produce a list of 3D points.
     *
     * @param orientedEdge the oriented edge to sample
     * @return list of sampled points along the edge
     */
    public static List<CartesianPoint> sampleOrientedEdge(OrientedEdge orientedEdge) {
        Edge edge = orientedEdge.edge();
        boolean naturalForward = orientedEdge.orientation() ? edge.sameSense() : !edge.sameSense();
        return sampleEdge(orientedEdge.startVertex().point(), orientedEdge.endVertex().point(), edge.curve(), naturalForward);
    }

    /**
     * Builds an edge payload for a given edge ID.
     *
     * @param edgeId the STEP edge ID
     * @param resolved the resolved entity map
     * @param builder the CAD builder
     * @param metadata the metadata extractor
     * @return the edge payload
     */
    public static EdgePayload buildEdgePayload(
            int edgeId,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        List<CartesianPoint> polyline = sampleEdgePreview(edgeId, resolved, builder);
        StepEntity entity = resolved.get(edgeId);
        ColorPayload color = resolveEdgeColor(edgeId, metadata);
        if (entity instanceof StepEdgeCurve) {
            StepEdgeCurve edge = (StepEdgeCurve) entity;
            CartesianPoint start = StepPointExtractor.pointFromStep(edge.start().point());
            CartesianPoint end = StepPointExtractor.pointFromStep(edge.end().point());
            return new EdgePayload(
                    edgeId,
                    PayloadConversionHelper.toPointPayloads(polyline),
                    edgeCurvePayload(edge.edgeGeometry(), start, end, edge.sameSense(), builder),
                    color
            );
        }
        if (entity instanceof StepSeamEdge) {
            StepSeamEdge seamEdge = (StepSeamEdge) entity;
            // Seam edge: curve geometry is resolved at the same ID in entitiesById.
            StepEntity actual = resolved.get(seamEdge.id());
            if (actual != null && actual != seamEdge) {
                Edge edge = builder.buildEdge(edgeId);
                CartesianPoint start = edge.start().point();
                CartesianPoint end = edge.end().point();
                EdgeCurvePayload curvePayload = edgeCurvePayload(actual, start, end, true, builder);
                if (curvePayload != null) {
                    return new EdgePayload(edgeId, PayloadConversionHelper.toPointPayloads(polyline), curvePayload, color);
                }
            }
        }
        if (entity instanceof StepFilletEdge) {
            StepFilletEdge filletEdge = (StepFilletEdge) entity;
            // Fillet edge: sample the original edge geometry for preview.
            StepEntity original = filletEdge.originalEdge();
            if (original != null) {
                EdgeCurvePayload curvePayload = edgeCurvePayload(original, polyline.get(0), polyline.get(polyline.size() - 1), true, builder);
                if (curvePayload != null) {
                    return new EdgePayload(edgeId, PayloadConversionHelper.toPointPayloads(polyline), curvePayload, color);
                }
            }
        }
        if (entity instanceof StepChamferEdge) {
            StepChamferEdge chamferEdge = (StepChamferEdge) entity;
            // Chamfer edge: sample the original edge geometry for preview.
            StepEntity original = chamferEdge.originalEdge();
            if (original != null) {
                EdgeCurvePayload curvePayload = edgeCurvePayload(original, polyline.get(0), polyline.get(polyline.size() - 1), true, builder);
                if (curvePayload != null) {
                    return new EdgePayload(edgeId, PayloadConversionHelper.toPointPayloads(polyline), curvePayload, color);
                }
            }
        }
        return new EdgePayload(edgeId, PayloadConversionHelper.toPointPayloads(polyline), null, color);
    }

    /**
     * Creates an edge payload with sampled curve geometry.
     *
     * @param item the STEP entity
     * @param builder the CAD builder
     * @return the edge payload, or null if sampling fails
     */
    public static EdgePayload sampledCurveEdgePayload(StepEntity item, StepCadBuilder builder) {
        List<CartesianPoint> points = sampleLooseEdgePoints(item, builder);
        if (points == null || points.size() < 2) {
            return null;
        }
        return new EdgePayload(item.id(), PayloadConversionHelper.toPointPayloads(points), sampledCurvePayload(item, builder), null);
    }

    /**
     * Samples loose edge points from a STEP entity.
     *
     * @param item the STEP entity
     * @param builder the CAD builder
     * @return list of sampled points, or null if not applicable
     */
    public static List<CartesianPoint> sampleLooseEdgePoints(StepEntity item, StepCadBuilder builder) {
        if (item instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) item;
            return sampleAnnotationFillAreaPoints(fillArea, builder);
        }
        if (item instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) item;
            return sampleAnnotationFillAreaPoints(fillAreaOccurrence.item(), builder);
        }
        if (item instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) item;
            return sampleWireframeBoundaryPoints(wireframeModel.boundaries(), builder);
        }
        if (item instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) item;
            return sampleWireframeBoundaryPoints(wireframeModel.boundaries(), builder);
        }
        if (item instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) item;
            return sampleMappedAnnotationPoints(
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(),
                    builder
            );
        }
        if (item instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) item;
            return sampleMappedAnnotationPoints(
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(),
                    builder
            );
        }
        if (item instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) item;
            return sampleMappedAnnotationPoints(
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(),
                    builder
            );
        }
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            List<CartesianPoint> parentPoints = sampleLooseEdgePoints(replica.parent(), builder);
            if (parentPoints == null) {
                return null;
            }
            List<CartesianPoint> transformed = new ArrayList<>(parentPoints.size());
            for (CartesianPoint point : parentPoints) {
                transformed.add(StepPointExtractor.transformPoint(point, replica.transformation(), builder));
            }
            return List.copyOf(transformed);
        }
        if (item instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) item;
            List<CartesianPoint> points = sampleLooseEdgePoints(orientedCurve.curveElement(), builder);
            if (points == null) {
                return null;
            }
            if (orientedCurve.orientation()) {
                return points;
            }
            List<CartesianPoint> reversed = new ArrayList<>(points);
            Collections.reverse(reversed);
            return List.copyOf(reversed);
        }
        if (item instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            return sampleGeometricCollectionPoints(geometricSet.elements(), builder);
        }
        if (item instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
            return sampleGeometricCollectionPoints(curveSet.elements(), builder);
        }
        if (item instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet connectedEdgeSet = (StepConnectedEdgeSet) item;
            return sampleGeometricCollectionPoints(connectedEdgeSet.edges(), builder);
        }
        if (item instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) item;
            return sampleWireShellPoints(wireShell, builder);
        }
        if (item instanceof StepEdgeWire) {
            StepEdgeWire edgeWire = (StepEdgeWire) item;
            return sampleGeometricCollectionPoints(edgeWire.edges(), builder);
        }
        Curve3 curve = curveForLooseEdge(item, builder);
        if (curve == null) {
            return null;
        }
        return sampleLooseCurve(curve);
    }

    /**
     * Samples a loose curve to produce points.
     *
     * @param curve the curve to sample
     * @return list of sampled points
     */
    public static List<CartesianPoint> sampleLooseCurve(Curve3 curve) {
        return Curve3SamplingHelper.sampleLooseCurve(curve);
    }

    /**
     * Samples annotation fill area points.
     *
     * @param fillArea the fill area
     * @param builder the CAD builder
     * @return list of sampled points
     */
    public static List<CartesianPoint> sampleAnnotationFillAreaPoints(
            StepAnnotationFillArea fillArea,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> points = new ArrayList<>();
        boolean first = true;
        for (StepEntity boundary : fillArea.boundaries()) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(boundary, builder);
            if (sampled == null || sampled.isEmpty()) {
                continue;
            }
            int start = first ? 0 : 1;
            for (int i = start; i < sampled.size(); i++) {
                points.add(sampled.get(i));
            }
            first = false;
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    // ================================================================================
    // EDGE SAMPLING METHODS
    // ================================================================================

    private static List<CartesianPoint> sampleEdgePreview(
            int edgeId,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        try {
            Edge edge = builder.buildEdge(edgeId);
            return sampleEdge(edge.start().point(), edge.end().point(), edge.curve(), edge.sameSense());
        } catch (TopologyException ex) {
            StepEntity entity = resolved.get(edgeId);
            if (!(entity instanceof StepEdgeCurve)) {
                throw ex;
            }
            StepEdgeCurve edge = (StepEdgeCurve) entity;
            CartesianPoint start = StepPointExtractor.pointFromStep(edge.start().point());
            CartesianPoint end = StepPointExtractor.pointFromStep(edge.end().point());
            StepEntity edgeGeometry = edge.edgeGeometry();
            Curve3 curve = curveForLooseEdge(edgeGeometry, builder);
            if (curve == null) {
                throw ex;
            }
            try {
                return sampleEdge(start, end, curve, edge.sameSense());
            } catch (GeometryException geometryException) {
                return List.of(start, end);
            }
        }
    }

    
    @FunctionalInterface
    private interface EdgeSampleHandler {
        List<CartesianPoint> sample(CartesianPoint start, CartesianPoint end, Curve3 curve, boolean naturalForward);
    }

    private record EdgeSampleRule(Class<?> type, EdgeSampleHandler handler) {
        boolean matches(Curve3 curve) {
            return type.isInstance(curve);
        }
    }

    private static EdgeSampleRule edgeSampleRule(Class<?> type, EdgeSampleHandler handler) {
        return new EdgeSampleRule(type, handler);
    }

    /**
     * Edge-sampling rules keyed by concrete curve type, replacing the former
     * 13-branch if/else-if chain. Order mirrors the original chain (first
     * match wins); branch bodies are verbatim. Any curve matching no rule
     * throws the original terminal UnsupportedGeometryException.
     */
    private static final List<EdgeSampleRule> EDGE_SAMPLE_RULES = List.of(
            edgeSampleRule(TrimmedCurve3.class, (start, end, curve, naturalForward) -> {
            TrimmedCurve3 trimmedCurve = (TrimmedCurve3) curve;
            List<CartesianPoint> points = new ArrayList<>(Curve3SamplingHelper.sampleTrimmedCurve3(trimmedCurve, 72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
                }),
            edgeSampleRule(SurfaceCurve3.class, (start, end, curve, naturalForward) -> {
            SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
            return sampleEdge(start, end, surfaceCurve.curve3d(), naturalForward);
                }),
            edgeSampleRule(BSplineCurve3.class, (start, end, curve, naturalForward) -> {
            BSplineCurve3 splineCurve = (BSplineCurve3) curve;
            List<CartesianPoint> points = new ArrayList<>(splineCurve.sample(72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
                }),
            edgeSampleRule(RationalBSplineCurve3.class, (start, end, curve, naturalForward) -> {
            RationalBSplineCurve3 splineCurve = (RationalBSplineCurve3) curve;
            List<CartesianPoint> points = new ArrayList<>(splineCurve.sample(72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
                }),
            edgeSampleRule(Line3.class, (start, end, curve, naturalForward) -> {
            return List.of(start, end);
                }),
            edgeSampleRule(Circle.class, (start, end, curve, naturalForward) -> {
            Circle circle = (Circle) curve;
            return Curve3SamplingHelper.sampleCircleArc(circle, start, end, naturalForward);
                }),
            edgeSampleRule(Ellipse3.class, (start, end, curve, naturalForward) -> {
            Ellipse3 ellipse = (Ellipse3) curve;
            return Curve3SamplingHelper.sampleEllipseArc(ellipse, start, end, naturalForward);
                }),
            edgeSampleRule(Polyline3.class, (start, end, curve, naturalForward) -> {
            Polyline3 polyline = (Polyline3) curve;
            List<CartesianPoint> points = new ArrayList<>(polyline.points());
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
                }),
            edgeSampleRule(CompositeCurve3.class, (start, end, curve, naturalForward) -> {
            CompositeCurve3 compositeCurve = (CompositeCurve3) curve;
            List<CartesianPoint> points = new ArrayList<>();
            boolean firstSegment = true;
            for (Curve3 segment : compositeCurve.segments()) {
                List<CartesianPoint> segmentPoints = sampleEdge(start, end, segment, naturalForward);
                int startIndex = firstSegment ? 0 : 1;
                for (int i = startIndex; i < segmentPoints.size(); i++) {
                    points.add(segmentPoints.get(i));
                }
                firstSegment = false;
            }
            if (!points.isEmpty()) {
                points.set(0, start);
                points.set(points.size() - 1, end);
            }
            return List.copyOf(points);
                }),
            edgeSampleRule(Parabola3.class, (start, end, curve, naturalForward) -> {
            Parabola3 parabola = (Parabola3) curve;
            List<CartesianPoint> points = new ArrayList<>(parabola.sample(72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            if (points.size() >= 2) {
                points.set(0, start);
                points.set(points.size() - 1, end);
            }
            return List.copyOf(points);
                }),
            edgeSampleRule(Hyperbola3.class, (start, end, curve, naturalForward) -> {
            Hyperbola3 hyperbola = (Hyperbola3) curve;
            List<CartesianPoint> points = new ArrayList<>(hyperbola.sample(72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            if (points.size() >= 2) {
                points.set(0, start);
                points.set(points.size() - 1, end);
            }
            return List.copyOf(points);
                }),
            edgeSampleRule(Clothoid3.class, (start, end, curve, naturalForward) -> {
            Clothoid3 clothoid = (Clothoid3) curve;
            List<CartesianPoint> points = new ArrayList<>(clothoid.sample(72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            if (points.size() >= 2) {
                points.set(0, start);
                points.set(points.size() - 1, end);
            }
            return List.copyOf(points);
                }),
            edgeSampleRule(DegenerateCurve3.class, (start, end, curve, naturalForward) -> {
            DegenerateCurve3 degenerate = (DegenerateCurve3) curve;
            // Degenerate curve: a single collapsed point; return start-end as a degenerate edge
            return List.of(start, end);
                })
    );

    public static List<CartesianPoint> sampleEdge(CartesianPoint start, CartesianPoint end, Curve3 curve, boolean naturalForward) {
        for (EdgeSampleRule rule : EDGE_SAMPLE_RULES) {
            if (rule.matches(curve)) {
                return rule.handler().sample(start, end, curve, naturalForward);
            }
        }
        throw new UnsupportedGeometryException("preview export requires LINE, CIRCLE, ELLIPSE, PARABOLA, HYPERBOLA, CLOTHOID, POLYLINE, COMPOSITE_CURVE, B_SPLINE, RATIONAL_B_SPLINE_CURVE, OFFSET_CURVE_2D/3D, SURFACE_CURVE, SEAM_CURVE, DEGENERATE_CURVE or TRIMMED_CURVE topology");
    }


    // ================================================================================
    // CURVE RESOLUTION METHODS
    // ================================================================================

    @FunctionalInterface
    private interface LooseEdgeCurveFactory {
        Curve3 build(StepEntity item, StepCadBuilder builder);
    }

    private record LooseEdgeCurveRule(Class<?> type, Predicate<StepEntity> guard, LooseEdgeCurveFactory factory) {
        boolean matches(StepEntity item) {
            return type.isInstance(item) && (guard == null || guard.test(item));
        }
    }

    private static LooseEdgeCurveRule looseEdgeRule(Class<?> type, LooseEdgeCurveFactory factory) {
        return new LooseEdgeCurveRule(type, null, factory);
    }

    /** Types materialized through builder.buildCurveReference3. */
    private static LooseEdgeCurveRule curveReferenceRule(Class<?> type) {
        return looseEdgeRule(type, (item, builder) -> builder.buildCurveReference3(item.id()));
    }

    /** Wrapper curves resolved through the curve they reference. */
    private static LooseEdgeCurveRule recurseRule(Class<?> type, Function<StepEntity, StepEntity> next) {
        return looseEdgeRule(type, (item, builder) -> curveForLooseEdge(next.apply(item), builder));
    }

    /** Pcurve families whose builder result is a 2D curve lifted to 3D. */
    private static LooseEdgeCurveRule pcurveRule(Class<?> type) {
        return looseEdgeRule(type, (item, builder) -> {
            Object built = builder.buildPcurve2(item.id());
            return built instanceof Curve2 ? liftCurve2((Curve2) built) : null;
        });
    }

    /** 2D curve families lifted to 3D through builder.buildCurve3From2D. */
    private static final List<Class<?>> CURVE_FROM_2D_TYPES = List.of(
            StepCircle2D.class,
            StepEllipse2D.class,
            StepHyperbola2D.class,
            StepParabola2D.class,
            StepPolyline2D.class,
            StepTrimmedCurve2D.class,
            StepCompositeCurve2D.class,
            StepBezierCurve2D.class,
            StepQuasiUniformCurve2D.class,
            StepUniformCurve2D.class,
            StepPiecewiseBezierCurve2D.class,
            StepIndexedPolyCurve2D.class,
            StepDegenerateCurve2D.class,
            StepBSplineCurve2D.class,
            StepRationalBSplineCurve2D.class,
            StepLine2D.class,
            StepCurve2D.class,
            StepBoundedCurve2D.class
    );

    /**
     * Loose-edge curve factories keyed by concrete type, replacing the former
     * 45-branch if/else-if chain. Order mirrors the original chain (first
     * match wins). Two dead duplicate branches (a second COMPOSITE_CURVE_ON_SURFACE
     * and a second SEAM_CURVE entry, each unreachable behind an identical earlier
     * match) are dropped. Rules in LOOSE_EDGE_CURVE_RULES run under the original
     * try block and swallow builder failures as null; the trailing rules keep
     * the old behavior of propagating those failures to the caller.
     */
    private static final List<LooseEdgeCurveRule> LOOSE_EDGE_CURVE_RULES = List.of(
            looseEdgeRule(StepLine.class, (item, builder) -> builder.buildLine(item.id())),
            looseEdgeRule(StepCircle.class, (item, builder) -> builder.buildCircle(item.id())),
            looseEdgeRule(StepEllipse.class, (item, builder) -> builder.buildEllipse(item.id())),
            looseEdgeRule(StepConicCurve.class, (item, builder) -> {
                List<CartesianPoint> points = ConicSamplingHelper.sampleConicCurvePoints((StepConicCurve) item, builder);
                return points == null ? null : new Polyline3(points);
            }),
            curveReferenceRule(StepBezierCurve.class),
            curveReferenceRule(StepUniformCurve.class),
            curveReferenceRule(StepQuasiUniformCurve.class),
            curveReferenceRule(StepPiecewiseBezierCurve.class),
            looseEdgeRule(StepBSplineCurveWithKnots.class, (item, builder) -> builder.buildBSplineCurve(item.id())),
            looseEdgeRule(StepSurfaceCurve.class, (item, builder) -> builder.buildSurfaceCurve(item.id())),
            looseEdgeRule(StepSeamCurve.class, (item, builder) -> builder.buildSeamCurve(item.id())),
            looseEdgeRule(StepTrimmedCurve.class, (item, builder) -> builder.buildTrimmedCurve(item.id())),
            looseEdgeRule(StepPolyline.class, (item, builder) -> builder.buildPolyline(item.id())),
            looseEdgeRule(StepCompositeCurve.class, (item, builder) -> builder.buildCompositeCurve(item.id())),
            looseEdgeRule(StepCompositeCurveOnSurface.class, (item, builder) -> builder.buildCompositeCurve(item.id())),
            looseEdgeRule(StepRationalBSplineCurve.class, (item, builder) -> builder.buildRationalBSplineCurve(item.id())),
            looseEdgeRule(StepOffsetCurve2D.class, (item, builder) -> liftCurve2(builder.buildOffsetCurve2(item.id()))),
            looseEdgeRule(StepOffsetCurve3D.class, (item, builder) -> builder.buildOffsetCurve3(item.id())),
            pcurveRule(StepPcurve.class),
            pcurveRule(StepDegeneratePcurve.class),
            recurseRule(StepOrientedCurve.class, item -> ((StepOrientedCurve) item).curveElement()),
            recurseRule(StepAnnotationCurveOccurrence.class, item -> ((StepAnnotationCurveOccurrence) item).item()),
            recurseRule(StepDimensionCurve.class, item -> ((StepDimensionCurve) item).item()),
            recurseRule(StepLeaderCurve.class, item -> ((StepLeaderCurve) item).item()),
            recurseRule(StepProjectionCurve.class, item -> ((StepProjectionCurve) item).item()),
            recurseRule(StepDraughtingAnnotationOccurrence.class, item -> ((StepDraughtingAnnotationOccurrence) item).item()),
            recurseRule(StepTerminatorSymbol.class, item -> ((StepTerminatorSymbol) item).annotatedCurve()),
            new LooseEdgeCurveRule(StepGeometricReplica.class,
                    item -> "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName()),
                    (item, builder) -> {
                        List<CartesianPoint> points = sampleLooseEdgePoints(item, builder);
                        return points == null ? null : new Polyline3(points);
                    }),
            curveReferenceRule(StepIndexedPolyCurve.class),
            curveReferenceRule(StepClothoid.class),
            curveReferenceRule(StepDegenerateCurve.class),
            curveReferenceRule(StepBSplineCurve.class),
            looseEdgeRule(StepBSplineCurveWithKnotsAndBreakpoints.class, (item, builder) ->
                    builder.buildBSplineCurveWithBreakpoints(item.id())),
            looseEdgeRule(StepLineSegment.class, (item, builder) -> {
                StepLineSegment lineSeg = (StepLineSegment) item;
                return new Polyline3(List.of(
                        builder.buildPoint(lineSeg.startPoint().id()),
                        builder.buildPoint(lineSeg.endPoint().id())
                ));
            }),
            curveReferenceRule(StepEdgeCurve.class),
            curveReferenceRule(StepSurfacedEdgeCurve.class),
            curveReferenceRule(StepCompositeCurveOnSurface3D.class),
            looseEdgeRule(StepPath.class, (item, builder) -> builder.buildPath(item.id())),
            looseEdgeRule(StepOpenPath.class, (item, builder) -> builder.buildPath(item.id())),
            looseEdgeRule(StepSubpath.class, (item, builder) -> builder.buildPath(item.id())),
            new LooseEdgeCurveRule(StepEntity.class,
                    item -> CURVE_FROM_2D_TYPES.stream().anyMatch(type -> type.isInstance(item)),
                    (item, builder) -> builder.buildCurve3From2D(item.id()))
    );

    /** Trailing rules of the old chain that sat outside its try block. */
    private static final List<LooseEdgeCurveRule> LOOSE_EDGE_FALLBACK_RULES = List.of(
            curveReferenceRule(StepBoundedCurve.class),
            recurseRule(StepMappedItem.class, item -> ((StepMappedItem) item).mappingTarget())
    );

    static Curve3 curveForLooseEdge(StepEntity item, StepCadBuilder builder) {
        try {
            for (LooseEdgeCurveRule rule : LOOSE_EDGE_CURVE_RULES) {
                if (rule.matches(item)) {
                    return rule.factory().build(item, builder);
                }
            }
        } catch (UnsupportedGeometryException | StepResolutionException ex) {
            return null;
        }
        for (LooseEdgeCurveRule rule : LOOSE_EDGE_FALLBACK_RULES) {
            if (rule.matches(item)) {
                return rule.factory().build(item, builder);
            }
        }
        return null;
    }

    private static Curve3 liftCurve2(Curve2 curve) {
        List<Point2> points2 = Curve2SamplingHelper.sampleLooseCurve2(curve);
        List<CartesianPoint> points3 = new ArrayList<>(points2.size());
        for (Point2 point : points2) {
            points3.add(new CartesianPoint(point.x(), point.y(), 0.0));
        }
        return new Polyline3(List.copyOf(points3));
    }

    // ================================================================================
    // EDGE PAYLOAD BUILDING METHODS
    // ================================================================================

    private static ColorPayload resolveEdgeColor(int edgeId, StepMetadataExtractor metadata) {
        StepMetadataExtractor.DisplayMetadata meta = metadata.forItem(edgeId);
        return meta.rgb() != null ? PayloadConversionHelper.toColorPayload(meta.rgb()) : null;
    }

    static EdgePayload buildTopologyEdgePayload(int edgeId, Edge edge) {
        return new EdgePayload(
                edgeId,
                PayloadConversionHelper.toPointPayloads(sampleEdge(edge.start().point(), edge.end().point(), edge.curve(), edge.sameSense())),
                null,
                null
        );
    }

    private static EdgePayload toPolylineEdgePayload(StepPolyline polyline) {
        List<CartesianPoint> points = polyline.points().stream()
                .map(StepPointExtractor::pointFromStep)
                .collect(Collectors.toList());
        return new EdgePayload(polyline.id(), PayloadConversionHelper.toPointPayloads(points), null, null);
    }

    private static EdgePayload toPolyLoopEdgePayload(StepPolyLoop polyLoop) {
        List<CartesianPoint> points = polyLoop.polygon().stream()
                .map(StepPointExtractor::pointFromStep)
                .collect(Collectors.toList());
        List<CartesianPoint> closed = new ArrayList<>(points);
        if (!closed.isEmpty() && closed.get(0).distanceTo(closed.get(closed.size() - 1)) > 1.0e-9) {
            closed.add(closed.get(0));
        }
        return new EdgePayload(polyLoop.id(), PayloadConversionHelper.toPointPayloads(List.copyOf(closed)), null, null);
    }

    static EdgeCurvePayload sampledCurvePayload(StepEntity item, StepCadBuilder builder) {
        String type = previewCurveTypeName(item);
        if (type == null) {
            return null;
        }
        String basisType = previewCurveBasisTypeName(item);
        Integer basisStepId = previewCurveBasisStepId(item);
        Boolean orientation = previewCurveOrientation(item);
        Boolean senseAgreement = previewCurveSenseAgreement(item);
        Double offsetDistance = previewCurveOffsetDistance(item);
        Boolean selfIntersect = previewCurveSelfIntersect(item);
        List<Double> refDirection = previewCurveRefDirection(item);
        Double transformScale = previewCurveTransformScale(item);
        String masterRepresentation = previewCurveMasterRepresentation(item);
        List<String> associatedSurfaceTypes = previewCurveAssociatedSurfaceTypes(item);
        List<Integer> associatedSurfaceStepIds = previewCurveAssociatedSurfaceStepIds(item);
        try {
            if (item instanceof StepCircle) {
            StepCircle circle = (StepCircle) item;
                Circle geometry = builder.buildCircle(circle.id());
                Axis2Placement3D placement = geometry.position();
                return new EdgeCurvePayload(
                        item.id(),
                        "CIRCLE",
                        basisType,
                        basisStepId,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        geometry.radius(),
                        null,
                        null,
                        orientation,
                        senseAgreement,
                        offsetDistance,
                        selfIntersect,
                        refDirection,
                        transformScale,
                        masterRepresentation,
                        associatedSurfaceTypes,
                        associatedSurfaceStepIds,
                        null,
                        null,
                        0.0,
                        Math.PI * 2.0
                );
            }
            if (item instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) item;
                Ellipse3 geometry = builder.buildEllipse(ellipse.id());
                Axis2Placement3D placement = geometry.position();
                return new EdgeCurvePayload(
                        item.id(),
                        "ELLIPSE",
                        basisType,
                        basisStepId,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        null,
                        geometry.semiAxis1(),
                        geometry.semiAxis2(),
                        orientation,
                        senseAgreement,
                        offsetDistance,
                        selfIntersect,
                        refDirection,
                        transformScale,
                        masterRepresentation,
                        associatedSurfaceTypes,
                        associatedSurfaceStepIds,
                        null,
                        null,
                        0.0,
                        Math.PI * 2.0
                );
            }
        } catch (GeometryException | StepResolutionException ex) {
            log.debug("stage={} curveId={}, reason={}", "sampled_curve_payload_fallback", item.id(), ex.getMessage());
        }
        return new EdgeCurvePayload(
                item.id(),
                type,
                basisType,
                basisStepId,
                null,
                null,
                null,
                null,
                null,
                null,
                orientation,
                senseAgreement,
                offsetDistance,
                selfIntersect,
                refDirection,
                transformScale,
                masterRepresentation,
                associatedSurfaceTypes,
                associatedSurfaceStepIds,
                null,
                null,
                0.0,
                0.0
        );
    }

    private static EdgeCurvePayload edgeCurvePayload(
            StepEntity edgeGeometry,
            CartesianPoint start,
            CartesianPoint end,
            boolean naturalForward,
            StepCadBuilder builder
    ) {
        try {
            if (edgeGeometry instanceof StepLine) {
                return sampledCurvePayload(edgeGeometry, builder);
            }
            if (edgeGeometry instanceof StepCircle) {
                StepCircle circle = (StepCircle) edgeGeometry;
                Circle geometry = builder.buildCircle(circle.id());
                Axis2Placement3D placement = geometry.position();
                double startAngle = geometry.angleOf(start);
                double endAngle = geometry.angleOf(end);
                return new EdgeCurvePayload(
                        edgeGeometry.id(),
                        "circle_arc",
                        null,
                        null,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        geometry.radius(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        startAngle,
                        Curve3SamplingHelper.arcSweep(startAngle, endAngle, start.distanceTo(end) <= Epsilon.EPS, naturalForward)
                );
            }
            if (edgeGeometry instanceof StepEllipse) {
                StepEllipse ellipse = (StepEllipse) edgeGeometry;
                Ellipse3 geometry = builder.buildEllipse(ellipse.id());
                Axis2Placement3D placement = geometry.position();
                double startAngle = geometry.angleOf(start);
                double endAngle = geometry.angleOf(end);
                return new EdgeCurvePayload(
                        edgeGeometry.id(),
                        "ellipse_arc",
                        null,
                        null,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        null,
                        geometry.semiAxis1(),
                        geometry.semiAxis2(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        startAngle,
                        Curve3SamplingHelper.arcSweep(startAngle, endAngle, start.distanceTo(end) <= Epsilon.EPS, naturalForward)
                );
            }
            if (edgeGeometry instanceof StepBSplineCurveWithKnots) {
                StepBSplineCurveWithKnots bspline = (StepBSplineCurveWithKnots) edgeGeometry;
                BSplineCurve3 geometry = builder.buildBSplineCurve(bspline.id());
                return newBSplineCurvePayload(edgeGeometry.id(), geometry);
            }
            if (edgeGeometry instanceof StepBSplineCurve) {
                StepBSplineCurve bspline = (StepBSplineCurve) edgeGeometry;
                BSplineCurve3 geometry = builder.buildBSplineCurve(bspline.id());
                return newBSplineCurvePayload(edgeGeometry.id(), geometry);
            }
            if (edgeGeometry instanceof StepRationalBSplineCurve) {
                StepRationalBSplineCurve rational = (StepRationalBSplineCurve) edgeGeometry;
                RationalBSplineCurve3 geometry = builder.buildRationalBSplineCurve3(rational.id());
                return newRationalBSplineCurvePayload(edgeGeometry.id(), geometry);
            }
            if (edgeGeometry instanceof StepPolyline) {
                StepPolyline polyline = (StepPolyline) edgeGeometry;
                Polyline3 geometry = builder.buildPolyline(polyline.id());
                return newPolylineCurvePayload(edgeGeometry.id(), geometry, start, end);
            }
            if (edgeGeometry instanceof StepLine) {
                StepLine line = (StepLine) edgeGeometry;
                return newLineCurvePayload(edgeGeometry.id(), builder, line, start, end);
            }
            EdgeCurvePayload generic = sampledCurvePayload(edgeGeometry, builder);
            if (generic != null) {
                return generic;
            }
        } catch (GeometryException | TopologyException ex) {
            log.debug("stage={} edgeGeometryId={}, reason={}", "edge_curve_payload_skipped", edgeGeometry.id(), ex.getMessage());
        }
        return null;
    }

    private static EdgeCurvePayload newBSplineCurvePayload(int stepId, BSplineCurve3 geometry) {
        return new EdgeCurvePayload(
                stepId, "bspline_curve", null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, 0.0, 0.0);
    }

    private static EdgeCurvePayload newRationalBSplineCurvePayload(int stepId, RationalBSplineCurve3 geometry) {
        return new EdgeCurvePayload(
                stepId, "rational_bspline_curve", null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, 0.0, 0.0);
    }

    private static EdgeCurvePayload newPolylineCurvePayload(int stepId, Polyline3 geometry, CartesianPoint start, CartesianPoint end) {
        return new EdgeCurvePayload(
                stepId, "polyline", null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, 0.0, 0.0);
    }

    private static EdgeCurvePayload newLineCurvePayload(int stepId, StepCadBuilder builder, StepLine line, CartesianPoint start, CartesianPoint end) {
        return new EdgeCurvePayload(
                stepId, "line", null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, 0.0, 0.0);
    }

    // ================================================================================
    // COLLECTION METHODS
    // ================================================================================

    /**
     * Collects standalone edges from a STEP entity.
     *
     * @param item the STEP entity
     * @param edges the edge payload map
     * @param resolved the resolved entity map
     * @param builder the CAD builder
     * @param metadata the metadata extractor
     */
    // collectStandaloneEdges dispatch table (first-match-return, mirrors the original sequential ifs).
    private record EdgeCollectRule(Class<? extends StepEntity> type, EdgeCollectHandler handler) {}

    private interface EdgeCollectHandler {
        void collect(StepEntity item, Map<Integer, EdgePayload> edges,
                Map<Integer, StepEntity> resolved, StepCadBuilder builder,
                StepMetadataExtractor metadata);
    }

    private static EdgeCollectRule edgeCollectRule(
            Class<? extends StepEntity> type, EdgeCollectHandler handler) {
        return new EdgeCollectRule(type, handler);
    }

    private static final List<EdgeCollectRule> EDGE_COLLECT_RULES = List.of(
        edgeCollectRule(StepStyledItem.class, (item, edges, resolved, builder, metadata) -> {
            StepStyledItem styledItem = (StepStyledItem) item;
            collectStandaloneEdges(styledItem.item(), edges, resolved, builder, metadata);
            return;
        }),
        edgeCollectRule(StepOverRidingStyledItem.class, (item, edges, resolved, builder, metadata) -> {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) item;
            collectStandaloneEdges(styledItem.item(), edges, resolved, builder, metadata);
            return;
        }),
        edgeCollectRule(StepPolyline.class, (item, edges, resolved, builder, metadata) -> {
            StepPolyline polyline = (StepPolyline) item;
            edges.putIfAbsent(polyline.id(), toPolylineEdgePayload(polyline));
            return;
        }),
        edgeCollectRule(StepGeometricCurveSet.class, (item, edges, resolved, builder, metadata) -> {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
            for (StepEntity element : curveSet.elements()) {
            collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepGeometricSet.class, (item, edges, resolved, builder, metadata) -> {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            for (StepEntity element : geometricSet.elements()) {
            collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepShellBasedWireframeModel.class, (item, edges, resolved, builder, metadata) -> {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) item;
            for (StepEntity boundary : wireframeModel.boundaries()) {
            collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepEdgeBasedWireframeModel.class, (item, edges, resolved, builder, metadata) -> {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) item;
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
            collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepConnectedEdgeSet.class, (item, edges, resolved, builder, metadata) -> {
            StepConnectedEdgeSet connectedEdgeSet = (StepConnectedEdgeSet) item;
            for (StepEntity edge : connectedEdgeSet.edges()) {
            collectStandaloneEdges(edge, edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepEdgeCurve.class, (item, edges, resolved, builder, metadata) -> {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) item;
            edges.putIfAbsent(edgeCurve.id(), buildEdgePayload(edgeCurve.id(), resolved, builder, metadata));
            return;
        }),
        edgeCollectRule(StepFilletEdge.class, (item, edges, resolved, builder, metadata) -> {
            StepFilletEdge filletEdge = (StepFilletEdge) item;
            edges.putIfAbsent(filletEdge.id(), buildEdgePayload(filletEdge.id(), resolved, builder, metadata));
            return;
        }),
        edgeCollectRule(StepChamferEdge.class, (item, edges, resolved, builder, metadata) -> {
            StepChamferEdge chamferEdge = (StepChamferEdge) item;
            edges.putIfAbsent(chamferEdge.id(), buildEdgePayload(chamferEdge.id(), resolved, builder, metadata));
            return;
        }),
        edgeCollectRule(StepPath.class, (item, edges, resolved, builder, metadata) -> {
            StepPath path = (StepPath) item;
            for (StepOrientedEdge orientedEdge : path.edges()) {
            edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }),
        edgeCollectRule(StepOpenPath.class, (item, edges, resolved, builder, metadata) -> {
            StepOpenPath path = (StepOpenPath) item;
            for (StepOrientedEdge orientedEdge : path.edges()) {
            edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }),
        edgeCollectRule(StepSubpath.class, (item, edges, resolved, builder, metadata) -> {
            StepSubpath subpath = (StepSubpath) item;
            for (StepOrientedEdge orientedEdge : subpath.edges()) {
            edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }),
        edgeCollectRule(StepOrientedPath.class, (item, edges, resolved, builder, metadata) -> {
            StepOrientedPath orientedPath = (StepOrientedPath) item;
            for (StepOrientedEdge orientedEdge : orientedPath.edges()) {
            edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }),
        edgeCollectRule(StepWireShell.class, (item, edges, resolved, builder, metadata) -> {
            StepWireShell wireShell = (StepWireShell) item;
            for (StepEntity loop : wireShell.loops()) {
            collectStandaloneEdges(loop, edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepEdgeWire.class, (item, edges, resolved, builder, metadata) -> {
            StepEdgeWire edgeWire = (StepEdgeWire) item;
            for (StepEntity edge : edgeWire.edges()) {
            collectStandaloneEdges(edge, edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepGeometricSurfaceSet.class, (item, edges, resolved, builder, metadata) -> {
            StepGeometricSurfaceSet surfaceSet = (StepGeometricSurfaceSet) item;
            for (StepEntity element : surfaceSet.elements()) {
            collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepEdgeLoop.class, (item, edges, resolved, builder, metadata) -> {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) item;
            for (StepOrientedEdge orientedEdge : edgeLoop.edges()) {
            edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }),
        edgeCollectRule(StepPolyLoop.class, (item, edges, resolved, builder, metadata) -> {
            StepPolyLoop polyLoop = (StepPolyLoop) item;
            edges.putIfAbsent(polyLoop.id(), toPolyLoopEdgePayload(polyLoop));
            return;
        }),
        edgeCollectRule(StepVertexShell.class, (item, edges, resolved, builder, metadata) -> {
            return;
        }),
        edgeCollectRule(com.minicad.step.model.StepVertexLoop.class, (item, edges, resolved, builder, metadata) -> {
            return;
        }),
        edgeCollectRule(StepAnnotationCurveOccurrence.class, (item, edges, resolved, builder, metadata) -> {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) item;
            collectStandaloneEdges(occurrence.item(), edges, resolved, builder, metadata);
            return;
        }),
        edgeCollectRule(StepAnnotationFillArea.class, (item, edges, resolved, builder, metadata) -> {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) item;
            for (StepEntity boundary : fillArea.boundaries()) {
            collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepAnnotationFillAreaOccurrence.class, (item, edges, resolved, builder, metadata) -> {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) item;
            collectStandaloneEdges(fillAreaOccurrence.item(), edges, resolved, builder, metadata);
            return;
        }),
        edgeCollectRule(StepAnnotationSymbol.class, (item, edges, resolved, builder, metadata) -> {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) item;
            collectMappedAnnotationEdges(
            annotationSymbol.id(),
            annotationSymbol.mappingSource().mappedRepresentation(),
            annotationSymbol.mappingSource().mappedOrigin(),
            annotationSymbol.mappingTarget(),
            null,
            null,
            edges,
            resolved,
            builder
            );
            return;
        }),
        edgeCollectRule(StepAnnotationSymbolOccurrence.class, (item, edges, resolved, builder, metadata) -> {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) item;
            if (!collectMappedAnnotationCarrierEdges(
            symbolOccurrence.id(),
            "ANNOTATION_SYMBOL_OCCURRENCE",
            symbolOccurrence.id(),
            symbolOccurrence.item(),
            edges,
            resolved,
            builder
            )) {
            collectStandaloneEdges(symbolOccurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepAnnotationSubfigureOccurrence.class, (item, edges, resolved, builder, metadata) -> {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) item;
            if (!collectMappedAnnotationCarrierEdges(
            subfigureOccurrence.id(),
            "ANNOTATION_SUBFIGURE_OCCURRENCE",
            subfigureOccurrence.id(),
            subfigureOccurrence.item(),
            edges,
            resolved,
            builder
            )) {
            collectStandaloneEdges(subfigureOccurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepAnnotationText.class, (item, edges, resolved, builder, metadata) -> {
            StepAnnotationText annotationText = (StepAnnotationText) item;
            collectMappedAnnotationEdges(
            annotationText.id(),
            annotationText.mappingSource().mappedRepresentation(),
            annotationText.mappingSource().mappedOrigin(),
            annotationText.mappingTarget(),
            null,
            null,
            edges,
            resolved,
            builder
            );
            return;
        }),
        edgeCollectRule(StepAnnotationTextCharacter.class, (item, edges, resolved, builder, metadata) -> {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) item;
            collectMappedAnnotationEdges(
            annotationTextCharacter.id(),
            annotationTextCharacter.mappingSource().mappedRepresentation(),
            annotationTextCharacter.mappingSource().mappedOrigin(),
            annotationTextCharacter.mappingTarget(),
            null,
            null,
            edges,
            resolved,
            builder
            );
            return;
        }),
        edgeCollectRule(StepDimensionCurve.class, (item, edges, resolved, builder, metadata) -> {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) item;
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
            edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
            collectStandaloneEdges(dimensionCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepLeaderCurve.class, (item, edges, resolved, builder, metadata) -> {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) item;
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
            edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
            collectStandaloneEdges(leaderCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepProjectionCurve.class, (item, edges, resolved, builder, metadata) -> {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) item;
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
            edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
            collectStandaloneEdges(projectionCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepDraughtingAnnotationOccurrence.class, (item, edges, resolved, builder, metadata) -> {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) item;
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
            edges.putIfAbsent(sampled.stepId(), sampled);
            } else if (collectMappedAnnotationCarrierEdges(
            annotationOccurrence.id(),
            "DRAUGHTING_ANNOTATION_OCCURRENCE",
            annotationOccurrence.id(),
            annotationOccurrence.item(),
            edges,
            resolved,
            builder
            )) {
            return;
            } else {
            collectStandaloneEdges(annotationOccurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepTerminatorSymbol.class, (item, edges, resolved, builder, metadata) -> {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) item;
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
            edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
            collectStandaloneEdges(terminatorSymbol.annotatedCurve(), edges, resolved, builder, metadata);
            }
            return;
        }),
        edgeCollectRule(StepSubedge.class, (item, edges, resolved, builder, metadata) -> {
            StepSubedge subedge = (StepSubedge) item;
            collectStandaloneEdges(subedge.parentEdge(), edges, resolved, builder, metadata);
            return;
        })
    );

    static void collectStandaloneEdges(
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        for (EdgeCollectRule rule : EDGE_COLLECT_RULES) {
            if (rule.type().isInstance(item)) {
                rule.handler().collect(item, edges, resolved, builder, metadata);
                return;
            }
        }

        if (isSampledCurveSource(item)) {
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            }
        }
    }

    static void collectMappedAnnotationEdges(
            int mappedOwnerId,
            StepRepresentation representation,
            StepEntity mappedOrigin,
            StepEntity mappingTarget,
            String sourceType,
            Integer sourceStepId,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        double[] matrix = StepPlacementTransformer.matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
        if (matrix == null) {
            return;
        }
        RepresentationBuildResult source = StepRepresentationPayloadBuilder.buildRepresentationPayload(
                representation,
                representation.name(),
                resolved,
                builder,
                StepMetadataExtractor.fromResolved(resolved),
                new LinkedHashSet<>()
        );
        for (EdgePayload edge : source.payload().edges()) {
            EdgePayload transformed = StepMappedItemTransformer.transformMappedEdge(edge, mappedOwnerId, matrix, sourceType, sourceStepId);
            edges.putIfAbsent(transformed.stepId(), transformed);
        }
    }

    // ================================================================================
    // HELPER SAMPLING METHODS
    // ================================================================================

    private static List<CartesianPoint> sampleGeometricCollectionPoints(
            List<StepEntity> elements,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> points = new ArrayList<>();
        for (StepEntity element : elements) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(element, builder);
            if (sampled != null && !sampled.isEmpty()) {
                points.addAll(sampled);
            }
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    private static List<CartesianPoint> sampleWireShellPoints(
            StepWireShell wireShell,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> points = new ArrayList<>();
        for (StepLoop loop : wireShell.loops()) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(loop, builder);
            if (sampled != null && !sampled.isEmpty()) {
                points.addAll(sampled);
            }
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    static List<CartesianPoint> sampleMappedAnnotationPoints(
            StepRepresentation representation,
            StepEntity mappedOrigin,
            StepEntity mappingTarget,
            StepCadBuilder builder
    ) {
        double[] matrix = StepPlacementTransformer.matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
        if (matrix == null) {
            return null;
        }
        List<CartesianPoint> points = new ArrayList<>();
        for (StepEntity content : representation.items()) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(content, builder);
            if (sampled == null) {
                continue;
            }
            for (CartesianPoint point : sampled) {
                points.add(MathUtilityHelper.transformCartesian(point, matrix));
            }
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    private static List<CartesianPoint> sampleWireframeBoundaryPoints(
            List<? extends StepEntity> boundaries,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> points = new ArrayList<>();
        boolean first = true;
        for (StepEntity boundary : boundaries) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(boundary, builder);
            if (sampled == null || sampled.isEmpty()) {
                continue;
            }
            int start = first ? 0 : 1;
            for (int i = start; i < sampled.size(); i++) {
                points.add(sampled.get(i));
            }
            first = false;
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    // ================================================================================
    // UNWRAP HELPER METHODS
    // ================================================================================

    private static StepEntity unwrapAssociatedCurveGeometry(StepEntity edgeGeometry) {
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

    // Delegate to StepSummaryBuilder - extracted utility class
    static String associatedGeometrySummary(StepEntity edgeGeometry) {
        return StepSummaryBuilder.associatedGeometrySummary(edgeGeometry);
    }

    // ================================================================================
    // SURFACE PATCH METHODS
    // ================================================================================

    static SurfacePatch buildFourSidedPatch(EdgeLoop outerLoop) {
        if (outerLoop.edges().size() != 4) {
            return null;
        }
        List<CartesianPoint> bottom = sampleOrientedEdge(outerLoop.edges().get(0));
        List<CartesianPoint> right = sampleOrientedEdge(outerLoop.edges().get(1));
        List<CartesianPoint> top = reversed(sampleOrientedEdge(outerLoop.edges().get(2)));
        List<CartesianPoint> left = reversed(sampleOrientedEdge(outerLoop.edges().get(3)));
        if (!cornersMatch(bottom, right, top, left)) {
            return null;
        }
        int uSegments = Math.max(Math.max(bottom.size(), top.size()) - 1, 8);
        int vSegments = Math.max(Math.max(left.size(), right.size()) - 1, 8);
        return new SurfacePatch(
                resamplePolyline(bottom, uSegments),
                resamplePolyline(top, uSegments),
                resamplePolyline(left, vSegments),
                resamplePolyline(right, vSegments)
        );
    }

    private static boolean cornersMatch(
            List<CartesianPoint> bottom,
            List<CartesianPoint> right,
            List<CartesianPoint> top,
            List<CartesianPoint> left
    ) {
        return close(bottom.get(0), left.get(0))
                && close(bottom.get(bottom.size() - 1), right.get(0))
                && close(top.get(0), left.get(left.size() - 1))
                && close(top.get(top.size() - 1), right.get(right.size() - 1));
    }

    private static boolean close(CartesianPoint left, CartesianPoint right) {
        return left.distanceTo(right) <= 1.0e-6;
    }

    // Delegate to StepGeometryHelper - extracted utility class
    private static List<CartesianPoint> reversed(List<CartesianPoint> points) {
        return StepGeometryHelper.reversed(points);
    }

    // Delegate to StepGeometryHelper - extracted utility class
    private static List<CartesianPoint> resamplePolyline(List<CartesianPoint> points, int segments) {
        return StepGeometryHelper.resamplePolyline(points, segments);
    }

    // ================================================================================
    // PREVIEW CURVE HELPER METHODS
    // ================================================================================

    private static String previewCurveTypeName(StepEntity item) {
        return StepCurveTypeNameResolver.previewCurveTypeName(item);
    }

    private static String previewCurveBasisTypeName(StepEntity item) {
        return StepCurveTypeNameResolver.previewCurveBasisTypeName(item);
    }

    private static Integer previewCurveBasisStepId(StepEntity item) {
        return StepCurveTypeNameResolver.previewCurveBasisStepId(item);
    }

    private static Boolean previewCurveOrientation(StepEntity item) {
        return StepCurveTypeNameResolver.previewCurveOrientation(item);
    }

    private static Boolean previewCurveSenseAgreement(StepEntity item) {
        return StepCurveTypeNameResolver.previewCurveSenseAgreement(item);
    }

    private static Double previewCurveOffsetDistance(StepEntity item) {
        return StepCurveTypeNameResolver.previewCurveOffsetDistance(item);
    }

    private static Boolean previewCurveSelfIntersect(StepEntity item) {
        return StepCurveTypeNameResolver.previewCurveSelfIntersect(item);
    }

    private static List<Double> previewCurveRefDirection(StepEntity item) {
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return List.copyOf(offsetCurve3D.refDirection().directionRatios());
        }
        return null;
    }

    private static Double previewCurveTransformScale(StepEntity item) {
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            return replica.transformation().scale();
        }
        return null;
    }

    private static String previewCurveMasterRepresentation(StepEntity item) {
        StepEntity semanticCurve = previewCurveSemanticItem(item);
        if (semanticCurve instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) semanticCurve;
            return surfaceCurve.masterRepresentation();
        }
        if (semanticCurve instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) semanticCurve;
            return seamCurve.masterRepresentation();
        }
        return null;
    }

    private static List<String> previewCurveAssociatedSurfaceTypes(StepEntity item) {
        List<StepEntity> associatedGeometry = previewCurveAssociatedGeometry(item);
        if (associatedGeometry == null || associatedGeometry.isEmpty()) {
            return null;
        }
        List<String> surfaceTypes = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve) {
                StepPcurve pcurve = (StepPcurve) associated;
                surfaceTypes.add(StepTypeNameResolver.surfaceTypeName(pcurve.basisSurface()));
            } else if (associated instanceof StepDegeneratePcurve) {
                StepDegeneratePcurve pcurve = (StepDegeneratePcurve) associated;
                surfaceTypes.add(StepTypeNameResolver.surfaceTypeName(pcurve.basisSurface()));
            }
        }
        return surfaceTypes.isEmpty() ? null : List.copyOf(surfaceTypes);
    }

    private static List<Integer> previewCurveAssociatedSurfaceStepIds(StepEntity item) {
        List<StepEntity> associatedGeometry = previewCurveAssociatedGeometry(item);
        if (associatedGeometry == null || associatedGeometry.isEmpty()) {
            return null;
        }
        List<Integer> surfaceIds = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve) {
                StepPcurve pcurve = (StepPcurve) associated;
                surfaceIds.add(pcurve.basisSurface().id());
            } else if (associated instanceof StepDegeneratePcurve) {
                StepDegeneratePcurve pcurve = (StepDegeneratePcurve) associated;
                surfaceIds.add(pcurve.basisSurface().id());
            }
        }
        return surfaceIds.isEmpty() ? null : List.copyOf(surfaceIds);
    }

    private static List<StepEntity> previewCurveAssociatedGeometry(StepEntity item) {
        StepEntity semanticCurve = previewCurveSemanticItem(item);
        if (semanticCurve instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) semanticCurve;
            return surfaceCurve.associatedGeometry();
        }
        if (semanticCurve instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) semanticCurve;
            return seamCurve.associatedGeometry();
        }
        return null;
    }

    private record SemanticCurveUnwrapRule(Class<?> type, Predicate<StepEntity> guard, Function<StepEntity, StepEntity> next) {
        boolean matches(StepEntity item) {
            return type.isInstance(item) && (guard == null || guard.test(item));
        }
    }

    /**
     * Wrapper curves unwrapped to the curve they reference, replacing the
     * former 8-branch if/else-if chain in previewCurveSemanticItem.
     */
    private static final List<SemanticCurveUnwrapRule> SEMANTIC_CURVE_UNWRAP_RULES = List.of(
            new SemanticCurveUnwrapRule(StepOrientedCurve.class, null, item -> ((StepOrientedCurve) item).curveElement()),
            new SemanticCurveUnwrapRule(StepAnnotationCurveOccurrence.class, null, item -> ((StepAnnotationCurveOccurrence) item).item()),
            new SemanticCurveUnwrapRule(StepDimensionCurve.class, null, item -> ((StepDimensionCurve) item).item()),
            new SemanticCurveUnwrapRule(StepLeaderCurve.class, null, item -> ((StepLeaderCurve) item).item()),
            new SemanticCurveUnwrapRule(StepProjectionCurve.class, null, item -> ((StepProjectionCurve) item).item()),
            new SemanticCurveUnwrapRule(StepDraughtingAnnotationOccurrence.class, null, item -> ((StepDraughtingAnnotationOccurrence) item).item()),
            new SemanticCurveUnwrapRule(StepTerminatorSymbol.class, null, item -> ((StepTerminatorSymbol) item).annotatedCurve()),
            new SemanticCurveUnwrapRule(StepGeometricReplica.class,
                    item -> "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName()),
                    item -> ((StepGeometricReplica) item).parent())
    );

    static StepEntity previewCurveSemanticItem(StepEntity item) {
        StepEntity current = item;
        while (true) {
            StepEntity next = null;
            for (SemanticCurveUnwrapRule rule : SEMANTIC_CURVE_UNWRAP_RULES) {
                if (rule.matches(current)) {
                    next = rule.next().apply(current);
                    break;
                }
            }
            if (next == null) {
                return current;
            }
            current = next;
        }
    }

    private record MappedAnnotationCarrier(
            Class<?> type,
            Function<StepEntity, StepRepresentation> mappedRepresentation,
            Function<StepEntity, StepEntity> mappedOrigin,
            Function<StepEntity, StepEntity> mappingTarget
    ) {
        boolean matches(StepEntity item) {
            return type.isInstance(item);
        }
    }

    /** Annotation carriers whose mapped representation supplies the collected edges. */
    private static final List<MappedAnnotationCarrier> MAPPED_ANNOTATION_CARRIERS = List.of(
            new MappedAnnotationCarrier(StepAnnotationSymbol.class,
                    item -> ((StepAnnotationSymbol) item).mappingSource().mappedRepresentation(),
                    item -> ((StepAnnotationSymbol) item).mappingSource().mappedOrigin(),
                    item -> ((StepAnnotationSymbol) item).mappingTarget()),
            new MappedAnnotationCarrier(StepAnnotationText.class,
                    item -> ((StepAnnotationText) item).mappingSource().mappedRepresentation(),
                    item -> ((StepAnnotationText) item).mappingSource().mappedOrigin(),
                    item -> ((StepAnnotationText) item).mappingTarget()),
            new MappedAnnotationCarrier(StepAnnotationTextCharacter.class,
                    item -> ((StepAnnotationTextCharacter) item).mappingSource().mappedRepresentation(),
                    item -> ((StepAnnotationTextCharacter) item).mappingSource().mappedOrigin(),
                    item -> ((StepAnnotationTextCharacter) item).mappingTarget())
    );

    /** Occurrence types recursed through to their underlying annotation carrier. */
    private static final List<SemanticCurveUnwrapRule> MAPPED_CARRIER_OCCURRENCE_RULES = List.of(
            new SemanticCurveUnwrapRule(StepAnnotationSymbolOccurrence.class, null, item -> ((StepAnnotationSymbolOccurrence) item).item()),
            new SemanticCurveUnwrapRule(StepAnnotationSubfigureOccurrence.class, null, item -> ((StepAnnotationSubfigureOccurrence) item).item())
    );

    static boolean collectMappedAnnotationCarrierEdges(
            int mappedOwnerId,
            String sourceType,
            Integer sourceStepId,
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        for (MappedAnnotationCarrier carrier : MAPPED_ANNOTATION_CARRIERS) {
            if (carrier.matches(item)) {
                collectMappedAnnotationEdges(
                        mappedOwnerId,
                        carrier.mappedRepresentation().apply(item),
                        carrier.mappedOrigin().apply(item),
                        carrier.mappingTarget().apply(item),
                        sourceType,
                        sourceStepId,
                        edges,
                        resolved,
                        builder
                );
                return true;
            }
        }
        for (SemanticCurveUnwrapRule rule : MAPPED_CARRIER_OCCURRENCE_RULES) {
            if (rule.matches(item)) {
                return collectMappedAnnotationCarrierEdges(
                        mappedOwnerId,
                        sourceType,
                        sourceStepId,
                        rule.next().apply(item),
                        edges,
                        resolved,
                        builder
                );
            }
        }
        return false;
    }

    private static boolean isSampledCurveSource(StepEntity item) {
        return StepValidationHelper.isSampledCurveSource(item);
    }

}
