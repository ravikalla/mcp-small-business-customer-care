#!/bin/bash
set -e
cd "$(dirname "$0")"

# Build classpath with all dependencies
CLASSPATH="target/classes"
for jar in target/lib/*.jar; do
    CLASSPATH="$CLASSPATH:$jar"
done

# Run directly with Java, bypassing Maven
exec java -cp "$CLASSPATH" com.ravikalla.McpServerLauncher 2>/dev/null