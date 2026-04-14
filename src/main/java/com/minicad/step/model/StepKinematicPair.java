package com.minicad.step.model;
public record StepKinematicPair(int id, String name, String description, StepEntity link1, StepEntity link2) implements StepEntity {}