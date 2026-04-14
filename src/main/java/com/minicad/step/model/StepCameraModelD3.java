package com.minicad.step.model;
public record StepCameraModelD3(int id, String name, StepEntity viewPlane, StepEntity viewReference, double fieldOfView) implements StepEntity {}