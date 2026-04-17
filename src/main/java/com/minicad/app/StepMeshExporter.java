package com.minicad.app;

import com.minicad.geometry.*;
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
            int id = entry.getKey();
            StepEntity entity = entry.getValue();
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
            } else {
                triangulateCurvedFace(face, surface, flipped);
            }
        }

        private void triangulatePlanarFace(Face face, Plane plane, boolean flipped) {
            FaceBound outer = face.outerBound();
            if (outer == null) return;

            List<CartesianPoint> loopPoints = extractLoopPoints(outer.loop());
            if (loopPoints.size() < 3) return;

            Vector3 normal = plane.normal().asVector();
            if (flipped) normal = normal.negate();

            int v0 = addVertex(loopPoints.get(0), normal);
            for (int i = 1; i < loopPoints.size() - 1; i++) {
                int v1 = addVertex(loopPoints.get(i), normal);
                int v2 = addVertex(loopPoints.get(i + 1), normal);
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

        private List<CartesianPoint> extractLoopPoints(Loop loop) {
            if (loop instanceof PolyLoop poly) {
                return poly.points();
            }
            if (loop instanceof EdgeLoop edgeLoop) {
                List<CartesianPoint> points = new ArrayList<>();
                for (OrientedEdge oe : edgeLoop.edges()) {
                    Edge e = oe.edge();
                    if (points.isEmpty() || !points.getLast().equals(e.start().point())) {
                        points.add(e.start().point());
                    }
                    points.add(e.end().point());
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
