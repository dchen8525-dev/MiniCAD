# MiniCAD 项目维护指南

## 版本
v0.1.0-audit-complete (2026-07-07)

---

## 日常维护

### 1. 测试验证

**每次修改后运行**:
```bash
# 快速测试
mvn test -q

# 完整测试（包括回归）
mvn test
mvn test -Dtest=ExamplesRegressionTest
```

**测试结果检查**:
- 1897 tests should pass
- 45 examples should parse successfully
- No failures or errors

### 2. 文件清理

**临时文件检查**:
```bash
# 查找临时文件
find . -name "*.tmp" -o -name "*.bak" -o -name "*~"

# 清理（如果有）
find . -name "*.tmp" -delete
find . -name "*.bak" -delete
```

**缓存目录**:
```bash
# 查看缓存大小
ls -la .minicad-cache

# 清理缓存（如果需要）
rm -rf .minicad-cache
```

**Target目录**:
```bash
# 清理编译产物
mvn clean

# 或手动删除
rm -rf target
```

### 3. Git维护

**状态检查**:
```bash
# 查看未提交文件
git status

# 查看分支状态
git branch -a

# 查看与远程同步状态
git fetch origin
git status
```

**提交检查**:
```bash
# 查看最近提交
git log --oneline -10

# 查看提交统计
git log --stat
```

**推送前检查**:
```bash
# 运行测试
mvn test

# 检查代码格式
mvn spotless:check

# 检查禁止API
mvn forbiddenapis:check

# 确认通过后再推送
git push origin main
```

---

## 定期维护

### 每周检查

| 任务 | 命令 | 说明 |
|------|------|------|
| 测试全量 | `mvn test` | 确保所有测试通过 |
| 依赖更新检查 | `mvn versions:display-dependency-updates` | 检查可用更新 |
| CI运行检查 | GitHub Actions页面 | 确认CI通过 |
| 文档更新 | 查看ROADMAP | 对齐进展 |

### 每月检查

| 任务 | 命令 | 说明 |
|------|------|------|
| 大文件测试 | `mvn exec:java -Dexec.args="large-file.stp"` | 验证大文件性能 |
| 内存使用检查 | JVM监控 | 确认无内存泄漏 |
| 依赖安全检查 | `mvn dependency-check:check` | CVE漏洞检查 |
| 文档同步 | 更新README/AGENTS | 状态同步 |

### 每季度检查

| 任务 | 说明 |
|------|------|
| 版本评估 | 是否需要发布新版本 |
| 功能回顾 | 对比ROADMAP进展 |
| 测试覆盖评估 | 是否需要新增测试 |
| 性能基准更新 | 更新性能指标 |

---

## 安全维护

### 依赖安全

**CVE检查**:
```bash
# 使用OWASP依赖检查
mvn dependency-check:check

# 查看报告
cat target/dependency-check-report.html
```

**发现漏洞时**:
1. 检查漏洞严重程度
2. 更新到安全版本
3. 运行测试验证
4. 更新CHANGELOG

### 密钥管理

**敏感信息检查**:
```bash
# 搜索可能的密钥泄露
grep -rn "password\|secret\|key\|token" --include="*.java" --include="*.properties"

# 确认无硬编码密钥
```

**配置安全**:
- 使用环境变量存储敏感配置
- 不要在代码中硬编码密钥
- 使用配置文件加密（如需要）

---

## 性能维护

### 内存监控

**JVM配置**:
```bash
# 大文件处理
mvn exec:java -Dexec.args="large.stp" \
  -Dexec.toolchain=jdk \
  -D-Xmx4g
```

**内存泄漏检查**:
- 使用JProfiler或VisualVM
- 监控长时间运行的viewer
- 检查cache增长

### 性能基准

**定期运行**:
```bash
# 基准测试
mvn exec:java -Dexec.mainClass=com.minicad.app.StepBenchmarkApp

# 记录结果
# 对比历史基准
```

**性能退化检测**:
- 建立基准数据库
- 对比每次运行结果
- 调查性能下降原因

---

## 备份策略

### 代码备份

**Git备份**:
```bash
# 推送到GitHub（主要备份）
git push origin main

# 本地备份（可选）
git bundle create minicad-backup.bundle --all
```

**重要文件备份**:
- examples/ 目录
- doc/generated/ 目录
- schemas/ 目录

### 文档备份

**关键文档**:
- README.md
- AGENTS.md
- ROADMAP.md
- USAGE_GUIDE.md
- SECURITY.md
- CONTRIBUTING.md

