# Update package declarations in all step.model files
$modelDir = "src/main/java/com/minicad/step/model"
$files = Get-ChildItem -Path $modelDir -Filter "*.java" -File

$count = 0
foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    # Replace package declaration: remove sub-package suffix (including multi-level subpackages)
    $newContent = $content -replace 'package com\.minicad\.step\.model\.[a-z0-9_.]+;', 'package com.minicad.step.model;'
    Set-Content -Path $file.FullName -Value $newContent -NoNewline
    $count++
}

Write-Host "Updated package declarations in $count files"