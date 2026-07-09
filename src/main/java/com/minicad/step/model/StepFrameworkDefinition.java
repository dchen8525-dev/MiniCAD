package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FRAMEWORK_DEFINITION.
 * A framework definition entity.
 *
 * @param id STEP instance id
 * @param name framework name
 * @param frameworkType framework variance type
 * @param frameworkDescription framework variance description
 * @param frameworkModules framework variance module definitions
 * @param frameworkExtensions framework variance extension points
 * @param frameworkStatus framework variance status
 */
/**
 * Resolved FRAMEWORK_DEFINITION.
 * A framework definition entity.
 *
 * @param id STEP instance id
 * @param name framework name
 * @param frameworkType framework variance type
 * @param frameworkDescription framework variance description
 * @param frameworkModules framework variance module definitions
 * @param frameworkExtensions framework variance extension points
 * @param frameworkStatus framework variance status
 */
public final class StepFrameworkDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String frameworkType;
    private final String frameworkDescription;
    private final List<StepEntity> frameworkModules;
    private final List<String> frameworkExtensions;
    private final String frameworkStatus;

    public StepFrameworkDefinition(int id, String name, String frameworkType, String frameworkDescription, List<StepEntity> frameworkModules, List<String> frameworkExtensions, String frameworkStatus) {
        this.id = id;
        this.name = name;
        this.frameworkType = frameworkType;
        this.frameworkDescription = frameworkDescription;
        this.frameworkModules = frameworkModules == null ? null : java.util.List.copyOf(frameworkModules);
        this.frameworkExtensions = frameworkExtensions == null ? null : java.util.List.copyOf(frameworkExtensions);
        this.frameworkStatus = frameworkStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFrameworkType() {
        return frameworkType;
    }

    public String getFrameworkDescription() {
        return frameworkDescription;
    }

    public List<StepEntity> getFrameworkModules() {
        return frameworkModules;
    }

    public List<String> getFrameworkExtensions() {
        return frameworkExtensions;
    }

    public String getFrameworkStatus() {
        return frameworkStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFrameworkDefinition that = (StepFrameworkDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(frameworkType, that.frameworkType) && Objects.equals(frameworkDescription, that.frameworkDescription) && Objects.equals(frameworkModules, that.frameworkModules) && Objects.equals(frameworkExtensions, that.frameworkExtensions) && Objects.equals(frameworkStatus, that.frameworkStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, frameworkType, frameworkDescription, frameworkModules, frameworkExtensions, frameworkStatus);
    }

    @Override
    public String toString() {
        return "StepFrameworkDefinition{" + "id=" + id + "name=" + name + "frameworkType=" + frameworkType + "frameworkDescription=" + frameworkDescription + "frameworkModules=" + frameworkModules + "frameworkExtensions=" + frameworkExtensions + "frameworkStatus=" + frameworkStatus + "}";
    }
}