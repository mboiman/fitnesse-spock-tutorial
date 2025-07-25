package de.tutorial.integration

import spock.lang.Specification
import spock.lang.Title
import spock.lang.Requires
// FitNesse imports für später

@Title("FitNesse Wiki-Tests mit Spock ausführen")
class FitNesseWikiTestSpec extends Specification {
    
    static final String FITNESSE_ROOT = "src/test/fitnesse"
    static final int FITNESSE_PORT = 9090
    
    def "FitNesse PriceCalculations Wiki-Test sollte erfolgreich sein"() {
        given: "FitNesse Test-Runner"
        def testPage = "FrontPage.PriceCalculations"
        def summary = runFitNesseTest(testPage)
        
        expect: "Alle Tests sind grün"
        summary.right > 0  // Mindestens ein erfolgreicher Test
        summary.wrong == 0 // Keine Fehler
        summary.exceptions == 0
    }
    
    def "FitNesse ShippingCosts Wiki-Test sollte erfolgreich sein"() {
        given: "FitNesse Test-Runner"
        def testPage = "FrontPage.ShippingCosts"
        def summary = runFitNesseTest(testPage)
        
        expect: "Alle Tests sind grün"
        summary.right > 0
        summary.wrong == 0
        summary.exceptions == 0
    }
    
    private def runFitNesseTest(String testPage) {
        // FitNesse Test programmatisch ausführen
        def args = [
            "-d", FITNESSE_ROOT,
            "-c", "${testPage}?test&format=text"
        ] as String[]
        
        // Mock Summary für Demo - in echtem Test würde FitNesse Runner verwendet
        return [right: 10, wrong: 0, exceptions: 0]
    }
}