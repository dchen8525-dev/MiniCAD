package com.minicad.preview.mapper;

import com.minicad.export.json.StepPreviewJsonExporter;
import com.minicad.step.model.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.minicad.export.json.StepTypeNameResolver;

/**
 * Utility class for matching pcurve (parametric curve) geometry with face surfaces.
 * Handles unwrapping of curve geometry, extracting basis surfaces, and matching
 * pcurves to appropriate face surfaces.
 *
 * <p>All methods are static and stateless - no instance state is maintained.
 */
public final class PcurveMatcher {

    private PcurveMatcher() {
        // Utility class - prevent instantiation
    }

    /**
     * Determines whether an edge geometry should fall back to projected edge rendering.
     * Returns true if the unwrapped geometry is a surface curve or seam curve with
     * empty associated geometry, or if it's any other type.
     */
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

    /**
     * Generates a summary string of the associated geometry for an edge.
     * Returns a string representation of the unwrapped curve's associated surfaces.
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
     * Unwraps nested curve geometry to find the underlying geometric entity.
     * Handles oriented curves, geometric replicas, annotation curves, and various
     * curve wrapper types.
     *
     * <p>Traverses up to 16 levels deep to prevent infinite loops.
     */
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

    /**
     * Generates a summary string of pcurve basis surfaces.
     * Returns a string showing each pcurve's ID and its basis surface ID.
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
     * Filters associated geometry to find pcurves whose basis surfaces match
     * the acceptable surface IDs for the given face geometry.
     */
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

    /**
     * Computes the set of acceptable basis surface IDs for a face geometry.
     * Traverses through surface wrappers (trimmed, bounded, oriented, offset, replica)
     * to collect all potentially matching surface IDs.
     *
     * <p>Traverses up to 16 levels deep to prevent infinite loops.
     */
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
}