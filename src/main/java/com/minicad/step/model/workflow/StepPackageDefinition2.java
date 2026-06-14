package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPackageDefinition2 implements StepEntity {
    private final int id;
    private final String name;
    private final String packageType;
    private final String packageDescription;
    private final List<StepEntity> packageContents;
    private final List<StepEntity> packageDependencies;
    private final String packageStatus;

    public StepPackageDefinition2(int id, String name, String packageType, String packageDescription, List<StepEntity> packageContents, List<StepEntity> packageDependencies, String packageStatus) {
        this.id = id;
        this.name = name;
        this.packageType = packageType;
        this.packageDescription = packageDescription;
        this.packageContents = packageContents == null ? null : java.util.List.copyOf(packageContents);
        this.packageDependencies = packageDependencies == null ? null : java.util.List.copyOf(packageDependencies);
        this.packageStatus = packageStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPackageDefinition2 that = (StepPackageDefinition2) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(packageType, that.packageType) && Objects.equals(packageDescription, that.packageDescription) && Objects.equals(packageContents, that.packageContents) && Objects.equals(packageDependencies, that.packageDependencies) && Objects.equals(packageStatus, that.packageStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, packageType, packageDescription, packageContents, packageDependencies, packageStatus);
    }

    @Override
    public String toString() {
        return "StepPackageDefinition2{" + "id=" + id + "name=" + name + "packageType=" + packageType + "packageDescription=" + packageDescription + "packageContents=" + packageContents + "packageDependencies=" + packageDependencies + "packageStatus=" + packageStatus + "}";
    }
}