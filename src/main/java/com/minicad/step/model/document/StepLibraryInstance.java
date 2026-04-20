package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepLibraryInstance(
    int id,
    String name,
    StepEntity libraryDefinition,
    String libraryState,
    String libraryVersion,
    boolean libraryLoaded,
    String libraryStatus) implements StepEntity {
}