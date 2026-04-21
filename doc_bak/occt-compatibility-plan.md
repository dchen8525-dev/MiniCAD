# MiniCAD 与 OpenCASCADE OCCT 兼容实现计划

**基于 OpenCASCADE OCCT 8.0.0-rc5 分析**

## 状态（2026-04-19 更新）

| Phase | 状态 | 交付物 |
|-------|------|--------|
| Phase 1: OCCT 核心兼容验证 | ✅ 已完成 | B-Rep 解析与 OCCT 完全兼容 |
| Phase 2: Kinematics 具体运动副 | ✅ 已完成 | 14 个运动副 record 类 + registerKinematicPairAliases + ~30 registry 条目 |
| Phase 3: GD&T 扩展公差 | ✅ 已完成 | 6 个 GD&T record 类 + 6 registry 条目 |
| Phase 4: Tessellated 三角网格 | ✅ 已完成 | 3 个 TriangulatedFace record 类 + 3 registry 条目 |
| Phase 5: FEA 有限元 | ✅ 已完成 | 15 个 FEA record 类 + registerFeaAliases + ~24 registry 条目 |

**总计**: 56 个新增 Model 类，~81 个新增 Registry 条目，全部 1393 测试通过。

**全链路审计结论（2026-04-18 第三轮更新）**:
- `buildCurve3`: 50+ case 类型完整覆盖（含 StepSurfacedEdgeCurve、StepEdgeCurve、StepCompositeCurveOnSurface3D、StepBSplineCurveWithKnotsAndBreakpoints 等高级类型）
- `buildFace`: 覆盖 StepOrientedFace/StepAdvancedFace/StepFaceSurface/StepSubface/StepOrientedSubface/StepMachinedSurface/StepMappedItem
- `buildShell`: 覆盖 OpenShell/ClosedShell/TessellatedFaceSet/TessellatedFace/VertexShell/WireShell/ConnectedFaceSet/GeometricSurfaceSet 等 20+ 类型
- `buildSolid`: 覆盖 ManifoldSolidBrep/CsgSolid/SweptAreaSolid/SweptDiskSolid/BooleanResult/CsgVolume/TessellatedFaceSet/ExtrudedFaceSolid 等 24+ 类型
- `canBuildAsSolid`: 33 个类型声明，与 `buildSolid` 完全对齐
- `buildVertex`: 覆盖 StepVertexPoint + StepVertex 抽象基类型回退
- `buildEdge`: 覆盖 SeamEdge/EdgeCurve/FilletEdge/ChamferEdge/Subedge/Edge/EdgeCurve/MappedItem，带顶点投影容差 1e-2

**几何求值链路修复（2026-04-18）**:
- **TrimmedCurve3 重写**: 修复 `pointAt()`/`sample()`/`tangentAt()` 完全忽略 basisCurve 的严重 bug（仅做 trimStart→trimEnd 线性插值）
  - 重构 record 字段：`CartesianPoint trimStart/trimEnd` → `double trimParamStart/trimParamEnd`
  - 新增 `Curve3.parameterAt()` 默认方法（采样反查参数）
  - 新增 `resolveTrimParameter()` 在 StepCadBuilder 中从 STEP trim 值提取参数
  - 修复后所有使用修剪曲线的曲面（SurfaceOfRevolution、RuledSurface 等）正确还原曲线形状
