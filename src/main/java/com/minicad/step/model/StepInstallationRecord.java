package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved INSTALLATION_RECORD.
 * An installation record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment installed variance equipment
 * @varianceLocation installation variance location
 * @varianceDate installation variance date
 * @varianceInstaller installer variance person/team
 * @varianceChecks installation variance verification checks
 * @varianceStatus record variance status
 */
public final class StepInstallationRecord extends AbstractStepEntity {
    private final StepEntity varianceEquipment;
    private final String varianceLocation;
    private final StepEntity varianceDate;
    private final StepEntity varianceInstaller;
    private final List<String> varianceChecks;
    private final String varianceStatus;

    public StepInstallationRecord(int id, String name, StepEntity varianceEquipment, String varianceLocation, StepEntity varianceDate, StepEntity varianceInstaller, List<String> varianceChecks, String varianceStatus) {
        super(id, name);
        this.varianceEquipment = varianceEquipment;
        this.varianceLocation = varianceLocation;
        this.varianceDate = varianceDate;
        this.varianceInstaller = varianceInstaller;
        this.varianceChecks = varianceChecks == null ? null : java.util.List.copyOf(varianceChecks);
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceEquipment() {
        return varianceEquipment;
    }

    public String getVarianceLocation() {
        return varianceLocation;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getVarianceInstaller() {
        return varianceInstaller;
    }

    public List<String> getVarianceChecks() {
        return varianceChecks;
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
        state.put("varianceLocation", varianceLocation);
        state.put("varianceDate", varianceDate);
        state.put("varianceInstaller", varianceInstaller);
        state.put("varianceChecks", varianceChecks);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
