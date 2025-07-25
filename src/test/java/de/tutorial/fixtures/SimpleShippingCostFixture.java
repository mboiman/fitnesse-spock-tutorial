package de.tutorial.fixtures;

import de.tutorial.bookstore.service.PriceCalculationService;
import java.math.BigDecimal;

/**
 * Einfache FitNesse Fixture ohne fit.jar Abhängigkeit
 */
public class SimpleShippingCostFixture {
    
    private PriceCalculationService priceService = new PriceCalculationService();
    private double bestellwert;
    
    // Setter für Decision Table
    public void setOrderValue(double orderValue) {
        this.bestellwert = orderValue;
    }
    
    public String shippingCost() {
        BigDecimal wert = new BigDecimal(String.valueOf(bestellwert));
        double kosten = priceService.calculateShippingCost(wert).doubleValue();
        return String.format(java.util.Locale.US, "%.2f", kosten);
    }
    
    public String versandkostenHinweis() {
        BigDecimal wert = new BigDecimal(String.valueOf(bestellwert));
        BigDecimal kosten = priceService.calculateShippingCost(wert);
        
        if (kosten.compareTo(BigDecimal.ZERO) == 0) {
            return "Kostenloser Versand!";
        } else if (kosten.compareTo(new BigDecimal("2.95")) == 0) {
            return "Reduzierte Versandkosten";
        } else {
            return "Standard-Versand";
        }
    }
    
    public String fehlbetragFuerKostenlosenVersand() {
        BigDecimal wert = new BigDecimal(String.valueOf(bestellwert));
        if (wert.compareTo(new BigDecimal("50.00")) >= 0) {
            return "0.00";
        }
        double fehlbetrag = new BigDecimal("50.00").subtract(wert).doubleValue();
        return String.format(java.util.Locale.US, "%.2f", fehlbetrag);
    }
}