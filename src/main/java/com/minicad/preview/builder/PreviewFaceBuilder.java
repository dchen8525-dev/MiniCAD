package com.minicad.preview.builder;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.export.json.StepEdgePayloadBuilder;
import com.minicad.export.json.StepPayloadBuilder;
import com.minicad.geometry.*;
import com.minicad.step.model.*;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.FaceBound;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.VertexLoop;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * What the preview pipeline still needs from a STEP face, and nothing else:
 * the wrapper-surface unwrap rules, closed-loop sampling, and the geometry
 * surface type names. Extracted from StepPreviewJsonExporter to isolate face
 * and geometry logic.
 *
 * <p>Everything else this class used to carry has been removed as dead twins
 * of live export-side code. Twenty-three members went in one pass, each
 * because its last production caller was one of the others: the nine
 * {@code toXxxFacePayload} handlers were byte-identical copies of the private
 * handlers {@code StepFacePayloadBuilder.PREVIEW_FACE_RULES} dispatches, and
 * the rest -- {@code buildFaceBounds}, the two
 * {@code describeUnsupportedPreviewSurface} overloads, {@code faceGeometry},
 * {@code faceSameSense}, {@code toUnsupportedFacePayload},
 * {@code resolveEdgeColor}, {@code buildTopologyEdgePayload},
 * {@code shellFaces}, {@code isShellEntity}, {@code isShellLikeEntity},
 * {@code computeNormal}, {@code toColorPayload}, {@code toPbrPayload},
 * {@code toPointPayload}, {@code toPointPayloads} and {@code sampleEdge} --
 * was reachable only from them, from each other, or from nothing at all.
 * The class shrank from 860 to 183 lines without losing an entry point: the
 * four live callers (two from {@code PreviewMeshExporter}, two from the
 * export-side payload builders) are all on the members that remain.
 */
public final class PreviewFaceBuilder {

    private PreviewFaceBuilder() {}

    // ─── Surface unwrapping ──────────────────────────────────────────────

    private record SurfaceUnwrapRule(
            Class<?> type,
            Predicate<StepEntity> guard,
            Function<StepEntity, StepEntity> basis
    ) {
        boolean matches(StepEntity surface) {
            return type.isInstance(surface) && (guard == null || guard.test(surface));
        }
    }

    private static SurfaceUnwrapRule unwrapRule(Class<?> type, Function<StepEntity, StepEntity> basis) {
        return new SurfaceUnwrapRule(type, null, basis);
    }

    /**
     * Wrapper-surface types unwrapped to the surface they reference. The two
     * former 11-branch if/else-if chains that walked them -- one here for
     * unwrapParametricPreviewSurface, one on the export side in
     * describeUnsupportedPreviewSurface -- are both replaced by this table,
     * which the surviving callers share.
     */
    private static final List<SurfaceUnwrapRule> SURFACE_UNWRAP_RULES = List.of(
            unwrapRule(StepRectangularTrimmedSurface.class, surface -> ((StepRectangularTrimmedSurface) surface).basisSurface()),
            unwrapRule(StepCurveBoundedSurface.class, surface -> ((StepCurveBoundedSurface) surface).basisSurface()),
            unwrapRule(StepOrientedSurface.class, surface -> ((StepOrientedSurface) surface).surfaceElement()),
            unwrapRule(StepOffsetSurface.class, surface -> ((StepOffsetSurface) surface).basisSurface()),
            unwrapRule(StepOffsetSurface2.class, surface -> ((StepOffsetSurface2) surface).basisSurface()),
            unwrapRule(StepSurfacePatch.class, surface -> ((StepSurfacePatch) surface).basisSurface()),
            unwrapRule(StepRectangularCompositeSurface.class, surface -> ((StepRectangularCompositeSurface) surface).parentSurface()),
            unwrapRule(StepMachinedSurface.class, surface -> ((StepMachinedSurface) surface).face()),
            unwrapRule(StepBlendedSurface.class, surface -> ((StepBlendedSurface) surface).primarySurface()),
            unwrapRule(StepMappedItem.class, surface -> ((StepMappedItem) surface).mappingTarget()),
            new SurfaceUnwrapRule(StepGeometricReplica.class,
                    surface -> "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName()),
                    surface -> ((StepGeometricReplica) surface).parent())
    );

    /** Basis surface of a wrapper type, or null when the surface is terminal. */
    private static StepEntity unwrapBasisSurface(StepEntity surface) {
        for (SurfaceUnwrapRule rule : SURFACE_UNWRAP_RULES) {
            if (rule.matches(surface)) {
                return rule.basis().apply(surface);
            }
        }
        return null;
    }

    /** Single-level unwrap access for other classes reusing SURFACE_UNWRAP_RULES. */
    public static StepEntity unwrapBasisSurfaceOnce(StepEntity surface) {
        return unwrapBasisSurface(surface);
    }

    public static StepEntity unwrapParametricPreviewSurface(StepEntity geometry) {
        StepEntity current = geometry;
        for (int depth = 0; depth < 16 && current != null; depth++) {
            StepEntity basis = unwrapBasisSurface(current);
            if (basis == null) {
                return current;
            }
            current = basis;
        }
        return current;
    }

