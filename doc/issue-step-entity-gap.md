# STEP 实体支持差距分析

> 基于 OpenCASCADE OCCT 8.0.0 RC5 文档（`occt_step_support.md`）与 MiniCAD 代码库对比分析
> 分析日期：2026-04-21

---

## 一、总体情况

| 维度 | OCCT | MiniCAD | 差距 |
|---|---|---|---|
| OCCT 文档列出的实体总数 | ~334 | — | — |
| MiniCAD 注册的解析器实体 | — | **1907**（含别名函数） | — |
| MiniCAD 已有的模型类 | — | 1176 | — |
| **OCCT 支持但 MiniCAD 完全缺失** | — | — | **104** |
| **已实现但 OCCT 文档未列出的 MiniCAD 扩展** | — | — | ~417 |

> 注：
> - MiniCAD 通过 `registry.put`（1231 个直接调用）+ 别名注册函数（`registerGeometricToleranceAliases`、`registerShapeAspectAliases`、`registerCharacterizedObjectAliases`、`registerRepresentationAliases`）共注册 1907 个唯一实体名。
> - 很多 "缺失" 实体实际上通过别名函数注册，或使用了与 OCCT 文档不同的 ISO STEP 标准命名（如 OCCT 的 `RoundnessTolerance` 在 STEP 中叫 `CIRCULARITY_TOLERANCE`）。

---

## 二、完全缺失实体（无解析器、无模型类）

### 2.1 StepGeom — 几何（2 个）

| 实体 | OCCT 功能 | 优先级 | 说明 |
|---|---|---|---|
| `PARABOLA` | 抛物线（3D） | 低 | 已注册为 `resolveConicCurve`，`StepConicCurve` 是 parse-only |
| `HYPERBOLA` | 双曲线（3D） | 低 | 同上 |

### 2.2 StepShape — 拓扑/形状（5 个）

| 实体 | OCCT 功能 | 优先级 |
|---|---|---|
| `LOOP` | 环（拓扑基类） | 中 |
| `BOUNDING_BOX` | 包围盒 | 低 |
| `FILL_AREA_SHAPE_USE` | 填充区域形状使用 | 低 |
| `POINT_ON_FACE` | 面上的点 | 低 |
| `TESSELLATED_COORDINATE_SET` | 三角化坐标集 | 低 |

### 2.3 StepRepr — 表示与关系（10 个）

| 实体 | OCCT 功能 | 优先级 |
|---|---|---|
| `SPECIFIC_HIGHER_USAGE_OCCURRENCE` | 特定高层使用关系（SHUO） | 中 |
| `USAGE_OCCURRENCE` | 使用关系（基类） | 中 |
| `SHAPE_REPRESENTATION_TRANSFORMATION` | 形状表示变换 | 中 |
| `REPRESENTATION_CONTEXT_3D` | 3D 表示上下文 | 低 |
| `APPLIED_ATTRIBUTE_CLASSIFICATION` | 应用属性分类 | 低 |
| `ATTRIBUTE_CLASSIFICATION` | 属性分类 | 低 |
| `FEATURE_RELATIONSHIP` | 特征关系 | 低 |
| `PROPERTY_DEFINITION_REPRESENTATION` | 属性定义表示 | 低 |
| `STRUCTURAL_ANALYSIS_REPRESENTATION` | 结构分析表示 | 低 |
| `STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS` | 结构分析表示参数 | 低 |
| `VALUE_REASON_PAIR` | 值-原因对 | 低 |

### 2.4 StepBasic — 基础产品数据（1 个）

| 实体 | OCCT 功能 | 优先级 |
|---|---|---|
| `UNCERTAINTY_MEASURE` | 不确定度度量 | 低 |

### 2.5 StepDimTol — GD&T 几何公差（10 个）

OCCT 在 AP242 模式下完整支持所有公差类型。MiniCAD 已支持大部分具体公差类型（通过 `registerGeometricToleranceAliases`），以下 10 个是真正缺失的：