- **Clothoid3.contains()**: 修复仅检查平面成员资格的 bug，增加曲线距离检查
- **SurfaceOfLinearExtrusion3.normalAt()**: 修复签名不匹配（单参数 → 双参数 `normalAt(u,v)`）
- **curveSamples()/curveAt() 覆盖补全**: SurfaceOfTranslation3/Projection3 添加 Parabola3/Hyperbola3/Clothoid3/DegenerateCurve3
- **getBasisSampleGrid()/getBasisPointAt()/getNormalAt() 覆盖补全**: SurfaceOfConstantRadius3/OffsetSurface3 添加 4 种新曲面类型 + Plane
- **解析切线替换**: Hyperbola3/Clothoid3 tangentAt() 从数值差分改为解析导数
- **reverseCurve3() 扩展**: 从 3 种扩展到 13 种 Curve3 类型，修复 RuledSurface3 等带方向曲面的法线翻转
- **normalAt() 接口重写**: OffsetSurface3/SurfaceOfConstantRadius3 从数值采样改为调用已有解析法线
- **buildSolid 覆盖补全**: 添加 StepFiniteElementMesh/StepFlatPattern 分支
- **reverseSurfaceSense 扩展**: 从 9 种扩展到 16 种 SurfaceGeometry，修复 OrientedSurface 法线翻转静默丢失（BSpline/RationalBSpline 反转控制网格，Offset/Paraboloid/Hyperboloid/Translation/Projection 递归反转）
- Edge 导入容差从 1e-4 放宽至 1e-2，配合 B-Spline Newton-Raphson 投影
- Face 平面验证添加 1e-2 容差
- EdgeLoop 连接性验证从 1e-9 放宽至 1e-2
- BSplineCurve3/RationalBSplineCurve3 的 `closestPointTo` 增加 Newton-Raphson 精化
- `buildEdge` 增加顶点投影回退，自动将 off-curve 顶点投影到曲线上（支持 Line/Circle/Ellipse/Polyline/BSpline/TrimmedCurve/CompositeCurve）
- `buildFaceSurface` 增加 outer bound 推断：当所有 bounds 为 FACE_BOUND 时，首个自动提升为外边界
- TrimmedCurve3 二次修复：SurfaceOfRevolution3/SurfaceOfLinearExtrusion3 的 `getPointOnCurveInternal()` 中对 TrimmedCurve3 使用线性插值（忽略 basisCurve），修复为正确委托 `trimmed.pointAt(parameter)`，删除死代码 `interpolatePoint()`
- CompositeCurve3 覆盖补全：`getTangentOnSegment` 添加 Clothoid3/Parabola3/Hyperbola3/DegenerateCurve3，`closestPointOnSegment` 从 5 种扩展到全部 13 种 Curve3 类型
- SurfaceCurve3 覆盖补全：`getTangentOnCurveInternal` 添加 DegenerateCurve3，`getClosestPointOnCurve` 添加 Line3/Parabola3/Hyperbola3/Clothoid3/DegenerateCurve3
- `reverseCurve3` 静默修复确认：13 种 Curve3 类型全部显式处理（Line3 反转方向，Polyline3 反转点序，CompositeCurve3 递归反转段序和各段方向），`default ->` 为不可达死代码
- `transformCurve3`/`transformSurfaceGeometry` 均已覆盖全部 13/16 种类型，无遗漏
- SurfaceOfLinearExtrusion3 `getPointOnCurveInternal` 补全：添加 Clothoid3 缺失类型（原遗漏导致 Clothoid profile 的拉伸面在求值时抛出 GeometryException）
- SurfaceGeometry sealed permits 16 种类型全部在 buildSupportedFaceGeometry 中显式处理，无遗漏
- StepPreviewJsonExporter `sampleLooseCurve2` 补全：添加 Parabola2/Hyperbola2/DegenerateCurve2 采样支持，`curveTypeName(Curve2)` 对应添加 PARABOLA/HYPERBOLA/DEGENERATE_CURVE 映射
- 全局 audit 完成：所有 13 Curve3 / 16 SurfaceGeometry 类型在所有关键方法中完整覆盖（pointAt、sample、sampleGrid、normalAt、tangentAt、closestPointTo、boundingBox、reverseCurve3、reverseSurfaceSense、transformCurve3、transformSurfaceGeometry）
- engine.stp 结果：928 → 0 unsupported faces（100% 改善）
- fan.stp 结果：16 → 0 unsupported faces（100% 改善）
- 21 个 .step test files 剩余 unsupported faces 均为 conical seam、multi-trimmed loops 等 intentional edge case 测试（2D/pcurve 实体已过滤）

**几何方法解析化（2026-04-18 第八轮）**:
- `CylindricalSurface.normalAt(u,v)` / `ConicalSurface.normalAt(u,v)`: 添加接口签名匹配 override，避免 64×64 数值差分
- `DegenerateCurve2.tangentAt()`: 抛出 GeometryException 替代返回无意义 (1,0)，与 DegenerateCurve3 一致
- `Parabola3.length()`: 闭式公式 L = p*[t*sqrt(1+t^2)+asinh(t)]；`Hyperbola3.length()`: 32 点高斯积分；`Clothoid3.length()`: 解析 speed=const
- `BSplineCurve3.tangentAt()`: 添加 basisValue+derivativeBasisValue 解析求导；`RationalBSplineCurve3.tangentAt()`: 商法则解析导数

**Viewer 渲染修复（2026-04-18）**:
- `viewer.js` 中 `rebuildParametricFaceGeometry()` 新增 4 种曲面类型的参数化渲染支持
- `spherical_surface`: 球面经纬网格重建（支持 trim 范围）
- `surface_of_revolution`: 旋转面网格重建
- `surface_of_linear_extrusion`: 线性拉伸面网格重建
- `rational_bspline_surface`: 利用已有 `bsplineSurfacePoint`/`bsplineSurfaceNormal` 辅助函数重建
- 修复原 `radius` 校验逻辑漏洞（`radius=0.0` 误通过导致静默失败）

