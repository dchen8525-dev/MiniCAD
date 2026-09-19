package com.minicad.preview.sampling;

import com.minicad.geometry.*;
import com.minicad.geometry2d.*;
import com.minicad.step.model.StepAnnotationCurveOccurrence;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.*;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.semantic.StepCadBuilder;

import java.util.List;
import java.util.function.Function;

/**
 * The curve-evaluator factory table for the preview pipeline.
 *
 * <p>This file used to carry a second copy of almost the whole json-side curve
 * stack: {@code sampleLooseEdgePoints} and its {@code LOOSE_EDGE_POINTS_RULES}
 * table, {@code curveForLooseEdge} and its {@code LOOSE_EDGE_RULES} table, the
 * {@code sampleAnnotationFillAreaPoints} / {@code sampleGeometricCollectionPoints} /
 * {@code sampleWireShellPoints} / {@code sampleWireframeBoundaryPoints} /
 * {@code sampleMappedAnnotationPoints} helpers, a private {@code transformPoint},
 * the conic samplers, and revolution helpers ({@code radialComponent},
 * {@code fallbackNormal}, {@code unwrapPeriodic}). None of it had a production
 * caller here -- the json stack in {@code StepEdgePayloadBuilder} had already
 * been extracted and refactored past it, so the copies were stale snapshots kept
 * alive only by their own twin tests. Only {@link #curveEvaluator} and
 * {@link #sampledCurveEvaluator} were reachable, via
 * {@code StepRepresentationPayloadBuilder.curveEvaluator}.
 *
 * <p>What remains is the one thing this file is the home of: the ordered
 * {@code CURVE_EVALUATOR_RULES} table that turns a {@code StepEntity} into a
 * {@link CurveEvaluator}. Everything else resolves through its live owner --
 * conic sampling through {@link ConicSamplingHelper}, loose-edge sampling and
 * loose-curve resolution through {@code StepEdgePayloadBuilder}, point transforms
 * through {@code MathUtilityHelper} / {@code StepPointExtractor}.
 */
public final class PreviewCurveEvaluator {

    private PreviewCurveEvaluator() {}

    // The evaluator abstraction is the sibling top-level type
    // {@link com.minicad.preview.sampling.CurveEvaluator}. A byte-identical nested
    // copy used to be declared right here, which shadowed the top-level name inside
    // this file and kept the json pipeline from sharing one interface; it was folded
    // into the top-level type so both pipelines now speak the same type.

    // ─── CurveEvaluator factory ──────────────────────────────────────────

    @FunctionalInterface
    private interface CurveEvaluatorFactory {
        CurveEvaluator evaluate(StepEntity curve, StepCadBuilder builder);
    }

    private record EvaluatorRule(Class<?> type, CurveEvaluatorFactory factory) {
        boolean matches(StepEntity curve) {
            return type.isInstance(curve);
        }
    }

    private static EvaluatorRule rule(Class<?> type, CurveEvaluatorFactory factory) {
        return new EvaluatorRule(type, factory);
    }

    /** Wrapper curves whose evaluator is the evaluator of a referenced curve. */
    private static EvaluatorRule wrapperRule(Class<?> type, Function<StepEntity, StepEntity> next) {
        return rule(type, (curve, builder) -> curveEvaluator(next.apply(curve), builder));
    }

    /** Curve families materialized through builder.buildCurveReference3. */
    private static EvaluatorRule curveReferenceRule(Class<?> type) {
        return rule(type, (curve, builder) -> sampledCurveEvaluator(builder.buildCurveReference3(curve.id())));
    }

    /** 2D curve families lifted to 3D through builder.buildCurve3From2D. */
    private static EvaluatorRule curve2DRule(Class<?> type) {
        return rule(type, (curve, builder) -> sampledCurveEvaluator(builder.buildCurve3From2D(curve.id())));
    }

    /** Path-family curves materialized through builder.buildPath. */
    private static EvaluatorRule pathRule(Class<?> type) {
        return rule(type, (curve, builder) -> sampledCurveEvaluator(builder.buildPath(curve.id())));
    }

