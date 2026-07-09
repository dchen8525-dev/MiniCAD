package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved THERMAL_ANALYSIS.
 * Thermal analysis type for FEA.
 */
/**
 * Resolved THERMAL_ANALYSIS.
 * Thermal analysis type for FEA.
 */
public final class StepThermalAnalysis implements StepEntity {
    private final int id;
    private final String name;
    private final String thermalAnalysisType;

    public StepThermalAnalysis(int id, String name, String thermalAnalysisType) {
        this.id = id;
        this.name = name;
        this.thermalAnalysisType = thermalAnalysisType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThermalAnalysisType() {
        return thermalAnalysisType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepThermalAnalysis that = (StepThermalAnalysis) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(thermalAnalysisType, that.thermalAnalysisType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, thermalAnalysisType);
    }

    @Override
    public String toString() {
        return "StepThermalAnalysis{" + "id=" + id + "name=" + name + "thermalAnalysisType=" + thermalAnalysisType + "}";
    }
}
