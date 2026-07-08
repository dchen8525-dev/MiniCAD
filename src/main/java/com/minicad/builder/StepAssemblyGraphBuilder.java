package com.minicad.builder;

import com.minicad.geometry.Vector3;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
import com.minicad.step.model.product.StepContextDependentShapeRepresentation;
import com.minicad.step.model.core.base.StepEntity;
import com.minicad.step.model.product.StepItemDefinedTransformation;
import com.minicad.step.model.product.StepNextAssemblyUsageOccurrence;
import com.minicad.step.model.product.StepProduct;
import com.minicad.step.model.product.StepProductDefinition;
import com.minicad.step.model.product.StepProductDefinitionShape;
import com.minicad.step.model.workflow.StepRepresentation;
import com.minicad.step.model.product.StepRepresentationRelationship;
import com.minicad.step.model.product.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.product.StepShapeDefinitionRepresentation;
import com.minicad.step.model.workflow.StepShapeRepresentationRelationship;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;

/**
 * Builds an assembly instance graph from resolved STEP product and representation entities.
 */
public final class StepAssemblyGraphBuilder {
    private static final double AXIS_PARALLEL_TOLERANCE = 1.0e-9;

    private StepAssemblyGraphBuilder() {
    }

    /**
     * Build assembly graph without unit scaling (default: scale factor = 1.0).
     *
     * @param resolved resolved STEP entities
     * @return assembly graph
     */
    public static AssemblyGraph build(Map<Integer, StepEntity> resolved) {
        return build(resolved, 1.0);
    }

