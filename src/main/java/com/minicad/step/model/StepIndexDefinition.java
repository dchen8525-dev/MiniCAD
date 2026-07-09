package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INDEX_DEFINITION.
 * An index definition entity.
 *
 * @param id STEP instance id
 * @param name index name
 * @param indexType index variance type
 * @param indexKey index variance key definition
 * @param indexFields index variance indexed fields
 * @param indexOrder index variance ordering
 * @param indexStatus index variance status
 */
/**
 * Resolved INDEX_DEFINITION.
 * An index definition entity.
 *
 * @param id STEP instance id
 * @param name index name
 * @param indexType index variance type
 * @param indexKey index variance key definition
 * @param indexFields index variance indexed fields
 * @param indexOrder index variance ordering
 * @param indexStatus index variance status
 */
public final class StepIndexDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String indexType;
    private final String indexKey;
    private final List<String> indexFields;
    private final String indexOrder;
    private final String indexStatus;

    public StepIndexDefinition(int id, String name, String indexType, String indexKey, List<String> indexFields, String indexOrder, String indexStatus) {
        this.id = id;
        this.name = name;
        this.indexType = indexType;
        this.indexKey = indexKey;
        this.indexFields = indexFields == null ? null : java.util.List.copyOf(indexFields);
        this.indexOrder = indexOrder;
        this.indexStatus = indexStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIndexType() {
        return indexType;
    }

    public String getIndexKey() {
        return indexKey;
    }

    public List<String> getIndexFields() {
        return indexFields;
    }

    public String getIndexOrder() {
        return indexOrder;
    }

    public String getIndexStatus() {
        return indexStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepIndexDefinition that = (StepIndexDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(indexType, that.indexType) && Objects.equals(indexKey, that.indexKey) && Objects.equals(indexFields, that.indexFields) && Objects.equals(indexOrder, that.indexOrder) && Objects.equals(indexStatus, that.indexStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, indexType, indexKey, indexFields, indexOrder, indexStatus);
    }

    @Override
    public String toString() {
        return "StepIndexDefinition{" + "id=" + id + "name=" + name + "indexType=" + indexType + "indexKey=" + indexKey + "indexFields=" + indexFields + "indexOrder=" + indexOrder + "indexStatus=" + indexStatus + "}";
    }
}