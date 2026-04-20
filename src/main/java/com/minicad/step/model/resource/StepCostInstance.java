package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COST_INSTANCE.
 * A cost instance entity.
 *
 * @param id STEP instance id
 * @param name cost instance name
 * @param costDefinition cost variance definition reference
 * @param costPlanned cost variance planned amount
 * @param costActual cost variance actual amount
 * @param costVariance cost variance difference
 * @param costBreakdown cost variance breakdown details
 * @param costStatus cost variance status
 */
public record StepCostInstance(
    int id,
    String name,
    StepEntity costDefinition,
    double costPlanned,
    double costActual,
    double costVariance,
    List<String> costBreakdown,
    String costStatus) implements StepEntity {
}