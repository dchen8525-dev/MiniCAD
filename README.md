# MiniCAD

实验性的 Java CAD 内核与 STEP 文件解析器，使用 Java 11 实现。

## 项目定位

MiniCAD 目前是面向 STEP (ISO 10303) 子集的实验性 CAD 内核。项目重点验证从 STEP 文本到语法 AST、语义模型、几何/拓扑构建和 Web 预览的端到端链路，但不宣称完整兼容 AP214/AP242。

当前目标是：

- **STEP 子集解析**: 覆盖常见机械 CAD 文件中的核心语法、HEADER、DATA 实体和部分 AP214/AP242 实体。
- **B-Rep 几何实验内核**: 支持常见边界表示、曲线、曲面和装配变换路径，但仍需要更多拓扑和几何正确性验证。
- **可视化预览**: 提供基于 Web 的三维模型预览能力，并显式报告无法构建或无法预览的内容。
- **真实兼容性推进**: 通过 schema 覆盖报告、示例回归和后续真实 CAD 文件语料库逐步验证 FreeCAD、OpenCascade、SolidWorks、Creo、NX、CATIA 等来源文件。

当前能力统计以生成文件为准：

- [STEP capability report](doc/generated/coverage.md) 区分 model、registered、builder、exporter、tested。
- 该报告是能力信号，不是几何正确性或规范完整性的证明。
- AP214/AP242 的规范级覆盖仍在建设中，跟踪见 [STEP AP214/AP242 support workflow](doc/step-ap214-ap242-workflow.md)。
- AP214/AP242 的第一批核心实体和验收标准见 [core entity priorities](doc/ap214-ap242-core-entity-priorities.md)。

## 包说明

| 包路径 | 文件数 | 说明 |
|--------|--------|------|
| `com.minicad.common` | 8 | 公共异常类和工具类（`Epsilon`, `Preconditions`, `GeometryException`, `StepParseException`, `StepResolutionException`, `TopologyException`, `UnsupportedGeometryException`, `UnsupportedStepEntityException`） |
| `com.minicad.geometry` | 37 | 3D 几何类型：`Curve3` (13 种子类型)、`SurfaceGeometry` (16 种子类型) 及辅助类型 |
| `com.minicad.geometry2d` | 16 | 2D 参数域几何类型（曲线、包围盒等） |
| `com.minicad.topology` | 11 | B-Rep 拓扑类型（Vertex、Edge、Face、Shell、Solid 等） |
| `com.minicad.step.syntax` | 5 | STEP 语法解析器（Tokenizer、Parser、AST 模型） |
| `com.minicad.step.semantic` | 6 | STEP 语义解析器（EntityResolver、EntityFactory、CadBuilder 等） |
| `com.minicad.step.model` | 1264 | STEP 实体模型类，分 26 个子包（见下表） |
| `com.minicad.app` | 13 | 应用入口：CLI 解析器、Web 预览器、JSON 导出器、装配图构建器等 |

### `step.model` 子包明细

| 子包 | 文件数 | 说明 |
|------|--------|------|
| `workflow` | 199 | 工作流与流程管理实体 |
| `geometry` | 115 | 几何表示实体（曲线、曲面、变换、坐标系等） |
| `product` | 107 | 产品定义与装配结构 |
| `annotation` | 117 | 标注与 PMI（Product Manufacturing Information） |
| `manufacturing` | 117 | 制造工艺与加工特征 |
| `resource` | 67 | 资源管理实体 |
| `validation` | 50 | 验证与检查结果表示 |
| `action` | 49 | 动作与操作链定义 |
| `classification` | 28 | 分类与编码体系 |
| `config_mgmt` | 28 | 配置管理与版本控制 |
| `tolerance` | 32 | GD&T 几何尺寸与公差 |
| `fea` | 24 | 有限元分析实体 |
| `security` | 22 | 安全分类与访问控制 |
| `log_audit` | 23 | 日志与审计追踪 |
| `document` | 21 | 文档与文件引用 |
| `organization` | 19 | 组织与人员信息 |
| `unit` | 19 | 单位定义与转换 |
| `kinematic` | 26 | 运动学副与机构状态 |
| `approval` | 13 | 审批流程与状态 |
| `backup_recovery` | 14 | 备份与恢复管理 |
| `analysis` | 15 | 分析属性与结果 |
| `date_time` | 11 | 日期与时间表示 |
| `system` | 10 | 系统级通用实体 |
| `profile` | 7 | 截面轮廓定义 |
| `topology` | 31 | STEP 拓扑实体映射 |
| `base` | 11 | 基础抽象（`StepRepresentationItem`、`StepGeometricRepresentationItem` 等） |

## STEP 支持范围

### 国际标准对比

以下数据来自当前 `HEAD` 的生成报告，不把 `registry.put()` 次数或静态类数量直接等同于完整规范兼容。

| 覆盖口径 | Schema 实体数 | Model | Registered | Builder | Exporter | Tested | Full signal | 说明 |
|----------|--------------:|------:|-----------:|--------:|---------:|-------:|------------:|------|
| **AP214 Curated** | 64 | 59 | 64 | 59 | 59 | 51 | 51 | 基于仓库 AP214/automotive_design 示例抽取的 curated baseline，不是完整 AP214 schema |
| **AP242 Ed2** | 2122 | 445 | 1550 | 434 | 334 | 441 | 301 | 基于 `schemas/ap242ed2_dis2_mim_lf_v1.101.exp` 的 schema 覆盖扫描 |
| **All scanned entities** | 2651 | 1264 | 2357 | 752 | 425 | 496 | - | 见 [doc/generated/MINI_CAD_CAPABILITY_REPORT.md](doc/generated/MINI_CAD_CAPABILITY_REPORT.md) |

