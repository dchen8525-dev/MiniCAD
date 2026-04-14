package com.minicad.step.model;
public record StepCameraUsage(int id, String name, String description, StepEntity camera) implements StepEntity {}