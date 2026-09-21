package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved VERSION_CONTROL.
 * A version control entity.
 *
 * @param id STEP instance id
 * @param name version name
 * @param versionNumber version number/identifier
 * @param versionDescription version description
 * @param versionDate version release date
 * @param versionAuthor version author
 * @param versionStatus version status (draft, released, archived)
 * @param previousVersion previous version reference
 * @param versionChanges changes from previous version
 */
public final class StepVersionControl extends AbstractStepEntity {
    private final String versionNumber;
    private final String versionDescription;
    private final StepEntity versionDate;
    private final StepEntity versionAuthor;
    private final String versionStatus;
    private final StepEntity previousVersion;
    private final List<StepEntity> versionChanges;

    public StepVersionControl(int id, String name, String versionNumber, String versionDescription, StepEntity versionDate, StepEntity versionAuthor, String versionStatus, StepEntity previousVersion, List<StepEntity> versionChanges) {
        super(id, name);
        this.versionNumber = versionNumber;
        this.versionDescription = versionDescription;
        this.versionDate = versionDate;
        this.versionAuthor = versionAuthor;
        this.versionStatus = versionStatus;
        this.previousVersion = previousVersion;
        this.versionChanges = versionChanges == null ? null : java.util.List.copyOf(versionChanges);
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public String getVersionDescription() {
        return versionDescription;
    }

    public StepEntity getVersionDate() {
        return versionDate;
    }

    public StepEntity getVersionAuthor() {
        return versionAuthor;
    }

    public String getVersionStatus() {
        return versionStatus;
    }

    public StepEntity getPreviousVersion() {
        return previousVersion;
    }

    public List<StepEntity> getVersionChanges() {
        return versionChanges;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("versionNumber", versionNumber);
        state.put("versionDescription", versionDescription);
        state.put("versionDate", versionDate);
        state.put("versionAuthor", versionAuthor);
        state.put("versionStatus", versionStatus);
        state.put("previousVersion", previousVersion);
        state.put("versionChanges", versionChanges);
        return state;
    }
}
