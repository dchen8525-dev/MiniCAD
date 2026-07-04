# MiniCAD 大文件重构设计文档

**日期**: 2026-07-04
**状态**: 设计完成，待用户审核
**作者**: ZCode Agent

---

## 1. 概述

### 1.1 目标

将MiniCAD项目中超过2000行的Java文件优化到2000行以下，提高代码可维护性、可测试性和可读性。

### 1.2 重构范围

优先处理最大的3个文件：
- `StepPreviewJsonExporter.java` - 14,973行
- `StepEntityResolver.java` - 13,324行
- `StepCadBuilder.java` - 7,429行

### 1.3 重构策略

**激进重构策略**：直接拆分文件，可能需要修改调用方和API，追求代码结构最优化。

**渐进式执行**：从简单到复杂，逐个文件拆分，每个文件拆分后立即测试验证。

---

## 2. 执行顺序

### 2.1 阶段划分

```
Phase 1: StepCadBuilder.java (7429行) → 8个文件
Phase 2: StepEntityResolver.java (13324行) → 3个文件
Phase 3: StepPreviewJsonExporter.java (14973行) → 9个文件
```

### 2.2 选择理由

1. **先处理StepCadBuilder**：结构清晰，方法分组明确，最易拆分
2. **再处理StepEntityResolver**：已部分拆分（注册逻辑独立），剩余部分适中
3. **最后处理StepPreviewJsonExporter**：最复杂，方法最多，依赖最重

---

## 3. Phase 1: StepCadBuilder.java拆分设计

### 3.1 当前状态

- **文件路径**: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`
- **行数**: 7429行
- **私有方法**: 216个
- **缓存字段**: 60个Map
- **import数量**: 252个

### 3.2 主要职责

CAD几何构建器，从解析后的StepEntity构建内部几何和拓扑对象（Point, Direction, Curve, Surface, Vertex, Edge, Face, Shell, Solid等）。

### 3.3 公共方法

约70个公共构建方法，按类型分组：
- 基础几何: `buildPoint`, `buildDirection`, `buildVector`, `buildPlacement`
- 2D曲线: `buildLine2`, `buildCircle2`, `buildEllipse2`, `buildBSplineCurve2`
- 3D曲线: `buildLine`, `buildCircle`, `buildBSplineCurve`, `buildTrimmedCurve`
- 曲面: `buildPlane`, `buildCylindricalSurface`, `buildBSplineSurface`
- 拓扑: `buildVertex`, `buildEdge`, `buildFace`, `buildShell`, `buildSolid`

### 3.4 拆分方案

| 新文件名 | 预估行数 | 功能内容 | 职责 |
|---------|---------|---------|-----|
| `StepCadBuilder.java` | ~1500 | 主入口类 | 公共API + 缓存管理 |
| `StepCadGeometryBuilder.java` | ~1500 | 几何构建 | Point, Direction, Vector, Placement, Axis |
| `StepCadCurveBuilder.java` | ~1000 | 曲线构建 | Line, Circle, Ellipse, BSpline, Composite (2D/3D) |
| `StepCadSurfaceBuilder.java` | ~1000 | 曲面构建 | Plane, Cylindrical, Conical, Spherical, Toroidal, BSpline |
| `StepCadTopologyBuilder.java` | ~500 | 拓扑构建 | Vertex, Edge, Face, Shell, Solid拓扑对象 |
| `StepCadSolidBuilder.java` | ~800 | Solid构建 | Extruded, Revolved, Box, Sphere, Cylinder实体 |
| `StepCadTransformOps.java` | ~500 | 变换操作 | Point3, Direction3, Curve3, Surface变换 |
| `StepCadSamplingUtils.java` | ~500 | 采样工具 | Fresnel函数, 曲线采样, 辅助工具 |

### 3.5 文件依赖关系

```
StepCadBuilder (主类)
  ├─→ StepCadGeometryBuilder
  ├─→ StepCadCurveBuilder
  ├─→ StepCadSurfaceBuilder
  ├─→ StepCadTopologyBuilder
  ├─→ StepCadSolidBuilder
  │   └─→ StepCadTransformOps
  │   └─→ StepCadSamplingUtils
  └─→ StepCadImplicitBspline (内部类提取)
