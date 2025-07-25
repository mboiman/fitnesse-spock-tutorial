# Architecture Guide 🏗️

Dieses Dokument beschreibt die Architektur und das Design der FitNesse-Spock Tutorial-Anwendung.

## 🎯 Überblick

Das Projekt demonstriert die Integration zweier Test-Frameworks in einer Spring Boot Anwendung:

```mermaid
graph TB
    subgraph "Test Layer"
        FT[FitNesse Tests<br/>Business Acceptance]
        ST[Spock Tests<br/>Developer BDD]
    end
    
    subgraph "Application Layer"
        API[REST Controllers]
        SVC[Business Services]
        REPO[Repositories]
    end
    
    subgraph "Data Layer"
        H2[(H2 Database<br/>In-Memory)]
    end
    
    FT --> API
    ST --> SVC
    ST --> API
    API --> SVC
    SVC --> REPO
    REPO --> H2
    
    style FT fill:#e1f5fe
    style ST fill:#f3e5f5
    style API fill:#fff3e0
    style SVC fill:#e8f5e9
    style REPO fill:#fce4ec
    style H2 fill:#f5f5f5
```

## 🔄 Test-Framework Integration

### ⚠️ Wichtige Architektur-Klarstellung

**FitNesse und Spock sind vollständig voneinander getrennte Test-Frameworks:**

- **KEINE Verbindung zwischen den Frameworks**: FitNesse Fixtures rufen niemals Spock Tests auf
- **Unabhängige Testausführung**: Beide Frameworks können separat und unabhängig voneinander laufen
- **Gemeinsamer Zugriff auf Services**: Beide testen dieselben Spring Services, aber auf unterschiedliche Weise

Diese Trennung ist bewusstes Design und folgt der Test-Pyramide:
- **Spock**: Unit/Integration Tests (Basis der Pyramide) - viele schnelle Tests
- **FitNesse**: Acceptance Tests (Spitze der Pyramide) - wenige umfassende Tests

### Datenfluss zwischen Frameworks

```mermaid
sequenceDiagram
    participant User
    participant FitNesse
    participant Fixture
    participant SpringService
    participant SpockTest
    participant Database
    
    User->>FitNesse: Führt Wiki-Test aus
    FitNesse->>Fixture: Ruft Fixture-Methode
    Fixture->>SpringService: Verwendet Service
    SpringService->>Database: Datenzugriff
    Database-->>SpringService: Daten
    SpringService-->>Fixture: Ergebnis
    Fixture-->>FitNesse: Formatiertes Ergebnis
    FitNesse-->>User: Test-Report
    
    Note over SpockTest: Unabhängig davon (KEINE Verbindung!)
    SpockTest->>SpringService: Testet gleichen Service
    SpringService->>Database: Datenzugriff
    Database-->>SpockTest: Mock/Real Data
```

## 📦 Komponenten-Architektur

### Spring Boot Application

```mermaid
classDiagram
    class BookController {
        +getAllBooks()
        +getBookById(id)
        +createBook(book)
        +updateBook(id, book)
        +deleteBook(id)
    }
    
    class BookService {
        +findAll()
        +findById(id)
        +findByTitle(title)
        +save(book)
        +delete(id)
        +calculatePrice(book, quantity)
    }
    
    class PriceCalculationService {
        +calculateQuantityDiscount(price, quantity)
        +calculateShippingCost(orderValue)
        +calculateVAT(price)
        +calculateFinalPrice(price, quantity)
    }
    
    class BookRepository {
        +findAll()
        +findById(id)
        +save(book)
        +delete(book)
    }
    
    class Book {
        -Long id
        -String isbn
        -String title
        -String author
        -BigDecimal price
        -Integer stock
    }
    
    BookController --> BookService
    BookController --> PriceCalculationService
    BookService --> BookRepository
    BookRepository --> Book
```

### Test-Architektur

```mermaid
graph LR
    subgraph "Spock Tests"
        UT[Unit Tests<br/>*Spec.groovy]
        IT[Integration Tests<br/>*IntegrationSpec.groovy]
        MT[Mock Tests]
    end
    
    subgraph "FitNesse Tests"
        DT[Decision Tables]
        ST[Script Tables]
        QT[Query Tables]
    end
    
    subgraph "Fixtures"
        SF[Simple Fixtures]
        SPF[Spring-Aware Fixtures]
        DTF[Decision Table Fixtures]
    end
    
    DT --> DTF
    ST --> SF
    QT --> SPF
    
    UT --> MT
    IT --> SPF
```

## 🗂️ Package-Struktur

### Hauptanwendung

