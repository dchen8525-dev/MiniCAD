# MiniCAD STEP 实体补全 Issue 总表

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

### 2.2 当前真正的短板

当前的主要缺口已经不是“有没有实体类”，而是以下三类：

1. resolver 已注册，但 builder 实现仍是占位或几何语义不完整
2. builder 已能构建，但 preview/export 仍会降级成 `unsupportedFaces` 或 `unsupportedBooleans`
3. 文档仍把部分已支持实体标成“不支持”，导致计划基线失真

### 2.3 当前高风险点

- `EXTRUDED_FACE_SOLID` / `REVOLVED_FACE_SOLID` 当前 builder 仍默认使用世界坐标方向，未正确消费 STEP placement / axis 语义
- `SURFACE_CURVE_SWEPT_FACE_SOLID` 已 resolve/build，但复杂轨迹和姿态一致性仍需校正
- `RIGHT_CIRCULAR_CONE_VOLUME` 当前注册复用了 `resolveCylinderVolume()`，属于明确的语义错误
- `BOOLEAN_RESULT` 当前仍以 half-space 特化为主，一般 `solid-solid` union/intersection/difference 仍不完整
- `StepIndexedPolycurve.java` / `StepIndexedPolyCurve.java` 重复定义会阻塞基于编译成功的矩阵生成

## 3. Issue 大表

支持状态分层统一使用以下术语：

- `resolved_only`
- `resolved_and_built`
- `resolved_built_previewed`
- `resolved_built_previewed_tested`

