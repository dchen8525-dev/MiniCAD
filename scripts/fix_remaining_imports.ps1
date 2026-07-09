# Fix remaining import issues in StepEntity.java and other files
$sourceDirs = @("src/main/java", "src/test/java")

$totalCount = 0

foreach ($dir in $sourceDirs) {
    $files = Get-ChildItem -Path $dir -Filter "*.java" -Recurse -File
    foreach ($file in $files) {
        $content = Get-Content $file.FullName -Raw
        $fileChanged = $false
        $newContent = $content

        # Fix 1: Broken imports like tepXXX -> StepXXX (missing 'S')
        $pattern1 = 'import com\.minicad\.step\.model\.tep'
        if ($newContent -match $pattern1) {
            $newContent = $newContent -replace $pattern1, 'import com.minicad.step.model.Step'
            $matches1 = ([regex]::Matches($content, $pattern1)).Count
            $totalCount += $matches1
            $fileChanged = $true
            Write-Host "Fixed $matches1 broken 'tep' imports in: $($file.FullName)"
        }

        # Fix 2: Remaining sub-package imports (approval, document, org, etc.)
        $pattern2 = 'import com\.minicad\.step\.model\.[a-z_]+\.'
        if ($newContent -match $pattern2) {
            $newContent = $newContent -replace $pattern2, 'import com.minicad.step.model.'
            $matches2 = ([regex]::Matches($content, $pattern2)).Count
            $totalCount += $matches2
            $fileChanged = $true
            Write-Host "Fixed $matches2 sub-package imports in: $($file.FullName)"
        }

        if ($fileChanged) {
            Set-Content -Path $file.FullName -Value $newContent -NoNewline
        }
    }
}

Write-Host "Total import statements fixed: $totalCount"