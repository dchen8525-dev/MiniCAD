package com.minicad.step.model.organization.org.approval;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CERTIFICATION_RECORD.
 * A certification record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @variancePerson certified variance person
 * @param certificationType certification type (skill, quality, safety)
 * @varianceLevel certification variance level
 * @varianceDate certification variance date
 * @varianceExpiration expiration variance date
 * @varianceAuthority certification variance authority
 * @varianceStatus certification variance status
 */
/**
 * Resolved CERTIFICATION_RECORD.
 * A certification record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @variancePerson certified variance person
 * @param certificationType certification type (skill, quality, safety)
 * @varianceLevel certification variance level
 * @varianceDate certification variance date
 * @varianceExpiration expiration variance date
 * @varianceAuthority certification variance authority
 * @varianceStatus certification variance status
 */
public final class StepCertificationRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity variancePerson;
    private final String certificationType;
    private final int varianceLevel;
    private final StepEntity varianceDate;
    private final StepEntity varianceExpiration;
    private final StepEntity varianceAuthority;
    private final String varianceStatus;

    public StepCertificationRecord(int id, String name, StepEntity variancePerson, String certificationType, int varianceLevel, StepEntity varianceDate, StepEntity varianceExpiration, StepEntity varianceAuthority, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.variancePerson = variancePerson;
        this.certificationType = certificationType;
        this.varianceLevel = varianceLevel;
        this.varianceDate = varianceDate;
        this.varianceExpiration = varianceExpiration;
        this.varianceAuthority = varianceAuthority;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVariancePerson() {
        return variancePerson;
    }

    public String getCertificationType() {
        return certificationType;
    }

    public int getVarianceLevel() {
        return varianceLevel;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getVarianceExpiration() {
        return varianceExpiration;
    }

    public StepEntity getVarianceAuthority() {
        return varianceAuthority;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCertificationRecord that = (StepCertificationRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(variancePerson, that.variancePerson) && Objects.equals(certificationType, that.certificationType) && varianceLevel == that.varianceLevel && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceExpiration, that.varianceExpiration) && Objects.equals(varianceAuthority, that.varianceAuthority) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, variancePerson, certificationType, varianceLevel, varianceDate, varianceExpiration, varianceAuthority, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepCertificationRecord{" + "id=" + id + "name=" + name + "variancePerson=" + variancePerson + "certificationType=" + certificationType + "varianceLevel=" + varianceLevel + "varianceDate=" + varianceDate + "varianceExpiration=" + varianceExpiration + "varianceAuthority=" + varianceAuthority + "varianceStatus=" + varianceStatus + "}";
    }
}