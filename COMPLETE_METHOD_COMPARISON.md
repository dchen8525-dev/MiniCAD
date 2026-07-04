# 完整方法对照表 - 所有6个提取文件

## 📊 总结

**总重复方法**: 190个
**提取文件**: 6个
**主文件**: StepPreviewJsonExporter.java (18094行，352个方法)

---

## 📋 文件1: PreviewSurfaceSampler.java

**信息**: 280行，5个方法，100%重复

| # | 方法名 | 提取文件行号 | 主文件行号 |
|---|--------|-------------|-----------|
| 1 | `buildBsplineSurface` | 38 | 3721 |
| 2 | `buildFreeFormSurface` | 70 | 3753 |
| 3 | `triangulatePatch` | 108 | 4258 |
| 4 | `triangulateSurfaceGrid` | 127 | 4277 |
| 5 | `buildFourSidedPatch` | 154 | 6672 |

**操作**: 右键类名 → Refactor → Inline
**预计时间**: 5-10分钟
**难度**: ⭐ 最简单

---

## 📋 文件2: PreviewEdgeSampler.java

**信息**: 733行，4个方法，75%重复（3个重复）

| # | 方法名 | 提取文件行号 | 主文件行号 |
|---|--------|-------------|-----------|
| 1 | `curveForLooseEdge` | 130 | 8395 |
| 2 | `collectMappedAnnotationEdges` | 350 | 1535 |
| 3 | `collectMappedAnnotationCarrierEdges` | 379 | 1564 |

**注意**: 有1个方法不重复（`sampleLooseEdgePoints`）
**操作**: 右键类名 → Refactor → Inline
**预计时间**: 10-15分钟
**难度**: ⭐⭐ 较简单

---

## 📋 文件3: PreviewPmiBuilder.java

**信息**: 1673行，46个方法，100%重复

| # | 方法名 | 提取文件行号 | 主文件行号 |
|---|--------|-------------|-----------|
| 1 | `isSupportedPmiUsageCarrier` | 72 | 10561 |
| 2 | `toStandalonePointPmi` | 93 | 10545 |
| 3 | `toStandalonePointPmi` | 97 | 10545 |
| 4 | `appendPointSetPmi` | 117 | 10367 |
| 5 | `appendGeometricMeasurementPmi` | 132 | 10382 |
| 6 | `appendFillAreaWithOutlinePmi` | 154 | 10404 |
| 7 | `appendGeometricTolerancePmi` | 172 | 10422 |
| 8 | `appendGeometricToleranceWithDatumPmi` | 187 | 10437 |
| 9 | `appendGeometricToleranceWithAreaUnitPmi` | 202 | 10452 |
| 10 | `appendGeometricToleranceWithMaxPmi` | 217 | 10467 |
| 11 | `appendDimensionalLocationPmi` | 235 | 10485 |
| 12 | `appendToleranceZonePmi` | 247 | 10497 |
| 13 | `appendDatumPmi` | 267 | 10517 |
| 14 | `appendDatumTargetPmi` | 279 | 10529 |
| 15 | `appendPlaceholderPmi` | 291 | 10284 |
| 16 | `appendAnnotationPlanePmi` | 314 | 10307 |
| 17 | `appendAnnotationOccurrenceRelationshipPmi` | 348 | 10341 |
| 18 | `appendDraughtingAnnotationPmi` | 362 | 10355 |
| 19 | `appendPmiLeader` | 378 | 11340 |
| 20 | `appendPmiLeaderForSolid` | 714 | 11310 |
| ... | **还有26个方法** | ... | ... |

**操作**: 右键类名 → Refactor → Inline
**预计时间**: 15-20分钟
**难度**: ⭐⭐⭐ 中等

---

## 📋 文件4: PreviewCurveEvaluator.java

**信息**: 1734行，48个方法，96%重复（46个重复）

