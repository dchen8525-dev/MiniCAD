# MiniCAD 与 OpenCASCADE OCCT 兼容实现计划

## Context

**目标**: 使 MiniCAD STEP 解析能力与 OpenCASCADE OCCT 8.0.0-rc3 兼容，确保能够完整解析 OCCT 支持的所有 STEP 实体类型。

**问题**: MiniCAD 当前虽然实体类型注册数量更多（1158 vs OCCT 713），但在 GD&T 扩展公差、Kinematics 运动学具体运动副、FEA 有限元实体等方面不如 OCCT 完整。

**发现**: OCCT 对 CSG Boolean、Swept Solid、Half Space 等实体**仅定义但不求值**，这与 MiniCAD 当前策略一致，无需额外几何求值实现。

---

## 实体类型对比分析

### OCCT 实体类型分布（713 个活跃注册）

| 模块 | 数量 | 主要内容 |
|------|------|----------|
| StepBasic | ~150 | 单位、日期、组织、产品定义 |
| StepGeom | ~90 | 几何实体（曲线、曲面） |
| StepShape | ~80 | 拓扑实体（B-Rep） |
| StepRepr | ~50 | 表示实体 |
| StepVisual | ~70 | 可视化、样式 |
| StepDimTol | ~55 | GD&T 公差实体 |
| StepFEA | ~70 | 有限元分析 |
| StepKinematics | ~65 | 运动学实体 |
| 其他 | ~133 | AP 协议特定、Header 等 |

### MiniCAD vs OCCT 对比

| 指标 | MiniCAD | OCCT |
|------|---------|------|
| 实体类型注册数 | 1158 | 713 |
| 实体模型类数 | 1062 | ~1350 |
| 覆盖 AP242 Ed2 (762种) | ~140% | ~93% |
| AP 协议支持 | AP214/AP242 | AP203/AP214/AP242/AP209 |
| GD&T 支持 | 部分 | 完整 (62种) |
| FEA 支持 | 部分 | 完整 (104种) |
| Kinematics 支持 | 部分 | 完整 (89种) |

### MiniCAD 缺失的关键实体类型

**P0 高优先级（OCCT 核心兼容）**:
- 具体运动副类型：`PRISMATIC_PAIR`, `REVOLUTE_PAIR`, `CYLINDRICAL_PAIR`, `SPHERICAL_PAIR`, `PLANAR_PAIR`, `UNIVERSAL_PAIR`, `SCREW_PAIR`, `GEAR_PAIR`
- 运动副范围：`*_PAIR_WITH_RANGE`, `ACTUATED_KINEMATIC_PAIR`
- 机构状态：`MECHANISM_STATE_REPRESENTATION`
- 三角网格扩展：`TRIANGULATED_FACE`, `COMPLEX_TRIANGULATED_FACE`

**P1 中优先级（GD&T 扩展）**:
- `GEOMETRIC_TOLERANCE_WITH_DEFINED_AREA_UNIT`
- `GEOMETRIC_TOLERANCE_WITH_MAXIMUM_TOLERANCE`
- `NON_UNIFORM_ZONE_DEFINITION`
- `RUNOUT_ZONE_DEFINITION_ORIENTATION`
- `DATUM_REFERENCE_MODIFIER_WITH_VALUE`

**P2 低优先级（FEA 扩展）**:
- `VOLUME_3D_ELEMENT_REPRESENTATION`, `VOLUME_3D_ELEMENT_PROPERTY`
- `FEA_MATERIAL_PROPERTY_REPRESENTATION`
- `ELEMENT_VOLUME_2D`, `ELEMENT_VOLUME_3D`, `NODE_SET`, `ELEMENT_SET`

---

## 实现步骤

### Phase 1: OCCT 核心兼容验证（Week 1-2）

**目标**: 验证现有 B-Rep 解析与 OCCT 完全兼容

**步骤**:
1. 使用 OCCT 导出的 STEP 文件测试 MiniCAD 解析
2. 完善 `StepDatumSystem` 解析逻辑
3. 完善 `StepGeometricToleranceWithDatumReference` 验证
4. 创建兼容性测试用例

**关键文件**:
- `StepEntityResolver.java` - 完善 resolveDatumSystem()
- `StepDatumSystem.java` - 验证字段完整性
- `StepEntityResolverTest.java` - 新增 OCCT 兼容性测试

