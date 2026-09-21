package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPerson extends AbstractStepEntity {
    private final String identifier;
    private final String lastName;
    private final String firstName;
    private final List<String> middleNames;
    private final List<String> prefixTitles;
    private final List<String> suffixTitles;

    public StepPerson(int id, String identifier, String lastName, String firstName, List<String> middleNames, List<String> prefixTitles, List<String> suffixTitles) {
        super(id, "");
        this.identifier = identifier;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleNames = middleNames == null ? null : java.util.List.copyOf(middleNames);
        this.prefixTitles = prefixTitles == null ? null : java.util.List.copyOf(prefixTitles);
        this.suffixTitles = suffixTitles == null ? null : java.util.List.copyOf(suffixTitles);
    }

    public String getName() {
        // Convention: present the person's full name from first/last name fields
        // when available. Returns the empty string when neither is supplied;
        // the raw STEP identifier remains available via {@link #getIdentifier()}.
        boolean hasFirst = firstName != null && !firstName.isEmpty();
        boolean hasLast = lastName != null && !lastName.isEmpty();
        if (hasFirst && hasLast) {
            return firstName + " " + lastName;
        } else if (hasFirst) {
            return firstName;
        } else if (hasLast) {
            return lastName;
        }
        return "";
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public List<String> getMiddleNames() {
        return middleNames;
    }

    public List<String> getPrefixTitles() {
        return prefixTitles;
    }

    public List<String> getSuffixTitles() {
        return suffixTitles;
    }

    // Record-style accessors
    public List<String> middleNames() {
        return middleNames;
    }

    public List<String> prefixTitles() {
        return prefixTitles;
    }

    public List<String> suffixTitles() {
        return suffixTitles;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("identifier", identifier);
        state.put("lastName", lastName);
        state.put("firstName", firstName);
        state.put("middleNames", middleNames);
        state.put("prefixTitles", prefixTitles);
        state.put("suffixTitles", suffixTitles);
        return state;
    }
}
