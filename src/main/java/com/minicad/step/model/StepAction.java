package com.minicad.step.model;
public record StepAction(int id, String name, String description, String actionMethod) implements StepEntity {}