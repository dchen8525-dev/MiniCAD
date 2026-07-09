package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved METRIC_DEFINITION.
 * A metric definition entity.
 *
 * @param id STEP instance id
 * @param name metric name
 * @param metricType metric variance type
 * @param metricUnit metric variance unit
 * @param metricRange metric variance valid range
 * @param metricFormula metric variance calculation formula
 * @param metricStatus metric variance status
 */
/**
 * Resolved METRIC_DEFINITION.
 * A metric definition entity.
 *
 * @param id STEP instance id
 * @param name metric name
 * @param metricType metric variance type
 * @param metricUnit metric variance unit
 * @param metricRange metric variance valid range
 * @param metricFormula metric variance calculation formula
 * @param metricStatus metric variance status
 */
public final class StepMetricDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String metricType;
    private final StepEntity metricUnit;
    private final List<Double> metricRange;
    private final String metricFormula;
    private final String metricStatus;

    public StepMetricDefinition(int id, String name, String metricType, StepEntity metricUnit, List<Double> metricRange, String metricFormula, String metricStatus) {
        this.id = id;
        this.name = name;
        this.metricType = metricType;
        this.metricUnit = metricUnit;
        this.metricRange = metricRange == null ? null : java.util.List.copyOf(metricRange);
        this.metricFormula = metricFormula;
        this.metricStatus = metricStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMetricType() {
        return metricType;
    }

    public StepEntity getMetricUnit() {
        return metricUnit;
    }

    public List<Double> getMetricRange() {
        return metricRange;
    }

    public String getMetricFormula() {
        return metricFormula;
    }

    public String getMetricStatus() {
        return metricStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMetricDefinition that = (StepMetricDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(metricType, that.metricType) && Objects.equals(metricUnit, that.metricUnit) && Objects.equals(metricRange, that.metricRange) && Objects.equals(metricFormula, that.metricFormula) && Objects.equals(metricStatus, that.metricStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, metricType, metricUnit, metricRange, metricFormula, metricStatus);
    }

    @Override
    public String toString() {
        return "StepMetricDefinition{" + "id=" + id + "name=" + name + "metricType=" + metricType + "metricUnit=" + metricUnit + "metricRange=" + metricRange + "metricFormula=" + metricFormula + "metricStatus=" + metricStatus + "}";
    }
}