package com.minicad.export.json;

import java.util.*;
import java.util.stream.Collectors;
import com.minicad.helper.StepMetadataExtractor;
import com.minicad.step.model.*;
import com.minicad.step.semantic.*;
import com.minicad.topology.*;
import com.minicad.geometry.*;
import com.minicad.common.*;
import com.minicad.preview.payload.*;
import com.minicad.preview.statistics.*;
import com.minicad.export.glb.*;
import com.minicad.builder.StepAssemblyGraphBuilder;
import com.minicad.helper.*;
import com.minicad.geometry2d.*;
import com.minicad.step.syntax.*;
import com.minicad.preview.sampling.*;
import com.minicad.preview.mapper.*;
import com.minicad.export.mesh.*;
import com.minicad.builder.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StepLegacyGeometryBuilder.
 */

public final class StepLegacyGeometryBuilder {
    private static final Logger log = LoggerFactory.getLogger(StepLegacyGeometryBuilder.class);
    private StepLegacyGeometryBuilder() {}


    /**
     * Builds the legacy geometry collection: shell-backed faces, solids and
     * standalone edges, each bucketed by its own classifier.
     *
     * <p>This method and its three helpers ({@code buildGeometryForShells},
     * {@code buildGeometryForSolids}, {@code mergeGeometry}) are the single home
     * of the legacy-geometry orchestration. The {@code preview} package used to
     * carry a copy of all four behind a {@code PreviewFaceBuilder} facade that
     * nothing called, and the copy had already drifted: it never received the
     * {@code StepFacetedBrepAndBrepWithVoids} bucket entry below, so the two
     * copies answered differently for that entity. The copy and its facade are
     * gone rather than re-synchronised, because a second copy is what drifts.
     */
    public static GeometryCollection buildLegacyGeometry(
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        Set<Integer> shellIds = new TreeSet<>();
        Set<Integer> solidIds = new TreeSet<>();
        Map<Integer, EdgePayload> standaloneEdges = new LinkedHashMap<>();
        for (StepEntity entity : resolved.values()) {
            collectShellLikeIds(entity, shellIds);
            if (isLegacyGeometrySolidItem(entity)) {
                solidIds.add(entity.id());
            }
            if (StepValidationHelper.isStandaloneEdgeSource(entity)) {
                StepEdgePayloadBuilder.collectStandaloneEdges(entity, standaloneEdges, resolved, builder, metadata);
            }
        }
        // Remove shells that are referenced by B-rep solids to avoid duplicate processing
        for (Integer solidId : solidIds) {
            removeShellsReferencedBySolids(resolved.get(solidId), shellIds);
        }
        GeometryCollection shellGeometry = buildGeometryForShells(shellIds, resolved, builder, metadata, Map.of());
        GeometryCollection solidGeometry = buildGeometryForSolids(solidIds, resolved, builder, metadata, Map.of());
        GeometryCollection edgeGeometry = new GeometryCollection(List.copyOf(standaloneEdges.values()), List.of(), List.of());
        return mergeGeometry(mergeGeometry(shellGeometry, solidGeometry), edgeGeometry);
    }