| 实体 | OCCT 功能 | 优先级 | 备注 |
|---|---|---|---|
| `ROUNDNESS_TOLERANCE` | 圆度公差 | 中 | STEP 标准名为 `CIRCULARITY_TOLERANCE`，**已注册** |
| `PROFILE_OF_LINE_TOLERANCE` | 线轮廓度公差 | 中 | STEP 标准名为 `PROFILE_OF_A_LINE_TOLERANCE`，**已注册** |
| `PROFILE_OF_SURFACE_TOLERANCE` | 面轮廓度公差 | 中 | STEP 标准名为 `PROFILE_OF_A_SURFACE_TOLERANCE`，**已注册** |
| `TOLERANCE_ZONE_DEFINITION` | 公差带定义 | 中 | **已注册** |
| `RUNOUT_ZONE_DEFINITION` | 跳动带定义 | 中 | 有 `RUNOUT_TOLERANCE_ZONE` 已注册 |
| `COMPOSITE_GROUP_TOLERANCE` | 组合组公差 | 低 | — |
| `GEOMETRIC_TOLERANCE_TARGET` | 几何公差目标 | 低 | — |
| `QUALIFIED_REPRESENTATION_ITEM` | 限定表示项 | 低 | — |
| `MODIFIER` | 修饰符 | 低 | — |
| `DATUM_REFERENCE_MODIFIER_WITH_SIGN` | 带符号的基准参考修饰符 | 低 | 已有 `DATUM_REFERENCE_MODIFIER` |
| `DIMENSIONAL_LOCATION_WITH_PATH` | 带路径的尺寸位置 | 低 | 已有 `DIMENSIONAL_LOCATION` |

> **实际上仅有 7 个真正缺失**：`COMPOSITE_GROUP_TOLERANCE`、`GEOMETRIC_TOLERANCE_TARGET`、`QUALIFIED_REPRESENTATION_ITEM`、`MODIFIER`、`DATUM_REFERENCE_MODIFIER_WITH_SIGN`、`DIMENSIONAL_LOCATION_WITH_PATH`、`RUNOUT_ZONE_DEFINITION`。
> 其余 3 个（`ROUNDNESS_TOLERANCE`、`PROFILE_OF_LINE_TOLERANCE`、`PROFILE_OF_SURFACE_TOLERANCE`）在 STEP 中使用不同的标准命名，MiniCAD 已以 `CIRCULARITY_TOLERANCE`、`PROFILE_OF_A_LINE_TOLERANCE`、`PROFILE_OF_A_SURFACE_TOLERANCE` 注册。

### 2.6 StepVisual — 可视化/颜色/图层（16 个）

| 实体 | OCCT 功能 | 优先级 |
|---|---|---|
| `COLOUR_RGB` / `RGB_COLOUR` | RGB 颜色 | 低 |
| `CURVE_STYLE_FILL` | 曲线填充样式 | 低 |
| `ANNOTATION_OCCURRENCE` | 标注出现 | 低 |
| `ANNOTATION_OCCURRENCE_ROLE_ASSOCIATION` | 标注出现角色关联 | 低 |
| `TEXT_LITERAL_WITH_EXTENT` | 带范围文本 | 低 |
| `MARKER_STYLE` | 标记样式 | 低 |
| `IMAGE_FILE` | 图像文件 | 低 |
| `TILE_CURVE` / `TILE_SYMBOL` | 平铺曲线/符号 | 低 |
| `FILL_AREA_STYLE_TILE_CURVE_WITH_STYLE` | 填充区域平铺曲线样式 | 低 |
| `PRESENTATION_LAYER_WITH_STYLE` | 带样式的表现图层 | 低 |
| `PRESENTATION_STYLE_BY_CONTEXT` | 按上下文的表现样式 | 低 |
| `VIEWING_PLANE` | 视图平面 | 低 |
| `VISIBLE_ACTION_REQUEST` | 可见动作请求 | 低 |
| `DRAWING_REVISION` | 图纸修订 | 低 |
| `STYLED_REPRESENTATION_ITEM` | 样式化表示项 | 低 |

> `DRAUGHTING_PRE_DEFINED_COLOUR`、`DRAUGHTING_MODEL` 等 **已注册**。

### 2.7 StepKinematics — 运动学（14 个）

