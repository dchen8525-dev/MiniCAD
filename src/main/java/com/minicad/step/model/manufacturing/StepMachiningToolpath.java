package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MACHINING_TOOLPATH.
 * A machining toolpath entity.
 *
 * @param id STEP instance id
 * @param name toolpath name
 * @param pathGeometry path geometry curve/curve set
 * @param tool tool used for this path
 * @param pathParameters path parameters (speed, feed, etc.)
 * @param approachStrategy approach strategy configuration
 */
/**
 * Resolved MACHINING_TOOLPATH.
 * A machining toolpath entity.
 *
 * @param id STEP instance id
 * @param name toolpath name
 * @param pathGeometry path geometry curve/curve set
 * @param tool tool used for this path
 * @param pathParameters path parameters (speed, feed, etc.)
 * @param approachStrategy approach strategy configuration
 */
public final class StepMachiningToolpath implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity pathGeometry;
    private final StepEntity tool;
    private final List<StepEntity> pathParameters;
    private final StepEntity approachStrategy;

    public StepMachiningToolpath(int id, String name, StepEntity pathGeometry, StepEntity tool, List<StepEntity> pathParameters, StepEntity approachStrategy) {
        this.id = id;
        this.name = name;
        this.pathGeometry = pathGeometry;
        this.tool = tool;
        this.pathParameters = pathParameters == null ? null : java.util.List.copyOf(pathParameters);
        this.approachStrategy = approachStrategy;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPathGeometry() {
        return pathGeometry;
    }

    public StepEntity getTool() {
        return tool;
    }

    public List<StepEntity> getPathParameters() {
        return pathParameters;
    }

    public StepEntity getApproachStrategy() {
        return approachStrategy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMachiningToolpath that = (StepMachiningToolpath) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(pathGeometry, that.pathGeometry) && Objects.equals(tool, that.tool) && Objects.equals(pathParameters, that.pathParameters) && Objects.equals(approachStrategy, that.approachStrategy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pathGeometry, tool, pathParameters, approachStrategy);
    }

    @Override
    public String toString() {
        return "StepMachiningToolpath{" + "id=" + id + "name=" + name + "pathGeometry=" + pathGeometry + "tool=" + tool + "pathParameters=" + pathParameters + "approachStrategy=" + approachStrategy + "}";
    }
}