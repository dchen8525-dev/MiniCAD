package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepQualityRecord extends AbstractStepEntity {
    private final String recordType;
    private final List<StepEntity> recordItems;
    private final StepEntity recordContext;
    private final StepEntity recordDate;
    private final StepEntity recordAuthor;
    private final String recordStatus;
    private final List<StepEntity> attachments;

    public StepQualityRecord(int id, String name, String recordType, List<StepEntity> recordItems, StepEntity recordContext, StepEntity recordDate, StepEntity recordAuthor, String recordStatus, List<StepEntity> attachments) {
        super(id, name);
        this.recordType = recordType;
        this.recordItems = recordItems == null ? null : java.util.List.copyOf(recordItems);
        this.recordContext = recordContext;
        this.recordDate = recordDate;
        this.recordAuthor = recordAuthor;
        this.recordStatus = recordStatus;
        this.attachments = attachments == null ? null : java.util.List.copyOf(attachments);
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("recordType", recordType);
        state.put("recordItems", recordItems);
        state.put("recordContext", recordContext);
        state.put("recordDate", recordDate);
        state.put("recordAuthor", recordAuthor);
        state.put("recordStatus", recordStatus);
        state.put("attachments", attachments);
        return state;
    }
}
