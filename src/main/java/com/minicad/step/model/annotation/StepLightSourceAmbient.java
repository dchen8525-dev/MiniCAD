package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;public record StepLightSourceAmbient(int id, String name, StepEntity color, double intensity) implements StepEntity {}