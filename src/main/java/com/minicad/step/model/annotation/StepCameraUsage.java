package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;public record StepCameraUsage(int id, String name, String description, StepEntity camera) implements StepEntity {}