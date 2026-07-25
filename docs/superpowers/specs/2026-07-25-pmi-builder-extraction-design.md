# StepPmiPayloadBuilder 提取设计文档

**日期：** 2026-07-25
**作者：** AI Assistant
**状态：** 已批准

## 1. 背景

### 1.1 当前问题

`StepPreviewJsonExporter.java` 是一个 God Class，拥有 13,601 行代码，违反了单一职责原则。虽然已经进行了多次重构（提取了 StepCurveTypeNameResolver、StepSummaryBuilder 等），但仍然过大。

### 1.2 目标

通过提取 PMI（Product Manufacturing Information）处理逻辑到独立的 `StepPmiPayloadBuilder` 类，将 `StepPreviewJsonExporter` 减少约 2,700 行代码，提升可维护性和可测试性。

### 1.3 范围

- 提取 PMI payload 构建逻辑
- 提取 PMI target 处理逻辑
- 保持零破坏性改动
- 运行全量测试确保无回归

## 2. 现状分析

### 2.1 PMI 模块结构

**核心方法（29个）：**

| 方法名 | 行号范围 | 职责 |
|--------|---------|------|
| `buildPmiPayloads` | 6088-6356 | 主入口，构建所有PMI payloads |
| `toPmiPayload` | 7015-7045 | 将 draughting callout 转换为 PMI payload |
| `toStandalonePointPmi` | 6614-6632 | 创建独立点的 PMI payload |
| `isSupportedPmiUsageCarrier` | 6634-6650 | 检查是否为支持的 PMI 载体 |
| `appendPlaceholderPmi` | 6357-6379 | 处理占位符 PMI |
| `appendAnnotationPlanePmi` | 6380-6413 | 处理标注平面 PMI |
| `appendDraughtingAnnotationPmi` | 6428-6439 | 处理制图标注 PMI |
| `appendPointSetPmi` | 6440-6454 | 处理点集 PMI |
| `appendGeometricMeasurementPmi` | 6455-6476 | 处理几何测量 PMI |
| `appendFillAreaWithOutlinePmi` | 6477-6494 | 处理填充区域 PMI |
| `appendGeometricTolerancePmi` | 6495-6509 | 处理几何公差 PMI |
| `appendGeometricToleranceWithDatumPmi` | 6510-6524 | 处理带基准的几何公差 PMI |
| `appendGeometricToleranceWithAreaUnitPmi` | 6525-6539 | 处理带面积单位的几何公差 PMI |
| `appendGeometricToleranceWithMaxPmi` | 6540-6557 | 处理最大公差 PMI |
| `appendDimensionalLocationPmi` | 6558-6569 | 处理尺寸位置 PMI |
| `appendToleranceZonePmi` | 6570-6589 | 处理公差区域 PMI |
| `appendDatumPmi` | 6590-6601 | 处理基准 PMI |
| `appendDatumTargetPmi` | 6602-6613 | 处理基准目标 PMI |
| `appendPmiLeader` | 7047-7469 | 添加 PMI 引导线（多个重载） |
| `appendPmiLeaderForSolid` | 7383-7402 | 为实体添加引导线 |
| `appendPmiPathLeader` | 7453-7469 | 添加路径引导线 |
| ... (其他辅助方法) | | |

**Target 处理方法（43个）：**

| 方法分类 | 方法数量 | 职责 |
|----------|---------|------|
| `appendPmiTarget` | 4 | 添加 PMI 目标（多个重载） |
| `appendRepresentationBacklinkTarget` | 2 | 处理表示链接目标 |
| `appendDefinitionBacklinkTarget` | 1 | 处理定义链接目标 |
| `appendRelationshipBacklinkTarget` | 1 | 处理关系链接目标 |
| `appendSemanticDefinitionTargets` | 1 | 处理语义定义目标（public方法） |
| `appendAttachedRepresentationRelationshipTargets` | 1 | 处理附加表示关系目标 |
| `appendProductRelationshipTargets` | 1 | 处理产品关系目标 |
| `appendProductDefinitionFormationRelationshipTargets` | 1 | 处理产品定义形成关系目标 |
| `appendProductDefinitionRelationshipTargets` | 1 | 处理产品定义关系目标 |
| `appendPropertyDefinitionRelationshipTargets` | 1 | 处理属性定义关系目标 |
| `appendPropertyRepresentationLinkTargets` | 2 | 处理属性表示链接目标 |
| `appendCarrierDefinitionTargets` | 1 | 处理载体定义目标 |
| `appendNestedDefinitionTargets` | 1 | 处理嵌套定义目标 |
| `appendDefinitionRelationshipTargets` | 1 | 处理定义关系目标 |
| `appendRelationshipSemanticTargets` | 1 | 处理关系语义目标 |
| `append*Targets` (其他) | 23 | 处理各种特定类型的目标 |

**辅助方法：**

