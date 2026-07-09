package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMaterialSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final String materialType;
    private final String materialGrade;
    private final List<Double> mechanicalProperties;
    private final List<String> chemicalComposition;
    private final List<String> standards;

    public StepMaterialSpecification(int id, String name, String materialType, String materialGrade, List<Double> mechanicalProperties, List<String> chemicalComposition, List<String> standards) {
        this.id = id;
        this.name = name;
        this.materialType = materialType;
        this.materialGrade = materialGrade;
        this.mechanicalProperties = mechanicalProperties == null ? null : java.util.List.copyOf(mechanicalProperties);
        this.chemicalComposition = chemicalComposition == null ? null : java.util.List.copyOf(chemicalComposition);
        this.standards = standards == null ? null : java.util.List.copyOf(standards);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMaterialSpecification that = (StepMaterialSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(materialType, that.materialType) && Objects.equals(materialGrade, that.materialGrade) && Objects.equals(mechanicalProperties, that.mechanicalProperties) && Objects.equals(chemicalComposition, that.chemicalComposition) && Objects.equals(standards, that.standards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, materialType, materialGrade, mechanicalProperties, chemicalComposition, standards);
    }

    @Override
    public String toString() {
        return "StepMaterialSpecification{" + "id=" + id + "name=" + name + "materialType=" + materialType + "materialGrade=" + materialGrade + "mechanicalProperties=" + mechanicalProperties + "chemicalComposition=" + chemicalComposition + "standards=" + standards + "}";
    }
}