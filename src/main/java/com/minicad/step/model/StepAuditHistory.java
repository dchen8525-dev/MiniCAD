package com.minicad.step.model.management.config;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved AUDIT_HISTORY.
 * An audit history entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @varianceItem audited variance item
 * @varianceAudits audit variance entries
 * @varianceLast last variance audit reference
 * @varianceNext next variance scheduled audit
 * @varianceStatus history variance status
 */
/**
 * Resolved AUDIT_HISTORY.
 * An audit history entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @varianceItem audited variance item
 * @varianceAudits audit variance entries
 * @varianceLast last variance audit reference
 * @varianceNext next variance scheduled audit
 * @varianceStatus history variance status
 */
public final class StepAuditHistory implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final List<StepEntity> varianceAudits;
    private final StepEntity varianceLast;
    private final StepEntity varianceNext;
    private final String varianceStatus;

    public StepAuditHistory(int id, String name, StepEntity varianceItem, List<StepEntity> varianceAudits, StepEntity varianceLast, StepEntity varianceNext, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceAudits = varianceAudits == null ? null : java.util.List.copyOf(varianceAudits);
        this.varianceLast = varianceLast;
        this.varianceNext = varianceNext;
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

    public List<StepEntity> getVarianceAudits() {
        return varianceAudits;
    }

    public StepEntity getVarianceLast() {
        return varianceLast;
    }

    public StepEntity getVarianceNext() {
        return varianceNext;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAuditHistory that = (StepAuditHistory) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceAudits, that.varianceAudits) && Objects.equals(varianceLast, that.varianceLast) && Objects.equals(varianceNext, that.varianceNext) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceAudits, varianceLast, varianceNext, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepAuditHistory{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceAudits=" + varianceAudits + "varianceLast=" + varianceLast + "varianceNext=" + varianceNext + "varianceStatus=" + varianceStatus + "}";
    }
}