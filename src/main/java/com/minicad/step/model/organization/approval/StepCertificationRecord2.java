package com.minicad.step.model.organization.org.approval;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CERTIFICATION_RECORD_2.
 * A certification record entity for products/systems.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem certified variance item
 * @varianceType certification variance type
 * @varianceNumber certification variance number
 * @varianceAuthority certification variance authority
 * @varianceValid validity variance period
 * @varianceStatus record variance status
 */
/**
 * Resolved CERTIFICATION_RECORD_2.
 * A certification record entity for products/systems.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem certified variance item
 * @varianceType certification variance type
 * @varianceNumber certification variance number
 * @varianceAuthority certification variance authority
 * @varianceValid validity variance period
 * @varianceStatus record variance status
 */
public final class StepCertificationRecord2 implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final String varianceType;
    private final String varianceNumber;
    private final StepEntity varianceAuthority;
    private final String varianceValid;
    private final String varianceStatus;

    public StepCertificationRecord2(int id, String name, StepEntity varianceItem, String varianceType, String varianceNumber, StepEntity varianceAuthority, String varianceValid, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceType = varianceType;
        this.varianceNumber = varianceNumber;
        this.varianceAuthority = varianceAuthority;
        this.varianceValid = varianceValid;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public String getVarianceNumber() {
        return varianceNumber;
    }

    public StepEntity getVarianceAuthority() {
        return varianceAuthority;
    }

    public String getVarianceValid() {
        return varianceValid;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCertificationRecord2 that = (StepCertificationRecord2) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceType, that.varianceType) && Objects.equals(varianceNumber, that.varianceNumber) && Objects.equals(varianceAuthority, that.varianceAuthority) && Objects.equals(varianceValid, that.varianceValid) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceType, varianceNumber, varianceAuthority, varianceValid, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepCertificationRecord2{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceType=" + varianceType + "varianceNumber=" + varianceNumber + "varianceAuthority=" + varianceAuthority + "varianceValid=" + varianceValid + "varianceStatus=" + varianceStatus + "}";
    }
}