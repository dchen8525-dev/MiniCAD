package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;public record StepRenderingProperties(int id, String name, double specularExponent, double specularRoughness) implements StepEntity {}