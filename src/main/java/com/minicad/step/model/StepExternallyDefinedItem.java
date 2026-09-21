package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal externally defined item metadata.
 *
 * @param id STEP instance id
 * @param itemId external item identifier
 * @param source external source
 * @param entityName concrete STEP entity name
 */
public final class StepExternallyDefinedItem extends AbstractStepEntity {
    private final String itemId;
    private final StepExternalSource source;
    private final String entityName;

    public StepExternallyDefinedItem(int id, String itemId, StepExternalSource source, String entityName) {
        super(id, "");
        this.itemId = itemId;
        this.source = source;
        this.entityName = entityName;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("itemId", itemId);
        state.put("source", source);
        state.put("entityName", entityName);
        return state;
    }
}