    // The seven facades that used to sit here -- collectShellLikeIds,
    // collectStandaloneEdges, buildMappedRepresentationGeometry,
    // buildRelatedRepresentationGeometry, expandMappedItemGeometry,
    // collectRepresentationShells and collectRepresentationSolids -- are gone
    // along with PreviewGeometryCollector itself. Each one forwarded into that
    // class, whose every member except the shell-like id walk was a dead twin of
    // a live export-side rule, and none of the seven had a caller outside this
    // file's own facade chain. The shell-like id walk now lives in
    // StepLegacyGeometryBuilder, next to the pipeline that drives it.

    // ─── Edge/loop building ──────────────────────────────────────────────

    public static List<CartesianPoint> sampleLoop(FaceBound bound) {
        if (bound.loop() instanceof VertexLoop) {
            VertexLoop vertexLoop = (VertexLoop) bound.loop();
            return List.of(vertexLoop.vertex().point());
        }
        if (bound.loop() instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) bound.loop();
            List<CartesianPoint> sampled = new ArrayList<>(polyLoop.points());
            if (!sampled.isEmpty() && sampled.get(0).distanceTo(sampled.get(sampled.size() - 1)) > 1.0e-9) {
                sampled.add(sampled.get(0));
            }
            return bound.orientation() ? sampled : reverseClosedLoop(sampled);
        }
        if (!(bound.loop() instanceof EdgeLoop)) {
            throw new UnsupportedGeometryException("preview export requires EDGE_LOOP, POLY_LOOP or VERTEX_LOOP");
        }
        EdgeLoop edgeLoop = (EdgeLoop) bound.loop();
        List<CartesianPoint> sampled = new ArrayList<>();
        boolean firstEdge = true;
        for (OrientedEdge orientedEdge : edgeLoop.edges()) {
            List<CartesianPoint> edgePoints = StepEdgePayloadBuilder.sampleOrientedEdge(orientedEdge);
            int startIndex = firstEdge ? 0 : 1;
            for (int i = startIndex; i < edgePoints.size(); i++) {
                sampled.add(edgePoints.get(i));
            }
            firstEdge = false;
        }
        if (!sampled.isEmpty() && sampled.get(0).distanceTo(sampled.get(sampled.size() - 1)) > 1.0e-9) {
            sampled.add(sampled.get(0));
        }
        return bound.orientation() ? sampled : reverseClosedLoop(sampled);
    }

    public static <T> List<T> reverseClosedLoop(List<T> points) {
        return StepPayloadBuilder.reverseClosedLoop(points);
    }

    private record SurfaceTypeNameEntry(Class<?> type, String name) {
    }

    /**
     * Geometry surface type names, replacing the former 16-branch if/else-if
     * chain. Order mirrors the original chain (first match wins), which also
     * preserves the old resolution if subtype overlaps ever appear.
     */
    private static final List<SurfaceTypeNameEntry> SURFACE_TYPE_NAMES = List.of(
            new SurfaceTypeNameEntry(Plane.class, "PLANE"),
            new SurfaceTypeNameEntry(CylindricalSurface.class, "CYLINDRICAL_SURFACE"),
            new SurfaceTypeNameEntry(ConicalSurface.class, "CONICAL_SURFACE"),
            new SurfaceTypeNameEntry(SphericalSurface.class, "SPHERICAL_SURFACE"),
            new SurfaceTypeNameEntry(ToroidalSurface.class, "TOROIDAL_SURFACE"),
            new SurfaceTypeNameEntry(BSplineSurface3.class, "BSPLINE_SURFACE"),
            new SurfaceTypeNameEntry(RationalBSplineSurface3.class, "RATIONAL_BSPLINE_SURFACE"),
            new SurfaceTypeNameEntry(RuledSurface3.class, "RULED_SURFACE"),
            new SurfaceTypeNameEntry(SurfaceOfRevolution3.class, "SURFACE_OF_REVOLUTION"),
            new SurfaceTypeNameEntry(OffsetSurface3.class, "OFFSET_SURFACE"),
            new SurfaceTypeNameEntry(SurfaceOfLinearExtrusion3.class, "SURFACE_OF_LINEAR_EXTRUSION"),
            new SurfaceTypeNameEntry(SurfaceOfConstantRadius3.class, "SURFACE_OF_CONSTANT_RADIUS"),
            new SurfaceTypeNameEntry(ParaboloidSurface.class, "PARABOLOID_SURFACE"),
            new SurfaceTypeNameEntry(HyperboloidSurface.class, "HYPERBOLOID_SURFACE"),
            new SurfaceTypeNameEntry(SurfaceOfTranslation3.class, "SURFACE_OF_TRANSLATION"),
            new SurfaceTypeNameEntry(SurfaceOfProjection3.class, "SURFACE_OF_PROJECTION")
    );

    public static String surfaceTypeNameForGeometry(SurfaceGeometry surface) {
        for (SurfaceTypeNameEntry entry : SURFACE_TYPE_NAMES) {
            if (entry.type().isInstance(surface)) {
                return entry.name();
            }
        }
        throw new IllegalArgumentException("Unknown value type: " + surface);
    }
}
