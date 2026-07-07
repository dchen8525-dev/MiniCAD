# MiniCAD 后续路线图

## 当前状态 (v0.1.0-audit-complete)

- ✅ 92/98 任务已解决 (94%)
- ✅ 1897/1897 测试通过
- ✅ 核心功能完整可用
- ✅ 文档体系完善
- ✅ CI/CD专业配置

---

## 短期计划 (可选增强)

### 优先级 1: 性能优化

| 任务 | 说明 | 影响 |
|------|------|------|
| L05: MDC logging | 结构化日志 | 生产环境监控 |
| 大文件优化 | JVM heap配置指南 | 性能改进 |

### 优先级 2: 测试增强

| 任务 | 说明 | 影响 |
|------|------|------|
| I05: Property tests | 边界case发现 | 质量提升 |
| M05: Fuzz target | 安全加固 | 安全提升 |

### 优先级 3: 工具开发

| 任务 | 说明 | 影响 |
|------|------|------|
| M04: Fixture minimizer | 调试效率 | 开发效率 |

---

## 中期计划 (功能扩展)

### 1. 完整AP214/AP242兼容性

**目标**: 从实验性子集到完整兼容

**步骤**:
1. 分析AP214/AP242 EXPRESS schema
2. 扩展entity resolver覆盖
3. 添加missing geometry types
4. 验证real-world CAD文件

**预期成果**: 95%+ AP214/AP242 entity支持

### 2. 导出格式扩展

**当前**: GLB preview export

**扩展目标**:
- STL export (已有基础)
- OBJ export
- STEP write (输出STEP文件)
- SVG 2D drawing export

### 3. 几何算法增强

**当前**: B-Rep validation + basic mesh

**增强目标**:
- 高级布尔运算
- 曲面缝合优化
- 网格质量提升
- 参数化建模

---

## 长期计划 (架构演进)

### 1. 插件架构

**目标**: 可扩展entity/geometry处理

**设计**:
```
Plugin Interface:
- registerEntities()
- resolveEntity()
- buildGeometry()
- exportFormat()
```

**用途**: 第三方扩展entity类型

### 2. 多格式输入

**当前**: STEP only

**扩展目标**:
- IGES import
- DXF import
- STL import
- OBJ import

### 3. 可视化增强

**当前**: Web viewer (Three.js)

**增强目标**:
- 高级材质渲染
- PMI annotation显示
- Section cut views
- Measurement tools
- Assembly tree navigation

---

## 技术债务清单

### 高优先级

| 项目 | 说明 | 影响 |
|------|------|------|
| E02: Open shell | 需要明确区分 | 功能完整性 |
| C07: Parameter count | Partial implementation | 验证完整性 |

### 中优先级

| 项目 | 说明 | 影响 |
|------|------|------|
| L06: Thread safety | 需要全面审计 | 生产稳定性 |

### 低优先级

| 项目 | 说明 | 影响 |
|------|------|------|
| B02/B05: Parser subset | 文档已说明 | 不影响使用 |

---

## 性能优化方向

### 1. 解析性能

**当前**: ANTLR4 parser

**优化方向**:
- 并行entity resolution
- 增量解析（大文件）
- 内存优化（减少临时对象）

### 2. 几何构建性能

**当前**: 逐面构建

**优化方向**:
- 批量face处理
- 并行geometry computation
- GPU加速（可选）

### 3. 导出性能

**当前**: 单次导出

**优化方向**:
- 并行mesh generation
- 流式导出（大模型）
- 增量更新

---

## 测试覆盖扩展

### 当前覆盖

| 类别 | 测试数 | 覆盖度 |
|------|--------|--------|
| Parser | 60 | High |
| Security | 36 | High |
| Geometry | 13+ | Medium |
| Topology | 13 | Medium |
| Assembly | 3 | Medium |

### 扩展目标

| 类别 | 目标测试数 | 说明 |
|------|------------|------|
| Parser | 80+ | +负面case |
| Geometry | 50+ | +高级curve/surface |
| Topology | 30+ | +复杂shell |
| Performance | 10+ | Benchmark tests |
| Integration | 20+ | End-to-end |

---

## 文档扩展计划

### 当前文档

- README.md (738 lines)
- SECURITY.md (72 lines)
- CONTRIBUTING.md (212 lines)
- AGENTS.md (1484 lines)
- Generated docs (3123 lines)

### 扩展目标

| 文档 | 内容 |
|------|------|
| ARCHITECTURE.md | 详细架构设计 |
| API.md | CLI/Web API文档 |
| ENTITY_SUPPORT.md | Entity支持列表 |
| PERFORMANCE.md | 性能优化指南 |
| EXAMPLES.md | 示例文件说明 |

---

## 社区建设

### GitHub优化

- Issue templates
- PR templates
- Wiki pages
- Discussion forum

### 文档网站

- GitHub Pages site
- API documentation (Javadoc)
- Example gallery
- Tutorial series

---

## 版本规划

### v0.2.0 (短期)

**目标**: 可选增强实现

**包含**:
- MDC logging
- 性能优化指南
- 扩展测试覆盖

### v0.3.0 (中期)

**目标**: 功能扩展

**包含**:
- 更多export格式
- 几何算法增强
- 插件架构基础

### v1.0.0 (长期)

**目标**: 生产级产品

**包含**:
- 完整AP214/AP242支持
- 多格式输入
- 可视化增强
- 性能优化完成

---

## 资源需求

### 开发资源

| 类型 | 说明 |
|------|------|
| 时间 | 6-12个月达到v1.0.0 |
| 人力 | 1-2名核心开发者 |
| 测试 | Real-world CAD文件集 |

### 测试资源

| 类型 | 说明 |
|------|------|
| STEP files | 多来源CAD文件 |
| Edge cases | 边界case收集 |
| Performance | 大文件测试集 |

---

## 成功指标

### v0.2.0

- ✅ 95%+ 任务解决
- ✅ 2000+ 测试通过
- ✅ 性能基准建立

### v0.3.0

- ✅ 多格式export
- ✅ 插件架构可用
- ✅ 50+ entity测试

### v1.0.0

- ✅ AP214/AP242完整兼容
- ✅ 生产级性能
- ✅ 完整文档网站
- ✅ 社区活跃

---

## 风险评估

### 技术风险

| 风险 | 说明 | 缓解措施 |
|------|------|----------|
| AP214复杂度 | Entity数量巨大 | 逐步扩展 |
| 性能瓶颈 | 大文件处理 | 优化+并行 |
| 兼容性 | 不同CAD系统 | 多来源测试 |

### 资源风险

| 风险 | 说明 | 缓解措施 |
|------|------|----------|
| 时间有限 | 开发进度 | 优先级管理 |
| 测试覆盖 | Real-world files | 社区贡献 |

---

## 下一步行动

### 立即可做

1. ✅ 检查GitHub Actions CI运行
2. ✅ 收集real-world STEP files
3. ✅ 建立性能基准

### 本周可做

1. 实现MDC logging (L05)
2. 扩展性能测试
3. 更新文档网站

### 本月可做

1. 实现Property tests (I05)
2. 扩展entity resolver
3. 优化大文件性能

---

## 结论

MiniCAD v0.1.0-audit-complete 已完成核心功能，可以投入使用。

后续路线图提供了清晰的演进路径：
- 短期：可选增强和优化
- 中期：功能扩展
- 长期：生产级产品

感谢使用 MiniCAD！🚀