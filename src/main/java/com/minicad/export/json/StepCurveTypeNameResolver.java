package com.minicad.export.json;

import java.util.List;
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
        for (CurveTypeNameRule rule : PREVIEW_CURVE_TYPE_NAME_RULES) {
            if (rule.type().isInstance(item)) {
                String name = rule.handler().name(item);
                if (name != null) {
                    return name;
                }
            }
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
    // previewCurveTypeName dispatch table (first-match-return; the guarded
    // StepGeometricReplica branch returns null to fall through). Mirrors the
    // original sequential ifs.
    private record CurveTypeNameRule(Class<? extends StepEntity> type, CurveTypeNameHandler handler) {}

    private interface CurveTypeNameHandler {
        String name(StepEntity item);
    }

    private static CurveTypeNameRule curveTypeNameRule(
            Class<? extends StepEntity> type, CurveTypeNameHandler handler) {
        return new CurveTypeNameRule(type, handler);
    }

    private static final List<CurveTypeNameRule> PREVIEW_CURVE_TYPE_NAME_RULES = List.of(
        curveTypeNameRule(StepLine.class, (item) -> "LINE"),
        curveTypeNameRule(StepCircle.class, (item) -> "CIRCLE"),
        curveTypeNameRule(StepEllipse.class, (item) -> "ELLIPSE"),
        curveTypeNameRule(StepConicCurve.class, (item) -> {
            StepConicCurve conic = (StepConicCurve) item;
            return conic.entityName();
        }),
        curveTypeNameRule(StepBezierCurve.class, (item) -> "BEZIER_CURVE"),
        curveTypeNameRule(StepUniformCurve.class, (item) -> "UNIFORM_CURVE"),
        curveTypeNameRule(StepQuasiUniformCurve.class, (item) -> "QUASI_UNIFORM_CURVE"),
        curveTypeNameRule(StepPiecewiseBezierCurve.class, (item) -> "PIECEWISE_BEZIER_CURVE"),
        curveTypeNameRule(StepBSplineCurveWithKnots.class, (item) -> "B_SPLINE_CURVE_WITH_KNOTS"),
        curveTypeNameRule(StepRationalBSplineCurve.class, (item) -> "RATIONAL_B_SPLINE_CURVE"),
        curveTypeNameRule(StepSurfaceCurve.class, (item) -> {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) item;
            return surfaceCurve.entityName();
        }),
        curveTypeNameRule(StepSeamCurve.class, (item) -> "SEAM_CURVE"),
        curveTypeNameRule(StepTrimmedCurve.class, (item) -> "TRIMMED_CURVE"),
        curveTypeNameRule(StepPolyline.class, (item) -> "POLYLINE"),
        curveTypeNameRule(StepCompositeCurve.class, (item) -> "COMPOSITE_CURVE"),
        curveTypeNameRule(StepCompositeCurveOnSurface.class, (item) -> "COMPOSITE_CURVE_ON_SURFACE"),
        curveTypeNameRule(StepOffsetCurve2D.class, (item) -> "OFFSET_CURVE_2D"),
        curveTypeNameRule(StepOffsetCurve3D.class, (item) -> "OFFSET_CURVE_3D"),
        curveTypeNameRule(StepPcurve.class, (item) -> "PCURVE"),
        curveTypeNameRule(StepDegeneratePcurve.class, (item) -> "DEGENERATE_PCURVE"),
        curveTypeNameRule(StepOrientedCurve.class, (item) -> "ORIENTED_CURVE"),
        curveTypeNameRule(StepAnnotationCurveOccurrence.class, (item) -> "ANNOTATION_CURVE_OCCURRENCE"),
        curveTypeNameRule(StepDimensionCurve.class, (item) -> "DIMENSION_CURVE"),
        curveTypeNameRule(StepLeaderCurve.class, (item) -> "LEADER_CURVE"),
        curveTypeNameRule(StepProjectionCurve.class, (item) -> "PROJECTION_CURVE"),
        curveTypeNameRule(StepDraughtingAnnotationOccurrence.class, (item) -> "DRAUGHTING_ANNOTATION_OCCURRENCE"),
        curveTypeNameRule(StepTerminatorSymbol.class, (item) -> "TERMINATOR_SYMBOL"),
        curveTypeNameRule(StepGeometricReplica.class, (item) -> {
            if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
                return "CURVE_REPLICA";
            }
            return null;
        }),
        curveTypeNameRule(StepBSplineCurve.class, (item) -> "B_SPLINE_CURVE"),
        curveTypeNameRule(StepCompositeCurveOnSurface3D.class, (item) -> "COMPOSITE_CURVE_ON_SURFACE_3D"),
        curveTypeNameRule(StepClothoid.class, (item) -> "CLOTHOID"),
        curveTypeNameRule(StepIndexedPolyCurve.class, (item) -> "INDEXED_POLY_CURVE"),
        curveTypeNameRule(StepDegenerateCurve.class, (item) -> "DEGENERATE_CURVE"),
        curveTypeNameRule(StepBSplineCurveWithKnotsAndBreakpoints.class, (item) -> "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS"),
        curveTypeNameRule(StepLineSegment.class, (item) -> "LINE_SEGMENT"),
        curveTypeNameRule(StepEdgeCurve.class, (item) -> "EDGE_CURVE"),
        curveTypeNameRule(StepSurfacedEdgeCurve.class, (item) -> "SURFACED_EDGE_CURVE"),
        curveTypeNameRule(StepPath.class, (item) -> "PATH"),
        curveTypeNameRule(StepOpenPath.class, (item) -> "OPEN_PATH"),
        curveTypeNameRule(StepSubpath.class, (item) -> "SUBPATH"),
        curveTypeNameRule(StepOrientedPath.class, (item) -> "ORIENTED_PATH"),
        curveTypeNameRule(StepCurve.class, (item) -> "CURVE"),
        curveTypeNameRule(StepBoundedCurve.class, (item) -> "BOUNDED_CURVE"),
        curveTypeNameRule(StepCircle2D.class, (item) -> "CIRCLE_2D"),
        curveTypeNameRule(StepEllipse2D.class, (item) -> "ELLIPSE_2D"),
        curveTypeNameRule(StepPolyline2D.class, (item) -> "POLYLINE_2D"),
        curveTypeNameRule(StepTrimmedCurve2D.class, (item) -> "TRIMMED_CURVE_2D"),
        curveTypeNameRule(StepCompositeCurve2D.class, (item) -> "COMPOSITE_CURVE_2D"),
        curveTypeNameRule(StepBezierCurve2D.class, (item) -> "BEZIER_CURVE_2D"),
        curveTypeNameRule(StepQuasiUniformCurve2D.class, (item) -> "QUASI_UNIFORM_CURVE_2D"),
        curveTypeNameRule(StepUniformCurve2D.class, (item) -> "UNIFORM_CURVE_2D"),
        curveTypeNameRule(StepPiecewiseBezierCurve2D.class, (item) -> "PIECEWISE_BEZIER_CURVE_2D"),
        curveTypeNameRule(StepIndexedPolyCurve2D.class, (item) -> "INDEXED_POLY_CURVE_2D"),
        curveTypeNameRule(StepDegenerateCurve2D.class, (item) -> "DEGENERATE_CURVE_2D"),
        curveTypeNameRule(StepBSplineCurve2D.class, (item) -> "B_SPLINE_CURVE_2D"),
        curveTypeNameRule(StepRationalBSplineCurve2D.class, (item) -> "RATIONAL_B_SPLINE_CURVE_2D"),
        curveTypeNameRule(StepLine2D.class, (item) -> "LINE_2D"),
        curveTypeNameRule(StepCurve2D.class, (item) -> "CURVE_2D"),
        curveTypeNameRule(StepHyperbola2D.class, (item) -> "HYPERBOLA_2D"),
        curveTypeNameRule(StepParabola2D.class, (item) -> "PARABOLA_2D")
    );

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

    /**
     * Returns the orientation of an oriented curve.
     *
     * @param item the STEP curve entity
     * @return the orientation flag, or null if not an oriented curve
     */
    public static Boolean previewCurveOrientation(StepEntity item) {
        if (item instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) item;
            return orientedCurve.orientation();
        }
        return null;
    }

    /**
     * Returns the sense agreement flag for trimmed curves.
     *
     * @param item the STEP curve entity
     * @return the sense agreement flag, or null if not a trimmed curve
     */
    public static Boolean previewCurveSenseAgreement(StepEntity item) {
        if (item instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) item;
            return trimmedCurve.senseAgreement();
        }
        if (item instanceof StepTrimmedCurve2D) {
            StepTrimmedCurve2D trimmedCurve2D = (StepTrimmedCurve2D) item;
            return trimmedCurve2D.senseAgreement();
        }
        return null;
    }

    /**
     * Returns the offset distance for offset curves.
     *
     * @param item the STEP curve entity
     * @return the offset distance, or null if not an offset curve
     */
    public static Double previewCurveOffsetDistance(StepEntity item) {
        if (item instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) item;
            return offsetCurve2D.distance();
        }
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return offsetCurve3D.distance();
        }
        return null;
    }

    /**
     * Returns the self-intersect flag for offset and composite curves.
     *
     * @param item the STEP curve entity
     * @return the self-intersect flag, or null if not applicable
     */
    public static Boolean previewCurveSelfIntersect(StepEntity item) {
        if (item instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) item;
            return offsetCurve2D.selfIntersect();
        }
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return offsetCurve3D.selfIntersect();
        }
        if (item instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeOnSurface = (StepCompositeCurveOnSurface) item;
            return compositeOnSurface.selfIntersect();
        }
        return null;
    }
}
