package com.minicad.app;

import com.minicad.step.model.base.StepEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;

import java.util.Map;
import java.util.Objects;

/**
 * Shared compiled STEP pipeline state that can be reused across exporters and diagnostics.
 */
record CompiledStepDocument(
        String stepText,
        StepFile stepFile,
        Map<Integer, StepEntity> resolved,
        StepCadBuilder builder
) {

    CompiledStepDocument {
        Objects.requireNonNull(stepText, "stepText");
        Objects.requireNonNull(stepFile, "stepFile");
        Objects.requireNonNull(resolved, "resolved");
        Objects.requireNonNull(builder, "builder");
    }

    static CompiledStepDocument compile(String stepText) {
        StepFile stepFile = StepParser.parse(stepText);
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        return of(stepText, stepFile, resolved);
    }

    static CompiledStepDocument of(String stepText, StepFile stepFile, Map<Integer, StepEntity> resolved) {
        return new CompiledStepDocument(stepText, stepFile, resolved, StepCadBuilder.fromResolved(resolved));
    }
}
