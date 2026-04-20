package com.minicad.app;

import com.minicad.geometry.*;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.step.model.StepEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import com.minicad.topology.*;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exports STEP files to OBJ and STL mesh formats.
 * Uses the existing STEP parser/resolver/builder pipeline, then triangulates
 * all faces and formats the result as OBJ or STL.
 */
public final class StepMeshExporter {

    private static final Logger LOG = Logger.getLogger(StepMeshExporter.class.getName());

    // STL binary layout constants
    private static final int STL_HEADER_SIZE = 80;
    private static final int STL_TRIANGLE_RECORD_SIZE = 50;
    private static final int STL_COUNT_SIZE = 4;

    // Tessellation defaults
    private static final int DEFAULT_CURVE_SEGMENTS = 32;
    private static final int MIN_CURVE_SEGMENTS = 8;
    private static final int MAX_CURVE_SEGMENTS = 64;
    private static final double BBOX_SEGMENT_MULTIPLIER = 10;

    // Vertex dedup precision
    private static final double VERTEX_ROUNDING = 1e9;

    // Degenerate triangle threshold
    private static final double MIN_TRIANGLE_AREA = 1e-12;

    private StepMeshExporter() {
    }

    /**
     * Exports STEP text to OBJ format.
     */
    public static String exportObj(String stepText) {
        MeshData mesh = buildMesh(stepText);
        return formatObj(mesh);
    }

    /**
     * Exports STEP text to binary STL format.
     */
    public static byte[] exportStlBinary(String stepText) {
        MeshData mesh = buildMesh(stepText);
        return formatStlBinary(mesh);
    }

    /**
     * Exports STEP text to text STL format.
     */
    public static String exportStlText(String stepText) {
        MeshData mesh = buildMesh(stepText);
        return formatStlText(mesh);
    }

    // ── Pipeline ──────────────────────────────────────────────────────────────

    private static MeshData buildMesh(String stepText) {
        StepFile stepFile = StepParser.parse(stepText);
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Triangulator t = new Triangulator();

        for (Map.Entry<Integer, StepEntity> entry : resolved.entrySet()) {
            StepEntity entity = entry.getValue();
            if (entity instanceof com.minicad.step.model.StepFaceEntity faceEntity) {
                try {
                    t.triangulateSemanticFace(faceEntity, builder);
                } catch (Exception e) {
                    LOG.log(Level.FINE, "Skipping semantic face #{0}: {1}", new Object[]{entry.getKey(), e.getMessage()});
                }
            }
        }

        for (Map.Entry<Integer, StepEntity> entry : resolved.entrySet()) {
            int id = entry.getKey();
            StepEntity entity = entry.getValue();
            if (isSemanticFaceBackedEntity(entity)) {
                continue;
            }
            if (builder.canBuildAsSolid(entity)) {
                try {
                    Solid solid = builder.buildSolid(id);
                    t.triangulateSolid(solid);
                } catch (Exception e) {
                    LOG.log(Level.FINE, "Skipping solid #{0}: {1}", new Object[]{id, e.getMessage()});
                }
            } else if (isShellCandidate(entity)) {
                try {
                    Shell shell = builder.buildShell(id);
                    t.triangulateShell(shell);
                } catch (Exception e) {
                    LOG.log(Level.FINE, "Skipping shell #{0}: {1}", new Object[]{id, e.getMessage()});
                }
            }
        }

        return t.toMeshData();
    }

    private static boolean isSemanticFaceBackedEntity(StepEntity entity) {
        return entity instanceof com.minicad.step.model.StepFaceEntity
                || entity instanceof com.minicad.step.model.StepOpenShell
                || entity instanceof com.minicad.step.model.StepClosedShell
                || entity instanceof com.minicad.step.model.StepSurfacedOpenShell
                || entity instanceof com.minicad.step.model.StepOrientedOpenShell
                || entity instanceof com.minicad.step.model.StepOrientedClosedShell
                || entity instanceof com.minicad.step.model.StepConnectedFaceSet
                || entity instanceof com.minicad.step.model.StepConnectedFaceSubSet
                || entity instanceof com.minicad.step.model.StepFaceBasedSurfaceModel
                || entity instanceof com.minicad.step.model.StepManifoldSurfaceModel
                || entity instanceof com.minicad.step.model.StepShellBasedSurfaceModel
                || entity instanceof com.minicad.step.model.StepManifoldSolidBrep
                || entity instanceof com.minicad.step.model.StepBrepWithVoids;
    }

    private static boolean isShellCandidate(StepEntity entity) {
        return entity instanceof com.minicad.step.model.StepOpenShell
                || entity instanceof com.minicad.step.model.StepClosedShell
                || entity instanceof com.minicad.step.model.StepSurfacedOpenShell
                || entity instanceof com.minicad.step.model.StepConnectedFaceSet
                || entity instanceof com.minicad.step.model.StepTessellatedFaceSet
                || entity instanceof com.minicad.step.model.StepTessellatedFace
                || entity instanceof com.minicad.step.model.StepFaceBasedSurfaceModel
                || entity instanceof com.minicad.step.model.StepManifoldSurfaceModel
                || entity instanceof com.minicad.step.model.StepShellBasedSurfaceModel;
    }

    // ── Triangulation ─────────────────────────────────────────────────────────

    private static class Triangulator {
        private final Map<MeshVertex, Integer> vertexIndex = new LinkedHashMap<>();
        private final List<int[]> faceIndices = new ArrayList<>();
        private static final double PLANAR_EPS = 1e-9;

        private record MeshVertex(double x, double y, double z, double nx, double ny, double nz) {
            MeshVertex(CartesianPoint p, Vector3 n) {
                this(Math.round(p.x() * VERTEX_ROUNDING) / VERTEX_ROUNDING,
                     Math.round(p.y() * VERTEX_ROUNDING) / VERTEX_ROUNDING,
                     Math.round(p.z() * VERTEX_ROUNDING) / VERTEX_ROUNDING,
                     Math.round(n.x() * VERTEX_ROUNDING) / VERTEX_ROUNDING,
                     Math.round(n.y() * VERTEX_ROUNDING) / VERTEX_ROUNDING,
                     Math.round(n.z() * VERTEX_ROUNDING) / VERTEX_ROUNDING);
            }
        }

        int addVertex(CartesianPoint p, Vector3 n) {
            MeshVertex key = new MeshVertex(p, n);
            Integer idx = vertexIndex.get(key);
            if (idx != null) {
                return idx;
            }
            idx = vertexIndex.size();
            vertexIndex.put(key, idx);
            return idx;
        }

        void addTriangle(int v0, int v1, int v2) {
            faceIndices.add(new int[]{v0, v1, v2});
        }

        void triangulateSolid(Solid solid) {
            for (Shell shell : solid.allShells()) {
                triangulateShell(shell);
            }
        }

        void triangulateShell(Shell shell) {
            for (Face face : shell.faces()) {
                triangulateFace(face);
            }
        }

        void triangulateFace(Face face) {
            SurfaceGeometry surface = face.surface();
            boolean flipped = !face.sameSense();

            if (surface instanceof Plane plane) {
                triangulatePlanarFace(face, plane, flipped);
            } else if (triangulateParametricFace(face, surface, flipped)) {
                return;
            } else {
                triangulateCurvedFace(face, surface, flipped);
            }
        }

        void triangulateSemanticFace(com.minicad.step.model.StepFaceEntity stepFace, StepCadBuilder builder) {
            StepEntity faceGeometry = semanticFaceGeometry(stepFace);
            boolean flipped = !semanticFaceSameSense(stepFace);
            SurfaceGeometry surface = buildSemanticSurfaceGeometry(faceGeometry, builder);
            if (!(surface instanceof Plane) && surface != null
                    && triangulateSemanticParametricFace(stepFace, faceGeometry, surface, builder, flipped)) {
                return;
            }

            Face builtFace = builder.buildFace(stepFace.id());
            if (surface == null) {
                surface = builtFace.surface();
            }
            if (surface instanceof Plane plane) {
                triangulatePlanarFace(builtFace, plane, flipped);
            } else if (!triangulateSemanticParametricFace(stepFace, faceGeometry, surface, builder, flipped)) {
                triangulateFace(builtFace);
            }
        }

        private void triangulatePlanarFace(Face face, Plane plane, boolean flipped) {
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
                int v0 = addVertex(polygon.get(triangle[0]).point(), normal);
                int v1 = addVertex(polygon.get(triangle[1]).point(), normal);
                int v2 = addVertex(polygon.get(triangle[2]).point(), normal);
                if (flipped) {
                    addTriangle(v0, v2, v1);
                } else {
                    addTriangle(v0, v1, v2);
                }
            }
        }

        private void triangulateCurvedFace(Face face, SurfaceGeometry surface, boolean flipped) {
            int uSegs = DEFAULT_CURVE_SEGMENTS;
            int vSegs = DEFAULT_CURVE_SEGMENTS;

            BoundingBox3 bbox = face.boundingBox();
            double diag = bbox.diagonal();
            if (diag > 0) {
                int base = Math.max(MIN_CURVE_SEGMENTS,
                    Math.min(MAX_CURVE_SEGMENTS, (int) Math.ceil(diag * BBOX_SEGMENT_MULTIPLIER)));
                uSegs = base;
                vSegs = base;
            }

            List<List<CartesianPoint>> grid = surface.sampleGrid(uSegs, vSegs);
            if (grid.isEmpty() || grid.get(0).isEmpty()) return;

            int rows = grid.size();
            int cols = grid.get(0).size();

            for (int i = 0; i < rows - 1; i++) {
                for (int j = 0; j < cols - 1; j++) {
                    CartesianPoint p00 = grid.get(i).get(j);
                    CartesianPoint p10 = grid.get(i + 1).get(j);
                    CartesianPoint p11 = grid.get(i + 1).get(j + 1);
                    CartesianPoint p01 = grid.get(i).get(j + 1);

                    Vector3 n = computeNormal(p00, p10, p11);
                    if (flipped) n = n.negate();

                    int v00 = addVertex(p00, n);
                    int v10 = addVertex(p10, n);
                    int v11 = addVertex(p11, n);
                    int v01 = addVertex(p01, n);

                    if (flipped) {
                        addTriangle(v00, v11, v10);
                        addTriangle(v00, v01, v11);
                    } else {
                        addTriangle(v00, v10, v11);
                        addTriangle(v00, v11, v01);
                    }
                }
            }
        }

