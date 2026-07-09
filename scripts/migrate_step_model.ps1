# PowerShell script to move all step.model sub-package files to root
$targetDir = "src/main/java/com/minicad/step/model"

# Get all sub-package directories (exclude the root model directory itself)
$subdirs = Get-ChildItem -Path $targetDir -Directory | Where-Object { $_.Name -ne "model" }

foreach ($subdir in $subdirs) {
    $files = Get-ChildItem -Path $subdir.FullName -Filter "*.java"
    foreach ($file in $files) {
        $destPath = Join-Path $targetDir $file.Name
        # Use git mv to preserve history
        git mv $file.FullName $destPath
        Write-Host "Moved: $($file.Name) from $($subdir.Name)"
    }
}

Write-Host "Total files moved: $(($subdirs | ForEach-Object { (Get-ChildItem $_.FullName -Filter '*.java').Count } | Measure-Object -Sum).Sum)"