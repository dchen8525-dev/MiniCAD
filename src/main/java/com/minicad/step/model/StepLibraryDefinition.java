package com.minicad.step.model;

import java.util.List;

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
public record StepLibraryDefinition(
    int id,
    String name,
    String libraryType,
    String libraryDescription,
    List<StepEntity> libraryDependencies,
    List<String> libraryExports,
    String libraryStatus) implements StepEntity {
}