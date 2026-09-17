package com.minicad.preview.builder;

import com.minicad.export.json.StepValidationHelper;
import com.minicad.builder.StepAssemblyGraphBuilder;
import com.minicad.export.json.StepPreviewJsonExporter;
import com.minicad.geometry.*;
import com.minicad.helper.ShellHelper;
import com.minicad.helper.StepMetadataExtractor;
import com.minicad.preview.payload.EdgePayload;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.GeometryCollection;
import com.minicad.preview.payload.RepresentationBuildResult;
import com.minicad.step.model.*;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.*;
import com.minicad.step.model.StepChamferEdge;
import com.minicad.step.model.StepFilletEdge;
import com.minicad.step.model.*;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.*;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.topology.FaceBound;
import com.minicad.topology.OrientedEdge;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import com.minicad.export.json.StepEdgePayloadBuilder;
import com.minicad.export.json.StepLegacyGeometryBuilder;
import com.minicad.export.json.StepMappedAnnotationEdgeCollector;
import com.minicad.export.json.StepMappedItemTransformer;
import com.minicad.export.json.StepRepresentationPayloadBuilder;

/**
 * Preview-side geometry collection helpers: shell-like id discovery, the
 * standalone-edge dispatch table and the mapped-representation expansion.
 *
 * <p>The legacy-geometry orchestration that used to sit at the top of this file
 * ({@code buildLegacyGeometry} with its shell/solid/merge helpers) now lives only
 * in {@code StepLegacyGeometryBuilder}, which is where its one live caller was.
 * The copy kept here was reachable through a {@code PreviewFaceBuilder} facade
 * that nothing called, and it had already drifted -- it never received the
 * {@code StepFacetedBrepAndBrepWithVoids} bucket entry -- so the copy was
 * deleted rather than re-synchronised.
 */
public final class PreviewGeometryCollector {

    private PreviewGeometryCollector() {}

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

    // collectStandaloneEdges dispatch table (first-match-return, mirrors the original sequential ifs).
    private record PreviewEdgeCollectRule(Class<? extends StepEntity> type, PreviewEdgeCollectHandler handler) {}

    private interface PreviewEdgeCollectHandler {
        void collect(StepEntity item, Map<Integer, EdgePayload> edges,
                Map<Integer, StepEntity> resolved, StepCadBuilder builder,
                StepMetadataExtractor metadata);
    }

    private static PreviewEdgeCollectRule previewEdgeCollectRule(
            Class<? extends StepEntity> type, PreviewEdgeCollectHandler handler) {
        return new PreviewEdgeCollectRule(type, handler);
    }