覆盖等级：

- `L0`: 有模型类信号
- `L1`: 已注册/可语义解析
- `L2`: builder 路径引用
- `L3`: exporter 或 preview 路径引用
- `L4`: 测试引用
- `L5`: 预留给真实 CAD 语料验证

> **注意**：这些报告是能力信号，不是几何正确性或规范完整性的证明。完整数据见 [AP214 coverage](doc/generated/ap214-coverage.md) 和 [AP242 coverage](doc/generated/ap242-coverage.md)。

### 已支持的实体类型（按领域）

<details>
<summary><b>3D 几何/拓扑 — 已支持（65 种核心实体）</b></summary>

| 标准实体名 | 支持状态 |
|-----------|---------|
| `ADVANCED_FACE` | ✅ 已接入（需持续验证） |
| `AXIS2_PLACEMENT_2D` | ✅ 已接入（需持续验证） |
| `AXIS2_PLACEMENT_3D` | ✅ 已接入（需持续验证） |
| `BREP_WITH_VOIDS` | ✅ 已接入（需持续验证） |
| `B_SPLINE_CURVE` | ✅ 已接入（需持续验证） |
| `B_SPLINE_CURVE_WITH_KNOTS` | ✅ 已接入（需持续验证） |
| `B_SPLINE_SURFACE` | ✅ 已接入（需持续验证） |
| `B_SPLINE_SURFACE_WITH_KNOTS` | ✅ 已接入（需持续验证） |
| `CARTESIAN_POINT` | ✅ 已接入（需持续验证） |
| `CIRCLE` | ✅ 已接入（需持续验证） |
| `CLOSED_SHELL` | ✅ 已接入（需持续验证） |
| `CLOTHOID` | ✅ 已接入（需持续验证） |
| `COMPOSITE_CURVE` | ✅ 已接入（需持续验证） |
| `COMPOSITE_CURVE_ON_SURFACE` | ✅ 已接入（需持续验证） |
| `COMPOSITE_CURVE_SEGMENT` | ✅ 已接入（需持续验证） |
| `CONICAL_SURFACE` | ✅ 已接入（需持续验证） |
| `CYLINDRICAL_SURFACE` | ✅ 已接入（需持续验证） |
| `DIRECTION` | ✅ 已接入（需持续验证） |
| `EDGE_CURVE` | ✅ 已接入（需持续验证） |
| `EDGE_LOOP` | ✅ 已接入（需持续验证） |
| `ELLIPSE` | ✅ 已接入（需持续验证） |
| `FACETED_BREP` | ✅ 已接入（需持续验证） |
| `FACE_BOUND` | ✅ 已接入（需持续验证） |
| `HYPERBOLA` | ✅ 已接入（需持续验证） |
| `LINE` | ✅ 已接入（需持续验证） |
| `MANIFOLD_SOLID_BREP` | ✅ 已接入（需持续验证） |
| `OFFSET_CURVE_2D` | ✅ 已接入（需持续验证） |
| `OFFSET_CURVE_3D` | ✅ 已接入（需持续验证） |
| `OFFSET_SURFACE` | ✅ 已接入（需持续验证） |
| `OPEN_SHELL` | ✅ 已接入（需持续验证） |
| `ORIENTED_CLOSED_SHELL` | ✅ 已接入（需持续验证） |
| `ORIENTED_EDGE` | ✅ 已接入（需持续验证） |
| `ORIENTED_OPEN_SHELL` | ✅ 已接入（需持续验证） |
| `PARABOLA` | ✅ 已接入（需持续验证） |
| `PCURVE` | ✅ 已接入（需持续验证） |
| `PLANE` | ✅ 已接入（需持续验证） |
| `POLYLINE` | ✅ 已接入（需持续验证） |
| `RATIONAL_B_SPLINE_CURVE` | ✅ 已接入（需持续验证） |
| `RATIONAL_B_SPLINE_SURFACE` | ✅ 已接入（需持续验证） |
| `SEAM_CURVE` | ✅ 已接入（需持续验证） |
| `SPHERICAL_SURFACE` | ✅ 已接入（需持续验证） |
| `SURFACE_CURVE` | ✅ 已接入（需持续验证） |
| `SURFACE_CURVE_SWEPT_AREA_SOLID` | ✅ 部分支持（B-Rep 生成） |
| `SURFACE_OF_LINEAR_EXTRUSION` | ✅ 已接入（需持续验证） |
| `SURFACE_OF_REVOLUTION` | ✅ 已接入（需持续验证） |
| `TOROIDAL_SURFACE` | ✅ 已接入（需持续验证） |
| `TRIMMED_CURVE` | ✅ 已接入（需持续验证） |
| `VECTOR` | ✅ 已接入（需持续验证） |
| `VERTEX_POINT` | ✅ 已接入（需持续验证） |
</details>

