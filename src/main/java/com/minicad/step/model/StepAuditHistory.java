package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepAuditHistory extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final List<StepEntity> varianceAudits;
    private final StepEntity varianceLast;
    private final StepEntity varianceNext;
    private final String varianceStatus;

    public StepAuditHistory(int id, String name, StepEntity varianceItem, List<StepEntity> varianceAudits, StepEntity varianceLast, StepEntity varianceNext, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceAudits = varianceAudits == null ? null : java.util.List.copyOf(varianceAudits);
        this.varianceLast = varianceLast;
        this.varianceNext = varianceNext;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceItem", varianceItem,
                "varianceAudits", varianceAudits, "varianceLast", varianceLast, "varianceNext", varianceNext,
                "varianceStatus", varianceStatus);
    }
}
