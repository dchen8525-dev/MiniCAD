package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;public record StepCameraModelD3(int id, String name, StepEntity viewPlane, StepEntity viewReference, double fieldOfView) implements StepEntity {}