# OCCT 8.0.0 RC5 STEP 格式支持程度

> 基于 OpenCASCADE OCCT 8.0.0 RC5 代码库分析（2026-04-21）

---

## 一、应用协议（AP）支持总览

| STEP 应用协议 | 读取支持 | 写入支持 | 说明 |
|---|---|---|---|
| **AP203** (配置管理设计) | ✅ 完整 | ✅ 完整 | 配置管理变更、合同、认证、审批 |
| **AP214** (汽车设计) | ✅ 完整 | ✅ 完整 | 支持 CD(1996)、DIS(1998)、IS(2002) 三个子版本 |
| **AP242** (MBD/PMI) | ⚠️ 部分 | ⚠️ 部分 | 支持 DIS 版本，ID属性、几何项用法、标注关联 |
| **AP209** (结构分析) | ⚠️ 部分 | ❌ 不支持 | 仅1个实体类，有限元分析相关 |

---

## 二、各 STEP 类型实体详细支持列表

### 2.1 StepBasic — 基础产品数据

| 实体数量 | 148 |
|---|---|
| 读写模块 | RWStepBasic（119个读写器） |
| 支持程度 | ✅ 完整 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `Product` | 产品定义 |
| `ProductDefinition` | 产品定义（含版本/形态） |
| `ProductDefinitionFormation` | 产品形成（特定版本/修订） |
| `ProductContext` | 产品上下文（应用领域） |
| `Organization` | 组织/公司信息 |
| `Person` | 人员信息 |
| `PersonAndOrganization` | 人员与组织关联 |
| `Approval` | 审批信息 |
| `ApprovalRole` | 审批角色 |
| `ApprovalStatus` | 审批状态 |
| `Date` / `DateAndTime` | 日期/日期时间 |
| `CalendarDate` / `LocalTime` | 日历日期/本地时间 |
| `LengthUnit` / `NamedUnit` / `SiUnit` | 长度单位/SI单位 |
| `UncertaintyMeasure` | 不确定度度量 |
| `MechanicalContext` | 机械上下文 |
| `DesignContext` | 设计上下文 |
| `Action` / `ActionMethod` / `ActionRequestSolution` | 动作/方法/请求解决方案 |
| `ActionAssignment` / `ActionProperty` | 动作分配/属性 |
| `AppliedApprovalAssignment` | 审批分配 |
| `AppliedDateAssignment` | 日期分配 |
| `ProductCategory` / `ProductDefinedGenericAttribute` | 产品分类/自定义属性 |

---

### 2.2 StepGeom — 几何

