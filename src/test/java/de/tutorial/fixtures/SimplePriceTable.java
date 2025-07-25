package de.tutorial.fixtures;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Simple FitNesse fixture for price calculations
 * Works with Slim Test System
 */
public class SimplePriceTable {
    
    private double buchpreis;
    private int anzahl;
    
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
    
    public String zwischensumme() {
        return formatPrice(buchpreis * anzahl);
    }
    
    public String rabattbetrag() {
        double rabatt = 0.0;
        if (anzahl >= 10) rabatt = 0.15;
        else if (anzahl >= 5) rabatt = 0.10;
        else if (anzahl >= 3) rabatt = 0.05;
        
        return formatPrice(buchpreis * anzahl * rabatt);
    }
    
    public String endpreis() {
        double zwischen = buchpreis * anzahl;
        double rabatt = 0.0;
        if (anzahl >= 10) rabatt = 0.15;
        else if (anzahl >= 5) rabatt = 0.10;
        else if (anzahl >= 3) rabatt = 0.05;
        double rabattBetrag = zwischen * rabatt;
        return formatPrice(zwischen - rabattBetrag);
    }
    
    public String mehrwertsteuer() {
        double zwischen = buchpreis * anzahl;
        double rabatt = 0.0;
        if (anzahl >= 10) rabatt = 0.15;
        else if (anzahl >= 5) rabatt = 0.10;
        else if (anzahl >= 3) rabatt = 0.05;
        double rabattBetrag = zwischen * rabatt;
        double endpreis = zwischen - rabattBetrag;
        return formatPrice(endpreis * 0.07);
    }
    
    public String gesamtpreisMitMwst() {
        double zwischen = buchpreis * anzahl;
        double rabatt = 0.0;
        if (anzahl >= 10) rabatt = 0.15;
        else if (anzahl >= 5) rabatt = 0.10;
        else if (anzahl >= 3) rabatt = 0.05;
        double rabattBetrag = zwischen * rabatt;
        double endpreis = zwischen - rabattBetrag;
        double mwst = endpreis * 0.07;
        return formatPrice(endpreis + mwst);
    }
    
    private String formatPrice(double value) {
        // Use US locale to ensure dot as decimal separator
        return String.format(java.util.Locale.US, "%.2f", round(value));
    }
    
    private double round(double value) {
        return BigDecimal.valueOf(value)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}