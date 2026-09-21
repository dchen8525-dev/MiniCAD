package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COST_DEFINITION.
 * A cost definition entity.
 *
 * @param id STEP instance id
 * @param name cost name
 * @param costType cost variance type
 * @param costCategory cost variance category
 * @param costElements cost variance breakdown elements
 * @param costCurrency cost variance currency
 * @param costStatus cost variance status
 */
public final class StepCostDefinition extends AbstractStepEntity {
    private final String costType;
    private final String costCategory;
    private final List<String> costElements;
    private final StepEntity costCurrency;
    private final String costStatus;

    public StepCostDefinition(int id, String name, String costType, String costCategory, List<String> costElements, StepEntity costCurrency, String costStatus) {
        super(id, name);
        this.costType = costType;
        this.costCategory = costCategory;
        this.costElements = costElements == null ? null : java.util.List.copyOf(costElements);
        this.costCurrency = costCurrency;
        this.costStatus = costStatus;
    }

    public String getCostType() {
        return costType;
    }

    public String getCostCategory() {
        return costCategory;
    }

    public List<String> getCostElements() {
        return costElements;
    }

    public StepEntity getCostCurrency() {
        return costCurrency;
    }

    public String getCostStatus() {
        return costStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("costType", costType);
        state.put("costCategory", costCategory);
        state.put("costElements", costElements);
        state.put("costCurrency", costCurrency);
        state.put("costStatus", costStatus);
        return state;
    }
}
