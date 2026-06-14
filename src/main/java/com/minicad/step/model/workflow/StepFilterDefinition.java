package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FILTER_DEFINITION.
 * A filter definition entity.
 *
 * @param id STEP instance id
 * @param name filter name
 * @param filterType filter variance type
 * @param filterExpression filter variance expression
 * @param filterConditions filter variance conditions
 * @param filterPriority filter variance priority
 * @param filterStatus filter variance status
 */
/**
 * Resolved FILTER_DEFINITION.
 * A filter definition entity.
 *
 * @param id STEP instance id
 * @param name filter name
 * @param filterType filter variance type
 * @param filterExpression filter variance expression
 * @param filterConditions filter variance conditions
 * @param filterPriority filter variance priority
 * @param filterStatus filter variance status
 */
public final class StepFilterDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String filterType;
    private final String filterExpression;
    private final List<String> filterConditions;
    private final int filterPriority;
    private final String filterStatus;

    public StepFilterDefinition(int id, String name, String filterType, String filterExpression, List<String> filterConditions, int filterPriority, String filterStatus) {
        this.id = id;
        this.name = name;
        this.filterType = filterType;
        this.filterExpression = filterExpression;
        this.filterConditions = filterConditions == null ? null : java.util.List.copyOf(filterConditions);
        this.filterPriority = filterPriority;
        this.filterStatus = filterStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFilterType() {
        return filterType;
    }

    public String getFilterExpression() {
        return filterExpression;
    }

    public List<String> getFilterConditions() {
        return filterConditions;
    }

    public int getFilterPriority() {
        return filterPriority;
    }

    public String getFilterStatus() {
        return filterStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFilterDefinition that = (StepFilterDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(filterType, that.filterType) && Objects.equals(filterExpression, that.filterExpression) && Objects.equals(filterConditions, that.filterConditions) && filterPriority == that.filterPriority && Objects.equals(filterStatus, that.filterStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, filterType, filterExpression, filterConditions, filterPriority, filterStatus);
    }

    @Override
    public String toString() {
        return "StepFilterDefinition{" + "id=" + id + "name=" + name + "filterType=" + filterType + "filterExpression=" + filterExpression + "filterConditions=" + filterConditions + "filterPriority=" + filterPriority + "filterStatus=" + filterStatus + "}";
    }
}