- `buildInstanceIdsByTargetId` - 构建实例 ID 映射
- `propagateCalloutTargets` - 传播标注目标
- `definitionTypeName` - 获取定义类型名称
- `relationshipTypeName` - 获取关系类型名称
- 多个 `collect*Targets` 方法 - 收集特定类型目标

### 2.2 依赖关系

**输入依赖：**
```java
Map<Integer, StepEntity> resolved  // 解析后的 STEP 实体
AssemblyData assembly              // 装配数据
StepCadBuilder builder             // CAD 构建器
```

**输出类型：**
```java
List<PmiPayload> pmiPayloads       // PMI payload 列表
PmiTargetPayload targetPayload     // PMI 目标 payload
```

**共享依赖（不迁移）：**
- `pointFrom*` 系列 - 点处理方法（被 PMI 和几何处理共享）
- `PayloadConversionHelper.toPointPayload` - payload 转换工具

### 2.3 代码行数估算

| 类别 | 行数估算 |
|------|---------|
| PMI 核心方法 | ~800 |
| Target 处理方法 | ~1,500 |
| 辅助方法 | ~400 |
| **总计** | **~2,700** |

## 3. 设计方案

### 3.1 新类结构

**类名：** `com.minicad.export.json.StepPmiPayloadBuilder`

**位置：** `src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java`

**类设计：**
```java
package com.minicad.export.json;

import com.minicad.geometry.CartesianPoint;
import com.minicad.preview.builder.PmiPayload;
import com.minicad.preview.builder.PmiTargetPayload;
import com.minicad.step.model.*;
import com.minicad.step.semantic.AssemblyData;
import com.minicad.step.semantic.StepCadBuilder;
import java.util.*;

/**
 * Builds PMI (Product Manufacturing Information) payloads from STEP entities.
 *
 * <p>Handles annotations, dimensions, tolerances, datum features, and other
 * manufacturing information that annotates geometry in CAD models.</p>
 *
 * <p>This class extracts PMI information from various STEP entity types:</p>
 * <ul>
 *   <li>Draughting callouts (dimensions, notes)</li>
 *   <li>Geometric tolerances (GD&T)</li>
 *   <li>Datum features and targets</li>
 *   <li>Annotation occurrences (text, symbols)</li>
 *   <li>PMI requirement associations</li>
 * </ul>
 *
 * @since 1.0
 */
public final class StepPmiPayloadBuilder {

    // Private constructor - utility class with static methods
    private StepPmiPayloadBuilder() {}

    // ========== Public API ==========

    /**
     * Builds all PMI payloads from resolved STEP entities.
     *
     * @param resolved Map of resolved STEP entities by ID
     * @param assembly Assembly data containing representations and instances
     * @param builder CAD builder for geometry resolution
     * @return List of PMI payloads, empty if none found
     */
    public static List<PmiPayload> buildPmiPayloads(
            Map<Integer, StepEntity> resolved,
            AssemblyData assembly,
            StepCadBuilder builder
    ) {
        // ... implementation
    }

    /**
     * Converts a draughting callout to PMI payload.
     *
     * @param callout The draughting callout to convert
     * @param targets List of target payloads for this callout
     * @param builder CAD builder for geometry resolution
     * @return PMI payload, or null if invalid
     */
    public static PmiPayload toPmiPayload(
            StepDraughtingCallout callout,
            List<PmiTargetPayload> targets,
            StepCadBuilder builder
    ) {
        // ... implementation
    }

    /**
     * Appends semantic definition targets for PMI.
     *
     * <p>This is a public method as it may be called from other builders.</p>
     *
     * @param targetsByUsageId Map to collect targets by usage ID
     * @param identifiedItem The identified item
     * @param definition The definition entity
     * @param resolved Map of resolved STEP entities
     * @param instanceIdsByTargetId Instance IDs by target ID
     */
    public static void appendSemanticDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        // ... implementation
    }

    // ========== Private Helper Methods ==========

    // PMI-specific methods (29 total)
    private static void appendPlaceholderPmi(...) { }
    private static void appendAnnotationPlanePmi(...) { }
    // ... 其他 private 方法

    // Target processing methods (43 total)
    private static void appendPmiTarget(...) { }
    private static void appendRepresentationBacklinkTarget(...) { }
    // ... 其他 private 方法

    // Utility methods
    private static boolean isSupportedPmiUsageCarrier(StepEntity entity) { }
    private static String definitionTypeName(StepEntity definition) { }
    private static String relationshipTypeName(StepEntity relationship) { }
}
```

### 3.2 迁移策略

#### 阶段1：创建新类并迁移方法

1. 创建 `StepPmiPayloadBuilder.java` 文件
2. 复制 PMI 相关方法（29个）到新类
3. 复制 Target 处理方法（43个）到新类
4. 复制辅助方法到新类
5. 调整包导入

#### 阶段2：更新原类调用

