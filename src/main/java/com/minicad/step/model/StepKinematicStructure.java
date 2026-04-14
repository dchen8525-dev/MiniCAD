package com.minicad.step.model;
public record StepKinematicStructure(int id, String name, String description, StepEntity mechanism) implements StepEntity {}