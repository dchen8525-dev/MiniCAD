package com.minicad.export.json;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.export.glb.PreviewMeshExporter;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.RationalBSplineSurface3;
import com.minicad.geometry.RuledSurface3;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.SurfaceOfConstantRadius3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.helper.MathUtilityHelper;
import com.minicad.helper.SurfaceGeometryHelper;
import com.minicad.helper.StepMetadataExtractor;
import com.minicad.preview.builder.PreviewFaceBuilder;
import com.minicad.preview.mapper.ParametricSurfaceMapper;
import com.minicad.preview.mapper.SurfaceMapperHelper;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.FaceSurfacePayload;
import com.minicad.preview.payload.LoopPayload;
import com.minicad.preview.payload.ParametricLoopPayload;
import com.minicad.preview.payload.PayloadConversionHelper;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.PreviewFaceResult;
import com.minicad.preview.payload.SurfacePatch;
import com.minicad.preview.payload.UvBounds;
import com.minicad.preview.payload.UvPoint;
import com.minicad.preview.payload.UnsupportedFacePayload;
import com.minicad.preview.payload.VectorPayload;
import com.minicad.preview.sampling.PcurveSamplingHelper;
import com.minicad.preview.sampling.TriangulationHelper;
import com.minicad.preview.statistics.PreviewStatisticsHelper;
import com.minicad.step.model.StepAnnotationCurveOccurrence;
import com.minicad.step.model.StepBSplineSurface;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepBezierSurface;
import com.minicad.step.model.StepBlendedSurface;
import com.minicad.step.model.StepBSplineSurfaceWithKnotsAndBreakpoints;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepCurveBoundedSurface;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepDegenerateToroidalSurface;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFreeFormSurface;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepHyperboloidSurface;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepMachinedSurface;
import com.minicad.step.model.StepMappedItem;
import com.minicad.step.model.StepOrientedCurve;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepOrientedSurface;
import com.minicad.step.model.StepOffsetSurface;
import com.minicad.step.model.StepOffsetSurface2;
import com.minicad.step.model.StepParaboloidSurface;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepPiecewiseBezierSurface;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepProjectionCurve;
import com.minicad.step.model.StepQuasiUniformSurface;
import com.minicad.step.model.StepRationalBSplineSurface;
import com.minicad.step.model.StepRectangularCompositeSurface;
import com.minicad.step.model.StepRectangularTrimmedSurface;
import com.minicad.step.model.StepRuledSurface;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSphericalSurface;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepSurfaceOfConstantRadius;
import com.minicad.step.model.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.StepSurfaceOfProjection;
import com.minicad.step.model.StepSurfaceOfRevolution;
import com.minicad.step.model.StepSurfaceOfTranslation;
import com.minicad.step.model.StepSurfacePatch;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepToroidalSurfaceWithCylindricalAxis;
import com.minicad.step.model.StepToroidalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepToroidalSurfaceWithSpecifiedBends;
import com.minicad.step.model.StepUniformSurface;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.step.model.StepCylindricalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepConicalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepSphericalSurfaceWithEllipticalAxis;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.FaceBound;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.VertexLoop;
import com.minicad.topology.PolyLoop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.SphericalSurface;
import com.minicad.geometry.SurfaceOfLinearExtrusion3;
import com.minicad.geometry.SurfaceOfRevolution3;

/**
 * Builds face payloads from STEP face entities.
 *
 * <p>Handles conversion of various surface types to preview payloads:</p>
 * <ul>
 *   <li>Planar surfaces (planes)</li>
 *   <li>Revolution surfaces (cylinders, cones, spheres, tori)</li>
 *   <li>Parametric surfaces (B-spline, NURBS)</li>
 *   <li>Derived surfaces (ruled, offset, swept)</li>
 * </ul>
 *
 * <p>Each surface type has dedicated payload construction methods
 * that handle geometry extraction, tessellation, and parameter mapping.</p>
 *
 * @since 1.0
 */
public final class StepFacePayloadBuilder {

    private static final Logger log = LoggerFactory.getLogger(StepFacePayloadBuilder.class);

    private StepFacePayloadBuilder() {
    }

