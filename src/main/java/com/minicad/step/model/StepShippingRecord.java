package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SHIPPING_RECORD.
 * A shipping record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItems shipped variance items
 * @varianceQuantities shipped variance quantities
 * @varianceCustomer customer variance reference
 * @varianceDate shipping variance date
 * @varianceCarrier carrier variance reference
 * @varianceTracking tracking variance number
 * @varianceStatus shipping variance status
 */
/**
 * Resolved SHIPPING_RECORD.
 * A shipping record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItems shipped variance items
 * @varianceQuantities shipped variance quantities
 * @varianceCustomer customer variance reference
 * @varianceDate shipping variance date
 * @varianceCarrier carrier variance reference
 * @varianceTracking tracking variance number
 * @varianceStatus shipping variance status
 */
public final class StepShippingRecord implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> varianceItems;
    private final List<Integer> varianceQuantities;
    private final StepEntity varianceCustomer;
    private final StepEntity varianceDate;
    private final StepEntity varianceCarrier;
    private final String varianceTracking;
    private final String varianceStatus;

    public StepShippingRecord(int id, String name, List<StepEntity> varianceItems, List<Integer> varianceQuantities, StepEntity varianceCustomer, StepEntity varianceDate, StepEntity varianceCarrier, String varianceTracking, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceQuantities = varianceQuantities == null ? null : java.util.List.copyOf(varianceQuantities);
        this.varianceCustomer = varianceCustomer;
        this.varianceDate = varianceDate;
        this.varianceCarrier = varianceCarrier;
        this.varianceTracking = varianceTracking;
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

    public StepEntity getVarianceCustomer() {
        return varianceCustomer;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getVarianceCarrier() {
        return varianceCarrier;
    }

    public String getVarianceTracking() {
        return varianceTracking;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShippingRecord that = (StepShippingRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItems, that.varianceItems) && Objects.equals(varianceQuantities, that.varianceQuantities) && Objects.equals(varianceCustomer, that.varianceCustomer) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceCarrier, that.varianceCarrier) && Objects.equals(varianceTracking, that.varianceTracking) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItems, varianceQuantities, varianceCustomer, varianceDate, varianceCarrier, varianceTracking, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepShippingRecord{" + "id=" + id + "name=" + name + "varianceItems=" + varianceItems + "varianceQuantities=" + varianceQuantities + "varianceCustomer=" + varianceCustomer + "varianceDate=" + varianceDate + "varianceCarrier=" + varianceCarrier + "varianceTracking=" + varianceTracking + "varianceStatus=" + varianceStatus + "}";
    }
}