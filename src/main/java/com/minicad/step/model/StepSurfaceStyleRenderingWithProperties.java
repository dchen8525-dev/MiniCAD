package com.minicad.step.model;
import java.util.List;
public record StepSurfaceStyleRenderingWithProperties(int id, String name, List<StepEntity> properties) implements StepEntity { public StepSurfaceStyleRenderingWithProperties { properties = List.copyOf(properties); } }