MiniCAD 已有 `PRISMATIC_PAIR`、`REVOLUTE_PAIR`、`CYLINDRICAL_PAIR`、`SPHERICAL_PAIR`、`PLANAR_PAIR`、`UNIVERSAL_PAIR`、`SCREW_PAIR`、`GEAR_PAIR` 等运动对类型注册，以及 `KINEMATIC_PATH`、`KINEMATIC_STRUCTURE` 等。但缺少 OCCT 标准的具体关节（Joint）类型：

| 实体 | OCCT 功能 | 优先级 |
|---|---|---|
| `REVOLUTE_JOINT` | 旋转副 | 中 |
| `PRISMATIC_JOINT` | 移动副 | 中 |
| `SPHERICAL_JOINT` | 球面副 | 中 |
| `CYLINDRICAL_JOINT` | 圆柱副 | 中 |
| `PLANAR_JOINT` | 平面副 | 中 |
| `SCREW_JOINT` | 螺旋副 | 中 |
| `GENERAL_JOINT` | 通用副 | 中 |
| `DIRECTION_SENSE` | 方向感 | 低 |
| `JOINT_VALUE` | 关节值 | 低 |
| `LINK` | 连杆 | 低 |
| `KINEMATIC_CHAIN` | 运动链 | 低 |
| `KINEMATIC_MODEL` | 运动学模型 | 低 |
| `KINEMATIC_PROPERTY` | 运动学属性 | 低 |
| `MOTION_CONSTRAINT` | 运动约束 | 低 |
| `MECHANISM` | 机构 | 低 |

### 2.8 StepFEA — 有限元分析（20 个）

| 实体 | OCCT 功能 | 优先级 |
|---|---|---|
| `NODE` | 节点 | 中 |
| `ELEMENT` | 单元 | 中 |
| `LOAD` | 载荷 | 中 |
| `FEA_MODEL` | 有限元模型 | 中 |
| `MATERIAL` | 材料 | 中 |
| `FEA_LINEAR_MATERIAL` | 线性材料 | 中 |
| `FEA_NON_LINEAR_MATERIAL` | 非线性材料 | 中 |
| `FEA_MASS_DENSITY` | 质量密度 | 低 |
| `FEA_YIELD_STRESS` | 屈服应力 | 低 |
| `FEA_ULTIMATE_STRESS` | 极限应力 | 低 |
| `DISPLACEMENT_BOUNDARY_CONDITION` | 位移边界条件 | 中 |
| `VELOCITY_BOUNDARY_CONDITION` | 速度边界条件 | 低 |
| `ACCELERATION_BOUNDARY_CONDITION` | 加速度边界条件 | 低 |
| `FORCE_BOUNDARY_CONDITION` | 力边界条件 | 中 |
| `PRESSURE_BOUNDARY_CONDITION` | 压力边界条件 | 中 |
| `THERMAL_BOUNDARY_CONDITION` | 热边界条件 | 低 |
| `STRESS_ANALYSIS` | 应力分析 | 低 |
| `BUCKLING_ANALYSIS` | 屈曲分析 | 低 |
| `MODAL_ANALYSIS` | 模态分析 | 低 |
| `THERMAL_ANALYSIS` | 热分析 | 低 |
| `STRUCTURAL_ANALYSIS_MODEL` | 结构分析模型 | 中 |

### 2.9 StepElement — 有限元单元（10 个）

| 实体 | OCCT 功能 | 优先级 |
|---|---|---|
| `ELEMENT_VOLUME` | 单元体积 | 中 |
| `VOLUME_ELEMENT` | 体单元 | 中 |
| `SURFACE_ELEMENT` | 面单元 | 中 |
| `LINE_ELEMENT` | 线单元 | 中 |
| `MASS_ELEMENT` | 质量单元 | 低 |
| `CONNECTIVITY_ELEMENT` | 连接单元 | 低 |
| `ELEMENT_GEOMETRIC_DESCRIPTION` | 单元几何描述 | 低 |
| `UNIFORM_SURFACE_ELEMENT` | 均匀面单元 | 低 |
| `UNIFORM_VOLUME_ELEMENT` | 均匀体单元 | 低 |
| `NODE_REPRESENTATION` | 节点表示 | 低 |

