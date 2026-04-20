package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;public record StepPersonAndOrganizationAddress(int id, String name, StepEntity personAndOrganization, StepEntity address) implements StepEntity {}