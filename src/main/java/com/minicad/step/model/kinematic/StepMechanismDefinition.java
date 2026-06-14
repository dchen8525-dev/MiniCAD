package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMechanismDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String mechanismType;
    private final List<StepEntity> links;
    private final List<StepEntity> joints;
    private final int degreesOfFreedom;
    private final StepEntity baseLink;
    private final List<StepEntity> actuatedJoints;

    public StepMechanismDefinition(int id, String name, String mechanismType, List<StepEntity> links, List<StepEntity> joints, int degreesOfFreedom, StepEntity baseLink, List<StepEntity> actuatedJoints) {
        this.id = id;
        this.name = name;
        this.mechanismType = mechanismType;
        this.links = links == null ? null : java.util.List.copyOf(links);
        this.joints = joints == null ? null : java.util.List.copyOf(joints);
        this.degreesOfFreedom = degreesOfFreedom;
        this.baseLink = baseLink;
        this.actuatedJoints = actuatedJoints == null ? null : java.util.List.copyOf(actuatedJoints);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMechanismDefinition that = (StepMechanismDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(mechanismType, that.mechanismType) && Objects.equals(links, that.links) && Objects.equals(joints, that.joints) && degreesOfFreedom == that.degreesOfFreedom && Objects.equals(baseLink, that.baseLink) && Objects.equals(actuatedJoints, that.actuatedJoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mechanismType, links, joints, degreesOfFreedom, baseLink, actuatedJoints);
    }

    @Override
    public String toString() {
        return "StepMechanismDefinition{" + "id=" + id + "name=" + name + "mechanismType=" + mechanismType + "links=" + links + "joints=" + joints + "degreesOfFreedom=" + degreesOfFreedom + "baseLink=" + baseLink + "actuatedJoints=" + actuatedJoints + "}";
    }
}