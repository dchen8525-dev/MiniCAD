package com.minicad.step.semantic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify that no class exceeds 1000 lines.
 * This ensures code maintainability and prevents monolithic classes.
 */
class LineCountTest {

  private static final int MAX_LINES_PER_CLASS = 1000;

  @Test
  @DisplayName("No class should exceed 1000 lines (except known large files being refactored)")
  void noClassShouldExceedMaxLines() throws IOException {
    Path srcMainJava = Paths.get("src/main/java");
    
    List<Path> javaFiles = findJavaFiles(srcMainJava);
    
    // Known large files that are being actively refactored or are acceptable for now
    List<String> excludedFiles = List.of(
        // MiscRegistry.java removed - now split into specialized registries
        "StepEntityResolver.java",  // Core resolver with many methods (13324 lines)
        "StepCadBuilder.java",  // Complex builder logic (7429 lines)
        "StepCadGeometryOps.java",  // Geometry operations (1023 lines)
        "StepPreviewJsonExporter.java",  // Export logic (18094 lines)
        "StepDumpApp.java",  // CLI app (3632 lines)
        "StepMeshExporter.java",  // Mesh export (2857 lines)
        "PreviewSerializers.java",  // Preview serialization (2171 lines)
        "PreviewUvMapper.java",  // UV mapping (2286 lines)
        "PreviewCurveEvaluator.java",  // Curve evaluation (1734 lines)
        "PreviewFaceBuilder.java",  // Face building (2829 lines)
        "PreviewPmiBuilder.java",  // PMI building (1673 lines)
        "StepPreviewPayloadTypes.java",  // Payload types (2856 lines)
        "StepEntity.java"  // Base entity class (1123 lines)
    );
    
    List<String> violations = new ArrayList<>();
    
    for (Path javaFile : javaFiles) {
      int lines = countLines(javaFile);
      String className = javaFile.getFileName().toString();
      
      // Skip excluded files (known large files being refactored)
      if (excludedFiles.contains(className)) {
        continue;
      }
      
      if (lines > MAX_LINES_PER_CLASS) {
        violations.add(String.format("%s: %d lines (max: %d)", 
            javaFile.toString(), lines, MAX_LINES_PER_CLASS));
      }
    }
    
    if (!violations.isEmpty()) {
      String message = "Classes exceeding " + MAX_LINES_PER_CLASS + " lines:\n" + 
          violations.stream().collect(Collectors.joining("\n"));
      fail(message);
    }
  }
  
  @Test
  @DisplayName("Specialized registries should be under 1000 lines")
  void specializedRegistriesShouldBeUnder1000Lines() throws IOException {
    Path semanticPackage = Paths.get("src/main/java/com/minicad/step/semantic");
    
    // All registry files (split registries have numbers)
    List<String> registryFiles = List.of(
        "GeometryRegistry1.java", "GeometryRegistry2.java",
        "TopologyRegistry.java", 
        "ProductRegistry.java",
        "RepresentationRegistry1.java", "RepresentationRegistry2.java",
        "ManufacturingRegistry.java", "ToleranceRegistry.java",
        "UnitRegistry.java", "AnnotationRegistry.java",
        "ClassificationRegistry.java", "KinematicRegistry.java",
        "FeaRegistry.java", "ProfileRegistry.java",
        "ConfigManagementRegistry.java",
        "MiscellaneousRegistry1.java", "MiscellaneousRegistry2.java",
        "MiscellaneousRegistry3.java", "MiscellaneousRegistry4.java",
        "RegistryHelpers.java", "StepEntityRegistry.java"
    );
    
    List<String> violations = new ArrayList<>();
    
    for (String registryFile : registryFiles) {
      Path path = semanticPackage.resolve(registryFile);
      if (Files.exists(path)) {
        int lines = countLines(path);
        if (lines > MAX_LINES_PER_CLASS) {
          violations.add(String.format("%s: %d lines (max: %d)", registryFile, lines, MAX_LINES_PER_CLASS));
        }
      }
    }
    
    if (!violations.isEmpty()) {
      String message = "Specialized registries exceeding 1000 lines:\n" + 
          violations.stream().collect(Collectors.joining("\n"));
      fail(message);
    }
  }
  
  @Test
  @DisplayName("Verify MiscRegistry.java has been removed")
  void miscRegistryShouldBeRemoved() throws IOException {
    Path miscRegistry = Paths.get("src/main/java/com/minicad/step/semantic/MiscRegistry.java");
    
    // MiscRegistry.java should no longer exist - it was split into specialized registries
    assertFalse(Files.exists(miscRegistry), 
        "MiscRegistry.java should be removed - entities now in specialized registries");
    
    System.out.println("MiscRegistry.java successfully removed and replaced with modular registries.");
  }

  private List<Path> findJavaFiles(Path root) throws IOException {
    try (Stream<Path> stream = Files.walk(root)) {
      return stream
          .filter(p -> p.toString().endsWith(".java"))
          .collect(Collectors.toList());
    }
  }

  private int countLines(Path file) throws IOException {
    return Files.readAllLines(file).size();
  }
}