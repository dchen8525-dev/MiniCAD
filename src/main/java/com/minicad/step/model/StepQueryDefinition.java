package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepQueryDefinition extends AbstractStepEntity {
    private final String queryType;
    private final String queryExpression;
    private final List<String> queryParameters;
    private final String queryReturnType;
    private final String queryStatus;

    public StepQueryDefinition(int id, String name, String queryType, String queryExpression, List<String> queryParameters, String queryReturnType, String queryStatus) {
        super(id, name);
        this.queryType = queryType;
        this.queryExpression = queryExpression;
        this.queryParameters = queryParameters == null ? null : java.util.List.copyOf(queryParameters);
        this.queryReturnType = queryReturnType;
        this.queryStatus = queryStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("queryType", queryType);
        state.put("queryExpression", queryExpression);
        state.put("queryParameters", queryParameters);
        state.put("queryReturnType", queryReturnType);
        state.put("queryStatus", queryStatus);
        return state;
    }
}
