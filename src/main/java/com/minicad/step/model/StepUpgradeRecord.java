package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved UPGRADE_RECORD.
 * An upgrade record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment upgraded variance equipment
 * @varianceFrom upgrade variance from version
 * @varianceTo upgrade variance to version
 * @varianceDate upgrade variance date
 * @varianceChanges upgrade variance changes
 * @varianceStatus record variance status
 */
public final class StepUpgradeRecord extends AbstractStepEntity {
    private final StepEntity varianceEquipment;
    private final String varianceFrom;
    private final String varianceTo;
    private final StepEntity varianceDate;
    private final List<String> varianceChanges;
    private final String varianceStatus;

    public StepUpgradeRecord(int id, String name, StepEntity varianceEquipment, String varianceFrom, String varianceTo, StepEntity varianceDate, List<String> varianceChanges, String varianceStatus) {
        super(id, name);
        this.varianceEquipment = varianceEquipment;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceDate = varianceDate;
        this.varianceChanges = varianceChanges == null ? null : java.util.List.copyOf(varianceChanges);
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceEquipment() {
        return varianceEquipment;
    }

    public String getVarianceFrom() {
        return varianceFrom;
    }

    public String getVarianceTo() {
        return varianceTo;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public List<String> getVarianceChanges() {
        return varianceChanges;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceEquipment", varianceEquipment);
        state.put("varianceFrom", varianceFrom);
        state.put("varianceTo", varianceTo);
        state.put("varianceDate", varianceDate);
        state.put("varianceChanges", varianceChanges);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
