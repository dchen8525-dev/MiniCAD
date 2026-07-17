# MiniCAD 代码优化路线图

> **生成时间**: 2026-07-09  
> **当前状态**: Resolver分解进行中  
> **目标**: 提高代码可维护性、可读性和性能

---

## 📊 当前代码库状态

### 大型文件分析

| 文件 | 行数 | 大小 | 状态 | 优先级 |
|------|------|------|------|--------|
| StepPreviewJsonExporter.java | 14,682 | 838KB | ⚠️ 需分解 | 高 |
| StepEntityResolver.java | 11,696 | 521KB | 🔄 进行中 | 高 |
| StepCadBuilder.java | 4,932 | 224KB | ⚠️ 需分解 | 中 |
| StepDumpApp.java | 3,238 | 190KB | ✅ 可接受 | 低 |
| PreviewFaceBuilder.java | 1,972 | 92KB | ✅ 可接受 | 低 |

### 已完成的重构

- ✅ **SurfaceResolver** (246行) - 曲面几何实体
- ✅ **BSplineResolver** (380行) - B-Spline曲线和曲面
- ✅ **BezierResolver** (348行) - Bezier曲线/曲面和Offset几何
- ✅ **GeometryResolver** (927行) - 已符合<1000行限制

---

## 🎯 优化任务清单

### Phase 1: 继续Resolver分解（最高优先级）

**目标**: 将 StepEntityResolver 从 11,696行 减少到 <5,000行

**当前进展**:
- ✅ 已委托方法: 97个（委托给6个专门Resolver）
- ⚠️ 未委托方法: 491个（直接在StepEntityResolver中实现）
- 🎯 需要继续提取的方法类型:
  - 几何特征方法（Chamfer, Fillet, Blended等）
  - 分析/仿真方法（Analysis, Simulation等）
  - 高级曲面方法（FreeForm, Bounded等）

#### 1.1 提取 TopologyResolver
- **预计行数**: ~1,500行
- **内容**: 拓扑实体（ClosedShell, OpenShell, FaceBound, EdgeLoop等）
- **状态**: ✅ 已完成（StepTopologyResolver已存在，719行）

#### 1.2 提取 GeometricFeatureResolver
- **预计行数**: ~800行
- **内容**: 几何特征实体（Chamfer, Fillet, Blended, FreeForm等）
- **状态**: ⏳ 待开始
- **优先级**: 高

#### 1.3 提取 AnalysisResolver
- **预计行数**: ~600行
- **内容**: 分析/仿真实体（Analysis, Simulation, Model等）
- **状态**: ⏳ 待开始
- **优先级**: 中

#### 1.4 提取 ProductResolver
- **预计行数**: ~1,000行
- **内容**: 产品定义实体（ProductDefinition, ProductCategory等）
- **状态**: ⏳ 待开始（已有StepProductResolver）

#### 1.5 提取 UnitResolver
- **预计行数**: ~800行
- **内容**: 单位和测量实体（SIUnit, ConversionBasedUnit等）
- **状态**: ⏳ 待开始

#### 1.6 提取 MaterialResolver
- **预计行数**: ~600行
- **内容**: 材料和外观实体（Material, SurfaceStyle等）
- **状态**: ⏳ 待开始

#### 1.7 提取 AnnotationResolver
- **预计行数**: ~500行
- **内容**: PMI和注释实体（Annotation, Dimension等）
- **状态**: ⏳ 待开始

#### 1.5 提取 AnnotationResolver
- **预计行数**: ~500行
- **内容**: PMI和注释实体（Annotation, Dimension等）
- **状态**: ⏳ 待开始

---

### Phase 2: 分解 StepPreviewJsonExporter（高优先级）

**目标**: 将 14,682行 分解为多个 <3,000行 的文件

#### 2.1 提取 PreviewGeometryCollector
- **预计行数**: ~3,000行
- **内容**: 几何收集逻辑
- **状态**: ⏳ 待开始

#### 2.2 提取 PreviewAssemblyBuilder
- **预计行数**: ~2,000行
- **内容**: 装配构建逻辑
- **状态**: ⏳ 待开始

#### 2.3 提取 PreviewMeshExporter
- **预计行数**: ~2,500行
- **内容**: 网格导出逻辑
- **状态**: ⏳ 待开始

#### 2.4 提取 PreviewMetadataExtractor
- **预计行数**: ~1,000行
- **内容**: 元数据提取逻辑
- **状态**: ⏳ 待开始

#### 2.5 提取 PreviewStatisticsCalculator
- **预计行数**: ~1,000行
- **内容**: 统计计算逻辑
- **状态**: ⏳ 待开始

---

### Phase 3: 分解 StepCadBuilder（中高优先级）

**目标**: 将 4,932行 分解为多个 <1,500行 的文件

#### 3.1 提取 StepCadPrimitiveBuilder
- **预计行数**: ~1,000行
- **内容**: 基础体素构建
- **状态**: ⏳ 待开始

