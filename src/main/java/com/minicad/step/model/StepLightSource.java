package com.minicad.step.model;
public record StepLightSource(int id, String name, StepEntity color, double intensity) implements StepEntity {}