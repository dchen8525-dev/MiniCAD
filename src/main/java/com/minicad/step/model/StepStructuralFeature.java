package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STRUCTURAL_FEATURE.
 * A structural feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param structuralType structural type (beam, column, plate, connection)
 * @param crossSection cross-section geometry
 * @param structuralLength length dimension
 * @param structuralMaterial material specification
 * @param endConditions end condition features
 * @param loadPoints load application points
 */
public final class StepStructuralFeature extends AbstractStepEntity {
    private final String structuralType;
    private final StepEntity crossSection;
    private final double structuralLength;
    private final StepEntity structuralMaterial;
    private final List<StepEntity> endConditions;
    private final List<StepEntity> loadPoints;

    public StepStructuralFeature(int id, String name, String structuralType, StepEntity crossSection, double structuralLength, StepEntity structuralMaterial, List<StepEntity> endConditions, List<StepEntity> loadPoints) {
        super(id, name);
        this.structuralType = structuralType;
        this.crossSection = crossSection;
        this.structuralLength = structuralLength;
        this.structuralMaterial = structuralMaterial;
        this.endConditions = endConditions == null ? null : java.util.List.copyOf(endConditions);
        this.loadPoints = loadPoints == null ? null : java.util.List.copyOf(loadPoints);
    }

    public String getStructuralType() {
        return structuralType;
    }

    public StepEntity getCrossSection() {
        return crossSection;
    }

    public double getStructuralLength() {
        return structuralLength;
    }

    public StepEntity getStructuralMaterial() {
        return structuralMaterial;
    }

    public List<StepEntity> getEndConditions() {
        return endConditions;
    }

    public List<StepEntity> getLoadPoints() {
        return loadPoints;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("structuralType", structuralType);
        state.put("crossSection", crossSection);
        state.put("structuralLength", structuralLength);
        state.put("structuralMaterial", structuralMaterial);
        state.put("endConditions", endConditions);
        state.put("loadPoints", loadPoints);
        return state;
    }
}
