package de.tutorial.fixtures;

/**
 * Einfache Fixture für FitNesse Slim Demo
 * Unterstützt Decision Table Format
 */
public class SimpleCalculator {
    
    private int a;
    private int b;
    
    // Setter für Decision Table
    public void setA(int a) {
        this.a = a;
    }
    
    public void setB(int b) {
        this.b = b;
    }
    
    // Getter für Ergebnis
    public int addPlusEquals() {
        return a + b;
    }
    
    // Alternative Methode für Script Table
    public int add(int a, int b) {
        return a + b;
    }
}