package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved METADATA_RECORD.
 * A metadata record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @param metadataType metadata variance type
 * @param metadataKey metadata variance key
 * @param metadataValue metadata variance value
 * @param metadataSource metadata variance source reference
 * @param metadataTimestamp metadata variance timestamp
 * @param metadataStatus metadata variance status
 */
public final class StepMetadataRecord extends AbstractStepEntity {
    private final String metadataType;
    private final String metadataKey;
    private final String metadataValue;
    private final StepEntity metadataSource;
    private final StepEntity metadataTimestamp;
    private final String metadataStatus;

    public StepMetadataRecord(int id, String name, String metadataType, String metadataKey, String metadataValue, StepEntity metadataSource, StepEntity metadataTimestamp, String metadataStatus) {
        super(id, name);
        this.metadataType = metadataType;
        this.metadataKey = metadataKey;
        this.metadataValue = metadataValue;
        this.metadataSource = metadataSource;
        this.metadataTimestamp = metadataTimestamp;
        this.metadataStatus = metadataStatus;
    }

    public String getMetadataType() {
        return metadataType;
    }

    public String getMetadataKey() {
        return metadataKey;
    }

    public String getMetadataValue() {
        return metadataValue;
    }

    public StepEntity getMetadataSource() {
        return metadataSource;
    }

    public StepEntity getMetadataTimestamp() {
        return metadataTimestamp;
    }

    public String getMetadataStatus() {
        return metadataStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "metadataType", metadataType, "metadataKey", metadataKey,
                "metadataValue", metadataValue, "metadataSource", metadataSource,
                "metadataTimestamp", metadataTimestamp, "metadataStatus", metadataStatus);
    }
}
