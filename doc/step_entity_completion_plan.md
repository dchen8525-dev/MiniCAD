# MiniCAD STEP 实体补全计划

> 更新时间：2026-04-21
>
> 参考资料：
> - `doc/occt_step_support.md`
> - `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`
> - `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`
> - `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

## 1. 目标与原则

本计划的目标不是单纯增加 `StepXxx` model 类或 `registry.put()` 数量，而是把 STEP 实体支持补齐到可验证的端到端链路：

`STEP syntax -> semantic resolve -> internal geometry/topology -> preview/export -> regression tests`

执行原则：

- 优先修复“看起来已支持、实际结果不正确”的实体
- 优先提升真实 CAD 文件打开率，而不是优先补冷门 AP242 辅助实体
- 每新增一个实体支持，都必须补 resolver、builder、exporter、tests 的一致性
- 文档基线必须与代码状态同步，避免重复按错误清单排期

## 2. 当前状态判断

### 2.1 已有基础

当前代码库在 resolver 层已经具备很广的注册范围，覆盖了大量 OCCT 报告中的实体类别，包括：

- 基础几何与拓扑
- Swept solids
- CSG / half-space / boolean
- Tessellated entities
- 高级曲面：`SURFACE_OF_TRANSLATION`、`SURFACE_OF_PROJECTION`、`PARABOLOID_SURFACE`、`HYPERBOLOID_SURFACE`
- AP242 关系类：`DATUM_SYSTEM`、`PROJECTED_ZONE_DEFINITION`、`NON_UNIFORM_ZONE_DEFINITION`
- Kinematic / validation / FEA 的一部分实体

对应入口：

- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`
- `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`
- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

### 2.2 当前真正的短板

当前的主要缺口已经不是“有没有实体类”，而是以下三类：

1. resolver 已注册，但 builder 实现仍是占位或几何语义不完整
2. builder 已能构建，但 preview/export 仍会降级成 `unsupportedFaces` 或 `unsupportedBooleans`
3. 文档仍把部分已支持实体标成“不支持”，导致计划基线失真

### 2.3 已识别的高风险实体

以下实体应优先校正，因为它们现在最容易出现“解析成功但结果错误”的假支持：

- `EXTRUDED_FACE_SOLID`
- `REVOLVED_FACE_SOLID`
- `SURFACE_CURVE_SWEPT_FACE_SOLID`
- `CYLINDER_VOLUME`
- `SPHERE_VOLUME`
- `TORUS_VOLUME`
- `PRISM_VOLUME`
- `RIGHT_CIRCULAR_CYLINDER_VOLUME`
- `RIGHT_CIRCULAR_CONE_VOLUME`

特别说明：

- `RIGHT_CIRCULAR_CONE_VOLUME` 当前直接复用 `resolveCylinderVolume()`，需要优先纠正语义模型和构建逻辑。
- `EXTRUDED_FACE_SOLID` / `REVOLVED_FACE_SOLID` 当前构建实现对 STEP 中的 sweep pose 消费不足，容易生成方向或轴错误的几何。

## 3. 分阶段路线图

### Phase 0：建立真实支持矩阵

#### 目标

把“支持”拆成 4 个层级，而不是只有“已注册 / 未注册”：

- `resolved_only`
- `resolved_and_built`
- `resolved_built_previewed`
- `resolved_built_previewed_tested`

#### 工作项

- 基于代码静态盘点，维护 resolver / builder / exporter / tests 四层支持矩阵
- 对照 `StepEntityResolver`、`StepCadBuilder`、`StepPreviewJsonExporter` 和测试代码，整理“已注册但未构建”“已构建但未导出”“已导出但未覆盖测试”的差集清单
- 生成 `doc/generated/step-support-matrix.md`
- 生成“已注册但未构建”和“已构建但未导出”的差集报告
- 将报告输出加入文档工作流或本地维护流程

#### 交付物

- `doc/generated/step-support-matrix.md`
- `doc/generated/ap242-gap-report.md`
- 一份人工维护的“高优先级实体队列”

#### 风险

- 当前仓库中可能存在未清理的重复类或过时文档，容易干扰静态盘点结果
- 已发现 `StepIndexedPolycurve.java` / `StepIndexedPolyCurve.java` 重复类定义问题，需先解除编译错误

### Phase 1：修复“假支持”实体

#### 目标

优先把已经暴露给用户、但几何语义可能错误的实体修成可靠实现。

#### 工作项

- 为 swept face solids 正确消费 `swept_face`、`position`、`extruded_direction` / `axis`
- 为 advanced volumes 正确消费 placement、orientation、dimensions
- 拆分 `RIGHT_CIRCULAR_CYLINDER_VOLUME` 与 `RIGHT_CIRCULAR_CONE_VOLUME` 的 resolver / model / builder
- 为每类实体补精确失败信息，而不是简单落入通用 `unsupported`

#### 涉及代码

- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`
- `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`
- 必要时新增/修正 `src/main/java/com/minicad/step/model/...`

#### 验收标准

- 典型样例中几何姿态正确
- `StepCadBuilderTest` 能覆盖放置、旋转、尺寸参数
- `StepDumpAppTest` 输出不再出现误导性的成功摘要

### Phase 2：补全主建模链路中的高价值实体

#### 目标

提升真实 STEP 文件的打开率和几何保真度。

#### 优先顺序

1. `SURFACE_CURVE_SWEPT_FACE_SOLID`
2. 一般化 `BOOLEAN_RESULT`
3. `BOOLEAN_CLIPPING_RESULT` / `COMPLEX_CLIPPING_RESULT`
4. `RIGHT_CIRCULAR_CYLINDER_VOLUME`
5. `RIGHT_CIRCULAR_CONE_VOLUME`
6. 其他 swept / csg / bounded-half-space 组合路径

#### 工作项

- 将 boolean 支持从当前“half-space 特化裁剪”扩展到一般 `solid-solid`
- 抽离统一的 boolean operand 转换和诊断逻辑
- 补 swept face / swept area 在复杂 profile、voids、trim loop 下的处理
- 对 half-space、boxed half-space、polygonal bounded half-space 建立一致的裁剪接口

#### 结果指标

- `unsupportedBooleanCount` 显著下降
- `unsupportedFaceCount` 在含 swept / csg 模型中明显下降
- `examples/` 与新增样例文件能稳定通过 dump/preview 测试

### Phase 3：做 builder 与 preview/export 一致化

#### 目标

避免“builder 支持了，但前端预览仍然不支持”。

#### 工作项

- 每新增实体都补 `StepPreviewJsonExporter`
- 统一 `viaDefinitionType`
- 对复杂几何统一输出面类型、原始 STEP 实体类型、失败原因、是否降级三角化
- 优先保证稳定三角化输出，再考虑 viewer 端参数化重建

#### 涉及代码

- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`
- `src/main/java/com/minicad/app/StepPreviewPayloadTypes.java`
- `src/main/resources/static/*`

### Phase 4：补 AP242 PMI / GD&T 关系链

#### 目标

把 PMI/GD&T 做成“解析 + 关联 + 导出元数据”能力，而不是一开始就追求几何求值。

#### 第一批实体

- `GEOMETRIC_TOLERANCE_RELATIONSHIP`
- `DATUM_SYSTEM`
- `PROJECTED_ZONE_DEFINITION`
- `NON_UNIFORM_ZONE_DEFINITION`
- `VALIDATION_PROPERTY_REPRESENTATION`
- `CALCULATED_GEOMETRIC_REPRESENTATION_ITEM`

#### 工作项

- 串起 `shape aspect -> representation -> annotation -> requirement item`
- 在 dump / preview payload 中增加可查询结构
- 为前端保留关联展示接口，但不强制先做复杂可视化

#### 成果定义

- 可以从产品定义或 shape aspect 查询到 PMI/GD&T 关系
- preview payload 中能稳定导出关联元数据
- dump 输出对调试 AP242 文件足够友好

### Phase 5：补 kinematic / validation / FEA

#### 目标

补齐 OCCT 报告中的扩展应用协议实体，但放在主几何链路之后。

#### 优先级建议

- `KINEMATIC_PATH`
- `KINEMATIC_FRAME_BASED_TRANSFORMATION`
- `VALIDATION_RESULT_REPRESENTATION`
- `ELEMENT_VOLUME_2D`
- `ELEMENT_VOLUME_3D`
- `NODE_SET`
- `ELEMENT_SET`

#### 原则

- 先做解析与结构化导出
- 几何或仿真语义求值仅在明确有业务需求时推进

## 4. 建议的实施顺序

建议按以下顺序推进：

1. 生成真实支持矩阵和差异清单
2. 修 swept face solids 和 advanced volumes 的语义正确性
3. 扩一般化 boolean
4. 补 preview/export 一致化
5. 补 PMI/GD&T 元数据链路
6. 最后补 kinematic / validation / FEA

## 5. 测试策略

每补一类实体，至少补 3 层测试：

- `StepEntityResolverTest`
- `StepCadBuilderTest`
- `StepPreviewJsonExporterTest`

必要时追加：

- `StepDumpAppTest`
- `StepBenchmarkAppTest`

#### 测试要求

- 覆盖 happy path 与 failure path
- 覆盖 placement / orientation / trimming / voids / aliases
- 验证 `unsupportedFaceCount` 与 `unsupportedBooleanCount`
- 尽量用小型 inline STEP 片段
- 对复杂回归场景新增 `examples/` 文件

## 6. 文档维护要求

以下文档需要和实现同步更新：

- `AGENTS.md`
- `doc/occt_step_support.md`
- `doc/step_entity_completion_plan.md`
- `doc/generated/*`

维护要求：

- 不再使用“解析支持”和“几何支持”混写的表述
- 每个实体状态至少标注到 resolver / builder / exporter / tests 四层之一
- 任何新增占位实现都必须在文档中明确标注限制条件

## 7. 当前阻塞项

当前仓库存在编译阻塞，影响自动化覆盖报告生成：

- `src/main/java/com/minicad/step/model/geometry/StepIndexedPolycurve.java`
- `src/main/java/com/minicad/step/model/geometry/StepIndexedPolyCurve.java`

两者导致 `StepIndexedPolyCurve` 重复定义。该问题应作为 Phase 0 之前的前置修复项处理。
