package com.minicad.step.model;
public record StepLightSourceDirectional(int id, String name, StepEntity color, double intensity, StepEntity orientation) implements StepEntity {}