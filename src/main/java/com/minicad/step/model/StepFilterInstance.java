package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepFilterInstance extends AbstractStepEntity {
    private final StepEntity filterDefinition;
    private final String filterState;
    private final int filterMatchCount;
    private final int filterRejectCount;
    private final String filterStatus;

    public StepFilterInstance(int id, String name, StepEntity filterDefinition, String filterState, int filterMatchCount, int filterRejectCount, String filterStatus) {
        super(id, name);
        this.filterDefinition = filterDefinition;
        this.filterState = filterState;
        this.filterMatchCount = filterMatchCount;
        this.filterRejectCount = filterRejectCount;
        this.filterStatus = filterStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("filterDefinition", filterDefinition);
        state.put("filterState", filterState);
        state.put("filterMatchCount", filterMatchCount);
        state.put("filterRejectCount", filterRejectCount);
        state.put("filterStatus", filterStatus);
        return state;
    }
}
