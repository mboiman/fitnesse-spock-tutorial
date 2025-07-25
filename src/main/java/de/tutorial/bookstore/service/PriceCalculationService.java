package de.tutorial.bookstore.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PriceCalculationService {
    
    private static final BigDecimal VAT_RATE = new BigDecimal("0.07"); // 7% MwSt für Bücher
    
    /**
     * Berechnet den Mengenrabatt basierend auf der Anzahl
     */
    public BigDecimal calculateQuantityDiscount(BigDecimal price, int quantity) {
        BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
        BigDecimal discountRate = getDiscountRate(quantity);
        BigDecimal discount = subtotal.multiply(discountRate);
        
        return discount.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Gibt den Rabattsatz basierend auf der Menge zurück
     */
    private BigDecimal getDiscountRate(int quantity) {
        if (quantity >= 10) {
            return new BigDecimal("0.15"); // 15% Rabatt
        } else if (quantity >= 5) {
            return new BigDecimal("0.10"); // 10% Rabatt
        } else if (quantity >= 3) {
            return new BigDecimal("0.05"); // 5% Rabatt
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Berechnet den Endpreis inklusive Rabatt
     */
    public BigDecimal calculateFinalPrice(BigDecimal price, int quantity) {
        BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
        BigDecimal discount = calculateQuantityDiscount(price, quantity);
        
        return subtotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Berechnet die Mehrwertsteuer
     */
    public BigDecimal calculateVAT(BigDecimal netPrice) {
        return netPrice.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Berechnet den Gesamtpreis inklusive MwSt
     */
    public BigDecimal calculateTotalWithVAT(BigDecimal netPrice) {
        BigDecimal vat = calculateVAT(netPrice);
        return netPrice.add(vat).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Berechnet Versandkosten basierend auf Bestellwert
     */
    public BigDecimal calculateShippingCost(BigDecimal orderValue) {
        if (orderValue.compareTo(new BigDecimal("50.00")) >= 0) {
            return BigDecimal.ZERO; // Kostenloser Versand ab 50€
        } else if (orderValue.compareTo(new BigDecimal("20.00")) >= 0) {
            return new BigDecimal("2.95");
        } else {
            return new BigDecimal("4.95");
        }
    }
}