```
de.tutorial.bookstore/
├── BookstoreApplication.java      # Spring Boot Main
├── controller/
│   ├── BookController.java       # REST Endpoints
│   └── OrderController.java      # Order Management
├── service/
│   ├── BookService.java         # Book Business Logic
│   ├── OrderService.java        # Order Processing
│   └── PriceCalculationService.java # Pricing Rules
├── repository/
│   ├── BookRepository.java      # Book Data Access
│   └── OrderRepository.java     # Order Data Access
└── model/
    ├── Book.java               # Book Entity
    ├── Order.java              # Order Entity
    └── OrderItem.java          # Order Line Items
```

### Test-Struktur

```
test/
├── groovy/de/tutorial/bookstore/
│   ├── service/
│   │   ├── BookServiceSpec.groovy
│   │   ├── PriceCalculationServiceSpec.groovy
│   │   └── OrderServiceSpec.groovy
│   ├── controller/
│   │   └── BookControllerIntegrationSpec.groovy
│   └── e2e/
│       └── BookstoreE2ESpec.groovy
│
├── java/de/tutorial/fixtures/
│   ├── SimpleCalculator.java
│   ├── BookSearchFixture.java
│   ├── PriceCalculationDecisionTable.java
│   └── SpringAwarePriceCalculationFixture.java
│
└── fitnesse/FitNesseRoot/
    ├── BookstoreTests/
    │   ├── SimpleCalculatorTest/
    │   ├── PriceCalculations/
    │   └── ShippingCosts/
    └── FixtureGallery/
```

## 💾 Datenmodell

```mermaid
erDiagram
    BOOK {
        Long id PK
        String isbn UK
        String title
        String author
        BigDecimal price
        Integer stock
        LocalDateTime created_at
        LocalDateTime updated_at
    }
    
    ORDER {
        Long id PK
        String order_number UK
        Long customer_id FK
        BigDecimal total_amount
        String status
        LocalDateTime order_date
    }
    
    ORDER_ITEM {
        Long id PK
        Long order_id FK
        Long book_id FK
        Integer quantity
        BigDecimal price
        BigDecimal discount
    }
    
    CUSTOMER {
        Long id PK
        String email UK
        String name
        String type
        BigDecimal discount_rate
    }
    
    ORDER ||--o{ ORDER_ITEM : contains
    ORDER_ITEM }o--|| BOOK : references
    ORDER }o--|| CUSTOMER : placed_by
```

## 🔧 Konfiguration

### Spring Profiles

```yaml
# application.yml
spring:
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}

---
# Development Profile
spring:
  config:
    activate:
      on-profile: dev
  h2:
    console:
      enabled: true
  datasource:
    url: jdbc:h2:mem:devdb

---
# Test Profile
spring:
  config:
    activate:
      on-profile: test
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
```

### Build-Konfiguration

```groovy
// build.gradle - Wichtige Konfigurationen
test {
    useJUnitPlatform()
    maxParallelForks = Runtime.runtime.availableProcessors()
}

fitnesse {
    port = 9090
    root = 'src/test/fitnesse'
    workingDir = 'build/fitnesse'
}
```

## 🚀 Deployment-Architektur

```mermaid
graph TB
    subgraph "Development"
        IDE[IDE/Editor]
        LOCAL[Local Tests]
    end
    
    subgraph "CI/CD Pipeline"
        GIT[Git Repository]
        CI[GitHub Actions]
        BUILD[Gradle Build]
        TEST[Test Execution]
        REPORT[Test Reports]
    end
    
    subgraph "Artifacts"
        JAR[Spring Boot JAR]
        DOC[Documentation]
        COV[Coverage Reports]
    end
    
    IDE --> GIT
    GIT --> CI
    CI --> BUILD
    BUILD --> TEST
    TEST --> REPORT
    TEST --> JAR
    TEST --> COV
    
    style IDE fill:#e3f2fd
    style CI fill:#f3e5f5
    style JAR fill:#e8f5e9
```

## 🔐 Sicherheitsaspekte

1. **Keine Produktionsdaten** in Tests
2. **In-Memory H2** für Isolation
3. **Separate Test-Profile** 
4. **Keine Credentials** im Code

## 📈 Performance-Überlegungen

- **Parallele Test-Ausführung** für Spock
- **Lazy Loading** in Spring Services
- **Connection Pooling** mit HikariCP
- **Test-Daten-Isolation** pro Test

## 🔗 Weiterführende Links

- [Testing Guide](testing-guide.md) - Details zu Test-Strategien
- [Best Practices](best-practices.md) - Architektur-Patterns
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spock Framework](https://spockframework.org/)
- [FitNesse Wiki](http://fitnesse.org/)