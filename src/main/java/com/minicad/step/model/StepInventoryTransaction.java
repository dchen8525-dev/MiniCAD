package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepInventoryTransaction extends AbstractStepEntity {
    private final String transactionType;
    private final StepEntity varianceItem;
    private final int varianceQuantity;
    private final String varianceFrom;
    private final String varianceTo;
    private final StepEntity varianceDate;
    private final String varianceReason;
    private final String varianceStatus;

    public StepInventoryTransaction(int id, String name, String transactionType, StepEntity varianceItem, int varianceQuantity, String varianceFrom, String varianceTo, StepEntity varianceDate, String varianceReason, String varianceStatus) {
        super(id, name);
        this.transactionType = transactionType;
        this.varianceItem = varianceItem;
        this.varianceQuantity = varianceQuantity;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceDate = varianceDate;
        this.varianceReason = varianceReason;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("transactionType", transactionType);
        state.put("varianceItem", varianceItem);
        state.put("varianceQuantity", varianceQuantity);
        state.put("varianceFrom", varianceFrom);
        state.put("varianceTo", varianceTo);
        state.put("varianceDate", varianceDate);
        state.put("varianceReason", varianceReason);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
