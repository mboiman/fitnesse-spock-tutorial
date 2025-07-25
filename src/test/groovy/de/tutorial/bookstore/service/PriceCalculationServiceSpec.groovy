package de.tutorial.bookstore.service

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

@Title("Preisberechnungs-Service Tests")
class PriceCalculationServiceSpec extends Specification {
    
    @Subject
    PriceCalculationService service = new PriceCalculationService()
    
    @Unroll
    def "sollte Mengenrabatt von #expectedDiscount% für #quantity Bücher berechnen"() {
        given: "Ein Buchpreis von 39.95€"
        def price = new BigDecimal("39.95")
        
        when: "Der Rabatt berechnet wird"
        def discount = service.calculateQuantityDiscount(price, quantity)
        
        then: "Ist der Rabattbetrag korrekt"
        discount == expectedAmount
        
        where:
        quantity | expectedDiscount | expectedAmount
        1        | 0               | 0.00
        2        | 0               | 0.00
        3        | 5               | 5.99
        5        | 10              | 19.98
        10       | 15              | 59.93
    }
    
    def "sollte Endpreis inklusive Rabatt berechnen"() {
        given: "Ein Buch für 39.95€"
        def price = new BigDecimal("39.95")
        
        when: "3 Bücher gekauft werden"
        def finalPrice = service.calculateFinalPrice(price, 3)
        
        then: "Ist der Endpreis nach 5% Rabatt korrekt"
        finalPrice == new BigDecimal("113.86")
    }
    
    @Unroll
    def "sollte #vat€ MwSt für Nettopreis #netPrice€ berechnen"() {
        when: "MwSt berechnet wird"
        def calculatedVat = service.calculateVAT(new BigDecimal(netPrice))
        
        then: "Ist die MwSt korrekt (7% für Bücher)"
        calculatedVat == new BigDecimal(vat)
        
        where:
        netPrice | vat
        "100.00" | "7.00"
        "39.95"  | "2.80"
        "19.99"  | "1.40"
    }
    
    @Unroll
    def "sollte Versandkosten von #shippingCost€ für Bestellwert #orderValue€ berechnen"() {
        expect:
        service.calculateShippingCost(new BigDecimal(orderValue)) == new BigDecimal(shippingCost)
        
        where:
        orderValue | shippingCost
        "10.00"    | "4.95"
        "25.00"    | "2.95"
        "50.00"    | "0.00"
        "100.00"   | "0.00"
    }
    
    def "sollte Gesamtpreis mit MwSt korrekt berechnen"() {
        given: "Ein Nettopreis"
        def netPrice = new BigDecimal("100.00")
        
        when: "Gesamtpreis mit MwSt berechnet wird"
        def totalWithVat = service.calculateTotalWithVAT(netPrice)
        
        then: "Ist der Gesamtpreis korrekt"
        totalWithVat == new BigDecimal("107.00")
    }
}