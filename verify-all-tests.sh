#!/bin/bash

# FitNesse & Spock Test Verifier
# This script runs all tests and verifies they work correctly

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Set Java environment
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"  
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"

echo "======================================"
echo "   Test Suite Verification Script"
echo "======================================"

# Initialize counters
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Function to check test result
check_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ $2 passed${NC}"
        ((PASSED_TESTS++))
    else
        echo -e "${RED}✗ $2 failed${NC}"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))
}

# 1. Compile the project
echo -e "\n${YELLOW}1. Compiling project...${NC}"
./gradlew clean compileJava compileTestJava
check_result $? "Project compilation"

# 2. Run Spock unit tests
echo -e "\n${YELLOW}2. Running Spock unit tests...${NC}"
./gradlew test --no-daemon
check_result $? "Spock unit tests"

# 3. Generate test report
echo -e "\n${YELLOW}3. Generating test coverage report...${NC}"
./gradlew jacocoTestReport
check_result $? "Test coverage report"

# 4. Start FitNesse server
echo -e "\n${YELLOW}4. Starting FitNesse server...${NC}"
./gradlew fitnesseStart > fitnesse.log 2>&1 &
FITNESSE_PID=$!
sleep 8  # Give server time to start

# Check if server is running
if curl -s http://localhost:9090 > /dev/null; then
    echo -e "${GREEN}✓ FitNesse server started successfully${NC}"
    ((PASSED_TESTS++))
else
    echo -e "${RED}✗ FitNesse server failed to start${NC}"
    ((FAILED_TESTS++))
fi
((TOTAL_TESTS++))

# 5. Run FitNesse tests via command line
echo -e "\n${YELLOW}5. Running FitNesse tests...${NC}"

# Run Demo Test
echo "   Running Demo Test..."
curl -s "http://localhost:9090/DemoTest?test" > demo-test-result.html
if grep -q "class=\"pass\"" demo-test-result.html; then
    check_result 0 "FitNesse Demo Test"
else
    check_result 1 "FitNesse Demo Test"
fi

# Run Price Calculation Test
echo "   Running Price Calculation Test..."
curl -s "http://localhost:9090/PriceCalculations?test" > price-test-result.html
if grep -q "class=\"pass\"" price-test-result.html; then
    check_result 0 "FitNesse Price Calculation Test"
else
    check_result 1 "FitNesse Price Calculation Test"
fi

# 6. Stop FitNesse server
echo -e "\n${YELLOW}6. Stopping FitNesse server...${NC}"
kill $FITNESSE_PID 2>/dev/null
echo -e "${GREEN}✓ Server stopped${NC}"

# 7. Summary
echo -e "\n======================================"
echo -e "         TEST SUMMARY"
echo -e "======================================"
echo -e "Total tests run: ${TOTAL_TESTS}"
echo -e "${GREEN}Passed: ${PASSED_TESTS}${NC}"
echo -e "${RED}Failed: ${FAILED_TESTS}${NC}"

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "\n${GREEN}✓ All tests passed successfully!${NC}"
    exit 0
else
    echo -e "\n${RED}✗ Some tests failed. Please check the logs.${NC}"
    exit 1
fi