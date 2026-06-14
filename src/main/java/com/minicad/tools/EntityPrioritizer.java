package com.minicad.tools;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;

/**
 * Entity Prioritizer - Categorizes missing entities and generates priority queue.
 *
 * Inputs:
 * - Missing entities list (generated/missing_entities.txt)
 * - Entity catalog from EXPRESS parser (generated/ap242-entity-catalog.json)
 * - Entity frequency from industrial files (generated/entity_frequency.txt)
 *
 * Outputs:
 * - Priority queue: generated/priority-queue.tsv
 * - Complexity categorization: generated/entity-complexity.tsv
 */
public class EntityPrioritizer {

  public static class EntityPriority {
    String name;
    String complexity;  // SIMPLE, MEDIUM, COMPLEX
    int frequency;      // Occurrence in industrial files
    int dependencyDepth; // How many other entities reference this
    String domain;      // geometry, topology, annotation, etc.
    int priorityScore;

    public String toTsv() {
      return String.format("%s\t%s\t%d\t%d\t%s\t%d",
        name, complexity, frequency, dependencyDepth, domain, priorityScore);
    }
  }

  private Map<String, EntityPriority> priorities = new LinkedHashMap<>();
  private Map<String, Integer> frequencyMap = new HashMap<>();
  private JSONObject entityCatalog;
  private Set<String> missingEntities;

  /**
   * Load entity catalog from EXPRESS parser output.
   */
  public void loadCatalog(Path catalogPath) throws IOException {
    String content = Files.readString(catalogPath);
    entityCatalog = JSONObject.parseObject(content);
  }

  /**
   * Load missing entities list.
   */
  public void loadMissingEntities(Path missingPath) throws IOException {
    missingEntities = new HashSet<>();
    List<String> lines = Files.readAllLines(missingPath);
    for (String line : lines) {
      if (!line.trim().isEmpty()) {
        missingEntities.add(line.trim().toUpperCase());
      }
    }
  }

  /**
   * Load entity frequency from industrial file analysis.
   */
  public void loadFrequency(Path freqPath) throws IOException {
    List<String> lines = Files.readAllLines(freqPath);
    for (String line : lines) {
      if (line.trim().isEmpty()) continue;
      // Format: "    28 #1=CARTESIAN_POINT(..."
      String[] parts = line.trim().split("\\s+");
      if (parts.length >= 2) {
        int count = Integer.parseInt(parts[0]);
        String entityName = parts[1].replaceAll("^#[0-9]+=", "").toUpperCase();
        frequencyMap.put(entityName, count);
      }
    }
  }

  /**
   * Analyze entity complexity based on attributes.
   */
  public void analyzeComplexity() {
    JSONObject entities = entityCatalog.getJSONObject("entities");

    for (String entityName : missingEntities) {
      EntityPriority ep = new EntityPriority();
      ep.name = entityName;
      ep.frequency = frequencyMap.getOrDefault(entityName, 0);

      JSONObject entityObj = entities.getJSONObject(entityName);
      if (entityObj != null) {
        JSONArray attributes = entityObj.getJSONArray("attributes");
        ep.complexity = classifyComplexity(attributes);
        ep.domain = classifyDomain(entityName);
        ep.dependencyDepth = calculateDependencyDepth(entityName, entities);
      } else {
        // Entity not found in catalog (may be TYPE or deprecated)
        ep.complexity = "UNKNOWN";
        ep.domain = "unknown";
        ep.dependencyDepth = 0;
      }

      // Calculate priority score
      ep.priorityScore = calculatePriorityScore(ep);

      priorities.put(entityName, ep);
    }
  }