### 2.10 StepAP203 — AP203 专属（5 个）

| 实体 | OCCT 功能 | 优先级 |
|---|---|---|
| `CHANGE` | 变更 | 中 |
| `CHANGE_REQUEST` | 变更请求 | 中 |
| `START_REQUEST` | 启动请求 | 低 |
| `START_WORK` | 启动工作 | 低 |
| `WORK_ITEM` | 工作项 | 低 |

> `CC_DESIGN_APPROVAL`、`CC_DESIGN_CERTIFICATION`、`CC_DESIGN_CONTRACT`、`SECURITY_CLASSIFICATION` 等 **已注册**。

### 2.11 StepAP214 — AP214 专属（7 个）

| 实体 | OCCT 功能 | 优先级 |
|---|---|---|
| `DATE_AND_TIME_ASSIGNMENT` | 日期时间分配 | 低 |
| `AUTO_DESIGN_APPROVAL_ASSIGNMENT` | 自动设计审批分配 | 低 |
| `AUTO_DESIGN_DATE_ASSIGNMENT` | 自动设计日期分配 | 低 |
| `AUTO_DESIGN_GROUP_ASSIGNMENT` | 自动设计分组分配 | 低 |
| `AUTO_DESIGN_ORGANIZATION_ASSIGNMENT` | 自动设计组织分配 | 低 |
| `AUTO_DESIGN_SECURITY_CLASSIFICATION_ASSIGNMENT` | 自动设计安全分类分配 | 低 |
| `REQUIREMENT_SOURCE` | 需求来源 | 低 |

> `PERSON_AND_ORGANIZATION_ASSIGNMENT`、`SECURITY_CLASSIFICATION_ASSIGNMENT`、`APPLIED_APPROVAL_ASSIGNMENT`、`PRESENTATION_VIEW`、`DRAWING_SHEET_REVISION`、`EXTERNALLY_DEFINED_CLASS`、`EXTERNALLY_DEFINED_GENERAL_PROPERTY`、`GENERAL_PROPERTY_DEFINITION`、`REQUIREMENT_ASSIGNMENT` 等 **已注册**。

### 2.12 StepAP242 — AP242 专属（0 个）

> `DRAUGHTING_MODEL_ITEM_ASSOCIATION`、`GEOMETRIC_ITEM_SPECIFIC_USAGE`、`ITEM_IDENTIFIED_REPRESENTATION_USAGE` **均已注册**，并有完整的解析和验证逻辑。AP242 PMI 标注关联在解析层已覆盖。

### 2.13 StepAP209 — AP209 专属（1 个）

| 实体 | OCCT 功能 | 优先级 |
|---|---|---|
| `STRUCT_ANALYSIS_MODEL` | 结构分析模型 | 低 |

### 2.14 HeaderSection — 文件头

| 实体 | OCCT 功能 | 优先级 |
|---|---|---|
| `FileDescription` | 文件描述（协议、实现级别） | 中 |
| `FileName` | 文件名（作者、组织、时间戳） | 中 |
| `FileSchema` | 文件模式声明 | 中 |

---

## 三、已注册但缺少独立模型类的实体

MiniCAD 有约 587 个已注册实体没有对应的独立 `StepXxx.java` 模型类。这些实体通过以下方式之一处理：

1. **映射到父类型**：如 `EXTRUDED_AREA_SOLID` → `StepSweptAreaSolid`，`RECTANGLE_PROFILE_DEF` → `StepProfileDef`
2. **内联解析**：直接在 resolver 方法中处理，不创建专用模型类
3. **泛型解析**：通过通用解析逻辑处理

这部分不影响实际功能使用，仅影响代码的可读性和类型安全性。以下列出部分值得关注的项目：

