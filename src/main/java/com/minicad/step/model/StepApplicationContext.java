package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal application context.
 *
 * @param id STEP instance id
 * @param application application domain text
 */
public final class StepApplicationContext extends AbstractStepEntity {
    private final String application;

    public StepApplicationContext(int id, String application) {
        super(id, "");
        this.application = application;
    }

    public String getApplication() {
        return application;
    }

    public String getName() {
        return application != null ? application : "";
    }

    // Record-style accessor - name from application
    public String name() {
        return application;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "application", application);
    }
}
