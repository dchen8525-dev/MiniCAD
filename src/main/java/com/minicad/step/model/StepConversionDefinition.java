package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONVERSION_DEFINITION.
 * A conversion definition entity.
 *
 * @param id STEP instance id
 * @param name conversion name
 * @param conversionType conversion variance type
 * @param conversionSource conversion variance source unit/type
 * @param conversionTarget conversion variance target unit/type
 * @param conversionFactor conversion variance factor
 * @param conversionOffset conversion variance offset
 * @param conversionStatus conversion variance status
 */
/**
 * Resolved CONVERSION_DEFINITION.
 * A conversion definition entity.
 *
 * @param id STEP instance id
 * @param name conversion name
 * @param conversionType conversion variance type
 * @param conversionSource conversion variance source unit/type
 * @param conversionTarget conversion variance target unit/type
 * @param conversionFactor conversion variance factor
 * @param conversionOffset conversion variance offset
 * @param conversionStatus conversion variance status
 */
public final class StepConversionDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String conversionType;
    private final StepEntity conversionSource;
    private final StepEntity conversionTarget;
    private final double conversionFactor;
    private final double conversionOffset;
    private final String conversionStatus;

    public StepConversionDefinition(int id, String name, String conversionType, StepEntity conversionSource, StepEntity conversionTarget, double conversionFactor, double conversionOffset, String conversionStatus) {
        this.id = id;
        this.name = name;
        this.conversionType = conversionType;
        this.conversionSource = conversionSource;
        this.conversionTarget = conversionTarget;
        this.conversionFactor = conversionFactor;
        this.conversionOffset = conversionOffset;
        this.conversionStatus = conversionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConversionType() {
        return conversionType;
    }

    public StepEntity getConversionSource() {
        return conversionSource;
    }

    public StepEntity getConversionTarget() {
        return conversionTarget;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double getConversionOffset() {
        return conversionOffset;
    }

    public String getConversionStatus() {
        return conversionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConversionDefinition that = (StepConversionDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(conversionType, that.conversionType) && Objects.equals(conversionSource, that.conversionSource) && Objects.equals(conversionTarget, that.conversionTarget) && conversionFactor == that.conversionFactor && conversionOffset == that.conversionOffset && Objects.equals(conversionStatus, that.conversionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, conversionType, conversionSource, conversionTarget, conversionFactor, conversionOffset, conversionStatus);
    }

    @Override
    public String toString() {
        return "StepConversionDefinition{" + "id=" + id + "name=" + name + "conversionType=" + conversionType + "conversionSource=" + conversionSource + "conversionTarget=" + conversionTarget + "conversionFactor=" + conversionFactor + "conversionOffset=" + conversionOffset + "conversionStatus=" + conversionStatus + "}";
    }
}