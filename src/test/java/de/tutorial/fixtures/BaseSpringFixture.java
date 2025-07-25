package de.tutorial.fixtures;

import de.tutorial.bookstore.BookstoreApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Basis-Klasse für alle FitNesse Fixtures, die Spring Services nutzen
 * Stellt sicher, dass FitNesse und Spock dieselbe Spring-Konfiguration verwenden
 */
public abstract class BaseSpringFixture {
    
    private static ConfigurableApplicationContext applicationContext;
    
    static {
        // Spring Boot Anwendung einmalig starten
        if (applicationContext == null) {
            System.setProperty("spring.profiles.active", "test");
            applicationContext = SpringApplication.run(BookstoreApplication.class);
        }
    }
    
    /**
     * Gibt eine Spring Bean zurück - genau wie in Spock Tests
     */
    protected <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }
    
    /**
     * Shutdown-Hook für sauberes Beenden
     */
    public static void shutdown() {
        if (applicationContext != null) {
            applicationContext.close();
        }
    }
}