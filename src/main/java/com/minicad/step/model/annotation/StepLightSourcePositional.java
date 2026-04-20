package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;public record StepLightSourcePositional(int id, String name, StepEntity color, double intensity, StepEntity position) implements StepEntity {}