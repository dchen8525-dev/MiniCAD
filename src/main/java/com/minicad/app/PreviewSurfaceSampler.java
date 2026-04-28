package com.minicad.app;

import com.minicad.common.Epsilon;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.*;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.*;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.topology.EdgeLoop;

import java.util.ArrayList;
import java.util.List;

/** Surface parameterization, grid sampling, and triangulation.
 *  Extracted from StepPreviewJsonExporter to isolate sampling logic. */
public final class PreviewSurfaceSampler {

    private PreviewSurfaceSampler() {}

    public static final int MAX_TOTAL_TRIANGLE_POINTS = 6_000_000;

    // ─── Surface grid sampling ───────────────────────────────────────────

    public static List<List<CartesianPoint>> sampleTopologySurfaceGrid(SurfaceGeometry surface, int segments) {
        return surface.sampleGrid(segments, segments);
    }

    public static List<List<CartesianPoint>> sampleSurfaceGrid(BSplineSurface3 surface, int uSegments, int vSegments) {
        return surface.sampleGrid(Math.max(uSegments, 2), Math.max(vSegments, 2));
    }

    public static List<List<CartesianPoint>> sampleSurfaceGrid(RationalBSplineSurface3 surface, int uSegments, int vSegments) {
        return surface.sampleGrid(Math.max(uSegments, 2), Math.max(vSegments, 2));
    }

    // ─── B-spline surface construction ───────────────────────────────────

    public static BSplineSurface3 buildBsplineSurface(StepEntity geometry, StepCadBuilder builder) {
        if (geometry instanceof StepBSplineSurfaceWithKnots splineSurface) {
            return builder.buildBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurface splineSurface) {
            return builder.buildGenericBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints splineSurface) {
            return builder.buildBSplineSurfaceWithBreakpoints(splineSurface.id());
        }
        if (geometry instanceof StepBezierSurface splineSurface) {
            return builder.buildBezierSurface(splineSurface.id());
        }
        if (geometry instanceof StepUniformSurface splineSurface) {
            return builder.buildUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepQuasiUniformSurface splineSurface) {
            return builder.buildQuasiUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepPiecewiseBezierSurface splineSurface) {
            return builder.buildPiecewiseBezierSurface(splineSurface.id());
        }
        throw new UnsupportedGeometryException(surfaceTypeName(geometry) + " is not a supported B-spline-like surface");
    }

    public static BSplineSurface3 buildFreeFormSurface(StepFreeFormSurface surface, StepCadBuilder builder) {
        int uCount = surface.controlPoints().size();
        int vCount = surface.controlPoints().isEmpty() ? 0 : surface.controlPoints().getFirst().size();
        if (uCount < 2 || vCount < 2) {
            throw new UnsupportedGeometryException("FREE_FORM_SURFACE requires at least 2x2 control points");
        }
        List<List<CartesianPoint>> controlPoints = new ArrayList<>(uCount);
        for (List<StepEntity> row : surface.controlPoints()) {
            List<CartesianPoint> pointRow = new ArrayList<>(row.size());
            for (StepEntity pt : row) {
                if (pt instanceof StepCartesianPoint cartesianPoint) {
                    pointRow.add(builder.buildPoint(cartesianPoint.id()));
                } else {
                    throw new UnsupportedGeometryException("FREE_FORM_SURFACE control points must be Cartesian points");
                }
            }
            controlPoints.add(List.copyOf(pointRow));
        }
        int uDegree = surface.degreeU();
        int vDegree = surface.degreeV();
        int uKnotCount = uCount + uDegree + 1;
        int vKnotCount = vCount + vDegree + 1;
        List<Double> uKnots = new ArrayList<>();
        for (int i = 0; i < uKnotCount; i++) {
            uKnots.add((double) i / (uKnotCount - 1));
        }
        List<Double> vKnots = new ArrayList<>();
        for (int i = 0; i < vKnotCount; i++) {
            vKnots.add((double) i / (vKnotCount - 1));
        }
        List<Integer> uMults = List.of(1);
        List<Integer> vMults = List.of(1);
        return new BSplineSurface3(uDegree, vDegree, controlPoints, uMults, vMults, uKnots, vKnots);
    }

