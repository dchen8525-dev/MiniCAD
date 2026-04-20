package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;import java.util.List;
public record StepSurfaceStyleRenderingWithProperties(int id, String name, List<StepEntity> properties) implements StepEntity { public StepSurfaceStyleRenderingWithProperties { properties = List.copyOf(properties); } }