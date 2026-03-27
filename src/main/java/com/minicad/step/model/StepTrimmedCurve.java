package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TRIMMED_CURVE for supported basis curves.
 *
 * @param id step id
 * @param name step label
 * @param basisCurve basis curve
 * @param trim1 first trim list
 * @param trim2 second trim list
 * @param senseAgreement orientation agreement
 * @param masterRepresentation trimming preference enum
 */
public record StepTrimmedCurve(
        int id,
        String name,
        StepEntity basisCurve,
        List<StepEntity> trim1,
        List<StepEntity> trim2,
        boolean senseAgreement,
        String masterRepresentation
) implements StepEntity {

    /**
     * Creates an immutable trimmed-curve record.
     */
    public StepTrimmedCurve {
        trim1 = List.copyOf(trim1);
        trim2 = List.copyOf(trim2);
    }
}
