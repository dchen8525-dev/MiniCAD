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
 * StepRepresentationPayloadBuilder.
 */

public final class StepRepresentationPayloadBuilder {
    private static final Logger log = LoggerFactory.getLogger(StepRepresentationPayloadBuilder.class);
    private StepRepresentationPayloadBuilder() {}

    // Delegate to StepPlacementTransformer - extracted utility class
    public static double[] matrixForMappedPlacement(
            StepEntity mappedOrigin,
            StepEntity mappingTarget,
            StepCadBuilder builder
    ) {
        return StepPlacementTransformer.matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
    }

    // Delegate to StepPlacementTransformer - extracted utility class
    public static double[] matrixForPlacementEntity(StepEntity placement, StepCadBuilder builder) {
        return StepPlacementTransformer.matrixForPlacementEntity(placement, builder);
    }


    static AssemblyData buildAssemblyData(
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            UnitExtractor.UnitInfo units
    ) {
        // F04: Assembly transforms should stay in source units when units are not normalized
        // When scaleToMeters != 1.0 (units not normalized), pass 1.0 to keep transforms in source units
        // When scaleToMeters == 1.0 (units normalized or SI), pass 1.0 (no scaling needed)
        StepAssemblyGraphBuilder.AssemblyGraph graph = StepAssemblyGraphBuilder.build(resolved, 1.0);
        Map<Integer, RepresentationPayload> representations = new LinkedHashMap<>();
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>();
        for (StepAssemblyGraphBuilder.AssemblyRepresentation assemblyRepresentation : graph.representations()) {
            StepEntity entity = resolved.get(assemblyRepresentation.representationId());
            if (entity instanceof StepRepresentation) { StepRepresentation representation = (StepRepresentation) entity;
                RepresentationBuildResult result = buildRepresentationPayload(
                        representation,
                        assemblyRepresentation.name(),
                        resolved,
                        builder,
                        metadata
                );
                unsupportedFaces.addAll(result.unsupportedFaces());
                representations.put(representation.id(), result.payload());
            }
        }

        if (representations.isEmpty()) {
            for (StepEntity entity : resolved.values()) {
                if (entity instanceof StepRepresentation) { StepRepresentation representation = (StepRepresentation) entity;
                    RepresentationBuildResult result = buildRepresentationPayload(
                            representation,
                            representation.name(),
                            resolved,
                            builder,
                            metadata
                    );
                    unsupportedFaces.addAll(result.unsupportedFaces());
                    representations.putIfAbsent(
                            representation.id(),
                            result.payload()
                    );
                }
            }
        }

        List<InstancePayload> instances = new ArrayList<>();
        for (StepAssemblyGraphBuilder.AssemblyNode node : graph.nodes()) {
            instances.add(new InstancePayload(
                    node.id(),
                    node.parentId(),
                    node.productDefinitionId(),
                    node.occurrenceId(),
                    node.representationIds().isEmpty() ? null : node.representationIds().get(0),
                    node.representationIds(),
                    node.label(),
                    node.description(),
                    node.localMatrix(),
                    node.worldMatrix(),
                    node.depth()
                ));
        }

        List<RepresentationPayload> representationList = List.copyOf(representations.values());
        List<InstancePayload> instanceList = List.copyOf(instances);
        AssemblyMetrics metrics = measureAssembly(representationList, instanceList);
        return new AssemblyData(
                representationList,
                instanceList,
                List.copyOf(unsupportedFaces),
                metrics.summary(),
                metrics.bounds()
        );
    }


    public static RepresentationBuildResult buildRepresentationPayload(
            StepRepresentation representation,
            String displayName,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        return buildRepresentationPayload(representation, displayName, resolved, builder, metadata, new LinkedHashSet<>());
    }


