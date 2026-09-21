package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepIndexDefinition extends AbstractStepEntity {
    private final String indexType;
    private final String indexKey;
    private final List<String> indexFields;
    private final String indexOrder;
    private final String indexStatus;

    public StepIndexDefinition(int id, String name, String indexType, String indexKey, List<String> indexFields, String indexOrder, String indexStatus) {
        super(id, name);
        this.indexType = indexType;
        this.indexKey = indexKey;
        this.indexFields = indexFields == null ? null : java.util.List.copyOf(indexFields);
        this.indexOrder = indexOrder;
        this.indexStatus = indexStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("indexType", indexType);
        state.put("indexKey", indexKey);
        state.put("indexFields", indexFields);
        state.put("indexOrder", indexOrder);
        state.put("indexStatus", indexStatus);
        return state;
    }
}
