# PowerShell script to move all step.model sub-package files to root (including nested)
$targetDir = "src/main/java/com/minicad/step/model"
$count = 0

# Get all .java files in sub-directories (any depth)
$files = Get-ChildItem -Path $targetDir -Filter "*.java" -Recurse | Where-Object {
    $_.DirectoryName -ne $targetDir
}

foreach ($file in $files) {
    $destPath = Join-Path $targetDir $file.Name
    # Use git mv to preserve history
    git mv $file.FullName $destPath
    $count++
    Write-Host "Moved: $($file.Name) from $($file.Directory.Name)"
}

Write-Host "Total files moved: $count"