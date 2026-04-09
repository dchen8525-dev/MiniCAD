package com.minicad.app;

import com.minicad.geometry.Vector3;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepContextDependentShapeRepresentation;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepItemDefinedTransformation;
import com.minicad.step.model.StepNextAssemblyUsageOccurrence;
import com.minicad.step.model.StepProduct;
import com.minicad.step.model.StepProductDefinition;
import com.minicad.step.model.StepProductDefinitionShape;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepRepresentationRelationship;
import com.minicad.step.model.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.StepShapeDefinitionRepresentation;
import com.minicad.step.model.StepShapeRepresentationRelationship;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builds an assembly instance graph from resolved STEP product and representation entities.
 */
public final class StepAssemblyGraphBuilder {

    private StepAssemblyGraphBuilder() {
    }

    public static AssemblyGraph build(Map<Integer, StepEntity> resolved) {
        Map<Integer, String> representationNames = new LinkedHashMap<>();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepRepresentation representation && representation.shapeRepresentation()) {
                representationNames.put(representation.id(), representation.name());
            }
        }

        Map<Integer, StepProduct> productByDefinitionId = new LinkedHashMap<>();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepProductDefinition productDefinition) {
                productByDefinitionId.put(productDefinition.id(), productDefinition.formation().ofProduct());
            }
        }

        Map<Integer, List<Integer>> repIdsByProductDefinition = new LinkedHashMap<>();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepShapeDefinitionRepresentation link) {
                Integer productDefinitionId = productDefinitionIdFor(link.definition().definition());
                if (productDefinitionId == null) {
                    continue;
                }
                repIdsByProductDefinition
                        .computeIfAbsent(productDefinitionId, ignored -> new ArrayList<>())
                        .add(link.usedRepresentation().id());
            }
        }
        for (List<Integer> repIds : repIdsByProductDefinition.values()) {
            repIds.sort(Integer::compareTo);
        }

        Map<Integer, StepContextDependentShapeRepresentation> contextByOccurrence = new LinkedHashMap<>();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepContextDependentShapeRepresentation contextDependent) {
                Integer occurrenceId = occurrenceIdFor(contextDependent.representedProductRelation());
                if (occurrenceId != null) {
                    contextByOccurrence.put(occurrenceId, contextDependent);
                }
            }
        }

        Map<Integer, List<StepNextAssemblyUsageOccurrence>> childrenByParent = new LinkedHashMap<>();
        Set<Integer> childDefinitions = new LinkedHashSet<>();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepNextAssemblyUsageOccurrence occurrence) {
                childrenByParent
                        .computeIfAbsent(occurrence.relatingProductDefinition().id(), ignored -> new ArrayList<>())
                        .add(occurrence);
                childDefinitions.add(occurrence.relatedProductDefinition().id());
            }
        }
        for (List<StepNextAssemblyUsageOccurrence> children : childrenByParent.values()) {
            children.sort(Comparator.comparingInt(StepNextAssemblyUsageOccurrence::id));
        }

        List<AssemblyNode> nodes = new ArrayList<>();
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepProductDefinition productDefinition)) {
                continue;
            }
            if (childDefinitions.contains(productDefinition.id())) {
                continue;
            }
            addNode(
                    productDefinition,
                    null,
                    null,
                    identityMatrix(),
                    identityMatrix(),
                    0,
                    repIdsByProductDefinition,
                    childrenByParent,
                    contextByOccurrence,
                    productByDefinitionId,
                    resolved,
                    nodes
            );
        }

        Set<Integer> usedRepresentationIds = new LinkedHashSet<>();
        for (AssemblyNode node : nodes) {
            usedRepresentationIds.addAll(node.representationIds());
        }
        List<AssemblyRepresentation> representations = new ArrayList<>();
        for (Integer representationId : usedRepresentationIds) {
            representations.add(new AssemblyRepresentation(
                    representationId,
                    representationNames.getOrDefault(representationId, "representation #" + representationId)
            ));
        }

        return new AssemblyGraph(List.copyOf(representations), List.copyOf(nodes));
    }

    private static Integer productDefinitionIdFor(StepEntity definition) {
        if (definition instanceof StepProductDefinition productDefinition) {
            return productDefinition.id();
        }
        if (definition instanceof StepNextAssemblyUsageOccurrence occurrence) {
            return occurrence.relatedProductDefinition().id();
        }
        return null;
    }

    private static Integer occurrenceIdFor(StepEntity relation) {
        if (relation instanceof StepNextAssemblyUsageOccurrence occurrence) {
            return occurrence.id();
        }
        if (relation instanceof StepProductDefinitionShape shape
                && shape.definition() instanceof StepNextAssemblyUsageOccurrence occurrence) {
            return occurrence.id();
        }
        return null;
    }

    private static void addNode(
            StepProductDefinition productDefinition,
            String nodeId,
            StepNextAssemblyUsageOccurrence occurrence,
            double[] localMatrix,
            double[] worldMatrix,
            int depth,
            Map<Integer, List<Integer>> repIdsByProductDefinition,
            Map<Integer, List<StepNextAssemblyUsageOccurrence>> childrenByParent,
            Map<Integer, StepContextDependentShapeRepresentation> contextByOccurrence,
            Map<Integer, StepProduct> productByDefinitionId,
            Map<Integer, StepEntity> resolved,
            List<AssemblyNode> nodes
    ) {
        String effectiveNodeId = nodeId != null
                ? nodeId
                : "pd-" + productDefinition.id();
        List<Integer> representationIds = List.copyOf(repIdsByProductDefinition.getOrDefault(productDefinition.id(), List.of()));
        StepProduct product = productByDefinitionId.get(productDefinition.id());
        String label = occurrence == null
                ? product != null && !product.name().isBlank() ? product.name() : productDefinition.identifier()
                : occurrence.identifier();
        String description = occurrence == null
                ? product != null && !product.description().isBlank() ? product.description() : productDefinition.description()
                : occurrence.description();
        nodes.add(new AssemblyNode(
                effectiveNodeId,
                occurrence == null ? null : parentIdOf(effectiveNodeId),
                productDefinition.id(),
                occurrence == null ? null : occurrence.id(),
                label,
                description,
                representationIds,
                localMatrix,
                worldMatrix,
                depth
        ));

        for (StepNextAssemblyUsageOccurrence child : childrenByParent.getOrDefault(productDefinition.id(), List.of())) {
            StepContextDependentShapeRepresentation contextDependent = contextByOccurrence.get(child.id());
            double[] childLocalMatrix = contextDependent == null
                    ? identityMatrix()
                    : localTransformationMatrixFor(contextDependent, resolved);
            double[] childWorldMatrix = multiplyMatrices(worldMatrix, childLocalMatrix);
            String childNodeId = effectiveNodeId + "/occ-" + child.id() + "-pd-" + child.relatedProductDefinition().id();
            addNode(
                    child.relatedProductDefinition(),
                    childNodeId,
                    child,
                    childLocalMatrix,
                    childWorldMatrix,
                    depth + 1,
                    repIdsByProductDefinition,
                    childrenByParent,
                    contextByOccurrence,
                    productByDefinitionId,
                    resolved,
                    nodes
            );
        }
    }

    private static String parentIdOf(String nodeId) {
        int slash = nodeId.lastIndexOf('/');
        if (slash < 0) {
            return null;
        }
        return nodeId.substring(0, slash);
    }

    static double[] localTransformationMatrixFor(
            StepContextDependentShapeRepresentation contextDependent,
            Map<Integer, StepEntity> resolved
    ) {
        StepEntity relation = resolved.get(contextDependent.representationRelationship().id());
        if (relation instanceof StepRepresentationRelationshipWithTransformation withTransformation) {
            return matrixFor(withTransformation.transformationOperator());
        }
        if (relation instanceof StepShapeRepresentationRelationship
                || relation instanceof StepRepresentationRelationship) {
            return identityMatrix();
        }
        return identityMatrix();
    }

    static double[] matrixFor(StepItemDefinedTransformation transformation) {
        double[] from = matrixForPlacement(transformation.transformItem1());
        double[] to = matrixForPlacement(transformation.transformItem2());
        return multiplyMatrices(to, inverseRigidTransform(from));
    }

    static double[] matrixForPlacement(StepAxis2Placement3D placement) {
        Vector3 z = directionVector(placement.axis()).normalize().asVector();
        Vector3 xSeed = directionVector(placement.refDirection()).normalize().asVector();
        Vector3 y = z.cross(xSeed).normalize().asVector();
        Vector3 x = y.cross(z).normalize().asVector();
        List<Double> origin = placement.location().coordinates();
        return new double[]{
                x.x(), y.x(), z.x(), origin.get(0),
                x.y(), y.y(), z.y(), origin.get(1),
                x.z(), y.z(), z.z(), origin.get(2),
                0.0, 0.0, 0.0, 1.0
        };
    }

    private static Vector3 directionVector(com.minicad.step.model.StepDirection direction) {
        List<Double> ratios = direction.directionRatios();
        return new Vector3(ratios.get(0), ratios.get(1), ratios.get(2));
    }

    static double[] inverseRigidTransform(double[] matrix) {
        double r00 = matrix[0];
        double r01 = matrix[1];
        double r02 = matrix[2];
        double tx = matrix[3];
        double r10 = matrix[4];
        double r11 = matrix[5];
        double r12 = matrix[6];
        double ty = matrix[7];
        double r20 = matrix[8];
        double r21 = matrix[9];
        double r22 = matrix[10];
        double tz = matrix[11];

        return new double[]{
                r00, r10, r20, -(r00 * tx + r10 * ty + r20 * tz),
                r01, r11, r21, -(r01 * tx + r11 * ty + r21 * tz),
                r02, r12, r22, -(r02 * tx + r12 * ty + r22 * tz),
                0.0, 0.0, 0.0, 1.0
        };
    }

    static double[] multiplyMatrices(double[] left, double[] right) {
        double[] result = new double[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                double value = 0.0;
                for (int k = 0; k < 4; k++) {
                    value += left[row * 4 + k] * right[k * 4 + col];
                }
                result[row * 4 + col] = value;
            }
        }
        return result;
    }

    static double[] identityMatrix() {
        return new double[]{
                1.0, 0.0, 0.0, 0.0,
                0.0, 1.0, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 0.0, 1.0
        };
    }

    public record AssemblyGraph(List<AssemblyRepresentation> representations, List<AssemblyNode> nodes) {
    }

    public record AssemblyRepresentation(int representationId, String name) {
    }

    public record AssemblyNode(
            String id,
            String parentId,
            int productDefinitionId,
            Integer occurrenceId,
            String label,
            String description,
            List<Integer> representationIds,
            double[] localMatrix,
            double[] worldMatrix,
            int depth
    ) {
    }
}
