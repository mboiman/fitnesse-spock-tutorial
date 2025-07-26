# Architecture Guide 🏗️

**Verstehen Sie das moderne Test-Framework Design mit CI/CD und Live-Reports**

Dieses Dokument erklärt die Architektur der **FitNesse-Spock Tutorial-Anwendung** mit Fokus auf professionelle Test-Integration und automatisierte Deployment-Pipeline.

## 🎯 System-Überblick

Ein **Dual-Framework Test-System** mit automatischer CI/CD Pipeline und GitHub Pages Integration:

```mermaid
graph TB
    subgraph "🔄 CI/CD Pipeline"
        GHA[GitHub Actions<br/>Build & Test]
        GHP[GitHub Pages<br/>Live Reports]
    end
    
    subgraph "🧪 Test Layer"
        FT[FitNesse Tests<br/>🎭 Business Acceptance]
        ST[Spock Tests<br/>🔬 Developer BDD]
        FV[FitNesse Viewer<br/>📊 XML → HTML]
    end
    
    subgraph "📱 Application Layer"
        API[REST Controllers<br/>📡 HTTP Endpoints]
        SVC[Business Services<br/>⚙️ Core Logic]
        REPO[Repositories<br/>💾 Data Access]
    end
    
    subgraph "💽 Data Layer"
        H2[(H2 Database<br/>🗄️ In-Memory)]
    end
    
    GHA --> GHP
    FT --> FV
    FT --> API
    ST --> SVC
    ST --> API
    API --> SVC
    SVC --> REPO
    REPO --> H2
    
    style GHA fill:#2ea043
    style GHP fill:#1f6feb
    style FT fill:#e1f5fe
    style ST fill:#f3e5f5
    style FV fill:#fff3cd
    style API fill:#fff3e0
    style SVC fill:#e8f5e9
    style REPO fill:#fce4ec
    style H2 fill:#f5f5f5
```

## 🔄 Test-Framework Integration

### ⚠️ Wichtige Architektur-Klarstellung

**FitNesse und Spock sind vollständig voneinander getrennte Test-Frameworks:**

- ❌ **KEINE Verbindung**: FitNesse Fixtures rufen niemals Spock Tests auf
- ✅ **Unabhängige Ausführung**: Beide Frameworks laufen separat und parallel
- 🔄 **Gemeinsame Services**: Beide testen dieselben Spring Services, aber unterschiedlich
- 📊 **Separate Reports**: Jedes Framework generiert eigene Test-Ergebnisse

```
📊 Test-Pyramid Architektur:
┌─────────────────────────────────┐
│        🎭 FitNesse              │ ← Business Acceptance Tests
│     (Stakeholder Tests)         │   
├─────────────────────────────────┤
│        🔬 Spock Integration     │ ← API & Service Integration  
├─────────────────────────────────┤
│        🔬 Spock Unit Tests      │ ← Logic & Component Tests
└─────────────────────────────────┘
           ⬇️ Both test ⬇️
        📱 Spring Boot Services
```

Diese Trennung ist bewusstes Design und folgt der Test-Pyramide:
- **Spock**: Unit/Integration Tests (Basis der Pyramide) - viele schnelle Tests
- **FitNesse**: Acceptance Tests (Spitze der Pyramide) - wenige umfassende Tests

## 🚀 CI/CD Pipeline Architektur

### GitHub Actions Workflow

```mermaid
graph LR
    subgraph "🔄 CI/CD Pipeline"
        A[📝 Code Push] --> B[⚙️ GitHub Actions]
        B --> C[🏗️ Build & Test]
        C --> D[📊 Generate Reports]
        D --> E[🎭 FitNesse Viewer]
        E --> F[📄 GitHub Pages]
    end
    
    subgraph "📊 Test Execution"
        G[🔬 Spock Tests<br/>39/39 ✅]
        H[🎭 FitNesse Tests<br/>XML Results]
        I[📈 JaCoCo Coverage<br/>89% ✅]
    end
    
    C --> G
    C --> H  
    C --> I
    G --> D
    H --> D
    I --> D
    
    style A fill:#f9f9f9
    style B fill:#2ea043
    style F fill:#1f6feb
    style G fill:#f3e5f5
    style H fill:#e1f5fe
    style I fill:#fff3e0
```

