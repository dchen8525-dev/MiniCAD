package com.minicad.export.mesh;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Vector3;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.Loop;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.OrientedEdge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Handles planar face triangulation for mesh export.
 * Extracted from StepMeshExporter.Triangulator for better code organization.
 */
final class MeshTriangulatorPlanar {

    private static final double PLANAR_EPS = 1e-9;
    private static final int DEFAULT_CURVE_SEGMENTS = 32;

    private MeshTriangulatorPlanar() {
    }

    /**
     * Triangulates a planar face using ear clipping algorithm.
     *
     * @param face the face to triangulate
     * @param plane the plane containing the face
     * @param flipped whether to flip the face normal
     * @param addVertex callback to add a vertex, returns vertex index
     * @param addTriangle callback to add a triangle (three vertex indices)
     */
    static void triangulatePlanarFace(
            Face face,
            Plane plane,
            boolean flipped,
            BiFunction<CartesianPoint, Vector3, Integer> addVertex,
            Consumer<int[]> addTriangle
    ) {
        FaceBound outer = face.outerBound();
        if (outer == null) return;
        Vector3 normal = plane.normal().asVector();
        if (flipped) normal = normal.negate();
        PlanarFrame frame = PlanarFrame.forPlane(plane);
        List<ProjectedLoop> loops = new ArrayList<>();
        for (FaceBound bound : face.bounds()) {
            List<CartesianPoint> loopPoints = extractLoopPoints(bound.loop());
            if (loopPoints.size() < 3) {
                continue;
            }
            ProjectedLoop projected = projectLoop(loopPoints, frame, bound.outer());
            if (projected.vertices().size() >= 3) {
                loops.add(projected);
            }
        }
        if (loops.isEmpty()) {
            return;
        }
        List<ProjectedVertex> polygon = buildSimplePolygon(loops);
        if (polygon.size() < 3) {
            return;
        }
        for (int[] triangle : earClip(polygon)) {
            int v0 = addVertex.apply(polygon.get(triangle[0]).point(), normal);
            int v1 = addVertex.apply(polygon.get(triangle[1]).point(), normal);
            int v2 = addVertex.apply(polygon.get(triangle[2]).point(), normal);
            if (flipped) {
                addTriangle.accept(new int[]{v0, v2, v1});
            } else {
                addTriangle.accept(new int[]{v0, v1, v2});
            }
        }
    }

    private static List<CartesianPoint> extractLoopPoints(Loop loop) {
        if (loop instanceof PolyLoop) {
            PolyLoop poly = (PolyLoop) loop;
            return poly.points();
        }
        if (loop instanceof EdgeLoop) {
            EdgeLoop edgeLoop = (EdgeLoop) loop;
            List<CartesianPoint> points = new ArrayList<>();
            for (OrientedEdge oe : edgeLoop.edges()) {
                List<CartesianPoint> edgePoints = orientSamples(oe, oe.edge().curve().sample(DEFAULT_CURVE_SEGMENTS));
                int startIndex = points.isEmpty() ? 0 : 1;
                for (int i = startIndex; i < edgePoints.size(); i++) {
                    points.add(edgePoints.get(i));
                }
            }
            if (points.size() > 2 && points.get(0).equals(points.get(points.size() - 1))) {
                points.remove(points.size() - 1);
            }
            return points;
        }
        return List.of();
    }

    private static List<CartesianPoint> orientSamples(OrientedEdge orientedEdge, List<CartesianPoint> samples) {
        if (samples.isEmpty()) {
            return List.of(
                    orientedEdge.startVertex().point(),
                    orientedEdge.endVertex().point()
            );
        }
        List<CartesianPoint> oriented = new ArrayList<>(samples);
        CartesianPoint expectedStart = orientedEdge.startVertex().point();
        CartesianPoint expectedEnd = orientedEdge.endVertex().point();
        double forward = samples.get(0).distanceTo(expectedStart) + samples.get(samples.size() - 1).distanceTo(expectedEnd);
        double backward = samples.get(0).distanceTo(expectedEnd) + samples.get(samples.size() - 1).distanceTo(expectedStart);
        if (backward < forward) {
            Collections.reverse(oriented);
        }
        if (!oriented.get(0).equals(expectedStart)) {
            oriented.set(0, expectedStart);
        }
        if (!oriented.get(oriented.size() - 1).equals(expectedEnd)) {
            oriented.set(oriented.size() - 1, expectedEnd);
        }
        return List.copyOf(oriented);
    }

