#!/bin/bash

# Simple FitNesse test runner
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"

echo "Starting FitNesse server on http://localhost:9090..."
echo "Press Ctrl+C to stop the server"
echo ""

# Compile first
./gradlew compileJava compileTestJava

# Start FitNesse
./gradlew fitnesseStart