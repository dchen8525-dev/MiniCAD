package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepVersionHistory extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final List<StepEntity> varianceVersions;
    private final StepEntity varianceCurrent;
    private final StepEntity varianceAuthor;
    private final String varianceStatus;

    public StepVersionHistory(int id, String name, StepEntity varianceItem, List<StepEntity> varianceVersions, StepEntity varianceCurrent, StepEntity varianceAuthor, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceVersions = varianceVersions == null ? null : java.util.List.copyOf(varianceVersions);
        this.varianceCurrent = varianceCurrent;
        this.varianceAuthor = varianceAuthor;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceItem", varianceItem);
        state.put("varianceVersions", varianceVersions);
        state.put("varianceCurrent", varianceCurrent);
        state.put("varianceAuthor", varianceAuthor);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