<details>
<summary><b>CSG/Swept Solid/Tessellated — 已支持（20 种）</b></summary>

| 标准实体名 | 支持状态 |
|-----------|---------|
| `BOOLEAN_RESULT` | ✅ 半空间裁剪 |
| `BLOCK` | ✅ 已接入（需持续验证） |
| `SPHERE` | ✅ 已接入（需持续验证） |
| `ELLIPSOID` | ✅ 已接入（需持续验证） |
| `TORUS` | ✅ 已接入（需持续验证） |
| `EXTRUDED_AREA_SOLID` | ✅ 已接入（需持续验证） |
| `REVOLVED_AREA_SOLID` | ✅ 已接入（需持续验证） |
| `HALF_SPACE_SOLID` | ✅ 裁剪支持 |
| `BOXED_HALF_SPACE` | ✅ 裁剪支持 |
| `TESSELLATED_FACE_SET` | ✅ 三角网格 B-Rep |
| `TESSELLATED_FACE` | ✅ 三角网格 B-Rep |
| `TRIANGULATED_FACE` | ✅ 三角网格 B-Rep |
</details>

<details>
<summary><b>Product/Assembly — 已支持（12 种）</b></summary>

| 标准实体名 | 支持状态 |
|-----------|---------|
| `PRODUCT` | ✅ 已接入（需持续验证） |
| `PRODUCT_DEFINITION` | ✅ 已接入（需持续验证） |
| `PRODUCT_DEFINITION_FORMATION` | ✅ 已接入（需持续验证） |
| `PRODUCT_DEFINITION_SHAPE` | ✅ 已接入（需持续验证） |
| `NEXT_ASSEMBLY_USAGE_OCCURRENCE` | ✅ 已接入（需持续验证） |
| `CONTEXT_DEPENDENT_SHAPE_REPRESENTATION` | ✅ 已接入（需持续验证） |
| `APPLICATION_CONTEXT` | ✅ 已接入（需持续验证） |
| `REPRESENTATION` | ✅ 已接入（需持续验证） |
| `SHAPE_REPRESENTATION` | ✅ 已接入（需持续验证） |
| `REPRESENTATION_MAP` | ✅ 已接入（需持续验证） |
| `MAPPED_ITEM` | ✅ 已接入（需持续验证） |
| `ITEM_DEFINED_TRANSFORMATION` | ✅ 已接入（需持续验证） |
</details>

<details>
<summary><b>PMI/Annotation — 已支持（10 种）</b></summary>

| 标准实体名 | 支持状态 |
|-----------|---------|
| `ANNOTATION_FILL_AREA` | ✅ 预览支持 |
| `ANNOTATION_PLANE` | ✅ 已接入（需持续验证） |
| `DRAUGHTING_CALLOUT` | ✅ 已接入（需持续验证） |
| `DRAUGHTING_PRE_DEFINED_COLOUR` | ✅ 已接入（需持续验证） |
| `COLOUR_RGB` | ✅ 已接入（需持续验证） |
| `STYLED_ITEM` | ✅ 已接入（需持续验证） |
| `PRESENTATION_STYLE_ASSIGNMENT` | ✅ 已接入（需持续验证） |
| `PRESENTATION_LAYER_ASSIGNMENT` | ✅ 已接入（需持续验证） |
| `TERMINATOR_SYMBOL` | ✅ 预览支持 |
| `MEASURE_REPRESENTATION_ITEM` | ✅ 已接入（需持续验证） |
</details>

<details>
<summary><b>GD&T 公差 — 已支持（8 种）</b></summary>

| 标准实体名 | 支持状态 |
|-----------|---------|
| `GEOMETRIC_TOLERANCE` | ✅ 预览支持（含变体） |
| `DATUM_REFERENCE_MODIFIER` | ✅ 已接入（需持续验证） |
| `RUNOUT_ZONE_DEFINITION_ORIENTATION` | ✅ 已接入（需持续验证） |
| `FLATNESS_TOLERANCE` | ✅ 已接入（需持续验证） |
| `CYLINDRICITY_TOLERANCE` | ✅ 已接入（需持续验证） |
| `CIRCULAR_RUNOUT_TOLERANCE` | ✅ 已接入（需持续验证） |
| `PERPENDICULARITY_TOLERANCE` | ✅ 已接入（需持续验证） |
| `POSITION_TOLERANCE` | ✅ 已接入（需持续验证） |
</details>

### 未支持的 AP242 Ed2 实体（按领域分类，1387 种）

<details>
<summary><b>3D 几何/拓扑 — 未支持（211 种）</b></summary>

主要包括：
- B-Spline 变体：`B_SPLINE_SURFACE_PATCH`, `B_SPLINE_SURFACE_STRIP`, `B_SPLINE_BASIS`, `B_SPLINE_FUNCTION`
- 边界曲线：`BOUNDARY_CURVE_OF_B_SPLINE_OR_RECTANGULAR_COMPOSITE_SURFACE`, `BOUNDED_PCURVE`, `BOUNDED_SURFACE_CURVE`
- 曲线变体：`B_SPLINE_CURVE_SEGMENT`, `REPARAMETRISED_B_SPLINE_CURVE_SEGMENT`, `INDEXED_POLY_CURVE`
- 曲面变体：`TWO_DIRECTIONAL_DERIVATIVE_SURFACE`, `TANGENTIAL_SURFACE`, `RECTANGULAR_COMPOSITE_SURFACE`, `BLENDED_SURFACE`
- 扫掠体：`GENERALIZED_SWEEP`, `SWEEPING_SURFACE`, `SWEEPING_SOLID`
- Voxel/体素：`BEZIER_VOLUME`
- 退化/奇异：`DEGENERATE_BSPLINE_CURVE`, `DEGENERATE_BSPLINE_SURFACE`
- 其他：`SURFACE_STYLE_FILL_AREA`, `FILL_AREA_STYLE_COLOUR`, `ANNOTATION_FILL_AREA_OCCURRENCE`
</details>

