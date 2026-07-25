# StepFacePayloadBuilder 提取设计文档

**日期：** 2026-07-25
**作者：** AI Assistant
**状态：** 已批准
**前置：** StepPmiPayloadBuilder提取完成（减少5,061行）

## 1. 背景

### 1.1 当前问题

继PMI模块提取后，`StepPreviewJsonExporter.java` 仍有 8,540 行代码（203个静态方法），需要继续优化。

### 1.2 目标

通过提取Face（面）处理逻辑到独立的 `StepFacePayloadBuilder` 类，将 `StepPreviewJsonExporter` 减少约1,200行代码，进一步提升可维护性。

### 1.3 范围

- 提取Face payload构建逻辑
- 提取各种曲面类型的处理方法
- 保持零破坏性改动
- 运行全量测试确保无回归

## 2. 现状分析

### 2.1 Face处理模块结构

**核心入口方法：**

| 方法名 | 行号范围 | 职责 |
|--------|---------|------|
| `buildPreviewFaceResult` | 1622-1960 | 主入口，处理所有face类型，分发到具体方法 |
| `toParametricTrimmedFaceResult` | - | 参数化trimmed face处理 |

**Face Payload构建方法（~15个）：**

| 方法名 | 处理类型 |
|--------|---------|
| `toCylindricalFacePayload` | 圆柱面（Cylindrical Surface） |
| `toConicalFacePayload` | 圆锥面（Conical Surface） |
| `toToroidalFacePayload` | 圆环面（Toroidal Surface） |
| `toToroidalWithSpecifiedBendsFacePayload` | 带指定弯角的圆环面 |
| `toBSplineSurfaceFacePayload` | B样条曲面（B-Spline Surface） |
| `toRationalBSplineSurfaceFacePayload` | 有理B样条曲面（NURBS） |
| `toFourSidedPatchFacePayload` | 四边形patch |
| `toRuledSurfaceFacePayload` | 直纹面（Ruled Surface） |
| `toSurfaceOfConstantRadiusFacePayload` | 等半径曲面 |
| `toParametricSurfaceFacePayload` | 参数化曲面 |
| `toSampledSurfaceFacePayload` | 采样曲面 |
| `toPlaneFacePayload` | 平面（Plane） - 如果存在 |

**辅助方法：**

| 方法名 | 职责 |
|--------|------|
| `faceGeometry` | 获取face几何实体 |
| `reverseFacePayload` | 反转face payload的法向量 |
| `unwrapParametricPreviewSurface` | 解包参数化预览曲面 |
| `describeUnsupportedPreviewSurface` | 描述不支持的曲面类型 |
| `logPreviewFacePayload` | 记录face payload日志 |

**相关统计：**
- Face相关行数：约1,255行（包含"Face"或"face"的行）
- Face payload方法数：约17个直接返回FacePayload的方法
- 总体估算：约1,000-1,200行代码可迁移

### 2.2 依赖关系

**输入依赖：**
```java
StepFaceEntity stepFace                      // STEP face实体
StepCadBuilder builder                       // CAD构建器
StepMetadataExtractor.DisplayMetadata metadata  // 显示元数据
```

**输出类型：**
```java
PreviewFaceResult                            // 预览face结果
  ├── FacePayload face                       // 成功的face payload
  └── UnsupportedFacePayload unsupportedFace // 不支持的face

FacePayload                                  // Face payload
UnsupportedFacePayload                       // 不支持的face payload
```

**外部依赖（委托调用）：**
- `PreviewMeshExporter.facePayloadFromTopologyFace()` - 从拓扑face构建payload
- `StepPreviewJsonExporter.faceDisplayName()` - 获取face显示名称（共享）
- `StepPreviewJsonExporter.toUnsupportedFacePayload()` - 创建不支持的face payload（共享）

### 2.3 独立性评估

**高独立性因素：**
- ✅ 所有方法都是静态方法
- ✅ 无实例字段依赖
- ✅ 输入输出明确（StepFaceEntity → PreviewFaceResult）
- ✅ 业务逻辑相对独立（face处理）

