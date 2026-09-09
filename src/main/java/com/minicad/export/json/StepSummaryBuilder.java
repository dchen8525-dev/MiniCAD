package com.minicad.export.json;

import com.minicad.step.model.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for generating summary strings for STEP entities.
 * Extracted from StepPreviewJsonExporter to improve maintainability.
 *
 * <p>This class provides methods to create human-readable summaries
 * of STEP entities for debugging and error reporting.
 */
public final class StepSummaryBuilder {

    private StepSummaryBuilder() {
        // Utility class - prevent instantiation
    }

    /**
     * Creates a summary string for associated geometry.
     *
     * @param edgeGeometry the edge geometry entity
     * @return a summary string
     */
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
                .collect(Collectors.joining("|"));
    }

    /**
     * Creates a summary string for PCURVE basis surfaces.
     *
     * @param pcurves the list of PCURVE entities
     * @return a summary string
     */
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
                .collect(Collectors.joining("|"));
    }

    /**
     * Unwraps associated curve geometry to extract the base curve.
     * Handles oriented curves, replicas, and annotation occurrences.
     * Delegates to the shared SEMANTIC_CURVE_UNWRAP_RULES table; see
     * StepEdgePayloadBuilder.unwrapAssociatedCurveGeometry.
     *
     * @param edgeGeometry the potentially wrapped curve
     * @return the unwrapped base curve
     */
    private static StepEntity unwrapAssociatedCurveGeometry(StepEntity edgeGeometry) {
        return StepEdgePayloadBuilder.unwrapAssociatedCurveGeometry(edgeGeometry);
    }

    /**
     * Creates a summary of unsupported boolean reason.
     *
     * @param builder the CAD builder
     * @param id the entity ID
     * @return a description string
     */
    public static String unsupportedBooleanReason(Object builder, int id) {
        return "Boolean operation not supported for entity #" + id;
    }
}
