package de.tutorial.integration

import de.tutorial.fixtures.PriceCalculationFixture
import de.tutorial.fixtures.ShippingCostFixture
import spock.lang.Specification
import spock.lang.Title
import spock.lang.Unroll

@Title("FitNesse Fixtures mit Spock testen")
class FitNesseIntegrationSpec extends Specification {
    
    def "FitNesse PriceCalculationFixture sollte korrekt rechnen"() {
        given: "Eine FitNesse Fixture"
        def fixture = new PriceCalculationFixture()
        
        when: "FitNesse-Style Eingaben gesetzt werden"
        fixture.buchpreis = 39.95
        fixture.anzahl = 3
        
        then: "Berechnungen stimmen mit Erwartungen überein"
        Math.abs(fixture.zwischensumme() - 119.85) < 0.01
        Math.abs(fixture.rabattbetrag() - 5.99) < 0.01
        Math.abs(fixture.endpreis() - 113.86) < 0.01
        Math.abs(fixture.mehrwertsteuer() - 7.97) < 0.01
        Math.abs(fixture.gesamtpreisMitMwst() - 121.83) < 0.01
        fixture.rabattsatz() == "5%"
    }
    
    @Unroll
    def "FitNesse Fixture berechnet #expectedDiscount Rabatt für #quantity Bücher"() {
        given: "FitNesse Fixture wie in Wiki-Tests"
        def fixture = new PriceCalculationFixture()
        fixture.buchpreis = 39.95
        fixture.anzahl = quantity
        
        expect: "Rabatt-Berechnung stimmt"
        fixture.rabattsatz() == expectedDiscount
        fixture.rabattbetrag() == expectedAmount
        
        where: "Testdaten aus FitNesse-Tabelle"
        quantity | expectedDiscount | expectedAmount
        1        | "0%"            | 0.00
        3        | "5%"            | 5.99
        5        | "10%"           | 19.98
        10       | "15%"           | 59.93
    }
    
    def "FitNesse ShippingCostFixture funktioniert korrekt"() {
        given: "Versandkosten Fixture"
        def fixture = new ShippingCostFixture()
        
        when: "Verschiedene Bestellwerte"
        fixture.bestellwert = bestellwert
        
        then: "Versandkosten stimmen"
        fixture.versandkosten() == expectedCost
        fixture.versandkostenHinweis() == expectedHint
        
        where:
        bestellwert | expectedCost | expectedHint
        10.00      | 4.95         | "Standard-Versand"
        25.00      | 2.95         | "Reduzierte Versandkosten"
        50.00      | 0.00         | "Kostenloser Versand!"
    }
}