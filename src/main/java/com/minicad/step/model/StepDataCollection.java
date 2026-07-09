package com.minicad.step.model.profile_analysis.analysis;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepDataCollection implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> varianceItems;
    private final String varianceSource;
    private final String varianceMethod;
    private final String varianceFrequency;
    private final String varianceStatus;

    public StepDataCollection(int id, String name, List<StepEntity> varianceItems, String varianceSource, String varianceMethod, String varianceFrequency, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceSource = varianceSource;
        this.varianceMethod = varianceMethod;
        this.varianceFrequency = varianceFrequency;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDataCollection that = (StepDataCollection) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItems, that.varianceItems) && Objects.equals(varianceSource, that.varianceSource) && Objects.equals(varianceMethod, that.varianceMethod) && Objects.equals(varianceFrequency, that.varianceFrequency) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItems, varianceSource, varianceMethod, varianceFrequency, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepDataCollection{" + "id=" + id + "name=" + name + "varianceItems=" + varianceItems + "varianceSource=" + varianceSource + "varianceMethod=" + varianceMethod + "varianceFrequency=" + varianceFrequency + "varianceStatus=" + varianceStatus + "}";
    }
}