package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepLibraryDefinition extends AbstractStepEntity {
    private final String libraryType;
    private final String libraryDescription;
    private final List<StepEntity> libraryDependencies;
    private final List<String> libraryExports;
    private final String libraryStatus;

    public StepLibraryDefinition(int id, String name, String libraryType, String libraryDescription, List<StepEntity> libraryDependencies, List<String> libraryExports, String libraryStatus) {
        super(id, name);
        this.libraryType = libraryType;
        this.libraryDescription = libraryDescription;
        this.libraryDependencies = libraryDependencies == null ? null : java.util.List.copyOf(libraryDependencies);
        this.libraryExports = libraryExports == null ? null : java.util.List.copyOf(libraryExports);
        this.libraryStatus = libraryStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("libraryType", libraryType);
        state.put("libraryDescription", libraryDescription);
        state.put("libraryDependencies", libraryDependencies);
        state.put("libraryExports", libraryExports);
        state.put("libraryStatus", libraryStatus);
        return state;
    }
}
