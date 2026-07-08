package com.minicad.preview.statistics;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.product.StepBooleanClippingResult;
import com.minicad.step.model.product.StepBooleanResult;
import com.minicad.step.model.product.StepBrepWithVoids;
import com.minicad.step.model.product.StepComplexClippingResult;
import com.minicad.step.model.product.StepCsgPrimitive;
import com.minicad.step.model.product.StepCsgSolid;
import com.minicad.step.model.product.StepCsgVolume;
import com.minicad.step.model.product.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.product.StepHalfSpaceSolid;
import com.minicad.step.model.product.StepManifoldSolidBrep;
import com.minicad.step.model.product.StepPolygonalBoundedHalfSpace;
import com.minicad.step.model.product.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.product.StepSolidReplica;
import com.minicad.step.model.product.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.product.StepSweptAreaSolid;
import com.minicad.step.model.product.StepSweptDiskSolid;
import com.minicad.step.model.product.StepBlockVolume;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper methods for preview statistics and summarization.
 * Extracted from StepPreviewJsonExporter for better code organization.
 */
final class PreviewStatisticsHelper {

    private PreviewStatisticsHelper() {
        // Static helper class - no instances
    }

    static int countEntities(Map<Integer, StepEntity> resolved, Class<? extends StepEntity> type) {
        int count = 0;
        for (StepEntity entity : resolved.values()) {
            if (type.isInstance(entity)) {
                count++;
            }
        }
        return count;
    }

    static int countSolidEntities(Map<Integer, StepEntity> resolved) {
        int count = 0;
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepManifoldSolidBrep
                    || entity instanceof StepBrepWithVoids
                    || entity instanceof StepSweptAreaSolid
                    || entity instanceof StepSolidReplica
                    || entity instanceof StepCsgSolid
                    || entity instanceof StepCsgPrimitive
                    || entity instanceof StepBooleanClippingResult
                    || entity instanceof StepBooleanResult
                    || entity instanceof StepSweptDiskSolid
                    || entity instanceof StepExtrudedAreaSolidTapered
                    || entity instanceof StepRevolvedAreaSolidTapered
                    || entity instanceof StepSurfaceCurveSweptAreaSolid
                    || entity instanceof StepPolygonalBoundedHalfSpace
                    || entity instanceof StepComplexClippingResult
                    || entity instanceof StepHalfSpaceSolid
                    || entity instanceof StepCsgVolume
                    || entity instanceof StepBlockVolume) {
                count++;
            }
        }
        return count;
    }

    static String summarizeUnsupportedFacesBySurfaceType(List<UnsupportedFacePayload> unsupportedFaces) {
        Map<String, Long> counts = unsupportedFaces.stream()
                .collect(Collectors.groupingBy(
                        face -> face.surfaceType() == null ? "UNKNOWN" : face.surfaceType(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    static String summarizeUnsupportedFacesByReason(List<UnsupportedFacePayload> unsupportedFaces) {
        Map<String, Long> counts = unsupportedFaces.stream()
                .collect(Collectors.groupingBy(
                        face -> face.reason() == null ? "unknown" : face.reason(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    static String summarizeUnsupportedBooleansByType(List<UnsupportedBooleanPayload> unsupportedBooleans) {
        Map<String, Long> counts = unsupportedBooleans.stream()
                .collect(Collectors.groupingBy(
                        UnsupportedBooleanPayload::type,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    static String summarizeUnsupportedBooleansByReason(List<UnsupportedBooleanPayload> unsupportedBooleans) {
        Map<String, Long> counts = unsupportedBooleans.stream()
                .collect(Collectors.groupingBy(
                        item -> item.reason() == null ? "unknown" : item.reason(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    static String summarizeLoopPointCounts(List<ParametricLoopPayload> loops) {
        return loops.stream()
                .map(loop -> (loop.outer() ? "outer" : "inner") + ":" + loop.points().size())
                .collect(Collectors.joining("|"));
    }

    static String formatUvBounds(UvBounds bounds) {
        return String.format(
                "(minU=%.6f,minV=%.6f,maxU=%.6f,maxV=%.6f,uSpan=%.6f,vSpan=%.6f)",
                bounds.minU(),
                bounds.minV(),
                bounds.maxU(),
                bounds.maxV(),
                bounds.uSpan(),
                bounds.vSpan()
        );
    }

    static int countShells(Map<Integer, StepEntity> resolved) {
        int count = 0;
        for (StepEntity entity : resolved.values()) {
            if (ShellHelper.isShellLikeEntity(entity)) {
                count++;
            }
        }
        return count;
    }
}