| # | 方法名 | 提取文件行号 | 主文件行号 |
|---|--------|-------------|-----------|
| 1 | `curveEvaluator` | 72 | 6258 |
| 2 | `sampledCurveEvaluator` | 315 | 6499 |
| 3 | `closestParameter` | 341 | 6523 |
| 4 | `radialComponent` | 381 | 6645 |
| 5 | `fallbackNormal` | 386 | 6650 |
| 6 | `unwrapPeriodic` | 395 | 6659 |
| 7 | `liftCurve2` | 446 | 8733 |
| 8 | `sampleLooseCurve2` | 455 | 8742 |
| 9 | `sampleTrimmedCurve2` | 513 | 8800 |
| 10 | `sampleCircle2Points` | 536 | 8952 |
| 11 | `sampleEllipse2Points` | 544 | 8960 |
| 12 | `curveTypeName` | 554 | 8915 |
| 13 | `curveTypeName` | 571 | 8915 |
| 14 | `sampleConicCurvePoints` | 588 | 8614 |
| 15 | `sampleEdge` | 717 | 9113 |
| 16 | `sampleTrimmedCurve3` | 834 | 9231 |
| 17 | `nearestPointIndex` | 857 | 9252 |
| 18 | `appendClosedTrimmedPoints` | 870 | 9265 |
| 19 | `appendOpenTrimmedPoints` | 885 | 9280 |
| 20 | `addDistinctPoint` | 902 | 9297 |
| ... | **还有26个方法** | ... | ... |

**注意**: 有2个方法不重复
**操作**: 右键类名 → Refactor → Inline
**预计时间**: 15-20分钟
**难度**: ⭐⭐⭐ 中等

---

## 📋 文件5: PreviewUvMapper.java

**信息**: 2286行，63个方法，100%重复

| # | 方法名 | 提取文件行号 | 主文件行号 |
|---|--------|-------------|-----------|
| 1 | `mapperForSurface` | 52 | 5473 |
| 2 | `extrusionMapper` | 634 | 6157 |
| 3 | `revolutionMapper` | 670 | 6193 |
| 4 | `nearestUvOnBSplineSurface` | 737 | 6090 |
| 5 | `nearestUvOnRationalBSplineSurface` | 800 | 7321 |
| 6 | `clamp` | 897 | 6153 |
| 7 | `buildParametricLoops` | 903 | 4338 |
| 8 | `buildParametricLoops` | 931 | 4338 |
| 9 | `normalizePeriodicLoop` | 975 | 4389 |
| 10 | `boundsOf` | 1016 | 4428 |
| 11 | `withSurfaceSourceMetadata` | 1037 | 4681 |
| 12 | `basisDirectionForNormal` | 1118 | 4760 |
| 13 | `shouldFallbackToProjectedEdge` | 1282 | 4917 |
| 14 | `associatedGeometrySummary` | 1295 | 4949 |
| 15 | `unwrapAssociatedCurveGeometry` | 1317 | 4969 |
| 16 | `pcurveBasisSurfaceSummary` | 1367 | 5017 |
| 17 | `matchingPcurves` | 1385 | 5033 |
| 18 | `acceptablePcurveBasisSurfaceIds` | 1402 | 5048 |
| 19 | `snapToLine` | 1439 | 5083 |
| 20 | `snapToCircle` | 1444 | 5088 |
| ... | **还有43个方法** | ... | ... |

**⚠️ 注意**: 包含内部类
- `ParametricSurfaceMapper`
- `UvPoint`

**操作**: 右键类名 → Refactor → Inline（IDEA会自动处理内部类）
**预计时间**: 20-25分钟
**难度**: ⭐⭐⭐⭐ 复杂

---

## 📋 文件6: PreviewFaceBuilder.java

**信息**: 2829行，56个方法，80%重复（45个重复）

| # | 方法名 | 提取文件行号 | 主文件行号 |
|---|--------|-------------|-----------|
| 1 | `buildFaceBounds` | 68 | 6991 |
| 2 | `faceGeometry` | 77 | 7000 |
| 3 | `faceSameSense` | 93 | 7377 |
| 4 | `reverseFacePayload` | 110 | 7394 |
| 5 | `toUnsupportedFacePayload` | 135 | 2593 |
| 6 | `unwrapParametricPreviewSurface` | 147 | 2016 |
| 7 | `describeUnsupportedPreviewSurface` | 210 | 2083 |
| 8 | `describeUnsupportedPreviewSurface` | 214 | 2083 |
| 9 | `toCylindricalFacePayload` | 275 | 3302 |
| 10 | `toConicalFacePayload` | 368 | 3392 |
| 11 | `toToroidalFacePayload` | 527 | 3478 |
| 12 | `toRationalBSplineSurfaceFacePayload` | 626 | 3790 |
| 13 | `toRuledSurfaceFacePayload` | 666 | 3874 |
| 14 | `toFourSidedPatchFacePayload` | 711 | 3830 |
| 15 | `buildLegacyGeometry` | 1144 | 810 |
| 16 | `buildGeometryForShells` | 1230 | 896 |
| 17 | `buildGeometryForSolids` | 1309 | 975 |
| 18 | `mergeGeometry` | 1369 | 1035 |
| 19 | `collectShellLikeIds` | 1379 | 1045 |
| 20 | `collectStandaloneEdges` | 1434 | 1102 |
| ... | **还有25个方法** | ... | ... |

