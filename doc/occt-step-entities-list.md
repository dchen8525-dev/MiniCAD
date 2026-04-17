# OpenCASCADE OCCT STEP 实体类型完整清单

## OCCT 支持的 STEP 应用协议 (AP)

| 协议 | Schema 标识 | 说明 |
|------|-------------|------|
| **AP203** | `CONFIG_CONTROL_DESIGN` | 配置控制设计（航空航天） |
| **AP214 CC2** | `AUTOMOTIVE_DESIGN_CC2 { 1 2 10303 214 -1 1 5 4 }` | 汽车设计 CC2 版本 |
| **AP214 DIS** | `AUTOMOTIVE_DESIGN { 1 2 10303 214 0 1 1 1 }` | 汽车设计 DIS 版本 |
| **AP214 IS** | `AUTOMOTIVE_DESIGN { 1 0 10303 214 1 1 1 1 }` | 汽车设计 IS 版本 |
| **AP242 DIS** | `AP242_MANAGED_MODEL_BASED_3D_ENGINEERING_MIM_LF {1 0 10303 442 1 1 4 }` | 管理模型工程 |
| **AP209** | （通过 StepElement/StepFEA 模块） | 有限元分析 |

---

## 模块实体类型数量统计

| 模块 | 头文件数 | 注册实体数 | 主要内容 |
|------|----------|------------|----------|
| **StepVisual** | 179 | ~70 | 可视化、样式、颜色、标注、网格化 |
| **StepBasic** | 168 | ~150 | 基础数据、单位、日期、组织、产品定义 |
| **StepShape** | 127 | ~105 | 拓扑实体（B-Rep、实体、面、边、壳） |
| **StepGeom** | 115 | ~90 | 几何实体（曲线、曲面、B样条、变换） |
| **StepRepr** | 97 | ~85 | 表示实体（形状表示、映射项、材料） |
| **StepFEA** | 87 | ~67 | 有限元分析（节点、单元、材料属性） |
| **StepKinematics** | 86 | ~65 | 运动学（运动副、机构表示） |
| **StepAP214** | 80 | ~46 | AP214 特有实体 |
| **StepDimTol** | 76 | ~55 | GD&T 公差实体 |
| **StepElement** | 67 | ~37 | 有限元元素描述符 |
| **StepAP203** | 41 | ~21 | AP203 特有实体 |
| **StepAP242** | 6 | ~12 | AP242 特有实体 |
| **总计** | **~1000** | **713** | 活跃注册实体 |

---

## 各模块实体类型详细清单

### 1. StepBasic（基础实体，~150 个）

| 编号 | 实体类型 | 功能 |
|------|----------|------|
| 1 | Address | 地址 |
| 13 | ApplicationContext | 应用上下文 |
| 14 | ApplicationContextElement | 应用上下文元素 |
| 15 | ApplicationProtocolDefinition | 应用协议定义 |
| 16 | Approval | 审批 |
| 18 | ApprovalPersonOrganization | 审批人员组织 |
| 19 | ApprovalRelationship | 审批关系 |
| 20 | ApprovalRole | 审批角色 |
| 21 | ApprovalStatus | 审批状态 |
| 53 | CalendarDate | 日历日期 |
| 79 | ConversionBasedUnit | 转换基单位 |
| 80 | CoordinatedUniversalTimeOffset | UTC偏移 |
| 91 | Date | 日期 |
| 92 | DateAndTime | 日期时间 |
| 95 | DateRole | 日期角色 |
| 96 | DateTimeRole | 日期时间角色 |
| 104 | DimensionalExponents | 量纲指数 |
| 150 | Group | 组 |
| 152 | GroupRelationship | 组关系 |
| 157 | LengthMeasureWithUnit | 长度测量带单位 |
| 158 | LengthUnit | 长度单位 |
| 169 | NamedUnit | 命名单位 |
| 176 | Organization | 组织 |
| 178 | OrganizationRole | 组织角色 |
| 179 | OrganizationalAddress | 组织地址 |
| 191 | Person | 人员 |
| 192 | PersonAndOrganization | 人员和组织 |
| 194 | PersonAndOrganizationRole | 人员组织角色 |
| 195 | PersonalAddress | 个人地址 |
| 200 | PlaneAngleMeasureWithUnit | 平面角测量带单位 |
| 201 | PlaneAngleUnit | 平面角单位 |
| 223 | Product | 产品 |
| 224 | ProductCategory | 产品类别 |
| 225 | ProductContext | 产品上下文 |
| 227 | ProductDefinition | 产品定义 |
| 228 | ProductDefinitionContext | 产品定义上下文 |
| 229 | ProductDefinitionFormation | 产品定义形成 |
| 230 | ProductDefinitionFormationWithSpecifiedSource | 指定来源的产品定义形成 |
| 232 | ProductRelatedProductCategory | 产品相关产品类别 |
| 233 | ProductType | 产品类型 |
| 255 | SecurityClassification | 安全分类 |
| 257 | SecurityClassificationLevel | 安全分类级别 |
| 264 | SiUnit | SI单位 |
| 265 | SolidAngleMeasureWithUnit | 立体角测量带单位 |
| 310 | UncertaintyMeasureWithUnit | 不确定度测量带单位 |
| 318 | WeekOfYearAndDayDate | 年周日日期 |
| 339 | MechanicalContext | 机械上下文 |
| 340 | DesignContext | 设计上下文 |
| 341-347 | 时间单位相关 | TimeMeasureWithUnit, RatioUnit, TimeUnit 等 |
| 348 | ApprovalDateTime | 审批日期时间 |
| 352 | DerivedUnit | 导出单位 |
| 353 | DerivedUnitElement | 导出单位元素 |
| 367-371 | 文档相关 | Document, DigitalDocument, DocumentRelationship, DocumentType, DocumentUsageConstraint |
| 372 | Effectivity | 有效性 |
| 373 | ProductDefinitionEffectivity | 产品定义有效性 |
| 374 | ProductDefinitionRelationship | 产品定义关系 |
| 401 | DocumentFile | 文档文件 |
| 402 | CharacterizedObject | 特性化对象 |
| 407-412 | 面积体积单位 | AreaUnit, VolumeUnit, SiUnitAndAreaUnit 等 |
| 413-440 | AP203基础实体 | Action, ActionMethod, Certification, Contract 等 |
| 450-462 | 外部引用 | DocumentRepresentationType, ObjectRole, RoleAssociation, IdentificationRole 等 |
| 500-502 | AP209基础 | EulerAngles, MassUnit, ThermodynamicTemperatureUnit |
| 574,578 | 质量温度单位 | SiUnitAndMassUnit, SiUnitAndThermodynamicTemperatureUnit |
| 600-601 | 文档产品关联 | DocumentProductAssociation, DocumentProductEquivalence |
| 650-651 | 质量测量 | ConversionBasedUnitAndMassUnit, MassMeasureWithUnit |
| 820-821 | 通用属性 | GeneralPropertyAssociation, GeneralPropertyRelationship |

