package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal application protocol definition metadata.
 *
 * @param id STEP instance id
 * @param status protocol status text
 * @param schemaName interpreted model schema name
 * @param year protocol year
 * @param application application context
 */
/**
 * Minimal application protocol definition metadata.
 *
 * @param id STEP instance id
 * @param status protocol status text
 * @param schemaName interpreted model schema name
 * @param year protocol year
 * @param application application context
 */
public final class StepApplicationProtocolDefinition implements StepEntity {
    private final int id;
    private final String status;
    private final String schemaName;
    private final int year;
    private final StepApplicationContext application;

    public StepApplicationProtocolDefinition(int id, String status, String schemaName, int year, StepApplicationContext application) {
        this.id = id;
        this.status = status;
        this.schemaName = schemaName;
        this.year = year;
        this.application = application;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepApplicationProtocolDefinition that = (StepApplicationProtocolDefinition) o;
        return id == that.id && Objects.equals(status, that.status) && Objects.equals(schemaName, that.schemaName) && year == that.year && Objects.equals(application, that.application);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, schemaName, year, application);
    }

    @Override
    public String toString() {
        return "StepApplicationProtocolDefinition{" + "id=" + id + "status=" + status + "schemaName=" + schemaName + "year=" + year + "application=" + application + "}";
    }
}
