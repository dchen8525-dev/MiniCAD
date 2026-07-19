package com.minicad.export.json;

import com.minicad.step.model.*;

/**
 * Utility class for resolving STEP curve type names.
 * Extracted from StepPreviewJsonExporter to improve maintainability.
 *
 * <p>This class provides human-readable type names for STEP curve entities,
 * used in JSON export for debugging and display purposes.
 */
public final class StepCurveTypeNameResolver {

    private StepCurveTypeNameResolver() {
        // Utility class - prevent instantiation
    }

    /**
     * Returns the STEP entity type name for the given curve entity.
     * Used for display and debugging in the preview JSON output.
     *
     * @param item the STEP curve entity
     * @return the STEP entity type name as a string, or null if not recognized
     */
    public static String previewCurveTypeName(StepEntity item) {
        if (item instanceof StepLine) {
            return "LINE";
        }
        if (item instanceof StepCircle) {
            return "CIRCLE";
        }
        if (item instanceof StepEllipse) {
            return "ELLIPSE";
        }
        if (item instanceof StepConicCurve) {
            StepConicCurve conic = (StepConicCurve) item;
            return conic.entityName();
        }
        if (item instanceof StepBezierCurve) {
            return "BEZIER_CURVE";
        }
        if (item instanceof StepUniformCurve) {
            return "UNIFORM_CURVE";
        }
        if (item instanceof StepQuasiUniformCurve) {
            return "QUASI_UNIFORM_CURVE";
        }
        if (item instanceof StepPiecewiseBezierCurve) {
            return "PIECEWISE_BEZIER_CURVE";
        }
        if (item instanceof StepBSplineCurveWithKnots) {
            return "B_SPLINE_CURVE_WITH_KNOTS";
        }
        if (item instanceof StepRationalBSplineCurve) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (item instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) item;
            return surfaceCurve.entityName();
        }
        if (item instanceof StepSeamCurve) {
            return "SEAM_CURVE";
        }
        if (item instanceof StepTrimmedCurve) {
            return "TRIMMED_CURVE";
        }
        if (item instanceof StepPolyline) {
            return "POLYLINE";
        }
        if (item instanceof StepCompositeCurve) {
            return "COMPOSITE_CURVE";
        }
        if (item instanceof StepCompositeCurveOnSurface) {
            return "COMPOSITE_CURVE_ON_SURFACE";
        }
        if (item instanceof StepOffsetCurve2D) {
            return "OFFSET_CURVE_2D";
        }
        if (item instanceof StepOffsetCurve3D) {
            return "OFFSET_CURVE_3D";
        }
        if (item instanceof StepPcurve) {
            return "PCURVE";
        }
        if (item instanceof StepDegeneratePcurve) {
            return "DEGENERATE_PCURVE";
        }
        if (item instanceof StepOrientedCurve) {
            return "ORIENTED_CURVE";
        }
        if (item instanceof StepAnnotationCurveOccurrence) {
            return "ANNOTATION_CURVE_OCCURRENCE";
        }
        if (item instanceof StepDimensionCurve) {
            return "DIMENSION_CURVE";
        }
        if (item instanceof StepLeaderCurve) {
            return "LEADER_CURVE";
        }
        if (item instanceof StepProjectionCurve) {
            return "PROJECTION_CURVE";
        }
        if (item instanceof StepDraughtingAnnotationOccurrence) {
            return "DRAUGHTING_ANNOTATION_OCCURRENCE";
        }
        if (item instanceof StepTerminatorSymbol) {
            return "TERMINATOR_SYMBOL";
        }
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            return "CURVE_REPLICA";
        }
        if (item instanceof StepBSplineCurve) {
            return "B_SPLINE_CURVE";
        }
        if (item instanceof StepCompositeCurveOnSurface3D) {
            return "COMPOSITE_CURVE_ON_SURFACE_3D";
        }
        if (item instanceof StepClothoid) {
            return "CLOTHOID";
        }
        if (item instanceof StepIndexedPolyCurve) {
            return "INDEXED_POLY_CURVE";
        }
        if (item instanceof StepDegenerateCurve) {
            return "DEGENERATE_CURVE";
        }
        if (item instanceof StepBSplineCurveWithKnotsAndBreakpoints) {
            return "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS";
        }
        if (item instanceof StepLineSegment) {
            return "LINE_SEGMENT";
        }
        if (item instanceof StepEdgeCurve) {
            return "EDGE_CURVE";
        }
        if (item instanceof StepSurfacedEdgeCurve) {
            return "SURFACED_EDGE_CURVE";
        }
        if (item instanceof StepPath) {
            return "PATH";
        }
        if (item instanceof StepOpenPath) {
            return "OPEN_PATH";
        }
        if (item instanceof StepSubpath) {
            return "SUBPATH";
        }
        if (item instanceof StepOrientedPath) {
            return "ORIENTED_PATH";
        }
        if (item instanceof StepCurve) {
            return "CURVE";
        }
        if (item instanceof StepBoundedCurve) {
            return "BOUNDED_CURVE";
        }
        if (item instanceof StepCircle2D) {
            return "CIRCLE_2D";
        }
        if (item instanceof StepEllipse2D) {
            return "ELLIPSE_2D";
        }
        if (item instanceof StepPolyline2D) {
            return "POLYLINE_2D";
        }
        if (item instanceof StepTrimmedCurve2D) {
            return "TRIMMED_CURVE_2D";
        }
        if (item instanceof StepCompositeCurve2D) {
            return "COMPOSITE_CURVE_2D";
        }
        if (item instanceof StepBezierCurve2D) {
            return "BEZIER_CURVE_2D";
        }
        if (item instanceof StepQuasiUniformCurve2D) {
            return "QUASI_UNIFORM_CURVE_2D";
        }
        if (item instanceof StepUniformCurve2D) {
            return "UNIFORM_CURVE_2D";
        }
        if (item instanceof StepPiecewiseBezierCurve2D) {
            return "PIECEWISE_BEZIER_CURVE_2D";
        }
        if (item instanceof StepIndexedPolyCurve2D) {
            return "INDEXED_POLY_CURVE_2D";
        }
        if (item instanceof StepDegenerateCurve2D) {
            return "DEGENERATE_CURVE_2D";
        }
        if (item instanceof StepBSplineCurve2D) {
            return "B_SPLINE_CURVE_2D";
        }
        if (item instanceof StepRationalBSplineCurve2D) {
            return "RATIONAL_B_SPLINE_CURVE_2D";
        }
        if (item instanceof StepLine2D) {
            return "LINE_2D";
        }
        if (item instanceof StepCurve2D) {
            return "CURVE_2D";
        }
        if (item instanceof StepHyperbola2D) {
            return "HYPERBOLA_2D";
        }
        if (item instanceof StepParabola2D) {
            return "PARABOLA_2D";
        }
        return null;
    }

    /**
     * Returns the type name of the basis curve for the given curve entity.
     * Used for composite curves and trimmed curves.
     *
     * @param item the STEP curve entity
     * @return the basis curve type name, or null if not applicable
     */
    public static String previewCurveBasisTypeName(StepEntity item) {
        if (item instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) item;
            return previewCurveTypeName(surfaceCurve.curve3d());
        }
        if (item instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) item;
            return previewCurveTypeName(seamCurve.curve3d());
        }
        if (item instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) item;
            return previewCurveTypeName(trimmedCurve.basisCurve());
        }
        if (item instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) item;
            return previewCurveTypeName(offsetCurve2D.basisCurve());
        }
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return previewCurveTypeName(offsetCurve3D.basisCurve());
        }
        if (item instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) item;
            return previewCurveTypeName(orientedCurve.curveElement());
        }
        if (item instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) item;
            return previewCurveTypeName(occurrence.item());
        }
        if (item instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) item;
            return previewCurveTypeName(dimensionCurve.item());
        }
        if (item instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) item;
            return previewCurveTypeName(leaderCurve.item());
        }
        if (item instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) item;
            return previewCurveTypeName(projectionCurve.item());
        }
        if (item instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) item;
            return previewCurveTypeName(annotationOccurrence.item());
        }
        if (item instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) item;
            return previewCurveTypeName(terminatorSymbol.annotatedCurve());
        }
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            return previewCurveTypeName(replica.parent());
        }
        if (item instanceof StepTrimmedCurve2D) {
            StepTrimmedCurve2D trimmedCurve2D = (StepTrimmedCurve2D) item;
            return previewCurveTypeName(trimmedCurve2D.basisCurve());
        }
        return null;
    }

    /**
     * Returns the step ID of the basis curve for the given curve entity.
     *
     * @param item the STEP curve entity
     * @return the basis curve step ID, or null if not applicable
     */
    public static Integer previewCurveBasisStepId(StepEntity item) {
        if (item instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) item;
            return surfaceCurve.curve3d().id();
        }
        if (item instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) item;
            return seamCurve.curve3d().id();
        }
        if (item instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) item;
            return trimmedCurve.basisCurve().id();
        }
        if (item instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) item;
            return offsetCurve2D.basisCurve().id();
        }
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return offsetCurve3D.basisCurve().id();
        }
        if (item instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) item;
            return orientedCurve.curveElement().id();
        }
        if (item instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) item;
            return occurrence.item().id();
        }
        if (item instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) item;
            return dimensionCurve.item().id();
        }
        if (item instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) item;
            return leaderCurve.item().id();
        }
        if (item instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) item;
            return projectionCurve.item().id();
        }
        if (item instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) item;
            return annotationOccurrence.item().id();
        }
        if (item instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) item;
            return terminatorSymbol.annotatedCurve().id();
        }
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            return replica.parent().id();
        }
        if (item instanceof StepTrimmedCurve2D) {
            StepTrimmedCurve2D trimmedCurve2D = (StepTrimmedCurve2D) item;
            return trimmedCurve2D.basisCurve().id();
        }
        return null;
    }
}