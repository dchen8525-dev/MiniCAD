package com.minicad.step.model;
public record StepPlanarBox(int id, String name, StepEntity placement, double width, double height) implements StepEntity {}