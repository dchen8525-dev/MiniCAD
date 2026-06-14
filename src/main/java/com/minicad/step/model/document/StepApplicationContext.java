package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal application context.
 *
 * @param id STEP instance id
 * @param application application domain text
 */
/**
 * Minimal application context.
 *
 * @param id STEP instance id
 * @param application application domain text
 */
public final class StepApplicationContext implements StepEntity {
    private final int id;
    private final String application;

    public StepApplicationContext(int id, String application) {
        this.id = id;
        this.application = application;
    }

    public int getId() {
        return id;
    }

    public String getApplication() {
        return application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepApplicationContext that = (StepApplicationContext) o;
        return id == that.id && Objects.equals(application, that.application);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, application);
    }

    @Override
    public String toString() {
        return "StepApplicationContext{" + "id=" + id + "application=" + application + "}";
    }
}
