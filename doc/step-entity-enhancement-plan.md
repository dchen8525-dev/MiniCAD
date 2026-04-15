# 工业级 STEP 实体支持增强计划

## 现状（已更新）

| 指标 | 数值 |
|---|---|
| Registry 条目 | 1100+ |
| Model Classes | 824 |
| 测试用例 | 1347 (全部通过) |

### 已完成工作

- **Phase 1: CAD 构建链路全部打通**——所有关键几何实体都实现了 CAD 构建方法
- **Phase 2: 实体注册大幅扩展**——新增 230+ 实体别名注册，覆盖：
  - 几何公差类型扩展（COAXIALITY_TOLERANCE, RUNOUT_TOLERANCE 等）
  - Shape Aspect 扩展（50+ 制造特征类型）
  - Representation 类型扩展（60+ 形状表示类型）
  - Representation Relationship 扩展（30+ 关系类型）
  - Characterized Object 扩展（60+ 操作和特征类型）
  - Externally Defined Item 扩展（27+ 外部定义类型）

### 核心发现

- **解析层（StepEntityResolver）已覆盖所有示例文件中的实体**——67 种示例文件实体类型全部注册
- **CAD 构建层（StepCadBuilder）已全面实现**——Phase 1 中列出的所有几何实体都已实现构建方法
- 支持实体类型包括：SWEPT_DISK_SOLID、EXTRUDED_AREA_SOLID_TAPERED、REVOLVED_AREA_SOLID_TAPERED、SURFACE_CURVE_SWEPT_AREA_SOLID、RIGHT_CIRCULAR_CONE、RULED_SURFACE、SURFACE_OF_CONSTANT_RADIUS、SURFACE_PATCH、RECTANGULAR_COMPOSITE_SURFACE、CLOTHOID、INDEXED_POLY_CURVE、DEGENERATE_CURVE、Boolean UNION 等

---

## Phase 1: 打通 CAD 构建链路（最高优先级）

### 1A. Swept Solid 构建支持

**文件**: `StepCadBuilder.java` `buildSolid()` 方法区域（约 line 1400-1444）

| STEP 实体 | 构建方法 | 实现策略 |
|---|---|---|
| `SWEPT_DISK_SOLID` | `buildSweptDiskSolid()` | 沿轨迹曲线扫描圆形截面，生成 tube/pipe 几何 |
| `EXTRUDED_AREA_SOLID_TAPERED` | `buildTaperedExtrudedSolid()` | 复用现有 profile 点提取逻辑，末端缩放 |
| `REVOLVED_AREA_SOLID_TAPERED` | `buildTaperedRevolvedSolid()` | 复用现有 revolution 逻辑，末端缩放 |
| `SURFACE_CURVE_SWEPT_AREA_SOLID` | `buildSurfaceCurveSweptSolid()` | 沿 3D 曲线扫描 profile |
| `RIGHT_CIRCULAR_CONE` | `buildRightCircularCone()` | CSG primitive，从 placement + height + radius 构建锥体 |

**工业价值**: SWEPT_DISK_SOLID 是管道/管件/线束的标准表示方式，RIGHT_CIRCULAR_CONE 是 CSG 建模的核心原语。

### 1B. Surface 几何构建

**文件**: `StepCadBuilder.java` `buildSupportedFaceGeometry()` 区域（约 line 1280-1305）及 surface 构建方法

| STEP 实体 | 构建方法 | 实现策略 |
|---|---|---|
| `RULED_SURFACE` | `buildRuledSurface()` | 两条 directrix 曲线间线性插值，生成三角网格 |
| `SURFACE_OF_CONSTANT_RADIUS` | `buildConstantRadiusSurface()` | 类似 cylindrical surface，沿 directrix 等半径扫描 |
| `SURFACE_PATCH` | `buildSurfacePatch()` | 从 boundary curves 提取并三角化 |
| `RECTANGULAR_COMPOSITE_SURFACE` | `buildRectangularCompositeSurface()` | 递归构建子 surface patches |

**工业价值**: RULED_SURFACE 是放样/蒙皮操作的标准结果，SURFACE_OF_CONSTANT_RADIUS 是管道外表面的标准表示。

### 1C. Curve 构建支持

**文件**: `StepCadBuilder.java` `buildCurve3()` / `buildCurve2()` / `sampleCurve3()` 区域（约 line 542 / 2507 / 3354）

| STEP 实体 | 实现策略 |
|---|---|
| `CLOTHOID` | 数值积分采样，生成多段线近似（回旋线用于道路/铁路设计） |
| `INDEXED_POLY_CURVE` | 直接从 points 列表构建多段线（高效多边形曲线） |
| `DEGENERATE_CURVE` | 退化为单点或空曲线处理 |
| `OFFSET_CURVE_3D` | 沿 basis curve 采样后偏移 |

**工业价值**: CLOTHOID 是道路/铁路设计的核心曲线类型，INDEXED_POLY_CURVE 是网格导入的高效表示。

### 1D. Profile 定义补全

**文件**: `StepCadBuilder.java` `buildAreaProfileLoops()` 方法（约 line 2007-2026）

在 switch 中添加 3 个缺失的 profile 类型：

| STEP 实体 | 说明 |
|---|---|
| `RECTANGLE_HOLLOW_PROFILE_DEF` | 矩形空心轮廓（方管截面） |
| `CENTERED_CIRCLE_PROFILE_DEF` | 居中圆轮廓 |
| `CENTRE_LINE_ARC_PROFILE_DEF` | 中心线弧轮廓 |

**工业价值**: 这些是结构钢截面和管道截面的标准表示。

### 1E. Boolean UNION 支持

