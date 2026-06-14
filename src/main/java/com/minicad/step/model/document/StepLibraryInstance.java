package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepLibraryInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity libraryDefinition;
    private final String libraryState;
    private final String libraryVersion;
    private final boolean libraryLoaded;
    private final String libraryStatus;

    public StepLibraryInstance(int id, String name, StepEntity libraryDefinition, String libraryState, String libraryVersion, boolean libraryLoaded, String libraryStatus) {
        this.id = id;
        this.name = name;
        this.libraryDefinition = libraryDefinition;
        this.libraryState = libraryState;
        this.libraryVersion = libraryVersion;
        this.libraryLoaded = libraryLoaded;
        this.libraryStatus = libraryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLibraryInstance that = (StepLibraryInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(libraryDefinition, that.libraryDefinition) && Objects.equals(libraryState, that.libraryState) && Objects.equals(libraryVersion, that.libraryVersion) && libraryLoaded == that.libraryLoaded && Objects.equals(libraryStatus, that.libraryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, libraryDefinition, libraryState, libraryVersion, libraryLoaded, libraryStatus);
    }

    @Override
    public String toString() {
        return "StepLibraryInstance{" + "id=" + id + "name=" + name + "libraryDefinition=" + libraryDefinition + "libraryState=" + libraryState + "libraryVersion=" + libraryVersion + "libraryLoaded=" + libraryLoaded + "libraryStatus=" + libraryStatus + "}";
    }
}