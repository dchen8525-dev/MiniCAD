package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepFilterDefinition extends AbstractStepEntity {
    private final String filterType;
    private final String filterExpression;
    private final List<String> filterConditions;
    private final int filterPriority;
    private final String filterStatus;

    public StepFilterDefinition(int id, String name, String filterType, String filterExpression, List<String> filterConditions, int filterPriority, String filterStatus) {
        super(id, name);
        this.filterType = filterType;
        this.filterExpression = filterExpression;
        this.filterConditions = filterConditions == null ? null : java.util.List.copyOf(filterConditions);
        this.filterPriority = filterPriority;
        this.filterStatus = filterStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("filterType", filterType);
        state.put("filterExpression", filterExpression);
        state.put("filterConditions", filterConditions);
        state.put("filterPriority", filterPriority);
        state.put("filterStatus", filterStatus);
        return state;
    }
}
