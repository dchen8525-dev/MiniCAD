package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MECHANISM_DEFINITION.
 * A mechanism definition entity.
 *
 * @param id STEP instance id
 * @param name mechanism name
 * @param mechanismType mechanism type classification
 * @param links mechanism links/parts
 * @param joints mechanism joints connecting links
 * @param degreesOfFreedom degrees of freedom count
 * @param baseLink base/grounded link
 * @param actuatedJoints actuated joints list
 */
public final class StepMechanismDefinition extends AbstractStepEntity {
    private final String mechanismType;
    private final List<StepEntity> links;
    private final List<StepEntity> joints;
    private final int degreesOfFreedom;
    private final StepEntity baseLink;
    private final List<StepEntity> actuatedJoints;

    public StepMechanismDefinition(int id, String name, String mechanismType, List<StepEntity> links, List<StepEntity> joints, int degreesOfFreedom, StepEntity baseLink, List<StepEntity> actuatedJoints) {
        super(id, name);
        this.mechanismType = mechanismType;
        this.links = links == null ? null : java.util.List.copyOf(links);
        this.joints = joints == null ? null : java.util.List.copyOf(joints);
        this.degreesOfFreedom = degreesOfFreedom;
        this.baseLink = baseLink;
        this.actuatedJoints = actuatedJoints == null ? null : java.util.List.copyOf(actuatedJoints);
    }

    public String getMechanismType() {
        return mechanismType;
    }

    public List<StepEntity> getLinks() {
        return links;
    }

    public List<StepEntity> getJoints() {
        return joints;
    }

    public int getDegreesOfFreedom() {
        return degreesOfFreedom;
    }

    public StepEntity getBaseLink() {
        return baseLink;
    }

    public List<StepEntity> getActuatedJoints() {
        return actuatedJoints;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("mechanismType", mechanismType);
        state.put("links", links);
        state.put("joints", joints);
        state.put("degreesOfFreedom", degreesOfFreedom);
        state.put("baseLink", baseLink);
        state.put("actuatedJoints", actuatedJoints);
        return state;
    }
}
