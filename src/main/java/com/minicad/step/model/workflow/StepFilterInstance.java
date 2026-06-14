package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FILTER_INSTANCE.
 * A filter instance entity.
 *
 * @param id STEP instance id
 * @param name filter instance name
 * @param filterDefinition filter variance definition reference
 * @param filterState filter variance state
 * @param filterMatchCount filter variance match count
 * @param filterRejectCount filter variance reject count
 * @param filterStatus filter variance status
 */
/**
 * Resolved FILTER_INSTANCE.
 * A filter instance entity.
 *
 * @param id STEP instance id
 * @param name filter instance name
 * @param filterDefinition filter variance definition reference
 * @param filterState filter variance state
 * @param filterMatchCount filter variance match count
 * @param filterRejectCount filter variance reject count
 * @param filterStatus filter variance status
 */
public final class StepFilterInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity filterDefinition;
    private final String filterState;
    private final int filterMatchCount;
    private final int filterRejectCount;
    private final String filterStatus;

    public StepFilterInstance(int id, String name, StepEntity filterDefinition, String filterState, int filterMatchCount, int filterRejectCount, String filterStatus) {
        this.id = id;
        this.name = name;
        this.filterDefinition = filterDefinition;
        this.filterState = filterState;
        this.filterMatchCount = filterMatchCount;
        this.filterRejectCount = filterRejectCount;
        this.filterStatus = filterStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getFilterDefinition() {
        return filterDefinition;
    }

    public String getFilterState() {
        return filterState;
    }

    public int getFilterMatchCount() {
        return filterMatchCount;
    }

    public int getFilterRejectCount() {
        return filterRejectCount;
    }

    public String getFilterStatus() {
        return filterStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFilterInstance that = (StepFilterInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(filterDefinition, that.filterDefinition) && Objects.equals(filterState, that.filterState) && filterMatchCount == that.filterMatchCount && filterRejectCount == that.filterRejectCount && Objects.equals(filterStatus, that.filterStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, filterDefinition, filterState, filterMatchCount, filterRejectCount, filterStatus);
    }

    @Override
    public String toString() {
        return "StepFilterInstance{" + "id=" + id + "name=" + name + "filterDefinition=" + filterDefinition + "filterState=" + filterState + "filterMatchCount=" + filterMatchCount + "filterRejectCount=" + filterRejectCount + "filterStatus=" + filterStatus + "}";
    }
}