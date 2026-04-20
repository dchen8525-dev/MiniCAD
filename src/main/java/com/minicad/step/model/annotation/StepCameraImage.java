package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;public record StepCameraImage(int id, String name, String description, int horizontalResolution, int verticalResolution) implements StepEntity {}