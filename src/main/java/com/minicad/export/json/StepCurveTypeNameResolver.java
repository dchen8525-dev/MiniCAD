package com.minicad.export.json;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
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

    private record BasisCurveRule(
            Class<?> type,
            Predicate<StepEntity> guard,
            Function<StepEntity, StepEntity> basisCurve
    ) {
        boolean matches(StepEntity item) {
            return type.isInstance(item) && (guard == null || guard.test(item));
        }
    }

    private static BasisCurveRule basisCurveRule(Class<?> type, Function<StepEntity, StepEntity> basisCurve) {
        return new BasisCurveRule(type, null, basisCurve);
    }

    /**
     * Wrapper curves unwrapped to the basis curve their type names and ids
     * refer to. Shared by previewCurveBasisTypeName and
     * previewCurveBasisStepId, replacing their two former 14-branch
     * if/else-if chains.
     */
    private static final List<BasisCurveRule> BASIS_CURVE_RULES = List.of(
            basisCurveRule(StepSurfaceCurve.class, item -> ((StepSurfaceCurve) item).curve3d()),
            basisCurveRule(StepSeamCurve.class, item -> ((StepSeamCurve) item).curve3d()),
            basisCurveRule(StepTrimmedCurve.class, item -> ((StepTrimmedCurve) item).basisCurve()),
            basisCurveRule(StepOffsetCurve2D.class, item -> ((StepOffsetCurve2D) item).basisCurve()),
            basisCurveRule(StepOffsetCurve3D.class, item -> ((StepOffsetCurve3D) item).basisCurve()),
            basisCurveRule(StepOrientedCurve.class, item -> ((StepOrientedCurve) item).curveElement()),
            basisCurveRule(StepAnnotationCurveOccurrence.class, item -> ((StepAnnotationCurveOccurrence) item).item()),
            basisCurveRule(StepDimensionCurve.class, item -> ((StepDimensionCurve) item).item()),
            basisCurveRule(StepLeaderCurve.class, item -> ((StepLeaderCurve) item).item()),
            basisCurveRule(StepProjectionCurve.class, item -> ((StepProjectionCurve) item).item()),
            basisCurveRule(StepDraughtingAnnotationOccurrence.class, item -> ((StepDraughtingAnnotationOccurrence) item).item()),
            basisCurveRule(StepTerminatorSymbol.class, item -> ((StepTerminatorSymbol) item).annotatedCurve()),
            new BasisCurveRule(StepGeometricReplica.class,
                    item -> "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName()),
                    item -> ((StepGeometricReplica) item).parent()),
            basisCurveRule(StepTrimmedCurve2D.class, item -> ((StepTrimmedCurve2D) item).basisCurve())
    );

    private static StepEntity basisCurveOf(StepEntity item) {
        for (BasisCurveRule rule : BASIS_CURVE_RULES) {
            if (rule.matches(item)) {
                return rule.basisCurve().apply(item);
            }
        }
        return null;
    }

    public static String previewCurveBasisTypeName(StepEntity item) {
        StepEntity basisCurve = basisCurveOf(item);
        return basisCurve == null ? null : previewCurveTypeName(basisCurve);
    }

    /**
     * Returns the step ID of the basis curve for the given curve entity.
     *
     * @param item the STEP curve entity
     * @return the basis curve step ID, or null if not applicable
     */
    public static Integer previewCurveBasisStepId(StepEntity item) {
        StepEntity basisCurve = basisCurveOf(item);
        return basisCurve == null ? null : basisCurve.id();
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