1. 在 `StepPreviewJsonExporter` 中导入 `StepPmiPayloadBuilder`
2. 将 `buildPmiPayloads(...)` 调用改为 `StepPmiPayloadBuilder.buildPmiPayloads(...)`
3. 保持其他逻辑不变

#### 阶段3：处理共享依赖

**方案：保留 pointFrom* 系列在原类**

```java
// StepPmiPayloadBuilder 中的调用
CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(
    textOccurrence.position(), builder
);
```

**原因：**
- `pointFrom*` 方法被 PMI 和几何处理共享
- 避免循环依赖
- 保持方法签名稳定

### 3.3 接口设计原则

1. **最小化 public API**
   - 只有 `buildPmiPayloads` 和 `toPmiPayload` 作为 public
   - `appendSemanticDefinitionTargets` 为 public（可能被其他模块调用）
   - 其他方法为 private

2. **保持方法签名不变**
   - 所有迁移的方法保持原有参数类型和返回类型
   - 确保零破坏性改动

3. **无状态设计**
   - 所有方法为 static
   - 无实例字段
   - 线程安全

## 4. 实施计划

### 4.1 准备阶段

- [ ] 确认所有测试通过（当前基线）
- [ ] 创建 Git 分支 `refactor/extract-pmi-builder`
- [ ] 备份当前代码状态

### 4.2 实施步骤

**步骤1：创建新类骨架**
```bash
# 创建文件
touch src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java
```

**步骤2：迁移 PMI 核心方法**
- 复制 `buildPmiPayloads` 及其调用的所有方法
- 总计约 800 行

**步骤3：迁移 Target 处理方法**
- 复制所有 `append*Target` 方法
- 总计约 1,500 行

**步骤4：迁移辅助方法**
- 复制工具方法
- 总计约 400 行

**步骤5：更新导入和引用**
- 在 `StepPreviewJsonExporter` 中添加 import
- 更新静态方法调用

**步骤6：编译验证**
```bash
mvn clean compile
```

**步骤7：运行测试**
```bash
mvn test
# 确保全部 1,897 个测试通过
```

**步骤8：代码审查**
- 检查代码格式（Spotless）
- 检查代码质量（SonarQube 或 manual review）

**步骤9：提交变更**
```bash
git add .
git commit -m "refactor: extract StepPmiPayloadBuilder for PMI handling"

# 减少了约 2,700 行
# StepPreviewJsonExporter: 13,601 -> ~10,900 lines
```

### 4.3 验证清单

- [ ] 所有单元测试通过（1,897 tests）
- [ ] 编译无警告
- [ ] Spotless 格式检查通过
- [ ] PMI 功能手动验证（通过示例 STEP 文件）
- [ ] 无性能退化（benchmark 测试）

## 5. 风险评估

### 5.1 风险矩阵

| 风险 | 概率 | 影响 | 缓解措施 |
|------|------|------|---------|
| 测试失败 | 低 | 高 | 保持方法签名不变，零破坏性改动 |
| 编译错误 | 低 | 中 | 使用 IDE 重构工具，确保导入正确 |
| 性能退化 | 极低 | 低 | 静态方法调用无额外开销 |
| 循环依赖 | 低 | 中 | pointFrom* 保留在原类，避免循环 |

### 5.2 回滚策略

如果出现问题，可以快速回滚：

```bash
git checkout main
git branch -D refactor/extract-pmi-builder
```

## 6. 后续优化

完成此次重构后，`StepPreviewJsonExporter` 仍有约 10,900 行，可以继续：

1. **提取几何构建模块** - 提取 `buildLegacyGeometry` 等方法（约 2,000 行）
2. **提取表示构建模块** - 提取 `buildRepresentationPayload` 等方法（约 1,500 行）
3. **统一目标处理逻辑** - 重构 43 个 `append*Target` 方法的重复模式（访问者模式）
4. **清理重复导入** - 移除 wildcard imports

目标：将 `StepPreviewJsonExporter` 减少到 5,000 行以下。

## 7. 成功标准

- ✅ StepPreviewJsonExporter 减少约 2,700 行（13,601 -> ~10,900）
- ✅ 所有 1,897 个测试通过
- ✅ 无性能退化
- ✅ PMI 逻辑集中在一个类中，职责清晰
- ✅ 代码可读性和可维护性提升

## 8. 参考资料

- [God Class 反模式](https://en.wikipedia.org/wiki/God_object)
- [单一职责原则](https://en.wikipedia.org/wiki/Single-responsibility_principle)
- [提取类重构](https://refactoring.guru/extract-class)
- 项目历史提交：
  - `4f1c62f` - delegate curve property methods to StepCurveTypeNameResolver
  - `24b5b31` - extract StepCurveTypeNameResolver for curve type name resolution
  - `3cdce2f` - delegate pcurveBasisSurfaceSummary to StepSummaryBuilder