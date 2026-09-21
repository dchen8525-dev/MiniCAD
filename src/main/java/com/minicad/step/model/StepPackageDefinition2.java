package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PACKAGE_DEFINITION.
 * A package definition entity.
 *
 * @param id STEP instance id
 * @param name package name
 * @param packageType package variance type
 * @param packageDescription package variance description
 * @param packageContents package variance content definitions
 * @param packageDependencies package variance dependencies
 * @param packageStatus package variance status
 */
public final class StepPackageDefinition2 extends AbstractStepEntity {
    private final String packageType;
    private final String packageDescription;
    private final List<StepEntity> packageContents;
    private final List<StepEntity> packageDependencies;
    private final String packageStatus;

    public StepPackageDefinition2(int id, String name, String packageType, String packageDescription, List<StepEntity> packageContents, List<StepEntity> packageDependencies, String packageStatus) {
        super(id, name);
        this.packageType = packageType;
        this.packageDescription = packageDescription;
        this.packageContents = packageContents == null ? null : java.util.List.copyOf(packageContents);
        this.packageDependencies = packageDependencies == null ? null : java.util.List.copyOf(packageDependencies);
        this.packageStatus = packageStatus;
    }

    public String getPackageType() {
        return packageType;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public List<StepEntity> getPackageContents() {
        return packageContents;
    }

    public List<StepEntity> getPackageDependencies() {
        return packageDependencies;
    }

    public String getPackageStatus() {
        return packageStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("packageType", packageType);
        state.put("packageDescription", packageDescription);
        state.put("packageContents", packageContents);
        state.put("packageDependencies", packageDependencies);
        state.put("packageStatus", packageStatus);
        return state;
    }
}
