package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTagRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String tagType;
    private final String tagValue;
    private final StepEntity tagTarget;
    private final String tagCategory;
    private final String tagStatus;

    public StepTagRecord(int id, String name, String tagType, String tagValue, StepEntity tagTarget, String tagCategory, String tagStatus) {
        this.id = id;
        this.name = name;
        this.tagType = tagType;
        this.tagValue = tagValue;
        this.tagTarget = tagTarget;
        this.tagCategory = tagCategory;
        this.tagStatus = tagStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTagRecord that = (StepTagRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(tagType, that.tagType) && Objects.equals(tagValue, that.tagValue) && Objects.equals(tagTarget, that.tagTarget) && Objects.equals(tagCategory, that.tagCategory) && Objects.equals(tagStatus, that.tagStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tagType, tagValue, tagTarget, tagCategory, tagStatus);
    }

    @Override
    public String toString() {
        return "StepTagRecord{" + "id=" + id + "name=" + name + "tagType=" + tagType + "tagValue=" + tagValue + "tagTarget=" + tagTarget + "tagCategory=" + tagCategory + "tagStatus=" + tagStatus + "}";
    }
}