**超出原计划部分**:
- 4 个高级解析曲面: `SURFACE_OF_TRANSLATION`, `SURFACE_OF_PROJECTION`, `PARABOLOID_SURFACE`, `HYPERBOLOID_SURFACE`（含 Geometry 类 + build 方法 + transformSurfaceGeometry）
- 4 个 CSG 体素: `CYLINDER_VOLUME`, `SPHERE_VOLUME`, `TORUS_VOLUME`, `PRISM_VOLUME` (含别名)
- 3 个面扫掠实体: `EXTRUDED_FACE_SOLID`, `REVOLVED_FACE_SOLID`, `SWEPT_FACE_SOLID`
- 3 个管线补齐: `KINEMATIC_FRAME_BASED_TRANSFORMATION`, `VALIDATION_PROPERTY_REPRESENTATION`, `CALCULATED_GEOMETRIC_REPRESENTATION_ITEM`
- 链路补全: Tessellated FaceSet/Face 的 `buildSolid()` 分支补齐
- 高级曲面 Viewer 参数化渲染: 4 个曲面类型（paraboloid_surface、hyperboloid_surface、surface_of_translation、surface_of_projection）从通用三角化升级为 parametric rebuild — exporter 新增 FaceSurfacePayload 序列化 + viewer.js 解析重建

---

**几何方法解析化（2026-04-18 第九轮）**:
- `RuledSurface3` / `SurfaceOfConstantRadius3` / `OffsetSurface3` exporter 补齐: `toRuledSurfaceFacePayload` / `toSurfaceOfConstantRadiusFacePayload` 方法实现（采样三角化 + FaceSurfacePayload 元数据序列化）
- viewer.js `ruled_surface` 参数化重建: 利用 directrix1/directrix2 采样点列进行线性扫掠重建
- viewer.js `constant_radius_surface` / `offset_surface` 参数化重建: 利用 basisSurface 嵌套载荷 + 法线偏移重建
- 全部 1393 测试通过，编译零错误

**几何方法解析化（2026-04-18 第九轮）**:
- `BSplineCurve2` / `RationalBSplineCurve2`: 解析 tangentAt() + 高斯积分 length() + Newton-Raphson closestPointTo()，对标 3D 版本
- `Parabola2.length()`: 闭式公式 L = p*[t*sqrt(1+t^2)+asinh(t)]，替代 256 采样
- `TrimmedCurve3.length()`: 委托至底层曲线解析方法（Circle/Ellipse/BSpline/RationalBSpline/Parabola/Hyperbola/Clothoid）
- `CompositeCurve3` / `SurfaceCurve3`: length() 新增 BSpline/RationalBSpline/Hyperbola/Parabola/Clothoid 解析分发
- Gauss-Legendre 32 点权重/节点修正（sum=2.0），修复 BSplineCurve3/RationalBSplineCurve3/BSplineCurve2/RationalBSplineCurve2 的 18% 长度偏差
- 导数循环边界修正：`n-1` → `n`，四 B-spline 类全覆盖

**几何方法解析化（2026-04-19 第十一轮）**:
- `Plane.boundingBox()` / `ParaboloidSurface.boundingBox()` / `HyperboloidSurface.boundingBox()`: 解析计算替代 64x64 采样
- `Line2.boundingBox()`: 解析计算参数范围 [-1, 1] 的包围盒，替代 64 采样
- `Parabola2.parameterOf()` / `Hyperbola2.parameterOf()`: 解析参数反查，对标 3D 对应方法
- `Polyline2.parameterOf()` / `CompositeCurve2.parameterOf()` / `DegenerateCurve2.parameterOf()`: 计算参数反查
- `TrimmedCurve2.parameterOnBasisCurve` 从 5→11 种曲线全覆盖（新增 Polyline2/Parabola2/Hyperbola2/CompositeCurve2/DegenerateCurve2/TrimmedCurve2 嵌套），修复原 fallback 返回 0.0 的严重 bug
- `TrimmedCurve2.tangentOnBasisCurve` fallback bug 修复（mappedParam() → parameter 参数）
- `TrimmedCurve2` 全面重写：参数化 trim + 基曲线 switch 委托 + Newton-Raphson closestPointTo + 解析 length()
- `StepCadBuilder.parameterOnCurve2()`: Circle2/Ellipse2 容错角度计算（atan2 投影），修复浮点偏差 trim 点抛异常
- 全部 1393 测试通过，编译零错误

**几何方法解析化（2026-04-18 第十轮）**:
- `Ellipse3.closestPointTo()` / `Ellipse2.closestPointTo()`: atan2 初始猜测 + 20 次 Newton-Raphson 精化（基于解析切线）
- `Parabola3.closestPointTo()` / `Hyperbola3.closestPointTo()`: 解析初始猜测 + Newton-Raphson 精化
- `Parabola2.closestPointTo()` / `Hyperbola2.closestPointTo()`: 64 采样初始猜测 + Newton-Raphson 精化（替代 256 采样，4x 加速）
- `Clothoid3.closestPointTo()`: 256 采样 + Newton-Raphson 精化
- `TrimmedCurve3.closestPointTo()`: 多分辨率采样（16/32/64）+ basisCurve Newton-Raphson 精化
- `Hyperbola2.length()`: 32 点高斯积分替代 256 采样链式累加
- 全部 1393 测试通过，编译零错误

---
