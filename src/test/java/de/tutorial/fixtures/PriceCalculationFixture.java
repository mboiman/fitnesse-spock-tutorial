package de.tutorial.fixtures;

import de.tutorial.bookstore.service.PriceCalculationService;
import fit.ColumnFixture;
import java.math.BigDecimal;

/**
 * FitNesse Fixture für Preisberechnungen
 */
public class PriceCalculationFixture extends ColumnFixture {
    
    private PriceCalculationService priceService = new PriceCalculationService();
    
    // Input-Felder - müssen public sein für FitNesse
    public double buchpreis;
    public int anzahl;
    
    // Setter werden nicht mehr benötigt, da die Felder public sind
    
    // Berechnungsmethoden für FitNesse
    public double zwischensumme() {
        BigDecimal preis = new BigDecimal(buchpreis);
        return preis.multiply(BigDecimal.valueOf(anzahl)).doubleValue();
    }
    
    public double rabattbetrag() {
        BigDecimal preis = new BigDecimal(buchpreis);
        return priceService.calculateQuantityDiscount(preis, anzahl).doubleValue();
    }
    
    public double endpreis() {
        BigDecimal preis = new BigDecimal(buchpreis);
        return priceService.calculateFinalPrice(preis, anzahl).doubleValue();
    }
    
    public double mehrwertsteuer() {
        BigDecimal preis = new BigDecimal(buchpreis);
        BigDecimal finalPrice = priceService.calculateFinalPrice(preis, anzahl);
        return priceService.calculateVAT(finalPrice).doubleValue();
    }
    
    public double gesamtpreisMitMwst() {
        BigDecimal preis = new BigDecimal(buchpreis);
        BigDecimal finalPrice = priceService.calculateFinalPrice(preis, anzahl);
        return priceService.calculateTotalWithVAT(finalPrice).doubleValue();
    }
    
    public String rabattsatz() {
        if (anzahl >= 10) return "15%";
        if (anzahl >= 5) return "10%";
        if (anzahl >= 3) return "5%";
        return "0%";
    }
}