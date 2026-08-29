package com.minicad.step.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minicad.helper.StepTextReader;
import com.minicad.step.model.StepEntity;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * 生成层访问器冒烟（回归守卫）。
 *
 * <p>step.model 下是 1265 个由 STEP  schema 自动生成的实体类，手写测试不现实。
 * 本测试把全部 samples/ 解析为实体后，用反射逐一调用每个实体的零参访问器
 * （getXxx、StepEntity.id()/name() 默认方法、toString/hashCode），以此覆盖
 * 生成类的访问器/构造体/equals 辅助代码，并能在 codegen 或映射回归时立即暴露异常。
 *
 * <p>注意：这是“访问器可达性”守卫，不验证业务语义——真实几何/拓扑语义由
 * SamplesParseSmokeTest 与 Export 序列化测试覆盖。
 */
class StepModelEntityAccessorTest {

    private static final java.util.Set<String> OBJECT_DENY = java.util.Set.of(
            "getClass", "wait", "notify", "notifyAll", "clone", "finalize");

    @Test
    void everyResolvedEntityAccessorIsInvocable() throws IOException {
        Path samplesDir = Path.of("samples");
        assertTrue(Files.exists(samplesDir), "samples/ 目录应存在");

        List<String> failures = new ArrayList<>();
        int entityCount = 0;
        int accessorCount = 0;

        try (var paths = Files.walk(samplesDir)) {
            List<Path> stepFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> {
                        String n = p.getFileName().toString().toLowerCase();
                        return n.endsWith(".step") || n.endsWith(".stp");
                    })
                    .sorted()
                    .collect(Collectors.toList());

            for (Path p : stepFiles) {
                String text = StepTextReader.read(p);
                StepFile stepFile = StepParser.parse(text);
                Map<Integer, StepEntity> entities = StepEntityResolver.resolveAll(stepFile);
                for (StepEntity entity : entities.values()) {
                    entityCount++;
                    accessorCount += invokeAccessors(entity, failures);
                }
            }
        }

        assertTrue(entityCount > 200,
                "应覆盖足量实体（实际 " + entityCount + "），否则测试近乎空跑");
        if (!failures.isEmpty()) {
            int shown = Math.min(failures.size(), 25);
            StringBuilder sb = new StringBuilder();
            sb.append("有 ").append(failures.size())
              .append(" 个访问器调用抛异常（共调用 ").append(accessorCount)
              .append(" 次）。前 ").append(shown).append(" 条：\n");
            for (int i = 0; i < shown; i++) {
                sb.append("  - ").append(failures.get(i)).append('\n');
            }
            assertTrue(failures.isEmpty(), sb.toString());
        }
    }

    private int invokeAccessors(StepEntity entity, List<String> failures) {
        Class<?> clazz = entity.getClass();
        int invoked = 0;
        for (Method m : clazz.getMethods()) {
            if (m.getParameterCount() != 0) {
                continue;
            }
            String name = m.getName();
            if (OBJECT_DENY.contains(name)) {
                continue;
            }
            // 只调用实体自身 / StepEntity 体系的方法；跳过 java.lang.Object 上的其余方法
            if (m.getDeclaringClass() == Object.class
                    && !(name.equals("toString") || name.equals("hashCode"))) {
                continue;
            }
            // 仅对“访问器”形态调用：getXxx、id/name、toString、hashCode
            boolean isAccessor = name.startsWith("get")
                    || name.equals("id") || name.equals("name")
                    || name.equals("toString") || name.equals("hashCode");
            if (!isAccessor) {
                continue;
            }
            try {
                m.setAccessible(true);
                m.invoke(entity);
                invoked++;
            } catch (Throwable t) {
                Throwable cause = t instanceof java.lang.reflect.InvocationTargetException && t.getCause() != null
                        ? t.getCause() : t;
                failures.add(clazz.getSimpleName() + "#" + name + " -> "
                        + cause.getClass().getSimpleName() + ": " + cause.getMessage());
            }
        }
        return invoked;
    }
}
