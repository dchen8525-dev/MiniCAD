package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved EXCEPTION_DEFINITION.
 * An exception definition entity.
 *
 * @param id STEP instance id
 * @param name exception name
 * @param exceptionType exception variance type
 * @param exceptionCode exception variance code
 * @param exceptionDescription exception variance description
 * @param exceptionHandler exception variance handler reference
 * @param exceptionStatus exception variance status
 */
/**
 * Resolved EXCEPTION_DEFINITION.
 * An exception definition entity.
 *
 * @param id STEP instance id
 * @param name exception name
 * @param exceptionType exception variance type
 * @param exceptionCode exception variance code
 * @param exceptionDescription exception variance description
 * @param exceptionHandler exception variance handler reference
 * @param exceptionStatus exception variance status
 */
public final class StepExceptionDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String exceptionType;
    private final String exceptionCode;
    private final String exceptionDescription;
    private final StepEntity exceptionHandler;
    private final String exceptionStatus;

    public StepExceptionDefinition(int id, String name, String exceptionType, String exceptionCode, String exceptionDescription, StepEntity exceptionHandler, String exceptionStatus) {
        this.id = id;
        this.name = name;
        this.exceptionType = exceptionType;
        this.exceptionCode = exceptionCode;
        this.exceptionDescription = exceptionDescription;
        this.exceptionHandler = exceptionHandler;
        this.exceptionStatus = exceptionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public String getExceptionDescription() {
        return exceptionDescription;
    }

    public StepEntity getExceptionHandler() {
        return exceptionHandler;
    }

    public String getExceptionStatus() {
        return exceptionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExceptionDefinition that = (StepExceptionDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(exceptionType, that.exceptionType) && Objects.equals(exceptionCode, that.exceptionCode) && Objects.equals(exceptionDescription, that.exceptionDescription) && Objects.equals(exceptionHandler, that.exceptionHandler) && Objects.equals(exceptionStatus, that.exceptionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, exceptionType, exceptionCode, exceptionDescription, exceptionHandler, exceptionStatus);
    }

    @Override
    public String toString() {
        return "StepExceptionDefinition{" + "id=" + id + "name=" + name + "exceptionType=" + exceptionType + "exceptionCode=" + exceptionCode + "exceptionDescription=" + exceptionDescription + "exceptionHandler=" + exceptionHandler + "exceptionStatus=" + exceptionStatus + "}";
    }
}