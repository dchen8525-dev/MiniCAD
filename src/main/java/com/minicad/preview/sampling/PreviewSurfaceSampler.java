package com.minicad.preview.sampling;

import com.minicad.export.glb.PreviewMeshExporter;
import com.minicad.geometry.*;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.SurfacePatch;
import com.minicad.step.model.*;
import com.minicad.step.semantic.StepCadBuilder;

import java.util.List;

/**
 * Surface parameterization, grid sampling, and triangulation -- the preview-side
 * names for work whose bodies live elsewhere. Every method here is a one-line
 * delegation, and none of the delegated-to types is imported from the export
 * side any more: the last one that was,
 * {@code StepEdgePayloadBuilder.sampleOrientedEdge}, only had the four-sided
 * patch builder as a caller.
 *
 * <p>The four-sided patch family left in one piece. {@code buildFourSidedPatch}
 * here was a byte-identical copy of the package-private builder in
 * {@code StepEdgePayloadBuilder}, and this file already treated that class as
 * the home for edge sampling. Its only caller was
 * {@code PreviewFaceBuilder.toFourSidedPatchFacePayload}, which had no caller
 * at all, so the copy went rather than growing a delegating facade -- the
 * export-side builder is the live one, reached from
 * {@code StepFacePayloadBuilder.toBSplineSurfaceFacePayload}.
 * {@code cornersMatch}, {@code close} and the two
 * {@code StepGeometryHelper} facades followed it, as did
 * {@code sampleOrientedEdge}, whose only caller it was.
 */
public final class PreviewSurfaceSampler {

    private PreviewSurfaceSampler() {}

    // ─── Surface grid sampling ───────────────────────────────────────────

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
}
