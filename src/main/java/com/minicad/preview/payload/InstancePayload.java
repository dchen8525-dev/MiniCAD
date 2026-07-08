package com.minicad.preview.payload;

import java.util.Arrays;
import java.util.List;

/**
 * Instance payload for assembly instance representation.
 */
public final class InstancePayload {
    private final String id;
    private final String parentId;
    private final int productDefinitionId;
    private final Integer occurrenceId;
    private final Integer representationId;
    private final List<Integer> representationIds;
    private final String label;
    private final String description;
    private final double[] localMatrix;
    private final double[] worldMatrix;
    private final int depth;

    public InstancePayload(String id, String parentId, int productDefinitionId, Integer occurrenceId, Integer representationId, List<Integer> representationIds, String label, String description, double[] localMatrix, double[] worldMatrix, int depth) {
        this.id = id;
        this.parentId = parentId;
        this.productDefinitionId = productDefinitionId;
        this.occurrenceId = occurrenceId;
        this.representationId = representationId;
        this.representationIds = PreviewPayloadCopies.copy(representationIds);
        this.label = label;
        this.description = description;
        this.localMatrix = PreviewPayloadCopies.copy(localMatrix);
        this.worldMatrix = PreviewPayloadCopies.copy(worldMatrix);
        this.depth = depth;
    }

    public String getId() { return id; }
    public String getParentId() { return parentId; }
    public int getProductDefinitionId() { return productDefinitionId; }
    public Integer getOccurrenceId() { return occurrenceId; }
    public Integer getRepresentationId() { return representationId; }
    public List<Integer> getRepresentationIds() { return representationIds; }
    public String getLabel() { return label; }
    public String getDescription() { return description; }
    public double[] getLocalMatrix() { return localMatrix; }
    public double[] getWorldMatrix() { return worldMatrix; }
    public int getDepth() { return depth; }

    // Record-style accessors
    public String id() { return id; }
    public String parentId() { return parentId; }
    public int productDefinitionId() { return productDefinitionId; }
    public Integer occurrenceId() { return occurrenceId; }
    public Integer representationId() { return representationId; }
    public List<Integer> representationIds() { return representationIds; }
    public String label() { return label; }
    public String description() { return description; }
    public double[] localMatrix() { return localMatrix; }
    public double[] worldMatrix() { return worldMatrix; }
    public int depth() { return depth; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstancePayload)) return false;
        InstancePayload that = (InstancePayload) o;
        return java.util.Objects.equals(id, that.id) && java.util.Objects.equals(parentId, that.parentId) && productDefinitionId == that.productDefinitionId && java.util.Objects.equals(occurrenceId, that.occurrenceId) && java.util.Objects.equals(representationId, that.representationId) && java.util.Objects.equals(representationIds, that.representationIds) && java.util.Objects.equals(label, that.label) && java.util.Objects.equals(description, that.description) && Arrays.equals(localMatrix, that.localMatrix) && Arrays.equals(worldMatrix, that.worldMatrix) && depth == that.depth;
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(id, parentId, productDefinitionId, occurrenceId, representationId, representationIds, label, description, Arrays.hashCode(localMatrix), Arrays.hashCode(worldMatrix), depth);
    }

    @Override public String toString() {
        return "InstancePayload{" + "id=id, parentId=parentId, productDefinitionId=productDefinitionId, occurrenceId=occurrenceId, representationId=representationId, representationIds=representationIds, label=label, description=description, localMatrix=Arrays.toString(localMatrix), worldMatrix=Arrays.toString(worldMatrix), depth=depth" + "}";
    }
}