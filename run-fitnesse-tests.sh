#!/bin/bash

# Set Java environment
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"

echo "Running FitNesse Test Suite..."
echo "=============================="

# Compile the project first
echo "1. Compiling project..."
./gradlew compileJava compileTestJava

# Start FitNesse server in background
echo -e "\n2. Starting FitNesse server..."
./gradlew fitnesseStart &
FITNESSE_PID=$!

# Wait for server to start
echo "   Waiting for server to start..."
sleep 5

# Run the tests
echo -e "\n3. Running FitNesse tests..."
./gradlew fitnesseTest

# Also run individual tests
echo -e "\n4. Running individual tests..."
./gradlew fitnessePriceTest

# Kill the server
echo -e "\n5. Stopping FitNesse server..."
kill $FITNESSE_PID 2>/dev/null

echo -e "\nTest execution completed!"