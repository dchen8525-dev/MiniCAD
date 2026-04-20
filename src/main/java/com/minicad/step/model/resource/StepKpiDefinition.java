package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved KPI_DEFINITION.
 * A KPI definition entity.
 *
 * @param id STEP instance id
 * @param name KPI name
 * @param kpiId KPI identifier
 * @varianceDescription KPI variance description
 * @varianceFormula KPI variance calculation formula
 * @varianceTarget target variance value
 * @varianceThreshold threshold variance values
 * @varianceUnit KPI variance unit
 * @varianceStatus KPI variance status
 */
public record StepKpiDefinition(
    int id,
    String name,
    String kpiId,
    String varianceDescription,
    String varianceFormula,
    double varianceTarget,
    List<Double> varianceThreshold,
    StepEntity varianceUnit,
    String varianceStatus) implements StepEntity {
}