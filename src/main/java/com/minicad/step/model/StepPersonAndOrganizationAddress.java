package com.minicad.step.model;
public record StepPersonAndOrganizationAddress(int id, String name, StepEntity personAndOrganization, StepEntity address) implements StepEntity {}