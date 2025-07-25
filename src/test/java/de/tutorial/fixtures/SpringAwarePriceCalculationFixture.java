package de.tutorial.fixtures;

import de.tutorial.bookstore.service.PriceCalculationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.math.BigDecimal;

/**
 * Spring-aware FitNesse Fixture für Slim Test System
 */
public class SpringAwarePriceCalculationFixture {
    
    private static ApplicationContext context;
    private PriceCalculationService priceService;
    
    // Input-Felder
    private BigDecimal buchpreis;
    private int anzahl;
    
    public SpringAwarePriceCalculationFixture() {
        // Spring Context initialisieren (nur einmal)
        if (context == null) {
            context = new AnnotationConfigApplicationContext("de.tutorial.bookstore");
        }
        // Den gleichen Service nutzen wie in Spock Tests
        this.priceService = context.getBean(PriceCalculationService.class);
    }
    
    // Setter für FitNesse
    public void setBuchpreis(double preis) {
        this.buchpreis = new BigDecimal(preis);
    }
    
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }
    
    // Berechnungsmethoden für FitNesse Slim Decision Table
    public String rabattsatz() {
        BigDecimal discount = priceService.calculateQuantityDiscount(buchpreis, anzahl);
        BigDecimal total = buchpreis.multiply(BigDecimal.valueOf(anzahl));
        if (total.compareTo(BigDecimal.ZERO) == 0) return "0.00";
        
        BigDecimal rate = discount.multiply(BigDecimal.valueOf(100)).divide(total, 2, BigDecimal.ROUND_HALF_UP);
        return String.format(java.util.Locale.US, "%.2f", rate.doubleValue());
    }
    
    public String endpreis() {
        BigDecimal finalPrice = priceService.calculateFinalPrice(buchpreis, anzahl);
        return String.format(java.util.Locale.US, "%.2f", finalPrice.doubleValue());
    }
}