### 2. StepGeom（几何实体，~90 个）

| 编号 | 实体类型 | 功能 |
|------|----------|------|
| 35 | Axis1Placement | 单轴定位 |
| 36 | Axis2Placement2d | 二维双轴定位 |
| 37 | Axis2Placement3d | 三维双轴定位 |
| 38 | BSplineCurve | B样条曲线 |
| 39 | BSplineCurveWithKnots | 带节点B样条曲线 |
| 40 | BSplineSurface | B样条曲面 |
| 41 | BSplineSurfaceWithKnots | 带节点B样条曲面 |
| 43 | BezierCurve | Bezier曲线 |
| 44 | BezierSurface | Bezier曲面 |
| 47 | BoundaryCurve | 边界曲线 |
| 48 | BoundedCurve | 有界曲线 |
| 49 | BoundedSurface | 有界曲面 |
| 59 | CartesianPoint | 笛卡尔点 |
| 60 | CartesianTransformationOperator | 笛卡尔变换算子 |
| 61 | CartesianTransformationOperator3d | 三维笛卡尔变换算子 |
| 62 | Circle | 圆 |
| 67 | CompositeCurve | 复合曲线 |
| 68 | CompositeCurveOnSurface | 曲面上复合曲线 |
| 69 | CompositeCurveSegment | 复合曲线段 |
| 74 | Conic | 圆锥曲线 |
| 75 | ConicalSurface | 圆锥面 |
| 84 | Curve | 曲线 |
| 85 | CurveBoundedSurface | 曲线有界曲面 |
| 86 | CurveReplica | 曲线副本 |
| 90 | CylindricalSurface | 圆柱面 |
| 99 | DegeneratePcurve | 退化参数曲线 |
| 100 | DegenerateToroidalSurface | 退化环面 |
| 105 | Direction | 方向 |
| 118 | ElementarySurface | 基本曲面 |
| 119 | Ellipse | 椭圆 |
| 120 | EvaluatedDegeneratePcurve | 计算退化参数曲线 |
| 154 | Hyperbola | 双曲线 |
| 155 | IntersectionCurve | 交线 |
| 159 | Line | 直线 |
| 171 | OffsetCurve3d | 三维偏移曲线 |
| 172 | OffsetSurface | 偏移曲面 |
| 185 | OuterBoundaryCurve | 外边界曲线 |
| 187 | Parabola | 抛物线 |
| 196 | Placement | 定位 |
| 199 | Plane | 平面 |
| 202 | Point | 点 |
| 203 | PointOnCurve | 曲线上点 |
| 204 | PointOnSurface | 曲面上点 |
| 205 | PointReplica | 点副本 |
| 208 | Polyline | 多义线 |
| 236 | QuasiUniformCurve | 准均匀曲线 |
| 237 | QuasiUniformSurface | 准均匀曲面 |
| 239 | RationalBSplineCurve | 有理B样条曲线 |
| 240 | RationalBSplineSurface | 有理B样条曲面 |
| 241 | RectangularCompositeSurface | 矩形复合曲面 |
| 242 | RectangularTrimmedSurface | 矩形修剪曲面 |
| 244 | ReparametrisedCompositeCurveSegment | 重参数化复合曲线段 |
| 254 | SeamCurve | 接缝曲线 |
| 269 | SphericalSurface | 球面 |
| 271 | Surface | 曲面 |
| 272 | SurfaceCurve | 曲面曲线 |
| 273 | SurfaceOfLinearExtrusion | 线性拉伸曲面 |
| 274 | SurfaceOfRevolution | 旋转曲面 |
| 275 | SurfacePatch | 曲面片 |
| 276 | SurfaceReplica | 曲面副本 |
| 305 | ToroidalSurface | 环面 |
| 308 | TrimmedCurve | 修剪曲线 |
| 311 | UniformCurve | 均匀曲线 |
| 312 | UniformSurface | 均匀曲面 |
| 313 | Vector | 向量 |
| 319-326 | 组合B样条类型 | 各种曲线曲面组合类型 |
| 327-330,334-336 | 单位组合类型 | SiUnitAndLengthUnit, SiUnitAndPlaneAngleUnit 等 |
| 331-333,338 | 表示上下文组合 | GeometricRepresentationContextAndGlobalUnitAssignedContext 等 |
| 351 | CartesianTransformationOperator2d | 二维笛卡尔变换算子 |
| 358 | SurfaceCurveAndBoundedCurve | 曲面曲线和有界曲线组合 |
| 492 | OrientedSurface | 方向曲面 |
| 726 | SuParameters | SU参数 |

