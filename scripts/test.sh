#!/bin/bash

# エラーが発生したら停止
set -e

echo "--- 🛠️ Compiling Main Sources ---"
mkdir -p build/classes
# すべてのJavaファイルをリストアップしてコンパイル（簡易版）
find src/main/java -name "*.java" > sources.txt
javac -d build/classes -encoding UTF-8 @sources.txt
rm sources.txt

echo "--- 🛠️ Compiling Test Sources ---"
mkdir -p build/test-classes
find src/test/java -name "*.java" > test-sources.txt
javac -d build/test-classes -encoding UTF-8 -cp "lib/junit-platform-console-standalone.jar:build/classes" @test-sources.txt
rm test-sources.txt

echo "--- 🧪 Running JUnit 5 Tests ---"
# JUnit Platform Console Launcher を実行
java -jar lib/junit-platform-console-standalone.jar \
    --class-path build/classes \
    --class-path build/test-classes \
    --scan-class-path \
    --details=tree \
    --reports-dir=build/test-reports

echo "✅ All tests passed!"
