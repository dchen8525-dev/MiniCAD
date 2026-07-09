package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LIBRARY_DEFINITION.
 * A library definition entity.
 *
 * @param id STEP instance id
 * @param name library name
 * @param libraryType library variance type
 * @param libraryDescription library variance description
 * @param libraryDependencies library variance dependencies
 * @param libraryExports library variance exported functions
 * @param libraryStatus library variance status
 */
/**
 * Resolved LIBRARY_DEFINITION.
 * A library definition entity.
 *
 * @param id STEP instance id
 * @param name library name
 * @param libraryType library variance type
 * @param libraryDescription library variance description
 * @param libraryDependencies library variance dependencies
 * @param libraryExports library variance exported functions
 * @param libraryStatus library variance status
 */
public final class StepLibraryDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String libraryType;
    private final String libraryDescription;
    private final List<StepEntity> libraryDependencies;
    private final List<String> libraryExports;
    private final String libraryStatus;

    public StepLibraryDefinition(int id, String name, String libraryType, String libraryDescription, List<StepEntity> libraryDependencies, List<String> libraryExports, String libraryStatus) {
        this.id = id;
        this.name = name;
        this.libraryType = libraryType;
        this.libraryDescription = libraryDescription;
        this.libraryDependencies = libraryDependencies == null ? null : java.util.List.copyOf(libraryDependencies);
        this.libraryExports = libraryExports == null ? null : java.util.List.copyOf(libraryExports);
        this.libraryStatus = libraryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLibraryType() {
        return libraryType;
    }

    public String getLibraryDescription() {
        return libraryDescription;
    }

    public List<StepEntity> getLibraryDependencies() {
        return libraryDependencies;
    }

    public List<String> getLibraryExports() {
        return libraryExports;
    }

    public String getLibraryStatus() {
        return libraryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLibraryDefinition that = (StepLibraryDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(libraryType, that.libraryType) && Objects.equals(libraryDescription, that.libraryDescription) && Objects.equals(libraryDependencies, that.libraryDependencies) && Objects.equals(libraryExports, that.libraryExports) && Objects.equals(libraryStatus, that.libraryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, libraryType, libraryDescription, libraryDependencies, libraryExports, libraryStatus);
    }

    @Override
    public String toString() {
        return "StepLibraryDefinition{" + "id=" + id + "name=" + name + "libraryType=" + libraryType + "libraryDescription=" + libraryDescription + "libraryDependencies=" + libraryDependencies + "libraryExports=" + libraryExports + "libraryStatus=" + libraryStatus + "}";
    }
}