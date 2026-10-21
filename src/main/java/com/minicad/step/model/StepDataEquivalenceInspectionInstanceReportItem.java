package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DATA_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM entity model.
 * Report item for data equivalence inspection instance.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param measuredValueForInspectedElement reference to equivalence measured value select
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepDataEquivalenceInspectionInstanceReportItem extends AbstractStepEntity {
    private final Object measuredValueForInspectedElement; // equivalence_measured_value_select reference
    private final String entityName;

    public StepDataEquivalenceInspectionInstanceReportItem(
        int id,
        String name,
        Object measuredValueForInspectedElement,
        String entityName) {
        super(id, name);
        this.measuredValueForInspectedElement = measuredValueForInspectedElement;
        this.entityName = entityName;
    }

    public Object getMeasuredValueForInspectedElement() {
        return measuredValueForInspectedElement;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("measuredValueForInspectedElement", measuredValueForInspectedElement);
        state.put("entityName", entityName);
        return state;
    }
}
