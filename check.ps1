# Глава 2 практикасын компиляциялап, тексерісті жүгіртеді.
#
#   .\check.ps1        -> барлық тапсырма
#   .\check.ps1 3      -> тек 3-тапсырма
#   .\check.ps1 3 5 7  -> бірнешеуі

$ErrorActionPreference = "Stop"
chcp 65001 > $null
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

$root = Split-Path -Parent $MyInvocation.MyCommand.Definition
$out = Join-Path $root "out"

if (Test-Path $out) { Remove-Item -Recurse -Force $out }
New-Item -ItemType Directory -Force -Path $out | Out-Null

$sources = Get-ChildItem -Path (Join-Path $root "src") -Recurse -Filter *.java | ForEach-Object { $_.FullName }

javac -encoding UTF-8 -d $out $sources
if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "Компиляция қатесі. Жоғарыдағы хабарламаларды оқы." -ForegroundColor Red
    exit 1
}

java "-Dfile.encoding=UTF-8" "-Dsun.stdout.encoding=UTF-8" -cp $out ch02.Ch02Runner @args
exit $LASTEXITCODE
