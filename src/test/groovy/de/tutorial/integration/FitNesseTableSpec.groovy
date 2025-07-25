package de.tutorial.integration

import de.tutorial.fixtures.PriceCalculationFixture
import spock.lang.Specification
import spock.lang.Title
import spock.lang.Unroll

@Title("FitNesse-Tabellen als Spock Data-Driven Tests")
class FitNesseTableSpec extends Specification {
    
    @Unroll
    def "FitNesse Preisberechnung: #anzahl Bücher à #buchpreis€"() {
        given: "FitNesse Fixture Setup"
        def fixture = new PriceCalculationFixture()
        fixture.buchpreis = buchpreis
        fixture.anzahl = anzahl
        
        expect: "Alle Berechnungen stimmen mit FitNesse-Tabelle überein"
        fixture.rabattsatz() == rabattsatz
        Math.abs(fixture.zwischensumme() - zwischensumme) < 0.01
        Math.abs(fixture.rabattbetrag() - rabattbetrag) < 0.01
        Math.abs(fixture.endpreis() - endpreis) < 0.01
        Math.abs(fixture.mehrwertsteuer() - mehrwertsteuer) < 0.01
        Math.abs(fixture.gesamtpreisMitMwst() - gesamtpreisMitMwst) < 0.01
        
        where: "Daten aus FitNesse-Tabelle"
        buchpreis | anzahl | rabattsatz | zwischensumme | rabattbetrag | endpreis | mehrwertsteuer | gesamtpreisMitMwst
        39.95     | 1      | "0%"       | 39.95         | 0.00         | 39.95    | 2.80          | 42.75
        39.95     | 2      | "0%"       | 79.90         | 0.00         | 79.90    | 5.59          | 85.49
        39.95     | 3      | "5%"       | 119.85        | 5.99         | 113.86   | 7.97          | 121.83
        39.95     | 4      | "5%"       | 159.80        | 7.99         | 151.81   | 10.63         | 162.44
        39.95     | 5      | "10%"      | 199.75        | 19.98        | 179.77   | 12.58         | 192.35
        39.95     | 9      | "10%"      | 359.55        | 35.96        | 323.59   | 22.65         | 346.24
        39.95     | 10     | "15%"      | 399.50        | 59.93        | 339.57   | 23.77         | 363.34
        39.95     | 20     | "15%"      | 799.00        | 119.85       | 679.15   | 47.54         | 726.69
    }
    
    def "Simuliere FitNesse Wiki-Test Ausführung"() {
        given: "Test-Tabelle wie in FitNesse"
        def testTable = [
            [buchpreis: 39.95, anzahl: 1,  expected_rabatt: "0%"],
            [buchpreis: 39.95, anzahl: 3,  expected_rabatt: "5%"],
            [buchpreis: 39.95, anzahl: 10, expected_rabatt: "15%"]
        ]
        
        when: "Alle Zeilen durchlaufen"
        def results = testTable.collect { row ->
            def fixture = new PriceCalculationFixture()
            fixture.buchpreis = row.buchpreis
            fixture.anzahl = row.anzahl
            
            [
                input: row,
                actual_rabatt: fixture.rabattsatz(),
                passed: fixture.rabattsatz() == row.expected_rabatt
            ]
        }
        
        then: "Alle Tests bestehen"
        results.every { it.passed }
        results.size() == 3
    }
}