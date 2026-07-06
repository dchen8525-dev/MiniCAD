package com.minicad.step.semantic;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.lang.reflect.Field;

class RegistryTest {
    @Test
    void checkRegistryOrder() throws Exception {
        Field registryField = StepEntityResolver.class.getDeclaredField("REGISTRY");
        registryField.setAccessible(true);
        Map<String, EntityFactory> registry = (Map<String, EntityFactory>) registryField.get(null);
        
        int index = 0;
        boolean foundGeometric = false;
        boolean foundGlobal = false;
        boolean foundRepresentation = false;
        
        for (String name : registry.keySet()) {
            if ("GEOMETRIC_REPRESENTATION_CONTEXT".equals(name)) {
                System.out.println("GEOMETRIC_REPRESENTATION_CONTEXT rank: " + index);
                foundGeometric = true;
            }
            if ("GLOBAL_UNIT_ASSIGNED_CONTEXT".equals(name)) {
                System.out.println("GLOBAL_UNIT_ASSIGNED_CONTEXT rank: " + index);
                foundGlobal = true;
            }
            if ("REPRESENTATION_CONTEXT".equals(name)) {
                System.out.println("REPRESENTATION_CONTEXT rank: " + index);
                foundRepresentation = true;
            }
            index++;
        }
        
        System.out.println("\nExistence check:");
        System.out.println("GEOMETRIC_REPRESENTATION_CONTEXT: " + foundGeometric);
        System.out.println("GLOBAL_UNIT_ASSIGNED_CONTEXT: " + foundGlobal);
        System.out.println("REPRESENTATION_CONTEXT: " + foundRepresentation);
    }
}