### Phase 2: Kinematics 具体运动副（Week 3-5）

**目标**: 完整支持 OCCT StepKinematics 模块的具体运动副类型

**步骤**:
1. 创建 `registerKinematicPairAliases()` 辅助方法（复用现有模式）
2. 新增 10 个低阶运动副 record 类
3. 新增 4 个传动副 record 类
4. 新增范围/状态实体
5. 注册所有实体并测试

**新增实体**（约 20 个）:
```
低阶运动副: StepPrismaticPair, StepRevolutePair, StepCylindricalPair,
           StepSphericalPair, StepPlanarPair, StepUniversalPair, StepScrewPair

传动副:     StepGearPair, StepGearPairWithRange, StepRackAndPinionPair

范围实体:   StepLowOrderKinematicPairWithRange, StepActuatedKinematicPair

状态实体:   StepMechanismStateRepresentation, StepKinematicPath
```

**实现模式**（复用 `registerGeometricToleranceAliases`）:
```java
private static void registerKinematicPairAliases(
    Map<String, EntityFactory> registry, String... entityNames) {
  for (String entityName : entityNames) {
    registry.put(entityName,
        (resolver, instance) -> resolver.resolveKinematicPair(instance, entityName));
  }
}
```

### Phase 3: GD&T 扩展公差（Week 6-7）

**目标**: 完整支持 StepDimTol 模块的扩展公差实体

**步骤**:
1. 新增 5 个 GD&T 扩展 record 类
2. 实现 resolve 方法
3. 扩展 `registerGeometricToleranceAliases()` 调用
4. 测试验证

**新增实体**（约 5 个）:
```
StepGeometricToleranceWithDefinedAreaUnit
StepGeometricToleranceWithMaximumTolerance
StepNonUniformZoneDefinition
StepDatumReferenceModifierWithValue
StepRunoutZoneDefinitionOrientation
```

### Phase 4: Tessellated 三角网格（Week 8）

**目标**: 完善 Tessellated 实体解析

**步骤**:
1. 新增 `StepTriangulatedFace` record 类
2. 新增 `StepComplexTriangulatedFace` record 类
3. 实现 resolve 方法
4. 完善 `StepTessellatedFaceSet` 解析

**新增实体**（约 3 个）:
```
StepTriangulatedFace
StepComplexTriangulatedFace
StepCubicBezierTriangulatedFace
```

### Phase 5: FEA 有限元（Week 9-10）

**目标**: 完整支持 StepFEA 模块

**步骤**:
1. 创建 `registerFeaAliases()` 辅助方法
2. 新增 15 个 FEA record 类
3. 实现解析逻辑
4. 测试验证

**新增实体**（约 15 个）:
```
StepVolume3dElementRepresentation, StepVolume3dElementProperty
StepCurve3dElementProperty, StepSurface3dElementProperty
StepFeaMaterialPropertyRepresentation
StepElementVolume2d, StepElementVolume3d
StepNodeSet, StepElementSet
StepFeaSecuredVariable, StepFeaConstantFunction3d
StepFeaLinearAlgebraicMatrix, StepFeaLinearAlgebraicVector
StepFeaAxis2Orientation3d, StepFeaGroupRepresentation
```

---

## 关键文件修改清单

| 文件 | 修改内容 |
|------|----------|
| `StepEntityResolver.java` | 新增 ~40 个 resolve 方法，新增 3 个 registerXxxAliases() 方法，新增 ~50 个 registry.put() |
| `StepEntity.java` | permits 列表新增 ~45 个实体类 |
| `step/model/*.java` | 新增 ~45 个 record 类文件 |

---

## StepEntity permits 列表扩展

在 `StepEntity.java` 第 406 行（`StepTessellatedTriangle` 后）新增：