### 3. StepShape（拓扑实体，~105 个）

| 编号 | 实体类型 | 功能 |
|------|----------|------|
| 2 | AdvancedBrepShapeRepresentation | 高级B-Rep形状表示 |
| 3 | AdvancedFace | 高级面 |
| 45 | Block | 块（CSG原语） |
| 46 | BooleanResult | 布尔结果 |
| 50 | BoxDomain | 盒域 |
| 51 | BoxedHalfSpace | 盒式半空间 |
| 52 | BrepWithVoids | 带空腔B-Rep |
| 63 | ClosedShell | 闭壳 |
| 76 | ConnectedFaceSet | 连接面集 |
| 82 | CsgShapeRepresentation | CSG形状表示 |
| 83 | CsgSolid | CSG实体 |
| 115 | Edge | 边 |
| 116 | EdgeCurve | 边曲线 |
| 117 | EdgeLoop | 边环 |
| 128 | ExtrudedAreaSolid | 拉伸面积实体 |
| 129 | Face | 面 |
| 131 | FaceBound | 面边界 |
| 132 | FaceOuterBound | 面外边界 |
| 133 | FaceSurface | 面曲面 |
| 134 | FacetedBrep | 简化B-Rep |
| 135 | FacetedBrepShapeRepresentation | 简化B-Rep形状表示 |
| 142 | GeometricCurveSet | 几何曲线集 |
| 145 | GeometricSet | 几何集 |
| 146 | GeometricallyBoundedSurfaceShapeRepresentation | 几何有界曲面形状表示 |
| 147 | GeometricallyBoundedWireframeShapeRepresentation | 几何有界线框形状表示 |
| 153 | HalfSpaceSolid | 半空间实体 |
| 161 | Loop | 环 |
| 162 | ManifoldSolidBrep | 流形实体B-Rep |
| 163 | ManifoldSurfaceShapeRepresentation | 流形曲面形状表示 |
| 174 | OpenShell | 开壳 |
| 180 | OrientedClosedShell | 方向闭壳 |
| 181 | OrientedEdge | 方向边 |
| 182 | OrientedFace | 方向面 |
| 183 | OrientedOpenShell | 方向开壳 |
| 184 | OrientedPath | 方向路径 |
| 189 | Path | 路径 |
| 207 | PolyLoop | 多边形环 |
| 250 | RevolvedAreaSolid | 旋转面积实体 |
| 251 | RightAngularWedge | 直角楔 |
| 252 | RightCircularCone | 正圆锥 |
| 253 | RightCircularCylinder | 正圆柱 |
| 261 | ShapeDefinitionRepresentation | 形状定义表示 |
| 262 | ShapeRepresentation | 形状表示 |
| 263 | ShellBasedSurfaceModel | 基壳曲面模型 |
| 266 | SolidModel | 实体模型 |
| 267 | SolidReplica | 实体副本 |
| 268 | Sphere | 球（CSG原语） |
| 285 | SweptAreaSolid | 扫掠面积实体 |
| 286 | SweptSurface | 扫掠曲面 |
| 304 | TopologicalRepresentationItem | 拓扑表示项 |
| 306 | Torus | 环（CSG原语） |
| 307 | TransitionalShapeRepresentation | 过渡形状表示 |
| 314 | Vertex | 顶点 |
| 315 | VertexLoop | 顶点环 |
| 316 | VertexPoint | 顶点点 |
| 332 | LoopAndPath | 环和路径组合 |
| 337 | FacetedBrepAndBrepWithVoids | 简化B-Rep和带空腔B-Rep组合 |
| 391 | ContextDependentShapeRepresentation | 上下文相关形状表示 |
| 403 | ExtrudedFaceSolid | 拉伸面实体 |
| 404 | RevolvedFaceSolid | 旋转面实体 |
| 405 | SweptFaceSolid | 扫掠面实体 |
| 442-449 | 尺寸位置 | AngularLocation, AngularSize, DimensionalLocation, DimensionalSize 等 |
| 463 | DefinitionalRepresentationAndShapeRepresentation | 定义表示和形状表示组合 |
| 470-484 | 形状方面 | CompositeShapeAspect, DerivedShapeAspect, Extension 等 |
| 485-496 | 边框模型 | CompoundShapeRepresentation, EdgeBasedWireframeModel, FaceBasedSurfaceModel 等 |
| 493-496 | 子拓扑 | Subface, Subedge, SeamEdge, ConnectedFaceSubSet |
| 568 | PointRepresentation | 点表示 |
| 610 | ShapeRepresentationWithParameters | 带参数形状表示 |

