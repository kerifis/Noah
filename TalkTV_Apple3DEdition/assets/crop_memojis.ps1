Add-Type -AssemblyName System.Drawing

$srcDir = "C:\Users\jkare\.gemini\antigravity-ide\brain\26f457f5-cbd9-4afd-a2d0-c5dc657449c4\.user_uploaded"
$outDir = "c:\Users\jkare\Documents\NOAH\TalkTV_Apple3DEdition\assets\memojis"
if (!(Test-Path $outDir)) { New-Item -ItemType Directory -Force -Path $outDir }

function Crop-And-Transparent {
    param(
        [System.Drawing.Bitmap]$sourceBmp,
        [int]$x, [int]$y, [int]$w, [int]$h,
        [string]$outputPath
    )

    $rect = New-Object System.Drawing.Rectangle($x, $y, $w, $h)
    $cropped = $sourceBmp.Clone($rect, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    
    # Process transparency for pure white / near-white background
    for ($px = 0; $px -lt $w; $px++) {
        for ($py = 0; $py -lt $h; $py++) {
            $color = $cropped.GetPixel($px, $py)
            # If near white
            if ($color.R -ge 245 -and $color.G -ge 245 -and $color.B -ge 245) {
                $cropped.SetPixel($px, $py, [System.Drawing.Color]::FromArgb(0, 255, 255, 255))
            } elseif ($color.R -ge 235 -and $color.G -ge 235 -and $color.B -ge 235) {
                # Smooth antialias feathering
                $alpha = [int](255 - (($color.R + $color.G + $color.B) / 3 - 235) * (255 / 10))
                if ($alpha -lt 0) { $alpha = 0 }
                if ($alpha -gt 255) { $alpha = 255 }
                $cropped.SetPixel($px, $py, [System.Drawing.Color]::FromArgb($alpha, $color.R, $color.G, $color.B))
            }
        }
    }

    $cropped.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)
    $cropped.Dispose()
    Write-Output "Saved: $outputPath"
}

# Image 2: media_1791320676038.png
$img2Path = Join-Path $srcDir "media_1791320676038.png"
if (Test-Path $img2Path) {
    $bmp2 = New-Object System.Drawing.Bitmap($img2Path)

    # 1. Noah boy with freckles (Row 1, Col 1) -> Target for "I" / "Noah"
    Crop-And-Transparent $bmp2 295 185 200 200 (Join-Path $outDir "memoji_noah_boy.png")

    # 2. Grandfather with blue hat and glasses (Row 0, Col 1) -> Target for "Grandpa"
    Crop-And-Transparent $bmp2 285 0 210 190 (Join-Path $outDir "memoji_grandpa_bluehat.png")

    # 3. Woman in purple hijab (Row 0, Col 2) -> Target for "Teacher / Aunt"
    Crop-And-Transparent $bmp2 510 0 200 190 (Join-Path $outDir "memoji_woman_hijab.png")

    # 4. Dreadlocks man with red glasses (Row 0, Col 3) -> Target for "Dad / Uncle"
    Crop-And-Transparent $bmp2 735 0 215 195 (Join-Path $outDir "memoji_dad_dreadlocks.png")

    # 5. Blonde woman with sunglasses (Row 1, Col 0) -> Target for "Mom / Sister"
    Crop-And-Transparent $bmp2 50 195 205 195 (Join-Path $outDir "memoji_woman_blonde.png")

    # 6. Red-haired boy with green glasses (Row 1, Col 2) -> Target for "Friend / Peer"
    Crop-And-Transparent $bmp2 545 190 200 200 (Join-Path $outDir "memoji_boy_green_glasses.png")

    # 7. Girl with fedora hat (Row 1, Col 3) -> Target for "Girl / Friend"
    Crop-And-Transparent $bmp2 770 190 200 200 (Join-Path $outDir "memoji_girl_fedora.png")

    # 8. Smiling woman with yellow earrings (Row 0, Col 0)
    Crop-And-Transparent $bmp2 55 0 200 195 (Join-Path $outDir "memoji_woman_earrings.png")

    # 9. Man with yellow turban (Row 2, Col 3)
    Crop-And-Transparent $bmp2 780 380 200 195 (Join-Path $outDir "memoji_man_turban.png")

    # 10. Curly afro woman with glasses (Row 2, Col 2)
    Crop-And-Transparent $bmp2 520 380 205 195 (Join-Path $outDir "memoji_woman_curly_afro.png")

    $bmp2.Dispose()
}

# Image 1: media_1791320549334.png
$img1Path = Join-Path $srcDir "media_1791320549334.png"
if (Test-Path $img1Path) {
    $bmp1 = New-Object System.Drawing.Bitmap($img1Path)

    # 11. White-haired man with mustache (Row 0, Col 0)
    Crop-And-Transparent $bmp1 70 70 200 205 (Join-Path $outDir "memoji_elder_man_mustache.png")

    # 12. Auburn hair woman (Row 0, Col 1) -> Target for "Mom / Auburn"
    Crop-And-Transparent $bmp1 280 70 205 205 (Join-Path $outDir "memoji_auburn_woman.png")

    # 13. Orange hair man (Row 0, Col 2) -> Target for "You / Brother"
    Crop-And-Transparent $bmp1 510 70 200 205 (Join-Path $outDir "memoji_orange_hair_man.png")

    # 14. Brown curly hair woman (Row 0, Col 3)
    Crop-And-Transparent $bmp1 720 70 210 205 (Join-Path $outDir "memoji_curly_hair_woman.png")

    # 15. Brown skin curly hair man (Row 1, Col 0) -> Target for "Zayd (Teacher)"
    Crop-And-Transparent $bmp1 70 300 200 210 (Join-Path $outDir "memoji_brown_skin_man.png")

    # 16. Bald smiling man (Row 1, Col 3)
    Crop-And-Transparent $bmp1 740 310 200 200 (Join-Path $outDir "memoji_bald_dark_man.png")

    $bmp1.Dispose()
}

Write-Output "All Apple Memoji avatars extracted and saved successfully!"
