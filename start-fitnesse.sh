#!/bin/bash

# FitNesse Server Starter
echo "Starting FitNesse Server..."

# Set Java environment
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"

# Find FitNesse JAR
FITNESSE_JAR=$(find ~/.gradle/caches -name "fitnesse-20250223-standalone.jar" 2>/dev/null | head -1)

if [ -z "$FITNESSE_JAR" ]; then
    echo "FitNesse JAR not found. Running gradle to download..."
    ./gradlew compileTestJava
    FITNESSE_JAR=$(find ~/.gradle/caches -name "fitnesse-20250223-standalone.jar" 2>/dev/null | head -1)
fi

echo "Using FitNesse JAR: $FITNESSE_JAR"

# Prepare classpath
CP="build/classes/java/main:build/classes/java/test:build/classes/groovy/main:build/classes/groovy/test"

# Add all JARs from gradle cache
for jar in $(find ~/.gradle/caches/modules-2/files-2.1 -name "*.jar" | grep -E "(spring|groovy|h2)" | head -20); do
    CP="$CP:$jar"
done

# Start FitNesse
echo "Starting FitNesse on http://localhost:9090"
echo "Press Ctrl+C to stop"
echo ""
echo "Available test pages:"
echo "  - http://localhost:9090/FrontPage"
echo "  - http://localhost:9090/DemoTest"
echo "  - http://localhost:9090/PriceCalculations"
echo "  - http://localhost:9090/ShippingCosts"
echo ""

java -cp "$CP:$FITNESSE_JAR" fitnesseMain.FitNesseMain -p 9090 -d $(pwd)/src/test/fitnesse