package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;public record StepPresentationLayerUsage(int id, String name, String description, StepEntity layer) implements StepEntity {}