#### 3.2 提取 StepCadSweptBuilder
- **预计行数**: ~1,000行
- **内容**: 扫掠体构建
- **状态**: ⏳ 待开始

#### 3.3 提取 StepCadBooleanBuilder
- **预计行数**: ~1,000行
- **内容**: 布尔运算
- **状态**: ⏳ 待开始

#### 3.4 提取 StepCadAdvancedBuilder
- **预计行数**: ~1,000行
- **内容**: 高级特征构建
- **状态**: ⏳ 待开始

---

### Phase 4: 异常处理优化（中优先级）

**目标**: 统一异常处理，使用具体异常类型

#### 4.1 修复 broad exception catching
- **数量**: 14处 `catch (Exception`
- **位置**: StepDumpApp, StepViewerApp等
- **建议**: 使用具体异常类型
- **状态**: ⏳ 待开始

---

### Phase 5: 性能优化（中优先级）

#### 5.1 集合初始化优化
- **数量**: 562处集合实例化
- **建议**: 预估初始容量减少resize
- **状态**: ⏳ 待开始

#### 5.2 添加缓存机制
- **建议**: 对重复计算的几何结果添加缓存
- **状态**: ⏳ 待开始

#### 5.3 性能监控
- **建议**: 添加 @Timed 注解监控关键方法
- **状态**: ⏳ 待开始

---

### Phase 6: 代码质量改进（中优先级）

#### 6.1 统一日志格式
- **建议**: 添加MDC上下文
- **状态**: ⏳ 待开始

#### 6.2 完善代码注释
- **建议**: 为复杂算法添加详细JavaDoc
- **状态**: ⏳ 待开始

---

### Phase 7: 测试增强（低优先级）

#### 7.1 性能回归测试
- **建议**: 添加大文件解析性能测试
- **状态**: ⏳ 待开始

#### 7.2 内存使用测试
- **建议**: 添加内存使用监控测试
- **状态**: ⏳ 待开始

---

### Phase 8: 文档改进（低优先级）

#### 8.1 架构文档
- **文件**: doc/architecture/
- **内容**: Resolver设计、导出器设计、性能指南
- **状态**: ⏳ 待开始

#### 8.2 开发者指南
- **文件**: CONTRIBUTING_RESOLVERS.md
- **内容**: 如何添加新Resolver
- **状态**: ⏳ 待开始

---

## 📈 预期收益

### 可维护性提升
- 大文件分解后更易理解和修改
- 代码审查时间预计减少50%
- 团队协作合并冲突预计减少80%

### 性能提升
- 小文件并行编译更快（预计提升30%）
- 缓存优化预计提升处理速度20%
- 减少内存分配开销

### 代码质量
- 单一职责原则更清晰
- 更好的模块化设计
- 更易添加新功能

---

## 🚀 实施策略

### 推荐执行顺序

1. **Phase 1** (Resolver分解) - 最大技术债务
2. **Phase 2** (Exporter分解) - 第二大文件
3. **Phase 3** (Builder分解) - 中等优先级
4. **Phase 4-5** (异常和性能) - 质量改进
5. **Phase 6-8** (测试和文档) - 长期改进

### 每阶段验收标准

- ✅ 所有测试通过（1,897个测试）
- ✅ LineCountTest通过（所有类<1000行）
- ✅ 编译成功无警告
- ✅ Git提交信息清晰
- ✅ 推送到远程仓库

---

## 📝 重构记录

### 2026-07-09 (Session 2)

**已完成**:
- ✅ 提取 SurfaceResolver (246行)
- ✅ 提取 BSplineResolver (380行)
- ✅ 提取 BezierResolver (348行)
- ✅ 优化 GeometryResolver (1125行 → 927行)
- ✅ 所有测试通过
- ✅ Git提交: 995fb3e

**进行中**:
- 🔄 分析 StepEntityResolver 分解策略
- 🔄 识别需要提取的方法类别

---

### 2026-07-09 (Session 1)

**已完成**:
- ✅ 创建 OPTIMIZATION_ROADMAP.md
- ✅ 分析代码库优化机会
- ✅ 制定分阶段优化计划

**进行中**:
- ⏳ 等待选择下一个优化任务

---

## 🎯 快速开始指南

### 今日任务建议

**选项1**: 继续Resolver分解
```bash
# 提取 TopologyResolver
# 预计时间: 2-3小时
# 预期收益: 最大（减少~1500行）
```

**选项2**: 开始Exporter分解
```bash
# 提取 PreviewGeometryCollector
# 预计时间: 3-4小时
# 预期收益: 大（减少~3000行）
```

**选项3**: 性能优化
```bash
# 添加集合初始化优化和缓存
# 预计时间: 1-2小时
# 预期收益: 中（性能提升）
```

---

## 📞 联系方式

如有问题或需要调整优先级，请反馈。

**下次审查**: 完成Phase 1后  
**文档更新**: 每次重大重构后更新

---

*最后更新: 2026-07-09*
