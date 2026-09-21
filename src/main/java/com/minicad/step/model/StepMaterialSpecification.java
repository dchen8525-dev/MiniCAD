package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MATERIAL_SPECIFICATION.
 * A material specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param materialType material type classification
 * @param materialGrade material grade specification
 * @param mechanicalProperties mechanical property values
 * @param chemicalComposition chemical composition specifications
 * @param standards applicable material standards
 */
public final class StepMaterialSpecification extends AbstractStepEntity {
    private final String materialType;
    private final String materialGrade;
    private final List<Double> mechanicalProperties;
    private final List<String> chemicalComposition;
    private final List<String> standards;

    public StepMaterialSpecification(int id, String name, String materialType, String materialGrade, List<Double> mechanicalProperties, List<String> chemicalComposition, List<String> standards) {
        super(id, name);
        this.materialType = materialType;
        this.materialGrade = materialGrade;
        this.mechanicalProperties = mechanicalProperties == null ? null : java.util.List.copyOf(mechanicalProperties);
        this.chemicalComposition = chemicalComposition == null ? null : java.util.List.copyOf(chemicalComposition);
        this.standards = standards == null ? null : java.util.List.copyOf(standards);
    }

    public String getMaterialType() {
        return materialType;
    }

    public String getMaterialGrade() {
        return materialGrade;
    }

    public List<Double> getMechanicalProperties() {
        return mechanicalProperties;
    }

    public List<String> getChemicalComposition() {
        return chemicalComposition;
    }

    public List<String> getStandards() {
        return standards;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("materialType", materialType);
        state.put("materialGrade", materialGrade);
        state.put("mechanicalProperties", mechanicalProperties);
        state.put("chemicalComposition", chemicalComposition);
        state.put("standards", standards);
        return state;
    }
}
