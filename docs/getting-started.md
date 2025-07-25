# Getting Started Guide 🚀

Dieses Tutorial führt Sie durch die Installation und ersten Schritte mit dem FitNesse & Spock Test-Framework.

## 📋 Voraussetzungen

- **Java 17+** (empfohlen: OpenJDK 17)
- **Git** für Versionskontrolle
- **IDE** mit Groovy-Support (IntelliJ IDEA empfohlen)
- **Browser** für FitNesse Wiki-Interface

## 🔧 Installation

### 1. Java 17 Setup

**macOS (Homebrew):**
```bash
brew install openjdk@17
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="$JAVA_HOME/bin:$PATH"
```

**Linux:**
```bash
sudo apt-get install openjdk-17-jdk
export JAVA_HOME="/usr/lib/jvm/java-17-openjdk"
export PATH="$JAVA_HOME/bin:$PATH"
```

**Windows:**
1. Download OpenJDK 17 von [adoptium.net](https://adoptium.net/)
2. Setze `JAVA_HOME` in den Systemvariablen
3. Füge `%JAVA_HOME%\bin` zum PATH hinzu

### 2. Projekt klonen

```bash
git clone https://github.com/your-org/fitnesse-spock-tutorial.git
cd fitnesse-spock-tutorial
```

### 3. Projekt bauen

```bash
# Gradle Wrapper macht die Installation von Gradle überflüssig
./gradlew build

# Auf Windows:
gradlew.bat build
```

## 🧪 Erste Tests ausführen

### Spock Tests (Unit & Integration)

```bash
# Alle Tests ausführen
./gradlew test

# Nur Unit Tests
./gradlew test --tests "*Spec"

# Nur Integration Tests  
./gradlew test --tests "*IntegrationSpec"

# Mit Test-Report
./gradlew test jacocoTestReport
# Report: build/reports/tests/test/index.html
```

### FitNesse Tests (Acceptance)

```bash
# FitNesse Server starten
./gradlew fitnesseStart

# Browser öffnen
open http://localhost:9090

# Oder direkt zu den Tests:
open http://localhost:9090/BookstoreTests
```

## 🏗️ Projekt-Struktur

```
fitnesse-spock-tutorial/
├── src/
│   ├── main/java/           # Spring Boot Application
│   │   └── de/tutorial/bookstore/
│   │       ├── controller/  # REST APIs
│   │       ├── service/     # Business Logic
│   │       └── model/       # Domain Models
│   │
│   └── test/
│       ├── groovy/         # Spock Tests
│       │   └── de/tutorial/bookstore/
│       │       ├── unit/   # Unit Tests (*Spec.groovy)
│       │       └── integration/ # Integration Tests
│       │
│       ├── java/           # FitNesse Fixtures
│       │   └── de/tutorial/fixtures/
│       │
│       └── fitnesse/       # FitNesse Wiki Pages
│           └── FitNesseRoot/
│               ├── BookstoreTests/
│               └── FixtureGallery/
│
├── build.gradle            # Build Configuration
├── gradle.properties       # Gradle Settings
└── docs/                  # Dokumentation
```

## 🎯 Nächste Schritte

### 1. Einen einfachen Spock Test verstehen

```groovy
// src/test/groovy/de/tutorial/bookstore/service/BookServiceSpec.groovy
class BookServiceSpec extends Specification {
    
    def "sollte Bücher nach Titel finden"() {
        given: "Ein BookService mit Test-Daten"
        def service = new BookService()
        
        when: "Nach 'Java' gesucht wird"
        def result = service.findByTitle("Java")
        
        then: "Werden Java-Bücher gefunden"
        result.size() > 0
        result.every { it.title.contains("Java") }
    }
}
```

### 2. Einen FitNesse Test verstehen

```
!|de.tutorial.fixtures.BookSearchFixture|
|suchbegriff|anzahl gefundene bücher?|
|Java       |5                       |
|Python     |3                       |
|Cobol      |0                       |
```

### 3. Die Beispiel-Anwendung erkunden

Die Beispiel-Anwendung ist eine Online-Buchhandlung mit:
- REST API für Bücherverwaltung
- Preisberechnung mit Mengenrabatten
- Versandkostenberechnung
- Bestellverwaltung

### 4. Eigene Tests schreiben

1. **Spock Test hinzufügen**: Erstelle eine neue `*Spec.groovy` Datei
2. **FitNesse Test hinzufügen**: Erstelle eine neue Wiki-Seite im Browser
3. **Fixture implementieren**: Erstelle eine Java-Klasse im fixtures Package

## 📚 Weiterführende Dokumentation

- [Architecture Guide](architecture.md) - Verstehen Sie die Systemarchitektur
- [Testing Guide](testing-guide.md) - Detaillierte Test-Framework Anleitungen
- [Best Practices](best-practices.md) - Professionelle Patterns und Tipps

## ❓ Hilfe & Support

Bei Problemen:
1. Konsultieren Sie [Troubleshooting](troubleshooting.md)
2. Prüfen Sie die [FAQ](troubleshooting.md#faq)
3. Erstellen Sie ein GitHub Issue

---

**Tipp**: Beginnen Sie mit dem Ausführen der existierenden Tests, bevor Sie eigene schreiben!