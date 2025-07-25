package de.tutorial;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Test Orchestrator - Coordinates FitNesse and Spock tests
 */
public class TestOrchestrator {
    
    private static final String FITNESSE_URL = "http://localhost:9090";
    private static final int FITNESSE_PORT = 9090;
    private Process fitnesseProcess;
    private ExecutorService executor = Executors.newFixedThreadPool(4);
    
    public static void main(String[] args) {
        TestOrchestrator orchestrator = new TestOrchestrator();
        orchestrator.runAllTests();
    }
    
    public void runAllTests() {
        System.out.println("=== Test Orchestrator Started ===");
        
        try {
            // 1. Compile project
            System.out.println("\n1. Compiling project...");
            executeCommand("./gradlew", "clean", "compileJava", "compileTestJava");
            
            // 2. Run Spock tests in parallel
            System.out.println("\n2. Running Spock tests...");
            CompletableFuture<Boolean> spockTests = CompletableFuture.supplyAsync(() -> 
                runSpockTests()
            );
            
            // 3. Start FitNesse server
            System.out.println("\n3. Starting FitNesse server...");
            startFitNesseServer();
            Thread.sleep(5000); // Wait for server startup
            
            // 4. Run FitNesse tests
            System.out.println("\n4. Running FitNesse tests...");
            List<CompletableFuture<TestResult>> fitnesseTests = new ArrayList<>();
            
            fitnesseTests.add(CompletableFuture.supplyAsync(() -> 
                runFitNesseTest("DemoTest", "Demo functionality test")
            ));
            
            fitnesseTests.add(CompletableFuture.supplyAsync(() -> 
                runFitNesseTest("PriceCalculations", "Price calculation test")
            ));
            
            fitnesseTests.add(CompletableFuture.supplyAsync(() -> 
                runFitNesseTest("ShippingCosts", "Shipping cost test")
            ));
            
            // Wait for all tests to complete
            CompletableFuture.allOf(fitnesseTests.toArray(new CompletableFuture[0])).join();
            boolean spockPassed = spockTests.get();
            
            // 5. Generate reports
            System.out.println("\n5. Generating test reports...");
            generateReports();
            
            // 6. Print summary
            printTestSummary(spockPassed, fitnesseTests);
            
        } catch (Exception e) {
            System.err.println("Test orchestration failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            stopFitNesseServer();
            executor.shutdown();
        }
    }
    
    private boolean runSpockTests() {
        try {
            Process process = new ProcessBuilder("./gradlew", "test")
                .redirectErrorStream(true)
                .start();
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("   " + line);
            }
            
            return process.waitFor() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void startFitNesseServer() throws IOException {
        fitnesseProcess = new ProcessBuilder(
            "./gradlew", "fitnesseStart"
        ).start();
    }
    
    private void stopFitNesseServer() {
        if (fitnesseProcess != null) {
            fitnesseProcess.destroyForcibly();
        }
    }
    
    private TestResult runFitNesseTest(String testName, String description) {
        try {
            URL url = new URL(FITNESSE_URL + "/" + testName + "?test&format=xml");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            
            int responseCode = conn.getResponseCode();
            boolean passed = false;
            
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
                );
                String content = in.lines().reduce("", (a, b) -> a + b);
                in.close();
                
                // Simple check for test pass
                passed = !content.contains("class=\"fail\"") && 
                        content.contains("class=\"pass\"");
            }
            
            return new TestResult(testName, description, passed);
            
        } catch (Exception e) {
            return new TestResult(testName, description, false, e.getMessage());
        }
    }
    
    private void generateReports() {
        executeCommand("./gradlew", "jacocoTestReport");
    }
    
    private void executeCommand(String... command) {
        try {
            Process process = new ProcessBuilder(command)
                .redirectErrorStream(true)
                .start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void printTestSummary(boolean spockPassed, List<CompletableFuture<TestResult>> fitnesseTests) {
        System.out.println("\n=== Test Summary ===");
        System.out.println("Spock Tests: " + (spockPassed ? "PASSED ✓" : "FAILED ✗"));
        
        System.out.println("\nFitNesse Tests:");
        fitnesseTests.forEach(future -> {
            try {
                TestResult result = future.get();
                System.out.println("  " + result.name + ": " + 
                    (result.passed ? "PASSED ✓" : "FAILED ✗") +
                    (result.error != null ? " (" + result.error + ")" : ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    static class TestResult {
        String name;
        String description;
        boolean passed;
        String error;
        
        TestResult(String name, String description, boolean passed) {
            this(name, description, passed, null);
        }
        
        TestResult(String name, String description, boolean passed, String error) {
            this.name = name;
            this.description = description;
            this.passed = passed;
            this.error = error;
        }
    }
}