```

### 3.6 注意事项

- `StepCadTopologyBuilder.java`和`StepCadSolidBuilder.java`可能已存在同名类，需检查并整合或重命名
- 内部类`ImplicitBSplineCurveData`和`ImplicitBSplineSurfaceData`可提取为独立类
- 缓存Map字段保留在主类中，通过构造函数或静态方法传递给子Builder

---

## 4. Phase 2: StepEntityResolver.java拆分设计

### 4.1 当前状态

- **文件路径**: `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`
- **行数**: 13324行
- **import数量**: 772个
- **已拆分**: 大部分注册逻辑已独立为Registry类

### 4.2 主要职责

STEP实体解析器，将STEP AST解析结果转换为语义化的StepEntity对象。通过EntityFactory注册表处理不同类型的STEP实体。

### 4.3 公共方法

- `resolveAll(StepFile file)` - 解析所有实体
- `resolveFactory(StepEntityInstance instance)` - 查找工厂

### 4.4 拆分方案

| 新文件名 | 预估行数 | 功能内容 | 职责 |
|---------|---------|---------|-----|
| `StepEntityResolver.java` | ~2000 | 主入口类 | resolveAll(), resolve(), resolveFactory(), 基础类型判断 |
| `StepResolverUtils.java` | ~1500 | 参数提取工具 | 参数值提取、类型转换、默认值处理、引用解析辅助 |
| `StepEntityValidator.java` | ~1000 | 实体验证 | isShellEntity(), isBooleanOperandEntity(), isSupported*() |

### 4.5 已存在的Registry类

项目已将注册逻辑拆分为多个独立Registry类：
- `StepEntityRegistry.java` (67行) - 主注册聚合器
- `GeometryRegistry1/2.java` - 几何实体
- `TopologyRegistry.java` - 拓扑实体
- `ProductRegistry.java` - 产品实体
- `RepresentationRegistry1/2.java` - 表示实体
- `ProfileRegistry.java` - 轮廓实体
- `ToleranceRegistry.java` - 公差实体
- `AnnotationRegistry.java` - 注释实体
- `UnitRegistry.java` - 单位实体
- `ManufacturingRegistry.java` - 制造特征
- `KinematicRegistry.java` - 运动学
- `FeaRegistry.java` - FEA
- `ConfigManagementRegistry.java` - 配置管理
- `ClassificationRegistry.java` - 分类
- `MiscellaneousRegistry1-4.java` - 其他

### 4.6 设计原则

- `StepResolverUtils`和`StepEntityValidator`为包级私有类
- 仅供`StepEntityResolver`内部使用
- 最小化public接口

---

## 5. Phase 3: StepPreviewJsonExporter.java拆分设计

### 5.1 当前状态

- **文件路径**: `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`
- **行数**: 14973行
- **私有方法**: 254个
- **import数量**: 508个

### 5.2 主要职责

STEP文件预览JSON导出器，将STEP文件转换为前端可渲染的JSON格式，包含几何、拓扑、PMI、装配体等数据。

### 5.3 公共方法

- `export(String stepText)` - 导出JSON字符串
- `exportBinary(String stepText)` - 导出二进制JSON
- `exportGlb(String stepText)` - 导出GLB格式

### 5.4 私有方法分组

| 功能模块 | 方法数量 | 代表方法 |
|---------|---------|---------|
| Payload构建 | ~30 | buildPayload, buildValidationPayload, buildGeometrySummary |
| Face处理 | ~40 | toCylindricalFacePayload, toConicalFacePayload, triangulateParametricFace |
| Edge处理 | ~25 | buildTopologyEdgePayload, sampledCurvePayload, sampleEdge |
| 采样/Sampling | ~20 | sampleTopologySurfaceGrid, sampleSurfaceGrid, sampleParametricOrientedEdge |
| PMI处理 | ~60 | buildPmiPayloads, appendDraughtingAnnotationPmi, toPmiPayload |
| 装配体处理 | ~15 | buildAssemblyData, collectLinkedShapeRepresentations |
| Target收集 | ~90 | collectSemanticTargets, collectTargetsFor*, append*Targets |
| 辅助工具 | ~20 | reverseFacePayload, interpolate, reverseClosedLoop |

### 5.5 拆分方案

| 新文件名 | 预估行数 | 功能内容 | 职责 |
|---------|---------|---------|-----|
| `StepPreviewJsonExporter.java` | ~800 | 主入口类 | export/exportBinary/exportGlb + 主流程编排 |
| `PreviewPayloadBuilder.java` | ~1200 | Payload构建 | buildPayload, buildValidationPayload, Payload组装 |
| `PreviewFaceBuilder.java` | ~2000 | Face处理 | 各种FacePayload构建 + Face曲面采样 |
| `PreviewEdgeBuilder.java` | ~1200 | Edge处理 | EdgePayload构建 + Edge曲线采样 |
| `PreviewPmiBuilder.java` | ~1500 | PMI处理 | PMI数据提取和构建 |
| `PreviewAssemblyBuilder.java` | ~1000 | 装配体处理 | 装配层级构建 |
| `PreviewTargetCollector.java` | ~2000 | Target收集 | 90+个target收集方法 |
| `PreviewSamplingUtils.java` | ~1000 | 采样工具 | 几何采样辅助方法 |
| `PreviewPayloadTypes.java` | ~500 | 类型定义 | 所有Payload类型定义和常量 |

### 5.6 已存在文件的处理

以下文件已存在，需整合或重命名新类：
- `PreviewFaceBuilder.java` (2829行) - 已存在
- `PreviewSamplingUtils.java` (2286行) - 已存在
- `PreviewPayloadTypes.java` (2856行) - 已存在
- `PreviewUvMapper.java` (2286行) - 已存在
- `PreviewSerializers.java` (2171行) - 已存在

**建议方案**：新类命名为`Preview*V2.java`，后续整合阶段合并

### 5.7 文件依赖关系

```
StepPreviewJsonExporter (主类)
  ├─→ PreviewPayloadBuilder
  ├─→ PreviewFaceBuilder
  ├─→ PreviewEdgeBuilder
  ├─→ PreviewPmiBuilder
  ├─→ PreviewAssemblyBuilder
  ├─→ PreviewTargetCollector
  └─→ PreviewSamplingUtils
      └─→ PreviewPayloadTypes