### 🔧 Pipeline Komponenten

| Komponente | Funktion | Output | Live URL |
|------------|----------|--------|----------|
| **GitHub Actions** | Automatische Builds | CI/CD Status | [Actions Tab](https://github.com/mboiman/fitnesse-spock-tutorial/actions) |
| **Spock Tests** | Unit/Integration Tests | HTML Reports | [📊 Spock Reports](https://mboiman.github.io/fitnesse-spock-tutorial/build/reports/tests/test/index.html) |
| **FitNesse Tests** | Acceptance Tests | XML → HTML Viewer | [🎭 FitNesse Results](https://mboiman.github.io/fitnesse-spock-tutorial/fitnesse-results.html) |
| **JaCoCo Coverage** | Code Coverage Analysis | Coverage Reports | [📈 Coverage Report](https://mboiman.github.io/fitnesse-spock-tutorial/build/reports/jacoco/test/html/index.html) |
| **GitHub Pages** | Static Site Hosting | Live Dashboard | [🌐 Live Demo](https://mboiman.github.io/fitnesse-spock-tutorial/) |

### 🎭 FitNesse Viewer Innovation

**Problem**: FitNesse generiert XML-Dateien, die im Browser nicht schön aussehen.

**Lösung**: Custom JavaScript-basierter XML-Viewer:

```
FitNesse XML Results → JavaScript Parser → Beautiful HTML Display
├── Test Statistics (Right/Wrong/Ignored/Exceptions)
├── Color-coded Status (✅ Pass / ❌ Fail)
├── Timestamp Information
└── Formatted Test Tables
```

**Features**:
- 📊 **Visual Test Statistics** mit Badges
- ✅ **Pass/Fail Indicators** für schnelle Übersicht  
- 📅 **Timestamp Parsing** aus Dateinamen
- 🎨 **Responsive Design** für alle Geräte
- 📱 **Mobile-friendly** Interface

## 🔄 Workflow Automation

### Deployment Pipeline

```mermaid
sequenceDiagram
    participant Dev as 👨‍💻 Developer
    participant GH as 📁 GitHub
    participant GA as ⚙️ Actions
    participant GP as 📄 Pages
    participant User as 👤 User
    
    Dev->>GH: 🔄 git push
    GH->>GA: 🚀 Trigger Workflow
    GA->>GA: 🏗️ Build Project
    GA->>GA: 🧪 Run Spock Tests (39/39)
    GA->>GA: 🎭 Run FitNesse Tests  
    GA->>GA: 📊 Generate Reports
    GA->>GA: 🎨 Create FitNesse Viewer
    GA->>GP: 📤 Deploy to Pages
    GP-->>User: 🌐 Live Test Reports
    
    Note over GA: 3-5 Minutes Fully Automated
    Note over GP: Available at: mboiman.github.io/...
```

### Datenfluss zwischen Frameworks

```mermaid
sequenceDiagram
    participant FT as 🎭 FitNesse
    participant FX as 🔧 Fixture  
    participant SVC as ⚙️ Service
    participant ST as 🔬 Spock
    participant DB as 💾 Database
    
    FT->>FX: Wiki-Test ausführen
    FX->>SVC: Service aufrufen
    SVC->>DB: Daten abrufen
    DB-->>SVC: Ergebnis
    SVC-->>FX: Business-Logik
    FX-->>FT: ✅/❌ Test-Ergebnis
    
    Note over ST: ⚡ Parallel & Unabhängig
    ST->>SVC: Gleichen Service testen
    SVC->>DB: Mock/Real Daten
    DB-->>ST: Test-Daten
    
    Note over FT,ST: Beide testen DIESELBEN Services!
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