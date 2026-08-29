package com.minicad.step.semantic;

import java.util.Map;

/**
 * Registry for profile entity types.
 * Extracted from MiscRegistry.java during refactoring.
 */
public final class ProfileRegistry {

  private ProfileRegistry() {}

  public static void register(Map<String, EntityFactory> registry) {
      registry.put("RECTANGLE_PROFILE_DEF", StepEntityResolver::resolveRectangleProfileDef);
      registry.put("RECTANGLE_HOLLOW_PROFILE_DEF", StepEntityResolver::resolveRectangleHollowProfileDef);
      registry.put(
          "CENTERED_RECTANGLE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(
                  instance, "CENTERED_RECTANGLE_PROFILE_DEF", 2));
      registry.put(
          "CIRCULAR_HOLLOW_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "CIRCULAR_HOLLOW_PROFILE_DEF", 2));
      registry.put(
          "ROUNDED_RECTANGLE_PROFILE_DEF",
          (resolver, instance) -> resolver.resolveParameterizedProfileDef(
              instance, "ROUNDED_RECTANGLE_PROFILE_DEF", 3));
      registry.put("ARBITRARY_CLOSED_PROFILE_DEF", (resolver, instance) ->
          resolver.resolveArbitraryClosedProfileDef(instance));
      registry.put("ARBITRARY_PROFILE_DEF", (resolver, instance) ->
          resolver.resolveArbitraryProfileDef(instance, "ARBITRARY_PROFILE_DEF"));
      registry.put("ARBITRARY_PROFILE_DEF_WITH_VOIDS", StepEntityResolver::resolveArbitraryProfileDefWithVoids);
      registry.put(
          "ARBITRARY_OPEN_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveArbitraryProfileDef(instance, "ARBITRARY_OPEN_PROFILE_DEF"));
      registry.put(
          "I_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "I_SHAPE_PROFILE_DEF", 6));
      registry.put(
          "T_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "T_SHAPE_PROFILE_DEF", 5));
      registry.put(
          "L_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "L_SHAPE_PROFILE_DEF", 4));
      registry.put(
          "U_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "U_SHAPE_PROFILE_DEF", 5));
      registry.put(
          "C_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "C_SHAPE_PROFILE_DEF", 5));
      registry.put(
          "Z_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "Z_SHAPE_PROFILE_DEF", 5));
      registry.put(
          "HAT_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "HAT_SHAPE_PROFILE_DEF", 5));
      registry.put(
          "ANGLE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "ANGLE_PROFILE_DEF", 4));
      registry.put(
          "CHANNEL_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "CHANNEL_PROFILE_DEF", 5));
      registry.put(
          "TEE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "TEE_PROFILE_DEF", 5));
      registry.put(
          "I_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "I_PROFILE_DEF", 6));
      registry.put(
          "L_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "L_PROFILE_DEF", 4));
      registry.put(
          "T_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "T_PROFILE_DEF", 5));
      registry.put(
          "U_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "U_PROFILE_DEF", 5));
      registry.put(
          "Z_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "Z_PROFILE_DEF", 5));
      registry.put(
          "FLAT_BAR_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "FLAT_BAR_PROFILE_DEF", 2));
      registry.put(
          "DOVE_TAIL_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "DOVE_TAIL_PROFILE_DEF", 4));
      registry.put("PARAMETERIZED_PROFILE_DEF", (resolver, instance) ->
          resolver.resolveParameterizedProfileDef(instance, "PARAMETERIZED_PROFILE_DEF", 3));
      registry.put("PROFILE_DEF", StepEntityResolver::resolveProfileDef);
  }
}
