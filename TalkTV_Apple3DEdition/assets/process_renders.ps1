Add-Type -AssemblyName System.Drawing

$srcDir = "C:\Users\jkare\.gemini\antigravity-ide\brain\26f457f5-cbd9-4afd-a2d0-c5dc657449c4"
$outDir = "c:\Users\jkare\Documents\NOAH\TalkTV_Apple3DEdition\assets\renders"
if (!(Test-Path $outDir)) { New-Item -ItemType Directory -Force -Path $outDir }

function Make-Transparent-Render {
    param(
        [string]$inputPath,
        [string]$outputPath
    )

    if (!(Test-Path $inputPath)) { 
        Write-Warning "File not found: $inputPath"
        return 
    }

    $sourceBmp = New-Object System.Drawing.Bitmap($inputPath)
    $w = $sourceBmp.Width
    $h = $sourceBmp.Height
    $rect = New-Object System.Drawing.Rectangle(0, 0, $w, $h)
    $result = $sourceBmp.Clone($rect, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    
    # Process transparency for pure white / near-white background
    for ($px = 0; $px -lt $w; $px++) {
        for ($py = 0; $py -lt $h; $py++) {
            $color = $result.GetPixel($px, $py)
            if ($color.R -ge 248 -and $color.G -ge 248 -and $color.B -ge 248) {
                $result.SetPixel($px, $py, [System.Drawing.Color]::FromArgb(0, 255, 255, 255))
            } elseif ($color.R -ge 235 -and $color.G -ge 235 -and $color.B -ge 235) {
                $alpha = [int](255 - (($color.R + $color.G + $color.B) / 3 - 235) * (255 / 13))
                if ($alpha -lt 0) { $alpha = 0 }
                if ($alpha -gt 255) { $alpha = 255 }
                $result.SetPixel($px, $py, [System.Drawing.Color]::FromArgb($alpha, $color.R, $color.G, $color.B))
            }
        }
    }

    $result.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)
    $result.Dispose()
    $sourceBmp.Dispose()
    Write-Output "Processed 3D render to: $outputPath"
}

Make-Transparent-Render (Join-Path $srcDir "apple_3d_monkey_1791322328816.jpg") (Join-Path $outDir "apple_3d_monkey.png")
Make-Transparent-Render (Join-Path $srcDir "apple_3d_pancake_1791322348608.jpg") (Join-Path $outDir "apple_3d_pancakes.png")
Make-Transparent-Render (Join-Path $srcDir "apple_3d_hands_1791322370477.jpg") (Join-Path $outDir "apple_3d_want_hands.png")

Write-Output "All 3D textured renders processed successfully!"
