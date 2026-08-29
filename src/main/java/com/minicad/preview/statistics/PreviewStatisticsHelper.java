package com.minicad.preview.statistics;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.minicad.helper.ShellHelper;
import com.minicad.preview.payload.ParametricLoopPayload;
import com.minicad.preview.payload.UvBounds;
import com.minicad.preview.payload.UnsupportedBooleanPayload;
import com.minicad.preview.payload.UnsupportedFacePayload;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepBooleanClippingResult;
import com.minicad.step.model.StepBooleanResult;
import com.minicad.step.model.StepBrepWithVoids;
import com.minicad.step.model.StepComplexClippingResult;
import com.minicad.step.model.StepCsgPrimitive;
import com.minicad.step.model.StepCsgSolid;
import com.minicad.step.model.StepCsgVolume;
import com.minicad.step.model.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.StepHalfSpaceSolid;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepPolygonalBoundedHalfSpace;
import com.minicad.step.model.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.StepSolidReplica;
import com.minicad.step.model.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.StepSweptAreaSolid;
import com.minicad.step.model.StepSweptDiskSolid;
import com.minicad.step.model.StepBlockVolume;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper methods for preview statistics and summarization.
 * Extracted from StepPreviewJsonExporter for better code organization.
 */
public final class PreviewStatisticsHelper {

    private PreviewStatisticsHelper() {
        // Static helper class - no instances
    }

    public static int countEntities(Map<Integer, StepEntity> resolved, Class<? extends StepEntity> type) {
        int count = 0;
        for (StepEntity entity : resolved.values()) {
            if (type.isInstance(entity)) {
                count++;
            }
        }
        return count;
    }

    public static int countSolidEntities(Map<Integer, StepEntity> resolved) {
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

    public static String summarizeUnsupportedFacesBySurfaceType(List<UnsupportedFacePayload> unsupportedFaces) {
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

    public static String summarizeUnsupportedFacesByReason(List<UnsupportedFacePayload> unsupportedFaces) {
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

    public static String summarizeUnsupportedBooleansByType(List<UnsupportedBooleanPayload> unsupportedBooleans) {
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

    public static String summarizeUnsupportedBooleansByReason(List<UnsupportedBooleanPayload> unsupportedBooleans) {
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

    public static String summarizeLoopPointCounts(List<ParametricLoopPayload> loops) {
        return loops.stream()
                .map(loop -> (loop.outer() ? "outer" : "inner") + ":" + loop.points().size())
                .collect(Collectors.joining("|"));
    }

    public static String formatUvBounds(UvBounds bounds) {
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

    public static int countShells(Map<Integer, StepEntity> resolved) {
        int count = 0;
        for (StepEntity entity : resolved.values()) {
            if (ShellHelper.isShellLikeEntity(entity)) {
                count++;
            }
        }
        return count;
    }
}
