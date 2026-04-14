package com.minicad.step.model;
public record StepLightSourcePositional(int id, String name, StepEntity color, double intensity, StepEntity position) implements StepEntity {}