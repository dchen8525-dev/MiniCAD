package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepAssemblyComponentUsage extends AbstractStepEntity {
    private final StepEntity parentAssembly;
    private final StepEntity childComponent;
    private final int quantity;
    private final String usageType;
    private final StepEntity location;

    public StepAssemblyComponentUsage(int id, String name, StepEntity parentAssembly, StepEntity childComponent, int quantity, String usageType, StepEntity location) {
        super(id, name);
        this.parentAssembly = parentAssembly;
        this.childComponent = childComponent;
        this.quantity = quantity;
        this.usageType = usageType;
        this.location = location;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("parentAssembly", parentAssembly);
        state.put("childComponent", childComponent);
        state.put("quantity", quantity);
        state.put("usageType", usageType);
        state.put("location", location);
        return state;
    }
}
