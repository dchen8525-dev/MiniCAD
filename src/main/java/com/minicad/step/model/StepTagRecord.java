package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TAG_RECORD.
 * A tag record entity.
 *
 * @param id STEP instance id
 * @param name tag name
 * @param tagType tag variance type
 * @param tagValue tag variance value
 * @param tagTarget tag variance target reference
 * @param tagCategory tag variance category
 * @param tagStatus tag variance status
 */
public final class StepTagRecord extends AbstractStepEntity {
    private final String tagType;
    private final String tagValue;
    private final StepEntity tagTarget;
    private final String tagCategory;
    private final String tagStatus;

    public StepTagRecord(int id, String name, String tagType, String tagValue, StepEntity tagTarget, String tagCategory, String tagStatus) {
        super(id, name);
        this.tagType = tagType;
        this.tagValue = tagValue;
        this.tagTarget = tagTarget;
        this.tagCategory = tagCategory;
        this.tagStatus = tagStatus;
    }

    public String getTagType() {
        return tagType;
    }

    public String getTagValue() {
        return tagValue;
    }

    public StepEntity getTagTarget() {
        return tagTarget;
    }

    public String getTagCategory() {
        return tagCategory;
    }

    public String getTagStatus() {
        return tagStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("tagType", tagType);
        state.put("tagValue", tagValue);
        state.put("tagTarget", tagTarget);
        state.put("tagCategory", tagCategory);
        state.put("tagStatus", tagStatus);
        return state;
    }
}
