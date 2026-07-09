package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LUBRICATION_FEATURE.
 * A lubrication feature entity.
 *
 * @param id STEP instance id
 * @param name lubrication name
 * @param lubricationType lubrication type (oil, grease, spray)
 * @param lubricationPoints lubrication point locations
 * @param lubricationMethod lubrication method specification
 * @param lubricationInterval lubrication interval/frequency
 * @param lubricantType lubricant type specification
 */
/**
 * Resolved LUBRICATION_FEATURE.
 * A lubrication feature entity.
 *
 * @param id STEP instance id
 * @param name lubrication name
 * @param lubricationType lubrication type (oil, grease, spray)
 * @param lubricationPoints lubrication point locations
 * @param lubricationMethod lubrication method specification
 * @param lubricationInterval lubrication interval/frequency
 * @param lubricantType lubricant type specification
 */
public final class StepLubricationFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String lubricationType;
    private final List<StepEntity> lubricationPoints;
    private final String lubricationMethod;
    private final String lubricationInterval;
    private final String lubricantType;

    public StepLubricationFeature(int id, String name, String lubricationType, List<StepEntity> lubricationPoints, String lubricationMethod, String lubricationInterval, String lubricantType) {
        this.id = id;
        this.name = name;
        this.lubricationType = lubricationType;
        this.lubricationPoints = lubricationPoints == null ? null : java.util.List.copyOf(lubricationPoints);
        this.lubricationMethod = lubricationMethod;
        this.lubricationInterval = lubricationInterval;
        this.lubricantType = lubricantType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLubricationType() {
        return lubricationType;
    }

    public List<StepEntity> getLubricationPoints() {
        return lubricationPoints;
    }

    public String getLubricationMethod() {
        return lubricationMethod;
    }

    public String getLubricationInterval() {
        return lubricationInterval;
    }

    public String getLubricantType() {
        return lubricantType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLubricationFeature that = (StepLubricationFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(lubricationType, that.lubricationType) && Objects.equals(lubricationPoints, that.lubricationPoints) && Objects.equals(lubricationMethod, that.lubricationMethod) && Objects.equals(lubricationInterval, that.lubricationInterval) && Objects.equals(lubricantType, that.lubricantType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lubricationType, lubricationPoints, lubricationMethod, lubricationInterval, lubricantType);
    }

    @Override
    public String toString() {
        return "StepLubricationFeature{" + "id=" + id + "name=" + name + "lubricationType=" + lubricationType + "lubricationPoints=" + lubricationPoints + "lubricationMethod=" + lubricationMethod + "lubricationInterval=" + lubricationInterval + "lubricantType=" + lubricantType + "}";
    }
}