    // ─── Triangulation ───────────────────────────────────────────────────

    public static List<PointPayload> triangulatePatch(SurfacePatch patch, boolean sameSense) {
        List<PointPayload> triangles = new ArrayList<>();
        for (int u = 0; u < patch.uSegments(); u++) {
            for (int v = 0; v < patch.vSegments(); v++) {
                CartesianPoint p00 = patch.pointAt((double) u / patch.uSegments(), (double) v / patch.vSegments());
                CartesianPoint p10 = patch.pointAt((double) (u + 1) / patch.uSegments(), (double) v / patch.vSegments());
                CartesianPoint p01 = patch.pointAt((double) u / patch.uSegments(), (double) (v + 1) / patch.vSegments());
                CartesianPoint p11 = patch.pointAt((double) (u + 1) / patch.uSegments(), (double) (v + 1) / patch.vSegments());
                Vector3 targetNormal = patch.normalAt((u + 0.5) / patch.uSegments(), (v + 0.5) / patch.vSegments());
                if (!sameSense) {
                    targetNormal = targetNormal.scale(-1.0);
                }
                appendOrientedTriangle(triangles, p00, p10, p11, targetNormal);
                appendOrientedTriangle(triangles, p00, p11, p01, targetNormal);
            }
        }
        return List.copyOf(triangles);
    }

    public static List<PointPayload> triangulateSurfaceGrid(List<List<CartesianPoint>> grid, boolean sameSense) {
        List<PointPayload> triangles = new ArrayList<>();
        if (grid.size() < 2 || grid.getFirst().size() < 2) {
            return List.of();
        }
        for (int u = 0; u + 1 < grid.size(); u++) {
            for (int v = 0; v + 1 < grid.get(u).size(); v++) {
                CartesianPoint p00 = grid.get(u).get(v);
                CartesianPoint p10 = grid.get(u + 1).get(v);
                CartesianPoint p01 = grid.get(u).get(v + 1);
                CartesianPoint p11 = grid.get(u + 1).get(v + 1);
                Vector3 targetNormal = p10.subtract(p00).cross(p01.subtract(p00));
                if (targetNormal.norm() <= Epsilon.EPS) {
                    continue;
                }
                if (!sameSense) {
                    targetNormal = targetNormal.scale(-1.0);
                }
                appendOrientedTriangle(triangles, p00, p10, p11, targetNormal);
                appendOrientedTriangle(triangles, p00, p11, p01, targetNormal);
            }
        }
        return List.copyOf(triangles);
    }

    // ─── Four-sided patch construction ───────────────────────────────────

    public static SurfacePatch buildFourSidedPatch(EdgeLoop outerLoop) {
        if (outerLoop.edges().size() != 4) {
            return null;
        }
        List<CartesianPoint> bottom = sampleOrientedEdge(outerLoop.edges().get(0));
        List<CartesianPoint> right = sampleOrientedEdge(outerLoop.edges().get(1));
        List<CartesianPoint> top = reversed(sampleOrientedEdge(outerLoop.edges().get(2)));
        List<CartesianPoint> left = reversed(sampleOrientedEdge(outerLoop.edges().get(3)));
        if (!cornersMatch(bottom, right, top, left)) {
            return null;
        }
        int uSegments = Math.max(Math.max(bottom.size(), top.size()) - 1, 8);
        int vSegments = Math.max(Math.max(left.size(), right.size()) - 1, 8);
        return new SurfacePatch(
                resamplePolyline(bottom, uSegments),
                resamplePolyline(top, uSegments),
                resamplePolyline(left, vSegments),
                resamplePolyline(right, vSegments)
        );
    }

    // ─── Polyline utilities ──────────────────────────────────────────────

