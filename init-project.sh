#!/bin/bash

echo "🚀 Initialisiere FitNesse-Spock Tutorial Projekt..."

# Gradle Wrapper initialisieren
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "📥 Lade Gradle Wrapper herunter..."
    curl -L https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar \
         -o gradle/wrapper/gradle-wrapper.jar
fi

# Ausführbar machen
chmod +x gradlew

# Projekt bauen
echo "🏗️ Baue Projekt..."
./gradlew build -x test

echo "✅ Projekt erfolgreich initialisiert!"
echo ""
echo "Nächste Schritte:"
echo "1. Spock Tests ausführen: ./gradlew test"
echo "2. FitNesse starten: ./gradlew fitnesseStart"
echo "3. Browser öffnen: http://localhost:9090"