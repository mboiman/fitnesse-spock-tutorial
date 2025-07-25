# Spock Test Examples 🧪

Eine Sammlung von Spock Test-Beispielen für verschiedene Szenarien.

## 📑 Inhalt

1. [Unit Tests](#unit-tests)
2. [Integration Tests](#integration-tests)
3. [Mocking Examples](#mocking-examples)
4. [Data-Driven Tests](#data-driven-tests)
5. [Spring Integration](#spring-integration)
6. [REST API Tests](#rest-api-tests)
7. [Async Tests](#async-tests)
8. [Custom Matchers](#custom-matchers)

---

## Unit Tests

### Einfacher Service Test

```groovy
package de.tutorial.bookstore.service

import spock.lang.Specification
import spock.lang.Subject

class BookServiceSpec extends Specification {
    
    @Subject
    BookService bookService = new BookService()
    
    def "sollte Buch nach ID finden"() {
        given: "eine existierende Buch-ID"
        def bookId = 1L
        def expectedBook = new Book(id: bookId, title: "Clean Code")
        bookService.addBook(expectedBook)
        
        when: "das Buch gesucht wird"
        def result = bookService.findById(bookId)
        
        then: "wird das korrekte Buch zurückgegeben"
        result.isPresent()
        result.get().title == "Clean Code"
    }
    
    def "sollte leeres Optional zurückgeben für nicht existierende ID"() {
        expect: "Optional.empty() für unbekannte ID"
        bookService.findById(999L) == Optional.empty()
    }
}
```

### Exception Testing

```groovy
class ValidationServiceSpec extends Specification {
    
    def validator = new BookValidator()
    
    def "sollte Exception werfen bei ungültiger ISBN"() {
        when: "ungültige ISBN validiert wird"
        validator.validateIsbn("123-invalid")
        
        then: "wird spezifische Exception mit Message geworfen"
        def e = thrown(InvalidIsbnException)
        e.message == "Invalid ISBN format: 123-invalid"
    }
    
    def "sollte verschiedene Exceptions für verschiedene Fehler werfen"() {
        when: "die Validierung fehlschlägt"
        validator.validate(book)
        
        then: "wird passende Exception geworfen"
        thrown(expectedException)
        
        where:
        book                                    || expectedException
        new Book(isbn: null)                   || IllegalArgumentException
        new Book(isbn: "123")                  || InvalidIsbnException
        new Book(isbn: "978-3-16-148410-0", 
                 price: -10)                    || InvalidPriceException
    }
}
```

## Integration Tests

### Spring Boot Integration Test

```groovy
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class BookControllerIntegrationSpec extends Specification {
    
    @Autowired
    MockMvc mockMvc
    
    @Autowired
    BookRepository bookRepository
    
    def setup() {
        bookRepository.deleteAll()
    }
    
    def "GET /api/books sollte alle Bücher zurückgeben"() {
        given: "Bücher in der Datenbank"
        bookRepository.saveAll([
            new Book(isbn: "123", title: "Book 1"),
            new Book(isbn: "456", title: "Book 2")
        ])
        
        when: "GET Request ausgeführt wird"
        def response = mockMvc.perform(get("/api/books")
            .contentType(MediaType.APPLICATION_JSON))
        
        then: "Status ist OK und Bücher werden zurückgegeben"
        response.andExpect(status().isOk())
            .andExpect(jsonPath('$[0].title').value("Book 1"))
            .andExpect(jsonPath('$[1].title').value("Book 2"))
    }
}
```

### Database Integration Test

```groovy
@DataJpaTest
class BookRepositoryIntegrationSpec extends Specification {
    
    @Autowired
    TestEntityManager entityManager
    
    @Autowired
    BookRepository bookRepository
    
    def "sollte Bücher nach Autor finden"() {
        given: "Bücher verschiedener Autoren"
        entityManager.persistAndFlush(
            new Book(title: "Clean Code", author: "Robert Martin"))
        entityManager.persistAndFlush(
            new Book(title: "Clean Architecture", author: "Robert Martin"))
        entityManager.persistAndFlush(
            new Book(title: "Refactoring", author: "Martin Fowler"))
        
        when: "nach Autor gesucht wird"
        def results = bookRepository.findByAuthor("Robert Martin")
        
        then: "werden nur Bücher dieses Autors gefunden"
        results.size() == 2
        results.every { it.author == "Robert Martin" }
    }
}
```

## Mocking Examples

### Service mit Dependencies

```groovy
class OrderServiceSpec extends Specification {
    
    // Mocks
    BookRepository bookRepository = Mock()
    PaymentService paymentService = Mock()
    EmailService emailService = Mock()
    
    // Subject under test
    @Subject
    OrderService orderService = new OrderService(
        bookRepository, paymentService, emailService)
    
    def "sollte komplette Bestellung verarbeiten"() {
        given: "ein verfügbares Buch und Kunde"
        def book = new Book(id: 1, price: 29.99, stock: 10)
        def customerId = "CUST-123"
        def quantity = 2
        
        and: "Mock-Verhalten definiert"
        bookRepository.findById(1) >> Optional.of(book)
        paymentService.processPayment(customerId, 59.98) >> 
            new PaymentResult(success: true, transactionId: "TXN-456")
        
        when: "Bestellung aufgegeben wird"
        def order = orderService.placeOrder(customerId, 1, quantity)
        
        then: "wird Lagerbestand reduziert"
        1 * bookRepository.save({ Book b -> 
            b.stock == 8
        })
        
        and: "wird Bestätigungs-Email gesendet"
        1 * emailService.sendOrderConfirmation(customerId, { Order o ->
            o.totalAmount == 59.98 &&
            o.transactionId == "TXN-456"
        })
        
        and: "wird erfolgreiche Bestellung zurückgegeben"
        order.status == OrderStatus.CONFIRMED
        order.items.size() == 1
    }
}
```

### Stubbing mit Closures

```groovy
class PriceCalculationServiceSpec extends Specification {
    
    DiscountService discountService = Mock()
    TaxService taxService = Mock()
    
    @Subject
    PriceCalculationService calculator = 
        new PriceCalculationService(discountService, taxService)
    
    def "sollte dynamische Rabatte berechnen"() {
        given: "dynamisches Stubbing basierend auf Input"
        discountService.getDiscountRate(_ as Customer) >> { Customer c ->
            switch(c.type) {
                case CustomerType.PREMIUM: return 0.20
                case CustomerType.REGULAR: return 0.05
                default: return 0.00
            }
        }
        
        taxService.getTaxRate(_ as String) >> { String country ->
            country == "DE" ? 0.19 : 0.15
        }
        
        expect: "korrekte Preisberechnung"
        calculator.calculateFinalPrice(customer, 100.00, country) == expectedPrice
        
        where:
        customer                              | country || expectedPrice
        new Customer(type: PREMIUM)           | "DE"    || 95.20  // 100 - 20% + 19% tax
        new Customer(type: REGULAR)           | "DE"    || 113.05 // 100 - 5% + 19% tax
        new Customer(type: PREMIUM)           | "AT"    || 92.00  // 100 - 20% + 15% tax
    }
}
```

## Data-Driven Tests

### Parameterisierte Tests mit @Unroll

```groovy
class ShippingCostCalculatorSpec extends Specification {
    
    def calculator = new ShippingCostCalculator()
    
    @Unroll
    def "Versandkosten für #country mit Wert #orderValue EUR = #expectedCost EUR"() {
        expect:
        calculator.calculate(orderValue, country, express) == expectedCost
        
        where:
        orderValue | country      | express || expectedCost
        25.00      | "DE"        | false   || 4.95
        50.00      | "DE"        | false   || 0.00
        25.00      | "DE"        | true    || 9.95
        50.00      | "DE"        | true    || 5.00
        25.00      | "AT"        | false   || 7.95
        50.00      | "AT"        | false   || 3.95
        25.00      | "CH"        | false   || 12.95
        50.00      | "CH"        | false   || 8.95
    }
}
```

### Komplexe Datenquellen

```groovy
class BookValidationSpec extends Specification {
    
    @Shared
    def validator = new BookValidator()
    
    @Unroll
    def "Validierung von: #description"() {
        expect:
        validator.isValid(book) == isValid
        
        where:
        book << loadTestBooks()
        isValid << expectedResults()
        description = book.toString()
    }
    
    def loadTestBooks() {
        [
            new Book(isbn: "978-3-16-148410-0", title: "Valid Book", price: 29.99),
            new Book(isbn: "invalid", title: "Invalid ISBN", price: 29.99),
            new Book(isbn: "978-3-16-148410-0", title: "", price: 29.99),
            new Book(isbn: "978-3-16-148410-0", title: "Negative Price", price: -10)
        ]
    }
    
    def expectedResults() {
        [true, false, false, false]
    }
}
```

## Spring Integration

### Testing mit @SpringBootTest

```groovy
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookstoreApplicationSpec extends Specification {
    
    @Autowired
    TestRestTemplate restTemplate
    
    @Value('${local.server.port}')
    int port
    
    def "sollte Health-Endpoint bereitstellen"() {
        when: "Health-Endpoint aufgerufen wird"
        def response = restTemplate.getForEntity(
            "http://localhost:$port/actuator/health", 
            Map)
        
        then: "ist Service UP"
        response.statusCode == HttpStatus.OK
        response.body.status == "UP"
    }
}
```

### Testing mit TestConfiguration

```groovy
@SpringBootTest
@Import(TestConfig)
class ServiceIntegrationSpec extends Specification {
    
    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        EmailService mockEmailService() {
            Mock(EmailService)
        }
    }
    
    @Autowired
    OrderService orderService
    
    @Autowired
    EmailService emailService
    
    def "sollte mit Mock EmailService arbeiten"() {
        given: "eine Bestellung"
        def order = new Order(customerEmail: "test@example.com")
        
        when: "Bestätigung gesendet wird"
        orderService.sendConfirmation(order)
        
        then: "wird Mock aufgerufen"
        1 * emailService.send("test@example.com", _)
    }
}
```

## REST API Tests

### REST-assured Integration

```groovy
@SpringBootTest(webEnvironment = RANDOM_PORT)
class BookApiSpec extends Specification {
    
    @LocalServerPort
    int port
    
    def setup() {
        RestAssured.port = port
    }
    
    def "sollte neues Buch erstellen"() {
        given: "ein neues Buch"
        def newBook = [
            isbn: "978-3-16-148410-0",
            title: "Test Book",
            author: "Test Author",
            price: 29.99
        ]
        
        when: "POST Request gesendet wird"
        def response = given()
            .contentType(ContentType.JSON)
            .body(newBook)
        .when()
            .post("/api/books")
        
        then: "wird Buch erstellt"
        response.then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("title", equalTo("Test Book"))
            .header("Location", containsString("/api/books/"))
    }
    
    def "sollte Bücher filtern können"() {
        when: "nach Büchern mit Filter gesucht wird"
        def response = given()
            .queryParam("author", "Martin")
            .queryParam("minPrice", 20)
            .queryParam("maxPrice", 50)
        .when()
            .get("/api/books/search")
        
        then: "werden gefilterte Ergebnisse zurückgegeben"
        response.then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("author", everyItem(containsString("Martin")))
            .body("price", everyItem(allOf(
                greaterThanOrEqualTo(20),
                lessThanOrEqualTo(50)
            )))
    }
}
```

## Async Tests

### Testing Async Operations

```groovy
class AsyncServiceSpec extends Specification {
    
    @Subject
    AsyncBookService asyncService = new AsyncBookService()
    
    def "sollte Bücher asynchron laden"() {
        when: "asynchroner Ladevorgang gestartet wird"
        def future = asyncService.loadBooksAsync()
        
        then: "wird Future zurückgegeben"
        future != null
        
        when: "auf Ergebnis gewartet wird"
        def books = future.get(5, TimeUnit.SECONDS)
        
        then: "werden Bücher geladen"
        books.size() > 0
    }
    
    def "sollte mit PollingConditions warten"() {
        given: "ein Service der verzögert antwortet"
        def service = new DelayedService()
        def conditions = new PollingConditions(timeout: 5)
        
        when: "Operation gestartet wird"
        service.startSlowOperation()
        
        then: "wird eventually erfolgreich"
        conditions.eventually {
            assert service.isComplete()
            assert service.getResult() == "SUCCESS"
        }
    }
}
```

## Custom Matchers

### Hamcrest-Style Matchers

```groovy
class CustomMatchersSpec extends Specification {
    
    def "sollte custom Matcher verwenden"() {
        given: "ein Buch"
        def book = new Book(
            isbn: "978-3-16-148410-0",
            title: "Clean Code",
            price: 39.95
        )
        
        expect: "custom Validierungen"
        book.isbn ==~ /978-\d-\d{2}-\d{6}-\d/
        book.price > 0 && book.price < 100
        book.title.toLowerCase().contains("clean")
    }
    
    def "sollte with() für multiple Assertions nutzen"() {
        when: "Bestellung verarbeitet wird"
        def order = orderService.process(orderRequest)
        
        then: "alle Eigenschaften sind korrekt"
        with(order) {
            status == OrderStatus.CONFIRMED
            items.size() == 3
            totalAmount > 0
            customerEmail == "test@example.com"
            createdAt <= LocalDateTime.now()
        }
    }
}
```

### Custom Conditions

```groovy
class BookConditions {
    static boolean isValidBook(Book book) {
        book != null &&
        book.isbn?.matches(/978-\d-\d{2}-\d{6}-\d/) &&
        book.title?.length() > 0 &&
        book.price > 0
    }
    
    static boolean hasMinimumStock(Book book, int minimum) {
        book.stock >= minimum
    }
}

class BookSpec extends Specification {
    
    def "sollte gültiges Buch sein"() {
        given:
        def book = new Book(/* ... */)
        
        expect:
        BookConditions.isValidBook(book)
        BookConditions.hasMinimumStock(book, 5)
    }
}
```

## 🎯 Best Practices für Spock Tests

1. **Beschreibende Namen**: Nutze aussagekräftige Strings für Methodennamen
2. **Arrange-Act-Assert**: Folge dem Given-When-Then Pattern
3. **Eine Assertion pro Then-Block**: Nutze `and:` für mehrere Assertions
4. **@Unroll für Klarheit**: Bei Data-Driven Tests
5. **Sinnvolle Defaults**: In Test-Daten Buildern
6. **Cleanup**: Nutze cleanup() für Ressourcen-Freigabe

## 📚 Weiterführende Themen

- [Spock Extensions](http://spockframework.org/spock/docs/2.3/extensions.html)
- [Spock Spring Module](http://spockframework.org/spock/docs/2.3/modules.html#_spring_module)
- [Spock Reports](https://github.com/renatoathaydes/spock-reports)
- [Geb Integration](https://gebish.org/) für Browser-Tests