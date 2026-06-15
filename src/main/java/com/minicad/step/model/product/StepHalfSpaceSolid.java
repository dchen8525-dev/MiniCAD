package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal parse-only half-space solid.
 *
 * @param id step id
 * @param name step label
 * @param baseSurface boundary surface
 * @param agreementFlag side agreement flag
 * @param enclosure optional enclosure entity for boxed half spaces
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal parse-only half-space solid.
 *
 * @param id step id
 * @param name step label
 * @param baseSurface boundary surface
 * @param agreementFlag side agreement flag
 * @param enclosure optional enclosure entity for boxed half spaces
 * @param entityName concrete STEP entity name
 */
public final class StepHalfSpaceSolid implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity baseSurface;
    private final boolean agreementFlag;
    private final StepEntity enclosure;
    private final String entityName;

    public StepHalfSpaceSolid(int id, String name, StepEntity baseSurface, boolean agreementFlag, StepEntity enclosure, String entityName) {
        this.id = id;
        this.name = name;
        this.baseSurface = baseSurface;
        this.agreementFlag = agreementFlag;
        this.enclosure = enclosure;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBaseSurface() {
        return baseSurface;
    }

    public boolean isAgreementFlag() {
        return agreementFlag;
    }

    public StepEntity getEnclosure() {
        return enclosure;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity baseSurface() { return getBaseSurface(); }
    public boolean agreementFlag() { return isAgreementFlag(); }
    public StepEntity enclosure() { return getEnclosure(); }
    public String entityName() { return getEntityName(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHalfSpaceSolid that = (StepHalfSpaceSolid) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(baseSurface, that.baseSurface) && agreementFlag == that.agreementFlag && Objects.equals(enclosure, that.enclosure) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, baseSurface, agreementFlag, enclosure, entityName);
    }

    @Override
    public String toString() {
        return "StepHalfSpaceSolid{" + "id=" + id + "name=" + name + "baseSurface=" + baseSurface + "agreementFlag=" + agreementFlag + "enclosure=" + enclosure + "entityName=" + entityName + "}";
    }
}
