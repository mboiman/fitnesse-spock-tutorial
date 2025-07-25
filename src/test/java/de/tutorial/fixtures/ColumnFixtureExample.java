package de.tutorial.fixtures;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Column Fixture Example - Preisberechnungen mit Rabatten
 * Demonstriert Decision Table Pattern für FitNesse
 */
public class ColumnFixtureExample {
    private BigDecimal buchpreis;
    private int anzahl;
    private BigDecimal rabattProzent;

    // Setter für Input-Spalten
    public void setBuchpreis(double buchpreis) {
        this.buchpreis = new BigDecimal(String.valueOf(buchpreis));
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    public void setRabattProzent(double rabattProzent) {
        this.rabattProzent = new BigDecimal(String.valueOf(rabattProzent));
    }

    // Getter für Output-Spalten (berechnet)
    public String zwischensumme() {
        BigDecimal summe = buchpreis.multiply(BigDecimal.valueOf(anzahl));
        return String.format(java.util.Locale.US, "%.2f", summe.setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

    public String rabattbetrag() {
        BigDecimal summe = buchpreis.multiply(BigDecimal.valueOf(anzahl));
        BigDecimal rabatt = summe.multiply(rabattProzent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return String.format(java.util.Locale.US, "%.2f", rabatt.doubleValue());
    }

    public String endsumme() {
        BigDecimal summe = buchpreis.multiply(BigDecimal.valueOf(anzahl));
        BigDecimal rabatt = summe.multiply(rabattProzent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal endpreis = summe.subtract(rabatt);
        return String.format(java.util.Locale.US, "%.2f", endpreis.setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

    public String mehrwertsteuer() {
        BigDecimal summe = buchpreis.multiply(BigDecimal.valueOf(anzahl));
        BigDecimal rabatt = summe.multiply(rabattProzent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal endpreis = summe.subtract(rabatt);
        BigDecimal mwst = endpreis.multiply(new BigDecimal("0.07")).setScale(2, RoundingMode.HALF_UP);
        return String.format(java.util.Locale.US, "%.2f", mwst.doubleValue());
    }

    public String gesamtpreisMitMwst() {
        BigDecimal summe = buchpreis.multiply(BigDecimal.valueOf(anzahl));
        BigDecimal rabatt = summe.multiply(rabattProzent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal endpreis = summe.subtract(rabatt);
        BigDecimal mwst = endpreis.multiply(new BigDecimal("0.07")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal gesamt = endpreis.add(mwst).setScale(2, RoundingMode.HALF_UP);
        return String.format(java.util.Locale.US, "%.2f", gesamt.doubleValue());
    }
}