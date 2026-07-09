package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved LOAD.
 * A finite element analysis load entity.
 */
/**
 * Resolved LOAD.
 * A finite element analysis load entity.
 */
public final class StepFeaLoad implements StepEntity {
    private final int id;
    private final String name;
    private final String loadType;
    private final StepEntity appliedTo;
    private final double magnitude;

    public StepFeaLoad(int id, String name, String loadType, StepEntity appliedTo, double magnitude) {
        this.id = id;
        this.name = name;
        this.loadType = loadType;
        this.appliedTo = appliedTo;
        this.magnitude = magnitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLoadType() {
        return loadType;
    }

    public StepEntity getAppliedTo() {
        return appliedTo;
    }

    public double getMagnitude() {
        return magnitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaLoad that = (StepFeaLoad) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(loadType, that.loadType) && Objects.equals(appliedTo, that.appliedTo) && magnitude == that.magnitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, loadType, appliedTo, magnitude);
    }

    @Override
    public String toString() {
        return "StepFeaLoad{" + "id=" + id + "name=" + name + "loadType=" + loadType + "appliedTo=" + appliedTo + "magnitude=" + magnitude + "}";
    }
}