**⚠️ 注意**:
- 有11个方法不重复
- 依赖最多，与其他文件有交叉引用

**操作**: 右键类名 → Refactor → Inline
**预计时间**: 25-30分钟
**难度**: ⭐⭐⭐⭐⭐ 最复杂

---

## 🎯 统计总结

| 文件 | 行数 | 方法数 | 重复数 | 重复率 | 难度 |
|------|------|--------|--------|--------|------|
| PreviewSurfaceSampler | 280 | 5 | 5 | 100% | ⭐ |
| PreviewEdgeSampler | 733 | 4 | 3 | 75% | ⭐⭐ |
| PreviewPmiBuilder | 1673 | 46 | 46 | 100% | ⭐⭐⭐ |
| PreviewCurveEvaluator | 1734 | 48 | 46 | 96% | ⭐⭐⭐ |
| PreviewUvMapper | 2286 | 63 | 63 | 100% | ⭐⭐⭐⭐ |
| PreviewFaceBuilder | 2829 | 56 | 45 | 80% | ⭐⭐⭐⭐⭐ |
| **总计** | **9534** | **222** | **190** | **85%** | |

---

## 📝 IDEA通用操作步骤

适用于所有6个文件：

### 步骤1: 打开提取文件
```
在IDEA中:
  导航到 src/main/java/com/minicad/app/
  打开 Preview*.java
```

### 步骤2: 执行Inline
```
右键类名 → Refactor → Inline (Ctrl+Alt+N)
选择: "Inline all methods and remove the class"
```

### 步骤3: 检查预览
```
确认:
  ✅ 所有方法调用被替换
  ✅ 提取文件被删除
  ✅ 主文件方法保留
```

### 步骤4: 执行重构
```
点击 "Do Refactor"
IDEA自动完成所有操作
```

### 步骤5: 验证
```bash
mvn clean compile -DskipTests
mvn test
```

### 步骤6: 提交
```bash
git add -A
git commit -m "Inline PreviewXXX - Remove XXX lines"
```

---

## ⚠️ 关键注意事项

### 1. 内部类处理（PreviewUvMapper.java）

IDEA会提示如何处理内部类：
- 选择"保留主文件中的版本"
- 或选择"合并"

### 2. 方法重名（多个toStandalonePointPmi）

IDEA会自动处理，保留主文件版本。

### 3. 不重复的方法（11个）

IDEA会从提取文件复制到主文件：
- `sampleLooseEdgePoints` (PreviewEdgeSampler)
- 其他不重复的方法

---

## ✅ 每次验证清单

- [ ] 提取文件已删除
- [ ] 主文件方法存在
- [ ] 编译成功 (`mvn clean compile`)
- [ ] 测试通过 (`mvn test`)
- [ ] Git已提交

---

## 🎯 推荐顺序

从简单到复杂：

1. ✅ **PreviewSurfaceSampler** (280行) - ⭐ 最简单
2. ⏳ **PreviewEdgeSampler** (733行) - ⭐⭐ 较简单
3. ⏳ **PreviewPmiBuilder** (1673行) - ⭐⭐⭐ 中等
4. ⏳ **PreviewCurveEvaluator** (1734行) - ⭐⭐⭐ 中等
5. ⏳ **PreviewUvMapper** (2286行) - ⭐⭐⭐⭐ 复杂（内部类）
6. ⏳ **PreviewFaceBuilder** (2829行) - ⭐⭐⭐⭐⭐ 最复杂

---

## 🚀 立即开始

**第一步**: 打开 `PreviewSurfaceSampler.java`
**参考**: 此对照表第1部分

按照步骤操作，5-10分钟完成第一个文件！

---

**总预计时间**: 90-120分钟
**预期减少**: 9534行代码
**最终目标**: 主文件从18094行 → ~8500行