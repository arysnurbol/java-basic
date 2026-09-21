# Практиканы компиляциялап, тексерісті жүгіртеді.
#
#   .\check.ps1          -> 2-глава, барлық тапсырма
#   .\check.ps1 3        -> 2-глава, тек 3-тапсырма
#   .\check.ps1 3 5 7    -> 2-глава, бірнешеуі
#   .\check.ps1 ch3      -> 3-глава, барлық тапсырма
#   .\check.ps1 ch3 12   -> 3-глава, тек 12-тапсырма

$ErrorActionPreference = "Stop"
chcp 65001 > $null
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

$root = Split-Path -Parent $MyInvocation.MyCommand.Definition
$out = Join-Path $root "out"

# Аргументтерді бөлеміз: "ch3"/"ch03" -> глава, қалғаны -> тапсырма нөмірлері
$chapter = "ch02"
$tasks = @()
foreach ($arg in $args) {
    if ("$arg" -match '^ch0?(\d+)$') {
        $chapter = "ch{0:D2}" -f [int]$Matches[1]
    } else {
        $tasks += "$arg"
    }
}

$runner = "$chapter." + $chapter.Substring(0, 1).ToUpper() + $chapter.Substring(1) + "Runner"

if (-not (Test-Path (Join-Path $root "src\$chapter"))) {
    Write-Host "Мұндай глава жоқ: src\$chapter" -ForegroundColor Red
    exit 1
}

if (Test-Path $out) { Remove-Item -Recurse -Force $out }
New-Item -ItemType Directory -Force -Path $out | Out-Null

$sources = Get-ChildItem -Path (Join-Path $root "src") -Recurse -Filter *.java | ForEach-Object { $_.FullName }

javac -encoding UTF-8 -d $out $sources
if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "Компиляция қатесі. Жоғарыдағы хабарламаларды оқы." -ForegroundColor Red
    exit 1
}

java "-Dfile.encoding=UTF-8" "-Dsun.stdout.encoding=UTF-8" -cp $out $runner @tasks
exit $LASTEXITCODE
