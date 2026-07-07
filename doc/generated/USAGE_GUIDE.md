# MiniCAD 使用指南

## 版本
v0.1.0-audit-complete (2026-07-07)

## 项目状态
- ✅ 92/98 任务已解决 (94%)
- ✅ 1897/1897 测试通过
- ✅ 45 个示例文件验证通过
- ✅ 核心功能完整可用

---

## 快速开始

### 1. 构建项目
```bash
mvn clean compile
```

### 2. 运行测试
```bash
mvn test
```

### 3. 解析 STEP 文件
```bash
mvn exec:java -Dexec.args="examples/minimal-square.step"
```

### 4. 启动 Web Viewer
```bash
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp
```
访问: http://127.0.0.1:8080

---

## CLI 使用

### StepDumpApp - 解析和验证

**基本解析**:
```bash
mvn exec:java -Dexec.args="your-file.step"
```

**JSON 输出**:
```bash
mvn exec:java -Dexec.args="--json your-file.step"
```

**仅验证不导出**:
```bash
mvn exec:java -Dexec.args="--validate-only your-file.step"
```

**Debug 模式**:
```bash
mvn exec:java -Dexec.args="--debug your-file.step"
```

**输出示例**:
```
Syntax Summary
  entityCount: 93829
  firstId: #1
  lastId: #93829

Semantic Summary
  ADVANCED_FACE: 2387
  CLOSED_SHELL: 31
  ...
```

---

## Web Viewer 使用

### 启动参数

**基本启动**:
```bash
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp
```

**自定义端口**:
```bash
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp --port=9090
```

**自定义主机**:
```bash
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp --host=0.0.0.0
```

**禁用缓存**:
```bash
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp --no-cache
```

**Debug 模式**:
```bash
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp --debug
```

### API 端点

| 端点 | 方法 | 说明 |
|------|------|------|
| `/api/preview` | POST | 上传 STEP 文件生成预览 |
| `/api/example?name=minimal-square` | GET | 获取示例文件 |
| `/api/config` | GET | 获取配置信息 |
| `/` | GET | Web UI |

### 文件上传限制

- **默认**: 50MB
- **配置**: `--max-upload=100MB`
- **系统属性**: `minicad.preview.maxUploadBytes`

### 缓存配置

- **默认目录**: `.minicad-cache/preview-glb-v1`
- **默认大小**: 1GB LRU
- **自定义目录**: `--cache-dir=/path/to/cache`
- **自定义大小**: `--max-cache=2GB`
- **禁用**: `--no-cache`

---

## 示例文件

### 小型测试文件 (快速验证)
- `examples/minimal-square.step` - 37 entities
- `examples/plate-with-round-hole.step` - 简单孔洞
- `examples/rectangular-frame.step` - 矩形框架

### 大型复杂文件 (性能测试)
- `examples/engine.stp` - 93,829 entities
- `examples/fan.stp` - 42K entities

### 几何覆盖测试
- `examples/bspline-patch.step` - B-Spline 曲面
- `examples/conical-band.step` - 圆锥几何
- `examples/toroidal-seam.step` - 环面几何

---

## 性能基准

### 运行基准测试
```bash
mvn exec:java -Dexec.mainClass=com.minicad.app.StepBenchmarkApp
```

### 输出指标
- parseMs: 解析时间
- resolveMs: 语义解析时间
- buildMs: 几何构建时间
- previewExportMs: 预览导出时间
- meshExportMs: Mesh导出时间

---

## 能力报告

### 生成能力报告
```bash
mvn exec:java -Dexec.mainClass=com.minicad.app.StepCapabilityReportApp
```

### JSON 输出
```bash
mvn exec:java -Dexec.mainClass=com.minicad.app.StepCapabilityReportApp --json
```

### Schema 对比
```bash
mvn exec:java -Dexec.mainClass=com.minicad.app.StepCapabilityReportApp \
  --schema=schemas/ap242ed2_dis2_mim_lf_v1.101.exp
```

---

## 错误处理

### 常见错误

| 错误 | 说明 | 解决方法 |
|------|------|----------|
| `UnsupportedClassVersionError` | Java版本错误 | 使用 Java 11+ |
| `StepParseException` | STEP语法错误 | 检查文件格式 |
| `StepResolutionException` | 实体解析错误 | 检查实体定义 |
| `GeometryException` | 几何构建错误 | 检查几何参数 |
| `TopologyException` | 拓扑验证错误 | 检查拓扑连接 |
| `413 Request Entity Too Large` | 文件过大 | 增加max-upload |

### Debug 技巧

**启用详细日志**:
```bash
mvn exec:java -Dexec.args="--debug your-file.step"
```

