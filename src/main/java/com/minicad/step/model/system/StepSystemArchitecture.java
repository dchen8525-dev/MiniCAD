package com.minicad.step.model.system;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SYSTEM_ARCHITECTURE.
 * A system architecture entity.
 *
 * @param id STEP instance id
 * @param name architecture name
 * @varianceComponents architecture variance components
 * @varianceConnections architecture variance connections
 * @varianceInterfaces architecture variance interfaces
 * @varianceHierarchy architecture variance hierarchy/levels
 * @varianceType architecture variance type (functional, physical, logical)
 * @varianceStatus architecture variance status
 */
public record StepSystemArchitecture(
    int id,
    String name,
    List<StepEntity> varianceComponents,
    List<StepEntity> varianceConnections,
    List<StepEntity> varianceInterfaces,
    int varianceHierarchy,
    String varianceType,
    String varianceStatus) implements StepEntity {
}