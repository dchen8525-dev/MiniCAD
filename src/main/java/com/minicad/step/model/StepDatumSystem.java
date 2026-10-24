package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved DATUM_SYSTEM.
 * A datum system entity with multiple datum references.
 *
 * @param id STEP instance id
 * @param name system name
 * @param datums ordered list of datums in the system
 * @param systemType datum system type classification
 * @param tolerance tolerance that uses this datum system
 */
public final class StepDatumSystem extends AbstractStepEntity {
    private final List<StepEntity> datums;
    private final String systemType;
    private final StepEntity tolerance;

    public StepDatumSystem(int id, String name, List<StepEntity> datums, String systemType, StepEntity tolerance) {
        super(id, name);
        this.datums = datums == null ? null : java.util.List.copyOf(datums);
        this.systemType = systemType;
        this.tolerance = tolerance;
    }

    public List<StepEntity> getDatums() {
        return datums;
    }

    public String getSystemType() {
        return systemType;
    }

    public StepEntity getTolerance() {
        return tolerance;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "datums", datums, "systemType", systemType,
                "tolerance", tolerance);
    }
}
