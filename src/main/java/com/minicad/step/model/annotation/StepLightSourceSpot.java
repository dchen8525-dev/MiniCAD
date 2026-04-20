package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;public record StepLightSourceSpot(int id, String name, StepEntity color, double intensity, StepEntity position, StepEntity orientation, double concentration, double spreadAngle) implements StepEntity {}