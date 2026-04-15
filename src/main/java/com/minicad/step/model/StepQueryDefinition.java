package com.minicad.step.model;

import java.util.List;

/**
 * Resolved QUERY_DEFINITION.
 * A query definition entity.
 *
 * @param id STEP instance id
 * @param name query name
 * @param queryType query variance type
 * @param queryExpression query variance expression
 * @param queryParameters query variance parameters
 * @param queryReturnType query variance return type
 * @param queryStatus query variance status
 */
public record StepQueryDefinition(
    int id,
    String name,
    String queryType,
    String queryExpression,
    List<String> queryParameters,
    String queryReturnType,
    String queryStatus) implements StepEntity {
}