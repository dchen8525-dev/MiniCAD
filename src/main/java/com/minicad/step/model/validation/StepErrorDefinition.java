package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ERROR_DEFINITION.
 * An error definition entity.
 *
 * @param id STEP instance id
 * @param name error name
 * @param errorType error variance type
 * @param errorCode error variance code
 * @param errorDescription error variance description
 * @param errorSeverity error variance severity level
 * @param errorStatus error variance status
 */
/**
 * Resolved ERROR_DEFINITION.
 * An error definition entity.
 *
 * @param id STEP instance id
 * @param name error name
 * @param errorType error variance type
 * @param errorCode error variance code
 * @param errorDescription error variance description
 * @param errorSeverity error variance severity level
 * @param errorStatus error variance status
 */
public final class StepErrorDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String errorType;
    private final String errorCode;
    private final String errorDescription;
    private final int errorSeverity;
    private final String errorStatus;

    public StepErrorDefinition(int id, String name, String errorType, String errorCode, String errorDescription, int errorSeverity, String errorStatus) {
        this.id = id;
        this.name = name;
        this.errorType = errorType;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.errorSeverity = errorSeverity;
        this.errorStatus = errorStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public int getErrorSeverity() {
        return errorSeverity;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepErrorDefinition that = (StepErrorDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(errorType, that.errorType) && Objects.equals(errorCode, that.errorCode) && Objects.equals(errorDescription, that.errorDescription) && errorSeverity == that.errorSeverity && Objects.equals(errorStatus, that.errorStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, errorType, errorCode, errorDescription, errorSeverity, errorStatus);
    }

    @Override
    public String toString() {
        return "StepErrorDefinition{" + "id=" + id + "name=" + name + "errorType=" + errorType + "errorCode=" + errorCode + "errorDescription=" + errorDescription + "errorSeverity=" + errorSeverity + "errorStatus=" + errorStatus + "}";
    }
}