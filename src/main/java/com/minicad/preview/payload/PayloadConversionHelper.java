package com.minicad.preview.payload;

import com.minicad.geometry.CartesianPoint;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper methods for payload conversion.
 * Extracted from StepPreviewJsonExporter for better code organization.
 */
final class PayloadConversionHelper {

    private PayloadConversionHelper() {
        // Static helper class - no instances
    }

    static ColorPayload toColorPayload(int[] rgb) {
        if (rgb == null) {
            return null;
        }
        return new ColorPayload(rgb[0], rgb[1], rgb[2]);
    }

    static PbrPayload toPbrPayload(StepMetadataExtractor.PbrMetadata metadata) {
        if (metadata == null) {
            return null;
        }
        return new PbrPayload(metadata.diffuse(), metadata.specular(), metadata.specularExponent(), metadata.specularColor());
    }

    static PointPayload toPointPayload(CartesianPoint point) {
        return new PointPayload(point.x(), point.y(), point.z());
    }

    static List<PointPayload> toPointPayloads(List<CartesianPoint> points) {
        return points.stream().map(PayloadConversionHelper::toPointPayload).collect(Collectors.toList());
    }
}