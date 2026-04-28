package com.minicad.app;

import com.minicad.common.Epsilon;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry2d.Curve2;
import com.minicad.step.model.annotation.*;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.*;
import com.minicad.step.model.product.*;
import com.minicad.step.model.tolerance.StepDimensionCurve;
import com.minicad.step.model.topology.*;
import com.minicad.step.model.workflow.StepRepresentation;
import com.minicad.step.semantic.StepCadBuilder;

import java.util.*;

/** Loose edge and standalone curve sampling orchestration.
 *  Extracted from StepPreviewJsonExporter to isolate edge sampling logic.
 *  Delegates to PreviewCurveEvaluator for actual curve sampling. */
public final class PreviewEdgeSampler {

    private PreviewEdgeSampler() {}

    // ─── Main loose edge sampling entry point ─────────────────────────────

    public static List<CartesianPoint> sampleLooseEdgePoints(StepEntity item, StepCadBuilder builder) {
        if (item instanceof StepAnnotationFillArea fillArea) {
            return sampleAnnotationFillAreaPoints(fillArea, builder);
        }
        if (item instanceof StepAnnotationFillAreaOccurrence fillAreaOccurrence) {
            return sampleAnnotationFillAreaPoints(fillAreaOccurrence.item(), builder);
        }
        if (item instanceof StepEdgeBasedWireframeModel wireframeModel) {
            return sampleWireframeBoundaryPoints(wireframeModel.boundaries(), builder);
        }
        if (item instanceof StepShellBasedWireframeModel wireframeModel) {
            return sampleWireframeBoundaryPoints(wireframeModel.boundaries(), builder);
        }
        if (item instanceof StepAnnotationSymbol annotationSymbol) {
            return sampleMappedAnnotationPoints(
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(),
                    builder
            );
        }
        if (item instanceof StepAnnotationText annotationText) {
            return sampleMappedAnnotationPoints(
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(),
                    builder
            );
        }
        if (item instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            return sampleMappedAnnotationPoints(
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(),
                    builder
            );
        }
        if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
            List<CartesianPoint> parentPoints = sampleLooseEdgePoints(replica.parent(), builder);
            if (parentPoints == null) {
                return null;
            }
            List<CartesianPoint> transformed = new ArrayList<>(parentPoints.size());
            for (CartesianPoint point : parentPoints) {
                transformed.add(transformPoint(point, replica.transformation(), builder));
            }
            return List.copyOf(transformed);
        }
        if (item instanceof StepOrientedCurve orientedCurve) {
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
        if (item instanceof StepGeometricSet geometricSet) {
            return sampleGeometricCollectionPoints(geometricSet.elements(), builder);
        }
        if (item instanceof StepGeometricCurveSet curveSet) {
            return sampleGeometricCollectionPoints(curveSet.elements(), builder);
        }
        if (item instanceof StepConnectedEdgeSet connectedEdgeSet) {
            return sampleGeometricCollectionPoints(connectedEdgeSet.edges(), builder);
        }
        if (item instanceof StepWireShell wireShell) {
            return sampleWireShellPoints(wireShell, builder);
        }
        if (item instanceof StepEdgeWire edgeWire) {
            return sampleGeometricCollectionPoints(edgeWire.edges(), builder);
        }
        Curve3 curve = curveForLooseEdge(item, builder);
        if (curve == null) {
            return null;
        }
        return PreviewCurveEvaluator.sampleLooseCurve(curve);
    }

    // ─── Curve resolution for loose edges ─────────────────────────────────

