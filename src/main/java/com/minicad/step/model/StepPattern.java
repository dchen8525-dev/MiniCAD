package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved PATTERN.
 */
/**
 * Resolved PATTERN.
 */
public final class StepPattern implements StepEntity {
    private final int id;
    private final String name;
    private final String patternType;
    private final StepEntity seedElement;

    public StepPattern(int id, String name, String patternType, StepEntity seedElement) {
        this.id = id;
        this.name = name;
        this.patternType = patternType;
        this.seedElement = seedElement;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPatternType() {
        return patternType;
    }

    public StepEntity getSeedElement() {
        return seedElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPattern that = (StepPattern) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(patternType, that.patternType) && Objects.equals(seedElement, that.seedElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, patternType, seedElement);
    }

    @Override
    public String toString() {
        return "StepPattern{" + "id=" + id + "name=" + name + "patternType=" + patternType + "seedElement=" + seedElement + "}";
    }
}
