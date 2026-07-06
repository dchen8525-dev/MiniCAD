package com.minicad.test;

import com.minicad.step.syntax.StepParser;
import com.minicad.step.syntax.StepFile;
import com.minicad.common.StepParseException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class test_real_step {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: test_real_step <file.step>");
            System.out.println("Testing available examples...");
            testExamples();
            return;
        }
        testFile(args[0]);
    }
    
    private static void testExamples() throws Exception {
        String[] examples = {
            "examples/minimal-square.step",
            "examples/bspline-patch.step",
            "examples/conical-band.step",
            "examples/cylindrical-band.step"
        };
        
        System.out.println("=== Real-world STEP File Parsing Test ===\n");
        
        int success = 0;
        int total = 0;
        
        for (String example : examples) {
            total++;
            try {
                Path path = Path.of(example);
                if (!Files.exists(path)) {
                    System.out.println("⚠️  File not found: " + example);
                    continue;
                }
                
                String content = Files.readString(path);
                StepFile file = StepParser.parse(content);
                success++;
                
                System.out.println("✅ " + example);
                System.out.println("   Entities: " + file.entities().size());
                System.out.println("   Headers: " + file.headerEntries().size());
                
                // Show entity types
                if (file.entities().size() > 0 && file.entities().size() <= 10) {
                    System.out.println("   Types: " + 
                        file.entities().stream()
                            .map(e -> e.type())
                            .distinct()
                            .collect(Collectors.joining(", ")));
                }
            } catch (StepParseException e) {
                System.out.println("❌ " + example);
                System.out.println("   Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ " + example);
                System.out.println("   Error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            System.out.println();
        }
        
        System.out.println("=== Summary ===");
        System.out.println("Success: " + success + "/" + total);
        System.out.println("Rate: " + (success * 100.0 / total) + "%");
    }
    
    private static void testFile(String filename) throws Exception {
        String content = Files.readString(Path.of(filename));
        StepFile file = StepParser.parse(content);
        System.out.println("✅ Parse success: " + filename);
        System.out.println("Entities: " + file.entities().size());
        System.out.println("Headers: " + file.headerEntries().size());
    }
}