| 实体 | 当前处理方式 | 建议 |
|---|---|---|
| `EXTRUDED_AREA_SOLID` | → `StepSweptAreaSolid` | 功能完整，可不改 |
| `REVOLVED_AREA_SOLID` | → `StepSweptAreaSolid` | 功能完整，可不改 |
| `RECTANGLE_PROFILE_DEF` | → `StepProfileDef` | 功能完整，可不改 |
| `CIRCLE_PROFILE_DEF` | → `StepProfileDef` | 功能完整，可不改 |
| `BOXED_HALF_SPACE` | → `StepHalfSpaceSolid` | 功能完整，可不改 |
| `FEA_2D_ELEMENT_PROPERTY` | 内联解析 | 考虑独立模型类 |
| `FEA_3D_ELEMENT_PROPERTY` | 内联解析 | 考虑独立模型类 |
| `CARTESIAN_TRANSFORMATION_OPERATOR_2D` | 内联解析 | 考虑独立模型类 |
| `CARTESIAN_TRANSFORMATION_OPERATOR_3D` | 内联解析 | 考虑独立模型类 |

---

## 四、建议优先级

### P0 — 高优先级

1. **GD&T 具体公差类型**（22 个）— 工业 CAD 核心需求，OCCT 完整支持，MiniCAD 目前仅通过通用 `GEOMETRIC_TOLERANCE` 处理，缺少类型化公差解析能力

### P1 — 中优先级

2. **FEA 基础实体**（NODE, ELEMENT, LOAD, MATERIAL, FEA_MODEL 等 ~20 个）— 有限元分析场景需要
3. **StepElement 单元类型**（10 个）— FEA 配套
4. **运动学关节类型**（7 种 Joint + 辅助实体 ~14 个）— 运动学分析场景
5. **HeaderSection**（FileDescription, FileName, FileSchema）— STEP 文件头信息提取，改善用户体验
6. **AP203 变更管理**（CHANGE, CHANGE_REQUEST 等 5 个）— 工程数据管理

### P2 — 低优先级

7. **Visual/样式实体**（16 个）— 当前渲染管线不依赖 STEP 样式
8. **StepRepr 高级实体**（10 个）— 特定场景需求
9. **StepAP214 AutoDesign 分配实体**（7 个）— 行业特定
10. **StepShape 辅助实体**（5 个）— 边缘场景
11. **`PARABOLA`/`HYPERBOLA`（3D）**（2 个）— 已有 2D 版本和抛物面/双曲面支持

---

## 五、OCCT 形状表示类型支持对比

| STEP Shape 表示类型 | OCCT | MiniCAD | 说明 |
|---|---|---|---|
| `advanced_brep_shape_representation` | ✅ | ⚠️ | `ADVANCED_BREP` 已注册 |
| `faceted_brep_shape_representation` | ✅ | ⚠️ | `FACETTED_BREP` 已注册，`FACETTED_BREP_AND_BREP_WITH_VOIDS` 缺失 |
| `manifold_surface_shape_representation` | ✅ | ⚠️ | `MANIFOLD_SURFACE_MODEL` 已注册 |
| `geometrically_bounded_wireframe_shape_representation` | ✅ | ⚠️ | 线框模型部分支持 |
| `geometrically_bounded_surface_shape_representation` | ✅ | ⚠️ | 曲面模型部分支持 |
| `hybrid representations` | ✅ | ❌ | 混合表示未显式支持 |
| `tessellated_shape_representation` | ✅ | ⚠️ | `TESSELLATED_FACE`/`TESSELLATED_FACE_SET` 已注册 |

---

## 六、总结

MiniCAD 在 STEP 实体解析覆盖方面已有**非常广泛**的基础（1907 个注册项，含别名函数），核心几何/拓扑/装配实体已基本覆盖。实际差距远小于初版分析：

1. **GD&T 公差**：大部分具体公差类型已注册（通过 `registerGeometricToleranceAliases`），仅 7 个真正缺失
2. **FEA/Element**（30 个实体）— 解析层缺失，不影响核心 B-Rep 能力
3. **运动学 Joint 类型**（14 个实体）— 与现有 Pair 类型正交，标准命名不同
4. **Visual/样式**（16 个实体）— 渲染管线不依赖
5. **StepRepr 高级实体**（10 个实体）— 特定场景需求
6. **HeaderSection**（3 个实体）— 文件头信息未独立建模
7. **AP203/AP214 变更管理**（12 个实体）— 工程数据管理场景