    static GeometryCollection buildMappedRepresentationGeometry(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        GeometryCollection geometry = new GeometryCollection(List.of(), List.of(), List.of());
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
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


    static GeometryCollection buildRelatedRepresentationGeometry(
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
            RepresentationBuildResult source = buildRepresentationPayload(
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
                    .map(face -> StepMappedItemTransformer.transformMappedFace(
                            face, relationship.id(), matrix, relationshipMetadata))
                    .collect(Collectors.toList());
            geometry = StepLegacyGeometryBuilder.mergeGeometry(geometry, new GeometryCollection(edges, faces, source.unsupportedFaces()));
        }
        return geometry;
    }


    static GeometryCollection expandMappedItemGeometry(
            StepMappedItem mappedItem,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        double[] matrix = mappedItemMatrix(mappedItem, builder);
        if (matrix == null) {
            return new GeometryCollection(List.of(), List.of(), List.of());
        }
        StepRepresentationMap mappingSource = mappedItem.mappingSource();
        RepresentationBuildResult source = buildRepresentationPayload(
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
                .map(face -> StepMappedItemTransformer.transformMappedFace(
                        face, mappedItem.id(), matrix, itemMetadata))
                .collect(Collectors.toList());
        return new GeometryCollection(edges, faces, source.unsupportedFaces());
    }


    static Set<Integer> collectRepresentationShells(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        Set<Integer> shellIds = new TreeSet<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = StepLegacyGeometryBuilder.unwrapStyledItem(item);
                if (!StepValidationHelper.isRepresentationSolidItem(unwrapped)) {
                    StepLegacyGeometryBuilder.collectShellLikeIds(item, shellIds);
                }
            }
        }
        return shellIds;
    }