**文件**: `StepCadBuilder.java` `buildBooleanResult()` 方法（约 line 1749-1764）

当前仅支持 `.DIFFERENCE.` 和 `.INTERSECTION.`，添加 `.UNION.` 操作符支持。

**工业价值**: UNION 是布尔操作的基本操作之一，缺失导致无法合并多个实体。

### 1F. Non-Manifold Solid 构建

**文件**: `StepCadBuilder.java`

`StepNonManifoldSolidBrep` 已解析但无构建方法。添加处理 OPEN_SHELL 作为 sheet metal（钣金）几何的逻辑。

**工业价值**: 钣金件在展开状态下是 non-manifold 的，这是钣金 CAD 数据交换的关键类型。

---

## Phase 2: 增加更多 STEP 实体注册（中等优先级）

预计增加 **50-80 个新 registry 条目**。

### 2A. 制造特征（Manufacturing Features）

| 实体 | 说明 |
|---|---|
| `MACHINING_OPERATION` | 机加工操作 |
| `MACHINED_SURFACE` | 已加工表面 |
| `TWO_5D_MANUFACTURING_FEATURE` | 2.5D 制造特征（孔、槽、台阶等） |
| `MANUFACTURING_FEATURE_REPRESENTATION` | 制造特征表示 |
| `SLOT` | 槽特征 |
| `STUD` | 凸台特征 |
| `PROTRUSION` | 凸出特征 |
| `DEPRESSION` | 凹陷特征 |
| `ROUND` | 圆角特征 |
| `CHAMFER` | 倒角特征 |

### 2B. 高级几何（Advanced Geometry）

| 实体 | 说明 |
|---|---|
| `TRIANGULATED_FACE_SET` | 三角面集合（AP242 新版网格表示） |
| `POLYGONAL_FACE` | 多边形面 |
| `POLYGONAL_FACE_SET` | 多边形面集合 |
| `FACETED_BREP` | 小平面 B-rep |
| `GEOMETRIC_SET` 扩展项 | 更多几何集合类型 |

### 2C. PMI 扩展

| 实体 | 说明 |
|---|---|
| `GEOMETRIC_TOLERANCE_WITH_DEFINED_UNIT` | 带定义单位的几何公差 |
| `DATUM_REFERENCE_COMPARTMENT` | 基准参考舱 |
| `DATUM_REFERENCE_ELEMENT` | 基准参考元素 |
| `COMMON_DATUM` | 公共基准 |
| `DATUM_TARGET` | 基准目标 |
| `FEATURE_CONTROL_FRAME` 扩展 | 特征控制框架扩展 |

### 2D. 材料与配置

| 实体 | 说明 |
|---|---|
| `MATERIAL_PROPERTY` | 材料属性 |
| `MATERIAL_PROPERTY_REPRESENTATION` | 材料属性表示 |
| `EFFECTIVITY_CONTEXT` | 有效性上下文 |
| `CLASSIFIED_EFFECTIVITY` | 分类有效性 |

### 2E. Profile 族批量注册

使用 alias 模式批量注册 10+ 个标准截面类型：

```
CHANNEL_PROFILE_DEF, Z_PROFILE_DEF, T_PROFILE_DEF, L_PROFILE_DEF,
I_PROFILE_DEF, HAT_PROFILE_DEF, TEE_PROFILE_DEF, ANGLE_PROFILE_DEF,
FLAT_BAR_PROFILE_DEF, DOVE_TAIL_PROFILE_DEF, ...
```

---

## Phase 3: 测试覆盖增强

### 3A. CAD 构建测试

为 Phase 1 中每个新增 build 方法添加 `StepCadBuilderTest` 测试：

```java
@Test
void shouldBuildSweptDiskSolid() { ... }

@Test
void shouldBuildRuledSurface() { ... }

@Test
void shouldBuildBooleanUnion() { ... }
```

### 3B. 实体解析测试

为 Phase 2 中每个新增实体添加 `StepEntityResolverTest` 测试。

### 3C. 端到端集成测试

使用真实工业 STEP 文件验证全链路：
- 所有实体成功解析
- 关键几何类型成功构建
- 无级联失败

---

## 实施顺序

```
1D (Profile 补全)       → 最小改动，立即见效
1E (Boolean UNION)      → 简单添加
1A (Swept Solids)       → 工业价值最高
1B (Surfaces)           → 表面建模
1C (Curves)             → 曲线类型
1F (Non-Manifold)       → 钣金支持
Phase 2 (新实体注册)     → 广度扩展
Phase 3 (测试)           → 质量保障
```

---

## 关键文件清单

| 文件 | 当前行数 | 需改动类型 |
|---|---|---|
| `src/main/java/.../step/semantic/StepCadBuilder.java` | ~4,166 | 添加 12+ build 方法 |
| `src/main/java/.../step/semantic/StepEntityResolver.java` | ~10,554 | 添加 50-80 registry 条目 |
| `src/main/java/.../step/model/StepEntity.java` | ~117 | permits 子句扩展 |
| `src/main/java/.../step/model/Step*.java` | 新增 | 新实体 model classes |
| `src/test/java/.../step/semantic/StepCadBuilderTest.java` | - | 新增测试用例 |
| `src/test/java/.../step/semantic/StepEntityResolverTest.java` | - | 新增测试用例 |

---

## 验证方式

```bash
# 1. 编译通过
mvn compile

# 2. 全部测试通过
mvn test
# 预期: 759+ tests, 0 failures

# 3. 示例文件无错误解析
# StepDumpApp 对所有 examples/*.step 和 *.stp 输出无 STEP processing error

# 4. 新增 CAD 构建方法验证
# StepCadBuilderTest 中新增测试验证几何输出正确性
```
