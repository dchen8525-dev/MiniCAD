package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved QUALITY_RECORD.
 * A quality record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @param recordType record type classification
 * @param recordItems record items/data
 * @param recordContext record context reference
 * @param recordDate record date
 * @param recordAuthor record author/inspector
 * @param recordStatus record status (approved, pending)
 * @param attachments record attachments/references
 */
/**
 * Resolved QUALITY_RECORD.
 * A quality record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @param recordType record type classification
 * @param recordItems record items/data
 * @param recordContext record context reference
 * @param recordDate record date
 * @param recordAuthor record author/inspector
 * @param recordStatus record status (approved, pending)
 * @param attachments record attachments/references
 */
public final class StepQualityRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String recordType;
    private final List<StepEntity> recordItems;
    private final StepEntity recordContext;
    private final StepEntity recordDate;
    private final StepEntity recordAuthor;
    private final String recordStatus;
    private final List<StepEntity> attachments;

    public StepQualityRecord(int id, String name, String recordType, List<StepEntity> recordItems, StepEntity recordContext, StepEntity recordDate, StepEntity recordAuthor, String recordStatus, List<StepEntity> attachments) {
        this.id = id;
        this.name = name;
        this.recordType = recordType;
        this.recordItems = recordItems == null ? null : java.util.List.copyOf(recordItems);
        this.recordContext = recordContext;
        this.recordDate = recordDate;
        this.recordAuthor = recordAuthor;
        this.recordStatus = recordStatus;
        this.attachments = attachments == null ? null : java.util.List.copyOf(attachments);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRecordType() {
        return recordType;
    }

    public List<StepEntity> getRecordItems() {
        return recordItems;
    }

    public StepEntity getRecordContext() {
        return recordContext;
    }

    public StepEntity getRecordDate() {
        return recordDate;
    }

    public StepEntity getRecordAuthor() {
        return recordAuthor;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public List<StepEntity> getAttachments() {
        return attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepQualityRecord that = (StepQualityRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(recordType, that.recordType) && Objects.equals(recordItems, that.recordItems) && Objects.equals(recordContext, that.recordContext) && Objects.equals(recordDate, that.recordDate) && Objects.equals(recordAuthor, that.recordAuthor) && Objects.equals(recordStatus, that.recordStatus) && Objects.equals(attachments, that.attachments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, recordType, recordItems, recordContext, recordDate, recordAuthor, recordStatus, attachments);
    }

    @Override
    public String toString() {
        return "StepQualityRecord{" + "id=" + id + "name=" + name + "recordType=" + recordType + "recordItems=" + recordItems + "recordContext=" + recordContext + "recordDate=" + recordDate + "recordAuthor=" + recordAuthor + "recordStatus=" + recordStatus + "attachments=" + attachments + "}";
    }
}