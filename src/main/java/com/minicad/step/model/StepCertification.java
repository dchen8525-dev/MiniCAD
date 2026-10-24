package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal CERTIFICATION metadata.
 *
 * @param id STEP instance id
 * @param name certification name
 * @param purpose certification purpose
 * @param kind certification type
 */
public final class StepCertification extends AbstractStepEntity {
    private final String purpose;
    private final StepCertificationType kind;

    public StepCertification(int id, String name, String purpose, StepCertificationType kind) {
        super(id, name);
        this.purpose = purpose;
        this.kind = kind;
    }

    public String getPurpose() {
        return purpose;
    }

    public StepCertificationType getKind() {
        return kind;
    }

    // Record-style accessor
    public StepCertificationType kind() {
        return kind;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "purpose", purpose, "kind", kind);
    }
}
