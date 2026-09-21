package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved EXECUTION_CONTEXT.
 * An execution context entity.
 *
 * @param id STEP instance id
 * @param name context name
 * @param contextType context variance type
 * @param contextVariables context variance variable values
 * @param contextParent context variance parent context reference
 * @param contextDepth context variance nesting depth
 * @param contextStatus context variance status
 */
public final class StepExecutionContext extends AbstractStepEntity {
    private final String contextType;
    private final List<String> contextVariables;
    private final StepEntity contextParent;
    private final int contextDepth;
    private final String contextStatus;

    public StepExecutionContext(int id, String name, String contextType, List<String> contextVariables, StepEntity contextParent, int contextDepth, String contextStatus) {
        super(id, name);
        this.contextType = contextType;
        this.contextVariables = contextVariables == null ? null : java.util.List.copyOf(contextVariables);
        this.contextParent = contextParent;
        this.contextDepth = contextDepth;
        this.contextStatus = contextStatus;
    }

    public String getContextType() {
        return contextType;
    }

    public List<String> getContextVariables() {
        return contextVariables;
    }

    public StepEntity getContextParent() {
        return contextParent;
    }

    public int getContextDepth() {
        return contextDepth;
    }

    public String getContextStatus() {
        return contextStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("contextType", contextType);
        state.put("contextVariables", contextVariables);
        state.put("contextParent", contextParent);
        state.put("contextDepth", contextDepth);
        state.put("contextStatus", contextStatus);
        return state;
    }
}
