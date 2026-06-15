# Java 21 to Java 11 Compatibility Fix - Progress Report

## Current Status (Session 1)

### Commits Made
1. `2f3159a` - Fix Java 21 to Java 11 compatibility - Part 1
   - Switch expressions → if-else
   - Pattern matching instanceof (~60 fixes)
   - removeLast() → remove(size()-1)
   
2. `44940f9` - Fix Java 21 to Java 11 compatibility - Part 2
   - Additional pattern matching instanceof (~20 fixes)

### Files Fixed
- ✅ StepProfileBuilder.java - switch表达式转换
- ✅ StepTrimResolver.java - 所有pattern matching
- ✅ PreviewUvMapper.java - removeLast()
- ✅ PreviewFaceBuilder.java - pattern matching
- ✅ StepMeshExporter.java - removeLast()
- ✅ StepPreviewJsonExporter.java - 约80处pattern matching (约15000行中的部分)

### Remaining Work

**Pattern Matching instanceof:** 93处

**位置：** 主要集中在StepPreviewJsonExporter.java的第17600-17800行范围

**具体类型：**
```
- StepAppliedDateAssignment assignment
- StepApproval approval
- StepSecurityClassification classification
- StepContract contract
- StepCertification certification
- StepApprovalPersonOrganization assignment
- StepOrganizationAssignment assignment
- StepAppliedOrganizationAssignment assignment
- StepPersonAndOrganizationAssignment assignment
- StepAppliedPersonAndOrganizationAssignment assignment
- StepClassificationAssignment assignment
- StepAppliedClassificationAssignment assignment
- StepIdentificationAssignment assignment
- StepAppliedIdentificationAssignment assignment
- StepExternalIdentificationAssignment assignment
- StepAppliedExternalIdentificationAssignment assignment
- StepDocument document
- StepAppliedDateTimeAssignment assignment
- ... (约93处)
```

## Next Session Task List

### Priority 1: Fix remaining 93 pattern matching instanceof

**方法：** 手动逐个修复，每次处理10-15个

**模式：**
```java
// Before (Java 21)
if (candidate instanceof Type variable && variable.method()) {
    // use variable
}

// After (Java 11)
if (candidate instanceof Type && ((Type) candidate).method()) {
    Type variable = (Type) candidate;
    // use variable
}
```

### 具体行号范围：
- 第17600-17800行：各种Assignment和Classification类型
- 查找命令：`grep -n "instanceof.*\w\+ \w\+$" src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

### 执行步骤：
1. 查看剩余pattern matching的具体位置
2. 批量修复相同模式的pattern matching（使用replace_all=true）
3. 编译测试（`mvn compile`）
4. 提交进度（每修复20-30个提交一次）

### 预计工作量：
- 93处pattern matching
- 每处需要2-3次编辑操作
- 预计需要4-5个session完成

## Success Criteria

编译成功：`mvn compile` 显示 `BUILD SUCCESS`

## Alternative Strategy

如果手动修复太慢，可以考虑：
- 使用正则批量替换
- 或升级到Java 21（修改pom.xml的maven.compiler.release）