    /**
     * Builds preview face result from STEP face entity.
     *
     * <p>This is the main entry point for face payload construction.
     * It dispatches to type-specific methods based on the face geometry.</p>
     *
     * @param stepFace The STEP face entity to convert
     * @param builder CAD builder for geometry resolution
     * @param metadata Display metadata for the face
     * @return Preview face result containing either a valid payload or unsupported face info
     */
    public static PreviewFaceResult buildPreviewFaceResult(
            StepFaceEntity stepFace,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
            PreviewFaceResult base = buildPreviewFaceResult(orientedFace.faceElement(), builder, metadata);
            if (base.face() == null) {
                return new PreviewFaceResult(
                        null,
                        StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, base.unsupportedFace() == null ? null : base.unsupportedFace().reason())
                );
            }
            if (orientedFace.orientation()) {
                return new PreviewFaceResult(base.face(), null);
            }
            FacePayload reversed = reverseFacePayload(base.face());
            logPreviewFacePayload("face_payload_built", reversed);
            return new PreviewFaceResult(reversed, null);
        }

        StepEntity geometry = faceGeometry(stepFace);
        StepEntity previewGeometry = unwrapParametricPreviewSurface(geometry);
        return dispatchPreviewFace(stepFace, geometry, previewGeometry, builder, metadata);
    }

    // buildPreviewFaceResult dispatch table (previewGeometry rules).
    // First-match-with-fallthrough: a rule whose handler returns non-null is adopted; a
    // null return continues to the next rule (replicating the original sequential ifs,
    // where a wrapped surface falls through the dedicated block to the generic fallback).
    private record PreviewFaceRule(Class<?> type, Predicate<StepEntity> matches, PreviewFaceHandler handler) {
        boolean matches(StepEntity entity) {
            return matches.test(entity);
        }
    }

    private interface PreviewFaceHandler {
        PreviewFaceResult handle(StepFaceEntity stepFace, StepEntity geometry, StepEntity previewGeometry,
                StepCadBuilder builder, StepMetadataExtractor.DisplayMetadata metadata);
    }

    private static PreviewFaceRule previewFaceRule(Class<?> type, Predicate<StepEntity> matches, PreviewFaceHandler handler) {
        return new PreviewFaceRule(type, matches, handler);
    }

    private static final List<PreviewFaceRule> PREVIEW_FACE_RULES = List.of(
        previewFaceRule(StepPlane.class, StepPlane.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            try {
                PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
                if (trimmed.face() != null) {
                    logPreviewFacePayload("face_payload_built", trimmed.face());
                    return trimmed;
                }
                if (geometry instanceof StepPlane) {
                    FacePayload payload = PreviewMeshExporter.facePayloadFromTopologyFace(stepFace.id(), builder.buildFace(stepFace.id()), StepMetadataHelper.faceDisplayName(stepFace), metadata);
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
                return trimmed;
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                String reason = ex.getMessage();
                if (reason != null && !reason.isBlank() && reason.contains("POLY_LOOP")) {
                    log.warn("Planar face build failed; returning unsupported face payload: {}", reason);
                    return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, reason));
                }
                log.warn("Planar face build failed; returning unsupported face payload", ex);
                return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "planar face build failed"));
            }
        }),
        previewFaceRule(StepCylindricalSurface.class, StepCylindricalSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) previewGeometry;
            try {
                if (geometry instanceof StepCylindricalSurface) {
                    FacePayload payload = toCylindricalFacePayload(stepFace, cylindricalSurface, builder, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Cylindrical face build failed; returning unsupported face payload", ex);
                return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "cylindrical face build failed: " + ex.getMessage()));
            }
        return null;
        }),
        previewFaceRule(StepConicalSurface.class, StepConicalSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepConicalSurface conicalSurface = (StepConicalSurface) previewGeometry;
            try {
                if (geometry instanceof StepConicalSurface) {
                    FacePayload payload = toConicalFacePayload(stepFace, conicalSurface, builder, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Conical face build failed; returning unsupported face payload", ex);
                return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "conical face build failed: " + ex.getMessage()));
            }
        return null;
        }),
        previewFaceRule(StepSphericalSurface.class, StepSphericalSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }),
        previewFaceRule(StepRationalBSplineSurface.class, StepRationalBSplineSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepRationalBSplineSurface splineSurface = (StepRationalBSplineSurface) previewGeometry;
            try {
                PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
                if (trimmed.face() != null || trimmed.unsupportedFace() != null) {
                    if (trimmed.face() != null) {
                        logPreviewFacePayload("face_payload_built", trimmed.face());
                    }
                    return trimmed;
                }
                FacePayload payload = toRationalBSplineSurfaceFacePayload(stepFace, splineSurface, builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
                return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "rational b-spline surface patch preview failed"));
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("stage={} faceId={}, surfaceId={}, reason={}", "rational_bspline_surface_preview_exception",
                        stepFace.id(), splineSurface.id(), ex.getMessage(), ex);
                return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "rational b-spline surface preview failed"));
            }
        }),
        previewFaceRule(StepBSplineSurfaceWithKnots.class, e -> StepBSplineSurfaceWithKnots.class.isInstance(e) || StepBSplineSurface.class.isInstance(e) || StepBezierSurface.class.isInstance(e) || StepUniformSurface.class.isInstance(e) || StepQuasiUniformSurface.class.isInstance(e) || StepPiecewiseBezierSurface.class.isInstance(e), (stepFace, geometry, previewGeometry, builder, metadata) -> {
            try {
                PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
                if (trimmed.face() != null || trimmed.unsupportedFace() != null) {
                    if (trimmed.face() != null) {
                        logPreviewFacePayload("face_payload_built", trimmed.face());
                    }
                    return trimmed;
                }
                FacePayload payload = toBSplineSurfaceFacePayload(stepFace, previewGeometry, builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
                return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "b-spline surface patch preview failed"));
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("stage={} faceId={}, surfaceId={}, reason={}", "bspline_surface_preview_exception",
                        stepFace.id(), previewGeometry.id(), ex.getMessage(), ex);
                return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "b-spline surface preview failed"));
            }
        }),
        previewFaceRule(StepSurfaceOfLinearExtrusion.class, e -> StepSurfaceOfLinearExtrusion.class.isInstance(e) || StepSurfaceOfRevolution.class.isInstance(e), (stepFace, geometry, previewGeometry, builder, metadata) -> {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }),
        previewFaceRule(StepDegenerateToroidalSurface.class, StepDegenerateToroidalSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }),
        previewFaceRule(StepToroidalSurfaceWithSpecifiedBends.class, StepToroidalSurfaceWithSpecifiedBends.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepToroidalSurfaceWithSpecifiedBends toroidalSurfaceWithBends = (StepToroidalSurfaceWithSpecifiedBends) previewGeometry;
            try {
                if (geometry instanceof StepToroidalSurfaceWithSpecifiedBends) {
                    FacePayload payload = toToroidalWithSpecifiedBendsFacePayload(stepFace, toroidalSurfaceWithBends, builder, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Toroidal surface with specified bends face build failed; returning unsupported face payload", ex);
                return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "toroidal surface with specified bends face build failed: " + ex.getMessage()));
            }
        return null;
        }),
        previewFaceRule(StepToroidalSurface.class, StepToroidalSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) previewGeometry;
            try {
                if (geometry instanceof StepToroidalSurface) {
                    FacePayload payload = toToroidalFacePayload(stepFace, toroidalSurface, builder, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Toroidal face build failed; returning unsupported face payload", ex);
                return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "toroidal face build failed: " + ex.getMessage()));
            }
        return null;
        }),
        previewFaceRule(StepCylindricalSurface.class, e -> StepCylindricalSurface.class.isInstance(e) || StepConicalSurface.class.isInstance(e) || StepDegenerateToroidalSurface.class.isInstance(e) || StepToroidalSurface.class.isInstance(e) || StepToroidalSurfaceWithSpecifiedBends.class.isInstance(e), (stepFace, geometry, previewGeometry, builder, metadata) -> {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }),
        previewFaceRule(StepCylindricalSurfaceWithEllipticalAxis.class, e -> StepCylindricalSurfaceWithEllipticalAxis.class.isInstance(e) || StepConicalSurfaceWithEllipticalAxis.class.isInstance(e) || StepSphericalSurfaceWithEllipticalAxis.class.isInstance(e) || StepToroidalSurfaceWithCylindricalAxis.class.isInstance(e) || StepToroidalSurfaceWithEllipticalAxis.class.isInstance(e), (stepFace, geometry, previewGeometry, builder, metadata) -> {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }),
        previewFaceRule(StepBSplineSurfaceWithKnotsAndBreakpoints.class, StepBSplineSurfaceWithKnotsAndBreakpoints.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }),
        previewFaceRule(StepFreeFormSurface.class, StepFreeFormSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }),
        previewFaceRule(StepRuledSurface.class, StepRuledSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepRuledSurface ruledSurface = (StepRuledSurface) previewGeometry;
            try {
                FacePayload payload = toRuledSurfaceFacePayload(stepFace, ruledSurface, builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Ruled surface preview failed; returning unsupported face payload", ex);
            }
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "ruled surface preview failed"));
        }),
        previewFaceRule(StepSurfaceOfConstantRadius.class, StepSurfaceOfConstantRadius.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepSurfaceOfConstantRadius surfaceOfConstantRadius = (StepSurfaceOfConstantRadius) previewGeometry;
            try {
                FacePayload payload = toSurfaceOfConstantRadiusFacePayload(stepFace, surfaceOfConstantRadius, builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Surface of constant radius preview failed; returning unsupported face payload", ex);
            }
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "surface of constant radius preview failed"));
        }),
        previewFaceRule(StepParaboloidSurface.class, StepParaboloidSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepParaboloidSurface paraboloidSurface = (StepParaboloidSurface) previewGeometry;
            try {
                FacePayload payload = toParametricSurfaceFacePayload(stepFace, paraboloidSurface, "PARABOLOID_SURFACE", builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Paraboloid surface preview failed; returning unsupported face payload", ex);
            }
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "paraboloid surface preview failed"));
        }),
        previewFaceRule(StepHyperboloidSurface.class, StepHyperboloidSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepHyperboloidSurface hyperboloidSurface = (StepHyperboloidSurface) previewGeometry;
            try {
                FacePayload payload = toParametricSurfaceFacePayload(stepFace, hyperboloidSurface, "HYPERBOLOID_SURFACE", builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Hyperboloid surface preview failed; returning unsupported face payload", ex);
            }
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "hyperboloid surface preview failed"));
        }),
        previewFaceRule(StepSurfaceOfTranslation.class, StepSurfaceOfTranslation.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepSurfaceOfTranslation translationSurface = (StepSurfaceOfTranslation) previewGeometry;
            try {
                FacePayload payload = toParametricSurfaceFacePayload(stepFace, translationSurface, "SURFACE_OF_TRANSLATION", builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Surface of translation preview failed; returning unsupported face payload", ex);
            }
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "surface of translation preview failed"));
        }),
        previewFaceRule(StepSurfaceOfProjection.class, StepSurfaceOfProjection.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepSurfaceOfProjection projectionSurface = (StepSurfaceOfProjection) previewGeometry;
            try {
                FacePayload payload = toParametricSurfaceFacePayload(stepFace, projectionSurface, "SURFACE_OF_PROJECTION", builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Surface of projection preview failed; returning unsupported face payload", ex);
            }
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "surface of projection preview failed"));
        }),
        previewFaceRule(StepBlendedSurface.class, StepBlendedSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepBlendedSurface blended = (StepBlendedSurface) previewGeometry;
            // Blended surface: approximate by rendering the primary surface with blend radius as metadata
            try {
                PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, blended.primarySurface(), metadata, builder);
                if (trimmed.face() != null) {
                    logPreviewFacePayload("face_payload_built", trimmed.face());
                    return trimmed;
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Blended surface preview failed; returning unsupported face payload", ex);
                // C03: No silent geometry loss - continue to fallback
            }
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "blended surface preview failed"));
        }),
        previewFaceRule(StepFreeFormSurface.class, StepFreeFormSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepFreeFormSurface freeForm = (StepFreeFormSurface) previewGeometry;
            try {
                PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
                if (trimmed.face() != null) {
                    logPreviewFacePayload("face_payload_built", trimmed.face());
                    return trimmed;
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Parametric surface preview failed; falling back to sampled-grid tessellation", ex);
                // C03: No silent geometry loss - continue to fallback tessellation
            }
            // Fallback: tessellate via sampled grid if parametric mapping fails
            try {
                List<FaceBound> bounds = StepFacePayloadBuilder.buildFaceBounds(stepFace, builder);
                if (!bounds.isEmpty()) {
                    BSplineSurface3 surface = PreviewMeshExporter.buildFreeFormSurface(freeForm, builder);
                    FacePayload payload = toSampledSurfaceFacePayload(stepFace, surface, "FREE_FORM_SURFACE", bounds, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.warn("Free-form surface preview failed; returning unsupported face payload", ex);
                // C03: No silent geometry loss - continue to unsupported face payload
            } catch (Exception ex) {
                log.warn("Free-form surface preview failed unexpectedly", ex);
                // C03: Catch unexpected errors - log and return unsupported face payload
                return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "free-form surface preview failed: unexpected error - " + ex.getMessage()));
            }
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "free-form surface preview failed"));
        }),
        previewFaceRule(StepMachinedSurface.class, StepMachinedSurface.class::isInstance, (stepFace, geometry, previewGeometry, builder, metadata) -> {
            StepMachinedSurface machinedSurface = (StepMachinedSurface) previewGeometry;
            return buildPreviewFaceResult((StepFaceEntity) machinedSurface.face(), builder, metadata);
        })
    );

    private static PreviewFaceResult dispatchPreviewFace(
            StepFaceEntity stepFace, StepEntity geometry, StepEntity previewGeometry,
            StepCadBuilder builder, StepMetadataExtractor.DisplayMetadata metadata) {
        for (PreviewFaceRule rule : PREVIEW_FACE_RULES) {
            if (rule.matches(previewGeometry)) {
                PreviewFaceResult result = rule.handler().handle(stepFace, geometry, previewGeometry, builder, metadata);
                if (result != null) {
                    return result;
                }
            }
        }
        String unsupportedSurface = describeUnsupportedPreviewSurface(geometry, builder);
        String reason = unsupportedSurface == null
                ? "surface type not previewable"
                : unsupportedSurface + " preview is unsupported";
        return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, reason));
    }


    /** Delegates to the shared SURFACE_UNWRAP_RULES table; see PreviewFaceBuilder. */
    private static StepEntity unwrapParametricPreviewSurface(StepEntity geometry) {
        return PreviewFaceBuilder.unwrapParametricPreviewSurface(geometry);
    }

    private static String describeUnsupportedPreviewSurface(StepEntity surface) {
        return describeUnsupportedPreviewSurface(surface, null);
    }

    private static String describeUnsupportedPreviewSurface(StepEntity surface, StepCadBuilder builder) {
        if (surface == null) {
            return null;
        }
        if (surface instanceof StepGeometricReplica
                && "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) surface;
            if (replica.transformation() instanceof com.minicad.step.model.StepCartesianTransformationOperator) {
                com.minicad.step.model.StepCartesianTransformationOperator transformation = (com.minicad.step.model.StepCartesianTransformationOperator) replica.transformation();
                double scale = transformation.scale() == null ? 1.0 : transformation.scale();
                if (Math.abs(scale) <= 1.0e-9) {
                    return "SURFACE_REPLICA zero scale preview is unsupported";
                }
                if (builder != null) {
                    double[] matrix = StepPlacementTransformer.matrixForTransformationOperator(transformation, builder);
                    if (MathUtilityHelper.inverseUniformScaleTransform(matrix) == null) {
                        return "SURFACE_REPLICA non-uniform scale preview is unsupported";
                    }
                }
            }
            return describeUnsupportedPreviewSurface(replica.parent(), builder);
        }
        // Unlike PreviewFaceBuilder.describeUnsupportedPreviewSurface, this copy
        // does not recurse through MAPPED_ITEM - it reports the mapped item itself.
        if (surface instanceof StepMappedItem) {
            return StepTypeNameResolver.surfaceTypeName(surface);
        }
        StepEntity basis = PreviewFaceBuilder.unwrapBasisSurfaceOnce(surface);
        if (basis != null) {
            return describeUnsupportedPreviewSurface(basis, builder);
        }
        return StepTypeNameResolver.surfaceTypeName(surface);
    }

    private static void logPreviewFacePayload(String stage, FacePayload face) {
        int loopCount = face.loops() == null ? 0 : face.loops().size();
        int innerLoopCount = face.loops() == null ? 0 : (int) face.loops().stream().filter(loop -> !loop.outer()).count();
        int triangleCount = face.triangles() == null ? 0 : face.triangles().size() / 3;
        int uvLoopCount = face.uvLoops() == null ? 0 : face.uvLoops().size();
        String parametricType = face.surface() == null ? "none" : face.surface().type();
        log.info("stage={} faceId={}, surfaceType={}, parametricType={}, loopCount={}, innerLoopCount={}, triangleCount={}, uvLoopCount={}, sameSense={}",
                stage,
                face.stepId(),
                face.surfaceType(),
                parametricType,
                loopCount,
                innerLoopCount,
                triangleCount,
                uvLoopCount,
                face.sameSense());
    }

    private static FacePayload toCylindricalFacePayload(
            StepFaceEntity stepFace,
            StepCylindricalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = StepFacePayloadBuilder.buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }

        if (!(bounds.get(0).loop() instanceof EdgeLoop)) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();
        if (outerLoop.edges().size() != 4) {
            return null;
        }

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .collect(Collectors.toList());
        List<OrientedEdge> lineEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Line3)
                .collect(Collectors.toList());
        if (circleEdges.size() != 2 || lineEdges.size() != 2) {
            return null;
        }

        CylindricalSurface surface = builder.buildCylindricalSurface(stepSurface.id());
        OrientedEdge lowerArc = circleEdges.get(0);
        OrientedEdge upperArc = circleEdges.get(circleEdges.size() - 1);
        if (SurfaceGeometryHelper.averageAxialHeight(surface, StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc)) > SurfaceGeometryHelper.averageAxialHeight(surface, StepEdgePayloadBuilder.sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.get(circleEdges.size() - 1);
            upperArc = circleEdges.get(0);
        }

        List<CartesianPoint> lowerArcPoints = StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = StepEdgePayloadBuilder.sampleOrientedEdge(upperArc);
        double lowerHeight = SurfaceGeometryHelper.averageAxialHeight(surface, lowerArcPoints);
        double upperHeight = SurfaceGeometryHelper.averageAxialHeight(surface, upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = SurfaceGeometryHelper.unwrapAngles(surface, lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = StepValidationHelper.faceSameSense(stepFace);
        List<PointPayload> triangles = TriangulationHelper.triangulateCylindricalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = SurfaceGeometryHelper.cylindricalNormal(surface, angles.get(0), sameSense);
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "CYLINDRICAL_SURFACE",
                PayloadConversionHelper.toPointPayload(SurfaceGeometryHelper.surfacePoint(surface, angles.get(0), lowerHeight)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                new FaceSurfacePayload(
                        "cylindrical_strip",
                        List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                        List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                        List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                        surface.radius(),
                        null,
                        null,
                        lowerHeight,
                        upperHeight,
                        angles.get(0),
                        angles.get(angles.size() - 1) - angles.get(0),
                        null,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload toConicalFacePayload(
            StepFaceEntity stepFace,
            StepConicalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = StepFacePayloadBuilder.buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .collect(Collectors.toList());
        List<OrientedEdge> lineEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Line3)
                .collect(Collectors.toList());
        if (circleEdges.size() != 2 || lineEdges.size() != 2) {
            return null;
        }

        ConicalSurface surface = builder.buildConicalSurface(stepSurface.id());
        OrientedEdge lowerArc = circleEdges.get(0);
        OrientedEdge upperArc = circleEdges.get(circleEdges.size() - 1);
        if (SurfaceGeometryHelper.averageAxialHeight(surface.position(), StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc)) > SurfaceGeometryHelper.averageAxialHeight(surface.position(), StepEdgePayloadBuilder.sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.get(circleEdges.size() - 1);
            upperArc = circleEdges.get(0);
        }

        List<CartesianPoint> lowerArcPoints = StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = StepEdgePayloadBuilder.sampleOrientedEdge(upperArc);
        double lowerHeight = SurfaceGeometryHelper.averageAxialHeight(surface.position(), lowerArcPoints);
        double upperHeight = SurfaceGeometryHelper.averageAxialHeight(surface.position(), upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = SurfaceGeometryHelper.unwrapAngles(surface.position(), lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = StepValidationHelper.faceSameSense(stepFace);
        List<PointPayload> triangles = TriangulationHelper.triangulateConicalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = SurfaceGeometryHelper.conicalNormal(surface, angles.get(0), sameSense);
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "CONICAL_SURFACE",
                PayloadConversionHelper.toPointPayload(SurfaceGeometryHelper.conicalSurfacePoint(surface, angles.get(0), lowerHeight)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                new FaceSurfacePayload(
                        "conical_strip",
                        List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                        List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                        List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                        surface.radius(),
                        null,
                        surface.semiAngle(),
                        lowerHeight,
                        upperHeight,
                        angles.get(0),
                        angles.get(angles.size() - 1) - angles.get(0),
                        null,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload toToroidalFacePayload(
            StepFaceEntity stepFace,
            StepToroidalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = StepFacePayloadBuilder.buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .collect(Collectors.toList());
        if (circleEdges.size() != 4) {
            return null;
        }

        ToroidalSurface surface = builder.buildToroidalSurface(stepSurface.id());
        List<OrientedEdge> varyingUEdges = new ArrayList<>();
        List<OrientedEdge> varyingVEdges = new ArrayList<>();
        for (OrientedEdge edge : circleEdges) {
            List<CartesianPoint> points = StepEdgePayloadBuilder.sampleOrientedEdge(edge);
            List<Double> uValues = SurfaceGeometryHelper.unwrapToroidalU(surface, points);
            List<Double> vValues = SurfaceGeometryHelper.unwrapToroidalV(surface, points);
            double uRange = Math.abs(uValues.get(uValues.size() - 1) - uValues.get(0));
            double vRange = Math.abs(vValues.get(vValues.size() - 1) - vValues.get(0));
            if (uRange >= vRange) {
                varyingUEdges.add(edge);
            } else {
                varyingVEdges.add(edge);
            }
        }
        if (varyingUEdges.size() != 2 || varyingVEdges.size() != 2) {
            return null;
        }

        OrientedEdge lowerVEdge = varyingUEdges.get(0);
        OrientedEdge upperVEdge = varyingUEdges.get(varyingUEdges.size() - 1);
        if (SurfaceGeometryHelper.averageToroidalV(surface, StepEdgePayloadBuilder.sampleOrientedEdge(lowerVEdge)) > SurfaceGeometryHelper.averageToroidalV(surface, StepEdgePayloadBuilder.sampleOrientedEdge(upperVEdge))) {
            lowerVEdge = varyingUEdges.get(varyingUEdges.size() - 1);
            upperVEdge = varyingUEdges.get(0);
        }

        List<CartesianPoint> lowerPoints = StepEdgePayloadBuilder.sampleOrientedEdge(lowerVEdge);
        List<Double> uValues = SurfaceGeometryHelper.unwrapToroidalU(surface, lowerPoints);
        double lowerV = SurfaceGeometryHelper.averageToroidalV(surface, lowerPoints);
        double upperV = SurfaceGeometryHelper.averageToroidalV(surface, StepEdgePayloadBuilder.sampleOrientedEdge(upperVEdge));
        if (Math.abs(upperV - lowerV) <= Epsilon.EPS || uValues.size() < 2) {
            return null;
        }

        boolean sameSense = StepValidationHelper.faceSameSense(stepFace);
        List<PointPayload> triangles = TriangulationHelper.triangulateToroidalStrip(surface, lowerV, upperV, uValues, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = SurfaceGeometryHelper.toroidalNormal(surface, uValues.get(0), lowerV, sameSense);
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "TOROIDAL_SURFACE",
                PayloadConversionHelper.toPointPayload(SurfaceGeometryHelper.toroidalSurfacePoint(surface, uValues.get(0), lowerV)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                new FaceSurfacePayload(
                        "toroidal_strip",
                        List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                        List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                        List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                        surface.majorRadius(),
                        surface.minorRadius(),
                        null,
                        lowerV,
                        upperV,
                        uValues.get(0),
                        uValues.get(uValues.size() - 1) - uValues.get(0),
                        null,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload toToroidalWithSpecifiedBendsFacePayload(
            StepFaceEntity stepFace,
            StepToroidalSurfaceWithSpecifiedBends stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = StepFacePayloadBuilder.buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .collect(Collectors.toList());
        if (circleEdges.size() != 4) {
            return null;
        }

        ToroidalSurface surface = builder.buildToroidalSurface(stepSurface.id());
        List<OrientedEdge> varyingUEdges = new ArrayList<>();
        List<OrientedEdge> varyingVEdges = new ArrayList<>();
        for (OrientedEdge edge : circleEdges) {
            List<CartesianPoint> points = StepEdgePayloadBuilder.sampleOrientedEdge(edge);
            List<Double> uValues = SurfaceGeometryHelper.unwrapToroidalU(surface, points);
            List<Double> vValues = SurfaceGeometryHelper.unwrapToroidalV(surface, points);
            double uRange = Math.abs(uValues.get(uValues.size() - 1) - uValues.get(0));
            double vRange = Math.abs(vValues.get(vValues.size() - 1) - vValues.get(0));
            if (uRange >= vRange) {
                varyingUEdges.add(edge);
            } else {
                varyingVEdges.add(edge);
            }
        }
        if (varyingUEdges.size() != 2 || varyingVEdges.size() != 2) {
            return null;
        }

        OrientedEdge lowerVEdge = varyingUEdges.get(0);
        OrientedEdge upperVEdge = varyingUEdges.get(varyingUEdges.size() - 1);
        if (SurfaceGeometryHelper.averageToroidalV(surface, StepEdgePayloadBuilder.sampleOrientedEdge(lowerVEdge)) > SurfaceGeometryHelper.averageToroidalV(surface, StepEdgePayloadBuilder.sampleOrientedEdge(upperVEdge))) {
            lowerVEdge = varyingUEdges.get(varyingUEdges.size() - 1);
            upperVEdge = varyingUEdges.get(0);
        }

        List<CartesianPoint> lowerPoints = StepEdgePayloadBuilder.sampleOrientedEdge(lowerVEdge);
        List<Double> uValues = SurfaceGeometryHelper.unwrapToroidalU(surface, lowerPoints);
        double lowerV = SurfaceGeometryHelper.averageToroidalV(surface, lowerPoints);
        double upperV = SurfaceGeometryHelper.averageToroidalV(surface, StepEdgePayloadBuilder.sampleOrientedEdge(upperVEdge));
        if (Math.abs(upperV - lowerV) <= Epsilon.EPS || uValues.size() < 2) {
            return null;
        }

        boolean sameSense = StepValidationHelper.faceSameSense(stepFace);
        List<PointPayload> triangles = TriangulationHelper.triangulateToroidalStrip(surface, lowerV, upperV, uValues, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = SurfaceGeometryHelper.toroidalNormal(surface, uValues.get(0), lowerV, sameSense);
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS",
                PayloadConversionHelper.toPointPayload(SurfaceGeometryHelper.toroidalSurfacePoint(surface, uValues.get(0), lowerV)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                new FaceSurfacePayload(
                        "toroidal_strip",
                        List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                        List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                        List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                        surface.majorRadius(),
                        surface.minorRadius(),
                        null,
                        lowerV,
                        upperV,
                        uValues.get(0),
                        uValues.get(uValues.size() - 1) - uValues.get(0),
                        null,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload toBSplineSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepEntity stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = StepFacePayloadBuilder.buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();

        SurfacePatch patch = StepEdgePayloadBuilder.buildFourSidedPatch(outerLoop);
        if (patch == null) {
            return null;
        }
        BSplineSurface3 surface = PreviewMeshExporter.buildBsplineSurface(stepSurface, builder);
        int uSegments = Math.max(patch.uSegments(), 10);
        int vSegments = Math.max(patch.vSegments(), 10);
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(
                sampleSurfaceGrid(surface, uSegments, vSegments),
                StepValidationHelper.faceSameSense(stepFace)
        );
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = surface.normalAt((surface.uStart() + surface.uEnd()) * 0.5, (surface.vStart() + surface.vEnd()) * 0.5);
        if (!StepValidationHelper.faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                StepTypeNameResolver.surfaceTypeName(stepSurface),
                PayloadConversionHelper.toPointPayload(surface.pointAt(surface.uStart(), surface.vStart())),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                StepValidationHelper.faceSameSense(stepFace),
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                null,
                null
        );
    }

    private static FacePayload toRationalBSplineSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepRationalBSplineSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = StepFacePayloadBuilder.buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        RationalBSplineSurface3 surface = builder.buildRationalBSplineSurface(stepSurface.id());
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(
                sampleSurfaceGrid(surface, 16, 16),
                StepValidationHelper.faceSameSense(stepFace)
        );
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = surface.normalAt((surface.uStart() + surface.uEnd()) * 0.5, (surface.vStart() + surface.vEnd()) * 0.5);
        if (!StepValidationHelper.faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "RATIONAL_B_SPLINE_SURFACE",
                PayloadConversionHelper.toPointPayload(surface.pointAt(surface.uStart(), surface.vStart())),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                StepValidationHelper.faceSameSense(stepFace),
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                null,
                null
        );
    }

    private static FacePayload toFourSidedPatchFacePayload(
            StepFaceEntity stepFace,
            StepEntity geometry,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) {
        List<FaceBound> bounds = StepFacePayloadBuilder.buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();
        SurfacePatch patch = StepEdgePayloadBuilder.buildFourSidedPatch(outerLoop);
        if (patch == null) {
            return null;
        }
        List<PointPayload> triangles = TriangulationHelper.triangulatePatch(patch, StepValidationHelper.faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = patch.normalAt(0.5, 0.5);
        if (!StepValidationHelper.faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                StepTypeNameResolver.surfaceTypeName(geometry),
                PayloadConversionHelper.toPointPayload(patch.pointAt(0.0, 0.0)),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                StepValidationHelper.faceSameSense(stepFace),
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                null,
                null
        );
    }

    private static FacePayload toRuledSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepRuledSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = StepFacePayloadBuilder.buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        RuledSurface3 surface = builder.buildRuledSurface(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, StepValidationHelper.faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = StepValidationHelper.faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), PayloadConversionHelper.toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "RULED_SURFACE",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                new FaceSurfacePayload(
                        "ruled_surface", null, null, null, 0.0, null, null,
                        0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload toSurfaceOfConstantRadiusFacePayload(
            StepFaceEntity stepFace,
            StepSurfaceOfConstantRadius stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = StepFacePayloadBuilder.buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        SurfaceOfConstantRadius3 surface = builder.buildSurfaceOfConstantRadius(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, StepValidationHelper.faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = StepValidationHelper.faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), PayloadConversionHelper.toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "SURFACE_OF_CONSTANT_RADIUS",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                new FaceSurfacePayload(
                        "constant_radius_surface", null, null, null, surface.radius(), null, null,
                        0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload toParametricSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepEntity stepSurface,
            String surfaceTypeName,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = StepFacePayloadBuilder.buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        SurfaceGeometry surface = builder.buildSurfaceGeometry(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, StepValidationHelper.faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = StepValidationHelper.faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), PayloadConversionHelper.toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                surfaceTypeName,
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                null,
                null
        );
    }

    private static FacePayload toSampledSurfaceFacePayload(
            StepFaceEntity stepFace,
            SurfaceGeometry surface,
            String surfaceType,
            List<FaceBound> bounds,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        int segments = 32;
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(segments, segments);
        if (grid.isEmpty()) {
            return null;
        }
        boolean sameSense = StepValidationHelper.faceSameSense(stepFace);
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                surfaceType,
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                null,
                null
        );
    }

    private static PreviewFaceResult toParametricTrimmedFaceResult(
            StepFaceEntity stepFace,
            StepEntity geometry,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) {
        List<FaceBound> normalizedBounds = List.of();
        try {
            normalizedBounds = StepFacePayloadBuilder.buildFaceBounds(stepFace, builder);
        } catch (TopologyException | StepResolutionException | UnsupportedGeometryException ex) {
            String unsupportedSurface = describeUnsupportedPreviewSurface(geometry, builder);
            if (unsupportedSurface != null && unsupportedSurface.contains("unsupported")) {
                log.warn("Face bounds derivation failed; returning unsupported face payload: {}", unsupportedSurface);
                return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, unsupportedSurface));
            }
            log.debug("stage={} faceId={}, surfaceType={}, reason={}", "parametric_bounds_fallback",
                    stepFace.id(), StepTypeNameResolver.surfaceTypeName(geometry), ex.getMessage());
        }
        ParametricSurfaceMapper mapper = SurfaceMapperHelper.mapperForSurface(geometry, builder);
        if (mapper == null) {
            String unsupportedSurface = describeUnsupportedPreviewSurface(geometry, builder);
            String reason = unsupportedSurface == null
                    ? "no parametric mapper for surface"
                    : unsupportedSurface.contains("unsupported")
                    ? unsupportedSurface
                    : unsupportedSurface + " preview is unsupported";
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, reason));
        }
        List<ParametricLoopPayload> loops = buildParametricLoops(stepFace, geometry, mapper, builder);
        if (loops.isEmpty()) {
            try {
                loops = buildParametricLoops(normalizedBounds, mapper);
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException ex) {
                log.warn("Face bounds derivation failed; returning unsupported face payload", ex);
                return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "failed to derive face bounds"));
            }
        }
        if (loops.isEmpty()) {
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "failed to build parametric loops"));
        }
        loops = normalizeLoopRoles(stepFace, geometry, loops);
        if (loops.stream().noneMatch(ParametricLoopPayload::outer)) {
            log.debug("stage={} faceId={}, surfaceType={}, semanticBoundCount={}, semanticOuterCount={}, normalizedBoundCount={}, loopCount={}", "parametric_outer_bound_missing",
                    stepFace.id(), StepTypeNameResolver.surfaceTypeName(geometry),
                            stepFace.bounds().size(),
                            stepFace.bounds().stream().filter(com.minicad.step.model.StepFaceBound::outer).count(),
                            normalizedBounds.size(),
                            loops.size());
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "missing outer bound"));
        }
        UvBounds uvBounds = boundsOf(loops);
        if (uvBounds == null || uvBounds.uSpan() <= Epsilon.EPS || uvBounds.vSpan() <= Epsilon.EPS) {
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "degenerate parametric bounds"));
        }

        int sampleCount = loops.stream().mapToInt(loop -> loop.points().size()).max().orElse(0);
        // Preview meshes should stay light enough for API transport and browser upload.
        int baseUSegments = Math.max(12, Math.min(32, sampleCount * 2));
        int baseVSegments = Math.max(8, Math.min(24, sampleCount * 2));
        if (geometry instanceof StepRationalBSplineSurface) {
            baseUSegments = Math.max(12, Math.min(24, sampleCount * 2));
            baseVSegments = Math.max(8, Math.min(18, sampleCount * 2));
        } else if (geometry instanceof StepBSplineSurfaceWithKnots) {
            baseUSegments = Math.max(12, Math.min(24, sampleCount * 2));
            baseVSegments = Math.max(8, Math.min(18, sampleCount * 2));
        } else if (geometry instanceof StepPlane) {
            int planeSegments = Math.max(16, Math.min(32, sampleCount * 2));
            if (loops.size() > 1) {
                planeSegments = Math.max(planeSegments, 40);
            }
            double dominantSpan = Math.max(uvBounds.uSpan(), uvBounds.vSpan());
            double uRatio = dominantSpan <= Epsilon.EPS ? 1.0 : uvBounds.uSpan() / dominantSpan;
            double vRatio = dominantSpan <= Epsilon.EPS ? 1.0 : uvBounds.vSpan() / dominantSpan;
            baseUSegments = Math.max(baseUSegments, Math.max(16, (int) Math.ceil(planeSegments * uRatio)));
            baseVSegments = Math.max(baseVSegments, Math.max(16, (int) Math.ceil(planeSegments * vRatio)));
        } else if (geometry instanceof StepCylindricalSurface) {
            baseUSegments = Math.max(baseUSegments, 28);
            baseVSegments = Math.max(baseVSegments, 16);
        } else if (geometry instanceof StepConicalSurface || geometry instanceof StepToroidalSurface) {
            baseUSegments = Math.max(baseUSegments, 28);
            baseVSegments = Math.max(baseVSegments, 16);
        }
        List<PointPayload> triangles = triangulateParametricFaceAdaptive(
                mapper,
                loops,
                uvBounds,
                baseUSegments,
                baseVSegments,
                StepValidationHelper.faceSameSense(stepFace)
        );
        if (triangles.isEmpty()) {
            log.debug("stage={} faceId={}, surfaceType={}, loopCount={}, outerLoopCount={}, innerLoopCount={}, uvBounds={}, sampleCount={}, baseUSegments={}, baseVSegments={}, loopPoints={}", "parametric_triangulation_empty",
                    stepFace.id(), StepTypeNameResolver.surfaceTypeName(geometry), loops.size(),
                            loops.stream().filter(ParametricLoopPayload::outer).count(),
                            loops.stream().filter(loop -> !loop.outer()).count(),
                            PreviewStatisticsHelper.formatUvBounds(uvBounds),
                            sampleCount,
                            baseUSegments,
                            baseVSegments,
                            PreviewStatisticsHelper.summarizeLoopPointCounts(loops));
            return new PreviewFaceResult(null, StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, "parametric triangulation produced no cells"));
        }

        double centerU = (uvBounds.minU() + uvBounds.maxU()) * 0.5;
        double centerV = (uvBounds.minV() + uvBounds.maxV()) * 0.5;
        Vector3 normal = mapper.normalAt(centerU, centerV);
        if (!StepValidationHelper.faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new PreviewFaceResult(
                new FacePayload(
                        stepFace.id(),
                        StepMetadataHelper.faceDisplayName(stepFace),
                        StepTypeNameResolver.surfaceTypeName(geometry),
                        PayloadConversionHelper.toPointPayload(mapper.pointAt(centerU, centerV)),
                        new VectorPayload(normal.x(), normal.y(), normal.z()),
                        StepValidationHelper.faceSameSense(stepFace),
                        PayloadConversionHelper.toColorPayload(metadata.rgb()),
                        metadata.transparency(),
                        PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                        metadata.layers(),
                        toParametricLoopPayloads(loops, mapper),
                        triangles,
                        faceSurfacePayload(geometry, uvBounds, builder),
                        loops
                ),
                null
        );
    }

    private static List<List<CartesianPoint>> sampleSurfaceGrid(BSplineSurface3 surface, int uSegments, int vSegments) {
        return surface.sampleGrid(Math.max(uSegments, 2), Math.max(vSegments, 2));
    }

    private static List<List<CartesianPoint>> sampleSurfaceGrid(RationalBSplineSurface3 surface, int uSegments, int vSegments) {
        return surface.sampleGrid(Math.max(uSegments, 2), Math.max(vSegments, 2));
    }

    private static List<ParametricLoopPayload> buildParametricLoops(List<FaceBound> bounds, ParametricSurfaceMapper mapper) {
        List<ParametricLoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            if (bound.loop() instanceof VertexLoop) {
                return List.of();
            }
            List<CartesianPoint> points3d = sampleLoop(bound);
            if (points3d.size() < 4) {
                return List.of();
            }
            List<UvPoint> uvPoints = new ArrayList<>(points3d.size());
            UvPoint previous = null;
            for (CartesianPoint point : points3d) {
                UvPoint uv = mapper.project(point, previous);
                if (uv == null) {
                    return List.of();
                }
                uvPoints.add(uv);
                previous = uv;
            }
            uvPoints = normalizePeriodicLoop(uvPoints, mapper);
            uvPoints.set(0, uvPoints.get(0));
            uvPoints.set(uvPoints.size() - 1, uvPoints.get(0));
            loops.add(new ParametricLoopPayload(bound.outer(), List.copyOf(uvPoints)));
        }
        return List.copyOf(loops);
    }

    private static List<ParametricLoopPayload> buildParametricLoops(
            StepFaceEntity stepFace,
            StepEntity geometry,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        List<ParametricLoopPayload> loops = new ArrayList<>();
        boolean promoteSingleOuter = stepFace.bounds().size() == 1
                && stepFace.bounds().stream().noneMatch(com.minicad.step.model.StepFaceBound::outer);
        for (com.minicad.step.model.StepFaceBound bound : stepFace.bounds()) {
            if (!(bound.loop() instanceof com.minicad.step.model.StepEdgeLoop)) {
                log.debug("stage={} faceId={}, surfaceType={}, boundId={}, reason={}", "parametric_loop_build_failed",
                        stepFace.id(), StepTypeNameResolver.surfaceTypeName(geometry), bound.id(), "bound loop is not EDGE_LOOP");
                return List.of();
            }
            com.minicad.step.model.StepEdgeLoop edgeLoop = (com.minicad.step.model.StepEdgeLoop) bound.loop();
            List<UvPoint> loopPoints = new ArrayList<>();
            boolean firstEdge = true;
            for (com.minicad.step.model.StepOrientedEdge orientedEdge : edgeLoop.edges()) {
                List<UvPoint> edgePoints = sampleParametricOrientedEdge(orientedEdge, geometry, mapper, builder);
                if (edgePoints == null || edgePoints.size() < 2) {
                    log.debug("stage={} faceId={}, surfaceType={}, boundId={}, edgeId={}, orientedEdgeId={}, reason={}", "parametric_loop_build_failed",
                            stepFace.id(), StepTypeNameResolver.surfaceTypeName(geometry), bound.id(),
                                    orientedEdge.edgeElement().id(), orientedEdge.id(),
                                    "edge sampling returned " + (edgePoints == null ? "null" : edgePoints.size() + " points"));
                    return List.of();
                }
                int startIndex = firstEdge ? 0 : 1;
                for (int index = startIndex; index < edgePoints.size(); index++) {
                    loopPoints.add(edgePoints.get(index));
                }
                firstEdge = false;
            }
            if (loopPoints.size() < 4) {
                log.debug("stage={} faceId={}, surfaceType={}, boundId={}, reason={}, loopPointCount={}", "parametric_loop_build_failed",
                        stepFace.id(), StepTypeNameResolver.surfaceTypeName(geometry), bound.id(),
                                "loop contains fewer than 4 UV points", loopPoints.size());
                return List.of();
            }
            if (!bound.orientation()) {
                loopPoints = StepPayloadBuilder.reverseClosedLoop(loopPoints);
            }
            loopPoints = normalizePeriodicLoop(loopPoints, mapper);
            if (!PcurveSamplingHelper.sameUv(loopPoints.get(0), loopPoints.get(loopPoints.size() - 1))) {
                loopPoints.add(loopPoints.get(0));
            }
            loops.add(new ParametricLoopPayload(bound.outer() || promoteSingleOuter, List.copyOf(loopPoints)));
        }
        return List.copyOf(loops);
    }

    private static List<UvPoint> normalizePeriodicLoop(List<UvPoint> points, ParametricSurfaceMapper mapper) {
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
                    u = MathUtilityHelper.unwrapPeriodic(u, previous.u(), uPeriod);
                }
                if (vPeriod != null) {
                    v = MathUtilityHelper.unwrapPeriodic(v, previous.v(), vPeriod);
                }
            }
            UvPoint normalizedPoint = new UvPoint(u, v);
            normalized.add(normalizedPoint);
            previous = normalizedPoint;
        }
        if (normalized.size() >= 2) {
            UvPoint first = normalized.get(0);
            UvPoint last = normalized.get(normalized.size() - 1);
            double u = last.u();
            double v = last.v();
            if (uPeriod != null) {
                u = MathUtilityHelper.unwrapPeriodic(u, first.u(), uPeriod);
            }
            if (vPeriod != null) {
                v = MathUtilityHelper.unwrapPeriodic(v, first.v(), vPeriod);
            }
            normalized.set(normalized.size() - 1, new UvPoint(u, v));
        }
        return normalized;
    }

    private static UvBounds boundsOf(List<ParametricLoopPayload> loops) {
        double minU = Double.POSITIVE_INFINITY;
        double minV = Double.POSITIVE_INFINITY;
        double maxU = Double.NEGATIVE_INFINITY;
        double maxV = Double.NEGATIVE_INFINITY;
        for (ParametricLoopPayload loop : loops) {
            for (UvPoint point : loop.points()) {
                minU = Math.min(minU, point.u());
                minV = Math.min(minV, point.v());
                maxU = Math.max(maxU, point.u());
                maxV = Math.max(maxV, point.v());
            }
        }
        if (!Double.isFinite(minU) || !Double.isFinite(minV) || !Double.isFinite(maxU) || !Double.isFinite(maxV)) {
            return null;
        }
        return new UvBounds(minU, minV, maxU, maxV);
    }

    private static FaceSurfacePayload faceSurfacePayload(
            StepEntity geometry,
            UvBounds uvBounds,
            StepCadBuilder builder
    ) {
        StepEntity surfaceGeometry = unwrapParametricPreviewSurface(geometry);
        if (surfaceGeometry instanceof StepPlane) {
            StepPlane stepPlane = (StepPlane) surfaceGeometry;
            Plane plane = builder.buildPlane(stepPlane.id());
            Direction3 normal = plane.normal();
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "plane_face",
                    List.of(plane.origin().x(), plane.origin().y(), plane.origin().z()),
                    List.of(normal.x(), normal.y(), normal.z()),
                    basisDirectionForNormal(normal),
                    0.0,
                    null,
                    null,
                    uvBounds.minU(),
                    uvBounds.maxU(),
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepCylindricalSurface) {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) surfaceGeometry;
            CylindricalSurface surface = builder.buildCylindricalSurface(cylindricalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "cylindrical_strip",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.radius(),
                    null,
                    null,
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepConicalSurface) {
            StepConicalSurface conicalSurface = (StepConicalSurface) surfaceGeometry;
            ConicalSurface surface = builder.buildConicalSurface(conicalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "conical_strip",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.radius(),
                    null,
                    surface.semiAngle(),
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepSphericalSurface) {
            StepSphericalSurface sphericalSurface = (StepSphericalSurface) surfaceGeometry;
            SphericalSurface surface = builder.buildSphericalSurface(sphericalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "spherical_surface",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.radius(),
                    null,
                    null,
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepToroidalSurface) {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) surfaceGeometry;
            ToroidalSurface surface = builder.buildToroidalSurface(toroidalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "toroidal_strip",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.majorRadius(),
                    surface.minorRadius(),
                    null,
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepDegenerateToroidalSurface) {
            StepDegenerateToroidalSurface toroidalSurface = (StepDegenerateToroidalSurface) surfaceGeometry;
            ToroidalSurface surface = builder.buildDegenerateToroidalSurface(toroidalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "toroidal_strip",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.majorRadius(),
                    surface.minorRadius(),
                    null,
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion extrusionSurface = (StepSurfaceOfLinearExtrusion) surfaceGeometry;
            SurfaceOfLinearExtrusion3 surface = builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            Direction3 axis = surface.extrusionVector().normalize().asDirection();
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "surface_of_linear_extrusion",
                    null,
                    List.of(axis.x(), axis.y(), axis.z()),
                    null,
                    0.0,
                    null,
                    null,
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution revolutionSurface = (StepSurfaceOfRevolution) surfaceGeometry;
            SurfaceOfRevolution3 surface = builder.buildSurfaceOfRevolution(revolutionSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "surface_of_revolution",
                    List.of(surface.axisOrigin().x(), surface.axisOrigin().y(), surface.axisOrigin().z()),
                    List.of(surface.axisDirection().x(), surface.axisDirection().y(), surface.axisDirection().z()),
                    null,
                    0.0,
                    null,
                    null,
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface splineSurface = (StepRationalBSplineSurface) surfaceGeometry;
            RationalBSplineSurface3 surface = builder.buildRationalBSplineSurface(splineSurface.id());
            List<List<List<Double>>> controlPoints = surface.controlPoints().stream()
                    .map(row -> row.stream()
                            .map(point -> List.of(point.x(), point.y(), point.z()))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "rational_bspline_surface",
                    null,
                    null,
                    null,
                    0.0,
                    null,
                    null,
                    surface.uStart(),
                    surface.uEnd(),
                    surface.vStart(),
                    surface.vEnd(),
                    surface.uDegree(),
                    surface.vDegree(),
                    controlPoints,
                    surface.uMultiplicities(),
                    surface.vMultiplicities(),
                    surface.uKnots(),
                    surface.vKnots(),
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepBSplineSurfaceWithKnots
                || surfaceGeometry instanceof StepBezierSurface
                || surfaceGeometry instanceof StepUniformSurface
                || surfaceGeometry instanceof StepQuasiUniformSurface
                || surfaceGeometry instanceof StepPiecewiseBezierSurface) {
            BSplineSurface3 surface = PreviewMeshExporter.buildBsplineSurface(surfaceGeometry, builder);
            List<List<List<Double>>> controlPoints = surface.controlPoints().stream()
                    .map(row -> row.stream()
                            .map(point -> List.of(point.x(), point.y(), point.z()))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "bspline_surface",
                    null,
                    null,
                    null,
                    0.0,
                    null,
                    null,
                    surface.uStart(),
                    surface.uEnd(),
                    surface.vStart(),
                    surface.vEnd(),
                    surface.uDegree(),
                    surface.vDegree(),
                    controlPoints,
                    surface.uMultiplicities(),
                    surface.vMultiplicities(),
                    surface.uKnots(),
                    surface.vKnots(),
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        return null;
    }

    private static FaceSurfacePayload withSurfaceSourceMetadata(FaceSurfacePayload base, StepEntity geometry) {
        if (base == null || geometry == null) {
            return base;
        }
        String basisType = null;
        Integer basisStepId = null;
        Boolean orientation = null;
        Double offsetDistance = null;
        Double trimU1 = null;
        Double trimU2 = null;
        Double trimV1 = null;
        Double trimV2 = null;
        Boolean implicitOuter = null;
        Double transformScale = null;

        if (geometry instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) geometry;
            basisType = StepTypeNameResolver.surfaceTypeName(trimmedSurface.basisSurface());
            basisStepId = trimmedSurface.basisSurface().id();
            trimU1 = trimmedSurface.u1();
            trimU2 = trimmedSurface.u2();
            trimV1 = trimmedSurface.v1();
            trimV2 = trimmedSurface.v2();
        } else if (geometry instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) geometry;
            basisType = StepTypeNameResolver.surfaceTypeName(boundedSurface.basisSurface());
            basisStepId = boundedSurface.basisSurface().id();
            implicitOuter = boundedSurface.implicitOuter();
        } else if (geometry instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) geometry;
            basisType = StepTypeNameResolver.surfaceTypeName(orientedSurface.surfaceElement());
            basisStepId = orientedSurface.surfaceElement().id();
            orientation = orientedSurface.orientation();
        } else if (geometry instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) geometry;
            basisType = StepTypeNameResolver.surfaceTypeName(offsetSurface.basisSurface());
            basisStepId = offsetSurface.basisSurface().id();
            offsetDistance = offsetSurface.distance();
        } else if (geometry instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) geometry).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) geometry;
            basisType = StepTypeNameResolver.surfaceTypeName(replica.parent());
            basisStepId = replica.parent().id();
            transformScale = replica.transformation().scale();
        }

        return new FaceSurfacePayload(
                base.type(),
                base.center(),
                base.axis(),
                base.xDirection(),
                base.radius(),
                base.minorRadius(),
                base.semiAngle(),
                base.lowerHeight(),
                base.upperHeight(),
                base.startAngle(),
                base.sweepAngle(),
                base.uDegree(),
                base.vDegree(),
                base.controlPoints(),
                base.uMultiplicities(),
                base.vMultiplicities(),
                base.uKnots(),
                base.vKnots(),
                StepTypeNameResolver.surfaceTypeName(geometry),
                geometry.id(),
                basisType,
                basisStepId,
                orientation,
                offsetDistance,
                trimU1,
                trimU2,
                trimV1,
                trimV2,
                implicitOuter,
                transformScale
        );
    }

    private static List<Double> basisDirectionForNormal(Direction3 normal) {
        Vector3 axis = normal.asVector();
        Vector3 reference = Math.abs(axis.x()) < 0.9
                ? new Vector3(1.0, 0.0, 0.0)
                : new Vector3(0.0, 1.0, 0.0);
        Direction3 xDirection = reference.subtract(axis.scale(reference.dot(axis))).normalize().asDirection();
        return List.of(xDirection.x(), xDirection.y(), xDirection.z());
    }

    private static List<UvPoint> sampleParametricOrientedEdge(
            com.minicad.step.model.StepOrientedEdge orientedEdge,
            StepEntity faceGeometry,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        StepVertexPoint startVertex = orientedEdge.orientation()
                ? orientedEdge.edgeElement().start()
                : orientedEdge.edgeElement().end();
        StepVertexPoint endVertex = orientedEdge.orientation()
                ? orientedEdge.edgeElement().end()
                : orientedEdge.edgeElement().start();
        StepEntity edgeGeometry = orientedEdge.edgeElement().edgeGeometry();
        StepEntity associatedSource = unwrapAssociatedCurveGeometry(edgeGeometry);
        List<StepEntity> pcurves = null;
        // Default to empty list for unsupported source types
        pcurves = List.of();
        if (pcurves.isEmpty()) {
            if (shouldFallbackToProjectedEdge(edgeGeometry)) {
                List<UvPoint> fallback = projectSampledEdge(orientedEdge, mapper, builder);
                if (fallback != null) {
                    log.debug("stage={} edgeId={}, orientedEdgeId={}, surfaceType={}, edgeGeometryType={}, reason={}", "parametric_edge_sampling_fallback",
                            orientedEdge.edgeElement().id(), orientedEdge.id(),
                                    StepTypeNameResolver.surfaceTypeName(faceGeometry), StepTypeNameResolver.surfaceTypeName(edgeGeometry),
                                    "projected sampled 3d edge because no pcurves");
                    return fallback;
                }
            }
            log.debug("stage={} edgeId={}, orientedEdgeId={}, surfaceType={}, edgeGeometryType={}, associatedGeometry={}, reason={}", "parametric_edge_sampling_failed",
                    orientedEdge.edgeElement().id(), orientedEdge.id(),
                            StepTypeNameResolver.surfaceTypeName(faceGeometry), StepTypeNameResolver.surfaceTypeName(edgeGeometry),
                            StepEdgePayloadBuilder.associatedGeometrySummary(edgeGeometry), "no matching pcurves");
            return null;
        }
        UvPoint projectedStart = mapper.project(StepPointExtractor.pointFromStep(startVertex.point()), null);
        UvPoint projectedEnd = mapper.project(StepPointExtractor.pointFromStep(endVertex.point()), projectedStart);
        List<UvPoint> best = null;
        double bestScore = Double.POSITIVE_INFINITY;
        int unsupportedPcurveCount = 0;
        for (StepEntity pcurve : pcurves) {
            Object built;
            try {
                built = builder.buildPcurve2(pcurve.id());
            } catch (UnsupportedGeometryException ex) {
                unsupportedPcurveCount++;
                continue;
            }
            if (built instanceof Line2) {
            Line2 line = (Line2) built;
                UvPoint start = PcurveSamplingHelper.snapToLine(projectedStart, line);
                UvPoint end = PcurveSamplingHelper.snapToLine(projectedEnd, line);
                double score = PcurveSamplingHelper.distanceSquared(projectedStart, start) + PcurveSamplingHelper.distanceSquared(projectedEnd, end);
                List<UvPoint> samples = PcurveSamplingHelper.sampleLinePcurve(line, start, end);
                if (best == null || score < bestScore) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof BSplineCurve2) {
            BSplineCurve2 spline = (BSplineCurve2) built;
                List<UvPoint> samples = PcurveSamplingHelper.sampleSplinePcurve(spline, projectedStart, projectedEnd);
                if (!samples.isEmpty()) {
                    double score = PcurveSamplingHelper.distanceSquared(projectedStart, samples.get(0)) + PcurveSamplingHelper.distanceSquared(projectedEnd, samples.get(samples.size() - 1));
                    if (best == null || score < bestScore) {
                        best = samples;
                        bestScore = score;
                    }
                }
                continue;
            }
            if (built instanceof Circle2) {
            Circle2 circle = (Circle2) built;
                UvPoint start = PcurveSamplingHelper.snapToCircle(projectedStart, circle);
                UvPoint end = PcurveSamplingHelper.snapToCircle(projectedEnd, circle);
                double score = PcurveSamplingHelper.distanceSquared(projectedStart, start) + PcurveSamplingHelper.distanceSquared(projectedEnd, end);
                List<UvPoint> samples = PcurveSamplingHelper.sampleCirclePcurve(circle, start, end);
                if (!samples.isEmpty() && (best == null || score < bestScore)) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof Ellipse2) {
            Ellipse2 ellipse = (Ellipse2) built;
                UvPoint start = PcurveSamplingHelper.snapToEllipse(projectedStart, ellipse);
                UvPoint end = PcurveSamplingHelper.snapToEllipse(projectedEnd, ellipse);
                double score = PcurveSamplingHelper.distanceSquared(projectedStart, start) + PcurveSamplingHelper.distanceSquared(projectedEnd, end);
                List<UvPoint> samples = PcurveSamplingHelper.sampleEllipsePcurve(ellipse, start, end);
                if (!samples.isEmpty() && (best == null || score < bestScore)) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof TrimmedCurve2) {
            TrimmedCurve2 trimmed = (TrimmedCurve2) built;
                List<UvPoint> samples = PcurveSamplingHelper.sampleTrimmedPcurve(trimmed, projectedStart, projectedEnd);
                if (!samples.isEmpty()) {
                    double score = PcurveSamplingHelper.distanceSquared(projectedStart, samples.get(0)) + PcurveSamplingHelper.distanceSquared(projectedEnd, samples.get(samples.size() - 1));
                    if (best == null || score < bestScore) {
                        best = samples;
                        bestScore = score;
                    }
                }
            }
        }
        if (best == null) {
            List<UvPoint> fallback = projectSampledEdge(orientedEdge, mapper, builder);
            if (fallback != null) {
                log.debug("stage={} edgeId={}, orientedEdgeId={}, surfaceType={}, edgeGeometryType={}, pcurveCount={}, unsupportedPcurveCount={}, reason={}", "parametric_edge_sampling_fallback",
                        orientedEdge.edgeElement().id(), orientedEdge.id(),
                                StepTypeNameResolver.surfaceTypeName(faceGeometry), StepTypeNameResolver.surfaceTypeName(edgeGeometry),
                                pcurves.size(), unsupportedPcurveCount,
                                "projected sampled 3d edge after unusable pcurves");
                return fallback;
            }
            log.debug("stage={} edgeId={}, orientedEdgeId={}, surfaceType={}, pcurveCount={}, unsupportedPcurveCount={}, pcurveBasisSurfaces={}, reason={}", "parametric_edge_sampling_failed",
                    orientedEdge.edgeElement().id(), orientedEdge.id(),
                            StepTypeNameResolver.surfaceTypeName(faceGeometry), pcurves.size(),
                            unsupportedPcurveCount, pcurveBasisSurfaceSummary(pcurves),
                            "no usable pcurve samples");
        }
        return best;
    }

    private static List<UvPoint> projectSampledEdge(
            com.minicad.step.model.StepOrientedEdge orientedEdge,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> sampled = sampleStepOrientedEdge(orientedEdge, builder);
        if (sampled.size() < 2) {
            return null;
        }
        List<UvPoint> points = new ArrayList<>(sampled.size());
        UvPoint previous = null;
        for (CartesianPoint point : sampled) {
            UvPoint uv = mapper.project(point, previous);
            if (uv == null) {
                return null;
            }
            points.add(uv);
            previous = uv;
        }
        return List.copyOf(points);
    }

    private static boolean shouldFallbackToProjectedEdge(StepEntity edgeGeometry) {
        StepEntity unwrapped = unwrapAssociatedCurveGeometry(edgeGeometry);
        if (unwrapped instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) unwrapped;
            return surfaceCurve.associatedGeometry().isEmpty();
        } else if (unwrapped instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) unwrapped;
            return seamCurve.associatedGeometry().isEmpty();
        } else {
            return true;
        }
    }

    private static List<CartesianPoint> sampleStepOrientedEdge(
            com.minicad.step.model.StepOrientedEdge orientedEdge,
            StepCadBuilder builder
    ) {
        StepEdgeCurve edge = orientedEdge.edgeElement();
        CartesianPoint start = StepPointExtractor.pointFromStep(orientedEdge.orientation() ? edge.start().point() : edge.end().point());
        CartesianPoint end = StepPointExtractor.pointFromStep(orientedEdge.orientation() ? edge.end().point() : edge.start().point());
        boolean naturalForward = orientedEdge.orientation() ? edge.sameSense() : !edge.sameSense();
        Curve3 curve = StepEdgePayloadBuilder.curveForLooseEdge(edge.edgeGeometry(), builder);
        if (curve == null) {
            return List.of();
        }
        try {
            return StepEdgePayloadBuilder.sampleEdge(start, end, curve, naturalForward);
        } catch (GeometryException ex) {
            return List.of(start, end);
        }
    }

    private static StepEntity unwrapAssociatedCurveGeometry(StepEntity edgeGeometry) {
        // Delegates to the shared SEMANTIC_CURVE_UNWRAP_RULES table; see StepEdgePayloadBuilder.
        return StepEdgePayloadBuilder.previewCurveSemanticItem(edgeGeometry);
    }

    private static List<StepEntity> matchingPcurves(List<StepEntity> associatedGeometry, StepEntity faceGeometry) {
        Set<Integer> acceptableSurfaceIds = acceptablePcurveBasisSurfaceIds(faceGeometry);
        List<StepEntity> matches = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve && acceptableSurfaceIds.contains(((StepPcurve) associated).basisSurface().id())) {
                StepPcurve pcurve = (StepPcurve) associated;
                matches.add(pcurve);
            } else if (associated instanceof StepDegeneratePcurve && acceptableSurfaceIds.contains(((StepDegeneratePcurve) associated).basisSurface().id())) {
                StepDegeneratePcurve pcurve = (StepDegeneratePcurve) associated;
                matches.add(pcurve);
            }
        }
        return List.copyOf(matches);
    }

    private static Set<Integer> acceptablePcurveBasisSurfaceIds(StepEntity faceGeometry) {
        LinkedHashSet<Integer> ids = new LinkedHashSet<>();
        StepEntity current = faceGeometry;
        for (int depth = 0; depth < 16 && current != null; depth++) {
            ids.add(current.id());
            if (current instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) current;
                current = trimmedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) current;
                current = boundedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) current;
                current = orientedSurface.surfaceElement();
                continue;
            }
            if (current instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) current;
                current = offsetSurface.basisSurface();
                continue;
            }
            if (current instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) current).entityName())) {
                StepGeometricReplica replica = (StepGeometricReplica) current;
                current = replica.parent();
                continue;
            }
            break;
        }
        return Set.copyOf(ids);
    }

    private static List<LoopPayload> toParametricLoopPayloads(List<ParametricLoopPayload> loops, ParametricSurfaceMapper mapper) {
        List<LoopPayload> payloads = new ArrayList<>(loops.size());
        for (ParametricLoopPayload loop : loops) {
            List<PointPayload> points = new ArrayList<>(loop.points().size());
            for (UvPoint point : loop.points()) {
                points.add(PayloadConversionHelper.toPointPayload(mapper.pointAt(point.u(), point.v())));
            }
            payloads.add(new LoopPayload(loop.outer(), List.copyOf(points)));
        }
        return List.copyOf(payloads);
    }

    private static List<PointPayload> triangulateParametricFace(
            ParametricSurfaceMapper mapper,
            List<ParametricLoopPayload> loops,
            UvBounds bounds,
            int uSegments,
            int vSegments,
            boolean sameSense
    ) {
        ParametricLoopPayload outer = loops.stream().filter(ParametricLoopPayload::outer).findFirst().orElse(null);
        if (outer == null) {
            return List.of();
        }
        List<ParametricLoopPayload> holes = loops.stream().filter(loop -> !loop.outer()).collect(Collectors.toList());
        List<PointPayload> triangles = new ArrayList<>();
        for (int ui = 0; ui < uSegments; ui++) {
            double u0 = bounds.minU() + bounds.uSpan() * ui / uSegments;
            double u1 = bounds.minU() + bounds.uSpan() * (ui + 1) / uSegments;
            for (int vi = 0; vi < vSegments; vi++) {
                double v0 = bounds.minV() + bounds.vSpan() * vi / vSegments;
                double v1 = bounds.minV() + bounds.vSpan() * (vi + 1) / vSegments;
                UvPoint center = new UvPoint((u0 + u1) * 0.5, (v0 + v1) * 0.5);
                if (!TriangulationHelper.contains(outer.points(), center)) {
                    continue;
                }
                boolean insideHole = false;
                for (ParametricLoopPayload hole : holes) {
                    if (TriangulationHelper.contains(hole.points(), center)) {
                        insideHole = true;
                        break;
                    }
                }
                if (insideHole) {
                    continue;
                }
                CartesianPoint p00 = mapper.pointAt(u0, v0);
                CartesianPoint p10 = mapper.pointAt(u1, v0);
                CartesianPoint p01 = mapper.pointAt(u0, v1);
                CartesianPoint p11 = mapper.pointAt(u1, v1);
                Vector3 normal = mapper.normalAt(center.u(), center.v());
                if (!sameSense) {
                    normal = normal.scale(-1.0);
                }
                TriangulationHelper.appendOrientedTriangle(triangles, p00, p10, p11, normal);
                TriangulationHelper.appendOrientedTriangle(triangles, p00, p11, p01, normal);
            }
        }
        return List.copyOf(triangles);
    }

    private static List<ParametricLoopPayload> normalizeLoopRoles(
            StepFaceEntity stepFace,
            StepEntity geometry,
            List<ParametricLoopPayload> loops
    ) {
        if (loops.isEmpty() || loops.stream().anyMatch(ParametricLoopPayload::outer)) {
            return loops;
        }
        int outerIndex = -1;
        double outerArea = Double.NEGATIVE_INFINITY;
        for (int index = 0; index < loops.size(); index++) {
            double area = Math.abs(TriangulationHelper.signedArea(loops.get(index).points()));
            if (area > outerArea + Epsilon.EPS) {
                outerArea = area;
                outerIndex = index;
            }
        }
        if (outerIndex < 0) {
            return loops;
        }
        log.debug("stage={} faceId={}, surfaceType={}, loopCount={}, inferredOuterIndex={}, inferredOuterArea={}", "parametric_outer_bound_inferred",
                stepFace.id(), StepTypeNameResolver.surfaceTypeName(geometry), loops.size(), outerIndex, outerArea);
        List<ParametricLoopPayload> normalized = new ArrayList<>(loops.size());
        for (int index = 0; index < loops.size(); index++) {
            normalized.add(new ParametricLoopPayload(index == outerIndex, loops.get(index).points()));
        }
        return List.copyOf(normalized);
    }

    private static List<PointPayload> triangulateParametricFaceAdaptive(
            ParametricSurfaceMapper mapper,
            List<ParametricLoopPayload> loops,
            UvBounds bounds,
            int baseUSegments,
            int baseVSegments,
            boolean sameSense
    ) {
        int uSegments = baseUSegments;
        int vSegments = baseVSegments;
        for (int attempt = 0; attempt < 4; attempt++) {
            List<PointPayload> triangles = triangulateParametricFace(mapper, loops, bounds, uSegments, vSegments, sameSense);
            if (!triangles.isEmpty()) {
                return triangles;
            }
            if (uSegments >= 512 && vSegments >= 256) {
                break;
            }
            uSegments = Math.min(uSegments * 2, 512);
            vSegments = Math.min(vSegments * 2, 256);
        }
        return List.of();
    }

    private static StepEntity faceGeometry(StepFaceEntity stepFace) {
        return StepGeometryHelper.faceGeometry(stepFace);
    }

    private static FacePayload reverseFacePayload(FacePayload base) {
        return StepPayloadBuilder.reverseFacePayload(base);
    }

    private static List<CartesianPoint> sampleLoop(FaceBound bound) {
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
            return bound.orientation() ? sampled : StepPayloadBuilder.reverseClosedLoop(sampled);
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
        return bound.orientation() ? sampled : StepPayloadBuilder.reverseClosedLoop(sampled);
    }
    private static String pcurveBasisSurfaceSummary(List<StepEntity> pcurves) {
        return StepSummaryBuilder.pcurveBasisSurfaceSummary(pcurves);
    }


    static UnsupportedFacePayload toUnsupportedFacePayload(StepFaceEntity stepFace, String reason) {
        StepEntity geometry = StepGeometryHelper.faceGeometry(stepFace);
        return new UnsupportedFacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                StepTypeNameResolver.surfaceTypeName(geometry),
                reason == null ? "preview export returned no mesh" : reason
        );
    }


    static List<FaceBound> buildFaceBounds(StepFaceEntity stepFace, StepCadBuilder builder) {
        List<FaceBound> bounds = stepFace.bounds().stream().map(bound -> builder.buildFaceBound(bound.id())).collect(Collectors.toList());
        if (bounds.stream().noneMatch(FaceBound::outer) && bounds.size() == 1) {
            FaceBound bound = bounds.get(0);
            return List.of(FaceBound.outer(bound.loop(), bound.orientation()));
        }
        return bounds;
    }

}
