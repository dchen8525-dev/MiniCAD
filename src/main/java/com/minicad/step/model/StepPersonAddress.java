package com.minicad.step.model;
public record StepPersonAddress(int id, String name, StepEntity person, StepEntity address) implements StepEntity {}