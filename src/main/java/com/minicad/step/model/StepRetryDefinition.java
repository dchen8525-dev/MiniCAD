package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RETRY_DEFINITION.
 * A retry definition entity.
 *
 * @param id STEP instance id
 * @param name retry name
 * @param retryType retry variance type
 * @param retryMaxAttempts retry variance max attempts
 * @param retryDelay retry variance delay between attempts
 * @param retryBackoff retry variance backoff strategy
 * @param retryStatus retry variance status
 */
/**
 * Resolved RETRY_DEFINITION.
 * A retry definition entity.
 *
 * @param id STEP instance id
 * @param name retry name
 * @param retryType retry variance type
 * @param retryMaxAttempts retry variance max attempts
 * @param retryDelay retry variance delay between attempts
 * @param retryBackoff retry variance backoff strategy
 * @param retryStatus retry variance status
 */
public final class StepRetryDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String retryType;
    private final int retryMaxAttempts;
    private final int retryDelay;
    private final String retryBackoff;
    private final String retryStatus;

    public StepRetryDefinition(int id, String name, String retryType, int retryMaxAttempts, int retryDelay, String retryBackoff, String retryStatus) {
        this.id = id;
        this.name = name;
        this.retryType = retryType;
        this.retryMaxAttempts = retryMaxAttempts;
        this.retryDelay = retryDelay;
        this.retryBackoff = retryBackoff;
        this.retryStatus = retryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRetryType() {
        return retryType;
    }

    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    public int getRetryDelay() {
        return retryDelay;
    }

    public String getRetryBackoff() {
        return retryBackoff;
    }

    public String getRetryStatus() {
        return retryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRetryDefinition that = (StepRetryDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(retryType, that.retryType) && retryMaxAttempts == that.retryMaxAttempts && retryDelay == that.retryDelay && Objects.equals(retryBackoff, that.retryBackoff) && Objects.equals(retryStatus, that.retryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, retryType, retryMaxAttempts, retryDelay, retryBackoff, retryStatus);
    }

    @Override
    public String toString() {
        return "StepRetryDefinition{" + "id=" + id + "name=" + name + "retryType=" + retryType + "retryMaxAttempts=" + retryMaxAttempts + "retryDelay=" + retryDelay + "retryBackoff=" + retryBackoff + "retryStatus=" + retryStatus + "}";
    }
}