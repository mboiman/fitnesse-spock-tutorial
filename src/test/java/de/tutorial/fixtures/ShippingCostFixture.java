package de.tutorial.fixtures;

import de.tutorial.bookstore.service.PriceCalculationService;
import fit.ColumnFixture;
import java.math.BigDecimal;

/**
 * FitNesse Fixture für Versandkosten-Berechnungen
 */
public class ShippingCostFixture extends ColumnFixture {
    
    private PriceCalculationService priceService = new PriceCalculationService();
    public double bestellwert;
    
    // Setter wird nicht benötigt, da das Feld public ist
    
    public double versandkosten() {
        BigDecimal wert = new BigDecimal(bestellwert);
        return priceService.calculateShippingCost(wert).doubleValue();
    }
    
    public String versandkostenHinweis() {
        BigDecimal wert = new BigDecimal(bestellwert);
        BigDecimal kosten = priceService.calculateShippingCost(wert);
        
        if (kosten.compareTo(BigDecimal.ZERO) == 0) {
            return "Kostenloser Versand!";
        } else if (kosten.compareTo(new BigDecimal("2.95")) == 0) {
            return "Reduzierte Versandkosten";
        } else {
            return "Standard-Versand";
        }
    }
    
    public double fehlbetragFuerKostenlosenVersand() {
        BigDecimal wert = new BigDecimal(bestellwert);
        if (wert.compareTo(new BigDecimal("50.00")) >= 0) {
            return 0.0;
        }
        return new BigDecimal("50.00").subtract(wert).doubleValue();
    }
}