package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepCostDefinition(
    int id,
    String name,
    String costType,
    String costCategory,
    List<String> costElements,
    StepEntity costCurrency,
    String costStatus) implements StepEntity {
}