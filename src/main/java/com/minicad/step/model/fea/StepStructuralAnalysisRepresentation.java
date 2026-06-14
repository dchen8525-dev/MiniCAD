package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;
/**
 * Resolved STRUCTURAL_ANALYSIS_REPRESENTATION.
 * A structural analysis representation.
 */
/**
 * Resolved STRUCTURAL_ANALYSIS_REPRESENTATION.
 * A structural analysis representation.
 */
public final class StepStructuralAnalysisRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final String analysisType;
    private final List<StepEntity> items;

    public StepStructuralAnalysisRepresentation(int id, String name, String analysisType, List<StepEntity> items) {
        this.id = id;
        this.name = name;
        this.analysisType = analysisType;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStructuralAnalysisRepresentation that = (StepStructuralAnalysisRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(analysisType, that.analysisType) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, analysisType, items);
    }

    @Override
    public String toString() {
        return "StepStructuralAnalysisRepresentation{" + "id=" + id + "name=" + name + "analysisType=" + analysisType + "items=" + items + "}";
    }
}
