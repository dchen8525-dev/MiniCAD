package com.minicad.step.model;
public record StepKinematicJoint(int id, String name, String description, StepEntity jointGeometry) implements StepEntity {}