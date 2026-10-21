package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DATA_EQUIVALENCE_REPORT_REQUEST entity model.
 * Request for data equivalence report.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param reportRequestType type of report request (for A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE)
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepDataEquivalenceReportRequest extends AbstractStepEntity {
    private final Object reportRequestType; // representative_value_type reference
    private final String entityName;

    public StepDataEquivalenceReportRequest(
        int id,
        String name,
        Object reportRequestType,
        String entityName) {
        super(id, name);
        this.reportRequestType = reportRequestType;
        this.entityName = entityName;
    }

    public Object getReportRequestType() {
        return reportRequestType;
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
        state.put("reportRequestType", reportRequestType);
        state.put("entityName", entityName);
        return state;
    }
}
