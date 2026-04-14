package com.minicad.step.model;
public record StepCameraImage(int id, String name, String description, int horizontalResolution, int verticalResolution) implements StepEntity {}