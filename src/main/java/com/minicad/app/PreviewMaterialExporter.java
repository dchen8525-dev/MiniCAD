package com.minicad.app;

import com.minicad.step.model.annotation.*;
import com.minicad.step.model.base.StepEntity;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Material and style export helpers for STEP preview export.
 * Extracted from StepPreviewJsonExporter to isolate material/style/color handling logic.
 */
final class PreviewMaterialExporter {

    private PreviewMaterialExporter() {}

    // ─── Standalone material target collection methods ───────────────────────────────

    /**
     * Collects targets for a style colour entity.
     * Handles FillAreaStyleColour, CurveStyle, PointStyle, TextStyleForDefinedFont,
     * SymbolColour, and SurfaceStyleReflectanceAmbientDiffuseSpecular colour references.
     */
    static Set<StepEntity> collectTargetsForStyleColour(
            int colourId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting,
            SemanticTargetsCollector collector
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepFillAreaStyleColour
                    && ((StepFillAreaStyleColour) candidate).colour().id() == colourId) {
                StepFillAreaStyleColour fillAreaStyleColour = (StepFillAreaStyleColour) candidate;
                targets.addAll(collector.collect(fillAreaStyleColour, resolved, visiting));
            } else if (candidate instanceof StepCurveStyle
                    && ((StepCurveStyle) candidate).colour().id() == colourId) {
                StepCurveStyle curveStyle = (StepCurveStyle) candidate;
                targets.addAll(collector.collect(curveStyle, resolved, visiting));
            } else if (candidate instanceof StepPointStyle
                    && ((StepPointStyle) candidate).colour().id() == colourId) {
                StepPointStyle pointStyle = (StepPointStyle) candidate;
                targets.addAll(collector.collect(pointStyle, resolved, visiting));
            } else if (candidate instanceof StepTextStyleForDefinedFont
                    && ((StepTextStyleForDefinedFont) candidate).textColour().id() == colourId) {
                StepTextStyleForDefinedFont textStyle = (StepTextStyleForDefinedFont) candidate;
                targets.addAll(collector.collect(textStyle, resolved, visiting));
            } else if (candidate instanceof StepSymbolColour
                    && ((StepSymbolColour) candidate).colour().id() == colourId) {
                StepSymbolColour symbolColour = (StepSymbolColour) candidate;
                targets.addAll(collector.collect(symbolColour, resolved, visiting));
            } else if (candidate instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular
                    && ((StepSurfaceStyleReflectanceAmbientDiffuseSpecular) candidate).specularColour().id() == colourId) {
                StepSurfaceStyleReflectanceAmbientDiffuseSpecular style = (StepSurfaceStyleReflectanceAmbientDiffuseSpecular) candidate;
                targets.addAll(collector.collect(style, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    /**
     * Collects targets for a curve font entity.
     * Handles CurveStyle curveFont references.
     */
    static Set<StepEntity> collectTargetsForCurveFont(
            int curveFontId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting,
            SemanticTargetsCollector collector
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepCurveStyle && ((StepCurveStyle) candidate).curveFont().id() == curveFontId) {
                StepCurveStyle curveStyle = (StepCurveStyle) candidate;
                targets.addAll(collector.collect(curveStyle, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    /**
     * Collects targets for a point marker entity.
     * Handles PointStyle marker references.
     */
    static Set<StepEntity> collectTargetsForPointMarker(
            int markerId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting,
            SemanticTargetsCollector collector
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPointStyle && ((StepPointStyle) candidate).marker().id() == markerId) {
                StepPointStyle pointStyle = (StepPointStyle) candidate;
                targets.addAll(collector.collect(pointStyle, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    // ─── Functional interface for semantic targets collection ───────────────────────────────

    /**
     * Functional interface to delegate semantic targets collection to the parent class.
     */
    @FunctionalInterface
    interface SemanticTargetsCollector {
        Set<StepEntity> collect(StepEntity entity, Map<Integer, StepEntity> resolved, Set<Integer> visiting);
    }
}