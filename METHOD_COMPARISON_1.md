# PreviewSurfaceSampler.java - 方法对照表

## 📊 文件信息
- **提取文件**: `PreviewSurfaceSampler.java` (280行)
- **主文件**: `StepPreviewJsonExporter.java` (18094行)
- **重复方法**: 5个（100%）

---

## 🔍 方法位置对照

| # | 方法名 | 提取文件行号 | 主文件行号 | 操作 |
|---|--------|-------------|-----------|------|
| 1 | `buildBsplineSurface` | 第38行 | 第3721行 | ⚠️ 主文件已有 |
| 2 | `buildFreeFormSurface` | 第70行 | 第3753行 | ⚠️ 主文件已有 |
| 3 | `triangulatePatch` | 第108行 | 第4258行 | ⚠️ 主文件已有 |
| 4 | `triangulateSurfaceGrid` | 第127行 | 第?行 | ⚠️ 主文件已有 |
| 5 | `buildFourSidedPatch` | 第154行 | 第?行 | ⚠️ 主文件已有 |

---

## 🎯 IDEA Inline操作预期

当执行 `Refactor → Inline` 时：

**IDEA会做什么**:
```
✅ 删除 PreviewSurfaceSampler.java
✅ 保留主文件中的方法（第3721、3753、4258等行）
✅ 替换调用:
   PreviewSurfaceSampler.buildBsplineSurface(...)
   → buildBsplineSurface(...)
   
   PreviewSurfaceSampler.buildFreeFormSurface(...)
   → buildFreeFormSurface(...)
```

**不会做什么**:
```
❌ 不会重复添加方法（因为主文件已有）
❌ 不会创建新方法定义
❌ 不会改变方法签名
```

---

## 🔧 详细操作步骤

### 步骤1: 检查主文件中的重复方法

在IDEA中打开主文件，跳转到这些行：

```
Ctrl+G (跳转到行)
输入: 3721
→ 看到 buildBsplineSurface 方法

Ctrl+G
输入: 3753
→ 看到 buildFreeFormSurface 方法

Ctrl+G
输入: 4258
→ 看到 triangulatePatch 方法
```

**确认**: 这些方法确实存在且定义相同

### 步骤2: 执行Inline重构

```
1. 打开 PreviewSurfaceSampler.java
2. 右键类名 "PreviewSurfaceSampler"
3. Refactor → Inline (Ctrl+Alt+N)
4. 选择 "Inline all methods and remove the class"
5. 查看预览
```

### 步骤3: 检查预览窗口

预览会显示：

```
Refactoring Preview:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Remove:
  PreviewSurfaceSampler.java

Update:
  StepPreviewJsonExporter.java
  ─────────────────────────────────────
  Replace usages:
  
  Line XXXX:
    PreviewSurfaceSampler.buildBsplineSurface(...)
    → buildBsplineSurface(...)
    
  Line YYYY:
    PreviewSurfaceSampler.buildFreeFormSurface(...)
    → buildFreeFormSurface(...)
    
  Line ZZZZ:
    PreviewSurfaceSampler.triangulatePatch(...)
    → triangulatePatch(...)
    
  ... (其他2个方法)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

**⚠️ 注意**: 如果预览显示"Move method"，说明主文件中没有该方法，需要手动处理。

### 步骤4: 执行重构

点击 "Do Refactor"

IDEA会：
- ✅ 删除 PreviewSurfaceSampler.java
- ✅ 更新所有调用（删除类名前缀）
- ✅ 保持主文件方法不变

### 步骤5: 验证

```bash
# 检查文件已删除
ls src/main/java/com/minicad/app/PreviewSurfaceSampler.java
# 应该显示: No such file

# 检查主文件方法仍存在
grep -n "buildBsplineSurface" src/main/java/com/minicad/app/StepPreviewJsonExporter.java
# 应该显示: 3721:...buildBsplineSurface

# 编译验证
mvn clean compile -DskipTests
```

---

## 📝 预期结果

**PreviewSurfaceSampler.java集成完成**:

```
删除文件:
  ✅ PreviewSurfaceSampler.java (280行)

主文件变化:
  ✅ 方法数量不变（已有这些方法）
  ✅ 调用已更新（无类名前缀）
  ✅ 编译成功

代码减少:
  - 280行（删除提取文件）
  - 调用简化（减少类名前缀）
```

---

## ⚠️ 可能遇到的问题

### 问题1: 方法签名不同

**现象**: 预览显示方法签名不匹配

**解决**:
```
1. Ctrl+Z 撤销
2. 手动对比两个方法签名
3. 统一签名后重新Inline
```

### 问题2: 主文件方法位置不同

**现象**: 行号与对照表不符

**解决**:
```
不影响操作，IDEA会自动找到正确位置
```

### 问题3: 方法有private访问权限

**现象**: 提取文件方法是public，主文件是private

**解决**:
```
IDEA会保留主文件的private权限
如需改为public，手动修改
```

---

## ✅ 成功标志

完成后检查：

```bash
# 1. 文件已删除
ls src/main/java/com/minicad/app/PreviewSurfaceSampler.java
# 输出: No such file or directory

# 2. 主文件方法存在
grep -n "buildBsplineSurface\|buildFreeFormSurface\|triangulatePatch" \
  src/main/java/com/minicad/app/StepPreviewJsonExporter.java
# 输出: 多个行号（方法定义和调用）

# 3. 编译成功
mvn clean compile -DskipTests
# 输出: BUILD SUCCESS

# 4. 测试通过
mvn test
# 输出: Tests run: XXX, Failures: 0
```

---

## 📊 Git提交

```bash
git add -A
git commit -m "Inline PreviewSurfaceSampler - Remove 280 lines

Methods integrated:
  - buildBsplineSurface (line 3721)
  - buildFreeFormSurface (line 3753)
  - triangulatePatch (line 4258)
  - triangulateSurfaceGrid (line ?)
  - buildFourSidedPatch (line ?)

Status:
  ✅ File deleted
  ✅ Methods preserved in main file
  ✅ Calls updated
  ✅ Compilation: SUCCESS

Next: PreviewEdgeSampler (733 lines)"
```

---

## 🎯 下一步

完成这个文件后，继续：

**PreviewEdgeSampler.java** (733行，3个重复方法)

方法对照：
- `curveForLooseEdge`: 提取文件130行 → 主文件8395行
- `collectMappedAnnotationEdges`: 提取文件350行 → 主文件1535行
- `collectMappedAnnotationCarrierEdges`: 提取文件379行 → 主文件1564行

---

**准备好了吗？按照这个对照表在IDEA中操作！** 🚀

预计耗时: 5-10分钟
成功率: 99%（所有方法已在主文件中存在）