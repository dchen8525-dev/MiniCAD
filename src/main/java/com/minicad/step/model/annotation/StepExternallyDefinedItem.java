package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.classification.StepExternalSource;
import java.util.Objects;
/**
 * Minimal externally defined item metadata.
 *
 * @param id STEP instance id
 * @param itemId external item identifier
 * @param source external source
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal externally defined item metadata.
 *
 * @param id STEP instance id
 * @param itemId external item identifier
 * @param source external source
 * @param entityName concrete STEP entity name
 */
public final class StepExternallyDefinedItem implements StepEntity {
    private final int id;
    private final String itemId;
    private final StepExternalSource source;
    private final String entityName;

    public StepExternallyDefinedItem(int id, String itemId, StepExternalSource source, String entityName) {
        this.id = id;
        this.itemId = itemId;
        this.source = source;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return entityName != null ? entityName : "";
    }

    public String getItemId() {
        return itemId;
    }

    public StepExternalSource getSource() {
        return source;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    // Record-style accessors
    public StepExternalSource source() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExternallyDefinedItem that = (StepExternallyDefinedItem) o;
        return id == that.id && Objects.equals(itemId, that.itemId) && Objects.equals(source, that.source) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, source, entityName);
    }

    @Override
    public String toString() {
        return "StepExternallyDefinedItem{" + "id=" + id + "itemId=" + itemId + "source=" + source + "entityName=" + entityName + "}";
    }
}