**查看错误位置**:
- 错误消息包含 entity ID (#123)
- 错误消息包含位置信息

**检查实体支持状态**:
```bash
mvn exec:java -Dexec.args="--json your-file.step"
# 查看 unsupportedFaceCount 和 unsupportedFaces
```

---

## 安全特性

### 已实现的安全措施

| 功能 | 说明 |
|------|------|
| Upload limits | 默认50MB，可配置 |
| Cache limits | 默认1GB LRU，可配置 |
| Path traversal | 文件名白名单验证 |
| Atomic cache writes | 临时文件 + atomic move |
| Security headers | nosniff, CSP, CORP |
| Loopback binding | 默认127.0.0.1 |
| Safe error messages | 不泄露内部细节 |
| Source logging | 默认禁用，debug启用 |

### 生产环境建议

1. **绑定地址**: 使用 `--host=127.0.0.1` (默认)
2. **上传限制**: 根据实际需求设置 `--max-upload`
3. **缓存大小**: 根据磁盘空间设置 `--max-cache`
4. **监控日志**: 使用 requestId 跟踪请求

---

## 测试覆盖

### 单元测试
```bash
mvn test
```
- 1897 tests total
- 60 parser tests (负面语法)
- 36 security tests
- B-Rep, geometry, topology tests

### 示例回归测试
```bash
mvn test -Dtest=ExamplesRegressionTest
```
- 45 example files tested

---

## 项目结构

```
MiniCAD/
├── src/main/
│   ├── antlr4/          # ANTLR4 grammar
│   ├── java/
│   │   ├── app/         # Applications (CLI, Viewer)
│   │   ├── common/      # Common utilities
│   │   ├── geometry/    # 3D geometry
│   │   ├── geometry2d/  # 2D geometry
│   │   ├── step/
│   │   │   ├── model/   # STEP model classes (1264)
│   │   │   ├── semantic/# Entity resolvers
│   │   │   └ syntax/    # Parser
│   │   │   └ topology/  # B-Rep topology
│   │   └── tools/       # Development tools
│   └── resources/
│       ├── static/      # Web viewer UI
│       └── schemas/     # EXPRESS schemas
├── src/test/            # Tests (1897)
├── examples/            # Example STEP files (45)
├── doc/
│   └── generated/       # Generated reports
├── README.md            # Documentation
├── SECURITY.md          # Security policy
├── CONTRIBUTING.md      # Contribution guide
├── AGENTS.md            # Task tracking (98 tasks)
└── pom.xml              # Maven configuration
```

---

## 统计信息

| 指标 | 数值 |
|------|------|
| Model classes | 1264 |
| Registry entries | 2357 |
| Entity factories | ~604 |
| Registry files | 21 |
| Test files | 1897 |
| Example files | 45 |
| Test LOC | 10,747+ |
| Documentation LOC | 2846 |

---

## 依赖版本

| 依赖 | 版本 | 说明 |
|------|------|------|
| Java | 11 | 最低版本 |
| Jetty | 11.0.24 | Web server |
| Logback | 1.5.18 | Logging |
| JUnit | 5.10.2 | Testing |
| ANTLR4 | 4.13.1 | Parser generator |
| Fastjson2 | 2.0.56 | JSON serialization |

---

## CI/CD

### GitHub Actions
- **Workflow**: `.github/workflows/ci.yml`
- **触发**: push/PR to main
- **矩阵**: Java 11
- **步骤**: compile → test → regression

### CodeQL
- **Workflow**: `.github/workflows/codeql.yml`
- **分析**: Java security analysis

### Maven Plugins
- **Spotless**: Code formatting
- **Forbidden APIs**: API usage check
- **Enforcer**: Java version requirement

---

## 已知限制

| 限制 | 说明 |
|------|------|
| AP214/AP242 兼容性 | 实验性子集，不宣称完整兼容 |
| 大文件性能 | 需要 JVM heap memory 配置 |
| 部分 entity 未构建 | unsupportedFaces 会报告 |
| Complex entity 多参数 | Grammar 支持，需验证实际文件 |

---

## 获取帮助

### 文档
- `README.md` - 项目介绍和架构
- `SECURITY.md` - 安全策略
- `CONTRIBUTING.md` - 贡献指南
- `AGENTS.md` - 任务跟踪
- `doc/generated/PROJECT_FINAL_STATUS_REPORT.md` - 最终报告

### GitHub
- **仓库**: https://github.com/dchen8525-dev/MiniCAD
- **Actions**: https://github.com/dchen8525-dev/MiniCAD/actions
- **Issues**: https://github.com/dchen8525-dev/MiniCAD/issues

---

## 版本历史

| Tag | 日期 | 说明 |
|------|------|------|
| v0.1.0-audit-complete | 2026-07-07 | AGENTS审计完成，92/98 (94%) |

---

## License
See LICENSE file in repository.

---

## 致谢

MiniCAD 是一个实验性的 Java CAD 内核，用于 STEP (ISO 10303) 文件解析和几何处理。

感谢所有贡献者和测试者！