    static GeometryCollection buildGeometryForShells(
            Set<Integer> shellIds,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Map<Integer, StepMetadataExtractor.DisplayMetadata> inheritedShellMetadata
    ) {
        log.debug("stage={} shellCount={}", "geometry_shells_start", shellIds.size());
        List<FacePayload> faces = new ArrayList<>();
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>();
        Set<Integer> uniqueEdgeIds = new LinkedHashSet<>();
        int processedFaces = 0;

        for (Integer shellId : shellIds) {
            StepEntity shellEntity = resolved.get(shellId);
            if (shellEntity instanceof StepTessellatedFaceSet) {
            StepTessellatedFaceSet tessellated = (StepTessellatedFaceSet) shellEntity;
                List<FacePayload> tessFaces = TessellatedFaceExporter.buildTessellatedFacePayloads(tessellated, metadata.forItem(shellId));
                faces.addAll(tessFaces);
                log.debug("stage={} shellId={}, tessellatedFaceCount={}", "geometry_tessellated_shell", shellId, tessFaces.size());
                continue;
            }
            if (shellEntity instanceof StepTessellatedFace) {
            StepTessellatedFace tessellatedFace = (StepTessellatedFace) shellEntity;
                FacePayload payload = TessellatedFaceExporter.buildTessellatedFacePayload(tessellatedFace, metadata.forItem(shellId));
                if (payload != null) {
                    faces.add(payload);
                }
                log.debug("stage={} shellId={}, tessellatedFaceBuilt={}", "geometry_tessellated_face", shellId, payload != null);
                continue;
            }
            List<StepFaceEntity> shellFaces = ShellHelper.shellFaces(shellEntity);
            log.debug("stage={} shellId={}, shellFaceCount={}", "geometry_shell_start", shellId, shellFaces.size());
            for (StepFaceEntity stepFace : shellFaces) {
                PreviewFaceResult previewFace = StepFacePayloadBuilder.buildPreviewFaceResult(
                        stepFace,
                        builder,
                        StepMetadataHelper.mergeMetadata(inheritedShellMetadata.get(shellId), metadata.forItem(stepFace.id()))
                );
                processedFaces++;
                if (previewFace.face() == null) {
                    unsupportedFaces.add(previewFace.unsupportedFace());
                    if (unsupportedFaces.size() <= 10 || unsupportedFaces.size() % StepLegacyGeometryBuilder.FACE_PROGRESS_INTERVAL == 0) {
                        log.debug("stage={} faceId={}, processedFaces={}, unsupportedFaces={}, reason={}", "geometry_face_unsupported",
                                stepFace.id(), processedFaces, unsupportedFaces.size(), (previewFace.unsupportedFace() == null ? "null" : previewFace.unsupportedFace().reason()));
                    }
                    continue;
                }
                faces.add(previewFace.face());
                if (processedFaces % StepLegacyGeometryBuilder.FACE_PROGRESS_INTERVAL == 0) {
                    log.debug("stage={} processedFaces={}, supportedFaces={}, unsupportedFaces={}, uniqueEdges={}", "geometry_face_progress",
                            processedFaces, faces.size(), unsupportedFaces.size(), uniqueEdgeIds.size());
                }
                for (com.minicad.step.model.StepFaceBound bound : stepFace.bounds()) {
                    if (bound.loop() instanceof com.minicad.step.model.StepEdgeLoop) {
                        com.minicad.step.model.StepEdgeLoop edgeLoop = (com.minicad.step.model.StepEdgeLoop) bound.loop();
                        for (StepOrientedEdge edge : edgeLoop.edges()) {
                            uniqueEdgeIds.add(edge.edgeElement().id());
                        }
                    }
                }
            }
        }

        List<EdgePayload> edges = new ArrayList<>();
        int processedEdges = 0;
        for (Integer edgeId : uniqueEdgeIds) {
            edges.add(StepEdgePayloadBuilder.buildEdgePayload(edgeId, resolved, builder, metadata));
            processedEdges++;
            if (processedEdges % StepLegacyGeometryBuilder.EDGE_PROGRESS_INTERVAL == 0) {
                log.debug("stage={} processedEdges={}, totalUniqueEdges={}", "geometry_edge_progress",
                        processedEdges, uniqueEdgeIds.size());
            }
        }
        log.debug("stage={} shellCount={}, processedFaces={}, supportedFaces={}, unsupportedFaces={}, edges={}", "geometry_shells_done",
                shellIds.size(), processedFaces, faces.size(), unsupportedFaces.size(), edges.size());
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }


