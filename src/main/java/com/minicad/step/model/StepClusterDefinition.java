package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CLUSTER_DEFINITION.
 * A cluster definition entity.
 *
 * @param id STEP instance id
 * @param name cluster name
 * @param clusterType cluster variance type
 * @param clusterNodes cluster variance node definitions
 * @param clusterPolicy cluster variance policy
 * @param clusterCapacity cluster variance capacity
 * @param clusterStatus cluster variance status
 */
public record StepClusterDefinition(
    int id,
    String name,
    String clusterType,
    List<StepEntity> clusterNodes,
    String clusterPolicy,
    double clusterCapacity,
    String clusterStatus) implements StepEntity {
}