### 4. StepRepr（表示实体，~85 个）

| 编号 | 实体类型 | 功能 |
|------|----------|------|
| 98 | DefinitionalRepresentation | 定义表示 |
| 101 | DescriptiveRepresentationItem | 描述表示项 |
| 141 | FunctionallyDefinedTransformation | 函数定义变换 |
| 148 | GlobalUncertaintyAssignedContext | 全局不确定度分配上下文 |
| 149 | GlobalUnitAssignedContext | 全局单位分配上下文 |
| 164 | MappedItem | 映射项 |
| 188 | ParametricRepresentationContext | 参数表示上下文 |
| 231 | ProductDefinitionShape | 产品定义形状 |
| 234 | PropertyDefinition | 属性定义 |
| 235 | PropertyDefinitionRepresentation | 属性定义表示 |
| 245 | Representation | 表示 |
| 246 | RepresentationContext | 表示上下文 |
| 247 | RepresentationItem | 表示项 |
| 248 | RepresentationMap | 表示映射 |
| 249 | RepresentationRelationship | 表示关系 |
| 258 | ShapeAspect | 形状方面 |
| 259 | ShapeAspectRelationship | 形状方面关系 |
| 260 | ShapeAspectTransition | 形状方面过渡 |
| 354 | ItemDefinedTransformation | 项定义变换 |
| 377 | ProductDefinitionUsage | 产品定义用法 |
| 378 | MakeFromUsageOption | 制造来源用法选项 |
| 379 | AssemblyComponentUsage | 装配组件用法 |
| 380 | NextAssemblyUsageOccurrence | 下一装配用法出现 |
| 381 | PromissoryUsageOccurrence | 承诺用法出现 |
| 382 | QuantifiedAssemblyComponentUsage | 定量装配组件用法 |
| 383 | SpecifiedHigherUsageOccurrence | 指定更高用法出现 |
| 384 | AssemblyComponentUsageSubstitute | 装配组件用法替代 |
| 385 | SuppliedPartRelationship | 供应零件关系 |
| 386 | ExternallyDefinedRepresentation | 外部定义表示 |
| 387 | ShapeRepresentationRelationship | 形状表示关系 |
| 388 | RepresentationRelationshipWithTransformation | 带变换表示关系 |
| 389 | ShapeRepresentationRelationshipWithTransformation | 带变换形状表示关系 |
| 390 | MaterialDesignation | 材料标识 |
| 406 | MeasureRepresentationItem | 测量表示项 |
| 429 | ConfigurationDesign | 配置设计 |
| 430 | ConfigurationEffectivity | 配置有效性 |
| 434 | ProductConcept | 产品概念 |
| 482 | CompoundRepresentationItem | 复合表示项 |
| 483 | ValueRange | 值范围 |
| 484 | ShapeAspectDerivingRelationship | 形状方面派生关系 |
| 565 | DataEnvironment | 数据环境 |
| 566 | MaterialPropertyRepresentation | 材料属性表示 |
| 567 | PropertyDefinitionRelationship | 属性定义关系 |
| 569 | MaterialProperty | 材料属性 |
| 576 | StructuralResponseProperty | 结构响应属性 |
| 577 | StructuralResponsePropertyDefinitionRepresentation | 结构响应属性定义表示 |
| 635 | ReprItemAndLengthMeasureWithUnit | 表示项和长度测量带单位 |
| 660-665 | 几何关系 | Apex, CentreOfSymmetry, GeometricAlignment, PerpendicularTo, Tangent, ParallelOffset |
| 669-672 | 形状方面扩展 | AllAroundShapeAspect, BetweenShapeAspect, CompositeGroupShapeAspect, ContinuosShapeAspect |
| 691-696 | 公差组合类型 | ReprItemAndPlaneAngleMeasureWithUnit, 各种几何公差组合类型 |
| 698-699 | 形状方面组合 | CompGroupShAspAndCompShAspAndDatumFeatAndShAsp 等 |
| 700-701 | 值表示项 | IntegerRepresentationItem, ValueRepresentationItem |
| 702 | FeatureForDatumTargetRelationship | 基准目标特征关系 |
| 712-714 | 构造几何 | ConstructiveGeometryRepresentation, ConstructiveGeometryRepresentationRelationship, CharacterizedRepresentation |
| 724-725 | 表示引用 | RepresentationContextReference, RepresentationReference |
| 822-824 | 表示项扩展 | BooleanRepresentationItem, RealRepresentationItem, MechanicalDesignAndDraughtingRelationship |

### 5. StepVisual（可视化实体，~70 个）

