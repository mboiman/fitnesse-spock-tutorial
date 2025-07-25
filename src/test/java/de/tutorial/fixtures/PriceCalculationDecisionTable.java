package de.tutorial.fixtures;

import de.tutorial.bookstore.service.PriceCalculationService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * FitNesse Decision Table für Preisberechnungen
 * Verwendet das Slim Test System
 */
public class PriceCalculationDecisionTable {
    
    private PriceCalculationService priceService = new PriceCalculationService();
    private BigDecimal buchpreis;
    private int anzahl;
    
    // Setter für Input-Spalten
    public void setBuchpreis(double preis) {
        this.buchpreis = BigDecimal.valueOf(preis);
    }
    
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }
    
    // Getter für Output-Spalten
    public String rabattsatz() {
        if (anzahl >= 10) return "15%";
        if (anzahl >= 5) return "10%";
        if (anzahl >= 3) return "5%";
        return "0%";
    }
    
    public String zwischensumme() {
        double result = buchpreis.multiply(BigDecimal.valueOf(anzahl))
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
        return String.format(java.util.Locale.US, "%.2f", result);
    }
    
    public String rabattbetrag() {
        double result = priceService.calculateQuantityDiscount(buchpreis, anzahl)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
        return String.format(java.util.Locale.US, "%.2f", result);
    }
    
    public String endpreis() {
        double result = priceService.calculateFinalPrice(buchpreis, anzahl)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
        return String.format(java.util.Locale.US, "%.2f", result);
    }
    
    public String mehrwertsteuer() {
        BigDecimal finalPrice = priceService.calculateFinalPrice(buchpreis, anzahl);
        double result = priceService.calculateVAT(finalPrice)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
        return String.format(java.util.Locale.US, "%.2f", result);
    }
    
    public String gesamtpreisMitMwst() {
        BigDecimal finalPrice = priceService.calculateFinalPrice(buchpreis, anzahl);
        double result = priceService.calculateTotalWithVAT(finalPrice)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
        return String.format(java.util.Locale.US, "%.2f", result);
    }
    
    // Query-Methode für Decision Table
    public List<List<String>> query() {
        List<List<String>> table = new ArrayList<>();
        return table;
    }
}