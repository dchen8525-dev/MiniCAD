package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LINK_DEFINITION.
 * A link definition entity.
 *
 * @param id STEP instance id
 * @param name link name
 * @param linkType link variance type
 * @param linkSource link variance source reference
 * @param linkTarget link variance target reference
 * @param linkBandwidth link variance bandwidth
 * @param linkStatus link variance status
 */
/**
 * Resolved LINK_DEFINITION.
 * A link definition entity.
 *
 * @param id STEP instance id
 * @param name link name
 * @param linkType link variance type
 * @param linkSource link variance source reference
 * @param linkTarget link variance target reference
 * @param linkBandwidth link variance bandwidth
 * @param linkStatus link variance status
 */
public final class StepLinkDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String linkType;
    private final StepEntity linkSource;
    private final StepEntity linkTarget;
    private final double linkBandwidth;
    private final String linkStatus;

    public StepLinkDefinition(int id, String name, String linkType, StepEntity linkSource, StepEntity linkTarget, double linkBandwidth, String linkStatus) {
        this.id = id;
        this.name = name;
        this.linkType = linkType;
        this.linkSource = linkSource;
        this.linkTarget = linkTarget;
        this.linkBandwidth = linkBandwidth;
        this.linkStatus = linkStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLinkType() {
        return linkType;
    }

    public StepEntity getLinkSource() {
        return linkSource;
    }

    public StepEntity getLinkTarget() {
        return linkTarget;
    }

    public double getLinkBandwidth() {
        return linkBandwidth;
    }

    public String getLinkStatus() {
        return linkStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLinkDefinition that = (StepLinkDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(linkType, that.linkType) && Objects.equals(linkSource, that.linkSource) && Objects.equals(linkTarget, that.linkTarget) && linkBandwidth == that.linkBandwidth && Objects.equals(linkStatus, that.linkStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, linkType, linkSource, linkTarget, linkBandwidth, linkStatus);
    }

    @Override
    public String toString() {
        return "StepLinkDefinition{" + "id=" + id + "name=" + name + "linkType=" + linkType + "linkSource=" + linkSource + "linkTarget=" + linkTarget + "linkBandwidth=" + linkBandwidth + "linkStatus=" + linkStatus + "}";
    }
}