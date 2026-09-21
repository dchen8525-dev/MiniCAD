package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved OPTIMIZATION_RESULT.
 * An optimization result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @param optimizedGeometry optimized geometry result
 * @param optimizedVariables optimized variable values
 * @param objectiveValue achieved objective value
 * @param iterationCount number of optimization iterations
 * @param convergenceStatus convergence status (converged, not converged)
 * @param constraintsMet constraints satisfaction status
 */
public final class StepOptimizationResult extends AbstractStepEntity {
    private final StepEntity optimizedGeometry;
    private final List<Double> optimizedVariables;
    private final double objectiveValue;
    private final int iterationCount;
    private final String convergenceStatus;
    private final List<Boolean> constraintsMet;

    public StepOptimizationResult(int id, String name, StepEntity optimizedGeometry, List<Double> optimizedVariables, double objectiveValue, int iterationCount, String convergenceStatus, List<Boolean> constraintsMet) {
        super(id, name);
        this.optimizedGeometry = optimizedGeometry;
        this.optimizedVariables = optimizedVariables == null ? null : java.util.List.copyOf(optimizedVariables);
        this.objectiveValue = objectiveValue;
        this.iterationCount = iterationCount;
        this.convergenceStatus = convergenceStatus;
        this.constraintsMet = constraintsMet == null ? null : java.util.List.copyOf(constraintsMet);
    }

    public StepEntity getOptimizedGeometry() {
        return optimizedGeometry;
    }

    public List<Double> getOptimizedVariables() {
        return optimizedVariables;
    }

    public double getObjectiveValue() {
        return objectiveValue;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public String getConvergenceStatus() {
        return convergenceStatus;
    }

    public List<Boolean> getConstraintsMet() {
        return constraintsMet;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("optimizedGeometry", optimizedGeometry);
        state.put("optimizedVariables", optimizedVariables);
        state.put("objectiveValue", objectiveValue);
        state.put("iterationCount", iterationCount);
        state.put("convergenceStatus", convergenceStatus);
        state.put("constraintsMet", constraintsMet);
        return state;
    }
}
