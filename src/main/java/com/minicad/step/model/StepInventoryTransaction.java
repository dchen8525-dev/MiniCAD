package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INVENTORY_TRANSACTION.
 * An inventory transaction entity.
 *
 * @param id STEP instance id
 * @param name transaction name
 * @param transactionType transaction type (receive, issue, transfer, adjust)
 * @varianceItem transaction variance item
 * @varianceQuantity transaction variance quantity
 * @varianceFrom source variance location
 * @varianceTo destination variance location
 * @varianceDate transaction variance date
 * @varianceReason transaction variance reason
 * @varianceStatus transaction variance status
 */
/**
 * Resolved INVENTORY_TRANSACTION.
 * An inventory transaction entity.
 *
 * @param id STEP instance id
 * @param name transaction name
 * @param transactionType transaction type (receive, issue, transfer, adjust)
 * @varianceItem transaction variance item
 * @varianceQuantity transaction variance quantity
 * @varianceFrom source variance location
 * @varianceTo destination variance location
 * @varianceDate transaction variance date
 * @varianceReason transaction variance reason
 * @varianceStatus transaction variance status
 */
public final class StepInventoryTransaction implements StepEntity {
    private final int id;
    private final String name;
    private final String transactionType;
    private final StepEntity varianceItem;
    private final int varianceQuantity;
    private final String varianceFrom;
    private final String varianceTo;
    private final StepEntity varianceDate;
    private final String varianceReason;
    private final String varianceStatus;

    public StepInventoryTransaction(int id, String name, String transactionType, StepEntity varianceItem, int varianceQuantity, String varianceFrom, String varianceTo, StepEntity varianceDate, String varianceReason, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.transactionType = transactionType;
        this.varianceItem = varianceItem;
        this.varianceQuantity = varianceQuantity;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceDate = varianceDate;
        this.varianceReason = varianceReason;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public int getVarianceQuantity() {
        return varianceQuantity;
    }

    public String getVarianceFrom() {
        return varianceFrom;
    }

    public String getVarianceTo() {
        return varianceTo;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceReason() {
        return varianceReason;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInventoryTransaction that = (StepInventoryTransaction) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(transactionType, that.transactionType) && Objects.equals(varianceItem, that.varianceItem) && varianceQuantity == that.varianceQuantity && Objects.equals(varianceFrom, that.varianceFrom) && Objects.equals(varianceTo, that.varianceTo) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceReason, that.varianceReason) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transactionType, varianceItem, varianceQuantity, varianceFrom, varianceTo, varianceDate, varianceReason, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepInventoryTransaction{" + "id=" + id + "name=" + name + "transactionType=" + transactionType + "varianceItem=" + varianceItem + "varianceQuantity=" + varianceQuantity + "varianceFrom=" + varianceFrom + "varianceTo=" + varianceTo + "varianceDate=" + varianceDate + "varianceReason=" + varianceReason + "varianceStatus=" + varianceStatus + "}";
    }
}