**依赖处理：**
- 共享工具方法（faceDisplayName, toUnsupportedFacePayload）保留在原类
- 新类通过静态调用使用共享方法

## 3. 设计方案

### 3.1 新类结构

**类名：** `com.minicad.export.json.StepFacePayloadBuilder`

**位置：** `src/main/java/com/minicad/export/json/StepFacePayloadBuilder.java`

**类设计：**
```java
package com.minicad.export.json;

import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.UnsupportedFacePayload;
import com.minicad.preview.payload.PreviewFaceResult;
import com.minicad.step.model.*;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.helper.StepMetadataExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds face payloads from STEP face entities.
 *
 * <p>Handles conversion of various surface types to preview payloads:</p>
 * <ul>
 *   <li>Planar surfaces (planes)</li>
 *   <li>Revolution surfaces (cylinders, cones, spheres, tori)</li>
 *   <li>Parametric surfaces (B-spline, NURBS)</li>
 *   <li>Derived surfaces (ruled, offset, swept)</li>
 * </ul>
 *
 * <p>Each surface type has dedicated payload construction methods
 * that handle geometry extraction, tessellation, and parameter mapping.</p>
 *
 * @since 1.0
 */
public final class StepFacePayloadBuilder {

    private static final Logger log = LoggerFactory.getLogger(StepFacePayloadBuilder.class);

    private StepFacePayloadBuilder() {
    }

    /**
     * Builds preview face result from STEP face entity.
     *
     * <p>This is the main entry point for face payload construction.
     * It dispatches to type-specific methods based on the face geometry.</p>
     *
     * @param stepFace The STEP face entity to convert
     * @param builder CAD builder for geometry resolution
     * @param metadata Display metadata for the face
     * @return Preview face result containing either a valid payload or unsupported face info
     */
    public static PreviewFaceResult buildPreviewFaceResult(
            StepFaceEntity stepFace,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        // Implementation
    }

    /**
     * Extracts the underlying geometry from a STEP face.
     */
    private static StepEntity faceGeometry(StepFaceEntity stepFace) {
        // Implementation
    }

    /**
     * Reverses the face payload (for oriented faces with negative orientation).
     */
    private static FacePayload reverseFacePayload(FacePayload face) {
        // Implementation
    }

    // Surface-specific payload construction methods
    private static FacePayload toCylindricalFacePayload(...) { }
    private static FacePayload toConicalFacePayload(...) { }
    private static FacePayload toToroidalFacePayload(...) { }
    // ... 其他方法
}
```

### 3.2 迁移策略

#### 策略：委托模式 + 保持签名

**原则：**
1. 所有方法签名保持不变
2. 共享方法保留在StepPreviewJsonExporter
3. 新类通过静态委托调用共享方法

**步骤：**

**Step 1: 创建新类骨架**
```bash
touch src/main/java/com/minicad/export/json/StepFacePayloadBuilder.java
```

**Step 2: 迁移buildPreviewFaceResult主方法**
- 复制方法及其调用的所有辅助方法
- 约1,200行代码

**Step 3: 迁移所有to*FacePayload方法**
- toCylindricalFacePayload
- toConicalFacePayload
- toToroidalFacePayload
- 等15+个方法

**Step 4: 迁移辅助方法**
- faceGeometry
- reverseFacePayload
- unwrapParametricPreviewSurface
- describeUnsupportedPreviewSurface
- logPreviewFacePayload

**Step 5: 更新调用者**
- StepPreviewJsonExporter中的调用改为：
  ```java
  PreviewFaceResult result = StepFacePayloadBuilder.buildPreviewFaceResult(stepFace, builder, metadata);
  ```

**Step 6: 处理共享方法**
- `faceDisplayName` 保留在原类，新类调用：
  ```java
  String name = StepPreviewJsonExporter.faceDisplayName(stepFace);
  ```
- `toUnsupportedFacePayload` 保留在原类，新类调用：
  ```java
  UnsupportedFacePayload unsupported = StepPreviewJsonExporter.toUnsupportedFacePayload(stepFace, reason);
  ```

