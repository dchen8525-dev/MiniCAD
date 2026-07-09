package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LINEAR_PATTERN.
 * Represents a linear pattern feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param baseFeature base feature being patterned
 * @param direction pattern direction
 * @param spacing spacing between features
 * @param count number of features
 */
/**
 * Resolved LINEAR_PATTERN.
 * Represents a linear pattern feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param baseFeature base feature being patterned
 * @param direction pattern direction
 * @param spacing spacing between features
 * @param count number of features
 */
public final class StepLinearPattern implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity baseFeature;
    private final StepEntity direction;
    private final Double spacing;
    private final Integer count;

    public StepLinearPattern(int id, String name, StepEntity baseFeature, StepEntity direction, Double spacing, Integer count) {
        this.id = id;
        this.name = name;
        this.baseFeature = baseFeature;
        this.direction = direction;
        this.spacing = spacing;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBaseFeature() {
        return baseFeature;
    }

    public StepEntity getDirection() {
        return direction;
    }

    public Double getSpacing() {
        return spacing;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLinearPattern that = (StepLinearPattern) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(baseFeature, that.baseFeature) && Objects.equals(direction, that.direction) && Objects.equals(spacing, that.spacing) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, baseFeature, direction, spacing, count);
    }

    @Override
    public String toString() {
        return "StepLinearPattern{" + "id=" + id + "name=" + name + "baseFeature=" + baseFeature + "direction=" + direction + "spacing=" + spacing + "count=" + count + "}";
    }
}