| 编号 | 实体类型 | 功能 |
|------|----------|------|
| 4-12 | 标注实体 | AnnotationCurveOccurrence, AnnotationFillArea, AnnotationOccurrence, AnnotationText 等 |
| 22 | AreaInSet | 区域在集合中 |
| 42 | BackgroundColour | 背景颜色 |
| 54 | CameraImage | 相机图像 |
| 55 | CameraModel | 相机模型 |
| 56 | CameraModelD2 | 二维相机模型 |
| 57 | CameraModelD3 | 三维相机模型 |
| 58 | CameraUsage | 相机用法 |
| 64-66 | 颜色 | Colour, ColourRgb, ColourSpecification |
| 70,73 | 复合文本 | CompositeText, CompositeTextWithExtent |
| 87-89 | 曲线样式 | CurveStyle, CurveStyleFont, CurveStyleFontPattern |
| 106-109 | 绘图 | DraughtingAnnotationOccurrence, DraughtingCallout, DraughtingPreDefinedColour, DraughtingPreDefinedCurveFont |
| 122 | ExternallyDefinedCurveFont | 外部定义曲线字体 |
| 126 | ExternallyDefinedTextFont | 外部定义文本字体 |
| 136-137 | 填充样式 | FillAreaStyle, FillAreaStyleColour |
| 156 | Invisibility | 不可见 |
| 166 | MechanicalDesignGeometricPresentationArea | 机械设计几何表示区域 |
| 167 | MechanicalDesignGeometricPresentationRepresentation | 机械设计几何表示表示 |
| 206 | PointStyle | 点样式 |
| 209-213 | 预定义 | PreDefinedColour, PreDefinedCurveFont, PreDefinedItem, PreDefinedTextFont |
| 214-221 | 表示层 | PresentationArea, PresentationLayerAssignment, PresentationRepresentation, PresentationSet 等 |
| 270 | StyledItem | 样式项 |
| 277-284 | 曲面样式 | SurfaceSideStyle, SurfaceStyleBoundary, SurfaceStyleFillArea, SurfaceStyleUsage 等 |
| 292-293 | 模板 | Template, TemplateInstance |
| 295 | TextLiteral | 文本字面 |
| 300-302 | 文本样式 | TextStyle, TextStyleForDefinedFont, TextStyleWithBoxCharacteristics |
| 317 | ViewVolume | 视图体积 |
| 349-350 | 相机图像缩放 | CameraImage2dWithScale, CameraImage3dWithScale |
| 355 | PresentedItemRepresentation | 呈现项表示 |
| 356 | PresentationLayerUsage | 表示层用法 |
| 441 | DraughtingModel | 绘图模型 |
| 651 | DraughtingCallout | 绘图标注 |
| 704 | AnnotationPlane | 标注平面 |
| 707-711 | 网格化 | TessellatedAnnotationOccurrence, TessellatedItem, TessellatedGeometricSet, TessellatedCurveSet, CoordinatesList |
| 715-719 | 相机裁剪 | CharacterizedObjAndRepresentationAndDraughtingModel, CameraModelD3MultiClipping 等 |
| 720-723 | 表面渲染 | SurfaceStyleTransparent, SurfaceStyleReflectanceAmbient, SurfaceStyleRendering, SurfaceStyleRenderingWithProperties |
| 802-813 | 网格化几何 | RepositionedTessellatedGeometricSet, TessellatedEdge, TessellatedShapeRepresentation 等 |
| 814-819 | 三角网格 | TriangulatedFace, ComplexTriangulatedFace, CubicBezierTriangulatedFace 等 |
| 825-826 | 反射 | SurfaceStyleReflectanceAmbientDiffuse, SurfaceStyleReflectanceAmbientDiffuseSpecular |

### 6. StepDimTol（GD&T公差实体，~55 个）

| 编号 | 实体类型 | 功能 |
|------|----------|------|
| 609 | CylindricityTolerance | 圆柱度公差 |
| 611 | AngularityTolerance | 角度公差 |
| 612 | ConcentricityTolerance | 同轴度公差 |
| 613 | CircularRunoutTolerance | 圆跳动公差 |
| 614 | CoaxialityTolerance | 同轴度公差 |
| 615 | FlatnessTolerance | 平面度公差 |
| 616 | LineProfileTolerance | 线轮廓度公差 |
| 617 | ParallelismTolerance | 平行度公差 |
| 618 | PerpendicularityTolerance | 垂直度公差 |
| 619 | PositionTolerance | 位置公差 |
| 620 | RoundnessTolerance | 圆度公差 |
| 621 | StraightnessTolerance | 直线度公差 |
| 622 | SurfaceProfileTolerance | 面轮廓度公差 |
| 623 | SymmetryTolerance | 对称度公差 |
| 624 | TotalRunoutTolerance | 全跳动公差 |
| 625 | GeometricTolerance | 几何公差 |
| 626 | GeometricToleranceRelationship | 几何公差关系 |
| 627 | GeometricToleranceWithDatumReference | 带基准几何公差 |
| 628 | ModifiedGeometricTolerance | 修改几何公差 |
| 629 | Datum | 基准 |
| 630 | DatumFeature | 基准特征 |
| 631 | DatumReference | 基准引用 |
| 632 | CommonDatum | 公共基准 |
| 633 | DatumTarget | 基准目标 |
| 634 | PlacedDatumTargetFeature | 放置基准目标特征 |
| 636 | GeoTolAndGeoTolWthDatRefAndModGeoTolAndPosTol | 公差组合类型 |
| 673 | GeometricToleranceWithDefinedAreaUnit | 定义区域单位几何公差 |
| 674 | GeometricToleranceWithDefinedUnit | 定义单位几何公差 |
| 675 | GeometricToleranceWithMaximumTolerance | 最大公差几何公差 |
| 676 | GeometricToleranceWithModifiers | 带修饰符几何公差 |
| 677 | UnequallyDisposedGeometricTolerance | 不等分布几何公差 |
| 678 | NonUniformZoneDefinition | 非均匀区域定义 |
| 679 | ProjectedZoneDefinition | 投影区域定义 |
| 680 | RunoutZoneDefinition | 跳动区域定义 |
| 681 | RunoutZoneOrientation | 跳动区域方向 |
| 682 | ToleranceZone | 公差区域 |
| 683 | ToleranceZoneDefinition | 公差区域定义 |
| 684 | ToleranceZoneForm | 公差区域形式 |
| 686 | DatumReferenceCompartment | 基准引用分区 |
| 687 | DatumReferenceElement | 基准引用元素 |
| 688 | DatumReferenceModifierWithValue | 基准引用修饰符带值 |
| 689 | DatumSystem | 基准系统 |
| 690 | GeneralDatumReference | 通用基准引用 |
| 694-697 | 公差组合类型 | 各种几何公差组合类型 |
| 705-706 | 最大公差组合 | GeoTolAndGeoTolWthDatRefAndGeoTolWthMaxTol, GeoTolAndGeoTolWthMaxTol |