---

## 故障恢复

### 测试失败

**步骤**:
1. 查看失败详情: `mvn test -Dtest=FailedTest`
2. 分析失败原因
3. 修复代码或测试
4. 运行全量测试
5. 提交修复

### 解析失败

**步骤**:
1. 启用debug模式: `--debug`
2. 查看错误位置和entity ID
3. 检查STEP文件格式
4. 查看entity支持状态
5. 必要时添加entity支持

### CI失败

**步骤**:
1. 查看GitHub Actions日志
2. 本地重现问题: `mvn test`
3. 修复问题
4. 确认本地通过
5. 推送修复

---

## 更新策略

### 依赖更新

**安全更新** (立即):
```bash
# 检查可用更新
mvn versions:display-dependency-updates

# 更新单个依赖
mvn versions:use-latest-releases -DincludeArtifact=junit

# 运行测试验证
mvn test
```

**版本更新** (计划):
- 在ROADMAP中规划
- 评估影响范围
- 充分测试后更新

### 功能更新

**流程**:
1. 查看ROADMAP优先级
2. 选择任务实现
3. 编写测试
4. 实现功能
5. 运行全量测试
6. 更新文档
7. 更新AGENTS.md
8. 提交并推送

---

## 监控指标

### 关键指标

| 指标 | 目标值 | 监控方式 |
|------|--------|----------|
| Test pass rate | 100% | mvn test |
| Parse success rate | >95% | ExamplesRegressionTest |
| Performance | 基准值 | BenchmarkApp |
| Memory usage | <2GB | JVM监控 |
| Cache size | <1GB | 目录检查 |

### 告警阈值

| 指标 | 告警条件 | 处理 |
|------|----------|------|
| Test failures | >0 | 立即修复 |
| Parse errors | >5% | 检查支持状态 |
| Memory >2GB | 大文件处理 | 优化内存 |
| Cache >1GB | 缓存增长 | 清理cache |

---

## 常见问题处理

### 问题1: 测试失败

**症状**: mvn test 显示 failures

**处理**:
```bash
# 查看失败详情
mvn test -Dtest=FailedTest

# 查看完整错误
mvn test --debug

# 修复后验证
mvn test
```

### 问题2: 大文件OOM

**症状**: OutOfMemoryError

**处理**:
```bash
# 增加内存
mvn exec:java -D-Xmx4g -Dexec.args="large.stp"

# 或设置环境变量
export MAVEN_OPTS="-Xmx4g"
```

### 问题3: Viewer启动失败

**症状**: Port已被占用

**处理**:
```bash
# 使用其他端口
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp --port=9090

# 检查端口占用
netstat -an | grep 8080
```

### 问题4: Cache增长过快

**症状**: 磁盘空间不足

**处理**:
```bash
# 清理cache
rm -rf .minicad-cache

# 减小cache限制
mvn exec:java ... --max-cache=500MB
```

---

## 维护检查清单

### 每日

- [ ] 无临时文件遗留
- [ ] Git状态clean
- [ ] 无编译错误

### 每周

- [ ] 全量测试通过
- [ ] CI运行成功
- [ ] 无安全告警

### 每月

- [ ] 大文件测试通过
- [ ] 依赖CVE检查通过
- [ ] 文档状态同步

### 每季度

- [ ] 版本评估完成
- [ ] ROADMAP更新
- [ ] 性能基准更新

---

## 维护资源

### 工具

| 工具 | 用途 |
|------|------|
| Maven | 构建/测试 |
| Git | 版本控制 |
| JVM监控 | 性能分析 |
| JProfiler | 内存分析 |

### 文档

| 文档 | 用途 |
|------|------|
| USAGE_GUIDE.md | 使用方法 |
| ROADMAP.md | 发展规划 |
| AGENTS.md | 任务状态 |
| CONTRIBUTING.md | 贡献指南 |

---

## 维护团队

### 角色分工

| 角色 | 职责 |
|------|------|
| 核心开发者 | 功能开发/修复 |
| 测试工程师 | 测试维护/扩展 |
| 文档维护者 | 文档更新 |
| 社区管理 | Issue处理 |

---

## 总结

MiniCAD v0.1.0-audit-complete 维护指南提供了：
- 日常维护流程
- 定期检查任务
- 安全维护策略
- 性能监控方法
- 故障恢复流程
- 更新策略

遵循此指南可确保项目稳定运行和持续改进。

**感谢维护 MiniCAD！** 🚀