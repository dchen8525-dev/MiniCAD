package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MOLD_FEATURE.
 * A mold feature entity.
 *
 * @param id STEP instance id
 * @param name mold name
 * @param moldType mold type classification (injection, compression, blow)
 * @param cavityGeometry cavity geometry representation
 * @param coreGeometry core geometry representation
 * @param partingLine parting line geometry
 * @param gatingSystem gating system features
 * @param coolingChannels cooling channel features
 */
public final class StepMoldFeature extends AbstractStepEntity {
    private final String moldType;
    private final StepEntity cavityGeometry;
    private final StepEntity coreGeometry;
    private final StepEntity partingLine;
    private final List<StepEntity> gatingSystem;
    private final List<StepEntity> coolingChannels;

    public StepMoldFeature(int id, String name, String moldType, StepEntity cavityGeometry, StepEntity coreGeometry, StepEntity partingLine, List<StepEntity> gatingSystem, List<StepEntity> coolingChannels) {
        super(id, name);
        this.moldType = moldType;
        this.cavityGeometry = cavityGeometry;
        this.coreGeometry = coreGeometry;
        this.partingLine = partingLine;
        this.gatingSystem = gatingSystem == null ? null : java.util.List.copyOf(gatingSystem);
        this.coolingChannels = coolingChannels == null ? null : java.util.List.copyOf(coolingChannels);
    }

    public String getMoldType() {
        return moldType;
    }

    public StepEntity getCavityGeometry() {
        return cavityGeometry;
    }

    public StepEntity getCoreGeometry() {
        return coreGeometry;
    }

    public StepEntity getPartingLine() {
        return partingLine;
    }

    public List<StepEntity> getGatingSystem() {
        return gatingSystem;
    }

    public List<StepEntity> getCoolingChannels() {
        return coolingChannels;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("moldType", moldType);
        state.put("cavityGeometry", cavityGeometry);
        state.put("coreGeometry", coreGeometry);
        state.put("partingLine", partingLine);
        state.put("gatingSystem", gatingSystem);
        state.put("coolingChannels", coolingChannels);
        return state;
    }
}
