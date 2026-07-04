# 重构总览 - 6个提取文件集成计划

## 📊 当前状态

**Git分支**: `refactor/preview-integration`
**准备状态**: ✅ 完成
**开始时间**: 2026-07-04

---

## 🎯 目标

将6个提取的Preview*文件集成回主文件，删除重复代码：

```
主文件: StepPreviewJsonExporter.java
  当前: 18094行
  目标: ~8500行
  减少: ~9534行 (52.6%)
```

---

## 📋 提取文件清单

按难度排序（从简单到复杂）：

### ✅ 已集成

| # | 文件名 | 行数 | 状态 | 说明 |
|---|--------|------|------|------|
| 0 | PreviewSerializers.java | - | ✅ 已集成 | 已在主文件中使用（8处调用） |

### ⏳ 待集成

| # | 文件名 | 行数 | 方法数 | 重复率 | 难度 | 预计时间 |
|---|--------|------|--------|--------|------|----------|
| 1 | **PreviewSurfaceSampler.java** | **280** | **5** | **100%** | ⭐ 最简单 | **5-10分钟** |
| 2 | PreviewEdgeSampler.java | 733 | 4 | 75% | ⭐⭐ 较简单 | 10-15分钟 |
| 3 | PreviewPmiBuilder.java | 1673 | 46 | 100% | ⭐⭐⭐ 中等 | 15-20分钟 |
| 4 | PreviewCurveEvaluator.java | 1734 | 48 | 96% | ⭐⭐⭐ 中等 | 15-20分钟 |
| 5 | PreviewUvMapper.java | 2286 | 63 | 100% | ⭐⭐⭐⭐ 复杂 | 20-25分钟 |
| 6 | PreviewFaceBuilder.java | 2829 | 56 | 80% | ⭐⭐⭐⭐⭐ 最复杂 | 25-30分钟 |
| **总计** | **6个文件** | **9534** | **222** | **85%平均** | | **90-120分钟** |

---

## 🔧 集成方法

使用IntelliJ IDEA的 **Inline Refactor** 功能：

```
右键类名 → Refactor → Inline (Ctrl+Alt+N)
选择: "Inline all methods and remove the class"
```

**IDEA会自动处理**:
- ✅ 删除提取文件
- ✅ 合并方法到主文件
- ✅ 更新所有调用
- ✅ 处理依赖关系
- ✅ 解决类型冲突
- ✅ 修复访问权限

---

## 📝 操作顺序

### 第1个: PreviewSurfaceSampler.java ⭐

**推荐理由**: 最简单，只有5个方法，280行

**详细步骤**: 请查看 `INTEGRATION_STEP_1.md`

**方法列表**:
- `buildBsplineSurface`
- `buildFreeFormSurface`
- `triangulatePatch`
- `triangulateSurfaceGrid`
- `buildFourSidedPatch`

**预计时间**: 5-10分钟

---

### 第2个: PreviewEdgeSampler.java ⭐⭐

**文件信息**: 733行，4个方法，75%重复

**方法列表**:
- `curveForLooseEdge`
- `sampleLooseEdgePoints`
- `collectMappedAnnotationEdges`
- `collectMappedAnnotationCarrierEdges`

**预计时间**: 10-15分钟

---

### 第3个: PreviewPmiBuilder.java ⭐⭐⭐

**文件信息**: 1673行，46个方法，100%重复

**特点**: PMI相关方法较多，需注意方法依赖

**预计时间**: 15-20分钟

---

### 第4个: PreviewCurveEvaluator.java ⭐⭐⭐

**文件信息**: 1734行，48个方法，96%重复

**特点**: 曲线评估方法，可能有内部类依赖

**预计时间**: 15-20分钟

---

### 第5个: PreviewUvMapper.java ⭐⭐⭐⭐

**文件信息**: 2286行，63个方法，100%重复

**特点**:
- ⚠️ 有内部类 `ParametricSurfaceMapper`
- ⚠️ 有 `UvPoint` 类型
- ⚠️ 方法依赖复杂

**预计时间**: 20-25分钟

---

### 第6个: PreviewFaceBuilder.java ⭐⭐⭐⭐⭐

**文件信息**: 2829行，56个方法，80%重复

**特点**:
- ⚠️ 方法最多
- ⚠️ 依赖最多
- ⚠️ 与其他提取文件有交叉引用