    static GeometryCollection buildGeometryForSolids(
            Set<Integer> solidIds,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Map<Integer, StepMetadataExtractor.DisplayMetadata> inheritedSolidMetadata
    ) {
        List<EdgePayload> edges = new ArrayList<>();
        List<FacePayload> faces = new ArrayList<>();
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>();
        Set<Edge> uniqueEdges = new LinkedHashSet<>();

        for (Integer solidId : solidIds) {
            StepEntity entity = resolved.get(solidId);
            StepMetadataExtractor.DisplayMetadata itemMetadata = StepMetadataHelper.mergeMetadata(
                    inheritedSolidMetadata.get(solidId),
                    metadata.forItem(solidId)
            );
            try {
                Solid solid = builder.buildSolid(solidId);
                String baseName = entity == null ? null : entity.name();
                List<StepEntity> shellEntities = solidShellEntities(entity);
                if (!shellEntities.isEmpty()) {
                    // A B-rep solid owns its shells' faces, and each of those was
                    // resolved against the STEP surface it names. Preview them
                    // through the semantic path the shell bucket uses, which
                    // tessellates the face's trim loops; the topology grid path
                    // below can only sample the whole carrier surface, so a
                    // trimmed cylinder or torus came out as the full patch.
                    // Edges stay on the built topology faces so this pass keeps
                    // emitting the same edge ids it always has.
                    collectSemanticFaces(shellEntities, itemMetadata, builder, faces, unsupportedFaces);
                    collectSolidTopologyEdges(solid, uniqueEdges);
                    continue;
                }
                int faceIndex = 0;
                for (Face face : solid.outerShell().faces()) {
                    collectFace(solidId * 1000 + faceIndex++, face, baseName, itemMetadata,
                            faces, uniqueEdges, unsupportedFaces);
                }
                for (var voidShell : solid.voidShells()) {
                    for (Face face : voidShell.faces()) {
                        collectFace(solidId * 1000 + faceIndex++, face, baseName, itemMetadata,
                                faces, uniqueEdges, unsupportedFaces);
                    }
                }
            } catch (UnsupportedGeometryException | StepResolutionException | TopologyException ex) {
                unsupportedFaces.add(new UnsupportedFacePayload(
                        solidId,
                        entity == null ? null : entity.name(),
                        StepTypeNameResolver.geometryTypeName(entity),
                        ex.getMessage()
                ));
            }
        }

        int edgeIndex = 0;
        for (Edge edge : uniqueEdges) {
            edges.add(StepEdgePayloadBuilder.buildTopologyEdgePayload(-(edgeIndex + 1), edge));
            edgeIndex++;
        }
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }

    /**
     * Adds the faces of the given shells through the semantic preview path, the
     * same one the shell bucket uses. A face that fails to build is recorded as
     * unsupported instead of taking the rest of the solid down with it.
     *
     * <p>Planar faces keep the payload the solid bucket has always emitted for
     * them: trim loops plus the {@code plane_face} surface, no triangles. The
     * viewer tessellates those from {@code surfaceLoops}, so adding a mesh here
     * would both double the payload and draw the face twice.</p>
     */
    private static void collectSemanticFaces(
            List<StepEntity> shellEntities,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder,
            List<FacePayload> faces,
            List<UnsupportedFacePayload> unsupportedFaces) {
        for (StepEntity shell : shellEntities) {
            for (StepFaceEntity stepFace : ShellHelper.shellFaces(shell)) {
                try {
                    if (StepGeometryHelper.faceGeometry(stepFace) instanceof StepPlane) {
                        FacePayload planePayload = PreviewMeshExporter.facePayloadFromTopologyFace(
                                stepFace.id(), builder.buildFace(stepFace.id()),
                                StepMetadataHelper.faceDisplayName(stepFace), metadata);
                        if (planePayload == null) {
                            unsupportedFaces.add(StepFacePayloadBuilder.toUnsupportedFacePayload(
                                    stepFace, "planar face preview returned no payload"));
                        } else {
                            faces.add(planePayload);
                        }
                        continue;
                    }
                    PreviewFaceResult previewFace =
                            StepFacePayloadBuilder.buildPreviewFaceResult(stepFace, builder, metadata);
                    if (previewFace.face() == null) {
                        unsupportedFaces.add(previewFace.unsupportedFace());
                        continue;
                    }
                    faces.add(previewFace.face());
                } catch (RuntimeException ex) {
                    unsupportedFaces.add(StepFacePayloadBuilder.toUnsupportedFacePayload(stepFace, ex.getMessage()));
                }
            }
        }
    }

