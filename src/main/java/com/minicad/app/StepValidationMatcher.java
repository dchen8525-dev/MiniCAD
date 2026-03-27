package com.minicad.app;

import java.util.Locale;

/**
 * Matches minimal validation-property names to canonical preview-derived metrics.
 */
public final class StepValidationMatcher {

    private StepValidationMatcher() {
    }

    public static String matchPropertyId(String name) {
        return matchPropertyId(name, null);
    }

    public static String matchPropertyId(String name, String measureType) {
        String normalized = normalize(name);
        String normalizedMeasureType = normalize(measureType);
        if (normalized.contains("surface area") || normalized.equals("area") || normalized.contains("face area")) {
            return "surface_area";
        }
        if (normalizedMeasureType.contains("area_measure")) {
            return "surface_area";
        }
        if (normalized.contains("edge length") || normalized.equals("length") || normalized.contains("wire length")) {
            return "edge_length";
        }
        if (normalizedMeasureType.contains("length_measure")) {
            if (normalized.contains("center x") || normalized.contains("centroid x")) {
                return "center_x";
            }
            if (normalized.contains("center y") || normalized.contains("centroid y")) {
                return "center_y";
            }
            if (normalized.contains("center z") || normalized.contains("centroid z")) {
                return "center_z";
            }
            if (normalized.contains("bbox x") || normalized.contains("size x") || normalized.contains("width")) {
                return "bbox_x";
            }
            if (normalized.contains("bbox y") || normalized.contains("size y") || normalized.contains("depth")) {
                return "bbox_y";
            }
            if (normalized.contains("bbox z") || normalized.contains("size z") || normalized.contains("height")) {
                return "bbox_z";
            }
            return "edge_length";
        }
        if (normalized.contains("center x") || normalized.contains("centroid x")) {
            return "center_x";
        }
        if (normalized.contains("center y") || normalized.contains("centroid y")) {
            return "center_y";
        }
        if (normalized.contains("center z") || normalized.contains("centroid z")) {
            return "center_z";
        }
        if (normalized.contains("bbox x") || normalized.contains("size x") || normalized.contains("width")) {
            return "bbox_x";
        }
        if (normalized.contains("bbox y") || normalized.contains("size y") || normalized.contains("depth")) {
            return "bbox_y";
        }
        if (normalized.contains("bbox z") || normalized.contains("size z") || normalized.contains("height")) {
            return "bbox_z";
        }
        if (normalized.contains("face count") || normalized.equals("face count")) {
            return "face_count";
        }
        if (normalized.contains("edge count") || normalized.equals("edge count")) {
            return "edge_count";
        }
        if (normalized.contains("representation count")) {
            return "representation_count";
        }
        if (normalized.contains("instance count")) {
            return "instance_count";
        }
        return "unknown";
    }

    private static String normalize(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }
}
