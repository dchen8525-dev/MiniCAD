package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;public record StepLightSourceDirectional(int id, String name, StepEntity color, double intensity, StepEntity orientation) implements StepEntity {}