| 实体数量 | 97 |
|---|---|
| 读写模块 | RWStepGeom（83个读写器） |
| 支持程度 | ✅ 完整 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `CartesianPoint` | 笛卡尔点 |
| `Direction` | 方向向量 |
| `Vector` | 向量 |
| `Line` | 直线 |
| `Circle` | 圆 |
| `Ellipse` | 椭圆 |
| `Hyperbola` | 双曲线 |
| `Parabola` | 抛物线 |
| `BSplineCurve` | B样条曲线 |
| `BSplineSurface` | B样条曲面 |
| `BezierCurve` / `BezierSurface` | Bezier曲线/曲面 |
| `Plane` | 平面 |
| `CylindricalSurface` | 柱面 |
| `ConicalSurface` | 锥面 |
| `SphericalSurface` | 球面 |
| `ToroidalSurface` | 环面 |
| `SurfaceOfRevolution` | 旋转曲面 |
| `SurfaceOfLinearExtrusion` | 线性拉伸曲面 |
| `OffsetSurface` | 偏移曲面 |
| `SweptSurface` / `SweptDiskSolid` | 扫掠曲面 |
| `PCurve` | 参数曲线（曲面参数空间中的曲线） |
| `TrimmedCurve` | 裁剪曲线 |
| `CompositeCurve` | 组合曲线 |
| `CompositeCurveOnSurface` | 曲面上的组合曲线 |
| `SurfaceCurve` | 曲面曲线（两曲面交线） |
| `Axis1Placement` | 单轴定位（仅方向） |
| `Axis2Placement2d` | 2D双轴定位 |
| `Axis2Placement3d` | 3D双轴定位 |
| `CartesianTransformationOperator` | 笛卡尔变换算子 |
| `CartesianTransformationOperator2d` | 2D笛卡尔变换算子 |
| `CartesianTransformationOperator3d` | 3D笛卡尔变换算子 |
| `Point` | 点（抽象基类） |
| `DegeneratePcurve` / `DegenerateToroidalSurface` | 退化曲面/曲线 |
| `UniformCurve` / `UniformSurface` | 均匀曲线/曲面 |
| `QuasiUniformCurve` / `QuasiUniformSurface` | 准均匀曲线/曲面 |
| `RationalBSplineCurve` / `RationalBSplineSurface` | 有理B样条曲线/曲面 |
| `NonUniformCurve` / `NonUniformSurface` | 非均匀曲线/曲面 |
| `RectangularTrimmedSurface` | 矩形裁剪曲面 |
| `FillAreaStyle` | 填充区域样式 |
| `GeometricRepresentationItem` | 几何表示项（基类） |
| `PointOnCurve` / `PointOnSurface` | 曲线/曲面上的点 |
| `CurveReplica` / `SurfaceReplica` | 曲线/曲面副本 |
| `EdgeCurve` | 边曲线（拓扑-几何关联） |
| `FaceSurface` | 面曲面（拓扑-几何关联） |

---

### 2.3 StepShape — 拓扑/形状

| 实体数量 | 105 |
|---|---|
| 读写模块 | RWStepShape（91个读写器） |
| 支持程度 | ✅ 完整 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `VertexPoint` | 顶点（点） |
| `VertexLoop` | 顶点环 |
| `EdgeCurve` | 边曲线 |
| `EdgeLoop` | 边环 |
| `OrientedEdge` | 有向边 |
| `PolyLoop` | 多边形环 |
| `FaceSurface` | 面曲面 |
| `AdvancedFace` | 高级面（带方向信息） |
| `FaceBound` | 面边界 |
| `FaceOuterBound` | 面外边界 |
| `ClosedShell` | 封闭壳 |
| `OpenShell` | 开放壳 |
| `ConnectedFaceSet` | 连接面集 |
| `ManifoldSolidBrep` | 流形实体B-Rep |
| `BrepWithVoids` | 带空洞的B-Rep |
| `FacetedBrep` | 小平面B-Rep |
| `FacetedBrepAndBrepWithVoids` | 小平面B-Rep与带空洞B-Rep |
| `ShellBasedSurfaceModel` | 基于壳的表面模型 |
| `GeometricSet` | 几何集 |
| `GeometricCurveSet` | 几何曲线集 |
| `TessellatedShell` | 三角化壳 |
| `TessellatedSolid` | 三角化实体 |
| `TriangulatedFace` | 三角化面 |
| `ComplexTriangulatedFace` | 复杂三角化面 |
| `TessellatedCoordinateSet` | 三角化坐标集 |
| `TessellatedFace` | 三角化面（通用） |
| `HalfSpaceSolid` | 半空间实体 |
| `SolidModel` | 实体模型（基类） |
| `BooleanResult` | 布尔运算结果 |
| `OrientedClosedShell` | 有向封闭壳 |
| `OrientedOpenShell` | 有向开放壳 |
| `OrientedPath` | 有向路径 |
| `Path` | 路径 |
| `Loop` | 环（基类） |
| `Edge` | 边（基类） |
| `Face` | 面（基类） |
| `Shell` | 壳（基类） |
| `ConnectedFaceSet` | 连接面集 |
| `CsgShapeRepresentation` | CSG形状表示 |
| `BoundingBox` | 包围盒 |
| `FillAreaShapeUse` | 填充区域形状使用 |
| `PointOnFace` | 面上的点 |
| `GeometricItemSpecificUsage` | 几何项特定用途 |
| `ItemIdentifiedRepresentationUsage` | 项目标识表示用法 |
| `IdAttribute` | ID属性 |
| `DraughtingModelItemAssociation` | 草图模型项关联 |

