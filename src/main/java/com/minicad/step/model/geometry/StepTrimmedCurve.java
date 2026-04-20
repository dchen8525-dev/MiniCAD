package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import com.minicad.step.syntax.StepValue;
import java.util.List;

/**
 * Resolved TRIMMED_CURVE for supported basis curves.
 * Trim values can be entity references (Cartesian points) or parameter values (numeric literals).
 *
 * @param id step id
 * @param name step label
 * @param basisCurve basis curve
 * @param trim1 first trim list (entity references or numeric parameter values)
 * @param trim2 second trim list (entity references or numeric parameter values)
 * @param senseAgreement orientation agreement
 * @param masterRepresentation trimming preference enum
 */
public record StepTrimmedCurve(
        int id,
        String name,
        StepEntity basisCurve,
        List<StepValue> trim1,
        List<StepValue> trim2,
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
