package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepCertificationRecord2 extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final String varianceType;
    private final String varianceNumber;
    private final StepEntity varianceAuthority;
    private final String varianceValid;
    private final String varianceStatus;

    public StepCertificationRecord2(int id, String name, StepEntity varianceItem, String varianceType, String varianceNumber, StepEntity varianceAuthority, String varianceValid, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceType = varianceType;
        this.varianceNumber = varianceNumber;
        this.varianceAuthority = varianceAuthority;
        this.varianceValid = varianceValid;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceItem", varianceItem);
        state.put("varianceType", varianceType);
        state.put("varianceNumber", varianceNumber);
        state.put("varianceAuthority", varianceAuthority);
        state.put("varianceValid", varianceValid);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
