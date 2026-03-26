package com.minicad.app;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import com.minicad.topology.Edge;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.Shell;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Converts supported STEP topology into a small JSON payload for the browser viewer.
 */
public final class StepPreviewJsonExporter {

    private StepPreviewJsonExporter() {
    }

    /**
     * Parses STEP text and exports a preview payload.
     *
     * @param stepText STEP file contents
     * @return JSON payload for the browser viewer
     */
    public static String export(String stepText) {
        StepFile stepFile = StepParser.parse(stepText);
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        PreviewPayload payload = buildPayload(stepFile, resolved, builder);
        return toJson(payload);
    }

    private static PreviewPayload buildPayload(
            StepFile stepFile,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        Set<Integer> shellIds = new TreeSet<>();
        int solidCount = 0;

        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepOpenShell openShell) {
                shellIds.add(openShell.id());
            } else if (entity instanceof StepClosedShell closedShell) {
                shellIds.add(closedShell.id());
            } else if (entity instanceof StepManifoldSolidBrep solidBrep) {
                shellIds.add(solidBrep.outer().id());
                solidCount++;
            }
        }

        List<FacePayload> faces = new ArrayList<>();
        Set<Edge> uniqueEdges = new LinkedHashSet<>();
        BoundsAccumulator bounds = new BoundsAccumulator();

        for (Integer shellId : shellIds) {
            Shell shell = builder.buildShell(shellId);
            for (Face face : shell.faces()) {
                faces.add(toFacePayload(face, bounds));
                for (FaceBound bound : face.bounds()) {
                    for (OrientedEdge orientedEdge : bound.loop().edges()) {
                        uniqueEdges.add(orientedEdge.edge());
                    }
                }
            }
        }

        List<EdgePayload> edges = new ArrayList<>();
        for (Edge edge : uniqueEdges) {
            CartesianPoint start = edge.start().point();
            CartesianPoint end = edge.end().point();
            bounds.include(start);
            bounds.include(end);
            edges.add(new EdgePayload(toPointPayload(start), toPointPayload(end)));
        }

        PreviewStats stats = new PreviewStats(stepFile.entities().size(), solidCount, shellIds.size(), faces.size(), edges.size());
        BoundsPayload boundsPayload = bounds.toPayload();
        return new PreviewPayload(stats, boundsPayload, edges, faces);
    }

    private static FacePayload toFacePayload(Face face, BoundsAccumulator bounds) {
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : face.bounds()) {
            List<PointPayload> points = new ArrayList<>();
            for (OrientedEdge orientedEdge : bound.loop().edges()) {
                CartesianPoint point = orientedEdge.startVertex().point();
                bounds.include(point);
                points.add(toPointPayload(point));
            }
            loops.add(new LoopPayload(bound.outer(), points));
        }
        Direction3 normal = face.surface().normal();
        return new FacePayload(
                toPointPayload(face.surface().origin()),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                face.sameSense(),
                loops
        );
    }

    private static PointPayload toPointPayload(CartesianPoint point) {
        return new PointPayload(point.x(), point.y(), point.z());
    }

    private static String toJson(PreviewPayload payload) {
        StringBuilder json = new StringBuilder(2048);
        json.append('{');
        json.append("\"stats\":");
        appendStats(json, payload.stats());
        json.append(",\"bounds\":");
        appendBounds(json, payload.bounds());
        json.append(",\"edges\":");
        appendEdges(json, payload.edges());
        json.append(",\"faces\":");
        appendFaces(json, payload.faces());
        json.append('}');
        return json.toString();
    }

    private static void appendStats(StringBuilder json, PreviewStats stats) {
        json.append('{');
        json.append("\"entityCount\":").append(stats.entityCount());
        json.append(",\"solidCount\":").append(stats.solidCount());
        json.append(",\"shellCount\":").append(stats.shellCount());
        json.append(",\"faceCount\":").append(stats.faceCount());
        json.append(",\"edgeCount\":").append(stats.edgeCount());
        json.append('}');
    }

    private static void appendBounds(StringBuilder json, BoundsPayload bounds) {
        json.append('{');
        json.append("\"min\":");
        appendPoint(json, bounds.min());
        json.append(",\"max\":");
        appendPoint(json, bounds.max());
        json.append('}');
    }

    private static void appendEdges(StringBuilder json, List<EdgePayload> edges) {
        json.append('[');
        for (int i = 0; i < edges.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            EdgePayload edge = edges.get(i);
            json.append('{');
            json.append("\"start\":");
            appendPoint(json, edge.start());
            json.append(",\"end\":");
            appendPoint(json, edge.end());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendFaces(StringBuilder json, List<FacePayload> faces) {
        json.append('[');
        for (int i = 0; i < faces.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            FacePayload face = faces.get(i);
            json.append('{');
            json.append("\"origin\":");
            appendPoint(json, face.origin());
            json.append(",\"normal\":");
            appendVector(json, face.normal());
            json.append(",\"sameSense\":").append(face.sameSense());
            json.append(",\"loops\":");
            appendLoops(json, face.loops());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendLoops(StringBuilder json, List<LoopPayload> loops) {
        json.append('[');
        for (int i = 0; i < loops.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            LoopPayload loop = loops.get(i);
            json.append('{');
            json.append("\"outer\":").append(loop.outer());
            json.append(",\"points\":");
            appendPoints(json, loop.points());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendPoints(StringBuilder json, List<PointPayload> points) {
        json.append('[');
        for (int i = 0; i < points.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            appendPoint(json, points.get(i));
        }
        json.append(']');
    }

    private static void appendPoint(StringBuilder json, PointPayload point) {
        json.append('[')
                .append(format(point.x()))
                .append(',')
                .append(format(point.y()))
                .append(',')
                .append(format(point.z()))
                .append(']');
    }

    private static void appendVector(StringBuilder json, VectorPayload vector) {
        json.append('[')
                .append(format(vector.x()))
                .append(',')
                .append(format(vector.y()))
                .append(',')
                .append(format(vector.z()))
                .append(']');
    }

    private static String format(double value) {
        return Double.toString(value);
    }

    private record PreviewPayload(PreviewStats stats, BoundsPayload bounds, List<EdgePayload> edges, List<FacePayload> faces) {
    }

    private record PreviewStats(int entityCount, int solidCount, int shellCount, int faceCount, int edgeCount) {
    }

    private record BoundsPayload(PointPayload min, PointPayload max) {
    }

    private record EdgePayload(PointPayload start, PointPayload end) {
    }

    private record FacePayload(PointPayload origin, VectorPayload normal, boolean sameSense, List<LoopPayload> loops) {
    }

    private record LoopPayload(boolean outer, List<PointPayload> points) {
    }

    private record PointPayload(double x, double y, double z) {
    }

    private record VectorPayload(double x, double y, double z) {
    }

    private static final class BoundsAccumulator {
        private double minX = Double.POSITIVE_INFINITY;
        private double minY = Double.POSITIVE_INFINITY;
        private double minZ = Double.POSITIVE_INFINITY;
        private double maxX = Double.NEGATIVE_INFINITY;
        private double maxY = Double.NEGATIVE_INFINITY;
        private double maxZ = Double.NEGATIVE_INFINITY;

        void include(CartesianPoint point) {
            minX = Math.min(minX, point.x());
            minY = Math.min(minY, point.y());
            minZ = Math.min(minZ, point.z());
            maxX = Math.max(maxX, point.x());
            maxY = Math.max(maxY, point.y());
            maxZ = Math.max(maxZ, point.z());
        }

        BoundsPayload toPayload() {
            if (!Double.isFinite(minX)) {
                PointPayload zero = new PointPayload(0.0, 0.0, 0.0);
                return new BoundsPayload(zero, zero);
            }
            return new BoundsPayload(
                    new PointPayload(minX, minY, minZ),
                    new PointPayload(maxX, maxY, maxZ)
            );
        }
    }
}
