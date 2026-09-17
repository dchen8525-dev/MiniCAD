package com.minicad.preview.sampling;

import com.minicad.export.json.StepGeometryHelper;
import com.minicad.export.json.StepPreviewJsonExporter;
import com.minicad.geometry.*;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.SurfacePatch;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.*;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.topology.EdgeLoop;

import java.util.List;
import com.minicad.export.json.StepEdgePayloadBuilder;
import com.minicad.export.glb.PreviewMeshExporter;

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
        return PreviewMeshExporter.buildBsplineSurface(geometry, builder);
    }

    public static BSplineSurface3 buildFreeFormSurface(StepFreeFormSurface surface, StepCadBuilder builder) {
        return PreviewMeshExporter.buildFreeFormSurface(surface, builder);
    }

    // ─── Triangulation ───────────────────────────────────────────────────

    public static List<PointPayload> triangulatePatch(SurfacePatch patch, boolean sameSense) {
        return TriangulationHelper.triangulatePatch(patch, sameSense);
    }

    public static List<PointPayload> triangulateSurfaceGrid(List<List<CartesianPoint>> grid, boolean sameSense) {
        return TriangulationHelper.triangulateSurfaceGrid(grid, sameSense);
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
        return close(bottom.get(0), left.get(0))
                && close(bottom.get(bottom.size() - 1), right.get(0))
                && close(top.get(0), left.get(left.size() - 1))
                && close(top.get(top.size() - 1), right.get(right.size() - 1));
    }

    private static boolean close(CartesianPoint left, CartesianPoint right) {
        return left.distanceTo(right) <= 1.0e-6;
    }

    private static List<CartesianPoint> reversed(List<CartesianPoint> points) {
        return StepGeometryHelper.reversed(points);
    }

    private static List<CartesianPoint> resamplePolyline(List<CartesianPoint> points, int segments) {
        return StepGeometryHelper.resamplePolyline(points, segments);
    }

    // ─── Facade for StepEdgePayloadBuilder's edge sampling ──────────────

    private static List<CartesianPoint> sampleOrientedEdge(com.minicad.topology.OrientedEdge edge) {
        return StepEdgePayloadBuilder.sampleOrientedEdge(edge);
    }

}
