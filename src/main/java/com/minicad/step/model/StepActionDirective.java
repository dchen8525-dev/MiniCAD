package com.minicad.step.model;
public record StepActionDirective(int id, String name, String description, String directive) implements StepEntity {}