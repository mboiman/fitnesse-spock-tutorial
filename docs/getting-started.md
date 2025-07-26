# Getting Started Guide 🚀

**Der einfachste Weg, um mit professionellem Testing zu starten!**

Dieses Tutorial führt Sie in **5 Minuten** von Null zu laufenden Tests mit automatischem CI/CD und Live-Reports.

## 🎯 Was Sie lernen werden

- ✅ **Spock Tests** schreiben und verstehen
- ✅ **FitNesse Tests** für Business-Akzeptanz
- ✅ **CI/CD Pipeline** mit GitHub Actions
- ✅ **Live Test Reports** auf GitHub Pages

## 📋 Voraussetzungen

| Tool | Version | Zweck | Installation |
|------|---------|-------|--------------|
| **Java** | 17+ | Runtime | [adoptium.net](https://adoptium.net/) |
| **Git** | Latest | Versionskontrolle | [git-scm.com](https://git-scm.com/) |
| **IDE** | Optional | Development | IntelliJ IDEA empfohlen |
| **Browser** | Aktuell | FitNesse UI & Reports | Chrome/Firefox/Safari |

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

### 2. ⚡ 5-Minuten Schnellstart

```bash
# Repository klonen
git clone https://github.com/mboiman/fitnesse-spock-tutorial.git
cd fitnesse-spock-tutorial

# Alles bauen und testen (5 Min)
./gradlew build test

# 🎉 Fertig! Tests sind gelaufen
```

**Das war's!** Sie haben gerade:
- ✅ 39 Spock Tests ausgeführt
- ✅ JaCoCo Coverage Report generiert  
- ✅ Spring Boot App getestet
- ✅ Build erfolgreich abgeschlossen

### 3. 📊 Live Demo anschauen

**[🌐 Test Reports Dashboard](https://mboiman.github.io/fitnesse-spock-tutorial/)**

Schauen Sie sich die automatisch generierten Test-Reports an:
- 📊 **Spock Test Results** - Unit/Integration Tests
- 🎭 **FitNesse Results** - Acceptance Tests mit schönem Viewer  
- 📈 **Coverage Reports** - Code-Abdeckung mit JaCoCo

## 🧪 Tests lokal ausführen

### 🔬 Spock Tests (Entwickler-Tests)

```bash
# 📊 Alle Tests + Coverage Report
./gradlew test jacocoTestReport

# 🎯 Spezifische Tests
./gradlew test --tests "*BookServiceSpec"     # Ein Test
./gradlew test --tests "*Spec"               # Alle Unit Tests
./gradlew test --tests "*IntegrationSpec"    # Alle Integration Tests

# 📈 Reports anschauen
open build/reports/tests/test/index.html     # Test Results
open build/reports/jacoco/test/html/index.html # Coverage
```

### 🎭 FitNesse Tests (Business-Tests)

```bash
# 🚀 FitNesse Server starten
./gradlew fitnesseStart

# 🌐 Im Browser öffnen
open http://localhost:9090

# 📋 Direkt zu den Tests:
open http://localhost:9090/BookstoreTests    # Alle Tests
open http://localhost:9090/PriceCalculations # Preis-Tests
```

**💡 Tipp**: FitNesse läuft als Wiki-Server. Sie können Tests im Browser bearbeiten und ausführen!

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

## 🎓 Von Neuling zum Profi - Lernpfad

### 🟢 Level 1: Verstehen (15 Min)

**1. Spock Test verstehen**
```groovy
// src/test/groovy/.../BookServiceSpec.groovy
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

**2. FitNesse Test verstehen**
```
!|de.tutorial.fixtures.BookSearchFixture|
|suchbegriff|anzahl gefundene bücher?|
|Java       |5                       |
|Python     |3                       |
|Cobol      |0                       |
```

### 🟡 Level 2: Anwenden (30 Min)

**3. Beispiel-App erkunden**
Die Online-Buchhandlung bietet:
- 📚 **Bücherverwaltung** via REST API
- 💰 **Preisberechnung** mit Mengenrabatten  
- 🚚 **Versandkosten** nach Gewicht/Land
- 🛒 **Bestellprozess** End-to-End

**4. Tests ausführen & Reports verstehen**
- 📊 **Spock Reports**: Welche Tests pass/fail?
- 🎭 **FitNesse Viewer**: Business-Test Ergebnisse
- 📈 **Coverage**: Welcher Code ist getestet?

### 🔴 Level 3: Meistern (60 Min)

**5. Eigene Tests schreiben**
1. **Spock Test**: Neue `*Spec.groovy` Datei erstellen
2. **FitNesse Test**: Neue Wiki-Seite im Browser erstellen  
3. **Fixture**: Java-Klasse im `fixtures` Package implementieren

**6. CI/CD verstehen**
- ⚙️ **GitHub Actions**: Wie funktioniert die Pipeline?
- 📊 **Auto-Deploy**: Wie kommen Reports auf GitHub Pages?
- 🔄 **Workflow**: Push → Build → Test → Deploy

## 🚀 Was als Nächstes?

### 👨‍💻 Für Entwickler
- 🏗️ **[Architecture Guide](architecture.md)** - System-Design verstehen
- 🧪 **[Testing Guide](testing-guide.md)** - Detaillierte Test-Anleitungen  
- ⭐ **[Best Practices](best-practices.md)** - Professionelle Patterns

### 🏢 Für Teams
- 📋 **Repository forken** als Template nutzen
- ⚙️ **CI/CD Pipeline** automatisch übernehmen
- 📊 **GitHub Pages** für eigene Test-Reports aktivieren

### 🆘 Probleme?
1. 🔧 **[Troubleshooting](troubleshooting.md)** - Häufige Probleme lösen
2. 📝 **[GitHub Issues](https://github.com/mboiman/fitnesse-spock-tutorial/issues)** - Bug Reports
3. 💬 **[Discussions](https://github.com/mboiman/fitnesse-spock-tutorial/discussions)** - Fragen stellen

## 🌟 Success Stories

**Was andere mit diesem Tutorial erreicht haben:**
- ✅ **Moderne Test-Architektur** in Legacy-Projekten eingeführt
- ✅ **Acceptance Testing** für Business-Stakeholder etabliert  
- ✅ **CI/CD Pipeline** mit automatischen Test-Reports aufgebaut
- ✅ **Test-driven Development** im Team implementiert

---

**🎯 Tipp für Neulinge**: Starten Sie mit dem 5-Minuten Schnellstart, schauen Sie sich die Live-Demo an, und arbeiten Sie dann den Lernpfad durch. So verstehen Sie das System am schnellsten!