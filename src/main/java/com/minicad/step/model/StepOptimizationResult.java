package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepOptimizationResult implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity optimizedGeometry;
    private final List<Double> optimizedVariables;
    private final double objectiveValue;
    private final int iterationCount;
    private final String convergenceStatus;
    private final List<Boolean> constraintsMet;

    public StepOptimizationResult(int id, String name, StepEntity optimizedGeometry, List<Double> optimizedVariables, double objectiveValue, int iterationCount, String convergenceStatus, List<Boolean> constraintsMet) {
        this.id = id;
        this.name = name;
        this.optimizedGeometry = optimizedGeometry;
        this.optimizedVariables = optimizedVariables == null ? null : java.util.List.copyOf(optimizedVariables);
        this.objectiveValue = objectiveValue;
        this.iterationCount = iterationCount;
        this.convergenceStatus = convergenceStatus;
        this.constraintsMet = constraintsMet == null ? null : java.util.List.copyOf(constraintsMet);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOptimizationResult that = (StepOptimizationResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(optimizedGeometry, that.optimizedGeometry) && Objects.equals(optimizedVariables, that.optimizedVariables) && objectiveValue == that.objectiveValue && iterationCount == that.iterationCount && Objects.equals(convergenceStatus, that.convergenceStatus) && Objects.equals(constraintsMet, that.constraintsMet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, optimizedGeometry, optimizedVariables, objectiveValue, iterationCount, convergenceStatus, constraintsMet);
    }

    @Override
    public String toString() {
        return "StepOptimizationResult{" + "id=" + id + "name=" + name + "optimizedGeometry=" + optimizedGeometry + "optimizedVariables=" + optimizedVariables + "objectiveValue=" + objectiveValue + "iterationCount=" + iterationCount + "convergenceStatus=" + convergenceStatus + "constraintsMet=" + constraintsMet + "}";
    }
}