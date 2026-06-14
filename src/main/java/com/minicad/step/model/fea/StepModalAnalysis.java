package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved MODAL_ANALYSIS.
 * Modal analysis type for FEA.
 */
/**
 * Resolved MODAL_ANALYSIS.
 * Modal analysis type for FEA.
 */
public final class StepModalAnalysis implements StepEntity {
    private final int id;
    private final String name;
    private final int numberOfModes;

    public StepModalAnalysis(int id, String name, int numberOfModes) {
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
        StepModalAnalysis that = (StepModalAnalysis) o;
        return id == that.id && Objects.equals(name, that.name) && numberOfModes == that.numberOfModes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, numberOfModes);
    }

    @Override
    public String toString() {
        return "StepModalAnalysis{" + "id=" + id + "name=" + name + "numberOfModes=" + numberOfModes + "}";
    }
}
