package com.minicad.app;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepCylindricalSurface;
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
        int unsupportedFaceCount = 0;

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
            for (StepAdvancedFace stepFace : shellFaces(resolved.get(shellId))) {
                if (stepFace.faceGeometry() instanceof StepCylindricalSurface) {
                    unsupportedFaceCount++;
                    continue;
                }
                Face face = builder.buildFace(stepFace.id());
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
            List<CartesianPoint> polyline = sampleEdge(edge.start().point(), edge.end().point(), edge.curve(), edge.sameSense());
            includeAll(bounds, polyline);
            edges.add(new EdgePayload(toPointPayloads(polyline)));
        }

        PreviewStats stats = new PreviewStats(
                stepFile.entities().size(),
                solidCount,
                shellIds.size(),
                faces.size(),
                edges.size(),
                unsupportedFaceCount
        );
        BoundsPayload boundsPayload = bounds.toPayload();
        return new PreviewPayload(stats, boundsPayload, edges, faces);
    }

    private static List<StepAdvancedFace> shellFaces(StepEntity entity) {
        if (entity instanceof StepOpenShell openShell) {
            return openShell.faces();
        }
        if (entity instanceof StepClosedShell closedShell) {
            return closedShell.faces();
        }
        throw new UnsupportedGeometryException("preview export requires OPEN_SHELL or CLOSED_SHELL");
    }

    private static FacePayload toFacePayload(Face face, BoundsAccumulator bounds) {
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : face.bounds()) {
            List<CartesianPoint> loopPoints = sampleLoop(bound);
            includeAll(bounds, loopPoints);
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(loopPoints)));
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

    private static List<PointPayload> toPointPayloads(List<CartesianPoint> points) {
        return points.stream().map(StepPreviewJsonExporter::toPointPayload).toList();
    }

    private static void includeAll(BoundsAccumulator bounds, List<CartesianPoint> points) {
        for (CartesianPoint point : points) {
            bounds.include(point);
        }
    }

    private static List<CartesianPoint> sampleLoop(FaceBound bound) {
        List<CartesianPoint> sampled = new ArrayList<>();
        boolean firstEdge = true;
        for (OrientedEdge orientedEdge : bound.loop().edges()) {
            List<CartesianPoint> edgePoints = sampleOrientedEdge(orientedEdge);
            int startIndex = firstEdge ? 0 : 1;
            for (int i = startIndex; i < edgePoints.size(); i++) {
                sampled.add(edgePoints.get(i));
            }
            firstEdge = false;
        }
        if (!sampled.isEmpty() && sampled.getFirst().distanceTo(sampled.getLast()) > 1.0e-9) {
            sampled.add(sampled.getFirst());
        }
        return sampled;
    }

    private static List<CartesianPoint> sampleOrientedEdge(OrientedEdge orientedEdge) {
        Edge edge = orientedEdge.edge();
        boolean naturalForward = orientedEdge.orientation() ? edge.sameSense() : !edge.sameSense();
        return sampleEdge(
                orientedEdge.startVertex().point(),
                orientedEdge.endVertex().point(),
                edge.curve(),
                naturalForward
        );
    }

    private static List<CartesianPoint> sampleEdge(CartesianPoint start, CartesianPoint end, Curve3 curve, boolean naturalForward) {
        if (curve instanceof Line3) {
            return List.of(start, end);
        }
        if (curve instanceof Circle circle) {
            return sampleCircleArc(circle, start, end, naturalForward);
        }
        throw new UnsupportedGeometryException("preview export requires LINE or CIRCLE topology");
    }

    private static List<CartesianPoint> sampleCircleArc(
            Circle circle,
            CartesianPoint start,
            CartesianPoint end,
            boolean naturalForward
    ) {
        double startAngle = circle.angleOf(start);
        double endAngle = circle.angleOf(end);
        double delta = endAngle - startAngle;
        if (naturalForward) {
            if (delta < 0.0) {
                delta += Math.PI * 2.0;
            }
        } else if (delta > 0.0) {
            delta -= Math.PI * 2.0;
        }

        int segments = Math.max(8, (int) Math.ceil(Math.abs(delta) / (Math.PI / 12.0)));
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + delta * i / segments;
            points.add(circle.pointAt(angle));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return points;
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
        json.append(",\"unsupportedFaceCount\":").append(stats.unsupportedFaceCount());
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
            json.append("\"points\":");
            appendPoints(json, edge.points());
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

    private record PreviewStats(
            int entityCount,
            int solidCount,
            int shellCount,
            int faceCount,
            int edgeCount,
            int unsupportedFaceCount
    ) {
    }

    private record BoundsPayload(PointPayload min, PointPayload max) {
    }

    private record EdgePayload(List<PointPayload> points) {
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