---

### 2.4 StepRepr — 表示与关系

| 实体数量 | 85 |
|---|---|
| 读写模块 | RWStepRepr（70个读写器） |
| 支持程度 | ✅ 完整 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `Representation` | 表示（基类） |
| `ShapeRepresentation` | 形状表示 |
| `ProductDefinitionShape` | 产品定义形状 |
| `ShapeDefinitionRepresentation` | 形状定义表示 |
| `RepresentationContext` | 表示上下文 |
| `RepresentationContext3d` | 3D表示上下文 |
| `GeometricRepresentationContext` | 几何表示上下文 |
| `PropertyDefinition` | 属性定义 |
| `PropertyDefinitionRepresentation` | 属性定义表示 |
| `ShapeAspect` | 形状特征（用于GD&T关联） |
| `ShapeAspectRelationship` | 形状特征关系 |
| `NextAssemblyUsageOccurrence` | 下一装配使用关系（NASU） |
| `UsageOccurrence` | 使用关系 |
| `ContextDependentShapeRepresentation` | 上下文依赖的形状表示（CDSR） |
| `RepresentationRelationship` | 表示关系 |
| `RepresentationMap` | 表示映射 |
| `MappedItem` | 映射项 |
| `ShapeRepresentationRelationship` | 形状表示关系 |
| `ShapeRepresentationTransformation` | 形状表示变换 |
| `ProductDefinitionRelationship` | 产品定义关系 |
| `SpecificHigherUsageOccurrence` | 特定高层使用关系（SHUO） |
| `AppliedAttributeClassification` | 应用属性分类 |
| `AttributeClassification` | 属性分类 |
| `ExternallyDefinedRepresentation` | 外部定义表示 |
| `ConfigurationDesign` / `ConfigurationItem` | 配置设计/项 |
| `FeatureRelationship` | 特征关系 |
| `PartRelationship` / `ProductRelationship` | 零件/产品关系 |
| `StructuralAnalysisRepresentation` | 结构分析表示 |
| `StructuralAnalysisRepresentationParameters` | 结构分析表示参数 |
| `ValueReasonPair` | 值-原因对 |
| `MeasureWithUnit` | 带单位的度量 |

---

### 2.5 StepVisual — 可视化/颜色/图层