        private boolean triangulateParametricFace(Face face, SurfaceGeometry surface, boolean flipped) {
            ParametricMapper mapper = mapperFor(surface);
            if (mapper == null) {
                return false;
            }

            List<ParametricLoop> loops = buildParametricLoops(face, mapper);
            if (loops.isEmpty() || loops.stream().noneMatch(ParametricLoop::outer)) {
                return false;
            }
            List<ParametricLoop> normalizedLoops = normalizeLoopPeriods(normalizeLoopRoles(loops), mapper);
            UvBounds bounds = boundsOf(normalizedLoops);
            if (bounds == null || bounds.uSpan() <= PLANAR_EPS || bounds.vSpan() <= PLANAR_EPS) {
                return false;
            }
            int sampleCount = normalizedLoops.stream().mapToInt(loop -> loop.points().size()).max().orElse(0);
            int uSegments = Math.max(16, Math.min(64, sampleCount * 2));
            int vSegments = Math.max(12, Math.min(48, sampleCount * 2));

            int trianglesBefore = faceIndices.size();
            for (int ui = 0; ui < uSegments; ui++) {
                double u0 = bounds.minU() + bounds.uSpan() * ui / uSegments;
                double u1 = bounds.minU() + bounds.uSpan() * (ui + 1) / uSegments;
                for (int vi = 0; vi < vSegments; vi++) {
                    double v0 = bounds.minV() + bounds.vSpan() * vi / vSegments;
                    double v1 = bounds.minV() + bounds.vSpan() * (vi + 1) / vSegments;
                    UvPoint center = new UvPoint((u0 + u1) * 0.5, (v0 + v1) * 0.5);
                    if (!containsParametricLoops(normalizedLoops, center)) {
                        continue;
                    }

                    CartesianPoint p00 = mapper.pointAt(u0, v0);
                    CartesianPoint p10 = mapper.pointAt(u1, v0);
                    CartesianPoint p01 = mapper.pointAt(u0, v1);
                    CartesianPoint p11 = mapper.pointAt(u1, v1);
                    Vector3 normal = mapper.normalAt(center.u(), center.v());
                    if (flipped) {
                        normal = normal.negate();
                    }
                    appendOrientedTriangle(p00, p10, p11, normal, flipped);
                    appendOrientedTriangle(p00, p11, p01, normal, flipped);
                }
            }
            return faceIndices.size() > trianglesBefore;
        }

        private boolean triangulateSemanticParametricFace(
                com.minicad.step.model.StepFaceEntity stepFace,
                StepEntity faceGeometry,
                SurfaceGeometry surface,
                StepCadBuilder builder,
                boolean flipped
        ) {
            ParametricMapper mapper = mapperFor(surface);
            if (mapper == null) {
                return false;
            }
            List<ParametricLoop> loops = buildSemanticParametricLoops(stepFace, faceGeometry, mapper, builder);
            if (loops.isEmpty() || loops.stream().noneMatch(ParametricLoop::outer)) {
                return false;
            }
            List<ParametricLoop> normalizedLoops = normalizeLoopPeriods(normalizeLoopRoles(loops), mapper);
            UvBounds bounds = boundsOf(normalizedLoops);
            if (bounds == null || bounds.uSpan() <= PLANAR_EPS || bounds.vSpan() <= PLANAR_EPS) {
                return false;
            }
            int sampleCount = normalizedLoops.stream().mapToInt(loop -> loop.points().size()).max().orElse(0);
            int uSegments = Math.max(16, Math.min(64, sampleCount * 2));
            int vSegments = Math.max(12, Math.min(48, sampleCount * 2));

            int trianglesBefore = faceIndices.size();
            for (int ui = 0; ui < uSegments; ui++) {
                double u0 = bounds.minU() + bounds.uSpan() * ui / uSegments;
                double u1 = bounds.minU() + bounds.uSpan() * (ui + 1) / uSegments;
                for (int vi = 0; vi < vSegments; vi++) {
                    double v0 = bounds.minV() + bounds.vSpan() * vi / vSegments;
                    double v1 = bounds.minV() + bounds.vSpan() * (vi + 1) / vSegments;
                    UvPoint center = new UvPoint((u0 + u1) * 0.5, (v0 + v1) * 0.5);
                    if (!containsParametricLoops(normalizedLoops, center)) {
                        continue;
                    }
                    CartesianPoint p00 = mapper.pointAt(u0, v0);
                    CartesianPoint p10 = mapper.pointAt(u1, v0);
                    CartesianPoint p01 = mapper.pointAt(u0, v1);
                    CartesianPoint p11 = mapper.pointAt(u1, v1);
                    Vector3 normal = mapper.normalAt(center.u(), center.v());
                    if (flipped) {
                        normal = normal.negate();
                    }
                    appendOrientedTriangle(p00, p10, p11, normal, flipped);
                    appendOrientedTriangle(p00, p11, p01, normal, flipped);
                }
            }
            return faceIndices.size() > trianglesBefore;
        }

        private List<CartesianPoint> extractLoopPoints(Loop loop) {
            if (loop instanceof PolyLoop poly) {
                return poly.points();
            }
            if (loop instanceof EdgeLoop edgeLoop) {
                List<CartesianPoint> points = new ArrayList<>();
                for (OrientedEdge oe : edgeLoop.edges()) {
                    List<CartesianPoint> edgePoints = orientSamples(oe, oe.edge().sample(DEFAULT_CURVE_SEGMENTS));
                    int startIndex = points.isEmpty() ? 0 : 1;
                    for (int i = startIndex; i < edgePoints.size(); i++) {
                        points.add(edgePoints.get(i));
                    }
                }
                if (points.size() > 2 && points.getFirst().equals(points.getLast())) {
                    points.removeLast();
                }
                return points;
            }
            return List.of();
        }

        private Vector3 computeNormal(CartesianPoint a, CartesianPoint b, CartesianPoint c) {
            Vector3 ab = b.subtract(a);
            Vector3 ac = c.subtract(a);
            Vector3 n = ab.cross(ac);
            double len = n.norm();
            if (len < MIN_TRIANGLE_AREA) {
                return new Vector3(0, 0, 1);
            }
            return n.normalize().asVector();
        }

        private void appendOrientedTriangle(
                CartesianPoint p0,
                CartesianPoint p1,
                CartesianPoint p2,
                Vector3 normal,
                boolean flipped
        ) {
            if (triangleArea(p0, p1, p2) <= MIN_TRIANGLE_AREA) {
                return;
            }
            int v0 = addVertex(p0, normal);
            int v1 = addVertex(p1, normal);
            int v2 = addVertex(p2, normal);
            if (flipped) {
                addTriangle(v0, v2, v1);
            } else {
                addTriangle(v0, v1, v2);
            }
        }

        private List<CartesianPoint> orientSamples(OrientedEdge orientedEdge, List<CartesianPoint> samples) {
            if (samples.isEmpty()) {
                return List.of(
                        orientedEdge.startVertex().point(),
                        orientedEdge.endVertex().point()
                );
            }
            List<CartesianPoint> oriented = new ArrayList<>(samples);
            CartesianPoint expectedStart = orientedEdge.startVertex().point();
            CartesianPoint expectedEnd = orientedEdge.endVertex().point();
            double forward = samples.getFirst().distanceTo(expectedStart) + samples.getLast().distanceTo(expectedEnd);
            double backward = samples.getFirst().distanceTo(expectedEnd) + samples.getLast().distanceTo(expectedStart);
            if (backward < forward) {
                Collections.reverse(oriented);
            }
            if (!oriented.getFirst().equals(expectedStart)) {
                oriented.set(0, expectedStart);
            }
            if (!oriented.getLast().equals(expectedEnd)) {
                oriented.set(oriented.size() - 1, expectedEnd);
            }
            return List.copyOf(oriented);
        }

        private ProjectedLoop projectLoop(List<CartesianPoint> points, PlanarFrame frame, boolean outer) {
            List<ProjectedVertex> projected = new ArrayList<>();
            for (CartesianPoint point : points) {
                ProjectedVertex vertex = frame.project(point);
                if (projected.isEmpty() || !samePoint(projected.getLast(), vertex)) {
                    projected.add(vertex);
                }
            }
            if (projected.size() > 1 && samePoint(projected.getFirst(), projected.getLast())) {
                projected.removeLast();
            }
            removeCollinear(projected);
            return new ProjectedLoop(projected, outer);
        }

