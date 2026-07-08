package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved LINK_INSTANCE.
 * A link instance entity.
 *
 * @param id STEP instance id
 * @param name link instance name
 * @param linkDefinition link variance definition reference
 * @param linkState link variance state
 * @param linkUtilization link variance utilization
 * @param linkStatus link variance status
 */
/**
 * Resolved LINK_INSTANCE.
 * A link instance entity.
 *
 * @param id STEP instance id
 * @param name link instance name
 * @param linkDefinition link variance definition reference
 * @param linkState link variance state
 * @param linkUtilization link variance utilization
 * @param linkStatus link variance status
 */
public final class StepLinkInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity linkDefinition;
    private final String linkState;
    private final double linkUtilization;
    private final String linkStatus;

    public StepLinkInstance(int id, String name, StepEntity linkDefinition, String linkState, double linkUtilization, String linkStatus) {
        this.id = id;
        this.name = name;
        this.linkDefinition = linkDefinition;
        this.linkState = linkState;
        this.linkUtilization = linkUtilization;
        this.linkStatus = linkStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getLinkDefinition() {
        return linkDefinition;
    }

    public String getLinkState() {
        return linkState;
    }

    public double getLinkUtilization() {
        return linkUtilization;
    }

    public String getLinkStatus() {
        return linkStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLinkInstance that = (StepLinkInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(linkDefinition, that.linkDefinition) && Objects.equals(linkState, that.linkState) && linkUtilization == that.linkUtilization && Objects.equals(linkStatus, that.linkStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, linkDefinition, linkState, linkUtilization, linkStatus);
    }

    @Override
    public String toString() {
        return "StepLinkInstance{" + "id=" + id + "name=" + name + "linkDefinition=" + linkDefinition + "linkState=" + linkState + "linkUtilization=" + linkUtilization + "linkStatus=" + linkStatus + "}";
    }
}