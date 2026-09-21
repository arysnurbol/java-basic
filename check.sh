#!/usr/bin/env bash
# Практиканы компиляциялап, тексерісті жүгіртеді.
#
#   ./check.sh          -> 2-глава, барлық тапсырма
#   ./check.sh 3        -> 2-глава, тек 3-тапсырма
#   ./check.sh ch3      -> 3-глава, барлық тапсырма
#   ./check.sh ch3 12   -> 3-глава, тек 12-тапсырма

set -e
cd "$(dirname "$0")"

chapter="ch02"
tasks=()
for arg in "$@"; do
    if [[ "$arg" =~ ^ch0?([0-9]+)$ ]]; then
        chapter=$(printf "ch%02d" "${BASH_REMATCH[1]}")
    else
        tasks+=("$arg")
    fi
done

if [ ! -d "src/$chapter" ]; then
    echo "Мұндай глава жоқ: src/$chapter" >&2
    exit 1
fi

runner="$chapter.$(echo "$chapter" | sed 's/^\(.\)/\U\1/')Runner"

rm -rf out
mkdir -p out

find src -name '*.java' > out/sources.txt
javac -encoding UTF-8 -d out @out/sources.txt

java -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -cp out "$runner" "${tasks[@]}"
