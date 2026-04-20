package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;public record StepOrganizationAddress(int id, String name, StepEntity organization, StepEntity address) implements StepEntity {}