### 7. StepKinematics（运动学实体，~65 个）

| 编号 | 实体类型 | 功能 |
|------|----------|------|
| 727 | RotationAboutDirection | 方向旋转 |
| 728 | KinematicJoint | 运动关节 |
| 729 | ActuatedKinematicPair | 驱动运动副 |
| 730 | ContextDependentKinematicLinkRepresentation | 上下文相关运动链表示 |
| 731 | CylindricalPair | 圆柱副 |
| 732 | CylindricalPairValue | 圆柱副值 |
| 733 | CylindricalPairWithRange | 带范围圆柱副 |
| 734 | FullyConstrainedPair | 全约束副 |
| 735 | GearPair | 齿轮副 |
| 736 | GearPairValue | 齿轮副值 |
| 737 | GearPairWithRange | 带范围齿轮副 |
| 738 | HomokineticPair | 等速副 |
| 739 | KinematicLink | 运动链 |
| 740 | KinematicLinkRepresentationAssociation | 运动链表示关联 |
| 741 | KinematicPropertyMechanismRepresentation | 运动属性机构表示 |
| 742 | KinematicTopologyStructure | 运动拓扑结构 |
| 743 | LowOrderKinematicPair | 低阶运动副 |
| 744 | LowOrderKinematicPairValue | 低阶运动副值 |
| 745 | LowOrderKinematicPairWithRange | 带范围低阶运动副 |
| 746 | MechanismRepresentation | 机构表示 |
| 747 | OrientedJoint | 方向关节 |
| 748 | PlanarCurvePair | 平面曲线副 |
| 749 | PlanarCurvePairRange | 平面曲线副范围 |
| 750 | PlanarPair | 平面副 |
| 751 | PlanarPairValue | 平面副值 |
| 752 | PlanarPairWithRange | 带范围平面副 |
| 753 | PointOnPlanarCurvePair | 平面曲线上点副 |
| 754 | PointOnPlanarCurvePairValue | 平面曲线上点副值 |
| 755 | PointOnPlanarCurvePairWithRange | 带范围平面曲线上点副 |
| 756 | PointOnSurfacePair | 曲面上点副 |
| 757 | PointOnSurfacePairValue | 曲面上点副值 |
| 758 | PointOnSurfacePairWithRange | 带范围曲面上点副 |
| 759 | PrismaticPair | 移动副 |
| 760 | PrismaticPairValue | 移动副值 |
| 761 | PrismaticPairWithRange | 带范围移动副 |
| 762 | ProductDefinitionKinematics | 产品定义运动学 |
| 763 | ProductDefinitionRelationshipKinematics | 产品定义关系运动学 |
| 764 | RackAndPinionPair | 齿轮齿条副 |
| 765 | RackAndPinionPairValue | 齿轮齿条副值 |
| 766 | RackAndPinionPairWithRange | 带范围齿轮齿条副 |
| 767 | RevolutePair | 旋转副 |
| 768 | RevolutePairValue | 旋转副值 |
| 769 | RevolutePairWithRange | 带范围旋转副 |
| 770 | RollingCurvePair | 滚动曲线副 |
| 771 | RollingCurvePairValue | 滚动曲线副值 |
| 772 | RollingSurfacePair | 滚动曲面副 |
| 773 | RollingSurfacePairValue | 滚动曲面副值 |
| 774 | ScrewPair | 螺旋副 |
| 775 | ScrewPairValue | 螺旋副值 |
| 776 | ScrewPairWithRange | 带范围螺旋副 |
| 777 | SlidingCurvePair | 滑动曲线副 |
| 778 | SlidingCurvePairValue | 滑动曲线副值 |
| 779 | SlidingSurfacePair | 滑动曲面副 |
| 780 | SlidingSurfacePairValue | 滑动曲面副值 |
| 781 | SphericalPair | 球面副 |
| 782 | SphericalPairValue | 球面副值 |
| 783 | SphericalPairWithPin | 销球面副 |
| 784 | SphericalPairWithPinAndRange | 带范围带销球面副 |
| 785 | SphericalPairWithRange | 带范围球面副 |
| 786 | SurfacePairWithRange | 带范围曲面副 |
| 787 | UnconstrainedPair | 无约束副 |
| 788 | UnconstrainedPairValue | 无约束副值 |
| 789 | UniversalPair | 万向副 |
| 790 | UniversalPairValue | 万向副值 |
| 791 | UniversalPairWithRange | 带范围万向副 |
| 792 | PairRepresentationRelationship | 运动副表示关系 |
| 793 | RigidLinkRepresentation | 刚性链表示 |
| 794 | KinematicTopologyDirectedStructure | 运动拓扑定向结构 |
| 795 | KinematicTopologyNetworkStructure | 运动拓扑网络结构 |
| 796 | LinearFlexibleAndPinionPair | 线性柔性齿轮副 |
| 797 | LinearFlexibleAndPlanarCurvePair | 线性柔性平面曲线副 |
| 798 | LinearFlexibleLinkRepresentation | 线性柔性链表示 |
| 800 | ActuatedKinPairAndOrderKinPair | 驱动运动副和低阶运动副组合 |
| 801 | MechanismStateRepresentation | 机构状态表示 |

