package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;public record StepPlanarBox(int id, String name, StepEntity placement, double width, double height) implements StepEntity {}