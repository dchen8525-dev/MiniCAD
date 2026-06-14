package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepQueryDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String queryType;
    private final String queryExpression;
    private final List<String> queryParameters;
    private final String queryReturnType;
    private final String queryStatus;

    public StepQueryDefinition(int id, String name, String queryType, String queryExpression, List<String> queryParameters, String queryReturnType, String queryStatus) {
        this.id = id;
        this.name = name;
        this.queryType = queryType;
        this.queryExpression = queryExpression;
        this.queryParameters = queryParameters == null ? null : java.util.List.copyOf(queryParameters);
        this.queryReturnType = queryReturnType;
        this.queryStatus = queryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getQueryType() {
        return queryType;
    }

    public String getQueryExpression() {
        return queryExpression;
    }

    public List<String> getQueryParameters() {
        return queryParameters;
    }

    public String getQueryReturnType() {
        return queryReturnType;
    }

    public String getQueryStatus() {
        return queryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepQueryDefinition that = (StepQueryDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(queryType, that.queryType) && Objects.equals(queryExpression, that.queryExpression) && Objects.equals(queryParameters, that.queryParameters) && Objects.equals(queryReturnType, that.queryReturnType) && Objects.equals(queryStatus, that.queryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, queryType, queryExpression, queryParameters, queryReturnType, queryStatus);
    }

    @Override
    public String toString() {
        return "StepQueryDefinition{" + "id=" + id + "name=" + name + "queryType=" + queryType + "queryExpression=" + queryExpression + "queryParameters=" + queryParameters + "queryReturnType=" + queryReturnType + "queryStatus=" + queryStatus + "}";
    }
}