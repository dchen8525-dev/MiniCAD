package com.minicad.preview.payload;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PayloadReductionHelper {
    private static final Logger log = LoggerFactory.getLogger(PayloadReductionHelper.class);
    static final int MAX_TOTAL_TRIANGLE_POINTS = 1000000;
    static final int MAX_TOTAL_LOOP_POINTS = 500000;
    static PreviewPayload reducePayloadGeometry(PreviewPayload payload) {
        return reducePayloadGeometry(payload, MAX_TOTAL_TRIANGLE_POINTS, MAX_TOTAL_LOOP_POINTS, "payload_geometry_reduced");
    }

    static PreviewPayload reducePayloadGeometry(
            PreviewPayload payload,
            int maxTrianglePoints,
            int maxLoopPoints,
            String reductionStage
    ) {
        int trianglePoints = countTrianglePoints(payload);
        int loopPoints = countLoopPoints(payload);
        int triangleFactor = Math.max(1, (int) Math.ceil(trianglePoints / (double) maxTrianglePoints));
        int loopFactor = Math.max(1, (int) Math.ceil(loopPoints / (double) maxLoopPoints));
        if (triangleFactor == 1 && loopFactor == 1) {
            return payload;
        }
        List<FacePayload> faces = payload.faces().stream()
                .map(face -> reduceFacePayload(face, triangleFactor, loopFactor))
                .collect(Collectors.toList());
        List<RepresentationPayload> representations = payload.representations().stream()
                .map(representation -> new RepresentationPayload(
                        representation.id(),
                        representation.name(),
                        representation.layers(),
                        representation.color(),
                        representation.edges(),
                        representation.faces().stream()
                                .map(face -> reduceFacePayload(face, triangleFactor, loopFactor))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        PreviewPayload reduced = new PreviewPayload(
                payload.stats(),
                payload.bounds(),
                payload.validation(),
                payload.product(),
                payload.units(),
                payload.pmi(),
                payload.issues(),
                payload.unsupportedBooleans(),
                payload.unsupportedFaces(),
                payload.edges(),
                faces,
                representations,
                payload.instances()
        );
        log.info("stage={} originalTrianglePoints={}, reducedTrianglePoints={}, originalLoopPoints={}, reducedLoopPoints={}, triangleFactor={}, loopFactor={}, maxTrianglePoints={}, maxLoopPoints={}",
                reductionStage,
                trianglePoints,
                countTrianglePoints(reduced),
                loopPoints,
                countLoopPoints(reduced),
                triangleFactor,
                loopFactor,
                maxTrianglePoints,
                maxLoopPoints);
        return reduced;
    }

    static FacePayload reduceFacePayload(FacePayload face, int triangleFactor, int loopFactor) {
        return new FacePayload(
                face.stepId(),
                face.name(),
                face.surfaceType(),
                face.origin(),
                face.normal(),
                face.sameSense(),
                face.color(),
                face.transparency(),
                face.pbr(),
                face.layers(),
                reduceLoopPoints(face.loops(), loopFactor),
                reduceTrianglePoints(face.triangles(), triangleFactor),
                face.surface(),
                face.uvLoops()
        );
    }

    static List<PointPayload> reduceTrianglePoints(List<PointPayload> triangles, int factor) {
        if (factor <= 1 || triangles.size() <= 3) {
            return triangles;
        }
        int triangleCount = triangles.size() / 3;
        int sampleCount = Math.max(1, (triangleCount + factor - 1) / factor);
        List<PointPayload> reduced = new ArrayList<>(sampleCount * 3);
        int previousTriangleIndex = -1;
        for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
            int triangleIndex;
            if (sampleCount == 1) {
                triangleIndex = 0;
            } else {
                triangleIndex = (int) Math.round(sampleIndex * (triangleCount - 1) / (double) (sampleCount - 1));
            }
            if (triangleIndex == previousTriangleIndex) {
                continue;
            }
            previousTriangleIndex = triangleIndex;
            int base = triangleIndex * 3;
            reduced.add(triangles.get(base));
            reduced.add(triangles.get(base + 1));
            reduced.add(triangles.get(base + 2));
        }
        return List.copyOf(reduced);
    }

    static List<LoopPayload> reduceLoopPoints(List<LoopPayload> loops, int factor) {
        if (factor <= 1) {
            return loops;
        }
        List<LoopPayload> reduced = new ArrayList<>(loops.size());
        for (LoopPayload loop : loops) {
            if (loop.points().size() <= 2) {
                reduced.add(loop);
                continue;
            }
            List<PointPayload> points = new ArrayList<>(Math.max(2, loop.points().size() / factor));
            for (int index = 0; index < loop.points().size(); index += factor) {
                points.add(loop.points().get(index));
            }
            PointPayload last = loop.points().get(loop.points().size() - 1);
            if (!points.get(points.size() - 1).equals(last)) {
                points.add(last);
            }
            reduced.add(new LoopPayload(loop.outer(), List.copyOf(points)));
        }
        return List.copyOf(reduced);
    }

    static int countTrianglePoints(PreviewPayload payload) {
        int count = payload.faces().stream().mapToInt(face -> face.triangles().size()).sum();
        count += payload.representations().stream()
                .flatMap(representation -> representation.faces().stream())
                .mapToInt(face -> face.triangles().size())
                .sum();
        return count;
    }

    static int countLoopPoints(PreviewPayload payload) {
        int count = payload.faces().stream()
                .flatMap(face -> face.loops().stream())
                .mapToInt(loop -> loop.points().size())
                .sum();
        count += payload.representations().stream()
                .flatMap(representation -> representation.faces().stream())
                .flatMap(face -> face.loops().stream())
                .mapToInt(loop -> loop.points().size())
                .sum();
        return count;
    }

    static int countEdgePoints(PreviewPayload payload) {
        int count = payload.edges().stream().mapToInt(edge -> edge.points().size()).sum();
        count += payload.representations().stream()
                .flatMap(representation -> representation.edges().stream())
                .mapToInt(edge -> edge.points().size())
                .sum();
        return count;
    }

    static int countPmiPoints(PreviewPayload payload) {
        return payload.pmi().stream().mapToInt(item -> item.leader().size() + 1).sum();
    }
}
