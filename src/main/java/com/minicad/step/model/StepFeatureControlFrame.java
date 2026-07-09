package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FEATURE_CONTROL_FRAME.
 * A GD&T feature control frame containing tolerances and datum references.
 *
 * @param id STEP instance id
 * @param name frame name
 * @param datumSystem datum references
 * @param tolerance the geometric tolerance value
 */
/**
 * Resolved FEATURE_CONTROL_FRAME.
 * A GD&T feature control frame containing tolerances and datum references.
 *
 * @param id STEP instance id
 * @param name frame name
 * @param datumSystem datum references
 * @param tolerance the geometric tolerance value
 */
public final class StepFeatureControlFrame implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> datumSystem;
    private final StepEntity tolerance;

    public StepFeatureControlFrame(int id, String name, List<StepEntity> datumSystem, StepEntity tolerance) {
        this.id = id;
        this.name = name;
        this.datumSystem = datumSystem == null ? null : java.util.List.copyOf(datumSystem);
        this.tolerance = tolerance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getDatumSystem() {
        return datumSystem;
    }

    public StepEntity getTolerance() {
        return tolerance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeatureControlFrame that = (StepFeatureControlFrame) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(datumSystem, that.datumSystem) && Objects.equals(tolerance, that.tolerance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, datumSystem, tolerance);
    }

    @Override
    public String toString() {
        return "StepFeatureControlFrame{" + "id=" + id + "name=" + name + "datumSystem=" + datumSystem + "tolerance=" + tolerance + "}";
    }
}
