package com.minicad.step.model;

import java.util.List;

/**
 * Resolved INTERFACE_SPECIFICATION.
 * An interface specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceInterface interface variance reference
 * @varianceMechanical mechanical variance requirements
 * @varianceElectrical electrical variance requirements
 * @varianceData data variance requirements
 * @varianceStandard interface variance standard reference
 * @varianceStatus specification variance status
 */
public record StepInterfaceSpecification(
    int id,
    String name,
    StepEntity varianceInterface,
    List<StepEntity> varianceMechanical,
    List<StepEntity> varianceElectrical,
    List<StepEntity> varianceData,
    String varianceStandard,
    String varianceStatus) implements StepEntity {
}