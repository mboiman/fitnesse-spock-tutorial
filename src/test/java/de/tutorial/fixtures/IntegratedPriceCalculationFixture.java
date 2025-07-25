package de.tutorial.fixtures;

import de.tutorial.bookstore.service.PriceCalculationService;
import java.math.BigDecimal;

/**
 * FitNesse Fixture, die denselben Spring Context wie Spock nutzt
 * Dadurch werden beide Test-Frameworks mit denselben Services getestet
 */
public class IntegratedPriceCalculationFixture extends BaseSpringFixture {
    
    private final PriceCalculationService priceService;
    
    // Input-Felder
    private BigDecimal buchpreis;
    private int anzahl;
    
    public IntegratedPriceCalculationFixture() {
        // Nutze denselben Service wie Spock Tests
        this.priceService = getBean(PriceCalculationService.class);
    }
    
    // Setter für FitNesse
    public void setBuchpreis(double preis) {
        this.buchpreis = new BigDecimal(preis);
    }
    
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }
    
    // Berechnungsmethoden - identisch mit Spock Tests
    public double zwischensumme() {
        return buchpreis.multiply(BigDecimal.valueOf(anzahl)).doubleValue();
    }
    
    public double rabattbetrag() {
        return priceService.calculateQuantityDiscount(buchpreis, anzahl).doubleValue();
    }
    
    public double endpreis() {
        return priceService.calculateFinalPrice(buchpreis, anzahl).doubleValue();
    }
    
    public double mehrwertsteuer() {
        BigDecimal finalPrice = priceService.calculateFinalPrice(buchpreis, anzahl);
        return priceService.calculateVAT(finalPrice).doubleValue();
    }
    
    public double gesamtpreisMitMwst() {
        BigDecimal finalPrice = priceService.calculateFinalPrice(buchpreis, anzahl);
        return priceService.calculateTotalWithVAT(finalPrice).doubleValue();
    }
    
    public String rabattsatz() {
        // Könnte auch aus dem Service kommen
        if (anzahl >= 10) return "15%";
        if (anzahl >= 5) return "10%";
        if (anzahl >= 3) return "5%";
        return "0%";
    }
}