    /**
     * Curve-evaluator factories keyed by concrete type, replacing the former
     * 59-branch if/else-if chain. Order mirrors the original chain (first
     * match wins) so subtype overlaps keep their old resolution.
     */
    private static final List<EvaluatorRule> CURVE_EVALUATOR_RULES = List.of(
            rule(StepLine.class, (curve, builder) -> {
                StepLine line = (StepLine) curve;
                Line3 geometry = builder.buildLine(line.id());
                return new CurveEvaluator() {
                    @Override
                    public double start() { return -1.0; }
                    @Override
                    public double end() { return 1.0; }
                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }),
            rule(StepCircle.class, (curve, builder) -> {
                StepCircle circle = (StepCircle) curve;
                Circle geometry = builder.buildCircle(circle.id());
                return new CurveEvaluator() {
                    @Override
                    public double start() { return 0.0; }
                    @Override
                    public double end() { return Math.PI * 2.0; }
                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }),
            rule(StepEllipse.class, (curve, builder) -> {
                StepEllipse ellipse = (StepEllipse) curve;
                Ellipse3 geometry = builder.buildEllipse(ellipse.id());
                return new CurveEvaluator() {
                    @Override
                    public double start() { return 0.0; }
                    @Override
                    public double end() { return Math.PI * 2.0; }
                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }),
            rule(StepBSplineCurveWithKnots.class, (curve, builder) -> {
                StepBSplineCurveWithKnots spline = (StepBSplineCurveWithKnots) curve;
                BSplineCurve3 geometry = builder.buildBSplineCurve(spline.id());
                return new CurveEvaluator() {
                    @Override
                    public double start() { return geometry.startParameter(); }
                    @Override
                    public double end() { return geometry.endParameter(); }
                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }),
            wrapperRule(StepTrimmedCurve.class, curve -> ((StepTrimmedCurve) curve).basisCurve()),
            wrapperRule(StepSurfaceCurve.class, curve -> ((StepSurfaceCurve) curve).curve3d()),
            rule(StepRationalBSplineCurve.class, (curve, builder) -> {
                StepRationalBSplineCurve spline = (StepRationalBSplineCurve) curve;
                RationalBSplineCurve3 geometry = builder.buildRationalBSplineCurve(spline.id());
                return new CurveEvaluator() {
                    @Override public double start() { return geometry.startParameter(); }
                    @Override public double end() { return geometry.endParameter(); }
                    @Override public CartesianPoint pointAt(double parameter) { return geometry.pointAt(parameter); }
                };
            }),
            rule(StepPolyline.class, (curve, builder) -> {
                StepPolyline polyline = (StepPolyline) curve;
                Polyline3 geometry = builder.buildPolyline(polyline.id());
                return new CurveEvaluator() {
                    @Override public double start() { return 0.0; }
                    @Override public double end() { return 1.0; }
                    @Override public CartesianPoint pointAt(double parameter) { return geometry.pointAt(parameter); }
                };
            }),
            rule(StepCompositeCurve.class, (curve, builder) -> {
                StepCompositeCurve compositeCurve = (StepCompositeCurve) curve;
                CompositeCurve3 geometry = builder.buildCompositeCurve(compositeCurve.id());
                return sampledCurveEvaluator(geometry);
            }),
            curveReferenceRule(StepBezierCurve.class),
            curveReferenceRule(StepUniformCurve.class),
            curveReferenceRule(StepQuasiUniformCurve.class),
            curveReferenceRule(StepPiecewiseBezierCurve.class),
            rule(StepOffsetCurve3D.class, (curve, builder) -> {
                StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) curve;
                return sampledCurveEvaluator(builder.buildOffsetCurve3(offsetCurve3D.id()));
            }),
            rule(StepConicCurve.class, (curve, builder) -> {
                StepConicCurve conic = (StepConicCurve) curve;
                // Conic point sampling has one home, ConicSamplingHelper; the private
                // circle/ellipse/parabola/hyperbola samplers that used to sit in this
                // file were a stale byte-identical copy of it and are gone.
                List<CartesianPoint> points = ConicSamplingHelper.sampleConicCurvePoints(conic, builder);
                if (points == null || points.size() < 2) return null;
                return sampledCurveEvaluator(new Polyline3(points));
            }),
            wrapperRule(StepOrientedCurve.class, curve -> ((StepOrientedCurve) curve).curveElement()),
            wrapperRule(StepGeometricReplica.class, curve -> ((StepGeometricReplica) curve).parent()),
            curveReferenceRule(StepBSplineCurve.class),
            rule(StepSeamCurve.class, (curve, builder) -> {
                StepSeamCurve seamCurve = (StepSeamCurve) curve;
                return sampledCurveEvaluator(builder.buildSeamCurve(seamCurve.id()).curve3d());
            }),
            curve2DRule(StepCircle2D.class),
            curve2DRule(StepEllipse2D.class),
            curve2DRule(StepPolyline2D.class),
            curve2DRule(StepTrimmedCurve2D.class),
            curve2DRule(StepCompositeCurve2D.class),
            curve2DRule(StepBezierCurve2D.class),
            curve2DRule(StepQuasiUniformCurve2D.class),
            curve2DRule(StepUniformCurve2D.class),
            curve2DRule(StepPiecewiseBezierCurve2D.class),
            curve2DRule(StepIndexedPolyCurve2D.class),
            curve2DRule(StepDegenerateCurve2D.class),
            curve2DRule(StepBSplineCurve2D.class),
            curve2DRule(StepRationalBSplineCurve2D.class),
            curve2DRule(StepLine2D.class),
            curve2DRule(StepCurve2D.class),
            curve2DRule(StepHyperbola2D.class),
            curve2DRule(StepParabola2D.class),
            curve2DRule(StepOffsetCurve2D.class),
            curveReferenceRule(StepClothoid.class),
            curveReferenceRule(StepIndexedPolyCurve.class),
            curveReferenceRule(StepDegenerateCurve.class),
            rule(StepBSplineCurveWithKnotsAndBreakpoints.class, (curve, builder) -> {
                StepBSplineCurveWithKnotsAndBreakpoints splineBreak = (StepBSplineCurveWithKnotsAndBreakpoints) curve;
                return sampledCurveEvaluator(builder.buildBSplineCurveWithBreakpoints(splineBreak.id()));
            }),
            curveReferenceRule(StepCompositeCurveOnSurface.class),
            curveReferenceRule(StepCompositeCurveOnSurface3D.class),
            rule(StepLineSegment.class, (curve, builder) -> {
                StepLineSegment lineSeg = (StepLineSegment) curve;
                List<CartesianPoint> pts = List.of(
                        builder.buildPoint(lineSeg.startPoint().id()),
                        builder.buildPoint(lineSeg.endPoint().id())
                );
                return sampledCurveEvaluator(new Polyline3(pts));
            }),
            pathRule(StepPath.class),
            pathRule(StepOpenPath.class),
            pathRule(StepSubpath.class),
            pathRule(StepOrientedPath.class),
            curveReferenceRule(StepEdgeCurve.class),
            curveReferenceRule(StepSurfacedEdgeCurve.class),
            wrapperRule(StepAnnotationCurveOccurrence.class, curve -> ((StepAnnotationCurveOccurrence) curve).item()),
            wrapperRule(StepDimensionCurve.class, curve -> ((StepDimensionCurve) curve).item()),
            wrapperRule(StepLeaderCurve.class, curve -> ((StepLeaderCurve) curve).item()),
            wrapperRule(StepProjectionCurve.class, curve -> ((StepProjectionCurve) curve).item()),
            wrapperRule(StepDraughtingAnnotationOccurrence.class, curve -> ((StepDraughtingAnnotationOccurrence) curve).item()),
            wrapperRule(StepTerminatorSymbol.class, curve -> ((StepTerminatorSymbol) curve).annotatedCurve()),
            curveReferenceRule(StepCurve.class),
            curveReferenceRule(StepBoundedCurve.class),
            wrapperRule(StepMappedItem.class, curve -> ((StepMappedItem) curve).mappingTarget())
    );

    public static CurveEvaluator curveEvaluator(StepEntity curve, StepCadBuilder builder) {
        for (EvaluatorRule candidate : CURVE_EVALUATOR_RULES) {
            if (candidate.matches(curve)) {
                return candidate.factory().evaluate(curve, builder);
            }
        }
        return null;
    }

    // ─── Sampled curve evaluator ─────────────────────────────────────────

    public static CurveEvaluator sampledCurveEvaluator(Curve3 curve) {
        List<CartesianPoint> points = curve.sample(128);
        if (points.size() < 2) return null;
        return new CurveEvaluator() {
            @Override public double start() { return 0.0; }
            @Override public double end() { return 1.0; }
            @Override
            public CartesianPoint pointAt(double parameter) {
                double t = Math.max(0, Math.min(1, parameter));
                double idx = t * (points.size() - 1);
                int i0 = (int) idx;
                int i1 = Math.min(i0 + 1, points.size() - 1);
                double f = idx - i0;
                CartesianPoint p0 = points.get(i0);
                CartesianPoint p1 = points.get(i1);
                return new CartesianPoint(
                        p0.x() + (p1.x() - p0.x()) * f,
                        p0.y() + (p1.y() - p0.y()) * f,
                        p0.z() + (p1.z() - p0.z()) * f
                );
            }
        };
    }
}
