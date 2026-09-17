package com.minicad.export.json;

import com.minicad.helper.StepMetadataExtractor;
import com.minicad.preview.payload.EdgePayload;
import com.minicad.preview.payload.RepresentationBuildResult;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.semantic.StepCadBuilder;

import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Folds the edges of a mapped representation into the edge map of the entity the
 * mapping is attached to.
 *
 * <p>The body was declared twice, byte for byte: once in
 * {@code StepEdgePayloadBuilder} and once in {@code PreviewGeometryCollector}.
 * Both callers are edge-payload producers that already reach across packages for
 * {@link StepMappedItemTransformer} and {@link StepPlacementTransformer}, so the
 * rule got its own home rather than one caller owning it -- a private copy
 * inside a 1700-line builder is how the second copy came about in the first
 * place.
 *
 * <p>This is not a classification decision: the body resolves the placement
 * matrix, rebuilds the mapped representation's payload, and folds the
 * transformed edges in with {@code putIfAbsent}, so a shared edge id keeps the
 * first writer's payload.
 */
public final class StepMappedAnnotationEdgeCollector {

    private StepMappedAnnotationEdgeCollector() {
        // Utility class - prevent instantiation
    }

    /**
     * Rebuilds the mapped representation's payload and adds its transformed
     * edges to {@code edges}.
     *
     * <p>Does nothing when the mapping placement yields no matrix.
     *
     * @param mappedOwnerId the id the collected edges are attributed to
     * @param representation the mapped representation whose edges are collected
     * @param mappedOrigin the mapping's origin
     * @param mappingTarget the mapping's target
     * @param sourceType the source type tag recorded on each transformed edge
     * @param sourceStepId the source step id recorded on each transformed edge
     * @param edges the edge map to fold into
     * @param resolved the resolved entity table
     * @param builder the STEP builder used to evaluate placements
     */
    public static void collect(
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
        double[] matrix = StepPlacementTransformer.matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
        if (matrix == null) {
            return;
        }
        RepresentationBuildResult source = StepRepresentationPayloadBuilder.buildRepresentationPayload(
                representation,
                representation.name(),
                resolved,
                builder,
                StepMetadataExtractor.fromResolved(resolved),
                new LinkedHashSet<>()
        );
        for (EdgePayload edge : source.payload().edges()) {
            EdgePayload transformed = StepMappedItemTransformer.transformMappedEdge(edge, mappedOwnerId, matrix, sourceType, sourceStepId);
            edges.putIfAbsent(transformed.stepId(), transformed);
        }
    }
}