    static Set<Integer> collectRepresentationSolids(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        Set<Integer> solidIds = new TreeSet<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = StepLegacyGeometryBuilder.unwrapStyledItem(item);
                if (StepValidationHelper.isRepresentationSolidItem(unwrapped)) {
                    solidIds.add(unwrapped.id());
                }
            }
        }
        return solidIds;
    }


    static List<EdgePayload> collectRepresentationLooseEdges(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        Map<Integer, EdgePayload> edges = new LinkedHashMap<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEdgePayloadBuilder.collectStandaloneEdges(item, edges, resolved, builder, metadata);
            }
        }
        return List.copyOf(edges.values());
    }


    static Map<Integer, StepMetadataExtractor.DisplayMetadata> collectInheritedShellMetadata(
            StepRepresentation representation,
            StepMetadataExtractor metadata,
            Map<Integer, StepEntity> resolved
    ) {
        Map<Integer, StepMetadataExtractor.DisplayMetadata> metadataByShellId = new LinkedHashMap<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = StepLegacyGeometryBuilder.unwrapStyledItem(item);
                if (StepValidationHelper.isRepresentationSolidItem(unwrapped)) {
                    continue;
                }
                StepMetadataExtractor.DisplayMetadata itemMetadata = metadata.forItem(item.id());
                Set<Integer> itemShellIds = new LinkedHashSet<>();
                StepLegacyGeometryBuilder.collectShellLikeIds(item, itemShellIds);
                for (Integer shellId : itemShellIds) {
                    metadataByShellId.put(shellId, StepMetadataHelper.mergeMetadata(metadataByShellId.get(shellId), itemMetadata));
                }
            }
        }
        return Map.copyOf(metadataByShellId);
    }


    static Map<Integer, StepMetadataExtractor.DisplayMetadata> collectInheritedSolidMetadata(
            StepRepresentation representation,
            StepMetadataExtractor metadata,
            Map<Integer, StepEntity> resolved
    ) {
        Map<Integer, StepMetadataExtractor.DisplayMetadata> metadataBySolidId = new LinkedHashMap<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = StepLegacyGeometryBuilder.unwrapStyledItem(item);
                if (StepValidationHelper.isRepresentationSolidItem(unwrapped)) {
                    StepMetadataExtractor.DisplayMetadata itemMetadata = metadata.forItem(item.id());
                    metadataBySolidId.put(unwrapped.id(), StepMetadataHelper.mergeMetadata(metadataBySolidId.get(unwrapped.id()), itemMetadata));
                }
            }
        }
        return Map.copyOf(metadataBySolidId);
    }


    public static List<StepRepresentation> linkedShapeRepresentations(
            StepRepresentation seed,
            Map<Integer, StepEntity> resolved
    ) {
        List<StepRepresentation> ordered = new ArrayList<>();
        Set<Integer> visited = new LinkedHashSet<>();
        collectLinkedShapeRepresentations(seed, resolved, visited, ordered);
        return List.copyOf(ordered);
    }


    static void collectLinkedShapeRepresentations(
            StepRepresentation current,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visited,
            List<StepRepresentation> ordered
    ) {
        if (!visited.add(current.id())) {
            return;
        }
        ordered.add(current);
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) entity;
                if (!relationship.rep1().shapeRepresentation() || !relationship.rep2().shapeRepresentation()) {
                    continue;
                }
                if (relationship.rep1().id() == current.id()) {
                    collectLinkedShapeRepresentations(relationship.rep2(), resolved, visited, ordered);
                } else if (relationship.rep2().id() == current.id()) {
                    collectLinkedShapeRepresentations(relationship.rep1(), resolved, visited, ordered);
                }
                continue;
            }
            if (entity instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) entity;
                if (!relationship.rep1().shapeRepresentation() || !relationship.rep2().shapeRepresentation()) {
                    continue;
                }
                if (relationship.rep1().id() == current.id()) {
                    collectLinkedShapeRepresentations(relationship.rep2(), resolved, visited, ordered);
                } else if (relationship.rep2().id() == current.id()) {
                    collectLinkedShapeRepresentations(relationship.rep1(), resolved, visited, ordered);
                }
            }
        }
    }


    public static BSplineSurface3 buildBsplineSurface(StepEntity geometry, StepCadBuilder builder) {
        return PreviewMeshExporter.buildBsplineSurface(geometry, builder);
    }


    public static BSplineSurface3 buildFreeFormSurface(StepFreeFormSurface surface, StepCadBuilder builder) {
        return PreviewMeshExporter.buildFreeFormSurface(surface, builder);
    }


    /**
     * Evaluator for a curve entity, delegated to
     * {@link PreviewCurveEvaluator#curveEvaluator(StepEntity, StepCadBuilder)}.
     *
     * <p>This class used to carry its own copy of the 59-rule first-match-wins table,
     * entry for entry the same as the preview one (spelled out where the preview side
     * used its reference/from-2D/path wrappers). Two tables meant two places to update
     * and two chances to drift, so the table lives in PreviewCurveEvaluator only; the
     * frozen dispatch order is checked against that single table.
     */
    public static CurveEvaluator curveEvaluator(StepEntity curve, StepCadBuilder builder) {
        return PreviewCurveEvaluator.curveEvaluator(curve, builder);
    }

    // Delegate to StepMappedItemTransformer - extracted utility class
    public static double[] mappedItemMatrix(StepMappedItem mappedItem, StepCadBuilder builder) {
        return StepMappedItemTransformer.mappedItemMatrix(mappedItem, builder);
    }

    // Delegate to StepPlacementTransformer - extracted utility class
    public static double[] matrixForTransformationOperator(
            com.minicad.step.model.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        return StepPlacementTransformer.matrixForTransformationOperator(transformation, builder);
    }

    private static AssemblyMetrics measureAssembly(
            List<RepresentationPayload> representations,
            List<InstancePayload> instances
    ) {
        Map<Integer, RepresentationPayload> byId = representations.stream()
                .collect(Collectors.toMap(RepresentationPayload::id, representation -> representation, (left, right) -> left, LinkedHashMap::new));
        int faceCount = 0;
        int edgeCount = 0;
        double area = 0.0;
        double edgeLength = 0.0;
        BoundsAccumulator bounds = new BoundsAccumulator();
        for (InstancePayload instance : instances) {
            for (Integer representationId : instance.representationIds()) {
                RepresentationPayload representation = byId.get(representationId);
                if (representation == null) {
                    continue;
                }
                faceCount += representation.faces().size();
                edgeCount += representation.edges().size();
                area += GeometryMeasurementHelper.approximateSurfaceArea(representation.faces(), instance.worldMatrix());
                edgeLength += GeometryMeasurementHelper.approximateEdgeLength(representation.edges(), instance.worldMatrix());
                includeRepresentationBounds(bounds, representation, instance.worldMatrix());
            }
        }
        return new AssemblyMetrics(
                new GeometrySummary(faceCount, edgeCount, area, edgeLength),
                bounds.toPayload()
        );
    }


    static GeometrySummary summarizeGeometry(GeometryCollection geometry) {
        return new GeometrySummary(
                geometry.faces().size(),
                geometry.edges().size(),
                GeometryMeasurementHelper.approximateSurfaceArea(geometry.faces()),
                GeometryMeasurementHelper.approximateEdgeLength(geometry.edges())
        );
    }

    // Delegate to StepBoundsAccumulator - extracted utility class
    private static void includeRepresentationBounds(
            BoundsAccumulator bounds,
            RepresentationPayload representation,
            double[] matrix
    ) {
        StepBoundsAccumulator.includeRepresentationBounds(bounds, representation, matrix);
    }


    public static RepresentationBuildResult buildRepresentationPayload(
            StepRepresentation representation,
            String displayName,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        if (!visitingRepresentations.add(representation.id())) {
            return new RepresentationBuildResult(
                    new RepresentationPayload(
                            representation.id(),
                            displayName,
                            List.of(),
                            null,
                            List.of(),
                            List.of()
                    ),
                    List.of()
            );
        }
        Set<Integer> shellIds = collectRepresentationShells(representation, resolved);
        Set<Integer> solidIds = collectRepresentationSolids(representation, resolved);
        StepMetadataExtractor.DisplayMetadata representationMetadata = metadata.forItem(representation.id());
        GeometryCollection shellGeometry = StepLegacyGeometryBuilder.buildGeometryForShells(
                shellIds,
                resolved,
                builder,
                metadata,
                collectInheritedShellMetadata(representation, metadata, resolved)
        );
        GeometryCollection solidGeometry = StepLegacyGeometryBuilder.buildGeometryForSolids(
                solidIds,
                resolved,
                builder,
                metadata,
                collectInheritedSolidMetadata(representation, metadata, resolved)
        );
        GeometryCollection mappedGeometry = buildMappedRepresentationGeometry(
                representation,
                resolved,
                builder,
                metadata,
                visitingRepresentations
        );
        GeometryCollection relatedGeometry = buildRelatedRepresentationGeometry(
                representation,
                resolved,
                builder,
                metadata,
                visitingRepresentations
        );
        GeometryCollection geometry = StepLegacyGeometryBuilder.mergeGeometry(
                StepLegacyGeometryBuilder.mergeGeometry(StepLegacyGeometryBuilder.mergeGeometry(shellGeometry, solidGeometry), mappedGeometry),
                relatedGeometry
        );
        List<EdgePayload> representationEdges = new ArrayList<>(geometry.edges());
        representationEdges.addAll(collectRepresentationLooseEdges(representation, resolved, builder, metadata));
        RepresentationBuildResult result = new RepresentationBuildResult(
                new RepresentationPayload(
                        representation.id(),
                        displayName,
                        representationMetadata.layers(),
                        PayloadConversionHelper.toColorPayload(representationMetadata.rgb()),
                        List.copyOf(representationEdges),
                        geometry.faces()
                ),
                geometry.unsupportedFaces()
        );
        visitingRepresentations.remove(representation.id());
        return result;
    }

}
