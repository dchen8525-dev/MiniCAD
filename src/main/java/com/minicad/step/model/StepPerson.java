package com.minicad.step.model.organization.org.org;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPerson implements StepEntity {
    private final int id;
    private final String identifier;
    private final String lastName;
    private final String firstName;
    private final List<String> middleNames;
    private final List<String> prefixTitles;
    private final List<String> suffixTitles;

    public StepPerson(int id, String identifier, String lastName, String firstName, List<String> middleNames, List<String> prefixTitles, List<String> suffixTitles) {
        this.id = id;
        this.identifier = identifier;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleNames = middleNames == null ? null : java.util.List.copyOf(middleNames);
        this.prefixTitles = prefixTitles == null ? null : java.util.List.copyOf(prefixTitles);
        this.suffixTitles = suffixTitles == null ? null : java.util.List.copyOf(suffixTitles);
    }

    public int getId() {
        return id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPerson that = (StepPerson) o;
        return id == that.id && Objects.equals(identifier, that.identifier) && Objects.equals(lastName, that.lastName) && Objects.equals(firstName, that.firstName) && Objects.equals(middleNames, that.middleNames) && Objects.equals(prefixTitles, that.prefixTitles) && Objects.equals(suffixTitles, that.suffixTitles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, lastName, firstName, middleNames, prefixTitles, suffixTitles);
    }

    @Override
    public String toString() {
        return "StepPerson{" + "id=" + id + "identifier=" + identifier + "lastName=" + lastName + "firstName=" + firstName + "middleNames=" + middleNames + "prefixTitles=" + prefixTitles + "suffixTitles=" + suffixTitles + "}";
    }
}
