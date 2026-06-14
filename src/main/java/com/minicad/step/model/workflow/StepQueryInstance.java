package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepQueryInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity queryDefinition;
    private final String queryState;
    private final List<String> queryParameters;
    private final int queryResult;
    private final double queryExecutionTime;
    private final String queryStatus;

    public StepQueryInstance(int id, String name, StepEntity queryDefinition, String queryState, List<String> queryParameters, int queryResult, double queryExecutionTime, String queryStatus) {
        this.id = id;
        this.name = name;
        this.queryDefinition = queryDefinition;
        this.queryState = queryState;
        this.queryParameters = queryParameters == null ? null : java.util.List.copyOf(queryParameters);
        this.queryResult = queryResult;
        this.queryExecutionTime = queryExecutionTime;
        this.queryStatus = queryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepQueryInstance that = (StepQueryInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(queryDefinition, that.queryDefinition) && Objects.equals(queryState, that.queryState) && Objects.equals(queryParameters, that.queryParameters) && queryResult == that.queryResult && queryExecutionTime == that.queryExecutionTime && Objects.equals(queryStatus, that.queryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, queryDefinition, queryState, queryParameters, queryResult, queryExecutionTime, queryStatus);
    }

    @Override
    public String toString() {
        return "StepQueryInstance{" + "id=" + id + "name=" + name + "queryDefinition=" + queryDefinition + "queryState=" + queryState + "queryParameters=" + queryParameters + "queryResult=" + queryResult + "queryExecutionTime=" + queryExecutionTime + "queryStatus=" + queryStatus + "}";
    }
}