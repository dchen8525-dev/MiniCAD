package com.minicad.step.model;

import java.util.List;

/**
 * Minimal PERSON metadata.
 *
 * @param id STEP instance id
 * @param identifier person identifier
 * @param lastName last name
 * @param firstName first name
 * @param middleNames middle names
 * @param prefixTitles prefix titles
 * @param suffixTitles suffix titles
 */
public record StepPerson(
        int id,
        String identifier,
        String lastName,
        String firstName,
        List<String> middleNames,
        List<String> prefixTitles,
        List<String> suffixTitles
) implements StepEntity {

    public StepPerson {
        middleNames = List.copyOf(middleNames);
        prefixTitles = List.copyOf(prefixTitles);
        suffixTitles = List.copyOf(suffixTitles);
    }

    @Override
    public String name() {
        return String.join(" ", List.of(firstName, lastName)).trim();
    }
}
