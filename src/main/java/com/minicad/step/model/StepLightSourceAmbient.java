package com.minicad.step.model;
public record StepLightSourceAmbient(int id, String name, StepEntity color, double intensity) implements StepEntity {}