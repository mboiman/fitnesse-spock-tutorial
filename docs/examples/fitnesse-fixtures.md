# FitNesse Fixture-Galerie 🎨

Eine umfassende Sammlung von FitNesse Fixture-Beispielen für die Online-Buchhandlung.

## ⚠️ Wichtige Hinweise für FitNesse 2025

1. **Verwende Slim Test System**: `!define TEST_SYSTEM {slim}`
2. **Absolute Pfade**: Relative Pfade führen oft zu Problemen
3. **US-Locale für Dezimalzahlen**: `String.format(Locale.US, "%.2f", value)`
4. **Decision Tables**: Benötigen Setter für Input und parameterlose Getter für Output

## 📋 Übersicht der Fixture-Typen

1. [Column Fixture](#column-fixture) - Tabellenbasierte Tests
2. [Script Fixture](#script-fixture) - Story-driven Tests
3. [SetUp Fixture](#setup-fixture) - Testdaten-Vorbereitung
4. [Sequence Fixture](#sequence-fixture) - Ablauf-Tests
5. [Calculation Fixture](#calculation-fixture) - Berechnungen
6. [Constraint Fixture](#constraint-fixture) - Validierungen
7. [Compare Fixture](#compare-fixture) - Listenvergleiche
8. [Array Fixture](#array-fixture) - Array-Operationen
9. [Table Fixture](#table-fixture) - Komplexe Tabellen
10. [Query Fixture](#query-fixture) - Datenbank-Abfragen
11. [Custom Fixtures](#custom-fixtures) - Eigene Implementierungen

## Column Fixture

### Beispiel: Preisberechnung mit Rabatten

```
!|de.tutorial.fixtures.ColumnFixtureExample|
|buchpreis|anzahl|rabatt prozent|zwischensumme?|rabattbetrag?|endsumme?|mehrwertsteuer?|gesamtpreis mit mwst?|
|39.95|1|0|39.95|0.00|39.95|2.80|42.75|
|39.95|2|0|79.90|0.00|79.90|5.59|85.49|
|39.95|3|5|119.85|5.99|113.86|7.97|121.83|
|39.95|5|10|199.75|19.98|179.77|12.58|192.35|
|39.95|10|15|399.50|59.93|339.57|23.77|363.34|
```

### Beispiel: Versandkosten-Staffelung

```
!|VersandkostenFixture|
|bestellwert|land|express versand|versandkosten?|lieferzeit?|
|15.00|Deutschland|false|4.95|3-5 Tage|
|35.00|Deutschland|false|2.95|3-5 Tage|
|75.00|Deutschland|false|0.00|3-5 Tage|
|15.00|Deutschland|true|9.95|1 Tag|
|100.00|Österreich|false|7.95|5-7 Tage|
|100.00|Schweiz|false|12.95|7-10 Tage|
```

## Script Fixture

### Beispiel: Kompletter Bestellvorgang

```
!|script|de.tutorial.fixtures.ScriptFixture|
|# Kunde registrieren und anmelden|
|ich bin ein kunde mit dem namen|Maria Schmidt|
|ich registriere mich mit email|maria.schmidt@example.com|
|ich melde mich an mit passwort|sicher123|
|check|anmeldung erfolgreich|true|
|# Bücher suchen und auswählen|
|ich suche nach|Java Programmierung|
|check|anzahl suchergebnisse|5|
|ich waehle das buch nummer|1|
|check|ausgewaehltes buch ist|Java - Der Grundkurs|
|ich lege das buch in den warenkorb|
|# Weitere Bücher hinzufügen|
|ich suche nach|Clean Code|
|ich waehle das erste ergebnis|
|ich lege|2|exemplare in den warenkorb|
|# Warenkorb prüfen|
|check|anzahl artikel im warenkorb|3|
|check|gesamtpreis|127.85|
|# Bestellung abschließen|
|ich gehe zur kasse|
|ich verwende gespeicherte adresse|Hauptadresse|
|ich waehle zahlungsart|PayPal|
|ich bestatige die bestellung|
|check|bestellnummer|~=/B-2024-\d{6}/|
|check|voraussichtliche lieferung|~=/\d{1,2}\.\d{1,2}\.2024/|
```

### Beispiel: Warenkorb-Verwaltung

```
!|script|WarenkorbScript|
|ich habe einen leeren warenkorb|
|ich fuege hinzu|978-3-16-148410-0|mit menge|1|
|check|warenkorb total|39.95|
|ich aendere menge von|978-3-16-148410-0|auf|3|
|check|warenkorb total|119.85|
|ich entferne|978-3-16-148410-0|
|check|ist warenkorb leer|true|
```

## SetUp Fixture

### Beispiel: Testdaten für Integrationstests

```
!|de.tutorial.fixtures.SetUpFixture|
|# Datenbank vorbereiten|
|datenbank leeren|
|testdaten laden|
|# Bücher anlegen|
|buch anlegen|978-3-8362-7519-4|Java ist auch eine Insel|Christian Ullenboom|49.90|25|
|buch anlegen|978-3-8362-7759-4|Spring Boot 3|Michael Simons|44.90|15|
|buch anlegen|978-3-446-46408-1|Kotlin|Jörg Staudemeyer|39.99|20|
|# Kategorien zuweisen|
|kategorie zuweisen|978-3-8362-7519-4|Java|
|kategorie zuweisen|978-3-8362-7519-4|Programmierung|
|kategorie zuweisen|978-3-8362-7759-4|Java|
|kategorie zuweisen|978-3-8362-7759-4|Spring|
|kategorie zuweisen|978-3-446-46408-1|Kotlin|
|# Kunden anlegen|
|kunde anlegen|max.mustermann@test.de|Max|Mustermann|Premium|
|kunde anlegen|anna.schmidt@test.de|Anna|Schmidt|Standard|
|# Lagerbestände setzen|
|lagerbestand setzen|978-3-8362-7519-4|25|
|lagerbestand setzen|978-3-8362-7759-4|15|
|lagerbestand setzen|978-3-446-46408-1|20|
```

## Sequence Fixture

### Beispiel: Bestellablauf mit Validierung

```
!|de.tutorial.fixtures.SequenceFixture|
|kunde anmelden|test@example.com|password123|
|neue bestellung starten|
|artikel zur bestellung hinzufuegen|978-3-16-148410-0|2|
|artikel zur bestellung hinzufuegen|978-0-321-35668-0|1|
|check|gesamtbetrag der bestellung|125.40|
|lieferadresse angeben|Musterstraße 1|12345|Berlin|
|zahlungsmethode waehlen|Kreditkarte|
|check|bestellung abschliessen|~=/B-\d{10}/|
```

## Calculation Fixture

### Beispiel: Rabatt- und Preisberechnungen

```
!|de.tutorial.fixtures.CalculationFixture|
|# Versandkosten-Berechnung|
|bestellwert|versandkosten berechnen?|
|15.00|4.95|
|35.00|2.95|
|75.00|0.00|
|# Mengenrabatt|
|anzahl|einzelpreis|mengenrabatt berechnen?|
|2|29.99|0.00|
|3|29.99|4.50|
|5|29.99|15.00|
|10|29.99|44.99|
|# Kundenrabatt|
|kundentyp|bestellwert|kundenrabatt berechnen?|
|Standard|100.00|0.00|
|Silber|100.00|5.00|
|Gold|100.00|10.00|
|Premium|100.00|20.00|
```

## Constraint Fixture

### Beispiel: Datenvalidierung

```
!|de.tutorial.fixtures.ConstraintFixture|
|# ISBN Validierung|
|isbn|ist gueltige isbn?|
|978-3-16-148410-0|true|
|978-3-16-148410-5|false|
|123-4-56-789012-3|false|
|978-0-596-52068-7|true|
|# Preis Validierung|
|preis|ist gueltiger preis?|
|29.99|true|
|0.01|true|
|-5.00|false|
|1500.00|false|
|# Komplexe Validierung|
|isbn|titel|preis|kann buch angelegt werden?|
|978-3-16-148410-0|Clean Code|39.95|true|
|invalid-isbn|Clean Code|39.95|false|
|978-3-16-148410-0||39.95|false|
|978-3-16-148410-0|Clean Code|-10|false|
```

## Compare Fixture

### Beispiel: Bücher nach Kategorie

```
!|de.tutorial.fixtures.CompareFixture|
|buecher in kategorie|Java|
|ISBN|Titel|Autor|Preis|Lagerbestand|
|978-3-8362-7519-4|Java ist auch eine Insel|Christian Ullenboom|49.90|25|
|978-3-8362-7759-4|Spring Boot 3|Michael Simons|44.90|15|
|978-0-321-35668-0|Effective Java|Joshua Bloch|45.50|10|
```

### Beispiel: Top-Verkäufe

```
!|de.tutorial.fixtures.CompareFixture|
|top verkaeufe|
|Titel|Autor|Verkaufte Exemplare|
|Clean Code|Robert C. Martin|150|
|Effective Java|Joshua Bloch|120|
|Design Patterns|Gang of Four|95|
|Refactoring|Martin Fowler|80|
|The Pragmatic Programmer|Hunt & Thomas|75|
```

## Array Fixture

### Beispiel: Empfehlungen

```
!|de.tutorial.fixtures.ArrayFixture|
|empfehlungen fuer kunde|K-12345|
|Java - Der Grundkurs|
|Spring Boot in Action|
|Microservices mit Spring|
|Clean Architecture|
```

### Beispiel: Ähnliche Bücher

```
!|de.tutorial.fixtures.ArrayFixture|
|aehnliche buecher|978-3-16-148410-0|
|Refactoring von Martin Fowler|
|The Pragmatic Programmer von Hunt & Thomas|
|Code Complete von Steve McConnell|
|Design Patterns von Gang of Four|
```

## Table Fixture

### Beispiel: Rabattregeln verwalten

```
!|de.tutorial.fixtures.TableFixture|
|regeltyp|schwellwert|rabatt prozent|gueltig von|gueltig bis|regel angelegt?|status?|
|Mengenrabatt|100.00|5|01.01.2024|31.12.2024|true|✓ Erfolgreich|
|Mengenrabatt|200.00|10|01.01.2024|31.12.2024|true|✓ Erfolgreich|
|Saisonrabatt|50.00|15|01.12.2024|31.12.2024|true|✓ Erfolgreich|
|Kundenrabatt|0.00|20|01.01.2024|31.12.2024|true|✓ Erfolgreich|
```

## Query Fixture

### Beispiel: Bestellhistorie

```
!|Query:de.tutorial.fixtures.BestellHistorieQuery|
|kunde id|K-12345|
|bestell nummer|datum|status|betrag|artikel anzahl|
|B-2024000001|15.01.2024|Versendet|89.90|2|
|B-2024000045|28.02.2024|Geliefert|156.75|4|
|B-2024000089|15.03.2024|In Bearbeitung|45.50|1|
```

## Custom Fixtures

### Beispiel: REST API Test Fixture

```java
public class RestApiFixture extends Fixture {
    private String baseUrl = "http://localhost:8080/api";
    private RestTemplate restTemplate = new RestTemplate();
    
    public boolean buchAnlegen(String isbn, String titel, double preis) {
        Book book = new Book(isbn, titel, "", new BigDecimal(preis));
        ResponseEntity<Book> response = restTemplate.postForEntity(
            baseUrl + "/books", book, Book.class
        );
        return response.getStatusCode() == HttpStatus.CREATED;
    }
    
    public String buchSuchen(String query) {
        ResponseEntity<List> response = restTemplate.getForEntity(
            baseUrl + "/books/search?q=" + query, List.class
        );
        return response.getBody().size() + " Bücher gefunden";
    }
}
```

Verwendung in FitNesse:

```
!|RestApiFixture|
|buch anlegen|978-1-234567-89-0|Test API Buch|29.99|
|buch suchen|Test|
```

### Beispiel: Performance Test Fixture

```java
public class PerformanceFixture extends TimedFixture {
    
    public long buchSucheResponseZeit(String query) {
        long start = System.currentTimeMillis();
        bookService.searchBooks(query);
        return System.currentTimeMillis() - start;
    }
    
    public boolean responseZeitUnter(long maxMillis) {
        return getLastExecutionTime() < maxMillis;
    }
}
```

## Best Practices für Fixtures

1. **Klare Methodennamen**: Verwenden Sie deutsche, sprechende Namen
2. **Fehlerbehandlung**: Geben Sie aussagekräftige Fehlermeldungen zurück
3. **Wiederverwendbarkeit**: Erstellen Sie abstrakte Basis-Fixtures
4. **Testdaten-Isolation**: Räumen Sie nach Tests auf
5. **Performance**: Cachen Sie teure Operationen

## Fixture-Vererbung

```java
public abstract class BaseBookstoreFixture extends Fixture {
    protected BookService bookService;
    protected CustomerService customerService;
    
    public BaseBookstoreFixture() {
        // Gemeinsame Initialisierung
        bookService = ApplicationContext.getBean(BookService.class);
        customerService = ApplicationContext.getBean(CustomerService.class);
    }
    
    protected void cleanup() {
        // Gemeinsame Aufräumarbeiten
    }
}
```

## Tipps zur Fixture-Entwicklung

- Halten Sie Fixtures einfach und fokussiert
- Vermeiden Sie Geschäftslogik in Fixtures
- Nutzen Sie Dependency Injection
- Schreiben Sie Unit-Tests für komplexe Fixtures
- Dokumentieren Sie Fixture-Parameter