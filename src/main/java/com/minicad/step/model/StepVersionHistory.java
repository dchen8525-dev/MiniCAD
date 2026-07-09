package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved VERSION_HISTORY.
 * A version history entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @varianceItem versioned variance item
 * @varianceVersions version variance entries
 * @varianceCurrent current variance version reference
 * @varianceAuthor version variance author
 * @varianceStatus history variance status
 */
/**
 * Resolved VERSION_HISTORY.
 * A version history entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @varianceItem versioned variance item
 * @varianceVersions version variance entries
 * @varianceCurrent current variance version reference
 * @varianceAuthor version variance author
 * @varianceStatus history variance status
 */
public final class StepVersionHistory implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final List<StepEntity> varianceVersions;
    private final StepEntity varianceCurrent;
    private final StepEntity varianceAuthor;
    private final String varianceStatus;

    public StepVersionHistory(int id, String name, StepEntity varianceItem, List<StepEntity> varianceVersions, StepEntity varianceCurrent, StepEntity varianceAuthor, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceVersions = varianceVersions == null ? null : java.util.List.copyOf(varianceVersions);
        this.varianceCurrent = varianceCurrent;
        this.varianceAuthor = varianceAuthor;
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

    public List<StepEntity> getVarianceVersions() {
        return varianceVersions;
    }

    public StepEntity getVarianceCurrent() {
        return varianceCurrent;
    }

    public StepEntity getVarianceAuthor() {
        return varianceAuthor;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVersionHistory that = (StepVersionHistory) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceVersions, that.varianceVersions) && Objects.equals(varianceCurrent, that.varianceCurrent) && Objects.equals(varianceAuthor, that.varianceAuthor) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceVersions, varianceCurrent, varianceAuthor, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepVersionHistory{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceVersions=" + varianceVersions + "varianceCurrent=" + varianceCurrent + "varianceAuthor=" + varianceAuthor + "varianceStatus=" + varianceStatus + "}";
    }
}