    private static boolean cornersMatch(
            List<CartesianPoint> bottom,
            List<CartesianPoint> right,
            List<CartesianPoint> top,
            List<CartesianPoint> left
    ) {
        return close(bottom.getFirst(), left.getFirst())
                && close(bottom.getLast(), right.getFirst())
                && close(top.getFirst(), left.getLast())
                && close(top.getLast(), right.getLast());
    }

    private static boolean close(CartesianPoint left, CartesianPoint right) {
        return left.distanceTo(right) <= 1.0e-6;
    }

    private static List<CartesianPoint> reversed(List<CartesianPoint> points) {
        List<CartesianPoint> copy = new ArrayList<>(points);
        java.util.Collections.reverse(copy);
        return List.copyOf(copy);
    }

    private static List<CartesianPoint> resamplePolyline(List<CartesianPoint> points, int segments) {
        if (points.size() < 2) {
            return List.of(points.getFirst());
        }
        List<Double> lengths = new ArrayList<>(points.size());
        lengths.add(0.0);
        for (int i = 1; i < points.size(); i++) {
            lengths.add(lengths.get(i - 1) + points.get(i - 1).distanceTo(points.get(i)));
        }
        double total = lengths.getLast();
        if (total <= Epsilon.EPS) {
            return java.util.Collections.nCopies(segments + 1, points.getFirst());
        }
        List<CartesianPoint> result = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double target = total * i / segments;
            result.add(pointAtDistance(points, lengths, target));
        }
        result.set(0, points.getFirst());
        result.set(result.size() - 1, points.getLast());
        return List.copyOf(result);
    }

    private static CartesianPoint pointAtDistance(List<CartesianPoint> points, List<Double> lengths, double target) {
        for (int i = 1; i < lengths.size(); i++) {
            if (target <= lengths.get(i)) {
                double start = lengths.get(i - 1);
                double segment = lengths.get(i) - start;
                double alpha = segment <= Epsilon.EPS ? 0.0 : (target - start) / segment;
                return interpolate(points.get(i - 1), points.get(i), alpha);
            }
        }
        return points.getLast();
    }

    private static CartesianPoint interpolate(CartesianPoint a, CartesianPoint b, double alpha) {
        return new CartesianPoint(
                a.x() * (1.0 - alpha) + b.x() * alpha,
                a.y() * (1.0 - alpha) + b.y() * alpha,
                a.z() * (1.0 - alpha) + b.z() * alpha
        );
    }

    private static void appendOrientedTriangle(
            List<PointPayload> triangles,
            CartesianPoint a,
            CartesianPoint b,
            CartesianPoint c,
            Vector3 targetNormal
    ) {
        Vector3 normal = b.subtract(a).cross(c.subtract(a));
        if (normal.dot(targetNormal) < 0.0) {
            triangles.add(toPointPayload(a));
            triangles.add(toPointPayload(c));
            triangles.add(toPointPayload(b));
            return;
        }
        triangles.add(toPointPayload(a));
        triangles.add(toPointPayload(b));
        triangles.add(toPointPayload(c));
    }

    // ─── Delegate methods needed from StepPreviewJsonExporter ────────────
    // These are called by the methods above and must remain accessible.
    // They will be updated to delegate once the facade is finalized.

    private static String surfaceTypeName(StepEntity entity) {
        // Temporary: delegate to StepPreviewJsonExporter until facade extraction
        return StepPreviewJsonExporter.surfaceTypeName(entity);
    }

    private static List<CartesianPoint> sampleOrientedEdge(com.minicad.topology.OrientedEdge edge) {
        // Temporary: delegate to StepPreviewJsonExporter
        return StepPreviewJsonExporter.sampleOrientedEdge(edge);
    }

    private static PointPayload toPointPayload(CartesianPoint point) {
        // Temporary: delegate to StepPreviewJsonExporter
        return StepPreviewJsonExporter.toPointPayload(point);
    }
}
