package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;public record StepSurfaceStyleRendering(int id, String name, StepEntity surfaceStyle, double transparency, double diffuseReflection, double specularReflection) implements StepEntity {}