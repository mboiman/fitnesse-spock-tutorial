#!/bin/bash

echo "FitNesse Finale Lösung"
echo "====================="

# Java Environment
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"

# Stop any running FitNesse
pkill -f fitnesse 2>/dev/null
sleep 2

# Compile project
echo "1. Kompiliere Projekt..."
./gradlew clean compileJava compileTestJava

# Build classpath
echo -e "\n2. Erstelle Classpath..."
CP="build/classes/java/main:build/classes/java/test:build/classes/groovy/main:build/classes/groovy/test"

# Add all dependency JARs
for jar in ~/.gradle/caches/modules-2/files-2.1/*/*/*/*.jar; do
    CP="$CP:$jar"
done

# Find FitNesse JAR
FITNESSE_JAR=$(find ~/.gradle/caches -name "fitnesse-20250223-standalone.jar" | head -1)

echo -e "\n3. Starte FitNesse Server..."
echo "   URL: http://localhost:9090"
echo ""
echo "Verfügbare Tests:"
echo "  - http://localhost:9090/SimpleTest"
echo "  - http://localhost:9090/DemoTest" 
echo "  - http://localhost:9090/PriceCalculations"
echo ""
echo "WICHTIG: Die Tests müssen im Browser ausgeführt werden!"
echo "         Klicke auf 'Test' auf der jeweiligen Seite."
echo ""

# Start FitNesse with full classpath
java -cp "$CP:$FITNESSE_JAR" fitnesseMain.FitNesseMain -p 9090 -d src/test/fitnesse