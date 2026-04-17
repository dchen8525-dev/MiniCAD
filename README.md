# MiniCAD

面向工业应用的 CAD 内核与 STEP 文件解析器，使用 Java 21 实现。

## 项目定位

MiniCAD 致力于构建一个完整的工业 CAD 内核，提供 STEP (ISO 10303) 文件的完整解析能力，支持 AP214/AP242 协议的核心实体类型。项目的目标是：

- **完整 STEP 解析**: 支持所有主流 STEP 实体类型的解析和语义理解
- **B-Rep 几何内核**: 支持完整的边界表示 (Boundary Representation) 拓扑结构
- **可视化预览**: 提供基于 Web 的三维模型预览能力
- **工业级兼容**: 支持主流 CAD 软件 (CATIA、NX、SolidWorks、Creo 等) 导出的 STEP 文件

项目已实现：

- **1062 个 STEP 实体模型类** (`step/model`)
- **24000+ 种 STEP 实体类型注册**（约 1559 个直接 registry.put() 调用）
- **完整的语法解析链**：STEP 文本 → syntax AST → semantic model → geometry/topology
- **Web 预览器**：基于 Three.js 的浏览器可视化，支持平面、圆柱面、圆锥面、球面、环面、B-Spline 曲面等

## STEP 支持范围

### 国际标准实体类型数量

根据 ISO 10303 标准定义：

| 协议 | 实体类型数量 | 说明 |
|------|------------|------|
| **AP214** | 643 | 汽车/机械设计核心协议 |
| **AP242 Ed1** | 695 | AP214 + AP203 整合版 |
| **AP242 Ed2** | 762 | 最新最全的工程数据交换协议 |

### 当前库实现情况

| 类别 | 数量 | 说明 |
|------|------|------|
| **STEP 实体模型类** | 1062 | `step/model` 目录下的 Java record 类 |
| **实体类型注册** | 1158 | `registry.put()` 直接调用次数 |
| **覆盖 AP242 Ed2** | ~140% | 超过标准定义的实体类型数量 |

MiniCAD 实现了超过 AP242 Ed2 (762 种) 标准定义的实体类型数量，主要原因：
- 支持多协议兼容（AP214、AP203、AP242 等）
- 包含历史/废弃实体类型的向后兼容支持
- 实现了部分 AP242 Ed3 预览版本的实体类型

### 几何层

**3D 曲线** (30 种)：
- `Line3`, `Circle`, `Ellipse3`, `Hyperbola3`, `Parabola3`
- `Polyline3`, `CompositeCurve3`, `TrimmedCurve3`
- `BSplineCurve3`, `RationalBSplineCurve3`
- `SurfaceCurve3`, `Clothoid3`, `DegenerateCurve3`

**3D 曲面** (12 种)：
- `Plane`, `CylindricalSurface`, `ConicalSurface`, `ToroidalSurface`, `SphericalSurface`
- `BSplineSurface3`, `RationalBSplineSurface3`, `OffsetSurface3`
- `SurfaceOfLinearExtrusion3`, `SurfaceOfRevolution3`, `RuledSurface3`, `SurfaceOfConstantRadius3`

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

**CSG/Swept Solid/Profile**：
- `BOOLEAN_RESULT`, `BOOLEAN_CLIPPING_RESULT`, `CSG_SOLID`, `SOLID_REPLICA`
- `BLOCK`, `SPHERE`, `ELLIPSOID`, `RIGHT_CIRCULAR_CYLINDER`, `TORUS`, `RIGHT_ANGULAR_WEDGE`
- `EXTRUDED_AREA_SOLID`, `REVOLVED_AREA_SOLID`
- `CIRCLE_PROFILE_DEF`, `RECTANGLE_PROFILE_DEF`, `ELLIPSE_PROFILE_DEF`
- `ARBITRARY_CLOSED_PROFILE_DEF`, `ARBITRARY_PROFILE_DEF`
- `HALF_SPACE_SOLID`, `BOXED_HALF_SPACE`, `BOX_DOMAIN`

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
- `DRAUGHTING_CALLOUT`, `MEASURE_REPRESENTATION_ITEM`

**元数据/组织/审批**：
- `PERSON`, `ORGANIZATION`, `PERSON_AND_ORGANIZATION`
- `APPROVAL`, `APPROVAL_STATUS`, `APPROVAL_ROLE`
- `SECURITY_CLASSIFICATION`, `CERTIFICATION`, `CONTRACT`, `DOCUMENT`
- `DATE_AND_TIME`, `CALENDAR_DATE`, `LOCAL_TIME`
- `GROUP`, `CLASSIFICATION_ASSIGNMENT`, `IDENTIFICATION_ASSIGNMENT`

## 项目结构