    private static void collectSolidTopologyEdges(Solid solid, Set<Edge> uniqueEdges) {
        for (Face face : solid.outerShell().faces()) {
            StepPayloadBuilder.collectTopologyEdges(face, uniqueEdges);
        }
        for (Shell shell : solid.voidShells()) {
            for (Face face : shell.faces()) {
                StepPayloadBuilder.collectTopologyEdges(face, uniqueEdges);
            }
        }
    }

    /**
     * Adds a solid face to the payload, or records it as unsupported when the
     * face has no triangulable preview (for example a sliver whose sampled grid
     * collapses).
     */
    private static void collectFace(int stepId, Face face, String baseName,
                                    StepMetadataExtractor.DisplayMetadata metadata,
                                    List<FacePayload> faces, Set<Edge> uniqueEdges,
                                    List<UnsupportedFacePayload> unsupportedFaces) {
        FacePayload payload = PreviewMeshExporter.facePayloadFromTopologyFace(
                stepId, face, baseName, metadata);
        if (payload == null) {
            unsupportedFaces.add(new UnsupportedFacePayload(
                    stepId, baseName,
                    face.surface().getClass().getSimpleName().toUpperCase(Locale.ROOT),
                    "face preview could not be triangulated"));
            return;
        }
        faces.add(payload);
        StepPayloadBuilder.collectTopologyEdges(face, uniqueEdges);
    }


    /**
     * Concatenates the three payload lists of two collections. Public because the
     * legacy-geometry merge is also driven directly from tests; it is the merge
     * helper {@link #buildLegacyGeometry} composes its three buckets with.
     */
    public static GeometryCollection mergeGeometry(GeometryCollection left, GeometryCollection right) {
        List<EdgePayload> edges = new ArrayList<>(left.edges());
        edges.addAll(right.edges());
        List<FacePayload> faces = new ArrayList<>(left.faces());
        faces.addAll(right.faces());
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>(left.unsupportedFaces());
        unsupportedFaces.addAll(right.unsupportedFaces());
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }


    /**
     * Collects the ids of the shell-like items reachable from {@code item}.
     *
     * <p>This is the single home of the rule. The {@code preview} package used to
     * carry a second copy behind a {@code PreviewFaceBuilder} facade nothing
     * called, and this side was one line forwarding to it; inlined here so the
     * walk and its vocabulary sit next to the pipeline that consumes it.
     *
     * <p>Styled wrappers are unwrapped rather than skipped, and the entity types
     * that are known to hold no shell (solids, mapped items, surface patches) are
     * named explicitly so they do not fall through to the shell/face-set branches.
     */
    static void collectShellLikeIds(StepEntity item, Set<Integer> shellIds) {
        if (item instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) item;
            collectShellLikeIds(styledItem.item(), shellIds);
            return;
        }
        if (item instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) item;
            collectShellLikeIds(styledItem.item(), shellIds);
            return;
        }
        if (ShellHelper.isShellLikeEntity(item)) {
            shellIds.add(item.id());
            return;
        }
        if (item instanceof StepManifoldSolidBrep
                || item instanceof StepFacettedBrep
                || item instanceof StepNonManifoldSolidBrep
                || item instanceof StepAdvancedBrep
                || item instanceof StepBrepWithVoids
                || item instanceof StepMappedItem
                || item instanceof StepSolidModel
                || item instanceof StepSurfacePatch) {
            return;
        }
        if (item instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel surfaceModel = (StepShellBasedSurfaceModel) item;
            for (StepEntity shell : surfaceModel.shells()) {
                collectShellLikeIds(shell, shellIds);
            }
            return;
        }
        if (item instanceof StepTessellatedFaceSet) {
            shellIds.add(item.id());
            return;
        }
        if (item instanceof StepTessellatedFace) {
            shellIds.add(item.id());
            return;
        }
        if (item instanceof StepManifoldSurfaceModel) {
            StepManifoldSurfaceModel manifoldModel = (StepManifoldSurfaceModel) item;
            for (StepEntity shell : manifoldModel.shells()) {
                collectShellLikeIds(shell, shellIds);
            }
            return;
        }
        if (item instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel faceModel = (StepFaceBasedSurfaceModel) item;
            for (StepEntity faceSet : faceModel.faceSets()) {
                collectShellLikeIds(faceSet, shellIds);
            }
        }
    }

    // Delegate to StepEntityUnwrapper - extracted utility class
    static StepEntity unwrapStyledItem(StepEntity item) {
        return StepEntityUnwrapper.unwrapStyledItem(item);
    }

    // The two entity classifiers that used to be declared here are gone:
    // isStandaloneEdgeSource and isRepresentationSolidItem both live in
    // StepValidationHelper, and every caller in this package now names it
    // directly. Keeping a package-private twin here only invited drift --
    // this file's copy of isRepresentationSolidItem already disagreed with
    // the StepValidationHelper one by 14 types.

    // buildLegacyGeometry B-rep-solid shell-removal rules.
    // Clean first-match else-if chain: the six B-rep types are mutually exclusive (no subtype
    // relationship), so order is not load-bearing, but the table preserves the original order.
    private record LegacySolidShellRule(Class<? extends StepEntity> type, LegacySolidShellProvider provider) {}

    private interface LegacySolidShellProvider {
        List<StepEntity> shells(StepEntity solidEntity);
    }

    private static LegacySolidShellRule legacySolidShellRule(
            Class<? extends StepEntity> type, LegacySolidShellProvider provider) {
        return new LegacySolidShellRule(type, provider);
    }

    private static final List<LegacySolidShellRule> LEGACY_SOLID_SHELL_RULES = List.of(
        legacySolidShellRule(StepManifoldSolidBrep.class, solidEntity ->
                List.of(((StepManifoldSolidBrep) solidEntity).outer())),
        legacySolidShellRule(StepFacettedBrep.class, solidEntity ->
                List.of(((StepFacettedBrep) solidEntity).outer())),
        legacySolidShellRule(StepNonManifoldSolidBrep.class, solidEntity ->
                List.of(((StepNonManifoldSolidBrep) solidEntity).outer())),
        legacySolidShellRule(StepAdvancedBrep.class, solidEntity -> {
            StepAdvancedBrep brep = (StepAdvancedBrep) solidEntity;
            List<StepEntity> shells = new ArrayList<>(brep.voids().size() + 1);
            shells.add(brep.outer());
            shells.addAll(brep.voids());
            return List.copyOf(shells);
        }),
        legacySolidShellRule(StepBrepWithVoids.class, solidEntity -> {
            StepBrepWithVoids brep = (StepBrepWithVoids) solidEntity;
            List<StepEntity> shells = new ArrayList<>(brep.voids().size() + 1);
            shells.add(brep.outer());
            shells.addAll(brep.voids());
            return List.copyOf(shells);
        }),
        legacySolidShellRule(StepFacetedBrepAndBrepWithVoids.class, solidEntity -> {
            StepFacetedBrepAndBrepWithVoids brep = (StepFacetedBrepAndBrepWithVoids) solidEntity;
            List<StepEntity> shells = new ArrayList<>(brep.voids().size() + 1);
            shells.add(brep.outer());
            shells.addAll(brep.voids());
            return List.copyOf(shells);
        })
    );

    /**
     * Decides whether an entity goes into the <em>solid</em> bucket of
     * {@link #buildLegacyGeometry}, as opposed to the shell bucket or the
     * standalone-edge bucket.
     *
     * <p>This was an inline {@code instanceof} chain of 39 alternatives until it
     * was given a name, for two reasons. First, it is a rule, and a rule that
     * only exists as a 39-line expression inside a loop cannot be asserted
     * directly -- there was no way to ask "is this entity a solid?" without
     * driving a whole file through the orchestrator. Second, naming it makes the
     * relationship to {@link StepValidationHelper#isRepresentationSolidItem}
     * legible: that classifier is <em>narrower</em> (27 types) and this one is a
     * strict superset of it. The two are deliberately separate questions --
     * "would a representation list this as a solid item?" versus "does the legacy
     * geometry pipeline have a builder for it?" -- so the extra 12 types below
     * are enumerated rather than folded into the narrower list, and neither
     * method may be replaced by the other.
     *
     * <p>The 12 types this rule adds over {@code isRepresentationSolidItem} are
     * the face-backed solids ({@code StepExtrudedFaceSolid},
     * {@code StepRevolvedFaceSolid}, {@code StepSweptFaceSolid}), the primitive
     * volumes ({@code StepCylinderVolume}, {@code StepSphereVolume},
     * {@code StepTorusVolume}, {@code StepPrismVolume},
     * {@code StepRightCircularConeVolume}), the triangulated faces
     * ({@code StepTriangulatedFace}, {@code StepComplexTriangulatedFace},
     * {@code StepCubicBezierTriangulatedFace}) and
     * {@code StepFacetedBrepAndBrepWithVoids} -- the last of which is exactly the
     * type the deleted preview copy was missing.
     *
     * @param entity the STEP entity to bucket
     * @return true if the entity is built as solid geometry by the legacy pipeline
     */
    static boolean isLegacyGeometrySolidItem(StepEntity entity) {
        return entity instanceof StepSweptAreaSolid
                || entity instanceof StepSolidReplica
                || entity instanceof StepCsgSolid
                || entity instanceof StepCsgPrimitive
                || entity instanceof StepBooleanClippingResult
                || entity instanceof StepBooleanResult
                || entity instanceof StepSweptDiskSolid
                || entity instanceof StepExtrudedAreaSolidTapered
                || entity instanceof StepRevolvedAreaSolidTapered
                || entity instanceof StepSurfaceCurveSweptAreaSolid
                || entity instanceof StepPolygonalBoundedHalfSpace
                || entity instanceof StepComplexClippingResult
                || entity instanceof StepHalfSpaceSolid
                || entity instanceof StepCsgVolume
                || entity instanceof StepBlockVolume
                || entity instanceof StepFiniteElementMesh
                || entity instanceof StepFlatPattern
                || entity instanceof StepBrepWithVoids
                || entity instanceof StepFacetedBrepAndBrepWithVoids
                || entity instanceof StepManifoldSolidBrep
                || entity instanceof StepFacettedBrep
                || entity instanceof StepNonManifoldSolidBrep
                || entity instanceof StepAdvancedBrep
                || entity instanceof StepMappedItem
                || entity instanceof StepSolidModel
                || entity instanceof StepSurfacePatch
                || entity instanceof StepExtrudedFaceSolid
                || entity instanceof StepRevolvedFaceSolid
                || entity instanceof StepSweptFaceSolid
                || entity instanceof StepCylinderVolume
                || entity instanceof StepSphereVolume
                || entity instanceof StepTorusVolume
                || entity instanceof StepPrismVolume
                || entity instanceof StepRightCircularConeVolume
                || entity instanceof StepTessellatedFace
                || entity instanceof StepTessellatedFaceSet
                || entity instanceof StepTriangulatedFace
                || entity instanceof StepComplexTriangulatedFace
                || entity instanceof StepCubicBezierTriangulatedFace;
    }

    private static void removeShellsReferencedBySolids(StepEntity solidEntity, Set<Integer> shellIds) {
        for (StepEntity shell : solidShellEntities(solidEntity)) {
            shellIds.remove(shell.id());
        }
    }

    /**
     * Returns the shells a B-rep solid is made of (outer shell first, then any
     * void shells), or an empty list for the solid types that are not shell-backed
     * (sweeps, CSG, volumes, tessellated bodies). Those keep the topology path.
     */
    private static List<StepEntity> solidShellEntities(StepEntity solidEntity) {
        if (solidEntity == null) {
            return List.of();
        }
        for (LegacySolidShellRule rule : LEGACY_SOLID_SHELL_RULES) {
            if (rule.type().isInstance(solidEntity)) {
                return rule.provider().shells(solidEntity);
            }
        }
        return List.of();
    }

    private static final int FACE_PROGRESS_INTERVAL = 25;
    private static final int EDGE_PROGRESS_INTERVAL = 100;
}
