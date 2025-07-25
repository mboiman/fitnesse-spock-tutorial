# Best Practices für FitNesse und Spock 🎯

Basierend auf aktuellen Industry Standards (2024) und praktischen Erfahrungen.

## 📋 Inhaltsverzeichnis

1. [Test-Organisation](#test-organisation)
2. [FitNesse Best Practices](#fitnesse-best-practices)
3. [Spock Best Practices](#spock-best-practices)
4. [CI/CD Integration](#cicd-integration)
5. [Performance-Optimierung](#performance-optimierung)
6. [Wartbarkeit](#wartbarkeit)

## Test-Organisation

### Struktur nach Test-Ebenen

```
src/test/
├── unit/           # Schnelle, isolierte Tests
├── integration/    # Tests mit externen Systemen
├── functional/     # End-to-End Tests
└── performance/    # Last- und Performance-Tests
```

### Naming Conventions

**Test-Klassen:**
```groovy
// ✅ Gut
BookServiceSpec
OrderControllerIntegrationSpec
BookstoreE2ESpec

// ❌ Schlecht
TestBookService
BookTest
Test1
```

**Test-Methoden:**
```groovy
// ✅ Gut
def "sollte Bücher nach Kategorie filtern können"()
def "wirft Exception wenn Buch nicht gefunden wird"()

// ❌ Schlecht
def "test1"()
def "bookTest"()
```

## FitNesse Best Practices

### 1. Wiki-Struktur

```
FitNesseRoot/
├── FrontPage/              # Übersicht
├── TestSuites/            # Test-Sammlungen
│   ├── AcceptanceTests/
│   ├── RegressionTests/
│   └── SmokeTests/
├── TestData/              # Gemeinsame Testdaten
└── Documentation/         # Anleitungen
```

### 2. Fixture-Design

**Single Responsibility:**
```java
// ✅ Gut - Eine klare Aufgabe
public class BookSearchFixture {
    public List<Book> searchByTitle(String title) { }
    public List<Book> searchByAuthor(String author) { }
}

// ❌ Schlecht - Zu viele Verantwortlichkeiten
public class BookstoreFixture {
    public void searchBooks() { }
    public void createOrder() { }
    public void manageInventory() { }
}
```

**Aussagekräftige Rückgabewerte:**
```java
// ✅ Gut
public String bestellungAbschliessen() {
    try {
        Order order = orderService.complete(currentOrder);
        return "Erfolgreich: Bestellnummer " + order.getNumber();
    } catch (Exception e) {
        return "Fehler: " + e.getMessage();
    }
}

// ❌ Schlecht
public boolean processOrder() {
    return true; // Keine Details bei Fehler
}
```

### 3. Test-Daten Management

**SetUp und TearDown nutzen:**
```
!|SetUpFixture|
|testdaten laden|
|test umgebung vorbereiten|

!|TestFixture|
|... eigentliche Tests ...|

!|TearDownFixture|
|test daten bereinigen|
|verbindungen schliessen|
```

**Daten-Isolation:**
```java
public class TestDataFixture {
    private String testPrefix = "TEST_" + System.currentTimeMillis();
    
    public void createTestBook(String title) {
        // Prefix verhindert Kollisionen
        book.setIsbn(testPrefix + "_" + generateIsbn());
    }
}
```

### 4. Lesbare Tests

**Tabellenformatierung:**
```
!|Bestellvorgang|
|Schritt                    |Aktion                        |Ergebnis?            |
|Kunde anmelden            |mit Email test@example.com    |Erfolgreich          |
|Buch zum Warenkorb        |ISBN 978-3-16-148410-0       |Hinzugefügt          |
|Menge ändern              |auf 2 Stück                   |Aktualisiert         |
|Zur Kasse gehen           |mit Kreditkarte               |Bestellung B-12345   |
```

### 5. Variablen und Includes

**Gemeinsame Konfiguration:**
```
!define TEST_SYSTEM {slim}
!define ADMIN_USER {admin@bookstore.de}
!define ADMIN_PASS {secure123}

!include .TestData.CommonBooks
```

## Spock Best Practices

### 1. Given-When-Then Struktur

```groovy
def "sollte Rabatt für Premium-Kunden berechnen"() {
    given: "Ein Premium-Kunde mit einer Bestellung"
    def customer = new Customer(type: CustomerType.PREMIUM)
    def order = new Order(customer: customer, total: 100.0)
    
    when: "Der Rabatt berechnet wird"
    def discount = discountService.calculate(order)
    
    then: "Erhält der Kunde 20% Rabatt"
    discount == 20.0
}
```

### 2. Data-Driven Tests

```groovy
@Unroll
def "Versandkosten für #land mit Bestellwert #betrag sind #kosten EUR"() {
    expect:
    shippingService.calculate(betrag, land) == kosten
    
    where:
    betrag | land          | kosten
    25.00  | "Deutschland" | 4.95
    60.00  | "Deutschland" | 0.00
    50.00  | "Österreich"  | 7.95
    50.00  | "Schweiz"     | 12.95
}
```

### 3. Sinnvolle Mocks

```groovy
def "sollte Email bei erfolgreicher Bestellung senden"() {
    given: "Mock Services"
    def emailService = Mock(EmailService)
    def orderService = new OrderService(emailService: emailService)
    
    when: "Bestellung abgeschlossen wird"
    orderService.complete(order)
    
    then: "Wird genau eine Bestätigungs-Email gesendet"
    1 * emailService.sendConfirmation(order.customerEmail, _) >> { email, content ->
        assert content.contains(order.orderNumber)
    }
}
```

### 4. Aussagekräftige Assertions

```groovy
// ✅ Gut - Klare Fehlermeldungen
then: "Bestellung enthält korrekte Details"
with(order) {
    items.size() == 3
    totalAmount == 89.95
    status == OrderStatus.CONFIRMED
    deliveryDate - new Date() <= 5
}

// ❌ Schlecht - Unklare Fehler
then:
assert order.items.size() == 3
assert order.totalAmount == 89.95
```

### 5. Test-Helfer

```groovy
class BookstoreSpecHelper {
    static Book createTestBook(Map params = [:]) {
        new Book(
            isbn: params.isbn ?: "978-3-16-148410-0",
            title: params.title ?: "Test Buch",
            price: params.price ?: 29.99,
            stock: params.stock ?: 10
        )
    }
    
    static Customer createPremiumCustomer() {
        new Customer(
            type: CustomerType.PREMIUM,
            email: "premium@test.de",
            discountRate: 0.2
        )
    }
}
```

## CI/CD Integration

### GitHub Actions Optimierung

```yaml
name: Optimized Test Pipeline

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    strategy:
      matrix:
        test-suite: [unit, integration, acceptance]
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Cache Dependencies
      uses: actions/cache@v3
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*') }}
    
    - name: Run ${{ matrix.test-suite }} Tests
      run: ./gradlew test${{ matrix.test-suite }}
      
    - name: Upload Test Reports
      if: always()
      uses: actions/upload-artifact@v3
      with:
        name: test-reports-${{ matrix.test-suite }}
        path: build/reports/tests/
```

### Parallele Ausführung

```groovy
// build.gradle
test {
    // Parallele Ausführung
    maxParallelForks = Runtime.runtime.availableProcessors()
    
    // Test-Kategorisierung
    useJUnitPlatform {
        includeTags System.getProperty('test.suite', 'unit')
    }
}

tasks.register('testUnit') {
    systemProperty 'test.suite', 'unit'
}

tasks.register('testIntegration') {
    systemProperty 'test.suite', 'integration'
}
```

## Performance-Optimierung

### 1. Test-Geschwindigkeit

```groovy
// Gemeinsame Ressourcen wiederverwenden
@Shared
def expensiveResource = new ExpensiveResource()

def setupSpec() {
    // Einmalige teure Initialisierung
}

def cleanupSpec() {
    // Einmaliges Aufräumen
}
```

### 2. Selektive Test-Ausführung

```bash
# Nur geänderte Tests ausführen
./gradlew test --continuous

# Spezifische Test-Klasse
./gradlew test --tests BookServiceSpec

# Test-Pattern
./gradlew test --tests "*Integration*"
```

### 3. Test-Timeouts

```groovy
@Timeout(value = 2, unit = TimeUnit.SECONDS)
def "schneller Test"() {
    expect:
    service.quickOperation() == "result"
}

@Timeout(30)
class SlowIntegrationSpec extends Specification {
    // Alle Tests haben 30 Sekunden Timeout
}
```

## Wartbarkeit

### 1. Test-Dokumentation

```groovy
@Title("Buchverwaltungs-Service Spezifikation")
@Narrative("""
Als Buchhandlung
möchte ich Bücher verwalten können
um meinen Kunden ein aktuelles Sortiment anzubieten
""")
class BookServiceSpec extends Specification {
    
    @Subject
    BookService bookService
    
    @Issue("JIRA-1234")
    def "sollte doppelte ISBNs verhindern"() {
        // Test implementation
    }
}
```

### 2. Gemeinsame Test-Konfiguration

```groovy
// BaseSpec.groovy
abstract class BaseSpec extends Specification {
    @Shared
    ApplicationContext context
    
    def setupSpec() {
        context = new TestApplicationContext()
    }
    
    def cleanup() {
        // Automatisches Cleanup nach jedem Test
        DatabaseCleaner.clean()
    }
}
```

### 3. Test-Daten Builder

```groovy
class TestDataBuilder {
    static OrderBuilder order() {
        new OrderBuilder()
    }
    
    static class OrderBuilder {
        Order order = new Order()
        
        OrderBuilder withCustomer(Customer c) {
            order.customer = c
            this
        }
        
        OrderBuilder withItems(int count) {
            count.times {
                order.addItem(createRandomBook())
            }
            this
        }
        
        Order build() {
            order
        }
    }
}

// Verwendung
def order = TestDataBuilder.order()
    .withCustomer(premiumCustomer)
    .withItems(3)
    .build()
```

### 4. Fehleranalyse

```groovy
// Hilfreiche Fehlerausgaben
def "komplexe Berechnung"() {
    when:
    def result = service.calculate(input)
    
    then:
    verifyAll(result) {
        it.total == expected.total
        it.tax == expected.tax
        it.discount == expected.discount
    }
    
    where:
    input << loadTestData()
}

// Report-Anhänge
def cleanup() {
    if (specificationContext.currentIteration.dataVariables) {
        reportInfo "Test-Daten: ${specificationContext.currentIteration.dataVariables}"
    }
}
```

## Checkliste für Code Reviews

### FitNesse Tests
- [ ] Wiki-Seiten sind gut strukturiert
- [ ] Fixtures haben klare Verantwortlichkeiten
- [ ] Test-Daten werden isoliert
- [ ] SetUp/TearDown wird korrekt verwendet
- [ ] Tests sind selbstdokumentierend

### Spock Tests
- [ ] Given-When-Then Struktur eingehalten
- [ ] Aussagekräftige Test-Namen
- [ ] Sinnvolle Verwendung von Mocks
- [ ] Data-Driven Tests wo angebracht
- [ ] Keine Test-Interdependenzen

### Allgemein
- [ ] Tests laufen schnell
- [ ] Tests sind deterministisch
- [ ] Keine hardcodierten Werte
- [ ] Gute Test-Abdeckung
- [ ] CI/CD Integration funktioniert