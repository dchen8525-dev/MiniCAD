package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved VALUE_REASON_PAIR.
 * A value-reason pair for classification.
 */
/**
 * Resolved VALUE_REASON_PAIR.
 * A value-reason pair for classification.
 */
public final class StepValueReasonPair implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity value;
    private final String reason;

    public StepValueReasonPair(int id, String name, StepEntity value, String reason) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getValue() {
        return value;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepValueReasonPair that = (StepValueReasonPair) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(value, that.value) && Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value, reason);
    }

    @Override
    public String toString() {
        return "StepValueReasonPair{" + "id=" + id + "name=" + name + "value=" + value + "reason=" + reason + "}";
    }
}