**预计时间**: 25-30分钟

---

## ✅ 每次集成后的验证

```bash
# 1. 编译检查
export JAVA_HOME="/c/Users/admin/.jdks/ms-11.0.31"
export PATH="$JAVA_HOME/bin:$PATH"
mvn clean compile -DskipTests

# 2. 单元测试
mvn test

# 3. 检查行数
wc -l src/main/java/com/minicad/app/StepPreviewJsonExporter.java

# 4. Git提交
git add -A
git commit -m "Inline PreviewXXX - Remove XXX lines"
```

---

## 📊 进度跟踪表

| # | 文件名 | 行数 | 集成状态 | 编译状态 | 测试状态 | Git提交 |
|---|--------|------|----------|----------|----------|---------|
| 1 | PreviewSurfaceSampler | 280 | ⏳ 待开始 | - | - | - |
| 2 | PreviewEdgeSampler | 733 | ⏳ 待开始 | - | - | - |
| 3 | PreviewPmiBuilder | 1673 | ⏳ 待开始 | - | - | - |
| 4 | PreviewCurveEvaluator | 1734 | ⏳ 待开始 | - | - | - |
| 5 | PreviewUvMapper | 2286 | ⏳ 待开始 | - | - | - |
| 6 | PreviewFaceBuilder | 2829 | ⏳ 待开始 | - | - | - |

**更新此表**: 每完成一个文件后更新状态

---

## 🎯 最终目标检查清单

完成所有集成后：

- [ ] PreviewSurfaceSampler.java 已删除
- [ ] PreviewEdgeSampler.java 已删除
- [ ] PreviewPmiBuilder.java 已删除
- [ ] PreviewCurveEvaluator.java 已删除
- [ ] PreviewUvMapper.java 已删除
- [ ] PreviewFaceBuilder.java 已删除
- [ ] StepPreviewJsonExporter.java 行数 < 2000 (目标: ~8500)
- [ ] `mvn clean compile` 通过
- [ ] `mvn test` 通过
- [ ] `mvn exec:java -Dexec.args="examples/minimal-square.step"` 成功
- [ ] 所有Git提交完成
- [ ] 合并到main分支

---

## ⚠️ 关键注意事项

### 1. 内部类处理

`PreviewUvMapper.java` 和 `PreviewFaceBuilder.java` 可能包含内部类：

```
ParametricSurfaceMapper
UvPoint
PreviewFaceResult
```

**处理方案**:
- IDEA通常会自动处理
- 如果冲突，选择"保留主文件中的版本"
- 或手动合并到单独文件

### 2. 类型冲突

如果遇到类型不兼容：

```
不兼容的类型: PreviewUvMapper.ParametricSurfaceMapper
              无法转换为 StepPreviewJsonExporter.ParametricSurfaceMapper
```

**解决方案**:
- 确保内部类只在主文件中定义
- 或提取到独立文件

### 3. 访问权限

提取文件中的 `public static` 方法会保持public。

如果需要改为 `private static`：

```
在IDEA重构后:
1. 选中方法
2. Refactor → Change Visibility
3. 改为 private static
```

---

## 🆘 如果遇到问题

### 回滚策略

```bash
# 撤销最近一次Git提交
git reset --hard HEAD~1

# 回到初始状态
git checkout main

# 重新开始
git checkout refactor/preview-integration
```

### IDEA撤销

```
Ctrl+Z - 撤销最近操作
Ctrl+Shift+Z - 重做

或使用 Local History:
右键文件 → Local History → Show History
```

---

## 📚 相关文档

- **详细指南**: `IDEA_REFACTOR_GUIDE.md` - 总体指导
- **第1步**: `INTEGRATION_STEP_1.md` - PreviewSurfaceSampler详细步骤
- **备份**: `backup/extracted/` - 所有提取文件备份
- **原始备份**: `StepPreviewJsonExporter.java.backup` - 主文件备份

---

## 🚀 立即开始

**第一步**: 打开 `INTEGRATION_STEP_1.md`

按照指南集成 `PreviewSurfaceSampler.java` (280行，5分钟)

完成后更新此总览文档的进度表！

---

**总预计时间**: 90-120分钟
**成功率**: 95%（IDEA自动处理大部分问题）
**收益**: 减少9534行代码，主文件从18094行 → ~8500行

**加油！开始第一个文件的集成！** 🎉