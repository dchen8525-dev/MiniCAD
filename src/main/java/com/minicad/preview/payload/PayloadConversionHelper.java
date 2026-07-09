package com.minicad.preview.payload;

import com.minicad.geometry.CartesianPoint;
import com.minicad.helper.StepMetadataExtractor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper methods for payload conversion.
 * Extracted from StepPreviewJsonExporter for better code organization.
 */
public final class PayloadConversionHelper {

    private PayloadConversionHelper() {
        // Static helper class - no instances
    }

    public static ColorPayload toColorPayload(int[] rgb) {
        if (rgb == null) {
            return null;
        }
        return new ColorPayload(rgb[0], rgb[1], rgb[2]);
    }

    public static PbrPayload toPbrPayload(StepMetadataExtractor.PbrMetadata metadata) {
        if (metadata == null) {
            return null;
        }
        return new PbrPayload(metadata.diffuse(), metadata.specular(), metadata.specularExponent(), metadata.specularColor());
    }

    public static PointPayload toPointPayload(CartesianPoint point) {
        return new PointPayload(point.x(), point.y(), point.z());
    }

    public static List<PointPayload> toPointPayloads(List<CartesianPoint> points) {
        return points.stream().map(PayloadConversionHelper::toPointPayload).collect(Collectors.toList());
    }
}