package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPlatformInstance extends AbstractStepEntity {
    private final StepEntity platformDefinition;
    private final String platformState;
    private final String platformVersion;
    private final String platformHealth;
    private final String platformStatus;

    public StepPlatformInstance(int id, String name, StepEntity platformDefinition, String platformState, String platformVersion, String platformHealth, String platformStatus) {
        super(id, name);
        this.platformDefinition = platformDefinition;
        this.platformState = platformState;
        this.platformVersion = platformVersion;
        this.platformHealth = platformHealth;
        this.platformStatus = platformStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("platformDefinition", platformDefinition);
        state.put("platformState", platformState);
        state.put("platformVersion", platformVersion);
        state.put("platformHealth", platformHealth);
        state.put("platformStatus", platformStatus);
        return state;
    }
}
