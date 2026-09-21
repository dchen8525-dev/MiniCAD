package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepChangeOrder extends AbstractStepEntity {
    private final String orderNumber;
    private final StepEntity changeRequest;
    private final List<StepEntity> affectedItems;
    private final String orderStatus;
    private final StepEntity varianceDate;
    private final StepEntity orderApprover;
    private final StepEntity implementationPlan;

    public StepChangeOrder(int id, String name, String orderNumber, StepEntity changeRequest, List<StepEntity> affectedItems, String orderStatus, StepEntity varianceDate, StepEntity orderApprover, StepEntity implementationPlan) {
        super(id, name);
        this.orderNumber = orderNumber;
        this.changeRequest = changeRequest;
        this.affectedItems = affectedItems == null ? null : java.util.List.copyOf(affectedItems);
        this.orderStatus = orderStatus;
        this.varianceDate = varianceDate;
        this.orderApprover = orderApprover;
        this.implementationPlan = implementationPlan;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("orderNumber", orderNumber);
        state.put("changeRequest", changeRequest);
        state.put("affectedItems", affectedItems);
        state.put("orderStatus", orderStatus);
        state.put("varianceDate", varianceDate);
        state.put("orderApprover", orderApprover);
        state.put("implementationPlan", implementationPlan);
        return state;
    }
}
