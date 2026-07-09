package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved BUCKLING_ANALYSIS.
 * Buckling analysis type for FEA.
 */
/**
 * Resolved BUCKLING_ANALYSIS.
 * Buckling analysis type for FEA.
 */
public final class StepBucklingAnalysis implements StepEntity {
    private final int id;
    private final String name;
    private final int numberOfModes;

    public StepBucklingAnalysis(int id, String name, int numberOfModes) {
        this.id = id;
        this.name = name;
        this.numberOfModes = numberOfModes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfModes() {
        return numberOfModes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBucklingAnalysis that = (StepBucklingAnalysis) o;
        return id == that.id && Objects.equals(name, that.name) && numberOfModes == that.numberOfModes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, numberOfModes);
    }

    @Override
    public String toString() {
        return "StepBucklingAnalysis{" + "id=" + id + "name=" + name + "numberOfModes=" + numberOfModes + "}";
    }
}
