package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepLinkDefinition extends AbstractStepEntity {
    private final String linkType;
    private final StepEntity linkSource;
    private final StepEntity linkTarget;
    private final double linkBandwidth;
    private final String linkStatus;

    public StepLinkDefinition(int id, String name, String linkType, StepEntity linkSource, StepEntity linkTarget, double linkBandwidth, String linkStatus) {
        super(id, name);
        this.linkType = linkType;
        this.linkSource = linkSource;
        this.linkTarget = linkTarget;
        this.linkBandwidth = linkBandwidth;
        this.linkStatus = linkStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("linkType", linkType);
        state.put("linkSource", linkSource);
        state.put("linkTarget", linkTarget);
        state.put("linkBandwidth", linkBandwidth);
        state.put("linkStatus", linkStatus);
        return state;
    }
}
