package com.minicad.step.model.system;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepExecutionContext implements StepEntity {
    private final int id;
    private final String name;
    private final String contextType;
    private final List<String> contextVariables;
    private final StepEntity contextParent;
    private final int contextDepth;
    private final String contextStatus;

    public StepExecutionContext(int id, String name, String contextType, List<String> contextVariables, StepEntity contextParent, int contextDepth, String contextStatus) {
        this.id = id;
        this.name = name;
        this.contextType = contextType;
        this.contextVariables = contextVariables == null ? null : java.util.List.copyOf(contextVariables);
        this.contextParent = contextParent;
        this.contextDepth = contextDepth;
        this.contextStatus = contextStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExecutionContext that = (StepExecutionContext) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(contextType, that.contextType) && Objects.equals(contextVariables, that.contextVariables) && Objects.equals(contextParent, that.contextParent) && contextDepth == that.contextDepth && Objects.equals(contextStatus, that.contextStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contextType, contextVariables, contextParent, contextDepth, contextStatus);
    }

    @Override
    public String toString() {
        return "StepExecutionContext{" + "id=" + id + "name=" + name + "contextType=" + contextType + "contextVariables=" + contextVariables + "contextParent=" + contextParent + "contextDepth=" + contextDepth + "contextStatus=" + contextStatus + "}";
    }
}