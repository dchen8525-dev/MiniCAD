package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMoldFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String moldType;
    private final StepEntity cavityGeometry;
    private final StepEntity coreGeometry;
    private final StepEntity partingLine;
    private final List<StepEntity> gatingSystem;
    private final List<StepEntity> coolingChannels;

    public StepMoldFeature(int id, String name, String moldType, StepEntity cavityGeometry, StepEntity coreGeometry, StepEntity partingLine, List<StepEntity> gatingSystem, List<StepEntity> coolingChannels) {
        this.id = id;
        this.name = name;
        this.moldType = moldType;
        this.cavityGeometry = cavityGeometry;
        this.coreGeometry = coreGeometry;
        this.partingLine = partingLine;
        this.gatingSystem = gatingSystem == null ? null : java.util.List.copyOf(gatingSystem);
        this.coolingChannels = coolingChannels == null ? null : java.util.List.copyOf(coolingChannels);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMoldFeature that = (StepMoldFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(moldType, that.moldType) && Objects.equals(cavityGeometry, that.cavityGeometry) && Objects.equals(coreGeometry, that.coreGeometry) && Objects.equals(partingLine, that.partingLine) && Objects.equals(gatingSystem, that.gatingSystem) && Objects.equals(coolingChannels, that.coolingChannels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, moldType, cavityGeometry, coreGeometry, partingLine, gatingSystem, coolingChannels);
    }

    @Override
    public String toString() {
        return "StepMoldFeature{" + "id=" + id + "name=" + name + "moldType=" + moldType + "cavityGeometry=" + cavityGeometry + "coreGeometry=" + coreGeometry + "partingLine=" + partingLine + "gatingSystem=" + gatingSystem + "coolingChannels=" + coolingChannels + "}";
    }
}