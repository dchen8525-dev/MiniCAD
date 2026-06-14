package com.minicad.step.model.system;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PLATFORM_INSTANCE.
 * A platform instance entity.
 *
 * @param id STEP instance id
 * @param name platform instance name
 * @param platformDefinition platform variance definition reference
 * @param platformState platform variance state
 * @param platformVersion platform variance version
 * @param platformHealth platform variance health status
 * @param platformStatus platform variance status
 */
/**
 * Resolved PLATFORM_INSTANCE.
 * A platform instance entity.
 *
 * @param id STEP instance id
 * @param name platform instance name
 * @param platformDefinition platform variance definition reference
 * @param platformState platform variance state
 * @param platformVersion platform variance version
 * @param platformHealth platform variance health status
 * @param platformStatus platform variance status
 */
public final class StepPlatformInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity platformDefinition;
    private final String platformState;
    private final String platformVersion;
    private final String platformHealth;
    private final String platformStatus;

    public StepPlatformInstance(int id, String name, StepEntity platformDefinition, String platformState, String platformVersion, String platformHealth, String platformStatus) {
        this.id = id;
        this.name = name;
        this.platformDefinition = platformDefinition;
        this.platformState = platformState;
        this.platformVersion = platformVersion;
        this.platformHealth = platformHealth;
        this.platformStatus = platformStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPlatformDefinition() {
        return platformDefinition;
    }

    public String getPlatformState() {
        return platformState;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getPlatformHealth() {
        return platformHealth;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPlatformInstance that = (StepPlatformInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(platformDefinition, that.platformDefinition) && Objects.equals(platformState, that.platformState) && Objects.equals(platformVersion, that.platformVersion) && Objects.equals(platformHealth, that.platformHealth) && Objects.equals(platformStatus, that.platformStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, platformDefinition, platformState, platformVersion, platformHealth, platformStatus);
    }

    @Override
    public String toString() {
        return "StepPlatformInstance{" + "id=" + id + "name=" + name + "platformDefinition=" + platformDefinition + "platformState=" + platformState + "platformVersion=" + platformVersion + "platformHealth=" + platformHealth + "platformStatus=" + platformStatus + "}";
    }
}