```

---

## 6. 测试验证策略

### 6.1 组合测试策略

采用**单元测试 + 黄金文件测试**双重验证：

### 6.2 单元测试

为每个拆分后的新文件添加关键测试：

**Phase 1 测试文件**：
- `StepCadGeometryBuilderTest.java` (新增)
- `StepCadCurveBuilderTest.java` (新增)
- `StepCadSurfaceBuilderTest.java` (新增)
- `StepCadTopologyBuilderTest.java` (新增)
- `StepCadSolidBuilderTest.java` (新增)
- `StepCadSamplingUtilsTest.java` (新增)

**Phase 2 测试文件**：
- `StepResolverUtilsTest.java` (新增)
- `StepEntityValidatorTest.java` (新增)

**Phase 3 测试文件**：
- `PreviewPayloadBuilderTest.java` (新增)
- `PreviewFaceBuilderTest.java` (新增)
- `PreviewEdgeBuilderTest.java` (新增)
- `PreviewPmiBuilderTest.java` (新增)
- `PreviewAssemblyBuilderTest.java` (新增)
- `PreviewTargetCollectorTest.java` (新增)
- `PreviewSamplingUtilsTest.java` (新增)

**测试重点**：
- 每个新类的核心公共方法
- 边界条件和异常处理
- 参数验证

### 6.3 黄金文件测试

利用现有examples目录下的STEP文件进行回归测试。

**流程**：
1. **记录基线**：
   ```bash
   # 对每个examples/*.step文件：
   - export() → 保存JSON到 golden/*.json
   - exportGlb() → 保存GLB到 golden/*.glb
   ```

2. **重构后验证**：
   ```bash
   # 重新运行
   - 对比输出是否一致（或语义等价）
   ```

3. **关键测试文件**（优先）：
   - `minimal-square.step` - 最小立方
   - `plate-with-round-hole.step` - 带孔平板
   - `engine.stp` - 发动机装配
   - `bspline-patch.step` - B样条曲面
   - `conical-hole.step` - 锥形孔
   - `conical-seam-*.step` - 锥形缝合系列

**测试范围**：
- 优先测试：10-20个关键文件
- 完整测试：examples目录所有文件（约50+个）

**对比工具**：
- JSON对比：Jackson JSON compare
- GLB对比：二进制对比或GLB解析后对比

---

## 7. 实施时间表

### 7.1 Phase 1: StepCadBuilder拆分

| 天数 | 任务 |
|-----|------|
| Day 1-2 | 创建新文件骨架，移动方法 |
| Day 3 | 添加单元测试 |
| Day 4 | 黄金文件测试验证 |
| Day 5 | 修复问题，完成验证 |

### 7.2 Phase 2: StepEntityResolver拆分

| 天数 | 任务 |
|-----|------|
| Day 6-7 | 创建新文件骨架，移动方法 |
| Day 8 | 添加单元测试 |
| Day 9 | 黄金文件测试验证 |
| Day 10 | 修复问题，完成验证 |

### 7.3 Phase 3: StepPreviewJsonExporter拆分

| 天数 | 任务 |
|-----|------|
| Day 11-14 | 创建新文件骨架，移动方法（最复杂） |
| Day 15-16 | 添加单元测试 |
| Day 17-18 | 黄金文件测试验证 |
| Day 19-20 | 修复问题，完成验证 |

### 7.4 总时间

**预计**: 8-12工作日（约3周）

---

## 8. 拆分原则

### 8.1 设计原则

1. **单一职责原则**：每个新文件只负责一个明确的功能模块
2. **最小接口原则**：优先包级私有类，最小化public方法
3. **降低耦合**：减少跨文件调用，通过构造函数或回调传递依赖
4. **保持兼容**：主类的公共API尽可能保持稳定

### 8.2 代码组织原则

1. **方法分组**：按数据类型和功能分组移动方法
2. **依赖传递**：缓存和上下文通过构造函数传递
3. **内部类提取**：将内部类提取为独立类或嵌套在对应Builder中
4. **import优化**：移动方法时同步移动相关import

---

## 9. 风险与缓解措施

### 9.1 已识别风险

| 风险 | 影响 | 缓解措施 |
|-----|------|---------|
| 破坏现有API | 高 | 保持主类公共方法签名不变 |
| 测试覆盖不足 | 中 | 黄金文件测试覆盖所有examples |
| 跨文件调用复杂 | 中 | 通过构造函数传递依赖，减少静态调用 |
| 已存在同名类冲突 | 低 | 使用V2命名，后续整合 |

### 9.2 回滚策略

- 每个Phase完成后创建git tag
- 如果某Phase失败，可回滚到上一个Phase的tag
- 每个新文件独立，可单独修复或回滚

---

## 10. 成功标准

### 10.1 量化目标

- 所有目标文件行数 < 2000行
- 所有新文件行数 < 2000行
- 所有单元测试通过
- 黄金文件测试输出一致

### 10.2 质量目标

- 每个文件职责单一清晰
- 公共接口最小化
- 测试覆盖率提升
- 代码可读性提升

---

## 11. 附录

### 11.1 文件现状汇总

| 文件 | 当前行数 | 目标文件数 | 状态 |
|-----|---------|----------|-----|
| StepCadBuilder.java | 7429 | 8 | 待拆分 |
| StepEntityResolver.java | 13324 | 3 | 待拆分 |
| StepPreviewJsonExporter.java | 14973 | 9 | 待拆分 |
| StepDumpApp.java | 3632 | 2 | 后续处理 |
| StepMeshExporter.java | 2857 | 2 | 后续处理 |
| PreviewFaceBuilder.java | 2829 | 2 | 后续处理 |
| PreviewUvMapper.java | 2286 | 1 | 后续处理 |
| PreviewSerializers.java | 2171 | 1 | 后续处理 |

### 11.2 相关文档

- AGENTS.md - MiniCAD Overnight Fix Queue
- README.md - 项目说明
- examples/ - STEP测试文件目录

---

**文档状态**: 待用户审核
**下一步**: 用户审核后，调用writing-plans skill创建详细实施计划