| 实体数量 | 143 |
|---|---|
| 读写模块 | RWStepVisual（99个读写器） |
| 支持程度 | ✅ 完整 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `StyledItem` | 样式化项目 |
| `StyledRepresentationItem` | 样式化表示项 |
| `Colour` | 颜色（基类） |
| `RgbColour` | RGB颜色 |
| `PreDefinedColour` | 预定义颜色 |
| `DraughtingPreDefinedColour` | 草图预定义颜色 |
| `SurfaceStyleUsage` | 表面样式使用 |
| `SurfaceSideStyle` | 表面侧样式 |
| `SurfaceStyleFillArea` | 表面填充区域样式 |
| `SurfaceStyleRendering` | 表面渲染样式 |
| `SurfaceStyleSegmentationCurve` | 表面样式分割曲线 |
| `CurveStyle` | 曲线样式 |
| `CurveStyleFill` | 曲线填充样式 |
| `CurveStyleFont` | 曲线字体样式 |
| `FillAreaStyle` | 填充区域样式 |
| `FillAreaStyleColour` | 填充区域颜色样式 |
| `FillAreaStyleHatching` | 填充区域剖面线样式 |
| `FillAreaStyleTileCurveWithStyle` | 填充区域平铺曲线样式 |
| `TileCurve` | 平铺曲线 |
| `TileSymbol` | 平铺符号 |
| `SymbolStyle` | 符号样式 |
| `MarkerStyle` | 标记样式 |
| `TextStyle` | 文本样式 |
| `TextLiteral` | 文本字面量 |
| `TextLiteralWithExtent` | 带范围文本字面量 |
| `AnnotationCurveOccurrence` | 标注曲线出现 |
| `AnnotationFillAreaOccurrence` | 标注填充区域出现 |
| `PresentationLayerAssignment` | 表现图层分配 |
| `PresentationLayerUsage` | 表现图层使用 |
| `PresentationLayerWithStyle` | 带样式的表现图层 |
| `LayeredItem` | 图层项目 |
| `PresentationStyleAssignment` | 表现样式分配 |
| `PresentationStyleByContext` | 按上下文的表现样式 |
| `DraughtingModel` | 草图模型 |
| `AnnotationOccurrence` | 标注出现 |
| `AnnotationOccurrenceRoleAssociation` | 标注出现角色关联 |
| `AnnotationPlane` | 标注平面 |
| `AnnotationFillArea` | 标注填充区域 |
| `AnnotationSymbol` | 标注符号 |
| `AnnotationText` | 标注文本 |
| `DrawingRevision` | 图纸修订 |
| `PlanarBox` / `PlanarExtent` | 平面框/范围 |
| `ViewVolume` | 视图体 |
| `ViewingPlane` | 视图平面 |
| `VisibleActionRequest` | 可见动作请求 |
| `ImageFile` | 图像文件 |

---

### 2.6 StepDimTol — GD&T 几何公差

| 实体数量 | 62 |
|---|---|
| 读写模块 | RWStepDimTol（49个读写器） |
| 支持程度 | ✅ 完整（写入需手动设置 AP242 模式） |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `GeometricTolerance` | 几何公差（基类） |
| `FlatnessTolerance` | 平面度公差 |
| `StraightnessTolerance` | 直线度公差 |
| `RoundnessTolerance` | 圆度公差 |
| `CylindricityTolerance` | 圆柱度公差 |
| `ProfileOfLineTolerance` | 线轮廓度公差 |
| `ProfileOfSurfaceTolerance` | 面轮廓度公差 |
| `PositionTolerance` | 位置度公差 |
| `PerpendicularityTolerance` | 垂直度公差 |
| `ParallelismTolerance` | 平行度公差 |
| `AngularityTolerance` | 角度公差 |
| `ConcentricityTolerance` | 同心度公差 |
| `SymmetryTolerance` | 对称度公差 |
| `CircularRunoutTolerance` | 圆跳动公差 |
| `TotalRunoutTolerance` | 全跳动公差 |
| `DimensionalSize` | 尺寸大小 |
| `DimensionalLocation` | 尺寸位置 |
| `DimensionalLocationWithPath` | 带路径的尺寸位置 |
| `AngularSize` | 角度大小 |
| `AngularLocation` | 角度位置 |
| `Datum` | 基准 |
| `DatumFeature` | 基准特征 |
| `DatumTarget` | 基准目标 |
| `DatumReference` | 基准参考 |
| `DatumReferenceModifier` | 基准参考修饰符 |
| `DatumReferenceModifierWithSign` | 带符号的基准参考修饰符 |
| `ToleranceZone` | 公差带 |
| `ToleranceZoneForm` | 公差带形状 |
| `Modifier` | 修饰符 |
| `GeometricToleranceTarget` | 几何公差目标 |
| `GeometricToleranceWithDatumReference` | 带基准参考的几何公差 |
| `CompositeGroupTolerance` | 组合组公差 |
| `RunoutZoneDefinition` | 跳动带定义 |
| `ToleranceValue` | 公差值 |
| `ToleranceZoneDefinition` | 公差带定义 |
| `QualifiedRepresentationItem` | 限定表示项 |