### 8. StepFEA（有限元分析实体，~67 个）

| 编号 | 实体类型 | 功能 |
|------|----------|------|
| 518 | AlignedCurve3dElementCoordinateSystem | 对齐三维曲线单元坐标系 |
| 519 | ArbitraryVolume3dElementCoordinateSystem | 任意三维体积单元坐标系 |
| 520 | Curve3dElementProperty | 三维曲线单元属性 |
| 521 | Curve3dElementRepresentation | 三维曲线单元表示 |
| 522 | Node | 节点 |
| 524 | CurveElementEndOffset | 曲线单元端偏移 |
| 525 | CurveElementEndRelease | 曲线单元端释放 |
| 526 | CurveElementInterval | 曲线单元间隔 |
| 527 | CurveElementIntervalConstant | 曲线单元间隔常数 |
| 528 | DummyNode | 假节点 |
| 529 | CurveElementLocation | 曲线单元位置 |
| 530 | ElementGeometricRelationship | 单元几何关系 |
| 531 | ElementGroup | 单元组 |
| 532 | ElementRepresentation | 单元表示 |
| 533 | FeaAreaDensity | FEA面积密度 |
| 534 | FeaAxis2Placement3d | FEA三维轴定位 |
| 535 | FeaGroup | FEA组 |
| 536 | FeaLinearElasticity | FEA线性弹性 |
| 537 | FeaMassDensity | FEA质量密度 |
| 538 | FeaMaterialPropertyRepresentation | FEA材料属性表示 |
| 539 | FeaMaterialPropertyRepresentationItem | FEA材料属性表示项 |
| 540 | FeaModel | FEA模型 |
| 541 | FeaModel3d | FEA三维模型 |
| 542 | FeaMoistureAbsorption | FEA吸湿 |
| 543 | FeaParametricPoint | FEA参数点 |
| 544 | FeaRepresentationItem | FEA表示项 |
| 545 | FeaSecantCoefficientOfLinearThermalExpansion | FEA割线线性热膨胀系数 |
| 546 | FeaShellBendingStiffness | FEA壳弯曲刚度 |
| 547 | FeaShellMembraneBendingCouplingStiffness | FEA壳膜弯曲耦合刚度 |
| 548 | FeaShellMembraneStiffness | FEA壳膜刚度 |
| 549 | FeaShellShearStiffness | FEA壳剪切刚度 |
| 550 | GeometricNode | 几何节点 |
| 551 | FeaTangentialCoefficientOfLinearThermalExpansion | FEA切线线性热膨胀系数 |
| 552 | NodeGroup | 节点组 |
| 553 | NodeRepresentation | 节点表示 |
| 554 | NodeSet | 节点集 |
| 555 | NodeWithSolutionCoordinateSystem | 带解坐标系节点 |
| 556 | NodeWithVector | 带向量节点 |
| 557 | ParametricCurve3dElementCoordinateDirection | 参数三维曲线单元坐标方向 |
| 558 | ParametricCurve3dElementCoordinateSystem | 参数三维曲线单元坐标系 |
| 559 | ParametricSurface3dElementCoordinateSystem | 参数三维曲面单元坐标系 |
| 560 | Surface3dElementRepresentation | 三维曲面单元表示 |
| 564 | Volume3dElementRepresentation | 三维体积单元表示 |
| 570 | FeaModelDefinition | FEA模型定义 |
| 571 | FreedomAndCoefficient | 自由度和系数 |
| 572 | FreedomsList | 自由度列表 |
| 575 | NodeDefinition | 节点定义 |
| 579 | AlignedSurface3dElementCoordinateSystem | 对齐三维曲面单元坐标系 |
| 580 | ConstantSurface3dElementCoordinateSystem | 常数三维曲面单元坐标系 |
| 581 | CurveElementIntervalLinearlyVarying | 曲线单元间隔线性变化 |
| 582 | FeaCurveSectionGeometricRelationship | FEA曲线截面几何关系 |
| 583 | FeaSurfaceSectionGeometricRelationship | FEA曲面截面几何关系 |

### 9. StepElement（有限元元素描述，~37 个）

| 编号 | 实体类型 | 功能 |
|------|----------|------|
| 503 | AnalysisItemWithinRepresentation | 表示内分析项 |
| 504 | Curve3dElementDescriptor | 三维曲线单元描述符 |
| 505 | CurveElementEndReleasePacket | 曲线单元端释放包 |
| 506 | CurveElementSectionDefinition | 曲线单元截面定义 |
| 507 | CurveElementSectionDerivedDefinitions | 曲线单元截面派生定义 |
| 508 | ElementDescriptor | 单元描述符 |
| 509 | ElementMaterial | 单元材料 |
| 510 | Surface3dElementDescriptor | 三维曲面单元描述符 |
| 511 | SurfaceElementProperty | 曲面单元属性 |
| 512 | SurfaceSection | 曲面截面 |
| 513 | SurfaceSectionField | 曲面截面场 |
| 514 | SurfaceSectionFieldConstant | 曲面截面场常数 |
| 515 | SurfaceSectionFieldVarying | 曲面截面场变化 |
| 516 | UniformSurfaceSection | 均匀曲面截面 |
| 517 | Volume3dElementDescriptor | 三维体积单元描述符 |

