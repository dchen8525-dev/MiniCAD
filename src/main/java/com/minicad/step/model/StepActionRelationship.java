package com.minicad.step.model;
public record StepActionRelationship(int id, String name, String description, StepEntity relatingAction, StepEntity relatedAction) implements StepEntity {}