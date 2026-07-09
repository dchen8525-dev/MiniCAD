package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved CONFIGURATION_EFFECTIVITY.
 * Specifies when a configuration-managed item becomes effective.
 *
 * @param id STEP instance id
 * @param name effectivity name
 * @param configuration configuration item reference
 * @param itemConceived product definition being configured
 */
/**
 * Resolved CONFIGURATION_EFFECTIVITY.
 * Specifies when a configuration-managed item becomes effective.
 *
 * @param id STEP instance id
 * @param name effectivity name
 * @param configuration configuration item reference
 * @param itemConceived product definition being configured
 */
public final class StepConfigurationEffectivity implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity configuration;
    private final StepEntity itemConceived;

    public StepConfigurationEffectivity(int id, String name, StepEntity configuration, StepEntity itemConceived) {
        this.id = id;
        this.name = name;
        this.configuration = configuration;
        this.itemConceived = itemConceived;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getConfiguration() {
        return configuration;
    }

    public StepEntity getItemConceived() {
        return itemConceived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConfigurationEffectivity that = (StepConfigurationEffectivity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(configuration, that.configuration) && Objects.equals(itemConceived, that.itemConceived);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, configuration, itemConceived);
    }

    @Override
    public String toString() {
        return "StepConfigurationEffectivity{" + "id=" + id + "name=" + name + "configuration=" + configuration + "itemConceived=" + itemConceived + "}";
    }
}
