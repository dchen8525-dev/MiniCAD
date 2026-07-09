package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved WARRANTY_INFORMATION.
 * A warranty information entity.
 *
 * @param id STEP instance id
 * @param name warranty name
 * @param warrantyType warranty type (standard, extended, service)
 * @param warrantyPeriod warranty period duration
 * @param warrantyStart warranty start date
 * @param warrantyEnd warranty end date
 * @varianceConditions warranty variance conditions
 * @param warrantyProvider warranty provider reference
 * @param warrantyStatus warranty status (active, expired)
 */
/**
 * Resolved WARRANTY_INFORMATION.
 * A warranty information entity.
 *
 * @param id STEP instance id
 * @param name warranty name
 * @param warrantyType warranty type (standard, extended, service)
 * @param warrantyPeriod warranty period duration
 * @param warrantyStart warranty start date
 * @param warrantyEnd warranty end date
 * @varianceConditions warranty variance conditions
 * @param warrantyProvider warranty provider reference
 * @param warrantyStatus warranty status (active, expired)
 */
public final class StepWarrantyInformation implements StepEntity {
    private final int id;
    private final String name;
    private final String warrantyType;
    private final double warrantyPeriod;
    private final StepEntity warrantyStart;
    private final StepEntity warrantyEnd;
    private final List<String> varianceConditions;
    private final StepEntity warrantyProvider;
    private final String warrantyStatus;

    public StepWarrantyInformation(int id, String name, String warrantyType, double warrantyPeriod, StepEntity warrantyStart, StepEntity warrantyEnd, List<String> varianceConditions, StepEntity warrantyProvider, String warrantyStatus) {
        this.id = id;
        this.name = name;
        this.warrantyType = warrantyType;
        this.warrantyPeriod = warrantyPeriod;
        this.warrantyStart = warrantyStart;
        this.warrantyEnd = warrantyEnd;
        this.varianceConditions = varianceConditions == null ? null : java.util.List.copyOf(varianceConditions);
        this.warrantyProvider = warrantyProvider;
        this.warrantyStatus = warrantyStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWarrantyType() {
        return warrantyType;
    }

    public double getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public StepEntity getWarrantyStart() {
        return warrantyStart;
    }

    public StepEntity getWarrantyEnd() {
        return warrantyEnd;
    }

    public List<String> getVarianceConditions() {
        return varianceConditions;
    }

    public StepEntity getWarrantyProvider() {
        return warrantyProvider;
    }

    public String getWarrantyStatus() {
        return warrantyStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWarrantyInformation that = (StepWarrantyInformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(warrantyType, that.warrantyType) && warrantyPeriod == that.warrantyPeriod && Objects.equals(warrantyStart, that.warrantyStart) && Objects.equals(warrantyEnd, that.warrantyEnd) && Objects.equals(varianceConditions, that.varianceConditions) && Objects.equals(warrantyProvider, that.warrantyProvider) && Objects.equals(warrantyStatus, that.warrantyStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, warrantyType, warrantyPeriod, warrantyStart, warrantyEnd, varianceConditions, warrantyProvider, warrantyStatus);
    }

    @Override
    public String toString() {
        return "StepWarrantyInformation{" + "id=" + id + "name=" + name + "warrantyType=" + warrantyType + "warrantyPeriod=" + warrantyPeriod + "warrantyStart=" + warrantyStart + "warrantyEnd=" + warrantyEnd + "varianceConditions=" + varianceConditions + "warrantyProvider=" + warrantyProvider + "warrantyStatus=" + warrantyStatus + "}";
    }
}