AP242 PMI 标注关联的 3 个关键实体（`DRAUGHTING_MODEL_ITEM_ASSOCIATION`、`GEOMETRIC_ITEM_SPECIFIC_USAGE`、`ITEM_IDENTIFIED_REPRESENTATION_USAGE`）**已完整支持**。

---

## 七、实现任务清单

以下为本次差距分析确定的需实现实体，按优先级排列：

### Phase 1: GD&T 补充（7 个）

- [ ] `COMPOSITE_GROUP_TOLERANCE` — 组合组公差
- [ ] `GEOMETRIC_TOLERANCE_TARGET` — 几何公差目标
- [ ] `QUALIFIED_REPRESENTATION_ITEM` — 限定表示项
- [ ] `MODIFIER` — 修饰符
- [ ] `DATUM_REFERENCE_MODIFIER_WITH_SIGN` — 带符号的基准参考修饰符
- [ ] `DIMENSIONAL_LOCATION_WITH_PATH` — 带路径的尺寸位置
- [ ] `RUNOUT_ZONE_DEFINITION` — 跳动带定义

### Phase 2: FEA 基础实体（20 个）

- [ ] `NODE` — 节点
- [ ] `ELEMENT` — 单元
- [ ] `LOAD` — 载荷
- [ ] `FEA_MODEL` — 有限元模型
- [ ] `MATERIAL` — 材料
- [ ] `FEA_LINEAR_MATERIAL` — 线性材料
- [ ] `FEA_NON_LINEAR_MATERIAL` — 非线性材料
- [ ] `FEA_MASS_DENSITY` — 质量密度
- [ ] `FEA_YIELD_STRESS` — 屈服应力
- [ ] `FEA_ULTIMATE_STRESS` — 极限应力
- [ ] `DISPLACEMENT_BOUNDARY_CONDITION` — 位移边界条件
- [ ] `VELOCITY_BOUNDARY_CONDITION` — 速度边界条件
- [ ] `ACCELERATION_BOUNDARY_CONDITION` — 加速度边界条件
- [ ] `FORCE_BOUNDARY_CONDITION` — 力边界条件
- [ ] `PRESSURE_BOUNDARY_CONDITION` — 压力边界条件
- [ ] `THERMAL_BOUNDARY_CONDITION` — 热边界条件
- [ ] `STRESS_ANALYSIS` — 应力分析
- [ ] `BUCKLING_ANALYSIS` — 屈曲分析
- [ ] `MODAL_ANALYSIS` — 模态分析
- [ ] `THERMAL_ANALYSIS` — 热分析
- [ ] `STRUCTURAL_ANALYSIS_MODEL` — 结构分析模型

### Phase 3: StepElement 单元类型（10 个）

- [ ] `ELEMENT_VOLUME` — 单元体积
- [ ] `VOLUME_ELEMENT` — 体单元
- [ ] `SURFACE_ELEMENT` — 面单元
- [ ] `LINE_ELEMENT` — 线单元
- [ ] `MASS_ELEMENT` — 质量单元
- [ ] `CONNECTIVITY_ELEMENT` — 连接单元
- [ ] `ELEMENT_GEOMETRIC_DESCRIPTION` — 单元几何描述
- [ ] `UNIFORM_SURFACE_ELEMENT` — 均匀面单元
- [ ] `UNIFORM_VOLUME_ELEMENT` — 均匀体单元
- [ ] `NODE_REPRESENTATION` — 节点表示

### Phase 4: 运动学关节类型（14 个）

- [ ] `REVOLUTE_JOINT` — 旋转副
- [ ] `PRISMATIC_JOINT` — 移动副
- [ ] `SPHERICAL_JOINT` — 球面副
- [ ] `CYLINDRICAL_JOINT` — 圆柱副
- [ ] `PLANAR_JOINT` — 平面副
- [ ] `SCREW_JOINT` — 螺旋副
- [ ] `GENERAL_JOINT` — 通用副
- [ ] `DIRECTION_SENSE` — 方向感
- [ ] `JOINT_VALUE` — 关节值
- [ ] `LINK` — 连杆
- [ ] `KINEMATIC_CHAIN` — 运动链
- [ ] `KINEMATIC_MODEL` — 运动学模型
- [ ] `KINEMATIC_PROPERTY` — 运动学属性
- [ ] `MOTION_CONSTRAINT` — 运动约束
- [ ] `MECHANISM` — 机构

