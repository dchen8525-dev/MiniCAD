package com.minicad.export.json;

import com.minicad.preview.builder.PreviewFaceBuilder;
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
     * Delegates to the shared SURFACE_UNWRAP_RULES table in
     * PreviewFaceBuilder, which handles the same wrapper types
     * (trimmed, bounded, oriented, offset, patched, mapped, replica).
     *
     * @param geometry the potentially wrapped surface
     * @return the unwrapped base surface
     */
    public static StepEntity unwrapParametricPreviewSurface(StepEntity geometry) {
        return PreviewFaceBuilder.unwrapParametricPreviewSurface(geometry);
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
