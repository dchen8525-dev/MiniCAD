package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COATING_REPRESENTATION_ITEM.
 * A coating representation item entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param coatingType coating variance type
 * @param coatingThickness coating variance thickness
 * @param coatingUnit coating variance unit reference
 * @param coatingStatus coating variance status
 */
/**
 * Resolved COATING_REPRESENTATION_ITEM.
 * A coating representation item entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param coatingType coating variance type
 * @param coatingThickness coating variance thickness
 * @param coatingUnit coating variance unit reference
 * @param coatingStatus coating variance status
 */
public final class StepCoatingRepresentationItem implements StepEntity {
    private final int id;
    private final String name;
    private final String coatingType;
    private final double coatingThickness;
    private final StepEntity coatingUnit;
    private final String coatingStatus;

    public StepCoatingRepresentationItem(int id, String name, String coatingType, double coatingThickness, StepEntity coatingUnit, String coatingStatus) {
        this.id = id;
        this.name = name;
        this.coatingType = coatingType;
        this.coatingThickness = coatingThickness;
        this.coatingUnit = coatingUnit;
        this.coatingStatus = coatingStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCoatingType() {
        return coatingType;
    }

    public double getCoatingThickness() {
        return coatingThickness;
    }

    public StepEntity getCoatingUnit() {
        return coatingUnit;
    }

    public String getCoatingStatus() {
        return coatingStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCoatingRepresentationItem that = (StepCoatingRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(coatingType, that.coatingType) && coatingThickness == that.coatingThickness && Objects.equals(coatingUnit, that.coatingUnit) && Objects.equals(coatingStatus, that.coatingStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coatingType, coatingThickness, coatingUnit, coatingStatus);
    }

    @Override
    public String toString() {
        return "StepCoatingRepresentationItem{" + "id=" + id + "name=" + name + "coatingType=" + coatingType + "coatingThickness=" + coatingThickness + "coatingUnit=" + coatingUnit + "coatingStatus=" + coatingStatus + "}";
    }
}