---

### 2.7 StepKinematics — 运动学

| 实体数量 | 86 |
|---|---|
| 读写模块 | RWStepKinematics（74个读写器） |
| 支持程度 | ✅ 完整 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `KinematicJoint` | 运动副 |
| `KinematicPair` | 运动对 |
| `Mechanism` | 机构 |
| `Link` | 连杆 |
| `MotionConstraint` | 运动约束 |
| `KinematicChain` | 运动链 |
| `KinematicModel` | 运动学模型 |
| `KinematicProperty` | 运动学属性 |
| `RevoluteJoint` | 旋转副 |
| `PrismaticJoint` | 移动副 |
| `CylindricalJoint` | 圆柱副 |
| `SphericalJoint` | 球面副 |
| `PlanarJoint` | 平面副 |
| `ScrewJoint` | 螺旋副 |
| `GeneralJoint` | 通用副 |
| `DirectionSense` | 方向感 |
| `JointValue` | 关节值 |

---

### 2.8 StepFEA — 有限元分析

| 实体数量 | 67 |
|---|---|
| 读写模块 | RWStepFEA（52个读写器） |
| 支持程度 | ✅ 完整 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `FeaModel` | 有限元模型 |
| `Node` | 节点 |
| `Element` | 单元 |
| `Load` | 载荷 |
| `BoundaryCondition` | 边界条件 |
| `Material` | 材料 |
| `StructuralAnalysisModel` | 结构分析模型 |
| `StressAnalysis` | 应力分析 |
| `BucklingAnalysis` | 屈曲分析 |
| `ModalAnalysis` | 模态分析 |
| `ThermalAnalysis` | 热分析 |
| `DisplacementBoundaryCondition` | 位移边界条件 |
| `VelocityBoundaryCondition` | 速度边界条件 |
| `AccelerationBoundaryCondition` | 加速度边界条件 |
| `ForceBoundaryCondition` | 力边界条件 |
| `PressureBoundaryCondition` | 压力边界条件 |
| `ThermalBoundaryCondition` | 热边界条件 |
| `FeaLinearMaterial` | 线性材料 |
| `FeaNonLinearMaterial` | 非线性材料 |
| `FeaMassDensity` | 质量密度 |
| `FeaYieldStress` | 屈服应力 |
| `FeaUltimateStress` | 极限应力 |

---

### 2.9 StepElement — 有限元单元

| 实体数量 | 37 |
|---|---|
| 读写模块 | RWStepElement（15个读写器） |
| 支持程度 | ✅ 完整 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `VolumeElement` | 体单元 |
| `SurfaceElement` | 面单元 |
| `LineElement` | 线单元 |
| `MassElement` | 质量单元 |
| `ConnectivityElement` | 连接单元 |
| `ElementVolume` | 单元体积 |
| `NodeRepresentation` | 节点表示 |
| `ElementGeometricDescription` | 单元几何描述 |
| `UniformSurfaceElement` | 均匀面单元 |
| `UniformVolumeElement` | 均匀体单元 |

---

### 2.10 StepAP203 — AP203 专属实体

| 实体数量 | 21 |
|---|---|
| 读写模块 | RWStepAP203（11个读写器） |
| 支持程度 | ✅ 完整 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `CcDesignApproval` | 配置控制设计审批 |
| `CcDesignCertification` | 配置控制设计认证 |
| `CcDesignContract` | 配置控制设计合同 |
| `CcDesignDateAndTimeAssignment` | 配置控制设计日期时间分配 |
| `CcDesignPersonAndOrganizationAssignment` | 配置控制设计人员组织分配 |
| `CcDesignSecurityClassification` | 配置控制设计安全分类 |
| `Change` | 变更 |
| `ChangeRequest` | 变更请求 |
| `StartRequest` | 启动请求 |
| `StartWork` | 启动工作 |
| `WorkItem` | 工作项 |
| `MakeFromUsageOption` | 制造使用选项 |
| `SecurityClassification` | 安全分类 |
| `SecurityClassificationLevel` | 安全分类等级 |
| `Effectivity` | 有效性 |
| `DateAndTime` | 日期时间 |

