package com.minicad.step.model;
public record StepOrganizationAddress(int id, String name, StepEntity organization, StepEntity address) implements StepEntity {}