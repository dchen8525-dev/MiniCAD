package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STANDARD_DEFINITION.
 * A standard definition entity.
 *
 * @param id STEP instance id
 * @param name standard name
 * @param standardType standard variance type
 * @param standardCode standard variance code/identifier
 * @param standardVersion standard variance version
 * @param standardRequirements standard variance requirements
 * @param standardStatus standard variance status
 */
public final class StepStandardDefinition extends AbstractStepEntity {
    private final String standardType;
    private final String standardCode;
    private final String standardVersion;
    private final List<String> standardRequirements;
    private final String standardStatus;

    public StepStandardDefinition(int id, String name, String standardType, String standardCode, String standardVersion, List<String> standardRequirements, String standardStatus) {
        super(id, name);
        this.standardType = standardType;
        this.standardCode = standardCode;
        this.standardVersion = standardVersion;
        this.standardRequirements = standardRequirements == null ? null : java.util.List.copyOf(standardRequirements);
        this.standardStatus = standardStatus;
    }

    public String getStandardType() {
        return standardType;
    }

    public String getStandardCode() {
        return standardCode;
    }

    public String getStandardVersion() {
        return standardVersion;
    }

    public List<String> getStandardRequirements() {
        return standardRequirements;
    }

    public String getStandardStatus() {
        return standardStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("standardType", standardType);
        state.put("standardCode", standardCode);
        state.put("standardVersion", standardVersion);
        state.put("standardRequirements", standardRequirements);
        state.put("standardStatus", standardStatus);
        return state;
    }
}
