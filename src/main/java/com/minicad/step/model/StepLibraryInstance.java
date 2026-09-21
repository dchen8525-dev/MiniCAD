package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LIBRARY_INSTANCE.
 * A library instance entity.
 *
 * @param id STEP instance id
 * @param name library instance name
 * @param libraryDefinition library variance definition reference
 * @param libraryState library variance state
 * @param libraryVersion library variance version
 * @param libraryLoaded library variance loaded flag
 * @param libraryStatus library variance status
 */
public final class StepLibraryInstance extends AbstractStepEntity {
    private final StepEntity libraryDefinition;
    private final String libraryState;
    private final String libraryVersion;
    private final boolean libraryLoaded;
    private final String libraryStatus;

    public StepLibraryInstance(int id, String name, StepEntity libraryDefinition, String libraryState, String libraryVersion, boolean libraryLoaded, String libraryStatus) {
        super(id, name);
        this.libraryDefinition = libraryDefinition;
        this.libraryState = libraryState;
        this.libraryVersion = libraryVersion;
        this.libraryLoaded = libraryLoaded;
        this.libraryStatus = libraryStatus;
    }

    public StepEntity getLibraryDefinition() {
        return libraryDefinition;
    }

    public String getLibraryState() {
        return libraryState;
    }

    public String getLibraryVersion() {
        return libraryVersion;
    }

    public boolean isLibraryLoaded() {
        return libraryLoaded;
    }

    public String getLibraryStatus() {
        return libraryStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("libraryDefinition", libraryDefinition);
        state.put("libraryState", libraryState);
        state.put("libraryVersion", libraryVersion);
        state.put("libraryLoaded", libraryLoaded);
        state.put("libraryStatus", libraryStatus);
        return state;
    }
}
