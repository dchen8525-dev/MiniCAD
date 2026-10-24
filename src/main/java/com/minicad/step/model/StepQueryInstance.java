package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepQueryInstance extends AbstractStepEntity {
    private final StepEntity queryDefinition;
    private final String queryState;
    private final List<String> queryParameters;
    private final int queryResult;
    private final double queryExecutionTime;
    private final String queryStatus;

    public StepQueryInstance(int id, String name, StepEntity queryDefinition, String queryState, List<String> queryParameters, int queryResult, double queryExecutionTime, String queryStatus) {
        super(id, name);
        this.queryDefinition = queryDefinition;
        this.queryState = queryState;
        this.queryParameters = queryParameters == null ? null : java.util.List.copyOf(queryParameters);
        this.queryResult = queryResult;
        this.queryExecutionTime = queryExecutionTime;
        this.queryStatus = queryStatus;
    }

    public StepEntity getQueryDefinition() {
        return queryDefinition;
    }

    public String getQueryState() {
        return queryState;
    }

    public List<String> getQueryParameters() {
        return queryParameters;
    }

    public int getQueryResult() {
        return queryResult;
    }

    public double getQueryExecutionTime() {
        return queryExecutionTime;
    }

    public String getQueryStatus() {
        return queryStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "queryDefinition", queryDefinition,
                "queryState", queryState, "queryParameters", queryParameters, "queryResult", queryResult,
                "queryExecutionTime", queryExecutionTime, "queryStatus", queryStatus);
    }
}
