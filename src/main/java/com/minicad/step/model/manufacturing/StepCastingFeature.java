package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CASTING_FEATURE.
 * A casting feature entity.
 *
 * @param id STEP instance id
 * @param name casting name
 * @param castingType casting type classification (sand, investment, die casting)
 * @param moldGeometry mold geometry representation
 * @param gatingSystem gating system features
 * @param riserFeatures riser/feeder features
 * @param partingSurface parting surface geometry
 * @param castingMaterial casting material specification
 */
/**
 * Resolved CASTING_FEATURE.
 * A casting feature entity.
 *
 * @param id STEP instance id
 * @param name casting name
 * @param castingType casting type classification (sand, investment, die casting)
 * @param moldGeometry mold geometry representation
 * @param gatingSystem gating system features
 * @param riserFeatures riser/feeder features
 * @param partingSurface parting surface geometry
 * @param castingMaterial casting material specification
 */
public final class StepCastingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String castingType;
    private final StepEntity moldGeometry;
    private final List<StepEntity> gatingSystem;
    private final List<StepEntity> riserFeatures;
    private final StepEntity partingSurface;
    private final StepEntity castingMaterial;

    public StepCastingFeature(int id, String name, String castingType, StepEntity moldGeometry, List<StepEntity> gatingSystem, List<StepEntity> riserFeatures, StepEntity partingSurface, StepEntity castingMaterial) {
        this.id = id;
        this.name = name;
        this.castingType = castingType;
        this.moldGeometry = moldGeometry;
        this.gatingSystem = gatingSystem == null ? null : java.util.List.copyOf(gatingSystem);
        this.riserFeatures = riserFeatures == null ? null : java.util.List.copyOf(riserFeatures);
        this.partingSurface = partingSurface;
        this.castingMaterial = castingMaterial;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCastingType() {
        return castingType;
    }

    public StepEntity getMoldGeometry() {
        return moldGeometry;
    }

    public List<StepEntity> getGatingSystem() {
        return gatingSystem;
    }

    public List<StepEntity> getRiserFeatures() {
        return riserFeatures;
    }

    public StepEntity getPartingSurface() {
        return partingSurface;
    }

    public StepEntity getCastingMaterial() {
        return castingMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCastingFeature that = (StepCastingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(castingType, that.castingType) && Objects.equals(moldGeometry, that.moldGeometry) && Objects.equals(gatingSystem, that.gatingSystem) && Objects.equals(riserFeatures, that.riserFeatures) && Objects.equals(partingSurface, that.partingSurface) && Objects.equals(castingMaterial, that.castingMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, castingType, moldGeometry, gatingSystem, riserFeatures, partingSurface, castingMaterial);
    }

    @Override
    public String toString() {
        return "StepCastingFeature{" + "id=" + id + "name=" + name + "castingType=" + castingType + "moldGeometry=" + moldGeometry + "gatingSystem=" + gatingSystem + "riserFeatures=" + riserFeatures + "partingSurface=" + partingSurface + "castingMaterial=" + castingMaterial + "}";
    }
}