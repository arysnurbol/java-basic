#!/usr/bin/env bash
# Глава 2 практикасын компиляциялап, тексерісті жүгіртеді.
#
#   ./check.sh        -> барлық тапсырма
#   ./check.sh 3      -> тек 3-тапсырма

set -e
cd "$(dirname "$0")"

rm -rf out
mkdir -p out

find src -name '*.java' > out/sources.txt
javac -encoding UTF-8 -d out @out/sources.txt

java -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -cp out ch02.Ch02Runner "$@"