---

### 2.11 StepAP214 — AP214 专属实体

| 实体数量 | 46 |
|---|---|
| 读写模块 | RWStepAP214（29个读写器） |
| 支持程度 | ✅ 完整 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `AppliedApprovalAssignment` | 应用审批分配 |
| `AppliedDateAssignment` | 应用日期分配 |
| `AppliedGroupAssignment` | 应用分组分配 |
| `AppliedSecurityClassificationAssignment` | 应用安全分类分配 |
| `AutoDesignApprovalAssignment` | 自动设计审批分配 |
| `AutoDesignDateAssignment` | 自动设计日期分配 |
| `AutoDesignGroupAssignment` | 自动设计分组分配 |
| `AutoDesignOrganizationAssignment` | 自动设计组织分配 |
| `AutoDesignSecurityClassificationAssignment` | 自动设计安全分类分配 |
| `ExternallyDefinedClass` | 外部定义类 |
| `ExternallyDefinedGeneralProperty` | 外部定义通用属性 |
| `Classification` / `ClassificationAssignment` | 分类/分类分配 |
| `DateAssignment` | 日期分配 |
| `Group` / `GroupAssignment` | 分组/分组分配 |
| `OrganizationAssignment` | 组织分配 |
| `SecurityClassificationAssignment` | 安全分类分配 |
| `ApprovalAssignment` | 审批分配 |
| `DateAndTimeAssignment` | 日期时间分配 |
| `PersonAndOrganizationAssignment` | 人员组织分配 |
| `PresentationView` | 表现视图 |
| `DrawingSheetRevision` | 图纸修订 |
| `ExecutedAction` / `ExecutedTask` | 已执行动作/任务 |
| `GeneralPropertyDefinition` | 通用属性定义 |
| `GroupAssignment` | 分组分配 |
| `RequirementAssignment` | 需求分配 |
| `RequirementSource` | 需求来源 |

---

### 2.12 StepAP242 — AP242 专属实体

| 实体数量 | 6 |
|---|---|
| 读写模块 | RWStepAP242（4个读写器） |
| 支持程度 | ⚠️ 部分 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `IdAttribute` | ID属性（模型基准标识） |
| `GeometricItemSpecificUsage` | 几何项特定用途（PMI关联） |
| `ItemIdentifiedRepresentationUsage` | 项目标识表示用法（标注关联） |
| `DraughtingModelItemAssociation` | 草图模型项关联（PMI标注） |

---

### 2.13 StepAP209 — AP209 专属实体

| 实体数量 | 1 |
|---|---|
| 读写模块 | 无独立读写模块 |
| 支持程度 | ⚠️ 极少 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `StructAnalysisModel` | 结构分析模型 |

---

### 2.14 HeaderSection — 文件头

| 实体数量 | 5 |
|---|---|
| 读写模块 | RWHeaderSection（6个读写器） |
| 支持程度 | ✅ 完整 |

**主要实体对象：**

| 实体对象 | 功能 |
|---|---|
| `FileDescription` | 文件描述（协议、实现级别） |
| `FileName` | 文件名（作者、组织、时间戳） |
| `FileSchema` | 文件模式声明（AP类型、版本） |

---

## 三、Shape 表示类型支持矩阵