    private static ProjectedLoop projectLoop(List<CartesianPoint> points, PlanarFrame frame, boolean outer) {
        List<ProjectedVertex> projected = new ArrayList<>();
        for (CartesianPoint point : points) {
            ProjectedVertex vertex = frame.project(point);
            if (projected.isEmpty() || !samePoint(projected.get(projected.size() - 1), vertex)) {
                projected.add(vertex);
            }
        }
        if (projected.size() > 1 && samePoint(projected.get(0), projected.get(projected.size() - 1))) {
            projected.remove(projected.size() - 1);
        }
        removeCollinear(projected);
        return new ProjectedLoop(projected, outer);
    }

    private static List<ProjectedVertex> buildSimplePolygon(List<ProjectedLoop> loops) {
        ProjectedLoop outer = loops.stream().filter(ProjectedLoop::outer).findFirst().orElse(null);
        if (outer == null) {
            return List.of();
        }
        List<ProjectedVertex> polygon = ensureOrientation(outer.vertices(), true);
        for (ProjectedLoop hole : loops) {
            if (hole.outer()) {
                continue;
            }
            polygon = mergeHole(polygon, ensureOrientation(hole.vertices(), false));
            if (polygon.isEmpty()) {
                return List.of();
            }
        }
        removeCollinear(polygon);
        return polygon;
    }

    private static List<ProjectedVertex> ensureOrientation(List<ProjectedVertex> vertices, boolean ccw) {
        List<ProjectedVertex> oriented = new ArrayList<>(vertices);
        boolean isCcw = signedArea(oriented) > 0.0;
        if (isCcw != ccw) {
            Collections.reverse(oriented);
        }
        return oriented;
    }

    private static List<ProjectedVertex> mergeHole(List<ProjectedVertex> outer, List<ProjectedVertex> hole) {
        if (outer.size() < 3 || hole.size() < 3) {
            return outer;
        }
        int holeIndex = rightmostIndex(hole);
        ProjectedVertex holeVertex = hole.get(holeIndex);
        int outerIndex = findVisibleOuterVertex(outer, holeVertex, List.of(outer, hole));
        if (outerIndex < 0) {
            return List.of();
        }
        List<ProjectedVertex> merged = new ArrayList<>(outer.size() + hole.size() + 2);
        for (int i = 0; i <= outerIndex; i++) {
            merged.add(outer.get(i));
        }
        merged.add(holeVertex);
        for (int i = 1; i < hole.size(); i++) {
            merged.add(hole.get((holeIndex + i) % hole.size()));
        }
        merged.add(holeVertex);
        merged.add(outer.get(outerIndex));
        for (int i = outerIndex + 1; i < outer.size(); i++) {
            merged.add(outer.get(i));
        }
        return merged;
    }

