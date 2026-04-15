package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CLUSTER_INSTANCE.
 * A cluster instance entity.
 *
 * @param id STEP instance id
 * @param name cluster instance name
 * @param clusterDefinition cluster variance definition reference
 * @param clusterState cluster variance state
 * @param clusterNodeCount cluster variance active node count
 * @param clusterLoad cluster variance load level
 * @param clusterStatus cluster variance status
 */
public record StepClusterInstance(
    int id,
    String name,
    StepEntity clusterDefinition,
    String clusterState,
    int clusterNodeCount,
    double clusterLoad,
    String clusterStatus) implements StepEntity {
}