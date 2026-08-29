package com.minicad.export.json;

import com.minicad.step.model.*;

/**
 * Utility class for unwrapping and extracting STEP entities.
 * Extracted from StepPreviewJsonExporter to improve maintainability.
 *
 * <p>This class provides methods to unwrap styled items, surfaces,
 * and other composite STEP entities to extract their base geometry.
 */
public final class StepEntityUnwrapper {

    private StepEntityUnwrapper() {
        // Utility class - prevent instantiation
    }

    /**
     * Unwraps styled items to extract the base item.
     * Handles StepStyledItem and StepOverRidingStyledItem.
     *
     * @param item the potentially styled item
     * @return the unwrapped base item
     */
    public static StepEntity unwrapStyledItem(StepEntity item) {
        StepEntity current = item;
        while (true) {
            if (current instanceof StepStyledItem) {
                StepStyledItem styledItem = (StepStyledItem) current;
                current = styledItem.item();
                continue;
            }
            if (current instanceof StepOverRidingStyledItem) {
                StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) current;
                current = styledItem.item();
                continue;
            }
            return current;
        }
    }

    /**
     * Unwraps parametric surfaces to extract the base surface.
     * Handles various surface wrapper types including trimmed,
     * bounded, oriented, offset, and mapped surfaces.
     *
     * @param geometry the potentially wrapped surface
     * @return the unwrapped base surface
     */
    public static StepEntity unwrapParametricPreviewSurface(StepEntity geometry) {
        StepEntity current = geometry;
        for (int depth = 0; depth < 16 && current != null; depth++) {
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
            if (current instanceof StepOffsetSurface2) {
                StepOffsetSurface2 offsetSurface2 = (StepOffsetSurface2) current;
                current = offsetSurface2.basisSurface();
                continue;
            }
            if (current instanceof StepSurfacePatch) {
                StepSurfacePatch surfacePatch = (StepSurfacePatch) current;
                current = surfacePatch.basisSurface();
                continue;
            }
            if (current instanceof StepRectangularCompositeSurface) {
                StepRectangularCompositeSurface compositeSurface = (StepRectangularCompositeSurface) current;
                current = compositeSurface.parentSurface();
                continue;
            }
            if (current instanceof StepMachinedSurface) {
                StepMachinedSurface machinedSurface = (StepMachinedSurface) current;
                current = machinedSurface.face();
                continue;
            }
            if (current instanceof StepBlendedSurface) {
                StepBlendedSurface blended = (StepBlendedSurface) current;
                current = blended.primarySurface();
                continue;
            }
            if (current instanceof StepMappedItem) {
                StepMappedItem mappedItem = (StepMappedItem) current;
                current = mappedItem.mappingTarget();
                continue;
            }
            if (current instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) current).entityName())) {
                StepGeometricReplica replica = (StepGeometricReplica) current;
                current = replica.parent();
                continue;
            }
            return current;
        }
        return current;
    }

    /**
     * Unwraps associated curve geometry to extract the base curve.
     *
     * @param edgeGeometry the potentially wrapped curve
     * @return the unwrapped base curve
     */
    public static StepEntity unwrapAssociatedCurveGeometry(StepEntity edgeGeometry) {
        // Basic implementation - can be extended as needed
        return unwrapStyledItem(edgeGeometry);
    }

    /**
     * Describes an unsupported preview surface for error reporting.
     *
     * @param surface the surface entity
     * @return a description string
     */
    public static String describeUnsupportedPreviewSurface(StepEntity surface) {
        if (surface == null) {
            return null;
        }
        // Recursively unwrap to find the base surface type
        StepEntity baseSurface = unwrapParametricPreviewSurface(surface);
        if (baseSurface == null) {
            return "UNKNOWN_SURFACE";
        }
        return StepTypeNameResolver.surfaceTypeName(baseSurface);
    }
}
