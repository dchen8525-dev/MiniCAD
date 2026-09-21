package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved VERSION_RECORD.
 * A version record entity.
 *
 * @param id STEP instance id
 * @param name version name
 * @param versionNumber version variance number
 * @param versionLabel version variance label
 * @param versionTarget version variance target reference
 * @param versionAuthor version variance author reference
 * @param versionTimestamp version variance timestamp
 * @param versionStatus version variance status
 */
public final class StepVersionRecord extends AbstractStepEntity {
    private final String versionNumber;
    private final String versionLabel;
    private final StepEntity versionTarget;
    private final StepEntity versionAuthor;
    private final StepEntity versionTimestamp;
    private final String versionStatus;

    public StepVersionRecord(int id, String name, String versionNumber, String versionLabel, StepEntity versionTarget, StepEntity versionAuthor, StepEntity versionTimestamp, String versionStatus) {
        super(id, name);
        this.versionNumber = versionNumber;
        this.versionLabel = versionLabel;
        this.versionTarget = versionTarget;
        this.versionAuthor = versionAuthor;
        this.versionTimestamp = versionTimestamp;
        this.versionStatus = versionStatus;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public String getVersionLabel() {
        return versionLabel;
    }

    public StepEntity getVersionTarget() {
        return versionTarget;
    }

    public StepEntity getVersionAuthor() {
        return versionAuthor;
    }

    public StepEntity getVersionTimestamp() {
        return versionTimestamp;
    }

    public String getVersionStatus() {
        return versionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("versionNumber", versionNumber);
        state.put("versionLabel", versionLabel);
        state.put("versionTarget", versionTarget);
        state.put("versionAuthor", versionAuthor);
        state.put("versionTimestamp", versionTimestamp);
        state.put("versionStatus", versionStatus);
        return state;
    }
}