### Phase 5: HeaderSection（3 个）

- [ ] `FileDescription` — 文件描述
- [ ] `FileName` — 文件名
- [ ] `FileSchema` — 文件模式声明

### Phase 6: AP203 变更管理（5 个）

- [ ] `CHANGE` — 变更
- [ ] `CHANGE_REQUEST` — 变更请求
- [ ] `START_REQUEST` — 启动请求
- [ ] `START_WORK` — 启动工作
- [ ] `WORK_ITEM` — 工作项

### Phase 7: StepRepr 高级实体（10 个）

- [ ] `SPECIFIC_HIGHER_USAGE_OCCURRENCE`
- [ ] `USAGE_OCCURRENCE`
- [ ] `SHAPE_REPRESENTATION_TRANSFORMATION`
- [ ] `REPRESENTATION_CONTEXT_3D`
- [ ] `APPLIED_ATTRIBUTE_CLASSIFICATION`
- [ ] `ATTRIBUTE_CLASSIFICATION`
- [ ] `FEATURE_RELATIONSHIP`
- [ ] `PROPERTY_DEFINITION_REPRESENTATION`
- [ ] `STRUCTURAL_ANALYSIS_REPRESENTATION`
- [ ] `STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS`
- [ ] `VALUE_REASON_PAIR`

### Phase 8: Visual/样式（16 个）

- [ ] `COLOUR_RGB` / `RGB_COLOUR`
- [ ] `CURVE_STYLE_FILL`
- [ ] `ANNOTATION_OCCURRENCE`
- [ ] `ANNOTATION_OCCURRENCE_ROLE_ASSOCIATION`
- [ ] `TEXT_LITERAL_WITH_EXTENT`
- [ ] `MARKER_STYLE`
- [ ] `IMAGE_FILE`
- [ ] `TILE_CURVE` / `TILE_SYMBOL`
- [ ] `FILL_AREA_STYLE_TILE_CURVE_WITH_STYLE`
- [ ] `PRESENTATION_LAYER_WITH_STYLE`
- [ ] `PRESENTATION_STYLE_BY_CONTEXT`
- [ ] `VIEWING_PLANE`
- [ ] `VISIBLE_ACTION_REQUEST`
- [ ] `DRAWING_REVISION`
- [ ] `STYLED_REPRESENTATION_ITEM`

### Phase 9: StepShape 辅助实体（5 个）

- [ ] `LOOP`
- [ ] `BOUNDING_BOX`
- [ ] `FILL_AREA_SHAPE_USE`
- [ ] `POINT_ON_FACE`
- [ ] `TESSELLATED_COORDINATE_SET`

### Phase 10: StepBasic（1 个）

- [ ] `UNCERTAINTY_MEASURE`

### Phase 11: StepAP214 AutoDesign（7 个）

- [ ] `DATE_AND_TIME_ASSIGNMENT`
- [ ] `AUTO_DESIGN_APPROVAL_ASSIGNMENT`
- [ ] `AUTO_DESIGN_DATE_ASSIGNMENT`
- [ ] `AUTO_DESIGN_GROUP_ASSIGNMENT`
- [ ] `AUTO_DESIGN_ORGANIZATION_ASSIGNMENT`
- [ ] `AUTO_DESIGN_SECURITY_CLASSIFICATION_ASSIGNMENT`
- [ ] `REQUIREMENT_SOURCE`

### Phase 12: AP209（1 个）

- [ ] `STRUCT_ANALYSIS_MODEL`

### Phase 13: 3D 圆锥曲线（2 个）

- [ ] `PARABOLA` — 抛物线（3D）
- [ ] `HYPERBOLA` — 双曲线（3D）

**总计：101 个实体需实现**
