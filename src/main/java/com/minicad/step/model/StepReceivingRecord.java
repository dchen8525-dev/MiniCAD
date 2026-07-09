package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RECEIVING_RECORD.
 * A receiving record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItems received variance items
 * @varianceQuantities received variance quantities
 * @varianceSupplier supplier variance reference
 * @varianceDate receiving variance date
 * @varianceCondition received variance condition
 * @varianceInspection inspection variance status
 * @varianceStatus receiving variance status
 */
/**
 * Resolved RECEIVING_RECORD.
 * A receiving record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItems received variance items
 * @varianceQuantities received variance quantities
 * @varianceSupplier supplier variance reference
 * @varianceDate receiving variance date
 * @varianceCondition received variance condition
 * @varianceInspection inspection variance status
 * @varianceStatus receiving variance status
 */
public final class StepReceivingRecord implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> varianceItems;
    private final List<Integer> varianceQuantities;
    private final StepEntity varianceSupplier;
    private final StepEntity varianceDate;
    private final String varianceCondition;
    private final String varianceInspection;
    private final String varianceStatus;

    public StepReceivingRecord(int id, String name, List<StepEntity> varianceItems, List<Integer> varianceQuantities, StepEntity varianceSupplier, StepEntity varianceDate, String varianceCondition, String varianceInspection, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceQuantities = varianceQuantities == null ? null : java.util.List.copyOf(varianceQuantities);
        this.varianceSupplier = varianceSupplier;
        this.varianceDate = varianceDate;
        this.varianceCondition = varianceCondition;
        this.varianceInspection = varianceInspection;
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

    public List<Integer> getVarianceQuantities() {
        return varianceQuantities;
    }

    public StepEntity getVarianceSupplier() {
        return varianceSupplier;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceCondition() {
        return varianceCondition;
    }

    public String getVarianceInspection() {
        return varianceInspection;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepReceivingRecord that = (StepReceivingRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItems, that.varianceItems) && Objects.equals(varianceQuantities, that.varianceQuantities) && Objects.equals(varianceSupplier, that.varianceSupplier) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceCondition, that.varianceCondition) && Objects.equals(varianceInspection, that.varianceInspection) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItems, varianceQuantities, varianceSupplier, varianceDate, varianceCondition, varianceInspection, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepReceivingRecord{" + "id=" + id + "name=" + name + "varianceItems=" + varianceItems + "varianceQuantities=" + varianceQuantities + "varianceSupplier=" + varianceSupplier + "varianceDate=" + varianceDate + "varianceCondition=" + varianceCondition + "varianceInspection=" + varianceInspection + "varianceStatus=" + varianceStatus + "}";
    }
}