  /**
   * Classify entity complexity based on attributes.
   */
  private String classifyComplexity(JSONArray attributes) {
    if (attributes == null || attributes.isEmpty()) {
      return "SIMPLE";
    }

    int primitiveCount = 0;
    int refCount = 0;
    int listCount = 0;

    for (int i = 0; i < attributes.size(); i++) {
      JSONObject attr = attributes.getJSONObject(i);
      boolean isEntityRef = attr.getBooleanValue("is_entity_ref");
      boolean isList = attr.getBooleanValue("is_list");

      if (isList) listCount++;
      if (isEntityRef) refCount++;
      if (!isEntityRef && !isList) primitiveCount++;
    }

    // Classification rules:
    // SIMPLE: Only primitives (strings, numbers, enums)
    // MEDIUM: 1-3 entity references
    // COMPLEX: 4+ references, or LIST of entities, or geometry/topology domain
    if (listCount > 0 || refCount >= 4) {
      return "COMPLEX";
    } else if (refCount >= 1) {
      return "MEDIUM";
    } else {
      return "SIMPLE";
    }
  }

  /**
   * Classify entity domain based on naming patterns.
   */
  private String classifyDomain(String entityName) {
    if (entityName.contains("GEOMETRY") || entityName.contains("CURVE") ||
        entityName.contains("SURFACE") || entityName.contains("POINT") ||
        entityName.contains("LINE") || entityName.contains("PLANE") ||
        entityName.contains("CIRCLE") || entityName.contains("ELLIPSE") ||
        entityName.contains("BSPLINE") || entityName.contains("SPLINE")) {
      return "geometry";
    }

    if (entityName.contains("VERTEX") || entityName.contains("EDGE") ||
        entityName.contains("FACE") || entityName.contains("SHELL") ||
        entityName.contains("LOOP") || entityName.contains("BOUND") ||
        entityName.contains("TOPOLOGY")) {
      return "topology";
    }

    if (entityName.contains("ANNOTATION") || entityName.contains("PMI") ||
        entityName.contains("DRAUGHTING") || entityName.contains("TEXT") ||
        entityName.contains("SYMBOL") || entityName.contains("COLOUR")) {
      return "annotation";
    }

    if (entityName.contains("TOLERANCE") || entityName.contains("DATUM") ||
        entityName.contains("DIMENSION") || entityName.contains("GD&T")) {
      return "tolerance";
    }

    if (entityName.contains("PRODUCT") || entityName.contains("ASSEMBLY") ||
        entityName.contains("COMPONENT") || entityName.contains("DEFINITION")) {
      return "product";
    }

    if (entityName.contains("MEASURE") || entityName.contains("UNIT")) {
      return "unit";
    }

    if (entityName.contains("ACTION") || entityName.contains("WORKFLOW") ||
        entityName.contains("PROCESS")) {
      return "action";
    }

    if (entityName.contains("KINEMATIC") || entityName.contains("MECHANISM") ||
        entityName.contains("PAIR") || entityName.contains("JOINT")) {
      return "kinematic";
    }

    if (entityName.contains("FEA") || entityName.contains("ELEMENT") ||
        entityName.contains("MESH") || entityName.contains("NODE")) {
      return "fea";
    }

    if (entityName.contains("MANUFACTURING") || entityName.contains("FEATURE") ||
        entityName.contains("MACHINING") || entityName.contains("OPERATION")) {
      return "manufacturing";
    }

    if (entityName.contains("APPLIED") || entityName.contains("ASSIGNMENT") ||
        entityName.contains("CLASSIFICATION")) {
      return "classification";
    }

    if (entityName.contains("VALIDATION") || entityName.contains("VERIFICATION") ||
        entityName.contains("INSPECTION") || entityName.contains("A3M")) {
      return "validation";
    }

    return "misc";
  }

  /**
   * Calculate dependency depth (how many other entities reference this).
   */
  private int calculateDependencyDepth(String entityName, JSONObject entities) {
    int depth = 0;

    for (String otherEntity : entities.keySet()) {
      JSONObject otherObj = entities.getJSONObject(otherEntity);
      JSONArray attributes = otherObj.getJSONArray("attributes");
      if (attributes == null) continue;

      for (int i = 0; i < attributes.size(); i++) {
        JSONObject attr = attributes.getJSONObject(i);
        String refEntity = attr.getString("referenced_entity");
        if (refEntity != null && refEntity.equals(entityName)) {
          depth++;
        }
      }
    }

    return depth;
  }

