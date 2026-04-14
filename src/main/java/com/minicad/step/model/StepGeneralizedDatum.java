package com.minicad.step.model;
public record StepGeneralizedDatum(int id, String name, String description, StepEntity datumTarget) implements StepEntity {}