<details>
<summary><b>Product/Assembly — 未支持（98 种）</b></summary>

主要包括：
- 产品变体：`PRODUCT_DEFINITION_FOR_ASSOCIATIVE_DRAFTING`, `PRODUCT_RELATED_PRODUCT_CATEGORY`
- 装配：`ALL_PRODUCT_CONTEXT`, `PRODUCT_DEFINITION_SUBSTITUTION`
- 配置：`EFFECTIVITY`, `DATE_EFFECTIVITY`, `LOT_EFFECTIVITY`, `SERIAL_EFFECTIVITY`
- 溯源：`TRACEABILITY`
</details>

<details>
<summary><b>Kinematic 运动学 — 未支持（41 种）</b></summary>

主要包括：
- 运动副：`PLANAR_PAIR`, `SPHERICAL_PAIR`, `CYLINDRICAL_PAIR`, `SCREW_PAIR` 等
- 机构：`KINEMATIC_MODEL`, `KINEMATIC_JOINT`, `KINEMATIC_PAIR_INSTANCE`
- 运动学路径：`KINEMATIC_LINK_PATH`, `KINEMATIC_MECHANISM`
</details>

<details>
<summary><b>FEA 有限元 — 未支持（62 种）</b></summary>

主要包括：
- 单元：`FINITE_ELEMENT_GEOMETRIC_REAL`, `FINITE_ELEMENT_MODEL`, `FEA_MESH`
- 结果：`FEA_VOLUME_RESULT`, `FEA_NODAL_RESULT`
- 材料：`FEA_MATERIAL`, `FEA_MATERIAL_PROPERTY`
- 载荷/约束：`FEA_LOAD`, `FEA_BOUNDARY_CONDITION`
</details>

<details>
<summary><b>Tolerance/GD&T — 未支持（55 种）</b></summary>

主要包括：
- 公差变体：`TOLERANCE_VALUE`, `GEOMETRIC_TOLERANCE_WITH_DEFINED_UNIT`
- 基准：`DATUM_REFERENCE`, `DATUM_TARGET`, `DATUM_FEATURE_CALLOUT`
- 修饰符：`MODIFIED_GEOMETRIC_TOLERANCE`, `MAXIMUM_MATERIAL_REQUIREMENT`
</details>

<details>
<summary><b>Presentation/Style — 未支持（47 种）</b></summary>

主要包括：
- 文本样式：`TEXT_STYLE_WITH_BOX_CHARACTERISTICS`, `TEXT_STYLE_FONT_MODEL`
- 表面纹理：`SURFACE_TEXTURE`, `SURFACE_CONDITION`, `MACHINING_ALLOWED_VALUE`
- 颜色/图层：`FILL_AREA_STYLE`, `CURVE_STYLE`
</details>

<details>
<summary><b>A3M 等效性检测 — 未支持（59 种）</b></summary>

AP242 Ed2 新增的 3D 模型等效性检测实体（`A3M_*`, `A3MA_*`, `A3MS_*` 系列），用于模型比较和验证。
</details>

<details>
<summary><b>其他未支持领域汇总</b></summary>

| 领域 | 未支持数量 | 说明 |
|------|-----------|------|
| Unit/Measure | 26 | 各类单位定义（`VOLUME_UNIT`, `MASS_UNIT` 等） |
| Configuration | 18 | 配置管理（`CONFIGURATION_EFFECTIVITY` 等） |
| Date/Time | 12 | 日期时间实体 |
| Organization/Person | 10 | 组织人员管理 |
| Approval/Contract | 7 | 审批合同 |
| Profile | 14 | 截面轮廓定义 |
| Manufacturing | 5 | 制造工艺实体 |
| Workflow | 5 | 工作流实体 |
| Representation/Context | 6 | 表示上下文 |
| Resource | 8 | 资源管理 |
| Action | 18 | 动作实体 |
| Analysis | 4 | 分析属性 |
| Validation | 3 | 验证结果 |
| Document | 4 | 文档引用 |
| Security | 2 | 安全分类 |
| 3D 几何/拓扑（其他）| 663 | 其他几何拓扑实体 |
</details>

### 数据来源

