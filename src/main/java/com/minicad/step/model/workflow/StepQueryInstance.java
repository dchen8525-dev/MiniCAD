package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved QUERY_INSTANCE.
 * A query instance entity.
 *
 * @param id STEP instance id
 * @param name query instance name
 * @param queryDefinition query variance definition reference
 * @param queryState query variance state
 * @param queryParameters query variance parameter values
 * @param queryResult query variance result count
 * @param queryExecutionTime query variance execution time
 * @param queryStatus query variance status
 */
public record StepQueryInstance(
    int id,
    String name,
    StepEntity queryDefinition,
    String queryState,
    List<String> queryParameters,
    int queryResult,
    double queryExecutionTime,
    String queryStatus) implements StepEntity {
}