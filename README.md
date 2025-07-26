# FitNesse & Spock Test-Framework Tutorial 🧪

Ein praktisches Tutorial für professionelle Test-Frameworks mit **FitNesse** (Acceptance Testing) und **Spock** (BDD Unit Testing). Demonstriert moderne Test-Architektur mit automatisiertem CI/CD und GitHub Pages Deployment.

## 🌐 Live Demo

**🔗 [Test Reports Dashboard](https://mboiman.github.io/fitnesse-spock-tutorial/)**  
Sehe dir die automatisch generierten Test-Berichte live an!

## 🚀 Schnellstart für Neulinge

### 1️⃣ Voraussetzungen
```bash
# Java 17 installieren (macOS mit Homebrew)
brew install openjdk@17

# Java Umgebung setzen
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="$JAVA_HOME/bin:$PATH"
```

### 2️⃣ Projekt klonen & starten
```bash
# Repository klonen
git clone https://github.com/mboiman/fitnesse-spock-tutorial.git
cd fitnesse-spock-tutorial

# Alles bauen und testen
./gradlew build test

# ✨ Das war's! Tests sind gelaufen
```

### 3️⃣ Was ist passiert?
- ✅ **39 Spock Tests** für Unit/Integration Testing
- ✅ **JaCoCo Coverage Report** für Code-Abdeckung
- ✅ **Build erfolgreich** - alles funktioniert!

## 🎯 Test-Status & Live Reports

| Framework | Tests | Status | Live Report |
|-----------|-------|--------|-------------|
| **Spock** | 39/39 | ✅ Passing | [📊 Spock Reports](https://mboiman.github.io/fitnesse-spock-tutorial/build/reports/tests/test/index.html) |
| **FitNesse** | 72/72 | ✅ Passing | [🎭 FitNesse Results](https://mboiman.github.io/fitnesse-spock-tutorial/fitnesse-results.html) |
| **Coverage** | 89% | ✅ Good | [📈 Coverage Report](https://mboiman.github.io/fitnesse-spock-tutorial/build/reports/jacoco/test/html/index.html) |

## 🎓 Lernpfad für Neulinge

### Für Einsteiger (5 Min)
1. 🚀 **[Schnellstart](#-schnellstart-für-neulinge)** - Projekt zum Laufen bringen
2. 🌐 **[Live Demo](#-live-demo)** - Test-Reports anschauen
3. 🏗️ **[Architektur verstehen](#-wichtige-architektur-info)**

### Für Entwickler (15 Min)
4. 🧪 **[Testing Guide](docs/testing-guide.md)** - Wie die Tests funktionieren
5. 📖 **[Spock Beispiele](docs/examples/spock-tests.md)** - BDD Tests verstehen
6. 🎭 **[FitNesse Fixtures](docs/examples/fitnesse-fixtures.md)** - Business-Tests schreiben

### Für Fortgeschrittene (30 Min)
7. 🏗️ **[Architecture Guide](docs/architecture.md)** - Projekt-Design verstehen
8. ⭐ **[Best Practices](docs/best-practices.md)** - Professionelle Patterns
9. 🔧 **[Troubleshooting](docs/troubleshooting.md)** - Probleme lösen

## 🎯 Was macht dieses Projekt besonders?

### ✨ Moderne Test-Architektur
- **🔄 CI/CD Pipeline** - Automatisierte Tests mit GitHub Actions
- **📊 Live Reports** - Test-Ergebnisse auf GitHub Pages
- **🎭 FitNesse Viewer** - Schöne Darstellung der XML-Test-Ergebnisse
- **📈 Coverage Tracking** - JaCoCo Integration mit Reports

### 🏗️ Professionelle Integration
- **Dual-Framework**: FitNesse (Business) + Spock (Developer)
- **Spring Boot**: Moderne Microservice-Architektur
- **Gradle Build**: Professionelles Build-System
- **Docker Ready**: Containerization Support

### ⚠️ Wichtige Architektur-Info

**FitNesse und Spock sind vollständig getrennte Test-Frameworks:**
- ❌ FitNesse Fixtures rufen **NICHT** Spock Tests auf
- ✅ Beide testen **unabhängig** dieselben Spring Services  
- 🔺 Folgt der **Test-Pyramide**: Spock (Unit/Integration) ↔ FitNesse (Acceptance)

```
┌─────────────────┐    ┌─────────────────┐
│   FitNesse      │    │      Spock      │
│  (Business)     │    │   (Developer)   │
└─────────┬───────┘    └─────────┬───────┘
          │                      │
          └──────┬─────┬─────────┘
                 │     │
         ┌───────▼─────▼───────┐
         │   Spring Services   │
         │   (BookService,     │
         │   PriceService...)  │
         └─────────────────────┘
```

## 📦 Technologie-Stack

| Komponente | Version | Zweck | Status |
|------------|---------|-------|--------|
| Spring Boot | 3.5.3 | Application Framework | ✅ Production Ready |
| Spock | 2.4-M6 | BDD Testing Framework | ✅ 39 Tests Passing |
| FitNesse | 20250223 | Acceptance Testing | ✅ Live Viewer |
| Java | 17+ | Runtime | ✅ LTS Support |
| Groovy | 4.0.24 | Test Language | ✅ Latest Stable |
| GitHub Actions | v4 | CI/CD Pipeline | ✅ Auto Deploy |
| GitHub Pages | - | Live Test Reports | ✅ Online Demo |

## 🚀 Nächste Schritte

### Als Neuling starten?
1. **[▶️ Schnellstart befolgen](#-schnellstart-für-neulinge)** - 5 Minuten Setup
2. **[🌐 Live Demo anschauen](https://mboiman.github.io/fitnesse-spock-tutorial/)** - Sehe Ergebnisse
3. **[📖 Testing Guide lesen](docs/testing-guide.md)** - Verstehe die Tests

### Als Entwickler einsteigen?
1. **[🏗️ Architektur verstehen](docs/architecture.md)** - System-Design
2. **[⭐ Best Practices lernen](docs/best-practices.md)** - Professionelle Patterns
3. **[🔧 Probleme lösen](docs/troubleshooting.md)** - Häufige Issues

### Eigenes Projekt aufsetzen?
1. **Repository forken** und als Template nutzen
2. **CI/CD Pipeline** automatisch übernehmen
3. **GitHub Pages** für eigene Test-Reports aktivieren

---

**🌟 Star das Repository, wenn es dir hilft!**  
*Für Support siehe [Issues](https://github.com/mboiman/fitnesse-spock-tutorial/issues) oder [Discussions](https://github.com/mboiman/fitnesse-spock-tutorial/discussions)*