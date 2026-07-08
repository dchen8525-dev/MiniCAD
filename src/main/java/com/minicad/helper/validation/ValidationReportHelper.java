package com.minicad.helper.validation;

import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.base.StepMeasureRepresentationItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Helper class for building validation report payloads.
 * Extracted from StepPreviewJsonExporter for better maintainability.
 */
final class ValidationReportHelper {

    private ValidationReportHelper() {
        // Utility class
    }

    static void includePmi(BoundsAccumulator bounds, List<PmiPayload> pmi) {
        for (PmiPayload item : pmi) {
            bounds.include(item.position());
            for (PointPayload point : item.leader()) {
                bounds.include(point);
            }
        }
    }

    static ValidationReportPayload buildValidationReport(
            Map<Integer, StepEntity> resolved,
            GeometrySummary summary,
            ValidationContext context
    ) {
        List<ValidationCheckPayload> checks = new ArrayList<>();
        int okCount = 0;
        int warnCount = 0;
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepMeasureRepresentationItem)) {
                continue;
            }
            StepMeasureRepresentationItem item = (StepMeasureRepresentationItem) entity;
            String propertyId = StepValidationMatcher.matchPropertyId(item.name(), item.measureType());
            Double actual = actualValidationValue(propertyId, summary, context);
            if (actual == null) {
                continue;
            }
            double delta = actual - item.value();
            boolean matches = Math.abs(delta) <= 1.0e-6;
            if (matches) {
                okCount++;
            } else {
                warnCount++;
            }
            checks.add(new ValidationCheckPayload(
                    propertyId,
                    item.name(),
                    item.measureType(),
                    item.value(),
                    actual,
                    delta,
                    matches ? "ok" : "warn",
                    matches
            ));
        }
        return new ValidationReportPayload(
                checks.isEmpty() ? "empty" : warnCount == 0 ? "ok" : "warn",
                okCount,
                warnCount,
                List.copyOf(checks)
        );
    }

    static Double actualValidationValue(String propertyId, GeometrySummary summary, ValidationContext context) {
        if ("surface_area".equals(propertyId)) {
            return summary.approxSurfaceArea();
        }
        if ("edge_length".equals(propertyId)) {
            return summary.approxEdgeLength();
        }
        if ("center_x".equals(propertyId)) {
            return context.center().x();
        }
        if ("center_y".equals(propertyId)) {
            return context.center().y();
        }
        if ("center_z".equals(propertyId)) {
            return context.center().z();
        }
        if ("bbox_x".equals(propertyId)) {
            return context.sizeX();
        }
        if ("bbox_y".equals(propertyId)) {
            return context.sizeY();
        }
        if ("bbox_z".equals(propertyId)) {
            return context.sizeZ();
        }
        if ("face_count".equals(propertyId)) {
            return (double) summary.faceCount();
        }
        if ("edge_count".equals(propertyId)) {
            return (double) summary.edgeCount();
        }
        if ("representation_count".equals(propertyId)) {
            return (double) context.representationCount();
        }
        if ("instance_count".equals(propertyId)) {
            return (double) context.instanceCount();
        }
        return null;
    }
}