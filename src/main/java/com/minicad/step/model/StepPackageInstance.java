package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPackageInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity packageDefinition;
    private final String packageState;
    private final String packageVersion;
    private final boolean packageInstalled;
    private final String packageStatus;

    public StepPackageInstance(int id, String name, StepEntity packageDefinition, String packageState, String packageVersion, boolean packageInstalled, String packageStatus) {
        this.id = id;
        this.name = name;
        this.packageDefinition = packageDefinition;
        this.packageState = packageState;
        this.packageVersion = packageVersion;
        this.packageInstalled = packageInstalled;
        this.packageStatus = packageStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPackageInstance that = (StepPackageInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(packageDefinition, that.packageDefinition) && Objects.equals(packageState, that.packageState) && Objects.equals(packageVersion, that.packageVersion) && packageInstalled == that.packageInstalled && Objects.equals(packageStatus, that.packageStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, packageDefinition, packageState, packageVersion, packageInstalled, packageStatus);
    }

    @Override
    public String toString() {
        return "StepPackageInstance{" + "id=" + id + "name=" + name + "packageDefinition=" + packageDefinition + "packageState=" + packageState + "packageVersion=" + packageVersion + "packageInstalled=" + packageInstalled + "packageStatus=" + packageStatus + "}";
    }
}