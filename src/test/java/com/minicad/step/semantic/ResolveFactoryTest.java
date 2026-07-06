package com.minicad.step.semantic;

import com.minicad.step.syntax.*;
import com.minicad.step.model.base.StepEntity;
import org.junit.jupiter.api.Test;
import java.util.Map;

class ResolveFactoryTest {
    @Test
    void testComplexEntityResolveFactory() throws Exception {
        // Parse complex entity STEP text
        String stepText = "DATA;\n"
            + "#12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) GLOBAL_UNIT_ASSIGNED_CONTEXT((#13,#14,#15)) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
            + "#13=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));\n"
            + "#14=(NAMED_UNIT(*) SI_UNIT($,.RADIAN.));\n"
            + "#15=(NAMED_UNIT(*) SI_UNIT($,.STERADIAN.));\n"
            + "ENDSEC;";
        
        StepFile file = StepParser.parse(stepText);
        StepEntityInstance instance = file.entitiesById().get(12);
        
        System.out.println("=== Entity #12 Analysis ===");
        System.out.println("instance.name(): " + instance.name());
        System.out.println("instance.isComplex(): " + instance.isComplex());
        System.out.println("normalizedDefinitionNames: " + instance.normalizedDefinitionNames());
        
        // Check registry
        java.lang.reflect.Field registryField = StepEntityResolver.class.getDeclaredField("REGISTRY");
        registryField.setAccessible(true);
        Map<String, EntityFactory> registry = (Map<String, EntityFactory>) registryField.get(null);
        
        System.out.println("\n=== Registry Check ===");
        for (String normalizedName : instance.normalizedDefinitionNames()) {
            EntityFactory factory = registry.get(normalizedName);
            System.out.println(normalizedName + " -> factory exists: " + (factory != null));
        }
        
        // Try resolve
        try {
            Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(file);
            System.out.println("\n=== Resolution Result ===");
            System.out.println("Resolution SUCCESS!");
            System.out.println("Resolved entity: " + resolved.get(12).getClass().getSimpleName());
        } catch (Exception e) {
            System.out.println("\n=== Resolution Result ===");
            System.out.println("Resolution FAILED: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
}