```
src/main/java/com/minicad/
├── common/           -- 公共异常类和工具类 (7 个)
├── geometry/         -- 3D 几何类型 (30 个)
├── geometry2d/       -- 2D 参数域几何类型 (16 个)
├── topology/         -- B-Rep 拓扑类型 (11 个)
├── step/
│   ├── model/        -- STEP 实体模型类 (1062 个)
│   ├── semantic/     -- STEP 语义解析器 (9 个)
│   └── syntax/       -- STEP 语法解析器 (3 个)
└── app/              -- CLI 和 Web 预览应用 (5 个)

examples/             -- STEP 示例文件 (44 个)
```

## 构建与运行

```bash
mvn test                                    # 运行全部测试
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

- Java 21
- Maven
- JUnit 5
- Jetty 11 (嵌入式 Web 服务器)
- Three.js (前端渲染)
- 无外部 CAD 内核依赖 (不依赖 OpenCascade、FreeCAD、Parasolid 等)

## 当前限制

### 几何求值未实现

以下实体已在解析层完整支持，但几何求值尚未实现：

- **CSG Boolean 运算**: `BOOLEAN_RESULT`, `BOOLEAN_CLIPPING_RESULT`, `COMPLEX_CLIPPING_RESULT` 解析后不执行几何布尔运算
- **Swept Solid**: `EXTRUDED_AREA_SOLID`, `REVOLVED_AREA_SOLID`, `SURFACE_CURVE_SWEPT_AREA_SOLID` 解析后不生成 B-Rep
- **Half Space**: `HALF_SPACE_SOLID`, `BOXED_HALF_SPACE`, `POLYGONAL_BOUNDED_HALF_SPACE` 不执行裁剪求值
- **Tessellated Geometry**: `TESSELLATED_FACE_SET`, `TESSELLATED_FACE`, `TESSELLATED_TRIANGLE` 不转换为解析几何

### STEP 实体解析尚未支持

以下 STEP AP214/AP242 实体类型尚未实现解析支持：

**高级几何曲面**:
- `SURFACE_OF_TRANSLATION` - 平移曲面
- `SURFACE_OF_PROJECTION` - 投影曲面
- `PARABOLOID_SURFACE` - 抛物面
- `HYPERBOLOID_SURFACE` - 双曲面
- `TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS` - 带指定弯曲的环面

**高级扫掠实体**:
- `EXTRUDED_FACE_SOLID` - 面扫掠实体
- `REVOLVED_FACE_SOLID` - 面旋转实体
- `SURFACE_CURVE_SWEPT_FACE_SOLID` - 曲面曲线扫掠面实体

**高级 CSG 体素**:
- `CYLINDER_VOLUME` - 圆柱体素
- `SPHERE_VOLUME` - 球体素
- `TORUS_VOLUME` - 环体素
- `RIGHT_CIRCULAR_CYLINDER_VOLUME` - 正圆柱体素
- `RIGHT_CIRCULAR_CONE_VOLUME` - 正圆锥体素
- `PRISM_VOLUME` - 棱柱体素

**高级 PMI/公差标注**:
- `GEOMETRIC_TOLERANCE_RELATIONSHIP` - 几何公差关系
- `DATUM_SYSTEM` - 基准系统（多基准组合）
- `PROJECTED_ZONE_DEFINITION` - 投影区域定义
- `NON_UNIFORM_ZONE_DEFINITION` - 非均匀区域定义

**高级装配/配置**:
- `PRODUCT_DEFINITION_USAGE` - 产品定义用法（抽象基类）
- `PRODUCT_DEFINITION_PROCESS` - 产品定义过程
- `ASSEMBLY_GROUP_COMPONENT_USAGE` - 装配组组件用法

**Validation Property 框架**:
- `VALIDATION_PROPERTY_REPRESENTATION` - 验证属性表示
- `VALIDATION_RESULT_REPRESENTATION` - 验证结果表示
- `CALCULATED_GEOMETRIC_REPRESENTATION_ITEM` - 计算几何表示项

**Kinematic 运动学 (部分支持，以下未实现)**:
- `KINEMATIC_PATH` - 运动路径
- `KINEMATIC_FRAME_BASED_TRANSFORMATION` - 运动学框架变换

**有限元/网格 (部分支持，以下未实现)**:
- `ELEMENT_VOLUME_2D` - 2D 单元体积
- `ELEMENT_VOLUME_3D` - 3D 单元体积
- `NODE_SET` - 节点集
- `ELEMENT_SET` - 单元集

**其他**:
- `REPLICATION_OPERATION` - 复制操作
- `B_SPLINE_CURVE_WITH_KNOTS_AND_SPECIFIED_BENDS` - 带指定弯曲的 B 样条曲线
- `TEXT_STYLE_WITH_BOX_CHARACTERISTICS` - 带框特征的文本样式
- `PRESENTATION_AREA` - 表示区域
- `PRESENTATION_VIEW` - 表示视图

## 开发原则

- **工业级可靠性**: 所有支持必须真实可运行，经过实际 STEP 文件测试
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
- 支持所有 STEP AP214/AP242 核心实体
- 实现完整的几何内核功能
- 支持 STEP 文件导出能力