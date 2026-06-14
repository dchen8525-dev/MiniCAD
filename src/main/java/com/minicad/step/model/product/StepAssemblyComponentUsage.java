package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ASSEMBLY_COMPONENT_USAGE.
 * An assembly component usage entity.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param parentAssembly parent assembly reference
 * @param childComponent child component reference
 * @param quantity quantity of components
 * @param usageType usage type classification
 * @param location placement location
 */
/**
 * Resolved ASSEMBLY_COMPONENT_USAGE.
 * An assembly component usage entity.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param parentAssembly parent assembly reference
 * @param childComponent child component reference
 * @param quantity quantity of components
 * @param usageType usage type classification
 * @param location placement location
 */
public final class StepAssemblyComponentUsage implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity parentAssembly;
    private final StepEntity childComponent;
    private final int quantity;
    private final String usageType;
    private final StepEntity location;

    public StepAssemblyComponentUsage(int id, String name, StepEntity parentAssembly, StepEntity childComponent, int quantity, String usageType, StepEntity location) {
        this.id = id;
        this.name = name;
        this.parentAssembly = parentAssembly;
        this.childComponent = childComponent;
        this.quantity = quantity;
        this.usageType = usageType;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getParentAssembly() {
        return parentAssembly;
    }

    public StepEntity getChildComponent() {
        return childComponent;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUsageType() {
        return usageType;
    }

    public StepEntity getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAssemblyComponentUsage that = (StepAssemblyComponentUsage) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(parentAssembly, that.parentAssembly) && Objects.equals(childComponent, that.childComponent) && quantity == that.quantity && Objects.equals(usageType, that.usageType) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentAssembly, childComponent, quantity, usageType, location);
    }

    @Override
    public String toString() {
        return "StepAssemblyComponentUsage{" + "id=" + id + "name=" + name + "parentAssembly=" + parentAssembly + "childComponent=" + childComponent + "quantity=" + quantity + "usageType=" + usageType + "location=" + location + "}";
    }
}