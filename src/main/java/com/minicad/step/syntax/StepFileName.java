package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

import java.util.List;
import java.util.Objects;

/**
 * Parsed HEADER section FileName entry.
 * Contains file name, timestamp, author, organization, etc.
 */
/**
 * Parsed HEADER section FileName entry.
 * Contains file name, timestamp, author, organization, etc.
 */
public final class StepFileName {
    private final String name;
    private final String timeStamp;
    private final List<String> author;
    private final List<String> organization;
    private final String preprocessorVersion;
    private final String originatingSystem;
    private final String authorization;

    public StepFileName(String name, String timeStamp, List<String> author, List<String> organization, String preprocessorVersion, String originatingSystem, String authorization) {
        this.name = name;
        this.timeStamp = timeStamp;
        this.author = author == null ? null : java.util.List.copyOf(author);
        this.organization = organization == null ? null : java.util.List.copyOf(organization);
        this.preprocessorVersion = preprocessorVersion;
        this.originatingSystem = originatingSystem;
        this.authorization = authorization;
    }

    public String getName() {
        return name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public List<String> getAuthor() {
        return author;
    }

    public List<String> getOrganization() {
        return organization;
    }

    public String getPreprocessorVersion() {
        return preprocessorVersion;
    }

    public String getOriginatingSystem() {
        return originatingSystem;
    }

    public String getAuthorization() {
        return authorization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFileName that = (StepFileName) o;
        return Objects.equals(name, that.name) && Objects.equals(timeStamp, that.timeStamp) && Objects.equals(author, that.author) && Objects.equals(organization, that.organization) && Objects.equals(preprocessorVersion, that.preprocessorVersion) && Objects.equals(originatingSystem, that.originatingSystem) && Objects.equals(authorization, that.authorization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, timeStamp, author, organization, preprocessorVersion, originatingSystem, authorization);
    }

    @Override
    public String toString() {
        return "StepFileName{" + "name=" + name + "timeStamp=" + timeStamp + "author=" + author + "organization=" + organization + "preprocessorVersion=" + preprocessorVersion + "originatingSystem=" + originatingSystem + "authorization=" + authorization + "}";
    }
}
