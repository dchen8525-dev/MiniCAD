package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal PRODUCT_DEFINITION_EFFECTIVITY metadata.
 *
 * @param id STEP instance id
 * @param effectivityId effectivity identifier
 * @param usage usage text
 * @param productDefinition affected product definition
 */
/**
 * Minimal PRODUCT_DEFINITION_EFFECTIVITY metadata.
 *
 * @param id STEP instance id
 * @param effectivityId effectivity identifier
 * @param usage usage text
 * @param productDefinition affected product definition
 */
public final class StepProductDefinitionEffectivity implements StepEntity {
    private final int id;
    private final String effectivityId;
    private final String usage;
    private final StepProductDefinition productDefinition;

    public StepProductDefinitionEffectivity(int id, String effectivityId, String usage, StepProductDefinition productDefinition) {
        this.id = id;
        this.effectivityId = effectivityId;
        this.usage = usage;
        this.productDefinition = productDefinition;
    }

    public int getId() {
        return id;
    }

    public String getEffectivityId() {
        return effectivityId;
    }

    public String getUsage() {
        return usage;
    }

    public StepProductDefinition getProductDefinition() {
        return productDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductDefinitionEffectivity that = (StepProductDefinitionEffectivity) o;
        return id == that.id && Objects.equals(effectivityId, that.effectivityId) && Objects.equals(usage, that.usage) && Objects.equals(productDefinition, that.productDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, effectivityId, usage, productDefinition);
    }

    @Override
    public String toString() {
        return "StepProductDefinitionEffectivity{" + "id=" + id + "effectivityId=" + effectivityId + "usage=" + usage + "productDefinition=" + productDefinition + "}";
    }
}