| STEP Shape 表示类型 | 读取 | 写入 | 说明 |
|---|---|---|---|
| `advanced_brep_shape_representation` | ✅ | ✅ | 高级B-Rep（含AdvancedFace） |
| `faceted_brep_shape_representation` | ✅ | ✅ | 小平面B-Rep |
| `manifold_surface_shape_representation` | ✅ | ✅ | 流形表面模型 |
| `geometrically_bounded_wireframe_shape_representation` | ✅ | ✅ | 几何约束线框 |
| `geometrically_bounded_surface_shape_representation` | ✅ | ✅ | 几何约束曲面 |
| `hybrid representations` | ✅ | ✅ | 混合表示 |
| `tessellated_shape_representation` | ✅ | ✅ | 三角化/网格表示 |

---

## 四、可转换的根实体类型（读取）

| 根实体类型 | 说明 |
|---|---|
| `product_definition` | 产品定义 |
| `next_assembly_usage_occurrence` | 装配使用关系 |
| `shape_definition_representation` | 形状定义表示 |
| `shape_representation` 及其子类 | 形状表示 |
| `manifold_solid_brep` | 流形实体B-Rep |
| `brep_with_voids` | 带空洞B-Rep |
| `faceted_brep` | 小平面B-Rep |
| `faceted_brep_and_brep_with_voids` | 小平面B-Rep与带空洞B-Rep |
| `shell_based_surface_model` | 基于壳的表面模型 |
| `geometric_set` / `geometric_curve_set` | 几何集/几何曲线集 |
| `mapped_item` | 映射项 |
| `face_surface` 及其子类（含`advanced_face`） | 面曲面及其子类 |
| `shape_representation_relationship` 及其子类 | 形状表示关系 |
| `context_dependent_shape_representation` | 上下文依赖形状表示 |
| `tessellated_shape_representation` | 三角化形状表示 |
| `tessellated_shell` | 三角化壳 |
| `tessellated_solid` | 三角化实体 |
| `triangulated_face` | 三角化面 |
| `complex_triangulated_face` | 复杂三角化面 |

---

## 五、XDE (STEPCAFControl) 属性级支持矩阵

| XDE 属性 | 从 STEP 读取 | 写入 STEP | 备注 |
|---|---|---|---|
| 装配结构 | ✅ | ✅ | 完整支持 |
| 产品名称 | ✅ | ✅ | 完整支持 |
| 颜色 (CAX-IF推荐实践) | ✅ | ✅ | 含可见性 |
| 图层 | ✅ | ✅ | 完整支持 |
| 材料 (名称、描述、密度) | ✅ | ✅ | 完整支持 |
| 校验属性 (面积/体积/质心) | ✅ | ✅ | 完整支持 |
| GD&T (AP214基础) | ✅ | ❌ | 仅AP214基础 |
| GD&T (AP242完整+PMI展示) | ✅ | ⚠️ | **需手动设为AP242模式** |
| 保存视图 (投影/裁剪/PMI) | ✅ | ❌ | 仅读取 |
| 用户自定义属性 | ✅ | ✅ | 元数据 |
| 外部引用 (多文件STEP) | ✅ | ❌ | 仅读取 |
| SHUO (特定高层使用关系) | ✅ | ✅ | 完整支持 |

---

## 六、总结

| 维度 | 详情 |
|---|---|
| **总工具包** | TKDESTEP（~2,839个源文件） |
| **总实体类** | ~1,277 个 STEP 实体 |
| **完全支持的协议** | AP203（读写）、AP214（读写，含CD/DIS/IS三个子版本） |
| **部分支持的协议** | AP242（GD&T PMI完整读取，写入需手动切换模式）、AP209（仅读取，1个实体类） |
| **几何与拓扑** | StepGeom(97) + StepShape(105)：覆盖所有主流 B-Rep、曲面、曲线、三角化表示 |
| **产品数据** | StepBasic(148) + StepRepr(85)：完整的产品结构、装配关系、映射变换 |
| **可视化** | StepVisual(143)：颜色、图层、标注、样式 |
| **GD&T** | StepDimTol(62)：完整公差类型覆盖，AP242模式下可写入 |
| **运动学/有限元** | StepKinematics(86) + StepFEA(67) + StepElement(37)：完整支持 |