    private static int findVisibleOuterVertex(
            List<ProjectedVertex> outer,
            ProjectedVertex holeVertex,
            List<List<ProjectedVertex>> loops
    ) {
        int bestIndex = -1;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < outer.size(); i++) {
            ProjectedVertex candidate = outer.get(i);
            if (candidate.u() + PLANAR_EPS < holeVertex.u()) {
                continue;
            }
            if (!isVisibleBridge(holeVertex, candidate, loops)) {
                continue;
            }
            double distance = distanceSquared(holeVertex, candidate);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    private static boolean isVisibleBridge(
            ProjectedVertex a,
            ProjectedVertex b,
            List<List<ProjectedVertex>> loops
    ) {
        if (samePoint(a, b)) {
            return false;
        }
        for (List<ProjectedVertex> loop : loops) {
            for (int i = 0; i < loop.size(); i++) {
                ProjectedVertex p = loop.get(i);
                ProjectedVertex q = loop.get((i + 1) % loop.size());
                if (samePoint(a, p) || samePoint(a, q) || samePoint(b, p) || samePoint(b, q)) {
                    continue;
                }
                if (segmentsIntersect(a, b, p, q)) {
                    return false;
                }
            }
        }
        ProjectedVertex midpoint = new ProjectedVertex(
                a.point().interpolate(b.point(), 0.5),
                (a.u() + b.u()) * 0.5,
                (a.v() + b.v()) * 0.5
        );
        if (!containsPoint(loops.get(0), midpoint)) {
            return false;
        }
        for (int i = 1; i < loops.size(); i++) {
            if (containsPoint(loops.get(i), midpoint)) {
                return false;
            }
        }
        return true;
    }

    private static List<int[]> earClip(List<ProjectedVertex> polygon) {
        List<int[]> triangles = new ArrayList<>();
        List<Integer> indices = new ArrayList<>(polygon.size());
        for (int i = 0; i < polygon.size(); i++) {
            indices.add(i);
        }
        int guard = 0;
        while (indices.size() > 3 && guard < polygon.size() * polygon.size()) {
            boolean clipped = false;
            for (int i = 0; i < indices.size(); i++) {
                int prev = indices.get((i - 1 + indices.size()) % indices.size());
                int curr = indices.get(i);
                int next = indices.get((i + 1) % indices.size());
                if (!isEar(prev, curr, next, indices, polygon)) {
                    continue;
                }
                triangles.add(new int[]{prev, curr, next});
                indices.remove(i);
                clipped = true;
                break;
            }
            if (!clipped) {
                break;
            }
            guard++;
        }
        if (indices.size() == 3) {
            triangles.add(new int[]{indices.get(0), indices.get(1), indices.get(2)});
        }
        return triangles;
    }

    private static boolean isEar(
            int prev,
            int curr,
            int next,
            List<Integer> polygonIndices,
            List<ProjectedVertex> polygon
    ) {
        ProjectedVertex a = polygon.get(prev);
        ProjectedVertex b = polygon.get(curr);
        ProjectedVertex c = polygon.get(next);
        if (cross(a, b, c) <= PLANAR_EPS) {
            return false;
        }
        for (int other : polygonIndices) {
            if (other == prev || other == curr || other == next) {
                continue;
            }
            ProjectedVertex point = polygon.get(other);
            if (samePoint(point, a) || samePoint(point, b) || samePoint(point, c)) {
                continue;
            }
            if (pointInTriangle(point, a, b, c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean pointInTriangle(ProjectedVertex p, ProjectedVertex a, ProjectedVertex b, ProjectedVertex c) {
        double c1 = cross(a, b, p);
        double c2 = cross(b, c, p);
        double c3 = cross(c, a, p);
        boolean hasNegative = c1 < -PLANAR_EPS || c2 < -PLANAR_EPS || c3 < -PLANAR_EPS;
        boolean hasPositive = c1 > PLANAR_EPS || c2 > PLANAR_EPS || c3 > PLANAR_EPS;
        return !(hasNegative && hasPositive);
    }

    private static boolean containsPoint(List<ProjectedVertex> polygon, ProjectedVertex point) {
        boolean inside = false;
        for (int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
            ProjectedVertex a = polygon.get(i);
            ProjectedVertex b = polygon.get(j);
            if (isOnSegment(a, b, point)) {
                return true;
            }
            boolean intersects = ((a.v() > point.v()) != (b.v() > point.v()))
                    && point.u() < (b.u() - a.u()) * (point.v() - a.v()) / (b.v() - a.v()) + a.u();
            if (intersects) {
                inside = !inside;
            }
        }
        return inside;
    }

    private static boolean segmentsIntersect(ProjectedVertex a, ProjectedVertex b, ProjectedVertex c, ProjectedVertex d) {
        double abC = cross(a, b, c);
        double abD = cross(a, b, d);
        double cdA = cross(c, d, a);
        double cdB = cross(c, d, b);
        if (((abC > PLANAR_EPS && abD < -PLANAR_EPS) || (abC < -PLANAR_EPS && abD > PLANAR_EPS))
                && ((cdA > PLANAR_EPS && cdB < -PLANAR_EPS) || (cdA < -PLANAR_EPS && cdB > PLANAR_EPS))) {
            return true;
        }
        return (Math.abs(abC) <= PLANAR_EPS && isOnSegment(a, b, c))
                || (Math.abs(abD) <= PLANAR_EPS && isOnSegment(a, b, d))
                || (Math.abs(cdA) <= PLANAR_EPS && isOnSegment(c, d, a))
                || (Math.abs(cdB) <= PLANAR_EPS && isOnSegment(c, d, b));
    }

    private static boolean isOnSegment(ProjectedVertex a, ProjectedVertex b, ProjectedVertex p) {
        if (Math.abs(cross(a, b, p)) > PLANAR_EPS) {
            return false;
        }
        return p.u() >= Math.min(a.u(), b.u()) - PLANAR_EPS
                && p.u() <= Math.max(a.u(), b.u()) + PLANAR_EPS
                && p.v() >= Math.min(a.v(), b.v()) - PLANAR_EPS
                && p.v() <= Math.max(a.v(), b.v()) + PLANAR_EPS;
    }

    private static int rightmostIndex(List<ProjectedVertex> vertices) {
        int index = 0;
        for (int i = 1; i < vertices.size(); i++) {
            ProjectedVertex candidate = vertices.get(i);
            ProjectedVertex best = vertices.get(index);
            if (candidate.u() > best.u() + PLANAR_EPS
                    || (Math.abs(candidate.u() - best.u()) <= PLANAR_EPS && candidate.v() < best.v())) {
                index = i;
            }
        }
        return index;
    }

    private static double signedArea(List<ProjectedVertex> vertices) {
        double area = 0.0;
        for (int i = 0; i < vertices.size(); i++) {
            ProjectedVertex a = vertices.get(i);
            ProjectedVertex b = vertices.get((i + 1) % vertices.size());
            area += a.u() * b.v() - b.u() * a.v();
        }
        return area * 0.5;
    }

    private static void removeCollinear(List<ProjectedVertex> vertices) {
        int index = 0;
        while (vertices.size() >= 3 && index < vertices.size()) {
            ProjectedVertex prev = vertices.get((index - 1 + vertices.size()) % vertices.size());
            ProjectedVertex curr = vertices.get(index);
            ProjectedVertex next = vertices.get((index + 1) % vertices.size());
            if (samePoint(prev, curr) || Math.abs(cross(prev, curr, next)) <= PLANAR_EPS) {
                vertices.remove(index);
                if (index > 0) {
                    index--;
                }
                continue;
            }
            index++;
        }
    }

    private static boolean samePoint(ProjectedVertex a, ProjectedVertex b) {
        return Math.abs(a.u() - b.u()) <= PLANAR_EPS && Math.abs(a.v() - b.v()) <= PLANAR_EPS;
    }

    private static double cross(ProjectedVertex a, ProjectedVertex b, ProjectedVertex c) {
        return (b.u() - a.u()) * (c.v() - a.v()) - (b.v() - a.v()) * (c.u() - a.u());
    }

    private static double distanceSquared(ProjectedVertex a, ProjectedVertex b) {
        double du = a.u() - b.u();
        double dv = a.v() - b.v();
        return du * du + dv * dv;
    }

    // --- Inner classes ---

    static final class ProjectedVertex {
        private final CartesianPoint point;
        private final double u;
        private final double v;

        ProjectedVertex(CartesianPoint point, double u, double v) {
            this.point = point;
            this.u = u;
            this.v = v;
        }

        CartesianPoint point() { return point; }
        double u() { return u; }
        double v() { return v; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProjectedVertex that = (ProjectedVertex) o;
            return Objects.equals(point, that.point) && Double.compare(u, that.u) == 0 && Double.compare(v, that.v) == 0;
        }

        @Override
        public int hashCode() { return Objects.hash(point, u, v); }
    }

    static final class ProjectedLoop {
        private final List<ProjectedVertex> vertices;
        private final boolean outer;

        ProjectedLoop(List<ProjectedVertex> vertices, boolean outer) {
            this.vertices = vertices == null ? null : List.copyOf(vertices);
            this.outer = outer;
        }

        List<ProjectedVertex> vertices() { return vertices; }
        boolean outer() { return outer; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProjectedLoop that = (ProjectedLoop) o;
            return outer == that.outer && Objects.equals(vertices, that.vertices);
        }

        @Override
        public int hashCode() { return Objects.hash(vertices, outer); }
    }

    static final class PlanarFrame {
        private final CartesianPoint origin;
        private final Vector3 xAxis;
        private final Vector3 yAxis;

        PlanarFrame(CartesianPoint origin, Vector3 xAxis, Vector3 yAxis) {
            this.origin = origin;
            this.xAxis = xAxis;
            this.yAxis = yAxis;
        }

        CartesianPoint origin() { return origin; }
        Vector3 xAxis() { return xAxis; }
        Vector3 yAxis() { return yAxis; }

        static PlanarFrame forPlane(Plane plane) {
            Vector3 normal = plane.normal().asVector();
            Vector3 xSeed = Math.abs(normal.x()) < 0.9 ? new Vector3(1, 0, 0) : new Vector3(0, 1, 0);
            Vector3 xAxis = normal.cross(xSeed).normalize().asVector();
            Vector3 yAxis = normal.cross(xAxis).normalize().asVector();
            return new PlanarFrame(plane.origin(), xAxis, yAxis);
        }

        ProjectedVertex project(CartesianPoint point) {
            Vector3 offset = point.subtract(origin);
            return new ProjectedVertex(point, offset.dot(xAxis), offset.dot(yAxis));
        }
    }
}