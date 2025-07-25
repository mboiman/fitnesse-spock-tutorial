package de.tutorial.fixtures;

import de.tutorial.bookstore.service.PriceCalculationService;
import java.math.BigDecimal;

/**
 * Einfache FitNesse Fixture ohne fit.jar Abhängigkeit
 * Funktioniert mit modernem FitNesse
 */
public class SimplePriceCalculationFixture {
    
    private PriceCalculationService priceService = new PriceCalculationService();
    
    // Input-Felder - public für FitNesse
    public double buchpreis;
    public int anzahl;
    
    // Berechnungsmethoden für FitNesse
    public String zwischensumme() {
        BigDecimal preis = new BigDecimal(String.valueOf(buchpreis));
        double result = preis.multiply(BigDecimal.valueOf(anzahl)).doubleValue();
        return String.format(java.util.Locale.US, "%.2f", result);
    }
    
    public String rabattbetrag() {
        BigDecimal preis = new BigDecimal(String.valueOf(buchpreis));
        double result = priceService.calculateQuantityDiscount(preis, anzahl).doubleValue();
        return String.format(java.util.Locale.US, "%.2f", result);
    }
    
    public String endpreis() {
        BigDecimal preis = new BigDecimal(String.valueOf(buchpreis));
        double result = priceService.calculateFinalPrice(preis, anzahl).doubleValue();
        return String.format(java.util.Locale.US, "%.2f", result);
    }
    
    public String mehrwertsteuer() {
        BigDecimal preis = new BigDecimal(String.valueOf(buchpreis));
        BigDecimal finalPrice = priceService.calculateFinalPrice(preis, anzahl);
        double result = priceService.calculateVAT(finalPrice).doubleValue();
        return String.format(java.util.Locale.US, "%.2f", result);
    }
    
    public String gesamtpreisMitMwst() {
        BigDecimal preis = new BigDecimal(String.valueOf(buchpreis));
        BigDecimal finalPrice = priceService.calculateFinalPrice(preis, anzahl);
        double result = priceService.calculateTotalWithVAT(finalPrice).doubleValue();
        return String.format(java.util.Locale.US, "%.2f", result);
    }
    
    // Setter für Slim
    public void setBuchpreis(double buchpreis) {
        this.buchpreis = buchpreis;
    }
    
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }
    
    public String rabattsatz() {
        if (anzahl >= 10) return "15%";
        if (anzahl >= 5) return "10%";
        if (anzahl >= 3) return "5%";
        return "0%";
    }
}