  /**
   * Calculate priority score.
   * Higher score = higher priority (implement first).
   */
  private int calculatePriorityScore(EntityPriority ep) {
    int score = 0;

    // Frequency factor (high frequency = high priority)
    score += ep.frequency * 10;

    // Dependency depth factor (deep dependencies = high priority)
    score += ep.dependencyDepth * 5;

    // Complexity factor (simple = high priority)
    if (ep.complexity.equals("SIMPLE")) score += 100;
    else if (ep.complexity.equals("MEDIUM")) score += 50;
    else if (ep.complexity.equals("COMPLEX")) score += 10;

    // Domain factor (core domains = high priority)
    if (ep.domain.equals("geometry")) score += 80;
    else if (ep.domain.equals("topology")) score += 70;
    else if (ep.domain.equals("product")) score += 60;
    else if (ep.domain.equals("annotation")) score += 50;
    else if (ep.domain.equals("unit")) score += 40;
    else if (ep.domain.equals("tolerance")) score += 30;
    else score += 20;

    return score;
  }

  /**
   * Sort by priority score (descending).
   */
  public void sortByPriority() {
    List<EntityPriority> list = new ArrayList<>(priorities.values());
    list.sort((a, b) -> Integer.compare(b.priorityScore, a.priorityScore));

    priorities.clear();
    for (EntityPriority ep : list) {
      priorities.put(ep.name, ep);
    }
  }

  /**
   * Write priority queue to TSV file.
   */
  public void writePriorityQueue(Path outputPath) throws IOException {
    List<String> lines = new ArrayList<>();
    lines.add("entity\tcomplexity\tfrequency\tdependency_depth\tdomain\tpriority_score");

    for (EntityPriority ep : priorities.values()) {
      lines.add(ep.toTsv());
    }

    Files.write(outputPath, lines);
  }

  /**
   * Write complexity statistics.
   */
  public void writeComplexityStats(Path outputPath) throws IOException {
    Map<String, Integer> complexityCounts = new HashMap<>();
    Map<String, Integer> domainCounts = new HashMap<>();

    for (EntityPriority ep : priorities.values()) {
      complexityCounts.merge(ep.complexity, 1, Integer::sum);
      domainCounts.merge(ep.domain, 1, Integer::sum);
    }

    List<String> lines = new ArrayList<>();
    lines.add("=== Complexity Distribution ===");
    for (Map.Entry<String, Integer> entry : complexityCounts.entrySet()) {
      lines.add(entry.getKey() + ": " + entry.getValue());
    }

    lines.add("\n=== Domain Distribution ===");
    for (Map.Entry<String, Integer> entry : domainCounts.entrySet()) {
      lines.add(entry.getKey() + ": " + entry.getValue());
    }

    Files.write(outputPath, lines);
  }

  /**
   * Main entry point.
   */
  public static void main(String[] args) throws IOException {
    EntityPrioritizer prioritizer = new EntityPrioritizer();

    // Load inputs
    System.out.println("Loading missing entities...");
    prioritizer.loadMissingEntities(Paths.get("generated/missing_entities.txt"));

    System.out.println("Loading entity catalog...");
    prioritizer.loadCatalog(Paths.get("generated/ap242-entity-catalog.json"));

    System.out.println("Loading frequency data...");
    prioritizer.loadFrequency(Paths.get("generated/entity_frequency.txt"));

    // Analyze
    System.out.println("Analyzing complexity...");
    prioritizer.analyzeComplexity();

    System.out.println("Sorting by priority...");
    prioritizer.sortByPriority();

    // Write outputs
    System.out.println("Writing priority queue...");
    prioritizer.writePriorityQueue(Paths.get("generated/priority-queue.tsv"));

    System.out.println("Writing complexity stats...");
    prioritizer.writeComplexityStats(Paths.get("generated/entity-complexity.tsv"));

    System.out.println("Done!");
  }
}