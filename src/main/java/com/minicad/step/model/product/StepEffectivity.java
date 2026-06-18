package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal EFFECTIVITY metadata.
 *
 * @param id STEP instance id
 * @param effectivityId effectivity identifier
 */
/**
 * Minimal EFFECTIVITY metadata.
 *
 * @param id STEP instance id
 * @param effectivityId effectivity identifier
 */
public final class StepEffectivity implements StepEntity {
    private final int id;
    private final String effectivityId;

    public StepEffectivity(int id, String effectivityId) {
        this.id = id;
        this.effectivityId = effectivityId;
    }

    public int getId() {
        return id;
    }

    public String getEffectivityId() {
        return effectivityId;
    }

    public String getName() {
        return effectivityId != null ? effectivityId : "";
    }

    // Record-style accessor - name from effectivityId
    public String name() {
        return effectivityId;
    }

    public String effectivityId() {
        return effectivityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEffectivity that = (StepEffectivity) o;
        return id == that.id && Objects.equals(effectivityId, that.effectivityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, effectivityId);
    }

    @Override
    public String toString() {
        return "StepEffectivity{" + "id=" + id + "effectivityId=" + effectivityId + "}";
    }
}
