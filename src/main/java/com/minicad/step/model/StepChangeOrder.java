package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CHANGE_ORDER.
 * A change order entity.
 *
 * @param id STEP instance id
 * @param name order name
 * @param orderNumber change order number
 * @param changeRequest reference change request
 * @param affectedItems items affected by change order
 * @param orderStatus order status (issued, implemented, closed)
 * @varianceDate variance/implementation date
 * @param orderApprover order approver reference
 * @param implementationPlan implementation plan reference
 */
/**
 * Resolved CHANGE_ORDER.
 * A change order entity.
 *
 * @param id STEP instance id
 * @param name order name
 * @param orderNumber change order number
 * @param changeRequest reference change request
 * @param affectedItems items affected by change order
 * @param orderStatus order status (issued, implemented, closed)
 * @varianceDate variance/implementation date
 * @param orderApprover order approver reference
 * @param implementationPlan implementation plan reference
 */
public final class StepChangeOrder implements StepEntity {
    private final int id;
    private final String name;
    private final String orderNumber;
    private final StepEntity changeRequest;
    private final List<StepEntity> affectedItems;
    private final String orderStatus;
    private final StepEntity varianceDate;
    private final StepEntity orderApprover;
    private final StepEntity implementationPlan;

    public StepChangeOrder(int id, String name, String orderNumber, StepEntity changeRequest, List<StepEntity> affectedItems, String orderStatus, StepEntity varianceDate, StepEntity orderApprover, StepEntity implementationPlan) {
        this.id = id;
        this.name = name;
        this.orderNumber = orderNumber;
        this.changeRequest = changeRequest;
        this.affectedItems = affectedItems == null ? null : java.util.List.copyOf(affectedItems);
        this.orderStatus = orderStatus;
        this.varianceDate = varianceDate;
        this.orderApprover = orderApprover;
        this.implementationPlan = implementationPlan;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public StepEntity getChangeRequest() {
        return changeRequest;
    }

    public List<StepEntity> getAffectedItems() {
        return affectedItems;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getOrderApprover() {
        return orderApprover;
    }

    public StepEntity getImplementationPlan() {
        return implementationPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepChangeOrder that = (StepChangeOrder) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(orderNumber, that.orderNumber) && Objects.equals(changeRequest, that.changeRequest) && Objects.equals(affectedItems, that.affectedItems) && Objects.equals(orderStatus, that.orderStatus) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(orderApprover, that.orderApprover) && Objects.equals(implementationPlan, that.implementationPlan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, orderNumber, changeRequest, affectedItems, orderStatus, varianceDate, orderApprover, implementationPlan);
    }

    @Override
    public String toString() {
        return "StepChangeOrder{" + "id=" + id + "name=" + name + "orderNumber=" + orderNumber + "changeRequest=" + changeRequest + "affectedItems=" + affectedItems + "orderStatus=" + orderStatus + "varianceDate=" + varianceDate + "orderApprover=" + orderApprover + "implementationPlan=" + implementationPlan + "}";
    }
}