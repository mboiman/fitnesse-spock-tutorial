package de.tutorial.fixtures;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Einfache Preisberechnung für FitNesse ohne Spring Dependencies
 */
public class SimplePriceCalculationWithRabattFixture {
    
    private BigDecimal buchpreis;
    private int anzahl;
    
    // Setter für Decision Table
    public void setBuchpreis(double buchpreis) {
        this.buchpreis = new BigDecimal(String.valueOf(buchpreis));
    }
    
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }
    
    // Getter für Output-Spalten
    public String rabattsatz() {
        if (anzahl >= 10) return "15.00";
        if (anzahl >= 5) return "10.00";
        if (anzahl >= 3) return "5.00";
        return "0.00";
    }
    
    public String endpreis() {
        BigDecimal summe = buchpreis.multiply(BigDecimal.valueOf(anzahl));
        BigDecimal rabattProzent = new BigDecimal(rabattsatz());
        // Berechne Rabatt mit mehr Präzision und runde erst am Ende
        BigDecimal rabatt = summe.multiply(rabattProzent).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal endpreis = summe.subtract(rabatt).setScale(2, RoundingMode.HALF_UP);
        return String.format(java.util.Locale.US, "%.2f", endpreis.doubleValue());
    }
}