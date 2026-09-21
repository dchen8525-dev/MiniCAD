package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PACKAGE_INSTANCE.
 * A package instance entity.
 *
 * @param id STEP instance id
 * @param name package instance name
 * @param packageDefinition package variance definition reference
 * @param packageState package variance state
 * @param packageVersion package variance version
 * @param packageInstalled package variance installed flag
 * @param packageStatus package variance status
 */
public final class StepPackageInstance extends AbstractStepEntity {
    private final StepEntity packageDefinition;
    private final String packageState;
    private final String packageVersion;
    private final boolean packageInstalled;
    private final String packageStatus;

    public StepPackageInstance(int id, String name, StepEntity packageDefinition, String packageState, String packageVersion, boolean packageInstalled, String packageStatus) {
        super(id, name);
        this.packageDefinition = packageDefinition;
        this.packageState = packageState;
        this.packageVersion = packageVersion;
        this.packageInstalled = packageInstalled;
        this.packageStatus = packageStatus;
    }

    public StepEntity getPackageDefinition() {
        return packageDefinition;
    }

    public String getPackageState() {
        return packageState;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public boolean isPackageInstalled() {
        return packageInstalled;
    }

    public String getPackageStatus() {
        return packageStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("packageDefinition", packageDefinition);
        state.put("packageState", packageState);
        state.put("packageVersion", packageVersion);
        state.put("packageInstalled", packageInstalled);
        state.put("packageStatus", packageStatus);
        return state;
    }
}