### 10. StepAP214（AP214特有实体，~46 个）

| 编号 | 实体类型 | 功能 |
|------|----------|------|
| 23 | AutoDesignActualDateAndTimeAssignment | 自动设计实际日期时间分配 |
| 24 | AutoDesignActualDateAssignment | 自动设计实际日期分配 |
| 25 | AutoDesignApprovalAssignment | 自动设计审批分配 |
| 26 | AutoDesignDateAndPersonAssignment | 自动设计日期人员分配 |
| 27 | AutoDesignGroupAssignment | 自动设计组分配 |
| 28 | AutoDesignNominalDateAndTimeAssignment | 自动设计名义日期时间分配 |
| 29 | AutoDesignNominalDateAssignment | 自动设计名义日期分配 |
| 30 | AutoDesignOrganizationAssignment | 自动设计组织分配 |
| 31 | AutoDesignPersonAndOrganizationAssignment | 自动设计人员组织分配 |
| 32 | AutoDesignPresentedItem | 自动设计呈现项 |
| 33 | AutoDesignSecurityClassificationAssignment | 自动设计安全分类分配 |
| 366 | AutoDesignDocumentReference | 自动设计文档引用 |
| 392-399 | Applied系列 | AppliedDateAndTimeAssignment, AppliedApprovalAssignment, AppliedGroupAssignment 等 |
| 400 | AppliedDocumentReference | 应用文档引用 |
| 462 | AppliedExternalIdentificationAssignment | 应用外部标识分配 |
| 243 | RepItemGroup | 表示项组 |
| 459 | Class | 类 |
| 460 | ExternallyDefinedClass | 外部定义类 |
| 461 | ExternallyDefinedGeneralProperty | 外部定义通用属性 |

### 11. StepAP203（AP203特有实体，~21 个）

| 编号 | 实体类型 | 功能 |
|------|----------|------|
| 417 | CcDesignApproval | CC设计审批 |
| 418 | CcDesignCertification | CC设计认证 |
| 419 | CcDesignContract | CC设计合同 |
| 420 | CcDesignDateAndTimeAssignment | CC设计日期时间分配 |
| 421 | CcDesignPersonAndOrganizationAssignment | CC设计人员组织分配 |
| 422 | CcDesignSecurityClassification | CC设计安全分类 |
| 423 | CcDesignSpecificationReference | CC设计规范引用 |
| 427 | Change | 变更 |
| 428 | ChangeRequest | 变更请求 |
| 436 | StartRequest | 开始请求 |
| 437 | StartWork | 开始工作 |

### 12. StepAP242（AP242特有实体，~12 个）

| 编号 | 实体类型 | 功能 |
|------|----------|------|
| 666 | GeometricItemSpecificUsage | 几何项特定用法 |
| 667 | IdAttribute | ID属性 |
| 668 | ItemIdentifiedRepresentationUsage | 项标识表示用法 |
| 703 | DraughtingModelItemAssociation | 绘图模型项关联 |

### 13. HeaderSection（文件头实体，~4 个）

| 实体类型 | 功能 |
|----------|------|
| FileDescription | 文件描述 |
| FileName | 文件名 |
| FileSchema | 文件模式 |
| SchemaIdentification | 模式标识 |

---

## 注释掉（未启用）的实体类型

以下实体在代码中被注释掉，表示在某些 AP 版本中不支持：

| 类别 | 实体类型 | 原因 |
|------|----------|------|
| 标注（CC1-Rev2移除） | AnnotationSubfigureOccurrence, AnnotationSymbol, AnnotationSymbolOccurrence | 版本演进移除 |
| 文本样式（CC1-Rev2移除） | CompositeTextWithAssociatedCurves, CompositeTextWithBlankingBox | 版本演进移除 |
| 填充样式（CC1-Rev2移除） | FillAreaStyleHatching, FillAreaStyleTiles, FillAreaStyleTileSymbolWithStyle | 版本演进移除 |
| 绘图（CC1-Rev2移除） | DraughtingSubfigureRepresentation, DraughtingSymbolRepresentation | 版本演进移除 |
| 符号（CC1-Rev2移除） | DefinedSymbol, PreDefinedSymbol, SymbolColour, SymbolStyle | 版本演进移除 |
| CSG（CC1-Rev2移除） | CsgRepresentation | 被 CsgShapeRepresentation 替代 |
| 其他 | AutoDesignViewArea, OneDirectionRepeatFactor, TwoDirectionRepeatFactor 等 | 版本演进移除 |

---

## 总结

OCCT 8.0.0-rc3 支持 **713 个活跃注册的 STEP 实体类型**，覆盖：
- AP203（配置控制设计）
- AP214（汽车设计，多版本）
- AP242（管理模型工程）
- AP209（有限元分析，通过 StepFEA/StepElement）

各模块实体类型数量分布合理，几何/拓扑核心模块（StepGeom + StepShape）覆盖约 200 个实体，满足主流 CAD 数据交换需求。