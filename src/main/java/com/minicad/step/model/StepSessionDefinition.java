package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SESSION_DEFINITION.
 * A session definition entity.
 *
 * @param id STEP instance id
 * @param name session name
 * @param sessionType session variance type
 * @param sessionTimeout session variance timeout in seconds
 * @param sessionMaxInactive session variance max inactive time
 * @param sessionFeatures session variance features
 * @param sessionStatus session variance status
 */
/**
 * Resolved SESSION_DEFINITION.
 * A session definition entity.
 *
 * @param id STEP instance id
 * @param name session name
 * @param sessionType session variance type
 * @param sessionTimeout session variance timeout in seconds
 * @param sessionMaxInactive session variance max inactive time
 * @param sessionFeatures session variance features
 * @param sessionStatus session variance status
 */
public final class StepSessionDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String sessionType;
    private final int sessionTimeout;
    private final int sessionMaxInactive;
    private final List<String> sessionFeatures;
    private final String sessionStatus;

    public StepSessionDefinition(int id, String name, String sessionType, int sessionTimeout, int sessionMaxInactive, List<String> sessionFeatures, String sessionStatus) {
        this.id = id;
        this.name = name;
        this.sessionType = sessionType;
        this.sessionTimeout = sessionTimeout;
        this.sessionMaxInactive = sessionMaxInactive;
        this.sessionFeatures = sessionFeatures == null ? null : java.util.List.copyOf(sessionFeatures);
        this.sessionStatus = sessionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSessionType() {
        return sessionType;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public int getSessionMaxInactive() {
        return sessionMaxInactive;
    }

    public List<String> getSessionFeatures() {
        return sessionFeatures;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSessionDefinition that = (StepSessionDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sessionType, that.sessionType) && sessionTimeout == that.sessionTimeout && sessionMaxInactive == that.sessionMaxInactive && Objects.equals(sessionFeatures, that.sessionFeatures) && Objects.equals(sessionStatus, that.sessionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sessionType, sessionTimeout, sessionMaxInactive, sessionFeatures, sessionStatus);
    }

    @Override
    public String toString() {
        return "StepSessionDefinition{" + "id=" + id + "name=" + name + "sessionType=" + sessionType + "sessionTimeout=" + sessionTimeout + "sessionMaxInactive=" + sessionMaxInactive + "sessionFeatures=" + sessionFeatures + "sessionStatus=" + sessionStatus + "}";
    }
}