```java
// Kinematic pair types (Phase 2)
StepPrismaticPair, StepRevolutePair, StepCylindricalPair,
StepSphericalPair, StepPlanarPair, StepUniversalPair, StepScrewPair,
StepGearPair, StepGearPairWithRange, StepRackAndPinionPair,
StepLowOrderKinematicPairWithRange, StepActuatedKinematicPair,
StepMechanismStateRepresentation, StepKinematicPath,
// GD&T extended types (Phase 3)
StepGeometricToleranceWithDefinedAreaUnit,
StepGeometricToleranceWithMaximumTolerance,
StepNonUniformZoneDefinition,
StepDatumReferenceModifierWithValue,
StepRunoutZoneDefinitionOrientation,
// Tessellated extended types (Phase 4)
StepTriangulatedFace, StepComplexTriangulatedFace,
StepCubicBezierTriangulatedFace,
// FEA element types (Phase 5)
StepVolume3dElementRepresentation, StepVolume3dElementProperty,
StepCurve3dElementProperty, StepSurface3dElementProperty,
StepFeaMaterialPropertyRepresentation,
StepElementVolume2d, StepElementVolume3d,
StepNodeSet, StepElementSet,
```

---

## 验证方法

### 测试策略

1. **解析层兼容**: 使用 OCCT 导出的 STEP 文件测试 MiniCAD 解析
2. **实体类型覆盖**: 对比 OCCT RW_xx.hxx 实体列表
3. **单元测试**: 每个新增实体必须有 inline STEP snippet 测试

### 测试用例模板

```java
@Test void resolvePrismaticPair() {
    String stepSnippet = """
        #1 = PRISMATIC_PAIR('name', $, #2, #3, #4);
        #2 = REPRESENTATION_ITEM('axis', 'SHAPE_REPRESENTATION');
        """;
    StepFile file = parse(stepSnippet);
    StepKinematicPair pair = resolver.resolve(file, 1);
    assertThat(pair).isInstanceOf(StepPrismaticPair.class);
}
```

### 兼容性验证工具

建议创建 `OcctCompatibilityChecker.java`:
```java
public class OcctCompatibilityChecker {
    public CompatibilityReport check(String stepFilePath) {
        // 检查所有实体类型是否被 registry 支持
        // 检查 B-Rep 拓扑完整性
        // 输出兼容性报告
    }
}
```

---

## 时间估算

| Phase | 实体数量 | 工作天数 |
|-------|----------|----------|
| Phase 1 | 10 验证 | 10 天 |
| Phase 2 | 20 Kinematic | 15 天 |
| Phase 3 | 5 GD&T | 10 天 |
| Phase 4 | 3 Tessellated | 5 天 |
| Phase 5 | 15 FEA | 10 天 |
| **总计** | **~53 新实体** | **50 天** |

---

## 里程碑

| 里程碑 | 时间 | 交付物 |
|--------|------|--------|
| M1 | Week 2 | OCCT 核心兼容验证完成 |
| M2 | Week 5 | Kinematics 完整支持 |
| M3 | Week 7 | GD&T 扩展完成 |
| M4 | Week 8 | Tessellated 完善 |
| M5 | Week 10 | FEA 完整支持，最终交付 |

---

## OCCT 参考文件路径

| 文件 | 路径 |
|------|------|
| 实体注册协议 | `D:\opencascade\OCCT-8_0_0_rc3\src\DataExchange\TKDESTEP\StepAP214\StepAP214_Protocol.cxx` |
| 头部协议 | `D:\opencascade\OCCT-8_0_0_rc3\src\DataExchange\TKDESTEP\HeaderSection\HeaderSection_Protocol.cxx` |
| StepDimTol 模块 | `D:\opencascade\OCCT-8_0_0_rc3\src\DataExchange\TKDESTEP\StepDimTol\*.hxx` |
| StepFEA 模块 | `D:\opencascade\OCCT-8_0_0_rc3\src\DataExchange\TKDESTEP\StepFEA\*.hxx` |
| StepKinematics 模块 | `D:\opencascade\OCCT-8_0_0_rc3\src\DataExchange\TKDESTEP\StepKinematics\*.hxx` |

---

## OCCT 几何求值限制说明

根据分析，OCCT STEP reader 有以下限制（MiniCAD 可保持相同策略）：

| 类别 | OCCT 行为 | MiniCAD 现状 |
|------|-----------|--------------|
| CSG Boolean | `BooleanResult`, `CsgSolid` 不被 Recognize() 识别 | 仅解析，不求值 |
| Swept Solid | `ExtrudedAreaSolid`, `RevolvedAreaSolid` 不被直接转换 | 仅解析，不求值 |
| Half Space | `HalfSpaceSolid` 不被处理 | 仅解析，不求值 |
| 显式 B-Rep | `ManifoldSolidBrep`, `FacetedBrep` 完全支持 | 完整解析和求值 |