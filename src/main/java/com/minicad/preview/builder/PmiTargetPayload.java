package com.minicad.preview.builder;

import com.minicad.preview.payload.PreviewPayloadCopies;

import java.util.List;

/**
 * PMI target payload for STEP preview export.
 */
public final class PmiTargetPayload {
    private final int id;
    private final String type;
    private final String name;
    private final List<String> instanceIds;
    private final String viaRelationshipType;
    private final Integer viaRelationshipId;
    private final String viaUsageType;
    private final Integer viaUsageId;
    private final String viaDefinitionType;
    private final Integer viaDefinitionId;

    public PmiTargetPayload(int id, String type, String name, List<String> instanceIds, String viaRelationshipType, Integer viaRelationshipId, String viaUsageType, Integer viaUsageId, String viaDefinitionType, Integer viaDefinitionId) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.instanceIds = PreviewPayloadCopies.copy(instanceIds);
        this.viaRelationshipType = viaRelationshipType;
        this.viaRelationshipId = viaRelationshipId;
        this.viaUsageType = viaUsageType;
        this.viaUsageId = viaUsageId;
        this.viaDefinitionType = viaDefinitionType;
        this.viaDefinitionId = viaDefinitionId;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getName() { return name; }
    public List<String> getInstanceIds() { return instanceIds; }
    public String getViaRelationshipType() { return viaRelationshipType; }
    public Integer getViaRelationshipId() { return viaRelationshipId; }
    public String getViaUsageType() { return viaUsageType; }
    public Integer getViaUsageId() { return viaUsageId; }
    public String getViaDefinitionType() { return viaDefinitionType; }
    public Integer getViaDefinitionId() { return viaDefinitionId; }

    // Record-style accessors
    public int id() { return id; }
    public String type() { return type; }
    public String name() { return name; }
    public List<String> instanceIds() { return instanceIds; }
    public String viaRelationshipType() { return viaRelationshipType; }
    public Integer viaRelationshipId() { return viaRelationshipId; }
    public String viaUsageType() { return viaUsageType; }
    public Integer viaUsageId() { return viaUsageId; }
    public String viaDefinitionType() { return viaDefinitionType; }
    public Integer viaDefinitionId() { return viaDefinitionId; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PmiTargetPayload)) return false;
        PmiTargetPayload that = (PmiTargetPayload) o;
        return id == that.id && java.util.Objects.equals(type, that.type) && java.util.Objects.equals(name, that.name) && java.util.Objects.equals(instanceIds, that.instanceIds) && java.util.Objects.equals(viaRelationshipType, that.viaRelationshipType) && java.util.Objects.equals(viaRelationshipId, that.viaRelationshipId) && java.util.Objects.equals(viaUsageType, that.viaUsageType) && java.util.Objects.equals(viaUsageId, that.viaUsageId) && java.util.Objects.equals(viaDefinitionType, that.viaDefinitionType) && java.util.Objects.equals(viaDefinitionId, that.viaDefinitionId);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(id, type, name, instanceIds, viaRelationshipType, viaRelationshipId, viaUsageType, viaUsageId, viaDefinitionType, viaDefinitionId);
    }

    @Override public String toString() {
        return "PmiTargetPayload{" + "id=id, type=type, name=name, instanceIds=instanceIds, viaRelationshipType=viaRelationshipType, viaRelationshipId=viaRelationshipId, viaUsageType=viaUsageType, viaUsageId=viaUsageId, viaDefinitionType=viaDefinitionType, viaDefinitionId=viaDefinitionId" + "}";
    }
}