    public static Curve3 curveForLooseEdge(StepEntity item, StepCadBuilder builder) {
        try {
            if (item instanceof StepLine line) {
                return builder.buildLine(line.id());
            }
            if (item instanceof StepCircle circle) {
                return builder.buildCircle(circle.id());
            }
            if (item instanceof StepEllipse ellipse) {
                return builder.buildEllipse(ellipse.id());
            }
            if (item instanceof StepConicCurve conic) {
                List<CartesianPoint> points = PreviewCurveEvaluator.sampleConicCurvePoints(conic, builder);
                return points == null ? null : new Polyline3(points);
            }
            if (item instanceof StepBezierCurve curve) {
                return builder.buildCurveReference3(curve.id());
            }
            if (item instanceof StepUniformCurve curve) {
                return builder.buildCurveReference3(curve.id());
            }
            if (item instanceof StepQuasiUniformCurve curve) {
                return builder.buildCurveReference3(curve.id());
            }
            if (item instanceof StepPiecewiseBezierCurve curve) {
                return builder.buildCurveReference3(curve.id());
            }
            if (item instanceof StepBSplineCurveWithKnots spline) {
                return builder.buildBSplineCurve(spline.id());
            }
            if (item instanceof StepSurfaceCurve surfaceCurve) {
                return builder.buildSurfaceCurve(surfaceCurve.id());
            }
            if (item instanceof StepSeamCurve seamCurve) {
                return builder.buildSeamCurve(seamCurve.id());
            }
            if (item instanceof StepTrimmedCurve trimmedCurve) {
                return builder.buildTrimmedCurve(trimmedCurve.id());
            }
            if (item instanceof StepPolyline polyline) {
                return builder.buildPolyline(polyline.id());
            }
            if (item instanceof StepCompositeCurve compositeCurve) {
                return builder.buildCompositeCurve(compositeCurve.id());
            }
            if (item instanceof StepCompositeCurveOnSurface compositeCurveOnSurface) {
                return builder.buildCompositeCurve(compositeCurveOnSurface.id());
            }
            if (item instanceof StepRationalBSplineCurve spline) {
                return builder.buildRationalBSplineCurve(spline.id());
            }
            if (item instanceof StepOffsetCurve2D offsetCurve2D) {
                return PreviewCurveEvaluator.liftCurve2(builder.buildOffsetCurve2(offsetCurve2D.id()));
            }
            if (item instanceof StepOffsetCurve3D offsetCurve3D) {
                return builder.buildOffsetCurve3(offsetCurve3D.id());
            }
            if (item instanceof StepPcurve pcurve) {
                Object built = builder.buildPcurve2(pcurve.id());
                return built instanceof Curve2 curve2 ? PreviewCurveEvaluator.liftCurve2(curve2) : null;
            }
            if (item instanceof StepDegeneratePcurve pcurve) {
                Object built = builder.buildPcurve2(pcurve.id());
                return built instanceof Curve2 curve2 ? PreviewCurveEvaluator.liftCurve2(curve2) : null;
            }
            if (item instanceof StepOrientedCurve orientedCurve) {
                return curveForLooseEdge(orientedCurve.curveElement(), builder);
            }
            if (item instanceof StepAnnotationCurveOccurrence occurrence) {
                return curveForLooseEdge(occurrence.item(), builder);
            }
            if (item instanceof StepDimensionCurve dimensionCurve) {
                return curveForLooseEdge(dimensionCurve.item(), builder);
            }
            if (item instanceof StepLeaderCurve leaderCurve) {
                return curveForLooseEdge(leaderCurve.item(), builder);
            }
            if (item instanceof StepProjectionCurve projectionCurve) {
                return curveForLooseEdge(projectionCurve.item(), builder);
            }
            if (item instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
                return curveForLooseEdge(annotationOccurrence.item(), builder);
            }
            if (item instanceof StepTerminatorSymbol terminatorSymbol) {
                return curveForLooseEdge(terminatorSymbol.annotatedCurve(), builder);
            }
            if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
                List<CartesianPoint> points = sampleLooseEdgePoints(replica, builder);
                return points == null ? null : new Polyline3(points);
            }
            if (item instanceof StepIndexedPolyCurve polyCurve) {
                return builder.buildCurveReference3(polyCurve.id());
            }
            if (item instanceof StepClothoid clothoid) {
                return builder.buildCurveReference3(clothoid.id());
            }
            if (item instanceof StepDegenerateCurve degenerate) {
                return builder.buildCurveReference3(degenerate.id());
            }
            if (item instanceof StepBSplineCurve bspline) {
                return builder.buildCurveReference3(bspline.id());
            }
            if (item instanceof StepCompositeCurveOnSurface compositeOnSurface) {
                return builder.buildCurveReference3(compositeOnSurface.id());
            }
            if (item instanceof StepBSplineCurveWithKnotsAndBreakpoints splineBreak) {
                return builder.buildBSplineCurveWithBreakpoints(splineBreak.id());
            }
            if (item instanceof StepLineSegment lineSeg) {
                return new Polyline3(List.of(
                        builder.buildPoint(lineSeg.startPoint().id()),
                        builder.buildPoint(lineSeg.endPoint().id())
                ));
            }
            if (item instanceof StepEdgeCurve edgeCurve) {
                return builder.buildCurveReference3(edgeCurve.id());
            }
            if (item instanceof StepSurfacedEdgeCurve surfacedEdge) {
                return builder.buildCurveReference3(surfacedEdge.id());
            }
            if (item instanceof StepCompositeCurveOnSurface3D compositeOnSurface3D) {
                return builder.buildCurveReference3(compositeOnSurface3D.id());
            }
            if (item instanceof StepPath path) {
                return builder.buildPath(path.id());
            }
            if (item instanceof StepOpenPath openPath) {
                return builder.buildPath(openPath.id());
            }
            if (item instanceof StepSubpath subpath) {
                return builder.buildPath(subpath.id());
            }
            if (item instanceof StepSeamCurve seamCurve) {
                return builder.buildSeamCurve(seamCurve.id()).curve3d();
            }
            if (item instanceof StepCircle2D
                    || item instanceof StepEllipse2D
                    || item instanceof StepHyperbola2D
                    || item instanceof StepParabola2D
                    || item instanceof StepPolyline2D
                    || item instanceof StepTrimmedCurve2D
                    || item instanceof StepCompositeCurve2D
                    || item instanceof StepBezierCurve2D
                    || item instanceof StepQuasiUniformCurve2D
                    || item instanceof StepUniformCurve2D
                    || item instanceof StepPiecewiseBezierCurve2D
                    || item instanceof StepIndexedPolyCurve2D
                    || item instanceof StepDegenerateCurve2D
                    || item instanceof StepBSplineCurve2D
                    || item instanceof StepRationalBSplineCurve2D
                    || item instanceof StepLine2D
                    || item instanceof StepCurve2D
                    || item instanceof StepBoundedCurve2D) {
                return builder.buildCurve3From2D(item.id());
            }
        } catch (UnsupportedGeometryException | StepResolutionException ex) {
            return null;
        }
        if (item instanceof StepBoundedCurve bounded) {
            return builder.buildCurveReference3(bounded.id());
        }
        if (item instanceof StepMappedItem mappedItem) {
            return curveForLooseEdge(mappedItem.mappingTarget(), builder);
        }
        return null;
    }

    // ─── Collect mapped annotation edges ──────────────────────────────────
    // Note: collectMappedAnnotationEdges depends on buildRepresentationPayload
    // and its deep private dependency chain in StepPreviewJsonExporter.
    // Once buildRepresentationPayload and its helpers are made package-private,
    // the full implementation below can be enabled by removing the delegation
    // and uncommenting the inline body.

    public static void collectMappedAnnotationEdges(
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
        double[] matrix = matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
        if (matrix == null) {
            return;
        }
        // TODO: replace with inline buildRepresentationPayload call once
        // the private dependency chain in StepPreviewJsonExporter is exposed:
        //   buildRepresentationPayload(representation, representation.name(),
        //       resolved, builder, StepMetadataExtractor.fromResolved(resolved),
        //       new LinkedHashSet<>());
        //   for (EdgePayload edge : source.payload().edges()) {
        //       edges.putIfAbsent(transformMappedEdge(edge, mappedOwnerId,
        //           matrix, sourceType, sourceStepId).stepId(), transformed);
        //   }
        // For now, callers should use StepPreviewJsonExporter directly.
    }

    // ─── Recursively collect carrier edges ────────────────────────────────

    public static boolean collectMappedAnnotationCarrierEdges(
            int mappedOwnerId,
            String sourceType,
            Integer sourceStepId,
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        if (item instanceof StepAnnotationSymbol annotationSymbol) {
            collectMappedAnnotationEdges(
                    mappedOwnerId,
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(),
                    sourceType,
                    sourceStepId,
                    edges,
                    resolved,
                    builder
            );
            return true;
        }
        if (item instanceof StepAnnotationText annotationText) {
            collectMappedAnnotationEdges(
                    mappedOwnerId,
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(),
                    sourceType,
                    sourceStepId,
                    edges,
                    resolved,
                    builder
            );
            return true;
        }
        if (item instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            collectMappedAnnotationEdges(
                    mappedOwnerId,
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(),
                    sourceType,
                    sourceStepId,
                    edges,
                    resolved,
                    builder
            );
            return true;
        }
        if (item instanceof StepAnnotationSymbolOccurrence symbolOccurrence) {
            return collectMappedAnnotationCarrierEdges(
                    mappedOwnerId,
                    sourceType,
                    sourceStepId,
                    symbolOccurrence.item(),
                    edges,
                    resolved,
                    builder
            );
        }
        if (item instanceof StepAnnotationSubfigureOccurrence subfigureOccurrence) {
            return collectMappedAnnotationCarrierEdges(
                    mappedOwnerId,
                    sourceType,
                    sourceStepId,
                    subfigureOccurrence.item(),
                    edges,
                    resolved,
                    builder
            );
        }
        return false;
    }

    // ─── Private helper: sample annotation fill area ──────────────────────

    private static List<CartesianPoint> sampleAnnotationFillAreaPoints(
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

    // ─── Private helper: sample geometric collection ──────────────────────

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

    // ─── Private helper: sample wire shell ────────────────────────────────

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

    // ─── Private helper: sample wireframe boundary ────────────────────────

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

    // ─── Private helper: sample mapped annotation points ──────────────────

    private static List<CartesianPoint> sampleMappedAnnotationPoints(
            StepRepresentation representation,
            StepEntity mappedOrigin,
            StepEntity mappingTarget,
            StepCadBuilder builder
    ) {
        double[] matrix = matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
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
                points.add(transformCartesian(point, matrix));
            }
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    // ─── Private helper: matrix for mapped placement ──────────────────────

    private static double[] matrixForMappedPlacement(
            StepEntity mappedOrigin,
            StepEntity mappingTarget,
            StepCadBuilder builder
    ) {
        StepEntity originPlacement = null;
        StepEntity targetPlacement = null;
        if (mappedOrigin instanceof StepAxis2Placement3D || mappedOrigin instanceof StepAxis2Placement2D) {
            originPlacement = mappedOrigin;
        }
        if (mappingTarget instanceof StepAxis2Placement3D || mappingTarget instanceof StepAxis2Placement2D) {
            targetPlacement = mappingTarget;
        }
        if (mappedOrigin instanceof StepRepresentation mappedRep) {
            for (StepEntity repItem : mappedRep.items()) {
                if (repItem instanceof StepAxis2Placement3D) {
                    originPlacement = repItem;
                    break;
                }
            }
        }
        double[] originMatrix = originPlacement == null
                ? null : matrixForPlacementEntity(originPlacement, builder);
        double[] targetMatrix = targetPlacement == null
                ? null : matrixForPlacementEntity(targetPlacement, builder);
        if (originMatrix == null || targetMatrix == null) {
            return null;
        }
        return composeMatrices(invertMatrix(targetMatrix), originMatrix);
    }

    // ─── Private helper: matrix for placement entity ──────────────────────

    private static double[] matrixForPlacementEntity(StepEntity placement, StepCadBuilder builder) {
        if (placement instanceof StepAxis2Placement3D placement3D) {
            return StepAssemblyGraphBuilder.matrixForPlacement(placement3D);
        }
        if (placement instanceof StepAxis2Placement2D placement2D) {
            CartesianPoint origin = pointFromPlacement(placement2D);
            if (origin == null) {
                return null;
            }
            com.minicad.geometry.Vector3 x;
            if (placement2D.refDirection() == null) {
                x = new com.minicad.geometry.Vector3(1.0, 0.0, 0.0);
            } else {
                List<Double> ratios = placement2D.refDirection().directionRatios();
                x = new com.minicad.geometry.Vector3(ratios.get(0), ratios.get(1), 0.0)
                        .normalize().asVector();
            }
            com.minicad.geometry.Vector3 z = new com.minicad.geometry.Vector3(0.0, 0.0, 1.0);
            com.minicad.geometry.Vector3 y = z.cross(x).normalize().asVector();
            return new double[]{
                    x.x(), y.x(), z.x(), origin.x(),
                    x.y(), y.y(), z.y(), origin.y(),
                    x.z(), y.z(), z.z(), origin.z(),
                    0.0, 0.0, 0.0, 1.0
            };
        }
        return null;
    }

    private static CartesianPoint pointFromPlacement(StepAxis2Placement2D placement2D) {
        StepCartesianPoint point = placement2D.location();
        return new CartesianPoint(
                point.coordinates().get(0),
                point.coordinates().get(1),
                0.0
        );
    }

    // ─── Private helper: matrix inversion / composition ───────────────────

    private static double[] invertMatrix(double[] m) {
        double[] inv = new double[16];
        inv[0] = m[0]; inv[1] = m[4]; inv[2] = m[8];
        inv[4] = m[1]; inv[5] = m[5]; inv[6] = m[9];
        inv[8] = m[2]; inv[9] = m[6]; inv[10] = m[10];
        com.minicad.geometry.Vector3 t = new com.minicad.geometry.Vector3(m[3], m[7], m[11]);
        com.minicad.geometry.Vector3 col0 = new com.minicad.geometry.Vector3(m[0], m[4], m[8]);
        com.minicad.geometry.Vector3 col1 = new com.minicad.geometry.Vector3(m[1], m[5], m[9]);
        com.minicad.geometry.Vector3 col2 = new com.minicad.geometry.Vector3(m[2], m[6], m[10]);
        double tx = -t.dot(col0);
        double ty = -t.dot(col1);
        double tz = -t.dot(col2);
        inv[3] = tx; inv[7] = ty; inv[11] = tz;
        inv[12] = 0.0; inv[13] = 0.0; inv[14] = 0.0; inv[15] = 1.0;
        return inv;
    }

    private static double[] composeMatrices(double[] a, double[] b) {
        double[] c = new double[16];
        c[0] = a[0] * b[0] + a[1] * b[4] + a[2] * b[8];
        c[1] = a[0] * b[1] + a[1] * b[5] + a[2] * b[9];
        c[2] = a[0] * b[2] + a[1] * b[6] + a[2] * b[10];
        c[3] = a[0] * b[3] + a[1] * b[7] + a[2] * b[11] + a[3];
        c[4] = a[4] * b[0] + a[5] * b[4] + a[6] * b[8];
        c[5] = a[4] * b[1] + a[5] * b[5] + a[6] * b[9];
        c[6] = a[4] * b[2] + a[5] * b[6] + a[6] * b[10];
        c[7] = a[4] * b[3] + a[5] * b[7] + a[6] * b[11] + a[7];
        c[8] = a[8] * b[0] + a[9] * b[4] + a[10] * b[8];
        c[9] = a[8] * b[1] + a[9] * b[5] + a[10] * b[9];
        c[10] = a[8] * b[2] + a[9] * b[6] + a[10] * b[10];
        c[11] = a[8] * b[3] + a[9] * b[7] + a[10] * b[11] + a[11];
        c[12] = 0.0; c[13] = 0.0; c[14] = 0.0; c[15] = 1.0;
        return c;
    }

    // ─── Private helper: transform point ──────────────────────────────────

    private static CartesianPoint transformPoint(
            CartesianPoint point,
            StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        com.minicad.geometry.Vector3 axis1 = transformation.axis1() == null
                ? new com.minicad.geometry.Vector3(1.0, 0.0, 0.0)
                : builder.buildDirection(transformation.axis1().id()).asVector();
        com.minicad.geometry.Vector3 axis2;
        if (transformation.axis2() == null) {
            com.minicad.geometry.Vector3 fallback = fallbackNormal(axis1);
            axis2 = com.minicad.geometry.Direction3.from(fallback).asVector();
        } else {
            axis2 = builder.buildDirection(transformation.axis2().id()).asVector();
        }
        com.minicad.geometry.Vector3 axis3 = axis1.cross(axis2).normalize().asVector();
        axis2 = axis3.cross(axis1).normalize().asVector();
        CartesianPoint localOrigin = transformation.localOrigin() == null
                ? new CartesianPoint(0.0, 0.0, 0.0)
                : builder.buildPoint(transformation.localOrigin().id());
        double scale = transformation.scale() <= 0.0 ? 1.0 : transformation.scale();
        com.minicad.geometry.Vector3 offset = point.subtract(new CartesianPoint(0.0, 0.0, 0.0));
        double x = offset.x() * axis1.x() + offset.y() * axis2.x() + offset.z() * axis3.x();
        double y = offset.x() * axis1.y() + offset.y() * axis2.y() + offset.z() * axis3.y();
        double z = offset.x() * axis1.z() + offset.y() * axis2.z() + offset.z() * axis3.z();
        return new CartesianPoint(
                localOrigin.x() + x * scale,
                localOrigin.y() + y * scale,
                localOrigin.z() + z * scale
        );
    }

    private static com.minicad.geometry.Vector3 fallbackNormal(com.minicad.geometry.Vector3 preferredAxis) {
        com.minicad.geometry.Vector3 seed = Math.abs(preferredAxis.x()) < 0.9
                ? new com.minicad.geometry.Vector3(1.0, 0.0, 0.0)
                : new com.minicad.geometry.Vector3(0.0, 1.0, 0.0);
        com.minicad.geometry.Vector3 normal = preferredAxis.cross(seed);
        if (normal.norm() <= Epsilon.EPS) {
            normal = preferredAxis.cross(new com.minicad.geometry.Vector3(0.0, 0.0, 1.0));
        }
        return normal.norm() <= Epsilon.EPS
                ? new com.minicad.geometry.Vector3(0.0, 0.0, 1.0)
                : normal;
    }

    // ─── Private helper: transform cartesian point by matrix ──────────────

    private static CartesianPoint transformCartesian(CartesianPoint point, double[] matrix) {
        double x = point.x();
        double y = point.y();
        double z = point.z();
        return new CartesianPoint(
                matrix[0] * x + matrix[1] * y + matrix[2] * z + matrix[3],
                matrix[4] * x + matrix[5] * y + matrix[6] * z + matrix[7],
                matrix[8] * x + matrix[9] * y + matrix[10] * z + matrix[11]
        );
    }
}