- **AP242 Ed2**: ISO 10303-242:2020 EXPRESS schema (`mim_lf.exp`), 来源: [ISO STEP Module Repository](https://standards.iso.org/iso/10303/smrl/v8/tech/smrlv8.zip)
- **AP203 Ed2**: ISO 10303-203 EXPRESS schema (`mim_lf.exp`), 同上
- **AP214**: NIST STEP File Analyzer 实体列表, 来源: [usnistgov/SFA](https://github.com/usnistgov/SFA)

### 几何层

**3D 曲线** (`Curve3`，sealed interface，13 种)：
- `Line3`, `Circle`, `Ellipse3`, `Hyperbola3`, `Parabola3`, `Clothoid3`
- `Polyline3`, `CompositeCurve3`, `TrimmedCurve3`
- `BSplineCurve3`, `RationalBSplineCurve3`
- `SurfaceCurve3`, `DegenerateCurve3`

**3D 曲面** (`SurfaceGeometry`，sealed interface，16 种)：
- `Plane`, `CylindricalSurface`, `ConicalSurface`, `ToroidalSurface`, `SphericalSurface`
- `BSplineSurface3`, `RationalBSplineSurface3`
- `SurfaceOfLinearExtrusion3`, `SurfaceOfRevolution3`, `RuledSurface3`
- `OffsetSurface3`, `SurfaceOfConstantRadius3`
- `ParaboloidSurface`, `HyperboloidSurface`, `SurfaceOfTranslation3`, `SurfaceOfProjection3`

**2D 参数域几何** (16 种)：
- `Point2`, `Vector2`, `Direction2`, `BoundingBox2`
- `Line2`, `Circle2`, `Ellipse2`, `Hyperbola2`, `Parabola2`
- `Polyline2`, `CompositeCurve2`, `TrimmedCurve2`
- `BSplineCurve2`, `RationalBSplineCurve2`, `Curve2`, `DegenerateCurve2`

**基础几何类型**：
- `CartesianPoint`, `Vector3`, `Direction3`, `Axis2Placement3D`
- `Transformation3`, `BoundingBox3`

### 拓扑层

完整 B-Rep 拓扑支持：
- `Vertex`, `Edge`, `OrientedEdge`, `EdgeLoop`, `VertexLoop`, `PolyLoop`
- `FaceBound`, `Face`, `Shell`, `Solid`

### STEP 实体类型覆盖

**基础几何/拓扑** (核心)：
- `CARTESIAN_POINT`, `DIRECTION`, `VECTOR`
- `AXIS2_PLACEMENT_3D`, `AXIS2_PLACEMENT_2D`, `AXIS1_PLACEMENT`
- `LINE`, `POLYLINE`, `CIRCLE`, `ELLIPSE`, `HYPERBOLA`, `PARABOLA`
- `TRIMMED_CURVE`, `COMPOSITE_CURVE`, `COMPOSITE_CURVE_ON_SURFACE`
- `B_SPLINE_CURVE_WITH_KNOTS`, `RATIONAL_B_SPLINE_CURVE`, `BEZIER_CURVE`
- `SURFACE_CURVE`, `SEAM_CURVE`, `PCURVE`, `OFFSET_CURVE_3D`, `OFFSET_CURVE_2D`
- `PLANE`, `CYLINDRICAL_SURFACE`, `CONICAL_SURFACE`, `TOROIDAL_SURFACE`, `SPHERICAL_SURFACE`
- `B_SPLINE_SURFACE_WITH_KNOTS`, `RATIONAL_B_SPLINE_SURFACE`
- `SURFACE_OF_LINEAR_EXTRUSION`, `SURFACE_OF_REVOLUTION`, `OFFSET_SURFACE`
- `RECTANGULAR_TRIMMED_SURFACE`, `CURVE_BOUNDED_SURFACE`
- `VERTEX_POINT`, `EDGE_CURVE`, `ORIENTED_EDGE`, `SUBEDGE`
- `EDGE_LOOP`, `VERTEX_LOOP`, `POLY_LOOP`
- `FACE_BOUND`, `FACE_OUTER_BOUND`, `ADVANCED_FACE`, `FACE_SURFACE`, `ORIENTED_FACE`
- `OPEN_SHELL`, `CLOSED_SHELL`, `ORIENTED_OPEN_SHELL`, `ORIENTED_CLOSED_SHELL`
- `MANIFOLD_SOLID_BREP`, `BREP_WITH_VOIDS`
- `SHELL_BASED_SURFACE_MODEL`, `FACE_BASED_SURFACE_MODEL`

**CSG/Swept Solid/Profile/Tessellated**：
- `BOOLEAN_RESULT`, `BOOLEAN_CLIPPING_RESULT`, `CSG_SOLID`, `SOLID_REPLICA`
- `BLOCK`, `SPHERE`, `ELLIPSOID`, `RIGHT_CIRCULAR_CYLINDER`, `TORUS`, `RIGHT_ANGULAR_WEDGE`
- `EXTRUDED_AREA_SOLID`, `REVOLVED_AREA_SOLID`
- `SURFACE_CURVE_SWEPT_AREA_SOLID`
- `EXTRUDED_AREA_SOLID_TAPERED`, `REVOLVED_AREA_SOLID_TAPERED`
- `HALF_SPACE_SOLID`, `BOXED_HALF_SPACE`, `POLYGONAL_BOUNDED_HALF_SPACE`, `BOX_DOMAIN`
- `SWEPT_DISK_SOLID`
- `CIRCLE_PROFILE_DEF`, `RECTANGLE_PROFILE_DEF`, `ELLIPSE_PROFILE_DEF`
- `ARBITRARY_CLOSED_PROFILE_DEF`, `ARBITRARY_PROFILE_DEF`
- `EXTRUDED_FACE_SOLID`, `REVOLVED_FACE_SOLID`, `SWEPT_FACE_SOLID`
- `TESSELLATED_FACE_SET`, `TESSELLATED_FACE` → 三角网格 B-Rep
- `TRIANGULATED_FACE`, `COMPLEX_TRIANGULATED_FACE`, `CUBIC_BEZIER_TRIANGULATED_FACE` → 三角网格 B-Rep
- `CYLINDER_VOLUME`, `SPHERE_VOLUME`, `TORUS_VOLUME`, `PRISM_VOLUME`, `CSG_SOLID`

**Representation/Context/Unit**：
- `REPRESENTATION`, `SHAPE_REPRESENTATION`, `REPRESENTATION_MAP`, `MAPPED_ITEM`
- `REPRESENTATION_CONTEXT`, `GEOMETRIC_REPRESENTATION_CONTEXT`
- `NAMED_UNIT`, `SI_UNIT`, `CONTEXT_DEPENDENT_UNIT`, `CONVERSION_BASED_UNIT`, `DERIVED_UNIT`
- `MEASURE_WITH_UNIT`, `UNCERTAINTY_MEASURE_WITH_UNIT`

**Product/Assembly**：
- `APPLICATION_CONTEXT`, `APPLICATION_PROTOCOL_DEFINITION`
- `PRODUCT_CONTEXT`, `PRODUCT`, `PRODUCT_DEFINITION`, `PRODUCT_DEFINITION_FORMATION`
- `PRODUCT_DEFINITION_SHAPE`, `SHAPE_DEFINITION_REPRESENTATION`
- `NEXT_ASSEMBLY_USAGE_OCCURRENCE`, `CONTEXT_DEPENDENT_SHAPE_REPRESENTATION`
- `ITEM_DEFINED_TRANSFORMATION`, `MAPPED_ITEM`

**颜色/样式/PMI**：
- `COLOUR_RGB`, `STYLED_ITEM`, `OVER_RIDING_STYLED_ITEM`
- `PRESENTATION_STYLE_ASSIGNMENT`, `PRESENTATION_LAYER_ASSIGNMENT`
- `ANNOTATION_TEXT_OCCURRENCE`, `ANNOTATION_CURVE_OCCURRENCE`, `ANNOTATION_PLANE`
- `ANNOTATION_FILL_AREA`, `ANNOTATION_FILL_AREA_REGION`, `FILL_AREA_WITH_OUTLINE`
- `DRAUGHTING_CALLOUT`, `DRAUGHTING_PRE_DEFINED_COLOUR`, `PRE_DEFINED_COLOUR`
- `MEASURE_REPRESENTATION_ITEM`, `TERMINATOR_SYMBOL`, `DIMENSIONAL_EXPONENT`

**GD&T 公差**：
- `GEOMETRIC_TOLERANCE` (含最大公差、定义区域单位、非均匀区域变体)
- `DATUM_REFERENCE_MODIFIER`, `DATUM_REFERENCE_MODIFIER_WITH_VALUE`
- `RUNOUT_ZONE_DEFINITION_ORIENTATION`

**运动学**：
- 14 种运动副类型（`PRISMATIC_PAIR`, `REVOLUTE_PAIR` 等）
- `MECHANISM_STATE_REPRESENTATION`, `KINEMATIC_PATH`

**有限元/网格**：
- `VOLUME_3D_ELEMENT_REPRESENTATION`, `FEA_MATERIAL_PROPERTY_REPRESENTATION`
- `ELEMENT_VOLUME_2D`, `ELEMENT_VOLUME_3D`, `NODE_SET`, `ELEMENT_SET`

**元数据/组织/审批**：
- `PERSON`, `ORGANIZATION`, `PERSON_AND_ORGANIZATION`
- `APPROVAL`, `APPROVAL_STATUS`, `APPROVAL_ROLE`
- `SECURITY_CLASSIFICATION`, `CERTIFICATION`, `CONTRACT`, `DOCUMENT`
- `DATE_AND_TIME`, `CALENDAR_DATE`, `LOCAL_TIME`
- `GROUP`, `CLASSIFICATION_ASSIGNMENT`, `IDENTIFICATION_ASSIGNMENT`

## 项目结构

```
src/main/java/com/minicad/
├── common/           -- 公共异常类和工具类 (8 个)
├── geometry/         -- 3D 几何类型 (37 个)
│   ├── Curve3        -- 曲线密封接口 (13 种实现)
│   └── SurfaceGeometry -- 曲面密封接口 (16 种实现)
├── geometry2d/       -- 2D 参数域几何类型 (16 个)
├── topology/         -- B-Rep 拓扑类型 (11 个)
├── step/
│   ├── syntax/       -- STEP 语法解析器 (5 个)
│   ├── semantic/     -- STEP 语义解析器 (6 个)
│   └── model/        -- STEP 实体模型类 (1264 个, 26 个子包)
│       ├── base/         -- 基础抽象 (11)
│       ├── geometry/     -- 几何实体 (115)
│       ├── topology/     -- 拓扑实体 (31)
│       ├── product/      -- 产品/装配 (107)
│       ├── annotation/   -- 标注/PMI (117)
│       ├── manufacturing/-- 制造工艺 (117)
│       ├── tolerance/    -- GD&T 公差 (32)
│       ├── unit/         -- 单位定义 (19)
│       ├── kinematic/    -- 运动学 (26)
│       ├── fea/          -- 有限元 (24)
│       ├── workflow/     -- 工作流 (199)
│       ├── validation/   -- 验证 (50)
│       ├── action/       -- 动作 (49)
│       ├── classification/ -- 分类 (28)
│       ├── config_mgmt/  -- 配置管理 (28)
│       ├── security/     -- 安全 (22)
│       ├── resource/     -- 资源 (67)
│       ├── organization/ -- 组织 (19)
│       ├── date_time/    -- 日期时间 (11)
│       ├── document/     -- 文档 (21)
│       ├── approval/     -- 审批 (13)
│       ├── log_audit/    -- 日志审计 (23)
│       ├── backup_recovery/ -- 备份恢复 (14)
│       ├── analysis/     -- 分析 (15)
│       ├── profile/      -- 截面轮廓 (7)
│       └── system/       -- 系统级 (10)
└── app/              -- 应用入口 (13 个)

examples/             -- STEP 示例文件
```

## 架构概览

```
┌─────────────────────────────────────────────────────────────────────┐
│                        STEP 文件 (.step / .stp)                     │
└─────────────────────────────────────────────────────────────────────┘
                                   │
                                   ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    语法层 (step.syntax)                              │
│                                                                     │
│  StepTokenizer ─────► StepParser ─────► StepFile (AST)             │
│                                                                     │
│  • ISO 10303-21 词法分析  • HEADER/DATA 解析  • 实体参数解析       │
│  • 字符串转义处理         • 注释跳过         • 重复 ID 检测        │
└─────────────────────────────────────────────────────────────────────┘
                                   │
                                   ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    语义层 (step.semantic)                            │
│                                                                     │
│  StepEntityResolver ─────► StepCadBuilder ─────► B-Rep 模型        │
│                                                                     │
│  • 实体解析 (1264 种)  • 几何构建           • 拓扑构建             │
│  • 前向引用处理        • 曲线/曲面求值      • Shell/Solid 验证     │
│  • 参数类型检查        • 装配变换           • 三角网格导出          │
└─────────────────────────────────────────────────────────────────────┘
                                   │
                                   ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    几何层 (geometry / topology)                      │
│                                                                     │
│  Curve3 (13 种)         SurfaceGeometry (16 种)                    │
│  • Line3, Circle        • Plane, Cylinder                         │
│  • BSpline, Trimmed     • BSpline, Offset                         │
│  • Composite, Polyline  • Torus, Sphere                           │
│                                                                     │
│  Topology:                                                         │
│  • Vertex → Edge → Face → Shell → Solid                           │
│  • TopologyValidator:闭合性、流形性、零面积检测                    │
└─────────────────────────────────────────────────────────────────────┘
                                   │
                                   ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    应用层 (app)                                      │
│                                                                     │
│  StepDumpApp ────────► CLI 解析输出                                 │
│  StepViewerApp ──────► Web 预览器 (Jetty + Three.js)               │
│  StepPreviewJsonExporter ──► GLB 导出                              │
│  StepCapabilityReportApp ──► 覆盖报告生成                          │
└─────────────────────────────────────────────────────────────────────┘
```

### 数据流

1. **输入**: STEP 文本 (ISO 10303-21)
2. **解析**: Tokenizer → Parser → AST (StepFile)
3. **解析**: Resolver → 语义模型 (StepEntity map)
4. **构建**: Builder → 几何/拓扑 (Curve, Surface, Topology)
5. **导出**: Exporter → GLB (Web 预览) 或 JSON (元数据)

### 扩展新实体

添加新 STEP 实体支持的步骤：

1. `step.model.<subpackage>`: 创建 `StepXxx` 模型类
2. `StepEntityResolver`: 添加 `resolveXxx()` 方法
3. `MiscRegistry`: 注册实体工厂
4. `StepCadBuilder`: 添加 `buildXxx()` 方法（如果需要几何）
5. 测试: 添加解析 + 解析 + 构建测试

## 构建与运行

```bash
mvn test                                    # 运行全部测试
mvn -q test                                 # 安静模式运行测试
mvn clean test                              # 清理后重新构建
mvn exec:java -Dexec.args="examples/minimal-square.step"  # CLI 解析 STEP 文件
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp exec:java  # 启动 Web 预览器
```

## Web 预览器

启动后访问 http://127.0.0.1:8080，支持：

- 上传 `.step` / `.stp` 文件
- 直接粘贴 STEP 文本
- 加载 `examples/` 目录中的示例文件
- 点击面、边、装配实例查看详细信息
- 查看颜色、图层、PMI 和 validation report

## 技术栈

- Java 11
- Maven
- JUnit 5
- Jetty 11 (嵌入式 Web 服务器)
- Three.js (前端渲染)
- 无外部 CAD 内核依赖 (不依赖 OpenCascade、FreeCAD、Parasolid 等)

## 当前限制

### 几何求值已知约束

- **CSG 实体 Solid-Solid Boolean**: `BOOLEAN_RESULT` 中两个有界实体的布尔运算（如球体减圆柱）需要网格布尔内核——未实现。仅实现了半空间裁剪（`HALF_SPACE_SOLID` / `BOXED_HALF_SPACE` 的差集/交集/并集）。
- **B-Spline 曲面修剪**: 修剪曲线在 B-Spline 曲面上使用 UV 投影；复杂多循环修剪可能产生伪影。
- **退化边**: 零长度边可能在拓扑构造期间失败。

### 尚未实现 B-Rep 生成的实体

以下 STEP AP214/AP242 实体类型已注册但无 B-Rep 生成：

**高级 PMI/公差**:
- `GEOMETRIC_TOLERANCE_RELATIONSHIP` - 几何公差关系
- `DATUM_SYSTEM` - 基准系统（多基准组合）

**Validation Property 框架**:
- `VALIDATION_RESULT_REPRESENTATION` - 验证结果表示

### 工业文件导入状态

| 文件 | 实体数 | solids | 未支持面数 | 备注 |
|------|--------|--------|-----------|------|
| `engine.stp` | 93829 | 31 | 0 | 顶点投影容差处理源数据 ~1mm 精度问题 |
| `fan.stp` | 41707 | 1 | 0 | - |

## 开发原则

- **可靠性目标**: 所有支持必须真实可运行，经过实际 STEP 文件测试
- **显式失败**: 所有不支持必须显式失败（抛出 `UnsupportedStepEntityException` 或 `UnsupportedGeometryException`），不静默忽略
- **代码显式优于泛化框架**: 保持代码清晰可读，避免过度抽象
- **渐进式支持**: 优先支持最常见的 STEP 实体类型，逐步扩展支持范围

## 路线图

### 近期目标
- 完善 B-Spline 曲面修剪支持
- 实现基本的布尔运算几何求值
- 支持更多 PMI 实体类型

### 中期目标
- 实现 Swept Solid 的几何求值
- 支持完整的 validation property 框架
- 添加拓扑修复/healing 功能

### 远期目标
- 逐步覆盖 STEP AP214/AP242 核心实体
- 持续完善几何内核功能
- 支持 STEP 文件导出能力

## 故障排除

### 常见问题

#### 1. Java 版本错误

**错误**: `UnsupportedClassVersionError` 或编译失败

**解决**: MiniCAD 需要 Java 11。检查并设置正确的 JAVA_HOME：

```bash
# Windows (PowerShell)
$env:JAVA_HOME = "C:\path\to\jdk-11"

# Linux/macOS
export JAVA_HOME=/path/to/jdk-11

# 验证
java -version  # 应显示 11.x.x
```

#### 2. Maven 依赖下载失败

**错误**: `Could not resolve dependencies` 或网络超时

**解决**: 检查 Maven 镜像配置（`~/.m2/settings.xml`）：

```xml
<settings>
  <mirrors>
    <mirror>
      <id>aliyun</id>
      <name>Aliyun Maven Mirror</name>
      <url>https://maven.aliyun.com/repository/public</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
  </mirrors>
</settings>
```

#### 3. 测试失败

**错误**: 测试用例失败或编译错误

**解决**:

```bash
# 清理并重新构建
mvn clean test

# 如果仍然失败，检查 Java 版本
java -version  # 必须是 Java 11

# 查看具体失败原因
mvn test -X  # 调试模式
```

#### 4. Web 预览器无法启动

**错误**: `Address already in use: bind 127.0.0.1:8080`

**解决**: 端口被占用，使用其他端口：

```bash
# 使用 --port 参数
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp \
    -Dexec.args="--port 9090"

# 或设置系统属性
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp \
    -Dminicad.preview.port=9090
```

#### 5. STEP 文件解析失败

**错误**: `StepParseException` 或 `UnsupportedStepEntityException`

**可能原因**:

- **语法错误**: STEP 文件不符合 ISO 10303-21 规范
  - 解决: 使用 FreeCAD 或其他 CAD 软件重新导出
  
- **不支持的实体**: 文件包含未实现的 STEP 实体类型
  - 解决: 查看 `doc/generated/coverage.md` 确认实体支持状态
  - 或提交 issue 请求支持

- **编码问题**: 文件编码不是 UTF-8
  - 解决: MiniCAD 支持 UTF-8、GBK、GB18030 自动检测
  - 如果仍然失败，转换为 UTF-8：`iconv -f GBK -t UTF-8 input.step > output.step`

#### 6. 大文件处理缓慢

**现象**: 解析大型 STEP 文件（>10MB）时内存占用高或速度慢

**解决**:

```bash
# 增加 JVM 堆内存
mvn exec:java -Dexec.args="large-file.step" \
    -Dexec.mainClass=com.minicad.app.StepDumpApp \
    -Dexec.jvmArgs="-Xmx4g"

# 或使用 --max-upload 调整预览限制
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp \
    -Dexec.args="--max-upload 100m"
```

#### 7. engine.stp 解析输出包含 379 个退化边

**现象**: `unsupportedFaces=379`, `edge must have distinct vertices`

**说明**: 这是已知限制。engine.stp 包含共线顶点导致的零长度边。MiniCAD 的拓扑验证器会检测并报告这些问题，但不影响其他几何的正确构建。

**影响**: 这 379 个面无法生成有效的 B-Rep，但其他 31 个 solid 仍然正确解析。

### 获取帮助

- **GitHub Issues**: 报告 bug 或请求功能
- **GitHub Discussions**: 提问或讨论
- **SECURITY.md**: 报告安全漏洞