    /**
     * Build assembly graph with unit scaling.
     * <p>
     * F04: Assembly transforms must apply after unit conversion consistently.
     * This method applies the given scale factor to all translation components
     * of assembly transformation matrices.
     *
     * @param resolved resolved STEP entities
     * @param scaleToMeters scale factor to convert STEP file units to meters
     *                      (e.g., 0.001 for millimeters, 0.0254 for inches, 1.0 for meters)
     * @return assembly graph with scaled transforms
     */
    public static AssemblyGraph build(Map<Integer, StepEntity> resolved, double scaleToMeters) {
        Map<Integer, String> representationNames = new LinkedHashMap<>();
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepRepresentation)) {
                continue;
            }
            StepRepresentation representation = (StepRepresentation) entity;
            representationNames.put(representation.id(), representation.name());
        }

        Map<Integer, StepProduct> productByDefinitionId = new LinkedHashMap<>();
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepProductDefinition)) {
                continue;
            }
            StepProductDefinition productDefinition = (StepProductDefinition) entity;
            productByDefinitionId.put(productDefinition.id(), productDefinition.formation().ofProduct());
        }

        Map<Integer, List<Integer>> repIdsByProductDefinition = new LinkedHashMap<>();
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepShapeDefinitionRepresentation)) {
                continue;
            }
            StepShapeDefinitionRepresentation link = (StepShapeDefinitionRepresentation) entity;
                Integer productDefinitionId = productDefinitionIdFor(link.definition().definition());
                if (productDefinitionId == null) {
                    continue;
                }
                repIdsByProductDefinition
                        .computeIfAbsent(productDefinitionId, ignored -> new ArrayList<>())
                        .add(link.usedRepresentation().id());
        }
        for (List<Integer> repIds : repIdsByProductDefinition.values()) {
            repIds.sort(Integer::compareTo);
        }

        Map<Integer, StepContextDependentShapeRepresentation> contextByOccurrence = new LinkedHashMap<>();
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepContextDependentShapeRepresentation)) {
                continue;
            }
            StepContextDependentShapeRepresentation contextDependent = (StepContextDependentShapeRepresentation) entity;
            Integer occurrenceId = occurrenceIdFor(contextDependent.representedProductRelation());
            if (occurrenceId != null) {
                contextByOccurrence.put(occurrenceId, contextDependent);
            }
        }

        Map<Integer, List<StepNextAssemblyUsageOccurrence>> childrenByParent = new LinkedHashMap<>();
        Set<Integer> childDefinitions = new LinkedHashSet<>();
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepNextAssemblyUsageOccurrence)) {
                continue;
            }
            StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) entity;
            childrenByParent
                    .computeIfAbsent(occurrence.relatingProductDefinition().id(), ignored -> new ArrayList<>())
                    .add(occurrence);
            childDefinitions.add(occurrence.relatedProductDefinition().id());
        }
        for (List<StepNextAssemblyUsageOccurrence> children : childrenByParent.values()) {
            children.sort(Comparator.comparingInt(StepNextAssemblyUsageOccurrence::id));
        }

        List<AssemblyNode> nodes = new ArrayList<>();
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepProductDefinition)) {
                continue;
            }
            StepProductDefinition productDefinition = (StepProductDefinition) entity;
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
                    nodes,
                    scaleToMeters
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
        if (definition instanceof StepProductDefinition) {
            StepProductDefinition productDefinition = (StepProductDefinition) definition;
            return productDefinition.id();
        }
        if (definition instanceof StepNextAssemblyUsageOccurrence) {
            StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) definition;
            return occurrence.relatedProductDefinition().id();
        }
        return null;
    }

    private static Integer occurrenceIdFor(StepEntity relation) {
        if (relation instanceof StepNextAssemblyUsageOccurrence) {
            StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) relation;
            return occurrence.id();
        }
        if (relation instanceof StepProductDefinitionShape
            && ((StepProductDefinitionShape) relation).definition() instanceof StepNextAssemblyUsageOccurrence) {
            StepProductDefinitionShape shape = (StepProductDefinitionShape) relation;
            StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) shape.definition();
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
            List<AssemblyNode> nodes,
            double scaleToMeters
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
                    : localTransformationMatrixFor(contextDependent, resolved, scaleToMeters);
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
                    nodes,
                    scaleToMeters
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

    public static double[] localTransformationMatrixFor(
            StepContextDependentShapeRepresentation contextDependent,
            Map<Integer, StepEntity> resolved
    ) {
        return localTransformationMatrixFor(contextDependent, resolved, 1.0);
    }

    public static double[] localTransformationMatrixFor(
            StepContextDependentShapeRepresentation contextDependent,
            Map<Integer, StepEntity> resolved,
            double scaleToMeters
    ) {
        StepEntity relation = resolved.get(contextDependent.representationRelationship().id());
        if (relation instanceof StepRepresentationRelationshipWithTransformation) {
            StepRepresentationRelationshipWithTransformation withTransformation = (StepRepresentationRelationshipWithTransformation) relation;
            return matrixFor(withTransformation.transformationOperator(), scaleToMeters);
        }
        if (relation instanceof StepShapeRepresentationRelationship
                || relation instanceof StepRepresentationRelationship) {
            return identityMatrix();
        }
        return identityMatrix();
    }

    public static double[] matrixFor(StepItemDefinedTransformation transformation) {
        return matrixFor(transformation, 1.0);
    }

    public static double[] matrixFor(StepItemDefinedTransformation transformation, double scaleToMeters) {
        double[] from = matrixForPlacement(transformation.transformItem1(), scaleToMeters);
        double[] to = matrixForPlacement(transformation.transformItem2(), scaleToMeters);
        return multiplyMatrices(to, inverseRigidTransform(from));
    }

    public static double[] matrixForPlacement(StepAxis2Placement3D placement) {
        return matrixForPlacement(placement, 1.0);
    }

    public static double[] matrixForPlacement(StepAxis2Placement3D placement, double scaleToMeters) {
        Vector3 z = directionVector(placement.axis()).normalize().asVector();
        Vector3 xSeed = directionVector(placement.refDirection()).normalize().asVector();
        Vector3 cross = z.cross(xSeed);
        if (cross.norm() <= AXIS_PARALLEL_TOLERANCE) {
            throw new IllegalArgumentException("AXIS2_PLACEMENT_3D #" + placement.id()
                    + " axis and refDirection must not be parallel");
        }
        Vector3 y = cross.normalize().asVector();
        Vector3 x = y.cross(z).normalize().asVector();
        List<Double> origin = placement.location().coordinates();
        // Apply unit scale to translation components (F04)
        double tx = origin.get(0) * scaleToMeters;
        double ty = origin.get(1) * scaleToMeters;
        double tz = origin.get(2) * scaleToMeters;
        return new double[]{
                x.x(), y.x(), z.x(), tx,
                x.y(), y.y(), z.y(), ty,
                x.z(), y.z(), z.z(), tz,
                0.0, 0.0, 0.0, 1.0
        };
    }

    private static Vector3 directionVector(com.minicad.step.model.geometry.StepDirection direction) {
        List<Double> ratios = direction.directionRatios();
        return new Vector3(ratios.get(0), ratios.get(1), ratios.get(2));
    }

    public static double[] inverseRigidTransform(double[] matrix) {
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

    public static double[] multiplyMatrices(double[] left, double[] right) {
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

    public static double[] identityMatrix() {
        return new double[]{
                1.0, 0.0, 0.0, 0.0,
                0.0, 1.0, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 0.0, 1.0
        };
    }

    
public static final class AssemblyGraph {
    private final List<AssemblyRepresentation> representations;
    private final List<AssemblyNode> nodes;

    public AssemblyGraph(List<AssemblyRepresentation> representations, List<AssemblyNode> nodes) {
        this.representations = representations == null ? null : java.util.List.copyOf(representations);
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
    }

    public List<AssemblyRepresentation> getRepresentations() {
        return representations;
    }

    public List<AssemblyNode> getNodes() {
        return nodes;
    }

    // Record-style accessors
    public List<AssemblyRepresentation> representations() { return representations; }
    public List<AssemblyNode> nodes() { return nodes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssemblyGraph that = (AssemblyGraph) o;
        return Objects.equals(representations, that.representations) && Objects.equals(nodes, that.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(representations, nodes);
    }

    @Override
    public String toString() {
        return "AssemblyGraph{" + "representations=" + representations + "nodes=" + nodes + "}";
    }
}

    public static final class AssemblyRepresentation {
        private final int representationId;
        private final String name;

        public AssemblyRepresentation(int representationId, String name) {
            this.representationId = representationId;
            this.name = name;
        }

        public int representationId() { return representationId; }
        public String name() { return name; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AssemblyRepresentation that = (AssemblyRepresentation) o;
            return representationId == that.representationId && Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(representationId, name);
        }

        @Override
        public String toString() {
            return "AssemblyRepresentation{representationId=" + representationId + ", name=" + name + "}";
        }
    }

    public static final class AssemblyNode {
        private final String id;
        private final String parentId;
        private final int productDefinitionId;
        private final Integer occurrenceId;
        private final String label;
        private final String description;
        private final List<Integer> representationIds;
        private final double[] localMatrix;
        private final double[] worldMatrix;
        private final int depth;

        public AssemblyNode(String id, String parentId, int productDefinitionId,
                            Integer occurrenceId, String label, String description,
                            List<Integer> representationIds, double[] localMatrix,
                            double[] worldMatrix, int depth) {
            this.id = id;
            this.parentId = parentId;
            this.productDefinitionId = productDefinitionId;
            this.occurrenceId = occurrenceId;
            this.label = label;
            this.description = description;
            this.representationIds = representationIds == null ? null : List.copyOf(representationIds);
            this.localMatrix = localMatrix == null ? null : localMatrix.clone();
            this.worldMatrix = worldMatrix == null ? null : worldMatrix.clone();
            this.depth = depth;
        }

        public String id() { return id; }
        public String parentId() { return parentId; }
        public int productDefinitionId() { return productDefinitionId; }
        public Integer occurrenceId() { return occurrenceId; }
        public String label() { return label; }
        public String description() { return description; }
        public List<Integer> representationIds() { return representationIds; }
        public double[] localMatrix() { return localMatrix == null ? null : localMatrix.clone(); }
        public double[] worldMatrix() { return worldMatrix == null ? null : worldMatrix.clone(); }
        public int depth() { return depth; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AssemblyNode that = (AssemblyNode) o;
            return productDefinitionId == that.productDefinitionId && depth == that.depth
                    && Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId)
                    && Objects.equals(occurrenceId, that.occurrenceId) && Objects.equals(label, that.label)
                    && Objects.equals(description, that.description) && Objects.equals(representationIds, that.representationIds)
                    && java.util.Arrays.equals(localMatrix, that.localMatrix)
                    && java.util.Arrays.equals(worldMatrix, that.worldMatrix);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(id, parentId, productDefinitionId, occurrenceId, label, description, representationIds, depth);
            result = 31 * result + java.util.Arrays.hashCode(localMatrix);
            result = 31 * result + java.util.Arrays.hashCode(worldMatrix);
            return result;
        }

        @Override
        public String toString() {
            return "AssemblyNode{id=" + id + ", parentId=" + parentId + ", productDefinitionId=" + productDefinitionId
                    + ", occurrenceId=" + occurrenceId + ", label=" + label + ", description=" + description
                    + ", representationIds=" + representationIds + ", depth=" + depth + "}";
        }
    }
}
