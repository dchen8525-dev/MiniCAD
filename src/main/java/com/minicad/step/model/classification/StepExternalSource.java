package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal EXTERNAL_SOURCE metadata.
 *
 * @param id STEP instance id
 * @param sourceId external source identifier
 */
/**
 * Minimal EXTERNAL_SOURCE metadata.
 *
 * @param id STEP instance id
 * @param sourceId external source identifier
 */
public final class StepExternalSource implements StepEntity {
    private final int id;
    private final String sourceId;

    public StepExternalSource(int id, String sourceId) {
        this.id = id;
        this.sourceId = sourceId;
    }

    public int getId() {
        return id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getName() {
        return sourceId != null ? sourceId : "";
    }

    // Record-style accessor
    public String sourceId() {
        return sourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExternalSource that = (StepExternalSource) o;
        return id == that.id && Objects.equals(sourceId, that.sourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceId);
    }

    @Override
    public String toString() {
        return "StepExternalSource{" + "id=" + id + "sourceId=" + sourceId + "}";
    }
}
