package com.minicad.step.model;
public record StepRenderingProperties(int id, String name, double specularExponent, double specularRoughness) implements StepEntity {}