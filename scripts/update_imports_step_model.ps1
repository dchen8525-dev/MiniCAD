# Update all import statements referencing step.model sub-packages
# Uses UTF-8 encoding to preserve file encoding
$sourceDirs = @("src/main/java", "src/test/java")

# Pattern to match sub-package imports (e.g., base.StepEntity, profile.*, etc.)
# This captures the entire import line up to and including the sub-package path
$pattern = 'import com\.minicad\.step\.model\.([a-z_]+(\.[a-z_]+)*)\.'
$replacement = 'import com.minicad.step.model.'

$totalCount = 0
foreach ($dir in $sourceDirs) {
    $files = Get-ChildItem -Path $dir -Filter "*.java" -Recurse -File
    foreach ($file in $files) {
        $content = Get-Content $file.FullName -Raw -Encoding UTF8
        if ($content -match $pattern) {
            $newContent = $content -replace $pattern, $replacement
            # Use UTF-8 encoding without BOM for Java source files
            $utf8NoBom = New-Object System.Text.UTF8Encoding $false
            [System.IO.File]::WriteAllText($file.FullName, $newContent, $utf8NoBom)
            $count = ([regex]::Matches($content, $pattern)).Count
            $totalCount += $count
            Write-Host "Updated $count imports in: $($file.FullName)"
        }
    }
}

Write-Host "Total import statements updated: $totalCount"