**Step 7: 删除已迁移方法**
- 从StepPreviewJsonExporter删除所有已迁移的face处理方法
- 保留共享方法

### 3.3 接口设计原则

1. **最小化public API**
   - 只有 `buildPreviewFaceResult` 为public
   - 其他方法为private

2. **保持方法签名不变**
   - 所有迁移方法保持原有参数类型和返回类型
   - 确保零破坏性改动

3. **无状态设计**
   - 所有方法为static
   - 无实例字段
   - 线程安全

## 4. 实施计划

### 4.1 准备阶段

- [ ] 确认当前在 refactor/extract-pmi-builder 分支
- [ ] 验证PMI提取成功（StepPreviewJsonExporter = 8,540行）
- [ ] 创建新的Git提交点（可选：创建新分支）

### 4.2 实施步骤

**步骤1：创建新类文件**
```bash
# 创建StepFacePayloadBuilder.java
# 添加类头、导入、类注释
```

**步骤2：迁移buildPreviewFaceResult主方法**
- 复制buildPreviewFaceResult方法（约340行）
- 复制其依赖的辅助方法

**步骤3：迁移所有surface payload方法**
- 复制所有to*FacePayload方法（约15个方法，约800行）

**步骤4：迁移辅助方法**
- faceGeometry
- reverseFacePayload
- unwrapParametricPreviewSurface
- describeUnsupportedPreviewSurface
- logPreviewFacePayload

**步骤5：更新调用关系**
- 在StepPreviewJsonExporter中添加import
- 更新buildPreviewFaceResult调用

**步骤6：删除已迁移方法**
- 从StepPreviewJsonExporter删除约1,200行
- 保留共享方法

**步骤7：验证编译**
```bash
mvn compile -DskipTests
```

**步骤8：运行测试**
```bash
mvn clean test
```

**步骤9：提交变更**
```bash
git add .
git commit -m "refactor: extract StepFacePayloadBuilder for face handling"
```

### 4.3 验证清单

- [ ] 编译无错误
- [ ] 所有单元测试通过
- [ ] Spotless格式检查通过
- [ ] PMI功能正常（之前的提取不受影响）
- [ ] Face处理功能正常（示例STEP文件验证）

## 5. 风险评估

### 5.1 风险矩阵

| 风险 | 概率 | 影响 | 缓解措施 |
|------|------|------|---------|
| 测试失败 | 低 | 高 | 保持方法签名不变，零破坏性改动 |
| 编译错误 | 低 | 中 | 使用IDE重构工具，确保导入正确 |
| 依赖遗漏 | 低 | 中 | 完整迁移所有依赖方法 |
| 共享方法冲突 | 低 | 低 | 通过静态委托调用共享方法 |

### 5.2 回滚策略

如果出现问题，可以回滚：

```bash
git reset --hard HEAD~1
# 或删除新类，恢复原类
git checkout HEAD -- src/main/java/com/minicad/export/json/StepPreviewJsonExporter.java
```

## 6. 后续优化

完成Face提取后，StepPreviewJsonExporter将减少到约7,300行，可继续：

1. **提取Edge处理模块** - sample*, edge*方法（约1,000行）
2. **提取表示构建模块** - buildRepresentationPayload方法（约500-700行）
3. **提取几何构建模块** - buildLegacyGeometry方法（约300行）

**最终目标：** 将StepPreviewJsonExporter减少到5,000行以下。

## 7. 成功标准

- ✅ StepPreviewJsonExporter减少约1,200行（8,540 → ~7,300）
- ✅ 所有测试通过
- ✅ 无性能退化
- ✅ Face处理逻辑集中，职责清晰
- ✅ 代码可读性和可维护性提升

## 8. 参考资料

- [提取类重构](https://refactoring.guru/extract-class)
- [单一职责原则](https://en.wikipedia.org/wiki/Single-responsibility_principle)
- 项目历史提交：
  - `74869da` - extract StepPmiPayloadBuilder (减少5,061行)
  - `ae35269` - PMI implementation plan
  - `7b1008f` - PMI design document