package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepChangeHistory extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final List<StepEntity> varianceChanges;
    private final StepEntity varianceCurrent;
    private final StepEntity varianceBaseline;
    private final String varianceStatus;

    public StepChangeHistory(int id, String name, StepEntity varianceItem, List<StepEntity> varianceChanges, StepEntity varianceCurrent, StepEntity varianceBaseline, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceChanges = varianceChanges == null ? null : java.util.List.copyOf(varianceChanges);
        this.varianceCurrent = varianceCurrent;
        this.varianceBaseline = varianceBaseline;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceItem", varianceItem);
        state.put("varianceChanges", varianceChanges);
        state.put("varianceCurrent", varianceCurrent);
        state.put("varianceBaseline", varianceBaseline);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