        private List<ProjectedVertex> buildSimplePolygon(List<ProjectedLoop> loops) {
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

        private List<ProjectedVertex> ensureOrientation(List<ProjectedVertex> vertices, boolean ccw) {
            List<ProjectedVertex> oriented = new ArrayList<>(vertices);
            boolean isCcw = signedArea(oriented) > 0.0;
            if (isCcw != ccw) {
                Collections.reverse(oriented);
            }
            return oriented;
        }

        private List<ProjectedVertex> mergeHole(List<ProjectedVertex> outer, List<ProjectedVertex> hole) {
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

        private int findVisibleOuterVertex(
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

        private boolean isVisibleBridge(
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
            if (!containsPoint(loops.getFirst(), midpoint)) {
                return false;
            }
            for (int i = 1; i < loops.size(); i++) {
                if (containsPoint(loops.get(i), midpoint)) {
                    return false;
                }
            }
            return true;
        }

        private List<int[]> earClip(List<ProjectedVertex> polygon) {
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

        private boolean isEar(
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

        private boolean pointInTriangle(ProjectedVertex p, ProjectedVertex a, ProjectedVertex b, ProjectedVertex c) {
            double c1 = cross(a, b, p);
            double c2 = cross(b, c, p);
            double c3 = cross(c, a, p);
            boolean hasNegative = c1 < -PLANAR_EPS || c2 < -PLANAR_EPS || c3 < -PLANAR_EPS;
            boolean hasPositive = c1 > PLANAR_EPS || c2 > PLANAR_EPS || c3 > PLANAR_EPS;
            return !(hasNegative && hasPositive);
        }

        private boolean containsPoint(List<ProjectedVertex> polygon, ProjectedVertex point) {
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

        private boolean segmentsIntersect(ProjectedVertex a, ProjectedVertex b, ProjectedVertex c, ProjectedVertex d) {
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

        private boolean isOnSegment(ProjectedVertex a, ProjectedVertex b, ProjectedVertex p) {
            if (Math.abs(cross(a, b, p)) > PLANAR_EPS) {
                return false;
            }
            return p.u() >= Math.min(a.u(), b.u()) - PLANAR_EPS
                    && p.u() <= Math.max(a.u(), b.u()) + PLANAR_EPS
                    && p.v() >= Math.min(a.v(), b.v()) - PLANAR_EPS
                    && p.v() <= Math.max(a.v(), b.v()) + PLANAR_EPS;
        }

        private int rightmostIndex(List<ProjectedVertex> vertices) {
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

        private double signedArea(List<ProjectedVertex> vertices) {
            double area = 0.0;
            for (int i = 0; i < vertices.size(); i++) {
                ProjectedVertex a = vertices.get(i);
                ProjectedVertex b = vertices.get((i + 1) % vertices.size());
                area += a.u() * b.v() - b.u() * a.v();
            }
            return area * 0.5;
        }

        private void removeCollinear(List<ProjectedVertex> vertices) {
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

        private boolean samePoint(ProjectedVertex a, ProjectedVertex b) {
            return Math.abs(a.u() - b.u()) <= PLANAR_EPS && Math.abs(a.v() - b.v()) <= PLANAR_EPS;
        }

        private double cross(ProjectedVertex a, ProjectedVertex b, ProjectedVertex c) {
            return (b.u() - a.u()) * (c.v() - a.v()) - (b.v() - a.v()) * (c.u() - a.u());
        }

        private double distanceSquared(ProjectedVertex a, ProjectedVertex b) {
            double du = a.u() - b.u();
            double dv = a.v() - b.v();
            return du * du + dv * dv;
        }

        private record ProjectedVertex(CartesianPoint point, double u, double v) {}

        private record ProjectedLoop(List<ProjectedVertex> vertices, boolean outer) {}

        private record UvPoint(double u, double v) {}

        private record ParametricLoop(boolean outer, List<UvPoint> points) {}

        private record UvBounds(double minU, double maxU, double minV, double maxV) {
            double uSpan() {
                return maxU - minU;
            }

            double vSpan() {
                return maxV - minV;
            }
        }

        @FunctionalInterface
        private interface SurfacePointSampler {
            CartesianPoint pointAt(double u, double v);
        }

        private interface ParametricMapper {
            CartesianPoint pointAt(double u, double v);

            Vector3 normalAt(double u, double v);

            UvPoint project(CartesianPoint point, UvPoint previous);

            default Double uPeriod() {
                return null;
            }

            default Double vPeriod() {
                return null;
            }
        }

        private ParametricMapper mapperFor(SurfaceGeometry surface) {
            if (surface instanceof CylindricalSurface cylinder) {
                return new ParametricMapper() {
                    @Override
                    public CartesianPoint pointAt(double u, double v) {
                        return cylinder.pointAt(u, v);
                    }

                    @Override
                    public Vector3 normalAt(double u, double v) {
                        return cylinder.normalAt(u);
                    }

                    @Override
                    public UvPoint project(CartesianPoint point, UvPoint previous) {
                        Vector3 offset = point.subtract(cylinder.position().location());
                        double v = offset.dot(cylinder.position().axis().asVector());
                        Vector3 radial = offset.subtract(cylinder.position().axis().asVector().scale(v));
                        double u = Math.atan2(
                                radial.dot(cylinder.position().yDirection().asVector()),
                                radial.dot(cylinder.position().xDirection().asVector())
                        );
                        return new UvPoint(u, v);
                    }

                    @Override
                    public Double uPeriod() {
                        return Math.PI * 2.0;
                    }
                };
            }
            if (surface instanceof ConicalSurface cone) {
                return new ParametricMapper() {
                    @Override
                    public CartesianPoint pointAt(double u, double v) {
                        return cone.pointAt(u, v);
                    }

                    @Override
                    public Vector3 normalAt(double u, double v) {
                        return cone.normalAt(u);
                    }

                    @Override
                    public UvPoint project(CartesianPoint point, UvPoint previous) {
                        Vector3 offset = point.subtract(cone.position().location());
                        double v = offset.dot(cone.position().axis().asVector());
                        Vector3 radial = offset.subtract(cone.position().axis().asVector().scale(v));
                        double u = Math.atan2(
                                radial.dot(cone.position().yDirection().asVector()),
                                radial.dot(cone.position().xDirection().asVector())
                        );
                        return new UvPoint(u, v);
                    }

                    @Override
                    public Double uPeriod() {
                        return Math.PI * 2.0;
                    }
                };
            }
            if (surface instanceof ToroidalSurface torus) {
                return new ParametricMapper() {
                    @Override
                    public CartesianPoint pointAt(double u, double v) {
                        return torus.pointAt(u, v);
                    }

                    @Override
                    public Vector3 normalAt(double u, double v) {
                        return torus.normalAt(u, v);
                    }

                    @Override
                    public UvPoint project(CartesianPoint point, UvPoint previous) {
                        Vector3 offset = point.subtract(torus.position().location());
                        double localX = offset.dot(torus.position().xDirection().asVector());
                        double localY = offset.dot(torus.position().yDirection().asVector());
                        double localZ = offset.dot(torus.position().axis().asVector());
                        double u = Math.atan2(localY, localX);
                        double radialDist = Math.sqrt(localX * localX + localY * localY);
                        double v = Math.atan2(localZ, radialDist - torus.majorRadius());
                        return new UvPoint(u, v);
                    }

                    @Override
                    public Double uPeriod() {
                        return Math.PI * 2.0;
                    }

                    @Override
                    public Double vPeriod() {
                        return Math.PI * 2.0;
                    }
                };
            }
            if (surface instanceof SphericalSurface sphere) {
                return new ParametricMapper() {
                    @Override
                    public CartesianPoint pointAt(double u, double v) {
                        return sphere.pointAt(u, v);
                    }

                    @Override
                    public Vector3 normalAt(double u, double v) {
                        return sphere.normalAt(u, v);
                    }

                    @Override
                    public UvPoint project(CartesianPoint point, UvPoint previous) {
                        Vector3 offset = point.subtract(sphere.position().location());
                        double radial = offset.norm();
                        if (radial <= PLANAR_EPS) {
                            return null;
                        }
                        double u = Math.atan2(
                                offset.dot(sphere.position().yDirection().asVector()),
                                offset.dot(sphere.position().xDirection().asVector())
                        );
                        double v = Math.acos(offset.dot(sphere.position().axis().asVector()) / radial);
                        return new UvPoint(u, v);
                    }

                    @Override
                    public Double uPeriod() {
                        return Math.PI * 2.0;
                    }
                };
            }
            if (surface instanceof SurfaceOfRevolution3 revolution) {
                return new ParametricMapper() {
                    @Override
                    public CartesianPoint pointAt(double u, double v) {
                        return revolution.pointAt(v, u);
                    }

                    @Override
                    public Vector3 normalAt(double u, double v) {
                        return revolution.normalAt(v, u);
                    }

                    @Override
                    public UvPoint project(CartesianPoint point, UvPoint previous) {
                        return approximateUv(point, previous, 96, 64,
                                0.0, Math.PI * 2.0,
                                -4.0, 4.0,
                                true,
                                (u, v) -> revolution.pointAt(v, u));
                    }

                    @Override
                    public Double uPeriod() {
                        return Math.PI * 2.0;
                    }
                };
            }
            if (surface instanceof SurfaceOfLinearExtrusion3 extrusion) {
                return new ParametricMapper() {
                    @Override
                    public CartesianPoint pointAt(double u, double v) {
                        return extrusion.pointAt(u, v);
                    }

                    @Override
                    public Vector3 normalAt(double u, double v) {
                        return extrusion.normalAt(u, v);
                    }

                    @Override
                    public UvPoint project(CartesianPoint point, UvPoint previous) {
                        return approximateUv(point, previous, 64, 48,
                                -4.0, 4.0,
                                0.0, 1.0,
                                false,
                                extrusion::pointAt);
                    }
                };
            }
            return null;
        }

        private UvPoint approximateUv(
                CartesianPoint point,
                UvPoint previous,
                int uSteps,
                int vSteps,
                double uMin,
                double uMax,
                double vMin,
                double vMax,
                boolean uPeriodic,
                SurfacePointSampler sampler
        ) {
            UvPoint best = null;
            double bestDistance = Double.POSITIVE_INFINITY;
            for (int ui = 0; ui <= uSteps; ui++) {
                double u = uMin + (uMax - uMin) * ui / uSteps;
                for (int vi = 0; vi <= vSteps; vi++) {
                    double v = vMin + (vMax - vMin) * vi / vSteps;
                    CartesianPoint sample = sampler.pointAt(u, v);
                    double distance = sample.distanceTo(point);
                    if (best == null || distance < bestDistance) {
                        best = new UvPoint(u, v);
                        bestDistance = distance;
                    }
                }
            }
            if (best == null) {
                return null;
            }
            for (int pass = 0; pass < 3; pass++) {
                double uRadius = (uMax - uMin) / Math.pow(8.0, pass + 1);
                double vRadius = (vMax - vMin) / Math.pow(8.0, pass + 1);
                UvPoint refined = best;
                for (int ui = -4; ui <= 4; ui++) {
                    double u = best.u() + uRadius * ui / 4.0;
                    if (uPeriodic) {
                        u = wrapPeriodic(u - uMin, uMax - uMin) + uMin;
                    } else {
                        u = clamp(u, uMin, uMax);
                    }
                    for (int vi = -4; vi <= 4; vi++) {
                        double v = best.v() + vRadius * vi / 4.0;
                        v = clamp(v, vMin, vMax);
                        CartesianPoint sample = sampler.pointAt(u, v);
                        double distance = sample.distanceTo(point);
                        if (distance < bestDistance) {
                            refined = new UvPoint(u, v);
                            bestDistance = distance;
                        }
                    }
                }
                best = refined;
            }
            if (previous != null && uPeriodic) {
                return new UvPoint(unwrapPeriodic(best.u(), previous.u(), uMax - uMin), best.v());
            }
            return best;
        }


        private double clamp(double value, double min, double max) {
            return Math.max(min, Math.min(max, value));
        }

        private double wrapPeriodic(double value, double period) {
            double wrapped = value % period;
            return wrapped < 0.0 ? wrapped + period : wrapped;
        }

        private double unwrapPeriodic(double value, double reference, double period) {
            double unwrapped = value;
            while (unwrapped - reference > period * 0.5) {
                unwrapped -= period;
            }
            while (unwrapped - reference < -period * 0.5) {
                unwrapped += period;
            }
            return unwrapped;
        }

        private List<ParametricLoop> buildParametricLoops(Face face, ParametricMapper mapper) {
            List<ParametricLoop> loops = new ArrayList<>();
            for (FaceBound bound : face.bounds()) {
                List<UvPoint> uvPoints = extractLoopUvPoints(bound.loop(), mapper, face.surface());
                if (!bound.orientation()) {
                    uvPoints = reverseLoop(uvPoints);
                }
                uvPoints = normalizePeriodicLoop(uvPoints, mapper);
                if (uvPoints.size() < 3) {
                    continue;
                }
                if (!sameUv(uvPoints.getFirst(), uvPoints.getLast())) {
                    uvPoints.add(uvPoints.getFirst());
                } else {
                    uvPoints.set(uvPoints.size() - 1, uvPoints.getFirst());
                }
                loops.add(new ParametricLoop(bound.outer(), List.copyOf(uvPoints)));
            }
            return List.copyOf(loops);
        }

        private List<UvPoint> reverseLoop(List<UvPoint> points) {
            if (points.isEmpty()) {
                return points;
            }
            List<UvPoint> reversed = new ArrayList<>(points);
            Collections.reverse(reversed);
            return List.copyOf(reversed);
        }

        private List<ParametricLoop> buildSemanticParametricLoops(
                com.minicad.step.model.StepFaceEntity stepFace,
                StepEntity faceGeometry,
                ParametricMapper mapper,
                StepCadBuilder builder
        ) {
            List<ParametricLoop> loops = new ArrayList<>();
            boolean promoteSingleOuter = stepFace.bounds().size() == 1
                    && stepFace.bounds().stream().noneMatch(com.minicad.step.model.StepFaceBound::outer);
            for (com.minicad.step.model.StepFaceBound bound : stepFace.bounds()) {
                if (!(bound.loop() instanceof com.minicad.step.model.StepEdgeLoop edgeLoop)) {
                    return List.of();
                }
                List<UvPoint> loopPoints = new ArrayList<>();
                boolean firstEdge = true;
                for (com.minicad.step.model.StepOrientedEdge orientedEdge : edgeLoop.edges()) {
                    List<UvPoint> edgePoints = sampleSemanticOrientedEdge(orientedEdge, faceGeometry, mapper, builder);
                    if (edgePoints == null || edgePoints.size() < 2) {
                        return List.of();
                    }
                    int startIndex = firstEdge ? 0 : 1;
                    for (int index = startIndex; index < edgePoints.size(); index++) {
                        loopPoints.add(edgePoints.get(index));
                    }
                    firstEdge = false;
                }
                if (!bound.orientation()) {
                    loopPoints = reverseLoop(loopPoints);
                }
                loopPoints = normalizePeriodicLoop(loopPoints, mapper);
                if (loopPoints.size() < 3) {
                    return List.of();
                }
                if (!sameUv(loopPoints.getFirst(), loopPoints.getLast())) {
                    loopPoints.add(loopPoints.getFirst());
                }
                loops.add(new ParametricLoop(bound.outer() || promoteSingleOuter, List.copyOf(loopPoints)));
            }
            return List.copyOf(loops);
        }

        private List<UvPoint> extractLoopUvPoints(Loop loop, ParametricMapper mapper, SurfaceGeometry surface) {
            if (loop instanceof PolyLoop polyLoop) {
                List<UvPoint> uvPoints = new ArrayList<>();
                UvPoint previous = null;
                for (CartesianPoint point : polyLoop.points()) {
                    UvPoint uv = mapper.project(point, previous);
                    if (uv == null) {
                        return List.of();
                    }
                    uvPoints.add(uv);
                    previous = uv;
                }
                return uvPoints;
            }
            if (loop instanceof EdgeLoop edgeLoop) {
                List<UvPoint> uvPoints = new ArrayList<>();
                for (OrientedEdge orientedEdge : edgeLoop.edges()) {
                    List<UvPoint> edgePoints = extractEdgeUvPoints(orientedEdge, mapper, surface);
                    if (edgePoints.size() < 2) {
                        return List.of();
                    }
                    int startIndex = uvPoints.isEmpty() ? 0 : 1;
                    for (int i = startIndex; i < edgePoints.size(); i++) {
                        uvPoints.add(edgePoints.get(i));
                    }
                }
                if (uvPoints.size() > 1 && sameUv(uvPoints.getFirst(), uvPoints.getLast())) {
                    uvPoints.removeLast();
                }
                return uvPoints;
            }
            return List.of();
        }

        private List<UvPoint> sampleSemanticOrientedEdge(
                com.minicad.step.model.StepOrientedEdge orientedEdge,
                StepEntity faceGeometry,
                ParametricMapper mapper,
                StepCadBuilder builder
        ) {
            StepEntity edgeGeometry = orientedEdge.edgeElement().edgeGeometry();
            StepEntity associated = unwrapAssociatedCurveGeometry(edgeGeometry);
            List<StepEntity> pcurves = matchingPcurves(associatedGeometry(associated), faceGeometry);
            CartesianPoint startPoint3d = mapPointIntoFaceGeometry(pointFromStep(orientedEdge.orientation()
                    ? orientedEdge.edgeElement().start().point()
                    : orientedEdge.edgeElement().end().point()), faceGeometry, builder);
            CartesianPoint endPoint3d = mapPointIntoFaceGeometry(pointFromStep(orientedEdge.orientation()
                    ? orientedEdge.edgeElement().end().point()
                    : orientedEdge.edgeElement().start().point()), faceGeometry, builder);
            UvPoint projectedStart = mapper.project(startPoint3d, null);
            UvPoint projectedEnd = projectedStart == null
                    ? mapper.project(endPoint3d, null)
                    : mapper.project(endPoint3d, projectedStart);

            List<UvPoint> best = List.of();
            double bestScore = Double.POSITIVE_INFINITY;
            for (StepEntity pcurve : pcurves) {
                Object built = builder.buildPcurve2(pcurve.id());
                if (!(built instanceof Curve2 curve2)) {
                    continue;
                }
                List<UvPoint> sampled = sampleCurve2(curve2, projectedStart, projectedEnd);
                if (sampled.isEmpty()) {
                    continue;
                }
                double score = score(projectedStart, projectedEnd, sampled);
                if (best.isEmpty() || score < bestScore) {
                    best = sampled;
                    bestScore = score;
                }
            }
            if (!best.isEmpty()) {
                return best;
            }

            Edge edge = builder.buildEdge(orientedEdge.edgeElement().id());
            OrientedEdge built = new OrientedEdge(edge, orientedEdge.orientation());
            List<CartesianPoint> points3d = orientSamples(built, built.edge().sample(DEFAULT_CURVE_SEGMENTS));
            List<UvPoint> uvPoints = new ArrayList<>();
            UvPoint previous = null;
            for (CartesianPoint point : points3d) {
                UvPoint uv = mapper.project(mapPointIntoFaceGeometry(point, faceGeometry, builder), previous);
                if (uv == null) {
                    return List.of();
                }
                uvPoints.add(uv);
                previous = uv;
            }
            return List.copyOf(uvPoints);
        }

        private CartesianPoint mapPointIntoFaceGeometry(
                CartesianPoint point,
                StepEntity faceGeometry,
                StepCadBuilder builder
        ) {
            StepEntity current = faceGeometry;
            CartesianPoint mapped = point;
            for (int depth = 0; depth < 16 && current != null; depth++) {
                if (current instanceof com.minicad.step.model.StepRectangularTrimmedSurface trimmedSurface) {
                    current = trimmedSurface.basisSurface();
                    continue;
                }
                if (current instanceof com.minicad.step.model.StepCurveBoundedSurface boundedSurface) {
                    current = boundedSurface.basisSurface();
                    continue;
                }
                if (current instanceof com.minicad.step.model.StepOrientedSurface orientedSurface) {
                    current = orientedSurface.surfaceElement();
                    continue;
                }
                if (current instanceof com.minicad.step.model.StepOffsetSurface offsetSurface) {
                    current = offsetSurface.basisSurface();
                    continue;
                }
                if (current instanceof com.minicad.step.model.StepGeometricReplica replica
                        && "SURFACE_REPLICA".equals(replica.entityName())) {
                    mapped = transformPoint3(mapped, replica.transformation(), builder);
                    current = replica.parent();
                    continue;
                }
                break;
            }
            return mapped;
        }

        private StepEntity unwrapAssociatedCurveGeometry(StepEntity edgeGeometry) {
            StepEntity current = edgeGeometry;
            for (int depth = 0; depth < 16; depth++) {
                if (current instanceof com.minicad.step.model.StepOrientedCurve orientedCurve) {
                    current = orientedCurve.curveElement();
                    continue;
                }
                if (current instanceof com.minicad.step.model.StepGeometricReplica replica
                        && "CURVE_REPLICA".equals(replica.entityName())) {
                    current = replica.parent();
                    continue;
                }
                return current;
            }
            return current;
        }

        private List<StepEntity> associatedGeometry(StepEntity edgeGeometry) {
            if (edgeGeometry instanceof com.minicad.step.model.StepSurfaceCurve surfaceCurve) {
                return surfaceCurve.associatedGeometry();
            }
            if (edgeGeometry instanceof com.minicad.step.model.StepSeamCurve seamCurve) {
                return seamCurve.associatedGeometry();
            }
            return List.of();
        }

        private List<StepEntity> matchingPcurves(List<StepEntity> associatedGeometry, StepEntity faceGeometry) {
            Set<Integer> acceptableSurfaceIds = acceptablePcurveBasisSurfaceIds(faceGeometry);
            List<StepEntity> matches = new ArrayList<>();
            for (StepEntity associated : associatedGeometry) {
                if (associated instanceof com.minicad.step.model.StepPcurve pcurve
                        && acceptableSurfaceIds.contains(pcurve.basisSurface().id())) {
                    matches.add(pcurve);
                } else if (associated instanceof com.minicad.step.model.StepDegeneratePcurve pcurve
                        && acceptableSurfaceIds.contains(pcurve.basisSurface().id())) {
                    matches.add(pcurve);
                }
            }
            return List.copyOf(matches);
        }

        private Set<Integer> acceptablePcurveBasisSurfaceIds(StepEntity faceGeometry) {
            LinkedHashSet<Integer> ids = new LinkedHashSet<>();
            StepEntity current = faceGeometry;
            for (int depth = 0; depth < 16 && current != null; depth++) {
                ids.add(current.id());
                if (current instanceof com.minicad.step.model.StepRectangularTrimmedSurface trimmedSurface) {
                    current = trimmedSurface.basisSurface();
                    continue;
                }
                if (current instanceof com.minicad.step.model.StepCurveBoundedSurface boundedSurface) {
                    current = boundedSurface.basisSurface();
                    continue;
                }
                if (current instanceof com.minicad.step.model.StepOrientedSurface orientedSurface) {
                    current = orientedSurface.surfaceElement();
                    continue;
                }
                if (current instanceof com.minicad.step.model.StepOffsetSurface offsetSurface) {
                    current = offsetSurface.basisSurface();
                    continue;
                }
                if (current instanceof com.minicad.step.model.StepGeometricReplica replica
                        && "SURFACE_REPLICA".equals(replica.entityName())) {
                    current = replica.parent();
                    continue;
                }
                break;
            }
            return Set.copyOf(ids);
        }

        private CartesianPoint pointFromStep(com.minicad.step.model.StepCartesianPoint point) {
            return new CartesianPoint(
                    point.coordinates().get(0),
                    point.coordinates().get(1),
                    point.coordinates().get(2)
            );
        }

        private StepEntity semanticFaceGeometry(com.minicad.step.model.StepFaceEntity stepFace) {
            if (stepFace instanceof com.minicad.step.model.StepAdvancedFace advancedFace) {
                return advancedFace.faceGeometry();
            }
            if (stepFace instanceof com.minicad.step.model.StepFaceSurface faceSurface) {
                return faceSurface.faceGeometry();
            }
            if (stepFace instanceof com.minicad.step.model.StepOrientedFace orientedFace) {
                return semanticFaceGeometry(orientedFace.faceElement());
            }
            throw new IllegalArgumentException("unsupported face subtype");
        }

        private boolean semanticFaceSameSense(com.minicad.step.model.StepFaceEntity stepFace) {
            if (stepFace instanceof com.minicad.step.model.StepAdvancedFace advancedFace) {
                return advancedFace.sameSense();
            }
            if (stepFace instanceof com.minicad.step.model.StepFaceSurface faceSurface) {
                return faceSurface.sameSense();
            }
            if (stepFace instanceof com.minicad.step.model.StepOrientedFace orientedFace) {
                boolean base = semanticFaceSameSense(orientedFace.faceElement());
                return orientedFace.orientation() ? base : !base;
            }
            throw new IllegalArgumentException("unsupported face subtype");
        }

        private SurfaceGeometry buildSemanticSurfaceGeometry(StepEntity geometry, StepCadBuilder builder) {
            if (geometry instanceof com.minicad.step.model.StepPlane plane) {
                return builder.buildPlane(plane.id());
            }
            if (geometry instanceof com.minicad.step.model.StepCylindricalSurface cylindricalSurface) {
                return builder.buildCylindricalSurface(cylindricalSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepConicalSurface conicalSurface) {
                return builder.buildConicalSurface(conicalSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepSphericalSurface sphericalSurface) {
                return builder.buildSphericalSurface(sphericalSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepToroidalSurface toroidalSurface) {
                return builder.buildToroidalSurface(toroidalSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepDegenerateToroidalSurface degenerateToroidalSurface) {
                return builder.buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepSurfaceOfLinearExtrusion extrusionSurface) {
                return builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepSurfaceOfRevolution revolutionSurface) {
                return builder.buildSurfaceOfRevolution(revolutionSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepRationalBSplineSurface rationalSplineSurface) {
                return builder.buildRationalBSplineSurface(rationalSplineSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepBSplineSurfaceWithKnots splineSurface) {
                return builder.buildBSplineSurface(splineSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepBSplineSurface splineSurface) {
                return builder.buildGenericBSplineSurface(splineSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepBezierSurface splineSurface) {
                return builder.buildBezierSurface(splineSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepUniformSurface splineSurface) {
                return builder.buildUniformSurface(splineSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepQuasiUniformSurface splineSurface) {
                return builder.buildQuasiUniformSurface(splineSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepPiecewiseBezierSurface splineSurface) {
                return builder.buildPiecewiseBezierSurface(splineSurface.id());
            }
            if (geometry instanceof com.minicad.step.model.StepRectangularTrimmedSurface trimmedSurface) {
                return buildSemanticSurfaceGeometry(trimmedSurface.basisSurface(), builder);
            }
            if (geometry instanceof com.minicad.step.model.StepCurveBoundedSurface boundedSurface) {
                return buildSemanticSurfaceGeometry(boundedSurface.basisSurface(), builder);
            }
            if (geometry instanceof com.minicad.step.model.StepOrientedSurface orientedSurface) {
                builder.buildOrientedSurface(orientedSurface.id());
                return buildSemanticSurfaceGeometry(orientedSurface.surfaceElement(), builder);
            }
            if (geometry instanceof com.minicad.step.model.StepOffsetSurface offsetSurface) {
                builder.buildOffsetSurface(offsetSurface.id());
                SurfaceGeometry base = buildSemanticSurfaceGeometry(offsetSurface.basisSurface(), builder);
                return offsetSemanticSurfaceGeometry(base, offsetSurface.distance());
            }
            if (geometry instanceof com.minicad.step.model.StepGeometricReplica replica
                    && "SURFACE_REPLICA".equals(replica.entityName())) {
                builder.buildSurfaceReplica(replica.id());
                SurfaceGeometry base = buildSemanticSurfaceGeometry(replica.parent(), builder);
                return transformSemanticSurfaceGeometry(base, replica.transformation(), builder);
            }
            return null;
        }

        private SurfaceGeometry offsetSemanticSurfaceGeometry(SurfaceGeometry base, double distance) {
            if (base == null) {
                return null;
            }
            if (base instanceof Plane plane) {
                return new Plane(
                        plane.origin().add(plane.normal().asVector().scale(distance)),
                        plane.normal());
            }
            if (base instanceof CylindricalSurface cylindricalSurface) {
                return new CylindricalSurface(
                        cylindricalSurface.position(),
                        cylindricalSurface.radius() + distance);
            }
            if (base instanceof SphericalSurface sphericalSurface) {
                return new SphericalSurface(
                        sphericalSurface.position(),
                        sphericalSurface.radius() + distance);
            }
            if (base instanceof ConicalSurface conicalSurface) {
                return offsetConicalSurface(conicalSurface, distance);
            }
            if (base instanceof ToroidalSurface toroidalSurface) {
                return new ToroidalSurface(
                        toroidalSurface.position(),
                        toroidalSurface.majorRadius(),
                        toroidalSurface.minorRadius() + distance);
            }
            return null;
        }

        private ConicalSurface offsetConicalSurface(ConicalSurface conicalSurface, double distance) {
            double semiAngle = conicalSurface.semiAngle();
            double radialOffset = distance * Math.cos(semiAngle);
            double axisOffset = -distance * Math.sin(semiAngle);
            Axis2Placement3D position = conicalSurface.position();
            return new ConicalSurface(
                    new Axis2Placement3D(
                            position.location().add(position.axis().asVector().scale(axisOffset)),
                            position.axis(),
                            position.refDirection()),
                    conicalSurface.radius() + radialOffset,
                    semiAngle);
        }

        private SurfaceGeometry transformSemanticSurfaceGeometry(
                SurfaceGeometry surface,
                com.minicad.step.model.StepCartesianTransformationOperator transformation,
                StepCadBuilder builder
        ) {
            if (surface == null) {
                return null;
            }
            double scale = Math.abs(transformationScale(transformation));
            if (surface instanceof Plane plane) {
                return new Plane(
                        transformPoint3(plane.origin(), transformation, builder),
                        transformDirection3(plane.normal(), transformation, builder));
            }
            if (surface instanceof CylindricalSurface cylindricalSurface) {
                return new CylindricalSurface(
                        transformPlacement(cylindricalSurface.position(), transformation, builder),
                        cylindricalSurface.radius() * scale);
            }
            if (surface instanceof ConicalSurface conicalSurface) {
                return new ConicalSurface(
                        transformPlacement(conicalSurface.position(), transformation, builder),
                        conicalSurface.radius() * scale,
                        conicalSurface.semiAngle());
            }
            if (surface instanceof SphericalSurface sphericalSurface) {
                return new SphericalSurface(
                        transformPlacement(sphericalSurface.position(), transformation, builder),
                        sphericalSurface.radius() * scale);
            }
            if (surface instanceof ToroidalSurface toroidalSurface) {
                return new ToroidalSurface(
                        transformPlacement(toroidalSurface.position(), transformation, builder),
                        toroidalSurface.majorRadius() * scale,
                        toroidalSurface.minorRadius() * scale);
            }
            if (surface instanceof SurfaceOfRevolution3 revolutionSurface) {
                Curve3 sweptCurve = transformSemanticCurve3(revolutionSurface.sweptCurve(), transformation, builder);
                if (sweptCurve == null) {
                    return null;
                }
                return new SurfaceOfRevolution3(
                        sweptCurve,
                        transformPoint3(revolutionSurface.axisOrigin(), transformation, builder),
                        transformDirection3(revolutionSurface.axisDirection(), transformation, builder));
            }
            if (surface instanceof SurfaceOfLinearExtrusion3 extrusionSurface) {
                Curve3 sweptCurve = transformSemanticCurve3(extrusionSurface.sweptCurve(), transformation, builder);
                if (sweptCurve == null) {
                    return null;
                }
                return new SurfaceOfLinearExtrusion3(
                        sweptCurve,
                        transformVector3(extrusionSurface.extrusionVector(), transformation, builder));
            }
            return null;
        }

        private Curve3 transformSemanticCurve3(
                Curve3 curve,
                com.minicad.step.model.StepCartesianTransformationOperator transformation,
                StepCadBuilder builder
        ) {
            double scale = transformationScale(transformation);
            if (curve instanceof Line3 line) {
                return new Line3(
                        transformPoint3(line.origin(), transformation, builder),
                        transformDirection3(line.direction(), transformation, builder),
                        line.parameterScale() * Math.abs(scale));
            }
            if (curve instanceof Circle circle) {
                return new Circle(
                        transformPlacement(circle.position(), transformation, builder),
                        circle.radius() * Math.abs(scale));
            }
            if (curve instanceof Ellipse3 ellipse) {
                return new Ellipse3(
                        transformPlacement(ellipse.position(), transformation, builder),
                        ellipse.semiAxis1() * Math.abs(scale),
                        ellipse.semiAxis2() * Math.abs(scale));
            }
            if (curve instanceof Polyline3 polyline) {
                return new Polyline3(polyline.points().stream()
                        .map(point -> transformPoint3(point, transformation, builder))
                        .toList());
            }
            if (curve instanceof BSplineCurve3 bsplineCurve) {
                return new BSplineCurve3(
                        bsplineCurve.degree(),
                        bsplineCurve.controlPoints().stream()
                                .map(point -> transformPoint3(point, transformation, builder))
                                .toList(),
                        bsplineCurve.knotMultiplicities(),
                        bsplineCurve.knots());
            }
            if (curve instanceof RationalBSplineCurve3 rationalBSplineCurve) {
                return new RationalBSplineCurve3(
                        rationalBSplineCurve.degree(),
                        rationalBSplineCurve.controlPoints().stream()
                                .map(point -> transformPoint3(point, transformation, builder))
                                .toList(),
                        rationalBSplineCurve.weights(),
                        rationalBSplineCurve.knotMultiplicities(),
                        rationalBSplineCurve.knots());
            }
            if (curve instanceof CompositeCurve3 compositeCurve) {
                List<Curve3> transformedSegments = new ArrayList<>(compositeCurve.segments().size());
                for (Curve3 segment : compositeCurve.segments()) {
                    Curve3 transformed = transformSemanticCurve3(segment, transformation, builder);
                    if (transformed == null) {
                        return null;
                    }
                    transformedSegments.add(transformed);
                }
                return new CompositeCurve3(transformedSegments);
            }
            if (curve instanceof TrimmedCurve3 trimmedCurve) {
                Curve3 basisCurve = transformSemanticCurve3(trimmedCurve.basisCurve(), transformation, builder);
                if (basisCurve == null) {
                    return null;
                }
                return new TrimmedCurve3(
                        basisCurve,
                        trimmedCurve.trimParamStart(),
                        trimmedCurve.trimParamEnd(),
                        trimmedCurve.senseAgreement());
            }
            if (curve instanceof SurfaceCurve3 surfaceCurve) {
                Curve3 curve3d = transformSemanticCurve3(surfaceCurve.curve3d(), transformation, builder);
                if (curve3d == null) {
                    return null;
                }
                return new SurfaceCurve3(curve3d, surfaceCurve.parametricCurves());
            }
            return null;
        }

        private Axis2Placement3D transformPlacement(
                Axis2Placement3D placement,
                com.minicad.step.model.StepCartesianTransformationOperator transformation,
                StepCadBuilder builder
        ) {
            return new Axis2Placement3D(
                    transformPoint3(placement.location(), transformation, builder),
                    transformDirection3(placement.axis(), transformation, builder),
                    transformDirection3(placement.refDirection(), transformation, builder));
        }

        private CartesianPoint transformPoint3(
                CartesianPoint point,
                com.minicad.step.model.StepCartesianTransformationOperator transformation,
                StepCadBuilder builder
        ) {
            Vector3 basisX = transformAxis1_3(transformation, builder);
            Vector3 basisY = transformAxis2OrDefault3(transformation, basisX, builder);
            Vector3 basisZ = transformAxis3OrDefault3(transformation, basisX, basisY, builder);
            double scale = transformationScale(transformation);
            Vector3 offset = basisX.scale(point.x() * scale)
                    .add(basisY.scale(point.y() * scale))
                    .add(basisZ.scale(point.z() * scale));
            return builder.buildPoint(transformation.localOrigin().id()).add(offset);
        }

        private Direction3 transformDirection3(
                Direction3 direction,
                com.minicad.step.model.StepCartesianTransformationOperator transformation,
                StepCadBuilder builder
        ) {
            Vector3 basisX = transformAxis1_3(transformation, builder);
            Vector3 basisY = transformAxis2OrDefault3(transformation, basisX, builder);
            Vector3 basisZ = transformAxis3OrDefault3(transformation, basisX, basisY, builder);
            Vector3 source = direction.asVector();
            return Direction3.from(
                    basisX.scale(source.x())
                            .add(basisY.scale(source.y()))
                            .add(basisZ.scale(source.z())));
        }

        private Vector3 transformVector3(
                Vector3 vector,
                com.minicad.step.model.StepCartesianTransformationOperator transformation,
                StepCadBuilder builder
        ) {
            Vector3 basisX = transformAxis1_3(transformation, builder);
            Vector3 basisY = transformAxis2OrDefault3(transformation, basisX, builder);
            Vector3 basisZ = transformAxis3OrDefault3(transformation, basisX, basisY, builder);
            double scale = transformationScale(transformation);
            return basisX.scale(vector.x() * scale)
                    .add(basisY.scale(vector.y() * scale))
                    .add(basisZ.scale(vector.z() * scale));
        }

        private Vector3 transformAxis1_3(
                com.minicad.step.model.StepCartesianTransformationOperator transformation,
                StepCadBuilder builder
        ) {
            return transformation.axis1() == null
                    ? new Vector3(1.0, 0.0, 0.0)
                    : builder.buildDirection(transformation.axis1().id()).asVector();
        }

        private Vector3 transformAxis2OrDefault3(
                com.minicad.step.model.StepCartesianTransformationOperator transformation,
                Vector3 axis1,
                StepCadBuilder builder
        ) {
            if (transformation.axis2() != null) {
                return builder.buildDirection(transformation.axis2().id()).asVector();
            }
            Vector3 fallback = new Vector3(0.0, 1.0, 0.0);
            return axis1.cross(fallback).isZero() ? new Vector3(0.0, 0.0, 1.0) : fallback;
        }

        private Vector3 transformAxis3OrDefault3(
                com.minicad.step.model.StepCartesianTransformationOperator transformation,
                Vector3 axis1,
                Vector3 axis2,
                StepCadBuilder builder
        ) {
            if (transformation.axis3() != null) {
                return builder.buildDirection(transformation.axis3().id()).asVector();
            }
            Vector3 cross = axis1.cross(axis2);
            return cross.isZero() ? new Vector3(0.0, 0.0, 1.0) : cross.normalize().asVector();
        }

        private double transformationScale(com.minicad.step.model.StepCartesianTransformationOperator transformation) {
            return transformation.scale() == null ? 1.0 : transformation.scale();
        }

        private List<UvPoint> extractEdgeUvPoints(OrientedEdge orientedEdge, ParametricMapper mapper, SurfaceGeometry surface) {
            List<UvPoint> pcurvePoints = extractSurfaceCurveUvPoints(orientedEdge, mapper, surface);
            if (!pcurvePoints.isEmpty()) {
                return pcurvePoints;
            }
            List<CartesianPoint> points3d = orientSamples(orientedEdge, orientedEdge.edge().sample(DEFAULT_CURVE_SEGMENTS));
            List<UvPoint> uvPoints = new ArrayList<>();
            UvPoint previous = null;
            for (CartesianPoint point : points3d) {
                UvPoint uv = mapper.project(point, previous);
                if (uv == null) {
                    return List.of();
                }
                uvPoints.add(uv);
                previous = uv;
            }
            return uvPoints;
        }

        private List<UvPoint> extractSurfaceCurveUvPoints(OrientedEdge orientedEdge, ParametricMapper mapper, SurfaceGeometry surface) {
            if (!(orientedEdge.edge().curve() instanceof SurfaceCurve3 surfaceCurve)) {
                return List.of();
            }
            List<SurfaceCurve3.ParametricCurve> bindings = matchingParametricCurves(surfaceCurve, surface);
            if (bindings.isEmpty()) {
                return List.of();
            }
            UvPoint projectedStart = mapper.project(orientedEdge.startVertex().point(), null);
            UvPoint projectedEnd = projectedStart == null
                    ? mapper.project(orientedEdge.endVertex().point(), null)
                    : mapper.project(orientedEdge.endVertex().point(), projectedStart);
            List<UvPoint> best = List.of();
            double bestScore = Double.POSITIVE_INFINITY;
            for (SurfaceCurve3.ParametricCurve binding : bindings) {
                List<UvPoint> sampled = sampleCurve2(binding.curve2(), projectedStart, projectedEnd);
                if (sampled.isEmpty()) {
                    continue;
                }
                List<UvPoint> uvPoints = normalizePeriodicLoop(sampled, mapper);
                List<UvPoint> oriented = orientUvSamples(orientedEdge, uvPoints, mapper);
                if (oriented.size() < 2) {
                    continue;
                }
                double score = uvDistance(oriented.getFirst(), projectedStart) + uvDistance(oriented.getLast(), projectedEnd);
                if (best.isEmpty() || score < bestScore) {
                    best = oriented;
                    bestScore = score;
                }
            }
            return best;
        }

        private List<SurfaceCurve3.ParametricCurve> matchingParametricCurves(SurfaceCurve3 surfaceCurve, SurfaceGeometry surface) {
            List<SurfaceCurve3.ParametricCurve> matches = new ArrayList<>();
            for (SurfaceCurve3.ParametricCurve binding : surfaceCurve.parametricCurves()) {
                if (binding.surface().equals(surface)) {
                    matches.add(binding);
                }
            }
            return List.copyOf(matches);
        }

        private List<UvPoint> orientUvSamples(OrientedEdge orientedEdge, List<UvPoint> samples, ParametricMapper mapper) {
            if (samples.isEmpty()) {
                return List.of();
            }
            List<UvPoint> oriented = new ArrayList<>(samples);
            UvPoint expectedStart = mapper.project(orientedEdge.startVertex().point(), null);
            UvPoint expectedEnd = expectedStart == null
                    ? mapper.project(orientedEdge.endVertex().point(), null)
                    : mapper.project(orientedEdge.endVertex().point(), expectedStart);
            if (expectedStart != null) {
                oriented.set(0, alignToReference(expectedStart, oriented.getFirst(), mapper));
            }
            if (expectedEnd != null) {
                oriented.set(oriented.size() - 1, alignToReference(expectedEnd, oriented.getLast(), mapper));
            }
            double forward = uvDistance(oriented.getFirst(), expectedStart) + uvDistance(oriented.getLast(), expectedEnd);
            double backward = uvDistance(oriented.getFirst(), expectedEnd) + uvDistance(oriented.getLast(), expectedStart);
            if (backward < forward) {
                Collections.reverse(oriented);
            }
            if (expectedStart != null) {
                oriented.set(0, alignToReference(expectedStart, oriented.getFirst(), mapper));
            }
            if (expectedEnd != null) {
                oriented.set(oriented.size() - 1, alignToReference(expectedEnd, oriented.getLast(), mapper));
            }
            return List.copyOf(oriented);
        }

        private UvPoint alignToReference(UvPoint point, UvPoint reference, ParametricMapper mapper) {
            if (point == null || reference == null) {
                return point;
            }
            double u = point.u();
            double v = point.v();
            Double uPeriod = mapper.uPeriod();
            Double vPeriod = mapper.vPeriod();
            if (uPeriod != null) {
                while (u - reference.u() > uPeriod * 0.5) {
                    u -= uPeriod;
                }
                while (u - reference.u() < -uPeriod * 0.5) {
                    u += uPeriod;
                }
            }
            if (vPeriod != null) {
                while (v - reference.v() > vPeriod * 0.5) {
                    v -= vPeriod;
                }
                while (v - reference.v() < -vPeriod * 0.5) {
                    v += vPeriod;
                }
            }
            return new UvPoint(u, v);
        }

        private double uvDistance(UvPoint a, UvPoint b) {
            if (a == null || b == null) {
                return Double.POSITIVE_INFINITY;
            }
            double du = a.u() - b.u();
            double dv = a.v() - b.v();
            return du * du + dv * dv;
        }

        private UvPoint snapToLine(UvPoint point, Line2 line) {
            Point2 snapped = line.closestPoint(new Point2(point.u(), point.v()));
            return new UvPoint(snapped.x(), snapped.y());
        }

        private UvPoint snapToCircle(UvPoint point, Circle2 circle) {
            com.minicad.geometry2d.Vector2 offset = new Point2(point.u(), point.v()).subtract(circle.center());
            double norm = offset.norm();
            if (norm <= PLANAR_EPS) {
                Point2 fallback = circle.pointAt(0.0);
                return new UvPoint(fallback.x(), fallback.y());
            }
            Point2 snapped = circle.center().add(offset.scale(circle.radius() / norm));
            return new UvPoint(snapped.x(), snapped.y());
        }

        private UvPoint snapToEllipse(UvPoint point, Ellipse2 ellipse) {
            double angle = ellipse.angleOf(ellipse.pointAt(ellipse.angleOf(snapEllipseSeed(point, ellipse))));
            Point2 snapped = ellipse.pointAt(angle);
            return new UvPoint(snapped.x(), snapped.y());
        }

        private Point2 snapEllipseSeed(UvPoint point, Ellipse2 ellipse) {
            com.minicad.geometry2d.Vector2 offset = new Point2(point.u(), point.v()).subtract(ellipse.center());
            if (offset.norm() <= PLANAR_EPS) {
                return ellipse.pointAt(0.0);
            }
            com.minicad.geometry2d.Vector2 x = ellipse.xDirection().asVector();
            com.minicad.geometry2d.Vector2 y = new com.minicad.geometry2d.Vector2(-x.y(), x.x());
            double nx = offset.dot(x) / ellipse.semiAxis1();
            double ny = offset.dot(y) / ellipse.semiAxis2();
            double norm = Math.hypot(nx, ny);
            if (norm <= PLANAR_EPS) {
                return ellipse.pointAt(0.0);
            }
            double angle = Math.atan2(ny / norm, nx / norm);
            return ellipse.pointAt(angle);
        }

        private List<UvPoint> sampleCurve2(Curve2 curve, UvPoint start, UvPoint end) {
            if (curve instanceof Line2 line) {
                return sampleLinePcurve(line, start, end);
            }
            if (curve instanceof Circle2 circle) {
                return sampleCirclePcurve(circle, start, end);
            }
            if (curve instanceof Ellipse2 ellipse) {
                return sampleEllipsePcurve(ellipse, start, end);
            }
            if (curve instanceof BSplineCurve2 spline) {
                return sampleSplinePcurve(spline, start, end);
            }
            if (curve instanceof TrimmedCurve2 trimmed) {
                return sampleTrimmedPcurve(trimmed, start, end);
            }
            return List.of();
        }

        private List<UvPoint> sampleLinePcurve(Line2 line, UvPoint start, UvPoint end) {
            Point2 startPoint = new Point2(start.u(), start.v());
            Point2 endPoint = new Point2(end.u(), end.v());
            double startParameter = line.parameterOf(startPoint);
            double endParameter = line.parameterOf(endPoint);
            int segments = Math.max(12, (int) Math.ceil(Math.abs(endParameter - startParameter) * 6.0));
            List<UvPoint> points = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                double parameter = startParameter + (endParameter - startParameter) * index / segments;
                Point2 point = line.pointAt(parameter);
                points.add(new UvPoint(point.x(), point.y()));
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }

        private List<UvPoint> sampleSplinePcurve(BSplineCurve2 spline, UvPoint start, UvPoint end) {
            List<Point2> sampled = spline.sample(48);
            if (sampled.size() < 2) {
                return List.of();
            }
            int startIndex = closestPointIndex(sampled, start);
            int endIndex = closestPointIndex(sampled, end);
            if (startIndex == endIndex) {
                return List.of(start, end);
            }
            List<UvPoint> points = new ArrayList<>();
            int step = startIndex <= endIndex ? 1 : -1;
            for (int index = startIndex; index != endIndex + step; index += step) {
                Point2 point = sampled.get(index);
                points.add(new UvPoint(point.x(), point.y()));
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }

        private int closestPointIndex(List<Point2> points, UvPoint target) {
            int bestIndex = 0;
            double bestDistance = Double.POSITIVE_INFINITY;
            for (int index = 0; index < points.size(); index++) {
                Point2 point = points.get(index);
                double du = point.x() - target.u();
                double dv = point.y() - target.v();
                double distance = du * du + dv * dv;
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestIndex = index;
                }
            }
            return bestIndex;
        }

        private List<UvPoint> sampleCirclePcurve(Circle2 circle, UvPoint start, UvPoint end) {
            Point2 startPoint = new Point2(start.u(), start.v());
            Point2 endPoint = new Point2(end.u(), end.v());
            double startAngle = circle.angleOf(startPoint);
            double endAngle = circle.angleOf(endPoint);
            double delta = endAngle - startAngle;
            if (delta > Math.PI) {
                delta -= Math.PI * 2.0;
            } else if (delta < -Math.PI) {
                delta += Math.PI * 2.0;
            }
            int segments = Math.max(18, (int) Math.ceil(Math.abs(delta) * 18.0));
            List<UvPoint> points = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                double angle = startAngle + delta * index / segments;
                Point2 point = circle.pointAt(angle);
                points.add(new UvPoint(point.x(), point.y()));
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }

        private List<UvPoint> sampleEllipsePcurve(Ellipse2 ellipse, UvPoint start, UvPoint end) {
            Point2 startPoint = new Point2(start.u(), start.v());
            Point2 endPoint = new Point2(end.u(), end.v());
            double startAngle = ellipse.angleOf(startPoint);
            double endAngle = ellipse.angleOf(endPoint);
            double delta = endAngle - startAngle;
            if (delta > Math.PI) {
                delta -= Math.PI * 2.0;
            } else if (delta < -Math.PI) {
                delta += Math.PI * 2.0;
            }
            int segments = Math.max(18, (int) Math.ceil(Math.abs(delta) * 18.0));
            List<UvPoint> points = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                double angle = startAngle + delta * index / segments;
                Point2 point = ellipse.pointAt(angle);
                points.add(new UvPoint(point.x(), point.y()));
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }

        private List<UvPoint> sampleTrimmedPcurve(TrimmedCurve2 trimmed, UvPoint start, UvPoint end) {
            UvPoint trimStart = new UvPoint(trimmed.trimStart().x(), trimmed.trimStart().y());
            UvPoint trimEnd = new UvPoint(trimmed.trimEnd().x(), trimmed.trimEnd().y());
            List<UvPoint> forward = sampleCurve2(trimmed.basisCurve(), trimStart, trimEnd);
            List<UvPoint> reverse = sampleCurve2(trimmed.basisCurve(), trimEnd, trimStart);
            if (forward.isEmpty() && reverse.isEmpty()) {
                return List.of();
            }
            List<UvPoint> preferred;
            if (!trimmed.senseAgreement()) {
                preferred = reverse.isEmpty() ? forward : reverse;
            } else {
                preferred = score(start, end, forward) <= score(start, end, reverse) ? forward : reverse;
            }
            return alignTrimmedSamples(preferred, start, end);
        }

        private double score(UvPoint start, UvPoint end, List<UvPoint> samples) {
            if (samples.isEmpty()) {
                return Double.POSITIVE_INFINITY;
            }
            return uvDistance(start, samples.getFirst()) + uvDistance(end, samples.getLast());
        }

        private List<UvPoint> alignTrimmedSamples(List<UvPoint> samples, UvPoint start, UvPoint end) {
            if (samples.isEmpty()) {
                return samples;
            }
            List<UvPoint> aligned = new ArrayList<>(samples);
            double forwardScore = uvDistance(start, aligned.getFirst()) + uvDistance(end, aligned.getLast());
            double reverseScore = uvDistance(start, aligned.getLast()) + uvDistance(end, aligned.getFirst());
            if (reverseScore < forwardScore) {
                Collections.reverse(aligned);
            }
            aligned.set(0, start);
            aligned.set(aligned.size() - 1, end);
            return List.copyOf(aligned);
        }

        private List<UvPoint> normalizePeriodicLoop(List<UvPoint> points, ParametricMapper mapper) {
            if (points.size() < 2) {
                return points;
            }
            Double uPeriod = mapper.uPeriod();
            Double vPeriod = mapper.vPeriod();
            List<UvPoint> normalized = new ArrayList<>(points.size());
            UvPoint previous = null;
            for (UvPoint point : points) {
                double u = point.u();
                double v = point.v();
                if (previous != null) {
                    if (uPeriod != null) {
                        while (u - previous.u() > uPeriod * 0.5) {
                            u -= uPeriod;
                        }
                        while (u - previous.u() < -uPeriod * 0.5) {
                            u += uPeriod;
                        }
                    }
                    if (vPeriod != null) {
                        while (v - previous.v() > vPeriod * 0.5) {
                            v -= vPeriod;
                        }
                        while (v - previous.v() < -vPeriod * 0.5) {
                            v += vPeriod;
                        }
                    }
                }
                UvPoint normalizedPoint = new UvPoint(u, v);
                normalized.add(normalizedPoint);
                previous = normalizedPoint;
            }
            return normalized;
        }

        private List<ParametricLoop> normalizeLoopRoles(List<ParametricLoop> loops) {
            if (loops.stream().anyMatch(ParametricLoop::outer)) {
                return loops;
            }
            int outerIndex = -1;
            double outerArea = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < loops.size(); i++) {
                double area = Math.abs(signedAreaUv(loops.get(i).points()));
                if (area > outerArea + PLANAR_EPS) {
                    outerArea = area;
                    outerIndex = i;
                }
            }
            if (outerIndex < 0) {
                return loops;
            }
            List<ParametricLoop> normalized = new ArrayList<>(loops.size());
            for (int i = 0; i < loops.size(); i++) {
                normalized.add(new ParametricLoop(i == outerIndex, loops.get(i).points()));
            }
            return List.copyOf(normalized);
        }

        private List<ParametricLoop> normalizeLoopPeriods(List<ParametricLoop> loops, ParametricMapper mapper) {
            if (loops.isEmpty()) {
                return loops;
            }
            ParametricLoop outer = loops.stream().filter(ParametricLoop::outer).findFirst().orElse(null);
            if (outer == null) {
                return loops;
            }
            Double uPeriod = mapper.uPeriod();
            Double vPeriod = mapper.vPeriod();
            if (uPeriod == null && vPeriod == null) {
                return loops;
            }
            UvPoint outerCenter = centroidUv(outer.points());
            List<ParametricLoop> normalized = new ArrayList<>(loops.size());
            for (ParametricLoop loop : loops) {
                if (loop.outer()) {
                    normalized.add(loop);
                    continue;
                }
                UvPoint center = centroidUv(loop.points());
                int bestUShift = 0;
                int bestVShift = 0;
                double bestDistance = uvDistance(center, outerCenter);
                for (int uShift = -1; uShift <= 1; uShift++) {
                    if (uPeriod == null && uShift != 0) {
                        continue;
                    }
                    for (int vShift = -1; vShift <= 1; vShift++) {
                        if (vPeriod == null && vShift != 0) {
                            continue;
                        }
                        UvPoint shiftedCenter = new UvPoint(
                                center.u() + (uPeriod == null ? 0.0 : uPeriod * uShift),
                                center.v() + (vPeriod == null ? 0.0 : vPeriod * vShift));
                        double distance = uvDistance(shiftedCenter, outerCenter);
                        if (distance + PLANAR_EPS < bestDistance) {
                            bestDistance = distance;
                            bestUShift = uShift;
                            bestVShift = vShift;
                        }
                    }
                }
                double du = uPeriod == null ? 0.0 : uPeriod * bestUShift;
                double dv = vPeriod == null ? 0.0 : vPeriod * bestVShift;
                List<UvPoint> shifted = loop.points().stream()
                        .map(point -> new UvPoint(point.u() + du, point.v() + dv))
                        .toList();
                normalized.add(new ParametricLoop(false, shifted));
            }
            return List.copyOf(normalized);
        }

        private UvPoint centroidUv(List<UvPoint> points) {
            if (points.isEmpty()) {
                return new UvPoint(0.0, 0.0);
            }
            int count = points.size();
            if (count > 1 && sameUv(points.getFirst(), points.getLast())) {
                count--;
            }
            if (count <= 0) {
                return points.getFirst();
            }
            double sumU = 0.0;
            double sumV = 0.0;
            for (int i = 0; i < count; i++) {
                sumU += points.get(i).u();
                sumV += points.get(i).v();
            }
            return new UvPoint(sumU / count, sumV / count);
        }

        private UvBounds boundsOf(List<ParametricLoop> loops) {
            double minU = Double.POSITIVE_INFINITY;
            double maxU = Double.NEGATIVE_INFINITY;
            double minV = Double.POSITIVE_INFINITY;
            double maxV = Double.NEGATIVE_INFINITY;
            boolean found = false;
            for (ParametricLoop loop : loops) {
                for (UvPoint point : loop.points()) {
                    minU = Math.min(minU, point.u());
                    maxU = Math.max(maxU, point.u());
                    minV = Math.min(minV, point.v());
                    maxV = Math.max(maxV, point.v());
                    found = true;
                }
            }
            return found ? new UvBounds(minU, maxU, minV, maxV) : null;
        }

        private boolean containsParametricLoops(List<ParametricLoop> loops, UvPoint point) {
            ParametricLoop outer = loops.stream().filter(ParametricLoop::outer).findFirst().orElse(null);
            if (outer == null || !containsUvPolygon(outer.points(), point)) {
                return false;
            }
            for (ParametricLoop hole : loops) {
                if (!hole.outer() && containsUvPolygon(hole.points(), point)) {
                    return false;
                }
            }
            return true;
        }

        private boolean containsUvPolygon(List<UvPoint> polygon, UvPoint point) {
            if (polygon.size() < 3) {
                return false;
            }
            if (isOnPolygonBoundary(polygon, point)) {
                return true;
            }
            boolean inside = false;
            for (int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
                UvPoint a = polygon.get(i);
                UvPoint b = polygon.get(j);
                boolean intersects = ((a.v() > point.v()) != (b.v() > point.v()))
                        && point.u() < (b.u() - a.u()) * (point.v() - a.v()) / ((b.v() - a.v()) + 1.0e-12) + a.u();
                if (intersects) {
                    inside = !inside;
                }
            }
            return inside;
        }

        private boolean isOnPolygonBoundary(List<UvPoint> polygon, UvPoint point) {
            for (int i = 0; i + 1 < polygon.size(); i++) {
                if (isOnSegment(polygon.get(i), polygon.get(i + 1), point)) {
                    return true;
                }
            }
            return false;
        }

        private boolean isOnSegment(UvPoint a, UvPoint b, UvPoint point) {
            double abU = b.u() - a.u();
            double abV = b.v() - a.v();
            double lengthSquared = abU * abU + abV * abV;
            if (lengthSquared <= 1.0e-18) {
                return distanceSquared(a, point) <= 1.0e-18;
            }
            double apU = point.u() - a.u();
            double apV = point.v() - a.v();
            double cross = abU * apV - abV * apU;
            if (Math.abs(cross) > 1.0e-9) {
                return false;
            }
            double dot = apU * abU + apV * abV;
            if (dot < -1.0e-9) {
                return false;
            }
            return dot <= lengthSquared + 1.0e-9;
        }

        private boolean sameUv(UvPoint left, UvPoint right) {
            return distanceSquared(left, right) <= 1.0e-12;
        }

        private double distanceSquared(UvPoint left, UvPoint right) {
            double du = left.u() - right.u();
            double dv = left.v() - right.v();
            return du * du + dv * dv;
        }

        private double signedAreaUv(List<UvPoint> points) {
            if (points.size() < 3) {
                return 0.0;
            }
            double area = 0.0;
            for (int i = 0; i + 1 < points.size(); i++) {
                UvPoint current = points.get(i);
                UvPoint next = points.get(i + 1);
                area += current.u() * next.v() - next.u() * current.v();
            }
            return area * 0.5;
        }

        private double triangleArea(CartesianPoint a, CartesianPoint b, CartesianPoint c) {
            return b.subtract(a).cross(c.subtract(a)).norm() * 0.5;
        }

        private record PlanarFrame(CartesianPoint origin, Vector3 xAxis, Vector3 yAxis) {
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

        MeshData toMeshData() {
            List<double[]> v = new ArrayList<>(vertexIndex.size());
            List<double[]> n = new ArrayList<>(vertexIndex.size());
            // Invert the map: index -> MeshVertex
            MeshVertex[] byIndex = new MeshVertex[vertexIndex.size()];
            for (Map.Entry<MeshVertex, Integer> e : vertexIndex.entrySet()) {
                byIndex[e.getValue()] = e.getKey();
            }
            for (MeshVertex mv : byIndex) {
                v.add(new double[]{mv.x(), mv.y(), mv.z()});
                n.add(new double[]{mv.nx(), mv.ny(), mv.nz()});
            }
            return new MeshData(v, n, faceIndices);
        }
    }

    public record MeshData(
            List<double[]> vertices,
            List<double[]> normals,
            List<int[]> triangles
    ) {}

    // ── OBJ Format ────────────────────────────────────────────────────────────

    private static String formatObj(MeshData mesh) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Generated by MiniCAD STEP Mesh Exporter\n");

        for (double[] v : mesh.vertices) {
            sb.append(String.format(Locale.US, "v %.6f %.6f %.6f\n", v[0], v[1], v[2]));
        }

        for (double[] n : mesh.normals) {
            sb.append(String.format(Locale.US, "vn %.6f %.6f %.6f\n", n[0], n[1], n[2]));
        }

        for (int[] tri : mesh.triangles) {
            int v0 = tri[0] + 1;
            int v1 = tri[1] + 1;
            int v2 = tri[2] + 1;
            sb.append(String.format(Locale.US, "f %d//%d %d//%d %d//%d\n", v0, v0, v1, v1, v2, v2));
        }

        return sb.toString();
    }

    // ── STL Binary Format ────────────────────────────────────────────────────

    private static byte[] formatStlBinary(MeshData mesh) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.writeBytes(new byte[80]);

        ByteBuffer bb = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(mesh.triangles.size());
        baos.writeBytes(bb.array());

        bb = ByteBuffer.allocate(50).order(ByteOrder.LITTLE_ENDIAN);
        for (int[] tri : mesh.triangles) {
            double[] n = mesh.normals.get(tri[0]);
            bb.putFloat((float) n[0]);
            bb.putFloat((float) n[1]);
            bb.putFloat((float) n[2]);

            for (int vi : tri) {
                double[] v = mesh.vertices.get(vi);
                bb.putFloat((float) v[0]);
                bb.putFloat((float) v[1]);
                bb.putFloat((float) v[2]);
            }

            bb.putShort((short) 0);
            baos.writeBytes(bb.array());
            bb.clear();
        }

        return baos.toByteArray();
    }

    // ── STL Text Format ──────────────────────────────────────────────────────

    private static String formatStlText(MeshData mesh) {
        StringBuilder sb = new StringBuilder();
        sb.append("solid MiniCAD\n");

        for (int[] tri : mesh.triangles) {
            double[] n = mesh.normals.get(tri[0]);
            sb.append(String.format(Locale.US, "  facet normal %.6f %.6f %.6f\n", n[0], n[1], n[2]));
            sb.append("    outer loop\n");
            for (int vi : tri) {
                double[] v = mesh.vertices.get(vi);
                sb.append(String.format(Locale.US, "      vertex %.6f %.6f %.6f\n", v[0], v[1], v[2]));
            }
            sb.append("    endloop\n");
            sb.append("  endfacet\n");
        }

        sb.append("endsolid MiniCAD\n");
        return sb.toString();
    }
}
