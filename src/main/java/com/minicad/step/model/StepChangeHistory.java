package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CHANGE_HISTORY.
 * A change history entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @varianceItem changed variance item
 * @varianceChanges change variance entries
 * @varianceCurrent current variance state
 * @varianceBaseline baseline variance reference
 * @varianceStatus history variance status
 */
/**
 * Resolved CHANGE_HISTORY.
 * A change history entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @varianceItem changed variance item
 * @varianceChanges change variance entries
 * @varianceCurrent current variance state
 * @varianceBaseline baseline variance reference
 * @varianceStatus history variance status
 */
public final class StepChangeHistory implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final List<StepEntity> varianceChanges;
    private final StepEntity varianceCurrent;
    private final StepEntity varianceBaseline;
    private final String varianceStatus;

    public StepChangeHistory(int id, String name, StepEntity varianceItem, List<StepEntity> varianceChanges, StepEntity varianceCurrent, StepEntity varianceBaseline, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceChanges = varianceChanges == null ? null : java.util.List.copyOf(varianceChanges);
        this.varianceCurrent = varianceCurrent;
        this.varianceBaseline = varianceBaseline;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public List<StepEntity> getVarianceChanges() {
        return varianceChanges;
    }

    public StepEntity getVarianceCurrent() {
        return varianceCurrent;
    }

    public StepEntity getVarianceBaseline() {
        return varianceBaseline;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepChangeHistory that = (StepChangeHistory) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceChanges, that.varianceChanges) && Objects.equals(varianceCurrent, that.varianceCurrent) && Objects.equals(varianceBaseline, that.varianceBaseline) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceChanges, varianceCurrent, varianceBaseline, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepChangeHistory{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceChanges=" + varianceChanges + "varianceCurrent=" + varianceCurrent + "varianceBaseline=" + varianceBaseline + "varianceStatus=" + varianceStatus + "}";
    }
}