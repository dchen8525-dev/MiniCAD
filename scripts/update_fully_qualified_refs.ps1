# Update fully qualified class references in Java code
# (e.g., com.minicad.step.model.geometry.StepCartesianPoint -> com.minicad.step.model.StepCartesianPoint)
$sourceDirs = @("src/main/java", "src/test/java")

# Pattern to match fully qualified class names with old sub-package structure
$pattern = 'com\.minicad\.step\.model\.([a-z_]+(\.[a-z_]+)*)\.([A-Z][a-zA-Z0-9]*)'
$replacement = 'com.minicad.step.model.$3'

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
            Write-Host "Updated $count fully qualified references in: $($file.FullName)"
        }
    }
}

Write-Host "Total fully qualified references updated: $totalCount"