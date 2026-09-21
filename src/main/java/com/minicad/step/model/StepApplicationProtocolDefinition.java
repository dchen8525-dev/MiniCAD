package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal application protocol definition metadata.
 *
 * @param id STEP instance id
 * @param status protocol status text
 * @param schemaName interpreted model schema name
 * @param year protocol year
 * @param application application context
 */
public final class StepApplicationProtocolDefinition extends AbstractStepEntity {
    private final String status;
    private final String schemaName;
    private final int year;
    private final StepApplicationContext application;

    public StepApplicationProtocolDefinition(int id, String status, String schemaName, int year, StepApplicationContext application) {
        super(id, "");
        this.status = status;
        this.schemaName = schemaName;
        this.year = year;
        this.application = application;
    }

    public String getStatus() {
        return status;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public int getYear() {
        return year;
    }

    public StepApplicationContext getApplication() {
        return application;
    }

    // Record-style accessors
    public String status() {
        return status;
    }

    public String schemaName() {
        return schemaName;
    }

    public int year() {
        return year;
    }

    public StepApplicationContext application() {
        return application;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("status", status);
        state.put("schemaName", schemaName);
        state.put("year", year);
        state.put("application", application);
        return state;
    }
}
