package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMetadataRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String metadataType;
    private final String metadataKey;
    private final String metadataValue;
    private final StepEntity metadataSource;
    private final StepEntity metadataTimestamp;
    private final String metadataStatus;

    public StepMetadataRecord(int id, String name, String metadataType, String metadataKey, String metadataValue, StepEntity metadataSource, StepEntity metadataTimestamp, String metadataStatus) {
        this.id = id;
        this.name = name;
        this.metadataType = metadataType;
        this.metadataKey = metadataKey;
        this.metadataValue = metadataValue;
        this.metadataSource = metadataSource;
        this.metadataTimestamp = metadataTimestamp;
        this.metadataStatus = metadataStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMetadataRecord that = (StepMetadataRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(metadataType, that.metadataType) && Objects.equals(metadataKey, that.metadataKey) && Objects.equals(metadataValue, that.metadataValue) && Objects.equals(metadataSource, that.metadataSource) && Objects.equals(metadataTimestamp, that.metadataTimestamp) && Objects.equals(metadataStatus, that.metadataStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, metadataType, metadataKey, metadataValue, metadataSource, metadataTimestamp, metadataStatus);
    }

    @Override
    public String toString() {
        return "StepMetadataRecord{" + "id=" + id + "name=" + name + "metadataType=" + metadataType + "metadataKey=" + metadataKey + "metadataValue=" + metadataValue + "metadataSource=" + metadataSource + "metadataTimestamp=" + metadataTimestamp + "metadataStatus=" + metadataStatus + "}";
    }
}