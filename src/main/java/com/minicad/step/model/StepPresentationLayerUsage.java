package com.minicad.step.model;
public record StepPresentationLayerUsage(int id, String name, String description, StepEntity layer) implements StepEntity {}