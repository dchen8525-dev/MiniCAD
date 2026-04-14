package com.minicad.step.model;
public record StepKinematicLink(int id, String name, String description, StepEntity shape) implements StepEntity {}