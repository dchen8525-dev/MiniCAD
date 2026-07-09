package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FILTER_FEATURE.
 * A filter feature entity.
 *
 * @param id STEP instance id
 * @param name filter name
 * @param filterType filter type (air, liquid, magnetic)
 * @param filterGeometry filter geometry representation
 * @param filterMedia filter media specification
 * @varianceMicron variance micron rating
 * @varianceFlow variance flow capacity
 * @param replacementInterval replacement interval specification
 */
/**
 * Resolved FILTER_FEATURE.
 * A filter feature entity.
 *
 * @param id STEP instance id
 * @param name filter name
 * @param filterType filter type (air, liquid, magnetic)
 * @param filterGeometry filter geometry representation
 * @param filterMedia filter media specification
 * @varianceMicron variance micron rating
 * @varianceFlow variance flow capacity
 * @param replacementInterval replacement interval specification
 */
public final class StepFilterFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String filterType;
    private final StepEntity filterGeometry;
    private final StepEntity filterMedia;
    private final double varianceMicron;
    private final double varianceFlow;
    private final String replacementInterval;

    public StepFilterFeature(int id, String name, String filterType, StepEntity filterGeometry, StepEntity filterMedia, double varianceMicron, double varianceFlow, String replacementInterval) {
        this.id = id;
        this.name = name;
        this.filterType = filterType;
        this.filterGeometry = filterGeometry;
        this.filterMedia = filterMedia;
        this.varianceMicron = varianceMicron;
        this.varianceFlow = varianceFlow;
        this.replacementInterval = replacementInterval;
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

    public StepEntity getFilterGeometry() {
        return filterGeometry;
    }

    public StepEntity getFilterMedia() {
        return filterMedia;
    }

    public double getVarianceMicron() {
        return varianceMicron;
    }

    public double getVarianceFlow() {
        return varianceFlow;
    }

    public String getReplacementInterval() {
        return replacementInterval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFilterFeature that = (StepFilterFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(filterType, that.filterType) && Objects.equals(filterGeometry, that.filterGeometry) && Objects.equals(filterMedia, that.filterMedia) && varianceMicron == that.varianceMicron && varianceFlow == that.varianceFlow && Objects.equals(replacementInterval, that.replacementInterval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, filterType, filterGeometry, filterMedia, varianceMicron, varianceFlow, replacementInterval);
    }

    @Override
    public String toString() {
        return "StepFilterFeature{" + "id=" + id + "name=" + name + "filterType=" + filterType + "filterGeometry=" + filterGeometry + "filterMedia=" + filterMedia + "varianceMicron=" + varianceMicron + "varianceFlow=" + varianceFlow + "replacementInterval=" + replacementInterval + "}";
    }
}