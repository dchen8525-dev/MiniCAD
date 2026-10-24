package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved DATA_COLLECTION.
 * A data collection entity.
 *
 * @param id STEP instance id
 * @param name collection name
 * @varianceItems collected variance items
 * @varianceSource data variance source
 * @varianceMethod collection variance method
 * @varianceFrequency collection variance frequency
 * @varianceStatus collection variance status
 */
public final class StepDataCollection extends AbstractStepEntity {
    private final List<StepEntity> varianceItems;
    private final String varianceSource;
    private final String varianceMethod;
    private final String varianceFrequency;
    private final String varianceStatus;

    public StepDataCollection(int id, String name, List<StepEntity> varianceItems, String varianceSource, String varianceMethod, String varianceFrequency, String varianceStatus) {
        super(id, name);
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceSource = varianceSource;
        this.varianceMethod = varianceMethod;
        this.varianceFrequency = varianceFrequency;
        this.varianceStatus = varianceStatus;
    }

    public List<StepEntity> getVarianceItems() {
        return varianceItems;
    }

    public String getVarianceSource() {
        return varianceSource;
    }

    public String getVarianceMethod() {
        return varianceMethod;
    }

    public String getVarianceFrequency() {
        return varianceFrequency;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceItems", varianceItems,
                "varianceSource", varianceSource, "varianceMethod", varianceMethod,
                "varianceFrequency", varianceFrequency, "varianceStatus", varianceStatus);
    }
}
