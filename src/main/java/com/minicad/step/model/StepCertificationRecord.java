package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepCertificationRecord extends AbstractStepEntity {
    private final StepEntity variancePerson;
    private final String certificationType;
    private final int varianceLevel;
    private final StepEntity varianceDate;
    private final StepEntity varianceExpiration;
    private final StepEntity varianceAuthority;
    private final String varianceStatus;

    public StepCertificationRecord(int id, String name, StepEntity variancePerson, String certificationType, int varianceLevel, StepEntity varianceDate, StepEntity varianceExpiration, StepEntity varianceAuthority, String varianceStatus) {
        super(id, name);
        this.variancePerson = variancePerson;
        this.certificationType = certificationType;
        this.varianceLevel = varianceLevel;
        this.varianceDate = varianceDate;
        this.varianceExpiration = varianceExpiration;
        this.varianceAuthority = varianceAuthority;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("variancePerson", variancePerson);
        state.put("certificationType", certificationType);
        state.put("varianceLevel", varianceLevel);
        state.put("varianceDate", varianceDate);
        state.put("varianceExpiration", varianceExpiration);
        state.put("varianceAuthority", varianceAuthority);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