| 优先级 | Issue 名称 | 涉及类和方法 | 修改思路 | 验证思路 | 当前状态 |
|---|---|---|---|---|---|
| P0 | 修复 `StepIndexedPolyCurve` 重复定义阻塞 | `src/main/java/com/minicad/step/model/geometry/StepIndexedPolycurve.java`<br>`src/main/java/com/minicad/step/model/geometry/StepIndexedPolyCurve.java` | 保留一个规范文件名和单一 record 定义；同步清理引用，确保大小写差异不再造成重复类型。 | 运行 `mvn test` 或至少编译相关模块；确认不再出现 duplicate class / duplicate type 定义错误。 | `blocked`：当前两个文件都声明 `public record StepIndexedPolyCurve`，属于 Phase 0 之前的前置阻塞。 |
| P0 | 建立 STEP 实体真实支持矩阵 | `StepEntityResolver` 注册表<br>`StepCadBuilder` 构建入口<br>`StepPreviewJsonExporter.buildPayload()` / `buildPreviewFaceResult()`<br>测试类：`StepEntityResolverTest`、`StepCadBuilderTest`、`StepPreviewJsonExporterTest`、`StepDumpAppTest` | 以“resolver / builder / exporter / tests”四层重新盘点实体状态，输出统一矩阵；不再使用“已注册=已支持”的表述。 | 生成 `doc/generated/step-support-matrix.md`；抽样核对高风险实体状态与代码一致。 | `todo`：文档已有原则，但仓库内暂无统一矩阵产物。 |
| P0 | 生成差集报告和人工优先队列 | `doc/generated/ap242-gap-report.md`<br>`StepEntityResolver`、`StepCadBuilder`、`StepPreviewJsonExporter` | 自动或半自动整理“已注册未构建”“已构建未导出”“已导出未测试”三类差集；把高价值实体收敛成人工 issue 队列。 | 差集报告可追溯到具体实体和代码入口；人工检查高优先级列表能覆盖 swept/boolean/advanced volume 主链路。 | `todo`：计划里已有交付物定义，但尚未物化。 |
| P1 | 修正 `EXTRUDED_FACE_SOLID` 几何语义 | `StepEntityResolver.resolveExtrudedFaceSolid()`<br>`StepCadBuilder.buildExtrudedFaceSolid()`<br>`StepCadBuilder.buildExtrudedProfile()`<br>`src/main/java/com/minicad/step/model/product/StepExtrudedFaceSolid.java` | 让 builder 真正消费 `sweptFace`、`position`、`direction`、`depth`；从 face 的局部坐标和 STEP 放置变换生成拉伸实体，而不是固定按世界 `Z` 方向拉伸。必要时补充局部坐标系解析。 | `StepCadBuilderTest` 增加 placement、rotation、reverse direction、depth 异常场景；`StepDumpAppTest` 断言不再出现误导性成功结果。 | `resolved_and_built`：resolver 已解析 4 个关键字段，但 builder 仍 `Direction3(0,0,depth)`，存在假支持风险。 |
| P1 | 修正 `REVOLVED_FACE_SOLID` 几何语义 | `StepEntityResolver.resolveRevolvedFaceSolid()`<br>`StepCadBuilder.buildRevolvedFaceSolid()`<br>`StepCadBuilder.buildRevolvedProfile()`<br>`src/main/java/com/minicad/step/model/product/StepRevolvedFaceSolid.java` | 正确消费 `position`、`axis`、`angle`；从 STEP 轴系推导真实旋转轴和原点，不再固定绕世界 `Z` 轴和世界原点旋转。补角度单位和部分旋转端盖逻辑校验。 | 增加 axis 偏移、axis 旋转、半角/整圆、非法 angle 测试；检查预览面数量与姿态是否正确。 | `resolved_and_built`：resolver 已持有 `position/axis/angle`，builder 仍固定 `axisOrigin=(0,0,0)`、`axis=(0,0,1)`。 |
| P1 | 修正 `SURFACE_CURVE_SWEPT_FACE_SOLID` / `SWEPT_FACE_SOLID` 扫掠链路 | `StepEntityResolver.resolveSweptFaceSolid()`<br>`StepCadBuilder.buildSweptFaceSolid()`<br>`StepCadBuilder.buildSweptProfileAlongCurve()`<br>`src/main/java/com/minicad/step/model/product/StepSweptFaceSolid.java` | 强化 trajectory 采样与姿态传播，明确 swept face 在曲线 Frenet/frame 变化中的朝向策略；必要时区分 `SURFACE_CURVE_SWEPT_FACE_SOLID` 与一般 `SWEPT_FACE_SOLID` 的限制。 | 添加直线、圆弧、空间曲线、小半径高曲率路径测试；检查 `unsupportedFaceCount` 和几何自交/翻转回归。 | `resolved_and_built`：已有 resolve/build，但复杂轨迹正确性尚未证明。 |
| P1 | 拆分 `RIGHT_CIRCULAR_CYLINDER_VOLUME` 与 `CYLINDER_VOLUME` 语义 | `StepEntityResolver.resolveCylinderVolume()`<br>`StepEntityResolver` registry `12050-12055` 附近<br>`StepCadBuilder.buildCylinderVolume()`<br>必要时新增 `StepRightCircularCylinderVolume` model | 将 `RIGHT_CIRCULAR_CYLINDER_VOLUME` 从通用 `resolveCylinderVolume()` 中拆出；根据实体定义补齐独立 model 或最少独立 resolver 分支，避免后续和 cone 共享错误语义。 | 新增 resolver/builder 测试，确认实体名、参数个数、placement 和尺寸字段按独立语义落地。 | `resolved_and_built` 但语义混用：当前注册直接复用 `resolveCylinderVolume()`。 |
| P1 | 修正 `RIGHT_CIRCULAR_CONE_VOLUME` 错误解析 | `StepEntityResolver.resolveCylinderVolume()`<br>`StepEntityResolver` registry `12054-12055` 附近<br>必要时新增 `resolveRightCircularConeVolume()`<br>`StepCadBuilder` 对应 cone builder / model | 为 `RIGHT_CIRCULAR_CONE_VOLUME` 建立独立 resolver、model、builder；按锥体参数消费 placement、height、半径/角度定义，彻底去掉对 cylinder 逻辑的复用。 | 增加与 cylinder 对照测试；断言 entityName、尺寸语义、预览几何都不再等同于圆柱。 | `high_risk`：当前直接注册到 `resolveCylinderVolume()`，属于明确错误实现。 |
| P1 | 完善 advanced volumes 的 placement / orientation / dimensions | `StepEntityResolver.resolveSphereVolume()`<br>`resolveTorusVolume()`<br>`resolvePrismVolume()`<br>`StepCadBuilder.buildCylinderVolume()`<br>`buildSphereVolume()`<br>`buildTorusVolume()`<br>`buildPrismVolume()` | 把 `CYLINDER_VOLUME`、`SPHERE_VOLUME`、`TORUS_VOLUME`、`PRISM_VOLUME` 的局部放置、方向、尺寸解释统一到 STEP placement 语义；避免仅按原点或局部默认朝向建模。 | 对每个 volume 补位置偏移、姿态旋转、非法尺寸测试；校验 preview 中 `viaDefinitionType`、面数、边界盒是否匹配。 | `resolved_and_built`：基本 builder 已存在，但 `buildCylinderVolume()`、`buildTorusVolume()` 基本仍按世界轴默认朝向，`buildPrismVolume()` 也只部分使用 location。 |
| P1 | 细化 swept/volume 失败诊断信息 | `StepCadBuilder.buildExtrudedFaceSolid()`<br>`buildRevolvedFaceSolid()`<br>`buildSweptFaceSolid()`<br>`buildBooleanResult()`<br>`StepDumpApp` unsupported reason 聚合逻辑 | 把“通用 unsupported”细分为 placement 不支持、axis 非法、trajectory 为空、profile 自交、参数非法等可定位原因；让 dump/preview 对失败具备工程可调试性。 | 检查 `StepDumpAppTest` 中 unsupported reason 文案；新增 failure path 用例，确保不同失败分支可区分。 | `partial`：boolean 已有较明确原因，其它 swept/volume 仍偏通用。 |
| P2 | 一般化 `BOOLEAN_RESULT`，支持 `solid-solid` 运算 | `StepEntityResolver.resolveBooleanResult()`<br>`StepCadBuilder.buildBooleanResult()`<br>`buildBooleanOperandSolid()` | 从当前 half-space 特化扩展到一般 `solid-solid` union/intersection/difference`；抽出统一 operand 转换层，保证 CSG、B-Rep、swept solids 都能进入同一路径。 | 增加 union/intersection/difference 的正向样例和失败样例；观测 `unsupportedBooleanCount` 显著下降。 | `resolved_built_previewed_tested` 但能力受限：现有测试已覆盖 half-space 特化和不支持提示，一般 solid-solid 仍未完成。 |
| P2 | 统一 half-space / boxed / polygonal bounded half-space 裁剪接口 | `StepEntityResolver.resolveHalfSpaceSolid()`<br>`resolveBoxedHalfSpace()`<br>`resolvePolygonalBoundedHalfSpace()`<br>`StepCadBuilder.buildHalfSpaceSolidAsSolid()`<br>`buildPolygonalBoundedHalfSpaceAsSolid()`<br>`clipSolidWithHalfSpace()`<br>`unionWithHalfSpace()` | 把半空间及其包围域裁剪逻辑抽象成一致接口，统一 difference / intersection / union 的行为和报错，减少散落的特例分支。 | 针对 `HALF_SPACE_SOLID`、`BOXED_HALF_SPACE`、`POLYGONAL_BOUNDED_HALF_SPACE` 分别做运算组合回归；检查错误消息是否一致。 | `partial`：已有基本能力，但接口和语义仍偏分散，union 仍明显受限。 |
| P2 | 补齐 builder 与 preview/export 一致性 | `StepPreviewJsonExporter.buildPayload()`<br>`buildPreviewFaceResult()`<br>`unwrapParametricPreviewSurface()`<br>`describeUnsupportedPreviewSurface()`<br>`StepPreviewPayloadTypes` | 每个新增/修正实体同步补 exporter；统一输出 `viaDefinitionType`、原始实体类型、失败原因、是否降级三角化，避免“builder 支持了但 preview 仍 silent downgrade”。 | `StepPreviewJsonExporterTest` 覆盖新实体和失败分支；核对 payload 中 `viaDefinitionType`、`unsupportedFaceCount`、`unsupportedBooleanCount`。 | `partial`：boolean 已有 `viaDefinitionType` 透出，更多 swept/advanced volume 仍需统一导出语义。 |
| P3 | 打通 AP242 PMI / GD&T 关系链 | `StepEntityResolver.resolveDatumSystem()`<br>`resolveDatumSystemReference()`<br>`resolveProjectedZoneDefinition()`<br>`resolveNonUniformZoneDefinition()`<br>`StepPreviewJsonExporter` PMI 元数据导出入口 | 先做“解析 + 关联 + 导出元数据”，串起 `shape aspect -> representation -> annotation -> requirement item`，不急于做几何求值；在 payload 中预留前端查询结构。 | `StepEntityResolverTest` 补关系解析断言；`StepPreviewJsonExporterTest` 补 PMI 元数据导出断言；人工验证 dump 输出可调试。 | `resolved_only` 到 `partial`：resolver 已有，测试仅确认少量 resolve；preview/export 链路尚未形成。 |
| P3 | 补 validation property 元数据导出 | `StepEntityResolver.resolveValidationPropertyRepresentation()`<br>`resolveCalculatedGeometricRepresentationItem()`<br>必要时补 `VALIDATION_RESULT_REPRESENTATION` resolver/exporter 路径<br>`StepPreviewJsonExporter` 元数据装配 | 把 validation property 从“可解析实体”推进到“可查询的结构化元数据”；优先导出到 dump/preview，不先做复杂求值。 | 新增 resolver/exporter 测试；确认 payload 可稳定包含 validation item 标识、关联对象和原始实体类型。 | `resolved_only`：resolver 已有 `VALIDATION_PROPERTY_REPRESENTATION`、`CALCULATED_GEOMETRIC_REPRESENTATION_ITEM` 入口，但端到端链路未闭合。 |
| P4 | 补 kinematic 结构化导出 | `StepEntityResolver.resolveKinematicPath()`<br>`StepPreviewJsonExporter` 结构化元数据导出<br>相关测试：`StepEntityResolverTest`、`StepDumpAppTest` | 延续当前 resolver 能力，把 `KINEMATIC_PATH` / `FOUNDED_KINEMATIC_PATH` 做成可导出的结构，不急于求解运动学。 | 保持现有 resolve/dump 测试通过，并新增 preview/export 元数据断言。 | `resolved_built_previewed_tested` 的范围有限：当前已有 resolve 和 dump 覆盖，但更偏“表示项存在”，缺结构化导出。 |
| P4 | 补 FEA 基础结构导出 | `StepEntityResolver.resolveElementVolume2d()`<br>后续补 `ELEMENT_VOLUME_3D`、`NODE_SET`、`ELEMENT_SET` 对应 resolver/exporter 路径<br>`StepPreviewJsonExporter` 元数据导出 | 先完成有限元实体的解析和结构化导出，不进入仿真求值；用统一数据结构表示节点集、单元集、体元关联。 | 为 `ELEMENT_VOLUME_2D` 起步补 resolver/exporter 测试，再逐步扩到 3D 和集合实体。 | `resolved_only`：`ELEMENT_VOLUME_2D` 已有 resolver 入口，但未见 builder/exporter/测试闭环。 |

## 4. 推荐推进顺序

1. 先清掉 `StepIndexedPolyCurve` 编译阻塞。
2. 再做支持矩阵和差集报告，校准真实基线。
3. 然后优先修 `EXTRUDED_FACE_SOLID`、`REVOLVED_FACE_SOLID`、`SURFACE_CURVE_SWEPT_FACE_SOLID` 和 advanced volumes。
4. 在主几何链路稳定后推进一般化 boolean 与 half-space 统一接口。
5. 最后补 AP242 PMI / GD&T、validation、kinematic、FEA 的结构化导出。

## 5. 测试要求

每个 issue 至少对齐以下检查层次：

- `StepEntityResolverTest`
- `StepCadBuilderTest`
- `StepPreviewJsonExporterTest`

必要时追加：

- `StepDumpAppTest`
- `StepBenchmarkAppTest`

统一要求：

- 覆盖 happy path 与 failure path
- 覆盖 placement / orientation / trimming / voids / aliases
- 验证 `unsupportedFaceCount` 与 `unsupportedBooleanCount`
- 尽量使用小型 inline STEP 片段
- 对复杂回归场景新增 `examples/` 样例

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
