package com.minicad.app;

import com.minicad.geometry.*;
import com.minicad.step.model.annotation.*;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.base.StepFaceEntity;
import com.minicad.step.model.fea.StepFiniteElementMesh;
import com.minicad.step.model.geometry.*;
import com.minicad.step.model.manufacturing.StepChamferEdge;
import com.minicad.step.model.manufacturing.StepFilletEdge;
import com.minicad.step.model.manufacturing.StepFlatPattern;
import com.minicad.step.model.product.*;
import com.minicad.step.model.tolerance.StepDimensionCurve;
import com.minicad.step.model.topology.*;
import com.minicad.step.model.workflow.StepRepresentation;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.topology.Edge;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.OrientedEdge;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Geometry collection orchestration for STEP preview export.
 * Extracted from PreviewFaceBuilder to isolate geometry collection logic.
 */
public final class PreviewGeometryCollector {

    private PreviewGeometryCollector() {}

    // ─── Geometry collection orchestration ───────────────────────────────

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
            if (entity instanceof StepSweptAreaSolid
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
                    || entity instanceof StepCubicBezierTriangulatedFace) {
                solidIds.add(entity.id());
            }
            if (isStandaloneEdgeSource(entity)) {
                collectStandaloneEdges(entity, standaloneEdges, resolved, builder, metadata);
            }
        }
        // Remove shells that are referenced by B-rep solids to avoid duplicate processing
        for (Integer solidId : solidIds) {
            StepEntity solidEntity = resolved.get(solidId);
            if (solidEntity instanceof StepManifoldSolidBrep) {
                StepManifoldSolidBrep brep = (StepManifoldSolidBrep) solidEntity;
                shellIds.remove(brep.outer().id());
            } else if (solidEntity instanceof StepFacettedBrep) {
                StepFacettedBrep brep = (StepFacettedBrep) solidEntity;
                shellIds.remove(brep.outer().id());
            } else if (solidEntity instanceof StepNonManifoldSolidBrep) {
                StepNonManifoldSolidBrep brep = (StepNonManifoldSolidBrep) solidEntity;
                shellIds.remove(brep.outer().id());
            } else if (solidEntity instanceof StepAdvancedBrep) {
                StepAdvancedBrep brep = (StepAdvancedBrep) solidEntity;
                shellIds.remove(brep.outer().id());
                for (StepEntity voidShell : brep.voids()) {
                    shellIds.remove(voidShell.id());
                }
            } else if (solidEntity instanceof StepBrepWithVoids) {
                StepBrepWithVoids brep = (StepBrepWithVoids) solidEntity;
                shellIds.remove(brep.outer().id());
                for (StepEntity voidShell : brep.voids()) {
                    shellIds.remove(voidShell.id());
                }
            }
        }
        GeometryCollection shellGeometry = buildGeometryForShells(shellIds, resolved, builder, metadata, Map.of());
        GeometryCollection solidGeometry = buildGeometryForSolids(solidIds, resolved, builder, metadata, Map.of());
        GeometryCollection edgeGeometry = new GeometryCollection(List.copyOf(standaloneEdges.values()), List.of(), List.of());
        return mergeGeometry(mergeGeometry(shellGeometry, solidGeometry), edgeGeometry);
    }

    public static GeometryCollection buildGeometryForShells(
            Set<Integer> shellIds,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Map<Integer, StepMetadataExtractor.DisplayMetadata> inheritedShellMetadata
    ) {
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PreviewGeometryCollector.class);
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
            List<StepFaceEntity> shellFaces = PreviewFaceBuilder.shellFaces(shellEntity);
            log.debug("stage={} shellId={}, shellFaceCount={}", "geometry_shell_start", shellId, shellFaces.size());
            for (StepFaceEntity stepFace : shellFaces) {
                PreviewFaceResult previewFace = StepPreviewJsonExporter.buildPreviewFaceResult(
                        stepFace,
                        builder,
                        StepPreviewJsonExporter.mergeMetadata(inheritedShellMetadata.get(shellId), metadata.forItem(stepFace.id()))
                );
                processedFaces++;
                if (previewFace.face() == null) {
                    unsupportedFaces.add(previewFace.unsupportedFace());
                    if (unsupportedFaces.size() <= 10 || unsupportedFaces.size() % 25 == 0) {
                        log.debug("stage={} faceId={}, processedFaces={}, unsupportedFaces={}, reason={}", "geometry_face_unsupported",
                                stepFace.id(), processedFaces, unsupportedFaces.size(), (previewFace.unsupportedFace() == null ? "null" : previewFace.unsupportedFace().reason()));
                    }
                    continue;
                }
                faces.add(previewFace.face());
                if (processedFaces % 25 == 0) {
                    log.debug("stage={} processedFaces={}, supportedFaces={}, unsupportedFaces={}, uniqueEdges={}", "geometry_face_progress",
                            processedFaces, faces.size(), unsupportedFaces.size(), uniqueEdgeIds.size());
                }
                for (StepFaceBound bound : stepFace.bounds()) {
                    if (bound.loop() instanceof StepEdgeLoop) {
                        StepEdgeLoop edgeLoop = (StepEdgeLoop) bound.loop();
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
            edges.add(StepPreviewJsonExporter.buildEdgePayload(edgeId, resolved, builder, metadata));
            processedEdges++;
            if (processedEdges % 100 == 0) {
                log.debug("stage={} processedEdges={}, totalUniqueEdges={}", "geometry_edge_progress",
                        processedEdges, uniqueEdgeIds.size());
            }
        }
        log.debug("stage={} shellCount={}, processedFaces={}, supportedFaces={}, unsupportedFaces={}, edges={}", "geometry_shells_done",
                shellIds.size(), processedFaces, faces.size(), unsupportedFaces.size(), edges.size());
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }

    public static GeometryCollection buildGeometryForSolids(
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
            StepMetadataExtractor.DisplayMetadata itemMetadata = StepPreviewJsonExporter.mergeMetadata(
                    inheritedSolidMetadata.get(solidId),
                    metadata.forItem(solidId)
            );
            try {
                com.minicad.topology.Solid solid = builder.buildSolid(solidId);
                String baseName = entity == null ? null : entity.name();
                int faceIndex = 0;
                for (Face face : solid.outerShell().faces()) {
                    faces.add(StepPreviewJsonExporter.facePayloadFromTopologyFace(
                            solidId * 1000 + faceIndex++,
                            face,
                            baseName,
                            itemMetadata
                    ));
                    PreviewFaceBuilder.collectTopologyEdges(face, uniqueEdges);
                }
                for (var voidShell : solid.voidShells()) {
                    for (Face face : voidShell.faces()) {
                        faces.add(StepPreviewJsonExporter.facePayloadFromTopologyFace(
                                solidId * 1000 + faceIndex++,
                                face,
                                baseName,
                                itemMetadata
                        ));
                        PreviewFaceBuilder.collectTopologyEdges(face, uniqueEdges);
                    }
                }
            } catch (com.minicad.common.UnsupportedGeometryException | com.minicad.common.StepResolutionException | com.minicad.common.TopologyException ex) {
                unsupportedFaces.add(new UnsupportedFacePayload(
                        solidId,
                        entity == null ? null : entity.name(),
                        entity == null ? "SOLID" : StepPreviewJsonExporter.surfaceTypeName(entity),
                        ex.getMessage()
                ));
            }
        }

        int edgeIndex = 0;
        for (Edge edge : uniqueEdges) {
            edges.add(PreviewFaceBuilder.buildTopologyEdgePayload(-(edgeIndex + 1), edge));
            edgeIndex++;
        }
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }

    public static GeometryCollection mergeGeometry(GeometryCollection left, GeometryCollection right) {
        List<EdgePayload> edges = new ArrayList<>(left.edges());
        edges.addAll(right.edges());
        List<FacePayload> faces = new ArrayList<>(left.faces());
        faces.addAll(right.faces());
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>(left.unsupportedFaces());
        unsupportedFaces.addAll(right.unsupportedFaces());
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }

    public static void collectShellLikeIds(StepEntity item, Set<Integer> shellIds) {
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
        if (PreviewFaceBuilder.isShellLikeEntity(item)) {
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

    public static void collectStandaloneEdges(
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        if (item instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) item;
            collectStandaloneEdges(styledItem.item(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) item;
            collectStandaloneEdges(styledItem.item(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) item;
            edges.putIfAbsent(polyline.id(), PreviewFaceBuilder.toPolylineEdgePayload(polyline));
            return;
        }
        if (item instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
            for (StepEntity element : curveSet.elements()) {
                collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            for (StepEntity element : geometricSet.elements()) {
                collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) item;
            for (StepEntity boundary : wireframeModel.boundaries()) {
                collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) item;
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet connectedEdgeSet = (StepConnectedEdgeSet) item;
            for (StepEntity edge : connectedEdgeSet.edges()) {
                collectStandaloneEdges(edge, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepEdgeCurve) {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) item;
            edges.putIfAbsent(edgeCurve.id(), StepPreviewJsonExporter.buildEdgePayload(edgeCurve.id(), resolved, builder, metadata));
            return;
        }
        if (item instanceof StepFilletEdge) {
            StepFilletEdge filletEdge = (StepFilletEdge) item;
            edges.putIfAbsent(filletEdge.id(), StepPreviewJsonExporter.buildEdgePayload(filletEdge.id(), resolved, builder, metadata));
            return;
        }
        if (item instanceof StepChamferEdge) {
            StepChamferEdge chamferEdge = (StepChamferEdge) item;
            edges.putIfAbsent(chamferEdge.id(), StepPreviewJsonExporter.buildEdgePayload(chamferEdge.id(), resolved, builder, metadata));
            return;
        }
        if (item instanceof StepPath) {
            StepPath path = (StepPath) item;
            for (StepOrientedEdge orientedEdge : path.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), StepPreviewJsonExporter.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepOpenPath) {
            StepOpenPath path = (StepOpenPath) item;
            for (StepOrientedEdge orientedEdge : path.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), StepPreviewJsonExporter.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) item;
            for (StepOrientedEdge orientedEdge : subpath.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), StepPreviewJsonExporter.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) item;
            for (StepOrientedEdge orientedEdge : orientedPath.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), StepPreviewJsonExporter.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) item;
            for (StepEntity loop : wireShell.loops()) {
                collectStandaloneEdges(loop, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepEdgeWire) {
            StepEdgeWire edgeWire = (StepEdgeWire) item;
            for (StepEntity edge : edgeWire.edges()) {
                collectStandaloneEdges(edge, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepGeometricSurfaceSet) {
            StepGeometricSurfaceSet surfaceSet = (StepGeometricSurfaceSet) item;
            for (StepEntity element : surfaceSet.elements()) {
                collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepEdgeLoop) {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) item;
            for (StepOrientedEdge orientedEdge : edgeLoop.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), StepPreviewJsonExporter.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) item;
            edges.putIfAbsent(polyLoop.id(), PreviewFaceBuilder.toPolyLoopEdgePayload(polyLoop));
            return;
        }
        if (item instanceof StepVertexShell || item instanceof StepVertexLoop) {
            return;
        }
        if (item instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) item;
            collectStandaloneEdges(occurrence.item(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) item;
            for (StepEntity boundary : fillArea.boundaries()) {
                collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) item;
            collectStandaloneEdges(fillAreaOccurrence.item(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) item;
            collectMappedAnnotationEdges(
                    annotationSymbol.id(),
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(),
                    null,
                    null,
                    edges,
                    resolved,
                    builder
            );
            return;
        }
        if (item instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) item;
            if (!collectMappedAnnotationCarrierEdges(
                    symbolOccurrence.id(),
                    "ANNOTATION_SYMBOL_OCCURRENCE",
                    symbolOccurrence.id(),
                    symbolOccurrence.item(),
                    edges,
                    resolved,
                    builder
            )) {
                collectStandaloneEdges(symbolOccurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) item;
            if (!collectMappedAnnotationCarrierEdges(
                    subfigureOccurrence.id(),
                    "ANNOTATION_SUBFIGURE_OCCURRENCE",
                    subfigureOccurrence.id(),
                    subfigureOccurrence.item(),
                    edges,
                    resolved,
                    builder
            )) {
                collectStandaloneEdges(subfigureOccurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) item;
            collectMappedAnnotationEdges(
                    annotationText.id(),
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(),
                    null,
                    null,
                    edges,
                    resolved,
                    builder
            );
            return;
        }
        if (item instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) item;
            collectMappedAnnotationEdges(
                    annotationTextCharacter.id(),
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(),
                    null,
                    null,
                    edges,
                    resolved,
                    builder
            );
            return;
        }
        if (item instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) item;
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(dimensionCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) item;
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(leaderCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) item;
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(projectionCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) item;
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else if (collectMappedAnnotationCarrierEdges(
                    annotationOccurrence.id(),
                    "DRAUGHTING_ANNOTATION_OCCURRENCE",
                    annotationOccurrence.id(),
                    annotationOccurrence.item(),
                    edges,
                    resolved,
                    builder
            )) {
                return;
            } else {
                collectStandaloneEdges(annotationOccurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) item;
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(terminatorSymbol.annotatedCurve(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) item;
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(occurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepFilletEdge) {
            StepFilletEdge filletEdge = (StepFilletEdge) item;
            collectStandaloneEdges(filletEdge.originalEdge(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepChamferEdge) {
            StepChamferEdge chamferEdge = (StepChamferEdge) item;
            collectStandaloneEdges(chamferEdge.originalEdge(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepSubedge) {
            StepSubedge subedge = (StepSubedge) item;
            collectStandaloneEdges(subedge.parentEdge(), edges, resolved, builder, metadata);
            return;
        }
        if (PreviewFaceBuilder.isSampledCurveSource(item)) {
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            }
        }
    }

    // ─── Representation geometry ─────────────────────────────────────────

    public static GeometryCollection buildMappedRepresentationGeometry(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        GeometryCollection geometry = new GeometryCollection(List.of(), List.of(), List.of());
        for (StepRepresentation candidate : StepPreviewJsonExporter.linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                if (item instanceof StepMappedItem) {
                    StepMappedItem mappedItem = (StepMappedItem) item;
                    geometry = mergeGeometry(
                            geometry,
                            expandMappedItemGeometry(mappedItem, resolved, builder, metadata, visitingRepresentations)
                    );
                }
            }
        }
        return geometry;
    }

    public static GeometryCollection buildRelatedRepresentationGeometry(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        GeometryCollection geometry = new GeometryCollection(List.of(), List.of(), List.of());
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepRepresentationRelationshipWithTransformation)) {
                continue;
            }
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) entity;
            if (!relationship.rep1().shapeRepresentation()
                    || !relationship.rep2().shapeRepresentation()
                    || relationship.rep2().id() != representation.id()) {
                continue;
            }
            double[] matrix = StepAssemblyGraphBuilder.matrixFor(relationship.transformationOperator());
            RepresentationBuildResult source = StepPreviewJsonExporter.buildRepresentationPayload(
                    relationship.rep1(),
                    relationship.rep1().name(),
                    resolved,
                    builder,
                    metadata,
                    visitingRepresentations
            );
            StepMetadataExtractor.DisplayMetadata relationshipMetadata = metadata.forItem(relationship.id());
            List<EdgePayload> edges = source.payload().edges().stream()
                    .map(edge -> StepPreviewJsonExporter.transformMappedEdge(edge, relationship.id(), matrix))
                    .collect(Collectors.toList());
            List<FacePayload> faces = source.payload().faces().stream()
                    .map(face -> StepPreviewJsonExporter.transformMappedFace(face, relationship.id(), matrix, relationshipMetadata))
                    .collect(Collectors.toList());
            geometry = mergeGeometry(geometry, new GeometryCollection(edges, faces, source.unsupportedFaces()));
        }
        return geometry;
    }

    public static GeometryCollection expandMappedItemGeometry(
            StepMappedItem mappedItem,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        double[] matrix = StepPreviewJsonExporter.mappedItemMatrix(mappedItem, builder);
        if (matrix == null) {
            return new GeometryCollection(List.of(), List.of(), List.of());
        }
        StepRepresentationMap mappingSource = mappedItem.mappingSource();
        RepresentationBuildResult source = StepPreviewJsonExporter.buildRepresentationPayload(
                mappingSource.mappedRepresentation(),
                mappingSource.mappedRepresentation().name(),
                resolved,
                builder,
                metadata,
                visitingRepresentations
        );
        StepMetadataExtractor.DisplayMetadata itemMetadata = metadata.forItem(mappedItem.id());
        List<EdgePayload> edges = source.payload().edges().stream()
                .map(edge -> StepPreviewJsonExporter.transformMappedEdge(edge, mappedItem.id(), matrix))
                .collect(Collectors.toList());
        List<FacePayload> faces = source.payload().faces().stream()
                .map(face -> StepPreviewJsonExporter.transformMappedFace(face, mappedItem.id(), matrix, itemMetadata))
                .collect(Collectors.toList());
        return new GeometryCollection(edges, faces, source.unsupportedFaces());
    }

    public static Set<Integer> collectRepresentationShells(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        Set<Integer> shellIds = new TreeSet<>();
        for (StepRepresentation candidate : StepPreviewJsonExporter.linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = PreviewFaceBuilder.unwrapStyledItem(item);
                if (!isRepresentationSolidItem(unwrapped)) {
                    collectShellLikeIds(item, shellIds);
                }
            }
        }
        return shellIds;
    }

    public static Set<Integer> collectRepresentationSolids(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        Set<Integer> solidIds = new TreeSet<>();
        for (StepRepresentation candidate : StepPreviewJsonExporter.linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = PreviewFaceBuilder.unwrapStyledItem(item);
                if (isRepresentationSolidItem(unwrapped)) {
                    solidIds.add(unwrapped.id());
                }
            }
        }
        return solidIds;
    }

    // ─── Helper methods ──────────────────────────────────────────────────

    public static boolean isStandaloneEdgeSource(StepEntity item) {
        return item instanceof StepPolyline
                || item instanceof StepGeometricCurveSet
                || item instanceof StepGeometricSet
                || item instanceof StepShellBasedWireframeModel
                || item instanceof StepEdgeBasedWireframeModel
                || item instanceof StepConnectedEdgeSet
                || item instanceof StepEdgeWire
                || item instanceof StepPath
                || item instanceof StepOpenPath
                || item instanceof StepSubpath
                || item instanceof StepOrientedPath
                || item instanceof StepWireShell
                || item instanceof StepAnnotationCurveOccurrence
                || item instanceof StepAnnotationFillArea
                || item instanceof StepAnnotationFillAreaOccurrence
                || item instanceof StepAnnotationSymbol
                || item instanceof StepAnnotationSymbolOccurrence
                || item instanceof StepAnnotationSubfigureOccurrence
                || item instanceof StepFilletEdge
                || item instanceof StepChamferEdge
                || item instanceof StepSubedge
                || item instanceof StepAnnotationText
                || item instanceof StepAnnotationTextCharacter
                || item instanceof StepDimensionCurve
                || item instanceof StepLeaderCurve
                || item instanceof StepProjectionCurve
                || item instanceof StepDraughtingAnnotationOccurrence
                || item instanceof StepTerminatorSymbol
                || item instanceof StepGeometricSurfaceSet;
    }

    public static boolean isRepresentationSolidItem(StepEntity entity) {
        return entity instanceof StepManifoldSolidBrep
                || entity instanceof StepFacettedBrep
                || entity instanceof StepNonManifoldSolidBrep
                || entity instanceof StepAdvancedBrep
                || entity instanceof StepBrepWithVoids
                || entity instanceof StepSweptAreaSolid
                || entity instanceof StepSolidReplica
                || entity instanceof StepCsgSolid
                || entity instanceof StepCsgPrimitive
                || entity instanceof StepBooleanClippingResult
                || entity instanceof StepBooleanResult
                || entity instanceof StepTessellatedFaceSet
                || entity instanceof StepTessellatedFace
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
                || entity instanceof StepMappedItem
                || entity instanceof StepSolidModel
                || entity instanceof StepSurfacePatch;
    }

    // ─── Mapped annotation edge collection ───────────────────────────────

    private static void collectMappedAnnotationEdges(
            int mappedOwnerId,
            StepRepresentation representation,
            StepEntity mappedOrigin,
            StepEntity mappingTarget,
            String sourceType,
            Integer sourceStepId,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        double[] matrix = StepPreviewJsonExporter.matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
        if (matrix == null) {
            return;
        }
        RepresentationBuildResult source = StepPreviewJsonExporter.buildRepresentationPayload(
                representation,
                representation.name(),
                resolved,
                builder,
                StepMetadataExtractor.fromResolved(resolved),
                new LinkedHashSet<>()
        );
        for (EdgePayload edge : source.payload().edges()) {
            EdgePayload transformed = StepPreviewJsonExporter.transformMappedEdge(edge, mappedOwnerId, matrix, sourceType, sourceStepId);
            edges.putIfAbsent(transformed.stepId(), transformed);
        }
    }

    private static boolean collectMappedAnnotationCarrierEdges(
            int mappedOwnerId,
            String sourceType,
            Integer sourceStepId,
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        if (item instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) item;
            collectMappedAnnotationEdges(
                    mappedOwnerId,
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(),
                    sourceType,
                    sourceStepId,
                    edges,
                    resolved,
                    builder
            );
            return true;
        }
        if (item instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) item;
            collectMappedAnnotationEdges(
                    mappedOwnerId,
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(),
                    sourceType,
                    sourceStepId,
                    edges,
                    resolved,
                    builder
            );
            return true;
        }
        if (item instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) item;
            collectMappedAnnotationEdges(
                    mappedOwnerId,
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(),
                    sourceType,
                    sourceStepId,
                    edges,
                    resolved,
                    builder
            );
            return true;
        }
        if (item instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) item;
            return collectMappedAnnotationCarrierEdges(
                    mappedOwnerId,
                    sourceType,
                    sourceStepId,
                    symbolOccurrence.item(),
                    edges,
                    resolved,
                    builder
            );
        }
        if (item instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) item;
            return collectMappedAnnotationCarrierEdges(
                    mappedOwnerId,
                    sourceType,
                    sourceStepId,
                    subfigureOccurrence.item(),
                    edges,
                    resolved,
                    builder
            );
        }
        return false;
    }
}