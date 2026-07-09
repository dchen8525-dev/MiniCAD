package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCostDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String costType;
    private final String costCategory;
    private final List<String> costElements;
    private final StepEntity costCurrency;
    private final String costStatus;

    public StepCostDefinition(int id, String name, String costType, String costCategory, List<String> costElements, StepEntity costCurrency, String costStatus) {
        this.id = id;
        this.name = name;
        this.costType = costType;
        this.costCategory = costCategory;
        this.costElements = costElements == null ? null : java.util.List.copyOf(costElements);
        this.costCurrency = costCurrency;
        this.costStatus = costStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCostDefinition that = (StepCostDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(costType, that.costType) && Objects.equals(costCategory, that.costCategory) && Objects.equals(costElements, that.costElements) && Objects.equals(costCurrency, that.costCurrency) && Objects.equals(costStatus, that.costStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, costType, costCategory, costElements, costCurrency, costStatus);
    }

    @Override
    public String toString() {
        return "StepCostDefinition{" + "id=" + id + "name=" + name + "costType=" + costType + "costCategory=" + costCategory + "costElements=" + costElements + "costCurrency=" + costCurrency + "costStatus=" + costStatus + "}";
    }
}