    private static final List<PreviewEdgeCollectRule> PREVIEW_EDGE_COLLECT_RULES = List.of(
        previewEdgeCollectRule(StepStyledItem.class, (item, edges, resolved, builder, metadata) -> {
            StepStyledItem styledItem = (StepStyledItem) item;
            collectStandaloneEdges(styledItem.item(), edges, resolved, builder, metadata);
            return;
        }),
        previewEdgeCollectRule(StepOverRidingStyledItem.class, (item, edges, resolved, builder, metadata) -> {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) item;
            collectStandaloneEdges(styledItem.item(), edges, resolved, builder, metadata);
            return;
        }),
        previewEdgeCollectRule(StepPolyline.class, (item, edges, resolved, builder, metadata) -> {
            StepPolyline polyline = (StepPolyline) item;
            edges.putIfAbsent(polyline.id(), PreviewFaceBuilder.toPolylineEdgePayload(polyline));
            return;
        }),
        previewEdgeCollectRule(StepGeometricCurveSet.class, (item, edges, resolved, builder, metadata) -> {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
            for (StepEntity element : curveSet.elements()) {
            collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepGeometricSet.class, (item, edges, resolved, builder, metadata) -> {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            for (StepEntity element : geometricSet.elements()) {
            collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepShellBasedWireframeModel.class, (item, edges, resolved, builder, metadata) -> {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) item;
            for (StepEntity boundary : wireframeModel.boundaries()) {
            collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepEdgeBasedWireframeModel.class, (item, edges, resolved, builder, metadata) -> {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) item;
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
            collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepConnectedEdgeSet.class, (item, edges, resolved, builder, metadata) -> {
            StepConnectedEdgeSet connectedEdgeSet = (StepConnectedEdgeSet) item;
            for (StepEntity edge : connectedEdgeSet.edges()) {
            collectStandaloneEdges(edge, edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepEdgeCurve.class, (item, edges, resolved, builder, metadata) -> {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) item;
            edges.putIfAbsent(edgeCurve.id(), StepEdgePayloadBuilder.buildEdgePayload(edgeCurve.id(), resolved, builder, metadata));
            return;
        }),
        previewEdgeCollectRule(StepFilletEdge.class, (item, edges, resolved, builder, metadata) -> {
            StepFilletEdge filletEdge = (StepFilletEdge) item;
            edges.putIfAbsent(filletEdge.id(), StepEdgePayloadBuilder.buildEdgePayload(filletEdge.id(), resolved, builder, metadata));
            return;
        }),
        previewEdgeCollectRule(StepChamferEdge.class, (item, edges, resolved, builder, metadata) -> {
            StepChamferEdge chamferEdge = (StepChamferEdge) item;
            edges.putIfAbsent(chamferEdge.id(), StepEdgePayloadBuilder.buildEdgePayload(chamferEdge.id(), resolved, builder, metadata));
            return;
        }),
        previewEdgeCollectRule(StepPath.class, (item, edges, resolved, builder, metadata) -> {
            StepPath path = (StepPath) item;
            for (StepOrientedEdge orientedEdge : path.edges()) {
            edges.putIfAbsent(orientedEdge.edgeElement().id(), StepEdgePayloadBuilder.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }),
        previewEdgeCollectRule(StepOpenPath.class, (item, edges, resolved, builder, metadata) -> {
            StepOpenPath path = (StepOpenPath) item;
            for (StepOrientedEdge orientedEdge : path.edges()) {
            edges.putIfAbsent(orientedEdge.edgeElement().id(), StepEdgePayloadBuilder.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }),
        previewEdgeCollectRule(StepSubpath.class, (item, edges, resolved, builder, metadata) -> {
            StepSubpath subpath = (StepSubpath) item;
            for (StepOrientedEdge orientedEdge : subpath.edges()) {
            edges.putIfAbsent(orientedEdge.edgeElement().id(), StepEdgePayloadBuilder.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }),
        previewEdgeCollectRule(StepOrientedPath.class, (item, edges, resolved, builder, metadata) -> {
            StepOrientedPath orientedPath = (StepOrientedPath) item;
            for (StepOrientedEdge orientedEdge : orientedPath.edges()) {
            edges.putIfAbsent(orientedEdge.edgeElement().id(), StepEdgePayloadBuilder.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }),
        previewEdgeCollectRule(StepWireShell.class, (item, edges, resolved, builder, metadata) -> {
            StepWireShell wireShell = (StepWireShell) item;
            for (StepEntity loop : wireShell.loops()) {
            collectStandaloneEdges(loop, edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepEdgeWire.class, (item, edges, resolved, builder, metadata) -> {
            StepEdgeWire edgeWire = (StepEdgeWire) item;
            for (StepEntity edge : edgeWire.edges()) {
            collectStandaloneEdges(edge, edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepGeometricSurfaceSet.class, (item, edges, resolved, builder, metadata) -> {
            StepGeometricSurfaceSet surfaceSet = (StepGeometricSurfaceSet) item;
            for (StepEntity element : surfaceSet.elements()) {
            collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepEdgeLoop.class, (item, edges, resolved, builder, metadata) -> {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) item;
            for (StepOrientedEdge orientedEdge : edgeLoop.edges()) {
            edges.putIfAbsent(orientedEdge.edgeElement().id(), StepEdgePayloadBuilder.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }),
        previewEdgeCollectRule(StepPolyLoop.class, (item, edges, resolved, builder, metadata) -> {
            StepPolyLoop polyLoop = (StepPolyLoop) item;
            edges.putIfAbsent(polyLoop.id(), PreviewFaceBuilder.toPolyLoopEdgePayload(polyLoop));
            return;
        }),
        previewEdgeCollectRule(StepVertexShell.class, (item, edges, resolved, builder, metadata) -> {
            return;
        }),
        previewEdgeCollectRule(StepVertexLoop.class, (item, edges, resolved, builder, metadata) -> {
            return;
        }),
        previewEdgeCollectRule(StepAnnotationCurveOccurrence.class, (item, edges, resolved, builder, metadata) -> {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) item;
            collectStandaloneEdges(occurrence.item(), edges, resolved, builder, metadata);
            return;
        }),
        previewEdgeCollectRule(StepAnnotationFillArea.class, (item, edges, resolved, builder, metadata) -> {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) item;
            for (StepEntity boundary : fillArea.boundaries()) {
            collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepAnnotationFillAreaOccurrence.class, (item, edges, resolved, builder, metadata) -> {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) item;
            collectStandaloneEdges(fillAreaOccurrence.item(), edges, resolved, builder, metadata);
            return;
        }),
        previewEdgeCollectRule(StepAnnotationSymbol.class, (item, edges, resolved, builder, metadata) -> {
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
        }),
        previewEdgeCollectRule(StepAnnotationSymbolOccurrence.class, (item, edges, resolved, builder, metadata) -> {
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
        }),
        previewEdgeCollectRule(StepAnnotationSubfigureOccurrence.class, (item, edges, resolved, builder, metadata) -> {
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
        }),
        previewEdgeCollectRule(StepAnnotationText.class, (item, edges, resolved, builder, metadata) -> {
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
        }),
        previewEdgeCollectRule(StepAnnotationTextCharacter.class, (item, edges, resolved, builder, metadata) -> {
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
        }),
        previewEdgeCollectRule(StepDimensionCurve.class, (item, edges, resolved, builder, metadata) -> {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) item;
            EdgePayload sampled = StepEdgePayloadBuilder.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
            edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
            collectStandaloneEdges(dimensionCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepLeaderCurve.class, (item, edges, resolved, builder, metadata) -> {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) item;
            EdgePayload sampled = StepEdgePayloadBuilder.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
            edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
            collectStandaloneEdges(leaderCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepProjectionCurve.class, (item, edges, resolved, builder, metadata) -> {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) item;
            EdgePayload sampled = StepEdgePayloadBuilder.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
            edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
            collectStandaloneEdges(projectionCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepDraughtingAnnotationOccurrence.class, (item, edges, resolved, builder, metadata) -> {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) item;
            EdgePayload sampled = StepEdgePayloadBuilder.sampledCurveEdgePayload(item, builder);
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
        }),
        previewEdgeCollectRule(StepTerminatorSymbol.class, (item, edges, resolved, builder, metadata) -> {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) item;
            EdgePayload sampled = StepEdgePayloadBuilder.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
            edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
            collectStandaloneEdges(terminatorSymbol.annotatedCurve(), edges, resolved, builder, metadata);
            }
            return;
        }),
        previewEdgeCollectRule(StepSubedge.class, (item, edges, resolved, builder, metadata) -> {
            StepSubedge subedge = (StepSubedge) item;
            collectStandaloneEdges(subedge.parentEdge(), edges, resolved, builder, metadata);
            return;
        })
    );

    public static void collectStandaloneEdges(
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        for (PreviewEdgeCollectRule rule : PREVIEW_EDGE_COLLECT_RULES) {
            if (rule.type().isInstance(item)) {
                rule.handler().collect(item, edges, resolved, builder, metadata);
                return;
            }
        }

        if (StepValidationHelper.isSampledCurveSource(item)) {
            EdgePayload sampled = StepEdgePayloadBuilder.sampledCurveEdgePayload(item, builder);
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
        for (StepRepresentation candidate : StepRepresentationPayloadBuilder.linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                if (item instanceof StepMappedItem) {
                    StepMappedItem mappedItem = (StepMappedItem) item;
                    geometry = StepLegacyGeometryBuilder.mergeGeometry(
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
            RepresentationBuildResult source = StepRepresentationPayloadBuilder.buildRepresentationPayload(
                    relationship.rep1(),
                    relationship.rep1().name(),
                    resolved,
                    builder,
                    metadata,
                    visitingRepresentations
            );
            StepMetadataExtractor.DisplayMetadata relationshipMetadata = metadata.forItem(relationship.id());
            List<EdgePayload> edges = source.payload().edges().stream()
                    .map(edge -> StepMappedItemTransformer.transformMappedEdge(edge, relationship.id(), matrix))
                    .collect(Collectors.toList());
            List<FacePayload> faces = source.payload().faces().stream()
                    .map(face -> StepMappedItemTransformer.transformMappedFace(face, relationship.id(), matrix, relationshipMetadata))
                    .collect(Collectors.toList());
            geometry = StepLegacyGeometryBuilder.mergeGeometry(geometry, new GeometryCollection(edges, faces, source.unsupportedFaces()));
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
        double[] matrix = StepMappedItemTransformer.mappedItemMatrix(mappedItem, builder);
        if (matrix == null) {
            return new GeometryCollection(List.of(), List.of(), List.of());
        }
        StepRepresentationMap mappingSource = mappedItem.mappingSource();
        RepresentationBuildResult source = StepRepresentationPayloadBuilder.buildRepresentationPayload(
                mappingSource.mappedRepresentation(),
                mappingSource.mappedRepresentation().name(),
                resolved,
                builder,
                metadata,
                visitingRepresentations
        );
        StepMetadataExtractor.DisplayMetadata itemMetadata = metadata.forItem(mappedItem.id());
        List<EdgePayload> edges = source.payload().edges().stream()
                .map(edge -> StepMappedItemTransformer.transformMappedEdge(edge, mappedItem.id(), matrix))
                .collect(Collectors.toList());
        List<FacePayload> faces = source.payload().faces().stream()
                .map(face -> StepMappedItemTransformer.transformMappedFace(face, mappedItem.id(), matrix, itemMetadata))
                .collect(Collectors.toList());
        return new GeometryCollection(edges, faces, source.unsupportedFaces());
    }

    public static Set<Integer> collectRepresentationShells(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        Set<Integer> shellIds = new TreeSet<>();
        for (StepRepresentation candidate : StepRepresentationPayloadBuilder.linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = PreviewFaceBuilder.unwrapStyledItem(item);
                if (!StepValidationHelper.isRepresentationSolidItem(unwrapped)) {
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
        for (StepRepresentation candidate : StepRepresentationPayloadBuilder.linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = PreviewFaceBuilder.unwrapStyledItem(item);
                if (StepValidationHelper.isRepresentationSolidItem(unwrapped)) {
                    solidIds.add(unwrapped.id());
                }
            }
        }
        return solidIds;
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
        StepMappedAnnotationEdgeCollector.collect(mappedOwnerId, representation, mappedOrigin, mappingTarget,
                sourceType, sourceStepId, edges, resolved, builder);
    }

    // Shared by the three symbol/text carrier rules: unwrap a mappingSource
    // triple and hand it to collectMappedAnnotationEdges, reporting a hit.
    private static boolean collectMappedAnnotationEdgesFrom(
            int mappedOwnerId, String sourceType, Integer sourceStepId,
            StepRepresentation mappedRepresentation, StepEntity mappedOrigin, StepEntity mappingTarget,
            Map<Integer, EdgePayload> edges, Map<Integer, StepEntity> resolved, StepCadBuilder builder) {
        collectMappedAnnotationEdges(mappedOwnerId, mappedRepresentation, mappedOrigin, mappingTarget,
                sourceType, sourceStepId, edges, resolved, builder);
        return true;
    }

    // collectMappedAnnotationCarrierEdges dispatch table (first match wins,
    // mirrors the original sequential ifs). Each rule matches an annotation
    // carrier type and delegates: the three symbol/text carriers call
    // collectMappedAnnotationEdges with their mappingSource pair + mappingTarget
    // and report true; the two occurrence carriers recurse into this entry
    // method on their wrapped item and propagate its result. An item no rule
    // matches returns false, as the old trailing statement did. All 5 types are
    // final direct StepEntity implementations (no subtype relation today), so
    // the order is behaviour neutral but frozen by
    // mapped-annotation-carrier-dispatch-order.txt. This is a separate table
    // from the adjacent PREVIEW_EDGE_COLLECT_RULES on purpose -- different
    // return contract (boolean vs void) and different context (mapped-owner /
    // source metadata vs plain edges).
    private record MappedAnnotationCarrierRule(
            Class<? extends StepEntity> type, MappedAnnotationCarrierHandler handler) {}

    private interface MappedAnnotationCarrierHandler {
        boolean collect(int mappedOwnerId, String sourceType, Integer sourceStepId, StepEntity item,
                Map<Integer, EdgePayload> edges, Map<Integer, StepEntity> resolved, StepCadBuilder builder);
    }

    private static MappedAnnotationCarrierRule mappedAnnotationCarrierRule(
            Class<? extends StepEntity> type, MappedAnnotationCarrierHandler handler) {
        return new MappedAnnotationCarrierRule(type, handler);
    }

    private static final List<MappedAnnotationCarrierRule> MAPPED_ANNOTATION_CARRIER_RULES = List.of(
        mappedAnnotationCarrierRule(StepAnnotationSymbol.class, (mappedOwnerId, sourceType, sourceStepId, item, edges, resolved, builder) -> {
            StepAnnotationSymbol symbol = (StepAnnotationSymbol) item;
            return collectMappedAnnotationEdgesFrom(mappedOwnerId, sourceType, sourceStepId,
                    symbol.mappingSource().mappedRepresentation(), symbol.mappingSource().mappedOrigin(),
                    symbol.mappingTarget(), edges, resolved, builder);
        }),
        mappedAnnotationCarrierRule(StepAnnotationText.class, (mappedOwnerId, sourceType, sourceStepId, item, edges, resolved, builder) -> {
            StepAnnotationText text = (StepAnnotationText) item;
            return collectMappedAnnotationEdgesFrom(mappedOwnerId, sourceType, sourceStepId,
                    text.mappingSource().mappedRepresentation(), text.mappingSource().mappedOrigin(),
                    text.mappingTarget(), edges, resolved, builder);
        }),
        mappedAnnotationCarrierRule(StepAnnotationTextCharacter.class, (mappedOwnerId, sourceType, sourceStepId, item, edges, resolved, builder) -> {
            StepAnnotationTextCharacter character = (StepAnnotationTextCharacter) item;
            return collectMappedAnnotationEdgesFrom(mappedOwnerId, sourceType, sourceStepId,
                    character.mappingSource().mappedRepresentation(), character.mappingSource().mappedOrigin(),
                    character.mappingTarget(), edges, resolved, builder);
        }),
        mappedAnnotationCarrierRule(StepAnnotationSymbolOccurrence.class, (mappedOwnerId, sourceType, sourceStepId, item, edges, resolved, builder) -> {
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
        }),
        mappedAnnotationCarrierRule(StepAnnotationSubfigureOccurrence.class, (mappedOwnerId, sourceType, sourceStepId, item, edges, resolved, builder) -> {
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
        })
    );

    private static boolean collectMappedAnnotationCarrierEdges(
            int mappedOwnerId,
            String sourceType,
            Integer sourceStepId,
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        for (MappedAnnotationCarrierRule rule : MAPPED_ANNOTATION_CARRIER_RULES) {
            if (rule.type().isInstance(item)) {
                return rule.handler().collect(
                        mappedOwnerId, sourceType, sourceStepId, item, edges, resolved, builder);
            }
        }
        return false;
    }
}
