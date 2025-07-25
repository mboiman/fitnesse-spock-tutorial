# FitNesse & Spock Test-Framework Tutorial 🧪

Ein praktisches Tutorial für professionelle Test-Frameworks mit FitNesse (Acceptance Testing) und Spock (BDD Unit Testing).

## 🚀 Quick Start

```bash
# Java 17 Setup (macOS)
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="$JAVA_HOME/bin:$PATH"

# Build & Test
./gradlew build
./gradlew test           # Spock Tests (39/39 ✅)
./gradlew fitnesseStart  # FitNesse Server → http://localhost:9090
```

## 📊 Test-Status

| Framework | Tests | Status | Beschreibung |
|-----------|-------|--------|--------------|
| **Spock** | 39/39 | ✅ Passing | Unit & Integration Tests |
| **FitNesse** | 72/72 | ✅ Passing | Acceptance Tests |

## 📚 Dokumentation

- 🎯 [**Getting Started**](docs/getting-started.md) - Installation, Setup und erste Schritte
- 🏗️ [**Architecture Guide**](docs/architecture.md) - Projektstruktur, Design und Datenflüsse
- 🧪 [**Testing Guide**](docs/testing-guide.md) - Spock & FitNesse im Detail
- 🔧 [**Troubleshooting**](docs/troubleshooting.md) - Häufige Probleme und Lösungen
- ⭐ [**Best Practices**](docs/best-practices.md) - Empfehlungen und Design Patterns

### 📖 Tutorials & Beispiele

- [FitNesse Fixtures](docs/examples/fitnesse-fixtures.md) - Über 10 Fixture-Typen mit Beispielen
- [Spock Tests](docs/examples/spock-tests.md) - BDD Tests mit Groovy

## 🎯 Projekt-Highlights

- **Dual-Framework**: Zeigt Integration von FitNesse (Business-Tests) und Spock (Developer-Tests)
- **Spring Boot**: Moderne Microservice-Architektur
- **Best Practices**: Professionelle Patterns für beide Test-Frameworks
- **Vollständige Beispiele**: Von einfachen Unit-Tests bis zu komplexen Acceptance-Tests

### ⚠️ Wichtiger Hinweis zur Architektur

**FitNesse und Spock sind vollständig getrennte Test-Frameworks:**
- FitNesse Fixtures rufen **NICHT** Spock Tests auf
- Beide Frameworks testen unabhängig dieselben Spring Services
- Dies folgt der Test-Pyramide: Spock (Unit/Integration) und FitNesse (Acceptance)

## 📦 Technologie-Stack

| Komponente | Version | Zweck |
|------------|---------|-------|
| Spring Boot | 3.5.3 | Application Framework |
| Spock | 2.4-M6 | BDD Testing Framework |
| FitNesse | 20250223 | Acceptance Testing |
| Java | 17+ | Runtime |
| Groovy | 4.0.24 | Test Language |

---

*Für detaillierte Informationen siehe die [vollständige Dokumentation](docs/getting-started.md).*