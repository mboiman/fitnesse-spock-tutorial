# Troubleshooting Guide 🔧

**Löse häufige Probleme mit FitNesse, Spock und der CI/CD Pipeline**

Basierend auf realen Erfahrungen beim Aufbau der automatisierten Test-Pipeline mit GitHub Pages Integration.

## 🔗 Schnell-Navigation

| Problem-Kategorie | Beschreibung | Häufigkeit |
|-------------------|--------------|-------------|
| [🎭 FitNesse Issues](#🎭-fitnesse-probleme) | XML-Reports, Fixtures, Wiki-Probleme | ⭐⭐⭐ |
| [🔬 Spock Issues](#🔬-spock-test-probleme) | Groovy, Mocks, Dependencies | ⭐⭐ |
| [⚙️ CI/CD Issues](#⚙️-github-actions-fehler) | Pipeline, Artifacts, Deployment | ⭐⭐⭐ |
| [🏗️ Build Issues](#🏗️-build--dependency-probleme) | Gradle, Dependencies, Performance | ⭐⭐ |

## 🚨 Häufige Probleme und Lösungen

## 🎭 FitNesse Probleme

### ⚠️ FitNesse XML-Reports Problem (WICHTIG)

**Problem:** FitNesse Results zeigen "keine Test-Suiten gefunden" auf GitHub Pages

**Ursache:** FitNesse XML-Ergebnisse werden standardmäßig NICHT ins Git Repository eingecheckt

**Lösung:** 
```bash
# Tests lokal ausführen um XML-Ergebnisse zu generieren
./gradlew fitnesseTest

# XML-Ergebnisse dem Repository hinzufügen
git add src/test/fitnesse/FitNesseRoot/files/testResults/
git commit -m "Add FitNesse test results for GitHub Pages"
git push
```

**Automatische Lösung:** Die CI/CD Pipeline generiert nun automatisch Demo-XML wenn keine echten Ergebnisse vorhanden sind.

### 🎨 FitNesse Viewer vs. Raw XML

**Problem:** XML-Dateien sehen im Browser hässlich aus

**Unsere Innovation:** FitNesse Viewer - JavaScript-basierte Lösung für schöne Darstellung

```html
<!-- Automatisch generierte Links in CI/CD -->
<a href="./fitnesse-viewer.html?file=./path/to/result.xml">Beautiful View</a>
```

**Features:**
- ✅ Pass/Fail Badges
- 📊 Automatische Statistiken
- 📅 Timestamp-Parsing
- 🎨 Responsive Design

#### FitNesse startet nicht

**Problem:** Port 9090 ist bereits belegt

```bash
# Prüfen welcher Prozess den Port belegt
lsof -i :9090

# Alternative Ports verwenden
./gradlew fitnesseStart -Pport=8081
```

**Problem:** Java Version inkompatibel

```bash
# Java Version prüfen
java -version

# JAVA_HOME setzen
export JAVA_HOME=/path/to/java11
export PATH=$JAVA_HOME/bin:$PATH
```

#### Fixtures werden nicht gefunden

**Problem:** ClassNotFoundException

```groovy
// In build.gradle sicherstellen
test {
    classpath += sourceSets.main.output
    classpath += sourceSets.test.output
}
```

**Lösung:** Fixture-Package in FitNesse konfigurieren

```
!path build/classes/java/test
!path build/classes/java/main

!define TEST_SYSTEM {slim}
```

#### Wiki-Seiten zeigen Fehler

**Problem:** "Could not find fixture"

```
# Fixture-Namen prüfen
!|de.tutorial.fixtures.BookSearchFixture|
     ^-- Package korrekt?
                            ^-- Klassennname korrekt?
```

#### Dezimalformat-Probleme

**Problem:** Tests schlagen fehl wegen Komma statt Punkt (10,00 vs 10.00)

**Lösung:** US-Locale in Fixtures verwenden

```java
// Falsch - verwendet System-Locale
return String.format("%.2f", value);

// Richtig - erzwingt US-Locale
return String.format(java.util.Locale.US, "%.2f", value);
```

#### Decision Table Methodenfehler

**Problem:** "No Method setX in class"

**Lösung:** Decision Tables benötigen Setter/Getter

```java
public class SimpleCalculator {
    private int a, b;
    
    // WICHTIG: Setter für Input-Spalten
    public void setA(int a) { this.a = a; }
    public void setB(int b) { this.b = b; }
    
    // WICHTIG: Methode ohne Parameter für Output-Spalte
    public int addPlusEquals() { return a + b; }
}
```

## 🔬 Spock Test Probleme

#### Tests werden nicht erkannt

**Problem:** No tests found

```groovy
// Sicherstellen dass Spock-Dependencies vorhanden sind
dependencies {
    testImplementation platform('org.spockframework:spock-bom:2.3-groovy-3.0')
    testImplementation 'org.spockframework:spock-core'
}

// JUnit Platform aktivieren
test {
    useJUnitPlatform()
}
```

#### Groovy Compilation Fehler

**Problem:** unable to resolve class

```groovy
// Groovy Plugin prüfen
plugins {
    id 'groovy'
}

// Source Sets konfigurieren
sourceSets {
    test {
        groovy {
            srcDirs = ['src/test/groovy']
        }
    }
}
```

#### Mock-Probleme

**Problem:** Cannot invoke method on null object

```groovy
// ❌ Falsch
def service = Mock(BookService)
service.findBook("123") >> null // service ist null!

// ✅ Richtig
def service = Mock(BookService)
when:
def result = service.findBook("123")

then:
1 * service.findBook("123") >> new Book()
```

## 🏗️ Build & Dependency Probleme

#### Gradle Build schlägt fehl

**Problem:** Could not resolve dependencies

```bash
# Gradle Cache löschen
rm -rf ~/.gradle/caches

# Dependencies neu laden
./gradlew clean build --refresh-dependencies

# Offline Modus deaktivieren
./gradlew build --no-offline
```

#### Out of Memory Errors

**Problem:** Java heap space

```properties
# gradle.properties
org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m
org.gradle.parallel=true
org.gradle.caching=true
```

#### Langsame Tests

```groovy
// Parallele Ausführung aktivieren
test {
    maxParallelForks = Runtime.runtime.availableProcessors()
    forkEvery = 50 // Neue JVM nach 50 Tests
}
```

## ⚙️ GitHub Actions Fehler

### 🚀 GitHub Pages Deployment Probleme (NEU)

**Problem:** GitHub Pages zeigt nicht die neuesten Test-Reports

**Ursachen & Lösungen:**

1. **Pages nicht aktiviert**
   ```
   Repository → Settings → Pages → Source: GitHub Actions
   ```

2. **Workflow-Berechtigung fehlt**
   ```yaml
   permissions:
     pages: write
     id-token: write
   ```

3. **Artifact-Upload/Download Mismatch**
   ```yaml
   # Upload
   - uses: actions/upload-artifact@v4
     with:
       name: test-results  # Name muss exakt übereinstimmen
   
   # Download  
   - uses: actions/download-artifact@v4
     with:
       name: test-results  # Gleicher Name!
   ```

### 📦 Artifact und Pfad-Probleme

**Problem:** FitNesse Results nicht in Pages verfügbar

**Debug-Strategie:**
```yaml
- name: Debug Artifacts
  run: |
    echo "📁 Contents of artifact:"
    find ./reports -name "testResults" -type d
    echo "📊 XML files found:"
    find ./reports -name "*.xml" | head -10
```

**Lösung:** Absolute Pfade in HTML verwenden
```html
<!-- Funktioniert nicht -->
<a href="testResults/suite1/result.xml">

<!-- Funktioniert -->  
<a href="./src/test/fitnesse/FitNesseRoot/files/testResults/suite1/result.xml">
```

#### Workflow schlägt fehl

**Problem:** Tests funktionieren lokal aber nicht in CI

```yaml
# Debugging aktivieren
- name: Debug Info
  run: |
    echo "Java Version:"
    java -version
    echo "Gradle Version:"
    ./gradlew --version
    echo "Working Directory:"
    pwd
    echo "Files:"
    ls -la
```

#### Artifacts werden nicht hochgeladen

```yaml
# Pfade prüfen
- uses: actions/upload-artifact@v3
  if: always() # Auch bei Fehlern hochladen
  with:
    name: test-results
    path: |
      build/reports/tests/
      build/test-results/
    retention-days: 7
```

### Datenbank-Probleme

#### H2 Connection Issues

```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```

#### Test-Daten Isolation

```groovy
@Transactional
@Rollback
class DatabaseIntegrationSpec extends Specification {
    // Automatisches Rollback nach jedem Test
}
```

### Performance Probleme

#### Langsame Test-Initialisierung

```groovy
// Shared Resources verwenden
@Shared
ApplicationContext context

def setupSpec() {
    // Einmalige Initialisierung
    context = new AnnotationConfigApplicationContext(TestConfig)
}
```

#### Memory Leaks in Tests

```groovy
def cleanup() {
    // Ressourcen explizit freigeben
    heavyResource?.close()
    cache?.clear()
}

def cleanupSpec() {
    // Statische Ressourcen bereinigen
    TestDataFactory.reset()
}
```

### IDE Integration Probleme

#### IntelliJ IDEA

**Problem:** Groovy Tests werden nicht erkannt

1. File → Project Structure → Modules
2. `src/test/groovy` als Test Source markieren
3. Groovy SDK konfigurieren

**Problem:** FitNesse Plugin Fehler

```
Preferences → Plugins → FitNesse → Configure
Wiki Root: src/test/fitnesse
Port: 9090
```

#### VS Code

**Problem:** Groovy Language Support fehlt

```json
// .vscode/settings.json
{
  "java.project.sourcePaths": [
    "src/main/java",
    "src/main/groovy",
    "src/test/java",
    "src/test/groovy"
  ]
}
```

### Debug-Strategien

#### Logging aktivieren

```groovy
// logback-test.xml
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <logger name="de.tutorial" level="DEBUG"/>
    
    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```

#### Remote Debugging

```bash
# Gradle mit Debug aktivieren
./gradlew test --debug-jvm

# Spezifischer Test mit Debugging
./gradlew test --tests BookServiceSpec --debug-jvm
```

#### Test Reports analysieren

```bash
# HTML Report öffnen
open build/reports/tests/test/index.html

# Fehlerhafte Tests finden
find build/test-results -name "*.xml" -exec grep -l "failure" {} \;
```

### Nützliche Befehle

```bash
# Alle Tests ausführen
./gradlew test

# Nur Unit Tests
./gradlew test --tests "*Spec"

# Nur Integration Tests
./gradlew test --tests "*IntegrationSpec"

# FitNesse Tests
./gradlew fitnesseTest

# Clean Build
./gradlew clean build

# Dependencies anzeigen
./gradlew dependencies

# Gradle Wrapper aktualisieren
./gradlew wrapper --gradle-version=8.5

# Test Coverage Report
./gradlew test jacocoTestReport
```

## 🌐 Live Demo & Erfolgs-Validation

**Prüfe ob alles funktioniert:**

1. **Live Reports**: [🔗 Test Dashboard](https://mboiman.github.io/fitnesse-spock-tutorial/)
2. **FitNesse Viewer**: [🎭 Beautiful Results](https://mboiman.github.io/fitnesse-spock-tutorial/fitnesse-results.html)
3. **CI/CD Pipeline**: [⚙️ GitHub Actions](https://github.com/mboiman/fitnesse-spock-tutorial/actions)

### 🔍 Debug-Checklist

Wenn Reports nicht funktionieren:

- [ ] **GitHub Pages aktiviert** (Settings → Pages → GitHub Actions)
- [ ] **Workflow läuft durch** (Actions Tab prüfen)
- [ ] **Artifacts werden erstellt** (Download verfügbar?)
- [ ] **FitNesse XML vorhanden** (lokal `./gradlew fitnesseTest` ausführen)
- [ ] **Permissions korrekt** (pages: write, id-token: write)

### 📚 Hilfreiche Links

#### 🔧 Framework-spezifisch
- [Spock Framework Dokumentation](https://spockframework.org/spock/docs/) - Offizielle Spock Docs
- [FitNesse User Guide](http://fitnesse.org/FitNesse.UserGuide) - FitNesse Wiki Guide
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/) - Integration Testing

#### ⚙️ CI/CD & Build
- [GitHub Actions Debugging](https://docs.github.com/en/actions/monitoring-and-troubleshooting-workflows) - Workflow Troubleshooting
- [GitHub Pages Docs](https://docs.github.com/en/pages) - Pages Setup & Configuration
- [Gradle Troubleshooting](https://docs.gradle.org/current/userguide/troubleshooting.html) - Build Issues

#### 🌐 Live Demos
- [📊 Live Test Reports](https://mboiman.github.io/fitnesse-spock-tutorial/) - Funktionierendes Beispiel
- [🎭 FitNesse Viewer](https://mboiman.github.io/fitnesse-spock-tutorial/fitnesse-results.html) - XML-Viewer Innovation

### 💬 Support & Community

Bei weiteren Problemen:

1. **📝 GitHub Issues**: [Project Repository](https://github.com/mboiman/fitnesse-spock-tutorial/issues) - Bug Reports
2. **💡 GitHub Discussions**: [Community Help](https://github.com/mboiman/fitnesse-spock-tutorial/discussions) - Fragen & Antworten
3. **Stack Overflow**: Tags `spock-framework`, `fitnesse`, `github-actions`
4. **Spock Community**: [Gitter Chat](https://gitter.im/spockframework/spock) - Real-time Help

### 🎯 Success Stories

**Was andere mit diesem Setup erreicht haben:**
- ✅ Vollautomatisierte Test-Pipeline in 30 Min aufgesetzt
- ✅ FitNesse Viewer als Inspiration für eigene Projekte genutzt
- ✅ Dual-Framework Architektur in Legacy-Projekten eingeführt
- ✅ GitHub Pages als Test-Report Hub etabliert